package com.eval.newApp.model;


import java.util.*;

public class SalaryRegister {
    private List<String> fieldNames;
    private List<Map<String, Object>> data;

    // Constructeur
    public SalaryRegister(List<String> fieldNames, List<Map<String, Object>> data) {
        this.fieldNames = fieldNames;
        this.data = data;
    }

    // Getters
    public List<String> getFieldNames() {
        return fieldNames;
    }

    public List<Map<String, Object>> getData() {
        return data;
    }
}

