package com.int492.cifarclssfr.imageclassfr.model;

/* Unsupported Directly storing as redis key/value form */
public class PredictionModel {
    private int index;
    private String label;
    private double confidentPct;

    public PredictionModel(int index, String label, double confidentPct) {
        this.index = index;
        this.label = label;
        this.confidentPct = confidentPct;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getConfidentPct() {
        return confidentPct;
    }

    public void setConfidentPct(double confidentPct) {
        this.confidentPct = confidentPct;
    }

    @Override
    public String toString() {
        return index + ","  + label + "," + confidentPct;
    }
}
