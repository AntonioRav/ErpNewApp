package com.eval.newApp.model;

public class SalarySlipName {
    String name;
    String employee;
    String posting_date;
    String company;
    String currency;
    String start_date;
    String end_date;
    
    public SalarySlipName(String name, String employee, String posting_date, String company, String currency,
            String start_date, String end_date) {
        this.name = name;
        this.employee = employee;
        this.posting_date = posting_date;
        this.company = company;
        this.currency = currency;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public SalarySlipName(String employee, String posting_date, String company, String currency, String start_date,
            String end_date) {
        this.employee = employee;
        this.posting_date = posting_date;
        this.company = company;
        this.currency = currency;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public SalarySlipName() {
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getPosting_date() {
        return posting_date;
    }

    public void setPosting_date(String posting_date) {
        this.posting_date = posting_date;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
