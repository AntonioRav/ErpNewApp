package com.eval.newApp.model;

public class TotauxMensuels {
    private double totalGross = 0;
    private double totalDeduction = 0;
    private double totalNet = 0;
    private String moisNum;
    private String anneeNum;
    
    public void ajouter(double gross, double deduction, double net) {
        this.totalGross += gross;
        this.totalDeduction += deduction;
        this.totalNet += net;
    }

    public String getAnneeNum() {
        return anneeNum;
    }
    public void setAnneeNum(String anneeNum) {
        this.anneeNum = anneeNum;
    }
    public String getMoisNum() {
        return moisNum;
    }

    public void setMoisNum(String moisNum) {
        this.moisNum = moisNum;
    }


    public double getTotalGross() {
        return totalGross;
    }

    public double getTotalDeduction() {
        return totalDeduction;
    }

    public double getTotalNet() {
        return totalNet;
    }

    public void setTotalGross(double totalGross) {
        this.totalGross = totalGross;
    }

    public void setTotalDeduction(double totalDeduction) {
        this.totalDeduction = totalDeduction;
    }

    public void setTotalNet(double totalNet) {
        this.totalNet = totalNet;
    }
}
