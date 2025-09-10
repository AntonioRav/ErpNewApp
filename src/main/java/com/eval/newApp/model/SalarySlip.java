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
    Double gross_pay;
    Double total_deduction;
    String start_date;
    String end_date;

    public SalarySlip(String name, String employee_name, String employee, String status, String company,
            String posting_date, String salary_structure, Double net_pay, Double gross_pay, Double total_deduction,
            String start_date, String end_date) {
        this.name = name;
        this.employee_name = employee_name;
        this.employee = employee;
        this.status = status;
        this.company = company;
        this.posting_date = posting_date;
        this.salary_structure = salary_structure;
        this.net_pay = net_pay;
        this.gross_pay = gross_pay;
        this.total_deduction = total_deduction;
        this.start_date = start_date;
        this.end_date = end_date;
    }

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

    public Double getGross_pay() {
        return gross_pay;
    }

    public void setGross_pay(Double gross_pay) {
        this.gross_pay = gross_pay;
    }

    public Double getTotal_deduction() {
        return total_deduction;
    }

    public void setTotal_deduction(Double total_deduction) {
        this.total_deduction = total_deduction;
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
}
