package com.eval.newApp.model;
import java.util.Date;

public class Facture {
    String name;
    String supplier_name;
    Date posting_date;
    String status;
    Double outstanding_amount;

    public Facture() {}

    public Facture(String name, String supplier_name, Date posting_date, String status, Double outstanding_amount) {
        this.name = name;
        this.supplier_name = supplier_name;
        this.posting_date = posting_date;
        this.status = status;
        this.outstanding_amount = outstanding_amount;
    }

    public String getName() {
        return name;
    }    

    public String getSupplier_name() {
        return supplier_name;
    }

    public Date getPosting_date() {
        return posting_date;
    }

    public String getStatus() {
        return status;
    }

    public Double getOutstanding_amount() {
        return outstanding_amount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSupplier_name(String supplier_name) {
        this.supplier_name = supplier_name;
    }

    public void setPosting_date(Date posting_date) {
        this.posting_date = posting_date;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setOutstanding_amount(Double outstanding_amount) {
        this.outstanding_amount = outstanding_amount;
    }
}
