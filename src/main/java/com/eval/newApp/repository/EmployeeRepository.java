package com.eval.newApp.repository;

import com.eval.newApp.model.*;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
    // Tu peux ajouter des méthodes custom ici
    List<Employee> findByCompany(String company); // SELECT * FROM employee WHERE company = ?
}
