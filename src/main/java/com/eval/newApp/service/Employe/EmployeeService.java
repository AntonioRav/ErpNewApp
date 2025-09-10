package com.eval.newApp.service.Employe;

import com.eval.newApp.model.Fournisseur;
import com.eval.newApp.model.FournisseurResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.eval.newApp.service.Login.LoginService;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import com.eval.newApp.model.Devis;
import java.util.List;
import com.eval.newApp.model.Item;
import com.eval.newApp.repository.EmployeeRepository;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import com.eval.newApp.model.Commande;
import java.util.Date;
import com.eval.newApp.model.Facture;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import com.eval.newApp.model.Employee;

@Service
public class EmployeeService{
    private final RestTemplate restTemplate = new RestTemplate();
    private final LoginService loginService;
    @Autowired
    EmployeeRepository employeeRepository;

    public EmployeeService(LoginService loginService){
        this.loginService = loginService;
    }

    public List<Employee> getByCompany(String company){
        return employeeRepository.findByCompany(company);
    }

    public List<Employee> getEmployees(){
        String url = "http://127.0.0.1:8000/api/resource/Employee?&limit_page_length=1000";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", loginService.getSessionCookie()); // Utilise le cookie stocké

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            List<Map<String, Object>> data = (List<Map<String, Object>>) response.getBody().get("data");
            List<Employee> employees = new ArrayList<>();
            for (Map<String, Object> employeeData : data) {
                Employee employee = new Employee();
                String name=(String) employeeData.get("name");
                employee=getDetailsEmployee(name);
                employees.add(employee);
            }
            return employees;
        } else {
            throw new RuntimeException("Erreur appel fournisseur : " + response.getStatusCode());
        }
    }

    public Employee getDetailsEmployee(String name){
        String url = "http://127.0.0.1:8000/api/resource/Employee/" + name;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", loginService.getSessionCookie()); // Utilise le cookie stocké

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                Map.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> employeeData = (Map<String, Object>) response.getBody().get("data");
            Employee employee = new Employee();
            employee.setName((String) employeeData.get("name"));
            employee.setEmployee_name((String) employeeData.get("employee_name"));
            employee.setGender((String) employeeData.get("gender"));
            employee.setDate_of_birth((String) employeeData.get("date_of_birth"));
            employee.setDate_of_joining((String) employeeData.get("date_of_joining"));
            employee.setStatus((String) employeeData.get("status"));
            employee.setCompany((String) employeeData.get("company"));

            return employee;
        } else {
            throw new RuntimeException("Erreur appel fournisseur : " + response.getStatusCode());
        }
    }

    public List<Employee> rechercherParNom(String nom) {
        String search = nom.toLowerCase();
        return getEmployees().stream()
            .filter(emp -> emp.getEmployee_name() != null &&
                        emp.getEmployee_name().toLowerCase().contains(search))
            .collect(Collectors.toList());
    }

    public List<Employee> filtrerParGenre(String genre) {
        return getEmployees().stream()
            .filter(emp -> emp.getGender() != null && emp.getGender().equals(genre))
            .collect(Collectors.toList());
    }

    public List<Employee> filtrerParStatut(String statut) {
        return getEmployees().stream()
            .filter(emp -> emp.getStatus() != null && emp.getStatus().equals(statut))
            .collect(Collectors.toList());
    }

    public List<Employee> filtreParDateDebut(String date1, String date2) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate d1 = LocalDate.parse(date1, formatter);
        LocalDate d2 = LocalDate.parse(date2, formatter);
    
        return getEmployees().stream()
            .filter(emp -> {
                try {
                    if (emp.getDate_of_joining() == null || emp.getDate_of_joining().isEmpty()) {
                        return false;
                    }
                    LocalDate dateEmp = LocalDate.parse(emp.getDate_of_joining(), formatter);
                    return (dateEmp.isEqual(d1) || dateEmp.isAfter(d1)) &&
                           (dateEmp.isEqual(d2) || dateEmp.isBefore(d2));
                } catch (Exception e) {
                    return false; // en cas de mauvaise date formatée
                }
            })
            .collect(Collectors.toList());
    }

    
}