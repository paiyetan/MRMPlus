/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mrmplus.statistics;

import mrmplus.statistics.estimators.PeptideULOQEstimator;
import mrmplus.statistics.estimators.PeptideLinearityEstimator;
import mrmplus.statistics.estimators.PeptideLLOQEstimator;
import mrmplus.statistics.estimators.PeptideResponseCurveFitter;
import mrmplus.statistics.estimators.PeptideSpecValidator;
import mrmplus.statistics.estimators.PeptideCarryOverEstimator;
import mrmplus.statistics.estimators.PeptideLODEstimator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import mrmplus.PeptideRecord;
import mrmplus.PeptideResult;
import mrmplus.MRMRunMeta;
import ios.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import mrmplus.statistics.resultobjects.*;
import mrmplus.enums.*;


/**
 *
 * @author paiyeta1
 */
public class PeptideQCEstimator {
    
    
    public HashMap<String, LinkedList<PeptideResult>> estimatePeptidesQCs(HashMap<String, LinkedList<PeptideRecord>> pepToRecordsMap, 
                                                                                HashMap<String, String> config) throws FileNotFoundException, IOException{
        HashMap<String, LinkedList<PeptideResult>> peptideQCEstimates = new HashMap<String, LinkedList<PeptideResult>>();
        //instantiate peptide result object for each peptide and insert in peptideQCEstmates map...
        System.out.println(" Instantiating output peptide result object(s)...");
        Set<String> peptideSequences = pepToRecordsMap.keySet();
        for(String peptideSequence : peptideSequences){
            peptideQCEstimates.put(peptideSequence, new LinkedList<PeptideResult>());
        }
        
        
        /*
         * QC values to estimate 
         *  double limitOfDetection;
            double lowerLimitOfQuantification;
            //Linearity linearity;
            double slope;
            double slopeStandardError;
            double slopeStandardErrorPercent;

            double carryOver; //in Percentage
            //private LinkedList<PeptideTransition> transitions; 
            double upperLimitOfQuantification;
         */
        /*
         * [default] QC values in config file
            header=TRUE
            inputFile=./etc/data/inputFile.txt
            metadataFile=./etc/data/metadata.txt
            peptidesMonitored=43
            noOftransitions=3
            preCurveBlanks=3
            totalBlanks=9
            replicates=3
            serialDilutions=7
            computeLOD=TRUE
            computeLLOQ=TRUE
            computeLinearity=FALSE
            computeCarryOver=FALSE
            computePartialValidationOfSpecificity=FALSE
            computeULOQ=TRUE
            fitCurve=TRUE
            outputDirectory=./etc/text
            peptidesResultsOutputted=SUMMED
         * 
         */
        
        
        
        // ************************ //
        // *** Defaults to TRUE ***
        // ************************ //
        //Limit of Detection.
        System.out.println(" Estimating Limit of Detection(s)...");
        if(config.get("computeLOD").equalsIgnoreCase("TRUE")){
            PeptideLODEstimator lODEstimator = new PeptideLODEstimator();
            LinkedList<LimitOfDetection> lods = null;
            // for each of the peptide sequence
            for(String peptideSequence : peptideSequences){
                // get instantiated object [place holder for derived results]
                LinkedList<PeptideResult> peptideResults = peptideQCEstimates.remove(peptideSequence);
                // get associated PeptideRecords...
                LinkedList<PeptideRecord> peptideRecords = pepToRecordsMap.get(peptideSequence);
                // determine peptidesResultsOutputted
                if(config.get("peptidesResultsOutputted").equalsIgnoreCase("SUMMED")){
                    // compute summed LOD...
                    lods = lODEstimator.estimateLOD(peptideRecords, PeptideResultOutputType.SUMMED, config); 
                    //lODEstimator.estimateLOD(peptideResults, peptideRecords, PeptideResultOutputType.SUMMED, config); 
                } else if(config.get("peptidesResultsOutputted").equalsIgnoreCase("TRANSITIONS")){
                    // compute for each transitions
                    lods = lODEstimator.estimateLOD(peptideRecords, PeptideResultOutputType.TRANSITIONS, config);
                    //lODEstimator.estimateLOD(peptideResults, peptideRecords, PeptideResultOutputType.TRANSITIONS, config);
                } else {
                    // compute for both summed and transitions...
                    lods = lODEstimator.estimateLOD(peptideRecords, PeptideResultOutputType.BOTH, config);
                    //lODEstimator.estimateLOD(peptideResults, peptideRecords, PeptideResultOutputType.BOTH, config);
                }
                
                //peptideResult.setLimitsOfDetections(lods);
                if(peptideResults.size()==0){
                    for(int i = 0; i < lods.size(); i++){
                        PeptideResult peptideResult = new PeptideResult(peptideSequence);
                        peptideResult.setTransitionID(lods.get(i).getTransitionID());
                        peptideResult.setLimitOfDetection(lods.get(i));
                        peptideResults.add(peptideResult);
                    }
                }else{
                    if(peptideResults.size()!=lods.size()){
                        try{
                            throw new Exception(); 
                        }catch(Exception ex){
                            ex.printStackTrace();
                            System.out.println("ERROR: mapped peptideResults linked list size do not match computed number of LODs");
                        }
                    }else{
                        //for each lod
                        for(LimitOfDetection lod : lods){
                            //find peptideResult with same transitionID
                            String transitionID = lod.getTransitionID();
                            for(int i = 0; i < peptideResults.size(); i++){
                                if(transitionID.equalsIgnoreCase(peptideResults.get(i).getTransitionID())){
                                    peptideResults.get(i).setLimitOfDetection(lod);
                                }
                            }
                        }
                    }
                }
                //remove and re-insert peptideToPeptideResult mapping... 
                peptideQCEstimates.put(peptideSequence, peptideResults);
            }
        }
        //Lower Limit of Quantitation.
        
        if(config.get("computeLLOQ").equalsIgnoreCase("TRUE")){
            System.out.println(" Estimating Lower Limit of Quantitation(s)...");
            PeptideLLOQEstimator lLOQEstimator = new PeptideLLOQEstimator();
            
            // for each of the peptide sequence
            for(String peptideSequence : peptideSequences){
                // get instantiated PeptideResult object [place holder for derived results]
                LinkedList<PeptideResult> peptideResults = peptideQCEstimates.get(peptideSequence);
                // get associated PeptideRecords...
                LinkedList<PeptideRecord> peptideRecords = pepToRecordsMap.get(peptideSequence);
                
                
                // determine peptidesResultsOutputted
                if(config.get("peptidesResultsOutputted").equalsIgnoreCase("SUMMED")){
                    // compute summed...
                    
                } else if(config.get("peptidesResultsOutputted").equalsIgnoreCase("TRANSITIONS")){
                    // compute for each transitions
                    
                } else { //BOTH
                    // compute for both summed and transitions...
                }
                
                //set LLOQ(s) for peptide...
                
                //remove and re-insert peptideToPeptideResult mapping... 
            }
            
        }
        
        //Fit Curve...
        if(config.get("fitCurve").equalsIgnoreCase("TRUE")){
            System.out.println(" Fitting Curve(s)...");
            PeptideResponseCurveFitter curveFitter = new PeptideResponseCurveFitter();
            
            // for each of the peptide sequence
            for(String peptideSequence : peptideSequences){
                // get instantiated PeptideResult object [place holder for derived results]
                LinkedList<PeptideResult> peptideResults = peptideQCEstimates.get(peptideSequence);
                // get associated PeptideRecords...
                LinkedList<PeptideRecord> peptideRecords = pepToRecordsMap.get(peptideSequence);
                
                
                // determine peptidesResultsOutputted
                if(config.get("peptidesResultsOutputted").equalsIgnoreCase("SUMMED")){
                    // compute summed...
                    
                } else if(config.get("peptidesResultsOutputted").equalsIgnoreCase("TRANSITIONS")){
                    // compute for each transitions
                    
                } else {
                    // compute for both summed and transitions...
                }
                
                //set CurveFit for peptide...
                
                //remove and re-insert peptideToPeptideResult mapping... 
            }
        }
        
        // ************************* //
        // *** Defaults to FALSE ***
        // ************************* //
        //Estimate Upper Limit of Quantitation.
        if(config.get("computeULOQ").equalsIgnoreCase("TRUE")){
            System.out.println(" Estimating Upper Limit of Quantitation...");
            PeptideULOQEstimator uLOQEstimator = new PeptideULOQEstimator();
            
            // for each of the peptide sequence
            for(String peptideSequence : peptideSequences){
                // get instantiated PeptideResult object [place holder for derived results]
                LinkedList<PeptideResult> peptideResults = peptideQCEstimates.get(peptideSequence);
                // get associated PeptideRecords...
                LinkedList<PeptideRecord> peptideRecords = pepToRecordsMap.get(peptideSequence);
                
                
                // determine peptidesResultsOutputted
                if(config.get("peptidesResultsOutputted").equalsIgnoreCase("SUMMED")){
                    // compute summed...
                    
                } else if(config.get("peptidesResultsOutputted").equalsIgnoreCase("TRANSITIONS")){
                    // compute for each transitions
                    
                } else {
                    // compute for both summed and transitions...
                }
                
                //set ULOQ(s) for peptide...
                
                //remove and re-insert peptideToPeptideResult mapping... 
            }
            
        }
        
        //Estimate Linearity.
        if(config.get("computeLinearity").equalsIgnoreCase("TRUE")){
            System.out.println(" Computing Linearity...");
            PeptideLinearityEstimator lEstimator = new PeptideLinearityEstimator();
            
            // for each of the peptide sequence
            for(String peptideSequence : peptideSequences){
                // get instantiated PeptideResult object [place holder for derived results]
                LinkedList<PeptideResult> peptideResults = peptideQCEstimates.get(peptideSequence);
                // get associated PeptideRecords...
                LinkedList<PeptideRecord> peptideRecords = pepToRecordsMap.get(peptideSequence);
                
                
                // determine peptidesResultsOutputted
                if(config.get("peptidesResultsOutputted").equalsIgnoreCase("SUMMED")){
                    // compute summed...
                    
                } else if(config.get("peptidesResultsOutputted").equalsIgnoreCase("TRANSITIONS")){
                    // compute for each transitions
                    
                } else {
                    // compute for both summed and transitions...
                }
                
                //set Linearity(s) for peptide...
                
                //remove and re-insert peptideToPeptideResult mapping... 
            }
        }
        //Estimate Carry-Over.
        if(config.get("computeCarryOver").equalsIgnoreCase("TRUE")){
            System.out.println(" Computing carry-over...");
            PeptideCarryOverEstimator cOEstimator = new PeptideCarryOverEstimator();
            
            // for each of the peptide sequence
            for(String peptideSequence : peptideSequences){
                // get instantiated PeptideResult object [place holder for derived results]
                LinkedList<PeptideResult> peptideResults = peptideQCEstimates.get(peptideSequence);
                // get associated PeptideRecords...
                LinkedList<PeptideRecord> peptideRecords = pepToRecordsMap.get(peptideSequence);
                
                
                // determine peptidesResultsOutputted
                if(config.get("peptidesResultsOutputted").equalsIgnoreCase("SUMMED")){
                    // compute summed...
                    
                } else if(config.get("peptidesResultsOutputted").equalsIgnoreCase("TRANSITIONS")){
                    // compute for each transitions
                    
                } else {
                    // compute for both summed and transitions...
                }
                
                //set CarryOver(s) for peptide...
                
                //remove and re-insert peptideToPeptideResult mapping... 
            }
        }
        //Partially validate Specificity.
        if(config.get("computePartialValidationOfSpecificity").equalsIgnoreCase("TRUE")){
            System.out.println(" Partially validating specificity...");
            PeptideSpecValidator specValidator = new PeptideSpecValidator();
            
            // for each of the peptide sequence
            for(String peptideSequence : peptideSequences){
                // get instantiated PeptideResult object [place holder for derived results]
                LinkedList<PeptideResult> peptideResults = peptideQCEstimates.get(peptideSequence);
                // get associated PeptideRecords...
                LinkedList<PeptideRecord> peptideRecords = pepToRecordsMap.get(peptideSequence);
                
                
                // determine peptidesResultsOutputted
                if(config.get("peptidesResultsOutputted").equalsIgnoreCase("SUMMED")){
                    // compute summed...
                    
                } else if(config.get("peptidesResultsOutputted").equalsIgnoreCase("TRANSITIONS")){
                    // compute for each transitions
                    
                } else {
                    // compute for both summed and transitions...
                }
                
                //set PartialSpecValidationValues(s) for peptide...
                
                //remove and re-insert peptideToPeptideResult mapping... 
            }
        }
        
        return peptideQCEstimates;
    }

    
    
}
