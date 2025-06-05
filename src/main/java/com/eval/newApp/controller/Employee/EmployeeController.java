package com.eval.newApp.controller.Employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;    

import com.eval.newApp.service.Employe.EmployeeService;
import com.eval.newApp.model.Employee;
import org.springframework.ui.Model;
import java.util.List;
import org.springframework.stereotype.Controller;

@Controller
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/employees")
    public String getEmployees(Model model) {
        List<Employee> employees = employeeService.getEmployees();
        model.addAttribute("employees", employees);
        return "liste_employees";
    }

    @GetMapping("/rechercherParNom")
    public String getEmployeesFiltre(@RequestParam String nom, Model model) {
        List<Employee> employees = employeeService.rechercherParNom(nom);
        model.addAttribute("employees", employees); 
        return "liste_employees";
    }   

    @GetMapping("/filtrerParGenre")
    public String getEmployeesFiltreParGenre(@RequestParam String genre, Model model) {
        List<Employee> employees = employeeService.filtrerParGenre(genre);
        model.addAttribute("employees", employees); 
        return "liste_employees";
    }

    @GetMapping("/filtrerParStatut")
    public String getEmployeesFiltreParStatut(@RequestParam String statut, Model model) {
        List<Employee> employees = employeeService.filtrerParStatut(statut);
        model.addAttribute("employees", employees); 
        return "liste_employees";
    }

    @GetMapping("/filtrerParDateDebut")
    public String filtreParDateDebut(@RequestParam String date1,@RequestParam String date2, Model model ){
        List<Employee> employees=employeeService.filtreParDateDebut(date1, date2);
        model.addAttribute("employees",employees);
        return "liste_employees";
    }
}
