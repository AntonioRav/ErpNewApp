package com.eval.newApp.model;

public class Item {
    String item_code;
    int qty;
    double rate;
    double amount;
    String item_name;

    public Item() {}

    public Item(String item_code, int qty, double rate, double amount) {
        this.item_code = item_code;
        this.qty = qty;
        this.rate = rate;
        this.amount = amount;
    }

    public String getItem_code() {
        return item_code;
    }

    public void setItem_code(String item_code) {        
        this.item_code = item_code;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }
}