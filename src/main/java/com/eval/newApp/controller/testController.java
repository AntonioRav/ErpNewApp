package com.eval.newApp.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.eval.newApp.model.*;
import com.eval.newApp.service.Employe.*;
import com.eval.newApp.repository.*;

@Controller
public class testController {
    @Autowired
    EmployeeService employeeService;

    @GetMapping("/getEmp")
    public void getEmpByCompany() 
    {
        System.out.println("--------------emp------------");
        List<Employee> emp=employeeService.getByCompany("Orinasa SA");
        for (Employee e : emp) {
            System.out.println(e.getEmployee_name());
        }
    }
}
