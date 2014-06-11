/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ios;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import mrmplus.PeptideResult;
import mrmplus.statistics.resultobjects.LimitOfDetection;

/**
 *
 * @author paiyeta1
 */
public class QCEstimatesPrinter {

    public void printMRMPlusEstimates(HashMap<String, PeptideResult> peptideQCEstimates, 
            HashMap<String, String> config) throws FileNotFoundException {
        //throw new UnsupportedOperationException("Not yet implemented");
        String inputFile = config.get("inputFile");
        String outputDir = config.get("outputDirectory");
        String outputFileName = new File(inputFile).getName().replace(".txt", "") + ".mrmplus";
        String outputFile = outputDir + File.separator + outputFileName;
        
        PrintWriter printer = new PrintWriter(outputFile);
        printHeader(printer, config); //printHeader
        //print body
        Set<String> peptideSequences = peptideQCEstimates.keySet();
        for(String peptideSequence : peptideSequences){
            PeptideResult peptideResult = peptideQCEstimates.get(peptideSequence);
            printPeptideRowResult(peptideResult, printer, config);
        }
        
               
        
        
        
        printer.close();
    }

    private void printHeader(PrintWriter printer, HashMap<String, String> config) {
        // based off configuration file options...
        printer.print("Peptide" + "\t" + "Transition");
        // ************************ //
        // *** Defaults to TRUE ***
        // ************************ //
        //Limit of Detection.
        if(config.get("computeLOD").equalsIgnoreCase("TRUE")){
            // locally defined values...
            printer.print("\t" + "average");
            printer.print("\t" + "standardDeviation");
            printer.print("\t" + "limitOfDetectionValue");
            printer.print("\t" + "usedMinSpikedInConcentration");
            printer.print("\t" + "zeroFlagged");
        }
        //Lower Limit of Quantitation.
        if(config.get("computeLLOQ").equalsIgnoreCase("TRUE")){
            throw new UnsupportedOperationException("Not yet implemented");
        }
        
        //Fit Curve...
        if(config.get("fitCurve").equalsIgnoreCase("TRUE")){
            throw new UnsupportedOperationException("Not yet implemented");
        }
        
        // ************************* //
        // *** Defaults to FALSE ***
        // ************************* //
        //Estimate Upper Limit of Quantitation.
        if(config.get("computeULOQ").equalsIgnoreCase("TRUE")){
             throw new UnsupportedOperationException("Not yet implemented");           
        }
        
        //Estimate Linearity.
        if(config.get("computeLinearity").equalsIgnoreCase("TRUE")){
            throw new UnsupportedOperationException("Not yet implemented");
        }
        //Estimate Carry-Over.
        if(config.get("computeCarryOver").equalsIgnoreCase("TRUE")){
            throw new UnsupportedOperationException("Not yet implemented");
        }
        //Partially validate Specificity.
        if(config.get("computePartialValidationOfSpecificity").equalsIgnoreCase("TRUE")){
            throw new UnsupportedOperationException("Not yet implemented");
        }
        
        printer.print("\n");
    }

    private void printPeptideRowResult(PeptideResult peptideResult, PrintWriter printer, HashMap<String, String> config) {
        
        // ************************ //
        // *** Defaults to TRUE ***
        // ************************ //
        //Limit of Detection.
        if(config.get("computeLOD").equalsIgnoreCase("TRUE")){
            
            LinkedList<LimitOfDetection> lods = peptideResult.getLimitsOfDetections();
            
            for(LimitOfDetection lod : lods){
                // based off configuration file options...
                printer.print(peptideResult.getPeptideSequence());// + "\t" + "Transition");
        
                
                // locally defined values...
                printer.print("\t" + lod.getTransitionID()); // "transition type"
                printer.print("\t" + lod.getAverage()); // "average");
                printer.print("\t" + lod.getStandardDeviation()); //"standardDeviation");
                printer.print("\t" + lod.getLimitOfDetection()); //"limitOfDetectionValue");
                printer.print("\t" + lod.usedMinSpikedInConcentration()); //"usedMinSpikedInConcentration");
                printer.print("\t" + lod.isZeroValueFlagged()); //"zeroFlagged");
            }
        }
        //Lower Limit of Quantitation.
        if(config.get("computeLLOQ").equalsIgnoreCase("TRUE")){
            throw new UnsupportedOperationException("Not yet implemented");
        }
        
        //Fit Curve...
        if(config.get("fitCurve").equalsIgnoreCase("TRUE")){
            throw new UnsupportedOperationException("Not yet implemented");
        }
        
        // ************************* //
        // *** Defaults to FALSE ***
        // ************************* //
        //Estimate Upper Limit of Quantitation.
        if(config.get("computeULOQ").equalsIgnoreCase("TRUE")){
             throw new UnsupportedOperationException("Not yet implemented");           
        }
        
        //Estimate Linearity.
        if(config.get("computeLinearity").equalsIgnoreCase("TRUE")){
            throw new UnsupportedOperationException("Not yet implemented");
        }
        //Estimate Carry-Over.
        if(config.get("computeCarryOver").equalsIgnoreCase("TRUE")){
            throw new UnsupportedOperationException("Not yet implemented");
        }
        //Partially validate Specificity.
        if(config.get("computePartialValidationOfSpecificity").equalsIgnoreCase("TRUE")){
            throw new UnsupportedOperationException("Not yet implemented");
        }
        
        printer.print("\n");
        
        
        
    }
    
}
