package com.eval.newApp.model;

public class Employee{
    String name;
    String employee_name;
    String gender;
    String date_of_birth;
    String date_of_joining;
    String status;

    public Employee() {
    }

    public Employee(String name, String employee_name, String gender, String date_of_birth, String date_of_joining, String status) {
        this.name = name;
        this.employee_name = employee_name;
        this.gender = gender;
        this.date_of_birth = date_of_birth;
        this.date_of_joining = date_of_joining;
        this.status = status;
    }
    
    public Employee(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmployee_name(String employee_name) {
        this.employee_name = employee_name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setDate_of_birth(String date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public void setDate_of_joining(String date_of_joining) {
        this.date_of_joining = date_of_joining;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmployee_name() {
        return employee_name;
    }

    public String getGender() {
        return gender;
    }

    public String getDate_of_birth() {
        return date_of_birth;
    }

    public String getDate_of_joining() {
        return date_of_joining;
    }

    public String getStatus() {
        return status;
    }
}