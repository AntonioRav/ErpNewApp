package com.eval.newApp.model;

public class SalarySlip {
    String name;
    String employee_name;
    String employee;
    String status;
    String company;
    String posting_date;
    String salary_structure;
    Double net_pay;

    public SalarySlip(){

    }

    public SalarySlip(String name, String employee_name, String employee, String status, String company, String posting_date, String salary_structure, Double net_pay){
        this.name = name;
        this.employee_name = employee_name;
        this.employee = employee;
        this.status = status;
        this.company = company;
        this.posting_date = posting_date;
        this.salary_structure = salary_structure;
        this.net_pay = net_pay;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getPosting_date() {
        return posting_date;
    }

    public void setPosting_date(String posting_date) {
        this.posting_date = posting_date;
    }

    public String getSalary_structure() {
        return salary_structure;
    }

    public void setSalary_structure(String salary_structure) {
        this.salary_structure = salary_structure;
    }

    public Double getNet_pay() {
        return net_pay;
    }

    public void setNet_pay(Double net_pay) {
        this.net_pay = net_pay;
    }
}
