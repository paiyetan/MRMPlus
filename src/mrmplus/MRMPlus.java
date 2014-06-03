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
            inputFile="./etc/data/inputFile.txt
            header=TRUE
            peptidesMonitored=43
            noOftransitions=3
            totalBlanks=TRUE
            replicates=3
            serialDilutions=7
            computeLOD=TRUE
            computeLLOQ=TRUE
            computeLinearity=TRUE
            computeCarryOver=TRUE
            computePartialValidationOfSpecificity=TRUE
            computeULOQ=TRUE
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
