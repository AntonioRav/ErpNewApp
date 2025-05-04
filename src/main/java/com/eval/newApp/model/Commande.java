package com.eval.newApp.model;

import java.util.List;

public class Commande {
    String name;
    String supplier;
    String status;
    String transaction_date;

    public Commande() {}

    public Commande(String name, String supplier, String status, String transaction_date) {
        this.name = name;
        this.supplier = supplier;
        this.status = status;
        this.transaction_date = transaction_date;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSupplier() {
        return supplier;
    }
    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getTransaction_date() {
        return transaction_date;
    }
    public void setTransaction_date(String transaction_date) {
        this.transaction_date = transaction_date;
    }
    

    
}    