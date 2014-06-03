/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mrmplus.statistics;

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
         *  header=TRUE
            inputFile=./etc/data/inputFile.txt
            peptidesMonitored=43
            noOftransitions=3
            totalBlanks=9
            replicates=3
            serialDilutions=7
            computeLOD=TRUE
            computeLLOQ=TRUE
            computeLinearity=TRUE
            computeCarryOver=TRUE
            computePartialValidationOfSpecificity=TRUE
            computeULOQ=TRUE
         */
        
        //Limit of Detection.
        if(config.get("computeLOD").equalsIgnoreCase("TRUE")){
            PeptideLODEstimator lODEstimator = new PeptideLODEstimator();
        }
        //Lower Limit of Quantitation.
        if(config.get("computeLLOQ").equalsIgnoreCase("TRUE")){
            PeptideLLOQEstimator lLOQEstimator = new PeptideLLOQEstimator();
        }
        //Estimate Linearity.
        if(config.get("computeLinearity").equalsIgnoreCase("TRUE")){
            PeptideLinearityEstimator lEstimator = new PeptideLinearityEstimator();
        }
        //Estimate Carry-Over.
        if(config.get("computeCarryOver").equalsIgnoreCase("TRUE")){
            PeptideCarryOverEstimator cOEstimator = new PeptideCarryOverEstimator();
        }
        //Partially validate Specificity.
        if(config.get("computePartialValidationOfSpecificity").equalsIgnoreCase("TRUE")){
            PeptideSpecValidator specValidator = new PeptideSpecValidator();
        }
        //Estimate Upper Limit of Quantitation.
        if(config.get("computeULOQ").equalsIgnoreCase("TRUE")){
            PeptideULOQEstimator uLOQEstimator = new PeptideULOQEstimator();
        }
         
        return peptideQCEstimates;
    }
    
    
    
    
    
}
