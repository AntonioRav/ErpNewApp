package com.eval.newApp.model;

import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.*;


@Entity
@Table(name = "tabEmployee")
public class Employee{
    @Id
    String name;

    @Column(name = "employee_name")
    String employee_name;

    @Column(name="gender")
    String gender;

    @Column(name="date_of_birth")
    String date_of_birth;

    @Column(name="date_of_joining")
    String date_of_joining;

    @Column(name="status")
    String status;

    @Column(name="company")
    String company;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

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