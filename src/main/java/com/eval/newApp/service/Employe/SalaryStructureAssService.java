package com.eval.newApp.service.Employe;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import com.eval.newApp.model.*;
import com.eval.newApp.service.Generalise.GeneraliseService;
import com.eval.newApp.service.Login.LoginService;

@Service
public class SalaryStructureAssService {

    private final LoginService loginService=new LoginService();
    private final RestTemplate restTemplate = new RestTemplate();
    private final SalarySlipService salarySlipService;
    private final GeneraliseService generaliseService;


    public SalaryStructureAssService(SalarySlipService salarySlipService,
            GeneraliseService generaliseService) {
        this.salarySlipService = salarySlipService;
        this.generaliseService = generaliseService;
    }


    public List<LinkedHashMap<String,String>> getEmployeeAndDateToModif(String salaryComp,String signe,double valeur)
    {
        salaryComp=generaliseService.formatString(salaryComp);
        List <LinkedHashMap<String,String>> tenaVal=new ArrayList<>();
        List<Map<String,Object>> salaryRegister=salarySlipService.getSalaryRegister();
        int ind=0;
        for (Map<String,Object> map : salaryRegister) {
            if(ind+1!=salaryRegister.size()){

                
                LinkedHashMap<String,String> val=new LinkedHashMap<>();
                Object salaryComponent=map.get(salaryComp);
                double comp=(Double)salaryComponent;
                

                if(signe.equals(">")){
                    if (comp>valeur) {
                        val.put("employee", (String)map.get("employee"));
                        val.put("start_date", (String)map.get("start_date"));
                        tenaVal.add(val);
                        System.out.println("emp: "+map.get("employee"));
                        System.out.println("date: "+map.get("start_date"));
                        System.out.println("commmp :"+comp+"> valeur : "+valeur);
                    }
                }
                else if(signe.equals("<")){
                    if (comp<valeur) {
                        val.put("employee", (String)map.get("employee"));
                        val.put("start_date", (String)map.get("start_date"));            
                        tenaVal.add(val);   
                    }
                }   
                else if(signe.equals("=")){
                    if (comp==valeur) {
                        val.put("employee", (String)map.get("employee"));
                        val.put("start_date", (String)map.get("start_date"));   
                        tenaVal.add(val);
                    }
                }            
            }
            ind++;    
        }
        return tenaVal;
    }

}

