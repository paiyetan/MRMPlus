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

/**
 *
 * @author paiyeta1
 */
public class PeptideQCEstimator {
    
    
    public HashMap<String, PeptideResult> estimatePeptidesQCs(HashMap<String, LinkedList<PeptideRecord>> pepToRecordsMap, 
                                                                                HashMap<String, String> config){
        HashMap<String, PeptideResult> peptideQCEstimates = new HashMap<String, PeptideResult>();
        //instantiate peptide result object for each peptide and insert in peptideQCEstmates map...
        Set<String> peptideSequences = pepToRecordsMap.keySet();
        for(String peptideSequence : peptideSequences){
            peptideQCEstimates.put(peptideSequence, new PeptideResult(peptideSequence));
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
            peptidesMonitored=43
            noOftransitions=3
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
        if(config.get("computeLOD").equalsIgnoreCase("TRUE")){
            PeptideLODEstimator lODEstimator = new PeptideLODEstimator();
            
            // for each of the peptide sequence
            for(String peptideSequence : peptideSequences){
                // get instantiated PeptideResult object [place holder for derived results]
                PeptideResult peptideResult = peptideQCEstimates.get(peptideSequence);
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
                
                //set LOD(s) for peptide...
                
                //remove and re-insert peptideToPeptideResult mapping... 
            }
        }
        //Lower Limit of Quantitation.
        if(config.get("computeLLOQ").equalsIgnoreCase("TRUE")){
            PeptideLLOQEstimator lLOQEstimator = new PeptideLLOQEstimator();
            
            // for each of the peptide sequence
            for(String peptideSequence : peptideSequences){
                // get instantiated PeptideResult object [place holder for derived results]
                PeptideResult peptideResult = peptideQCEstimates.get(peptideSequence);
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
            PeptideResponseCurveFitter curveFitter = new PeptideResponseCurveFitter();
            
            // for each of the peptide sequence
            for(String peptideSequence : peptideSequences){
                // get instantiated PeptideResult object [place holder for derived results]
                PeptideResult peptideResult = peptideQCEstimates.get(peptideSequence);
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
            PeptideULOQEstimator uLOQEstimator = new PeptideULOQEstimator();
            
            // for each of the peptide sequence
            for(String peptideSequence : peptideSequences){
                // get instantiated PeptideResult object [place holder for derived results]
                PeptideResult peptideResult = peptideQCEstimates.get(peptideSequence);
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
            PeptideLinearityEstimator lEstimator = new PeptideLinearityEstimator();
            
            // for each of the peptide sequence
            for(String peptideSequence : peptideSequences){
                // get instantiated PeptideResult object [place holder for derived results]
                PeptideResult peptideResult = peptideQCEstimates.get(peptideSequence);
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
            PeptideCarryOverEstimator cOEstimator = new PeptideCarryOverEstimator();
            
            // for each of the peptide sequence
            for(String peptideSequence : peptideSequences){
                // get instantiated PeptideResult object [place holder for derived results]
                PeptideResult peptideResult = peptideQCEstimates.get(peptideSequence);
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
            PeptideSpecValidator specValidator = new PeptideSpecValidator();
            
            // for each of the peptide sequence
            for(String peptideSequence : peptideSequences){
                // get instantiated PeptideResult object [place holder for derived results]
                PeptideResult peptideResult = peptideQCEstimates.get(peptideSequence);
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
