/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mrmplus.statistics.estimators;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import mrmplus.MRMRunMeta;
import mrmplus.PeptideRecord;
import mrmplus.enums.PeptideResultOutputType;
import mrmplus.statistics.resultobjects.LimitOfDetection;
import mrmplus.*;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

/**
 *
 * @author paiyeta1
 */
public class PeptideLODEstimator {

    public LinkedList<LimitOfDetection> estimateLOD(
                                                    //LinkedList<PeptideResult> peptideResults,
                                                    LinkedList<PeptideRecord> peptideRecords, 
                                                    PeptideResultOutputType peptideResultOutputType,
                                                    HashMap<String, String> config) {
        //throw new UnsupportedOperationException("Not yet implemented");
        LinkedList<LimitOfDetection> lods = new LinkedList<LimitOfDetection>();
        switch (peptideResultOutputType){
            
            case TRANSITIONS:
                //estimate LOD per transition...
                PeptideTransitionToRecordsMapper transToRecordsMapper = new PeptideTransitionToRecordsMapper();
                HashMap<String, LinkedList<PeptideRecord>> transitionToRecords = 
                        transToRecordsMapper.mapTransitionsToRecords(peptideRecords);
                Set<String> transitions = transitionToRecords.keySet();
                for(String transition : transitions){
                    //for each transition,
                    LinkedList<PeptideRecord> transitionRecords = transitionToRecords.get(transition);
                    //getBlanks before the first curve
                    int preCurveBlanks = Integer.parseInt(config.get("preCurveBlanks"));
                    LinkedList<PeptideRecord> preFirstCurveBlanks = getPreFirstCurveBlanks(transitionRecords, preCurveBlanks);
                    LimitOfDetection transitionLOD;
                    transitionLOD = computeLOD(preFirstCurveBlanks);
                    // update attributes
                    transitionLOD.setUsedMinSpikedInConcentration(false);
                    transitionLOD.setTransitionID(transition);
                    
                    // however, if no detectable signal is observed in blanks... 
                    if(transitionLOD.getLimitOfDetection() <= 0){
                        //use standard deviation of signal observed/detected in lowest spiked sample 
                        LinkedList<PeptideRecord> lowestSpikedSampleRecords = getLowestSpikedSampleRecords(transitionRecords);
                        transitionLOD = computeLOD(lowestSpikedSampleRecords);
                        transitionLOD.setUsedMinSpikedInConcentration(true);
                        transitionLOD.setTransitionID(transition);
                    }
                    
                    /*
                     * 
                     *
                     * 
                    
                    //map calibration points to records (which are repectively from the [three] replicates)
                    ExperimentCalibrationPointToRecordsMapper pointToRecordsMapper = new ExperimentCalibrationPointToRecordsMapper();
                    HashMap<String,LinkedList<PeptideRecord>> caliPointToRecords = 
                            pointToRecordsMapper.mapCalibrationPointsToRecords(transitionRecords);
                    
                    //map replicates (curve) to serial dilution records
                    ExperimentReplicateToRecordsMapper expReplicateToRecords = new ExperimentReplicateToRecordsMapper();
                    HashMap<String,LinkedList<PeptideRecord>> replicateToRecords = 
                            expReplicateToRecords.mapReplicatesToRecords(transitionRecords);

                    * 
                    */
                    lods.add(transitionLOD);
                }
                break;
            
            case SUMMED:
                // uses all records mapped to peptide irrespective of  
                //getBlanks before the first curve
                int preCurveBlanks = Integer.parseInt(config.get("preCurveBlanks"));
                LinkedList<PeptideRecord> preFirstCurveBlanks = getPreFirstCurveBlanks(peptideRecords, preCurveBlanks);
                LimitOfDetection summedLOD;
                summedLOD = computeLOD(preFirstCurveBlanks);
                // update attributes
                summedLOD.setUsedMinSpikedInConcentration(false);
                summedLOD.setTransitionID("SUMMED");

                // however, if no detectable signal is observed in blanks... 
                if(summedLOD.getLimitOfDetection() <= 0){
                    //use standard deviation of signal observed/detected in lowest spiked sample 
                    LinkedList<PeptideRecord> lowestSpikedSampleRecords = getLowestSpikedSampleRecords(peptideRecords);
                    summedLOD = computeLOD(lowestSpikedSampleRecords);
                    summedLOD.setUsedMinSpikedInConcentration(true);
                    summedLOD.setTransitionID("SUMMED");
                }
                
                lods.add(summedLOD);

                break;
            
            default: // BOTH
                // estimate LOD per transition...
                LinkedList<LimitOfDetection> transitionsLODs = 
                        estimateLOD(peptideRecords, PeptideResultOutputType.TRANSITIONS, config);
                // add estimated transition-lods to lods
                for(LimitOfDetection transitionLOD : transitionsLODs){
                    lods.add(transitionLOD);
                }                
                // estimate summed lod...
                LinkedList<LimitOfDetection> summedLODs = 
                        estimateLOD(peptideRecords, PeptideResultOutputType.SUMMED, config);
                // add summed-lod to lods 
                for(LimitOfDetection sumLOD : summedLODs){
                    lods.add(sumLOD);
                }
            
        }
                       
        return lods;
    }

    private LinkedList<PeptideRecord> getPreFirstCurveBlanks(LinkedList<PeptideRecord> transitionRecords, int preCurveBlanks) {
        //throw new UnsupportedOperationException("Not yet implemented");
        LinkedList<PeptideRecord> preCurves = new LinkedList<PeptideRecord>();
        for(int runOrder = 1; runOrder <= preCurveBlanks; runOrder++){
            for(PeptideRecord record : transitionRecords){
                if(Integer.parseInt(record.getRunOrder()) == runOrder){
                    preCurves.add(record);
                }
            }
        }      
        return preCurves;
    }

    private LimitOfDetection computeLOD(LinkedList<PeptideRecord> preFirstCurveBlanks) {
        //throw new UnsupportedOperationException("Not yet implemented");
        LimitOfDetection lod = null;
        //determined from blanks injected before first curve using average plus 3x standard deviation of blank signal
        double[] values = new double[preFirstCurveBlanks.size()];
        for(int i = 0; i < values.length; i++){
            values[i] = preFirstCurveBlanks.get(i).getMeasuredConcentration();
        }
        
        boolean hasZeroValue = hasZeroValue(values);       
        
        //evaluate mean
        Mean mean = new Mean();
        double average = mean.evaluate(values);
        //evaluate sd
        StandardDeviation sd = new StandardDeviation();
        double sdvalue = sd.evaluate(values);
        
        //lod
        double lodValue = (average + (sdvalue * 3));
        lod = new LimitOfDetection(average,sdvalue,lodValue, hasZeroValue);
        return lod;
    }

    private LinkedList<PeptideRecord> getLowestSpikedSampleRecords(LinkedList<PeptideRecord> transitionRecords) {
        LinkedList<PeptideRecord> lowestSpikedSampleRecords = new LinkedList<PeptideRecord>();
        //get the lowest spikedIn Value
        for(PeptideRecord record: transitionRecords){
            if(record.getCalibrationPoint().equalsIgnoreCase("point_1")){
                lowestSpikedSampleRecords.add(record);
            }
        }
        return lowestSpikedSampleRecords;
    }

    private boolean hasZeroValue(double[] values) {
        //throw new UnsupportedOperationException("Not yet implemented");
        boolean hasZero = false;
        for(double value : values){
            if(value == 0){
                hasZero = true;
                break;
            }
        }
        
        return hasZero;
    }

    
    
}
