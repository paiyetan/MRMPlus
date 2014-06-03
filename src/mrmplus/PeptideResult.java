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
 */
public class PeptideResult {
    
    private String peptideSequence;
    private double limitOfDetection;
    private double lowerLimitOfQuantification;
    //Linearity linearity;
    private double slope;
    private double slopeStandardError;
    private double slopeStandardErrorPercent;
    
    private double carryOver; //in Percentage
    private LinkedList<PeptideTransition> transitions; 
    private double upperLimitOfQuantification;
    
    
    public PeptideResult(String peptideSequence, 
                    LinkedList<PeptideRecord> mappedRecords,
                        HashMap<String, String> config){
        this.peptideSequence = peptideSequence;
        //limitOfDetection
        //lowerLimitOfQuantification
        //slope
        //slopeStandardError
        
    }
    public PeptideResult(String peptideSequence){
        this.peptideSequence = peptideSequence;
    }

    
    public void setCarryOver(double carryOver) {
        this.carryOver = carryOver;
    }

    public void setLimitOfDetection(double limitOfDetection) {
        this.limitOfDetection = limitOfDetection;
    }

    public void setLowerLimitOfQuantification(double lowerLimitOfQuantification) {
        this.lowerLimitOfQuantification = lowerLimitOfQuantification;
    }

    public void setSlope(double slope) {
        this.slope = slope;
    }

    public void setSlopeStandardError(double slopeStandardError) {
        this.slopeStandardError = slopeStandardError;
    }

    public void setSlopeStandardErrorPercent(double slopeStandardErrorPercent) {
        this.slopeStandardErrorPercent = slopeStandardErrorPercent;
    }

    public void setTransitions(LinkedList<PeptideTransition> transitions) {
        this.transitions = transitions;
    }

    public void setUpperLimitOfQuantification(double upperLimitOfQuantification) {
        this.upperLimitOfQuantification = upperLimitOfQuantification;
    }
    
    public double getCarryOver() {
        return carryOver;
    }

    public double getLimitOfDetection() {
        return limitOfDetection;
    }

    public double getLowerLimitOfQuantification() {
        return lowerLimitOfQuantification;
    }

    public String getPeptideSequence() {
        return peptideSequence;
    }

    public double getSlope() {
        return slope;
    }

    public double getSlopeStandardError() {
        return slopeStandardError;
    }

    public double getSlopeStandardErrorPercent() {
        return slopeStandardErrorPercent;
    }

    public LinkedList<PeptideTransition> getTransitions() {
        return transitions;
    }

    public double getUpperLimitOfQuantification() {
        return upperLimitOfQuantification;
    }
    
    
    
    
    
}
