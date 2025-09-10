package com.eval.newApp.model;

public class EmpDate {
    String employee;
    String start_date;
    
    public EmpDate(String employee, String start_date) {
        this.employee = employee;
        this.start_date = start_date;
    }

    public EmpDate() {
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }
}
