/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mrmplus;

import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author paiyeta1
 * 
 * Class updates peptide record with metadata information...
 */
public class PeptideRecordsUpdater {

    public void updatePeptideRecords(LinkedList<PeptideRecord> peptideRecords, 
                                        HashMap<String, MRMRunMeta> replicateNameToMetadataMap) {
        //throw new UnsupportedOperationException("Not yet implemented");
        for(PeptideRecord peptideRecord : peptideRecords){
            String replicateName = peptideRecord.getReplicateName();
            MRMRunMeta runMeta = replicateNameToMetadataMap.get(replicateName);
            // retrieve info
            String RunOrder = runMeta.getRunOrder();	
            String AnalyteType = runMeta.getAnalyteType();	
            String Replicate = runMeta.getReplicate();	
            String CalibrationPoint = runMeta.getCalibrationPoint();
            
            //update peptide record
            peptideRecord.setRunOrder(RunOrder);
            peptideRecord.setAnalyteType(AnalyteType);
            peptideRecord.setReplicate(Replicate);
            peptideRecord.setCalibrationPoint(CalibrationPoint);
            
        }
    }

    public void updatePeptideRecordsDilutions(LinkedList<PeptideRecord> peptideRecords, HashMap<String, Double> pointToDilutionMap) {
        //throw new UnsupportedOperationException("Not yet implemented");
        for(PeptideRecord peptideRecord : peptideRecords){
            String calibrationPoint = peptideRecord.getCalibrationPoint();
            if(pointToDilutionMap.containsKey(calibrationPoint)){
                double dilution = pointToDilutionMap.get(calibrationPoint);
                //update peptide records' dilution
                peptideRecord.setDilution(dilution);
            }
            
        }
    }
    
    
    
}
