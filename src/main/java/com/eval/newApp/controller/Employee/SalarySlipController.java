package com.eval.newApp.controller.Employee;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.ui.Model;
import java.util.List;
import java.util.*;

import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import com.eval.newApp.model.*;
import com.eval.newApp.service.Employe.SalarySlipService;

@Controller
public class SalarySlipController {
    SalarySlipService salarySlipService;

    public SalarySlipController(SalarySlipService salarySlipService) {
        this.salarySlipService = salarySlipService;
    }

    @GetMapping("/salarySlipByEmployee/{employee}")
    public String getSalarySlipByEmployee(@PathVariable String employee, Model model) {
        List<SalarySlip> salarySlips = salarySlipService.getSalarySlipByEmployee(employee);
        model.addAttribute("salarySlips", salarySlips);
        return "salarySlip";
    }

    @GetMapping("/exporterPdf")
    public ResponseEntity<byte[]> exporterPdf(@RequestParam("salarySlip") String salarySlip) {
        try {
            byte[] pdfBytes = salarySlipService.exporterPdf(salarySlip);
            if (pdfBytes == null || pdfBytes.length == 0) {
                return ResponseEntity.badRequest().build();
            }
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(salarySlip + ".pdf")
                .build());
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/salaryRegister")
    public String getSalaryRegister(Model model) {
        List<Map<String, Object>> result = salarySlipService.getSalaryRegister();
        model.addAttribute("salaryRegister", result);
        return "salaryRegister";
    }
    
    @GetMapping("/salaryRegisterFiltre")
    public String getSalaryRegister(@RequestParam("mois") String mois,@RequestParam("annee") String annee, Model model) {
        List<Map<String, Object>> result = salarySlipService.getSalaryRegisterFiltre(Integer.parseInt(mois) ,Integer.parseInt(annee) );
        model.addAttribute("salaryRegister", result);
        return "salaryRegister";
    }


}    