/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mrmplus;

import ios.QCEstimatesPrinter;
import ios.ConfigurationReader;
import ios.*;
import mrmplus.statistics.PeptideQCEstimator;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author paiyeta1
 */
public class MRMPlus {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        System.out.println("Starting...");
        // TODO code application logic here
        /*
         *  
         *  default config...
            header=TRUE
            inputFile=./etc/data/inputFile.txt
            peptidesMonitored=43
            noOftransitions=3
            totalBlanks=9
            replicates=3
            serialDilutions=7
            computeLOD=TRUE
            computeLLOQ=TRUE
            fitCurve=TRUE
            computeLinearity=FALSE
            computeCarryOver=FALSE
            computePartialValidationOfSpecificity=FALSE
            computeULOQ=FALSE
            outputDirectory=./etc/text
            peptidesResultsOutputted=SUMMED
         * 
         */
        
        LinkedList<PeptideRecord> peptideRecords;
        HashMap<String, LinkedList<PeptideRecord>> pepToRecordsMap;
        
        //read configuration(s)...
        ConfigurationReader configReader = new ConfigurationReader();
        HashMap<String, String> config = configReader.readConfig("./MRMPlus.config");
        
        //read inputFile...
        InputFileReader inFileReader = new InputFileReader();
        String inputFile = config.get("inputFile"); //can be alternative derived 
        peptideRecords = inFileReader.readInputFile(inputFile, config);
        
        //read metadata file...
        
        
        //associate metadata-info with peptide records
        // Get associated information - metadata...
        String metadataFile = config.get("metadataFile");
        MetadataFileReader metadataFileReader = new MetadataFileReader();
        LinkedList<MRMRunMeta> metadata = metadataFileReader.readFile(metadataFile);
        
        // map replicate name to meta-info; the replicate name is used in this case because it is unique and could
        // be used to associate metadata to peptide record as we'll use subsequently...
        ExperimentMetadataMapper metadatamapper = new ExperimentMetadataMapper();
        HashMap<String, MRMRunMeta> replicateNameToMetadataMap = 
                metadatamapper.mapReplicateNameToMetadata(metadata);
        
        // update peptideRecords with metadata info...
        PeptideRecordsUpdater updater = new PeptideRecordsUpdater();
        updater.updatePeptideRecords(peptideRecords, replicateNameToMetadataMap);
        
        // get associated [spikedIn] serial dilution concentration...
        String dilutionFile = config.get("dilutionFile");
        DilutionFileReader dilFileReader = new DilutionFileReader();
        HashMap<String, Double> pointToDilutionMap = dilFileReader.readFile(dilutionFile);
        
        // update respective peptideRecord with associated point dilution
        updater.updatePeptideRecordsDilutions(peptideRecords, pointToDilutionMap);
        
        
        //map peptides to records...
        PeptideToRecordsMapper mapper = new PeptideToRecordsMapper();
        pepToRecordsMap = mapper.mapPeptideToRecord(peptideRecords);
        
        //compute statistics results...
        PeptideQCEstimator qcEstimator = new PeptideQCEstimator();
        HashMap<String, PeptideResult> peptideQCEstimates = 
                qcEstimator.estimatePeptidesQCs(pepToRecordsMap, config);
        
        //Print results
        QCEstimatesPrinter printer = new QCEstimatesPrinter();
        printer.printMRMPlusEstimates(peptideQCEstimates, config);
        
        
        
    }
    
    
    
}
