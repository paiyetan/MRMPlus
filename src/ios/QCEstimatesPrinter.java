/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ios;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.HashMap;
import mrmplus.PeptideResult;

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
        // Header (attributes) to print
        
        
        
        
        printer.close();
    }
    
}
