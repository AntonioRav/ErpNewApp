package com.eval.newApp.model;

public class SalaryStructureAss {
    String employee;
    String salary_structure;
    String company;
    String from_date;
    String currency;
    Double base;


    public SalaryStructureAss(String employee, String salary_structure, String company, String from_date,
            String currency, Double base) {
        this.employee = employee;
        this.salary_structure = salary_structure;
        this.company = company;
        this.from_date = from_date;
        this.currency = currency;
        this.base = base;
    }


    public SalaryStructureAss(){}

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getSalary_structure() {
        return salary_structure;
    }

    public void setSalary_structure(String salary_structure) {
        this.salary_structure = salary_structure;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getBase() {
        return base;
    }

    public void setBase(Double base) {
        this.base = base;
    }

}