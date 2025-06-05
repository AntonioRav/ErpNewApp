package com.eval.newApp.service.Employe;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.eval.newApp.service.Login.LoginService;
import com.eval.newApp.model.SalarySlip;
import com.eval.newApp.model.*;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.text.ParseException;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.kernel.pdf.PdfDocument;
import java.io.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Service
public class SalarySlipService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final LoginService loginService;

    public SalarySlipService(LoginService loginService){
        this.loginService = loginService;
    }

    public List<SalarySlip> getSalarySlipByEmployee(String employee){
        String url = "http://127.0.0.1:8000/api/resource/Salary Slip?filters=[[\"employee\", \"=\", \"" + employee + "\"]]";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", loginService.getSessionCookie()); // Utilise le cookie stocké

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        List<SalarySlip> salarySlips = new ArrayList<>();
        if (response.getStatusCode()==HttpStatus.OK) {
            List<Map<String, Object>> data = (List<Map<String, Object>>) response.getBody().get("data");
            for (Map<String, Object> salarySlipData : data) {
                SalarySlip salarySlip = new SalarySlip();
                String name=(String) salarySlipData.get("name");
                salarySlip=getDetailSalarySlip(name);
                salarySlips.add(salarySlip);
            }
        }
        return salarySlips;
    }

    public SalarySlip getDetailSalarySlip(String name){
        String url = "http://127.0.0.1:8000/api/resource/Salary Slip/" + name;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", loginService.getSessionCookie()); // Utilise le cookie stocké

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);


        Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");

        SalarySlip salarySlip = new SalarySlip();
        salarySlip.setName((String) data.get("name"));
        salarySlip.setEmployee_name((String) data.get("employee_name"));
        salarySlip.setEmployee((String) data.get("employee"));
        salarySlip.setStatus((String) data.get("status"));
        salarySlip.setCompany((String) data.get("company"));
        salarySlip.setPosting_date((String) data.get("posting_date"));
        salarySlip.setSalary_structure((String) data.get("salary_structure"));
        salarySlip.setNet_pay((Double) data.get("net_pay"));
        return salarySlip;
        
    }

    public byte[] exporterPdf(String salarySlipId){
        try{
            SalarySlip detailSalarySlip=getDetailSalarySlip(salarySlipId);
            ByteArrayOutputStream b= new ByteArrayOutputStream();            
            PdfWriter writer = new PdfWriter(b);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document=new Document(pdfDoc,PageSize.A4);

            document.add(new Paragraph("FICHE DE PAIE")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(24)
                .setBold());
            
            document.add(new Paragraph("\n"));
            
            document.add(new Paragraph(" Company: "+detailSalarySlip.getCompany())
                .setTextAlignment(TextAlignment.LEFT)
                .setFontSize(18)
                .setBold());

            document.add(new Paragraph("Fiche de paie N° : " + detailSalarySlip.getName())
                .setFontSize(12));
            document.add(new Paragraph("Date : " + detailSalarySlip.getPosting_date())
                .setFontSize(12));    
            document.add(new Paragraph("Employé N°: " + detailSalarySlip.getEmployee())
                .setFontSize(12));        
            document.add(new Paragraph("Nom : " + detailSalarySlip.getEmployee_name())
                .setFontSize(12));   
            document.add(new Paragraph("Structure de salaire : " + detailSalarySlip.getSalary_structure())
                .setFontSize(12));   
            document.add(new Paragraph("Paiment Brut : " + detailSalarySlip.getNet_pay())
                .setFontSize(12));   
                
            document.add(new Paragraph("\n\n"));
            document.add(new Paragraph("Merci pour votre confiance!")
            .setTextAlignment(TextAlignment.CENTER)
            .setFontSize(12)
            .setItalic());

            document.close();
            return b.toByteArray();

        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la generation du pdf : "+e.getMessage());
        }


    }

    public List<Map<String, Object>> getSalaryRegister() {
        String url = "http://127.0.0.1:8000/api/method/frappe.desk.query_report.run";
    
        // Corps de la requête : nom du rapport
        Map<String, Object> body = new HashMap<>();
        body.put("report_name", "Salary Register");
    
        // En-têtes HTTP avec cookie de session
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", loginService.getSessionCookie());
        headers.setContentType(MediaType.APPLICATION_JSON);
    
        // Création de la requête POST
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
    
        // Traitement de la réponse
        Map<String, Object> responseBody = response.getBody();
        Map<String, Object> message = (Map<String, Object>) responseBody.get("message");
    
        // Extraire les noms de colonnes
        List<Map<String, Object>> columns = (List<Map<String, Object>>) message.get("columns");
        List<String> fieldNames = columns.stream()
            .map(col -> (String) col.get("fieldname"))
            .toList();
    
        // Résultats bruts
        List<Object> rawResults = (List<Object>) message.get("result");
        List<Map<String, Object>> result = new ArrayList<>();
    
        // Parcourir chaque ligne de résultat
        for (Object rowObj : rawResults) {
            if (rowObj instanceof Map) {
                // Cas normal : données bien structurées
                result.add((Map<String, Object>) rowObj);
            } else if (rowObj instanceof List) {
                // Cas de la ligne des totaux (tableau brut)
                List<Object> row = (List<Object>) rowObj;
                Map<String, Object> ligne = new LinkedHashMap<>();
                for (int i = 0; i < Math.min(fieldNames.size(), row.size()); i++) {
                    ligne.put(fieldNames.get(i), row.get(i));
                }
                result.add(ligne);
            }
        }

        for (Map<String, Object> row : result) {
            row.remove("currency");
            row.remove("total_loan_repayment");
        }
    
        return result;
    }

    public List<Map<String, Object>> getSalaryRegisterFiltre(int mois, int annee) {
        String url = "http://127.0.0.1:8000/api/method/frappe.desk.query_report.run";
    
        Map<String, Object> body = new HashMap<>();
        body.put("report_name", "Salary Register");
    
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", loginService.getSessionCookie());
        headers.setContentType(MediaType.APPLICATION_JSON);
    
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
    
        Map<String, Object> responseBody = response.getBody();
        Map<String, Object> message = (Map<String, Object>) responseBody.get("message");
    
        List<Map<String, Object>> columns = (List<Map<String, Object>>) message.get("columns");
        List<String> fieldNames = columns.stream()
            .map(col -> (String) col.get("fieldname"))
            .toList();
    
        List<Object> rawResults = (List<Object>) message.get("result");
        List<Map<String, Object>> result = new ArrayList<>();
        Map<String, Object> ligneTotale = null;
    
        for (Object rowObj : rawResults) {
            Map<String, Object> ligne = new LinkedHashMap<>();
    
            if (rowObj instanceof Map) {
                ligne.putAll((Map<String, Object>) rowObj);
            } else if (rowObj instanceof List) {
                List<Object> row = (List<Object>) rowObj;
                for (int i = 0; i < Math.min(fieldNames.size(), row.size()); i++) {
                    ligne.put(fieldNames.get(i), row.get(i));
                }
            }
    
            Object startDateObj = ligne.get("start_date");
    
            if (startDateObj == null || startDateObj.toString().isEmpty()) {
                // C’est probablement la ligne de totaux => on la garde pour la fin
                ligneTotale = ligne;
                continue;
            }
    
            try {
                LocalDate date = LocalDate.parse(startDateObj.toString());
                if (date.getMonthValue() == mois && date.getYear() == annee) {
                    ligne.remove("currency");
                    ligne.remove("total_loan_repayment");
                    result.add(ligne);
                    if (ligneTotale != null) {
                        ligneTotale.remove("currency");
                        ligneTotale.remove("total_loan_repayment");
                        result.add(ligneTotale);
                    }
                }
            } catch (DateTimeParseException e) {
                // Ignorer les lignes mal formatées
            }
        }
    
        // Ajouter la ligne des totaux à la fin si elle existe
    
        return result;
    }
    

}
