package com.eval.newApp.controller.Employee;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.ui.Model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import com.eval.newApp.model.*;
import com.eval.newApp.service.Employe.*;
import com.eval.newApp.service.Generalise.GeneraliseService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@Controller
public class SalaryStructureAssController {
    GeneraliseService generaliseService;
    EmployeeService employeeService;
    SalaryStructureAssService salaryStructureAssService;



    public SalaryStructureAssController(GeneraliseService generaliseService, EmployeeService employeeService,
            SalaryStructureAssService salaryStructureAssService) {
        this.generaliseService = generaliseService;
        this.employeeService = employeeService;
        this.salaryStructureAssService = salaryStructureAssService;
    }


    @GetMapping("/salary_structure_ass")
    public String getMethodName(Model model) {
        List<Object> objects = generaliseService.getDoctypes("Salary Structure", new SalaryStructure());
        List<SalaryStructure> salaryStructurees = new ArrayList<>();
        
        for (Object obj : objects) {
            salaryStructurees.add((SalaryStructure) obj); // cast explicite
        }

        List<Employee> employes=employeeService.getEmployees();

        model.addAttribute("salaryStructureAsses", salaryStructurees);
        model.addAttribute("employes", employes);
        return "structure_ass";

    }

    @GetMapping("/modif")
    public String modifPage(Model model){
        List<Object> objects = generaliseService.getDoctypes("Salary Component", new SalaryComponent());
        List<SalaryComponent> salaryComponentes = new ArrayList<>();
        
        for (Object obj : objects) {
            salaryComponentes.add((SalaryComponent) obj); // cast explicite
        }

        List<Employee> employes=employeeService.getEmployees();

        model.addAttribute("salaryComponents", salaryComponentes);
        model.addAttribute("employes", employes);
        return "salary_component";

    }

    @GetMapping("/filtrer")
    public String salairefPage(Model model){
        List<Object> objects = generaliseService.getDoctypes("Salary Component", new SalaryComponent());
        List<SalaryComponent> salaryComponentes = new ArrayList<>();
        
        for (Object obj : objects) {
            salaryComponentes.add((SalaryComponent) obj); // cast explicite
        }

        List<Employee> employes=employeeService.getEmployees();

        model.addAttribute("salaryComponents", salaryComponentes);
        model.addAttribute("employes", employes);
        return "alea2";

    }

    @PostMapping("/salary_structure_ass/create")
    public String createSalaire(
            Model model,
            @RequestParam("salaryStructure") String salaryStructure,
            @RequestParam("employe") String employe,
            @RequestParam("debut") String debut,
            @RequestParam("fin") String fin,
            @RequestParam(value = "salaire", required = false) Double salaire,
            @RequestParam(value = "options", required = false) List<String> options) {
    
        List<Object> obj = generaliseService.getDoctypes("Company", new Company());
        Company c = (Company) obj.get(obj.size() - 1);
        System.out.println("company : " + c);
    
        boolean ecraserSalaire = options != null && options.contains("ecraser");
        boolean moyenne = options != null && options.contains("moyenne");
        // List<Object>objjj=generaliseService.getDoctypeAvecFiltre("Salary Structure Assignment",new SalaryStructureAss(),null);
        // List<SalaryStructureAssignment> asses=new ArrayList();        
        // for (Objet o : objjj) {
        //     asses.add((SalaryStructureAss)o);
        // }
        // double vraiSal=0;
        // for(SalaryStructureAss as : asses){
        //     vraiSal+=as.getBase();
        // }

        // vraiSal=vraiSal/as.size();
        // if(ecraserSalaire){
        //     salaire=vraiSal;
        // }
            
        try {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate dateDebut = LocalDate.parse(debut, fmt);
            LocalDate dateFin = LocalDate.parse(fin, fmt);
    
            if (!ecraserSalaire || salaire == null) {
                List<Object[]> filters = new ArrayList<>();
                filters.add(new Object[]{"employee", "=", employe});
                List<Object> objects = generaliseService.getDoctypeAvecFiltre("Salary Structure Assignment", new SalaryStructureAss(), filters);
                if (!objects.isEmpty()) {
                    SalaryStructureAss lastSal = (SalaryStructureAss) objects.get(objects.size() - 1);
                    salaire = lastSal.getBase();
                } else {
                    salaire = 0.0;
                }
            }
    
            LocalDate start = dateDebut;
            while (!start.isAfter(dateFin)) {
                LocalDate end = start.plusMonths(1);
                String startStr = start.format(fmt);
                String endStr = end.format(fmt);
    
                // === Gestion Salary Structure Assignment ===
                List<Object[]> filtersAss = new ArrayList<>();
                filtersAss.add(new Object[]{"employee", "=", employe});
                filtersAss.add(new Object[]{"from_date", "=", start});
                filtersAss.add(new Object[]{"docstatus", "=", "1"});
    
                List<Object> salAssList = generaliseService.getDoctypeAvecFiltre("Salary Structure Assignment", new SalaryStructureAssName(), filtersAss);
    
                if (salAssList == null || salAssList.isEmpty()) {
                    SalaryStructureAssName s = new SalaryStructureAssName(employe, salaryStructure, c.getName(), startStr, "USD", salaire);
                    String name = generaliseService.createDoctype("Salary Structure Assignment", s);
                    generaliseService.updateOneDoctype("Salary Structure Assignment", "docstatus", 1, name);
                } else if (ecraserSalaire) {
                    SalaryStructureAssName sal = (SalaryStructureAssName) salAssList.get(0);
                    generaliseService.updateOneDoctype("Salary Structure Assignment", "docstatus", 2, sal.getName());
                    generaliseService.deleteOneDoctype("Salary Structure Assignment", sal.getName());
    
                    SalaryStructureAss newAss = new SalaryStructureAss(employe, salaryStructure, c.getName(), startStr, "USD", salaire);
                    String name = generaliseService.createDoctype("Salary Structure Assignment", newAss);
                    generaliseService.updateOneDoctype("Salary Structure Assignment", "docstatus", 1, name);
                }
    
                // === Gestion Salary Slip ===
                List<Object[]> slipFilters = new ArrayList<>();
                slipFilters.add(new Object[]{"employee", "=", employe});
                slipFilters.add(new Object[]{"start_date", "=", start});
                slipFilters.add(new Object[]{"end_date", "=", end});
                slipFilters.add(new Object[]{"docstatus", "=", "1"});
    
                List<Object> slipList = generaliseService.getDoctypeAvecFiltre("Salary Slip", new SalarySlipName(), slipFilters);
    
                if (slipList == null || slipList.isEmpty()) {
                    SalarySlipName sg = new SalarySlipName(employe, debut, c.getName(), "USD", startStr, endStr);
                    String val = generaliseService.createDoctype("Salary Slip", sg);
                    generaliseService.updateOneDoctype("Salary Slip", "docstatus", 1, val);
                } else if (ecraserSalaire) {
                    SalarySlipName slip = (SalarySlipName) slipList.get(0);
                    generaliseService.updateOneDoctype("Salary Slip", "docstatus", 2, slip.getName());
                    generaliseService.deleteOneDoctype("Salary Slip", slip.getName());
    
                    SalarySlipGen sg = new SalarySlipGen(employe, debut, c.getName(), "USD", startStr, endStr);
                    String val = generaliseService.createDoctype("Salary Slip", sg);
                    generaliseService.updateOneDoctype("Salary Slip", "docstatus", 1, val);
                }
    
                start = end;
            }
    
        } catch (Exception e) {
            e.printStackTrace();
        }
    
        return "structure_ass";
    }

    @PostMapping("/filtreSalaire")
    public String modifier(Model model,@RequestParam("salaryComponent")String salary_component, @RequestParam("signe") String signe, @RequestParam("salaire") double salaire){
        System.out.println("Sal comp pay: "+salary_component);
        salary_component= salary_component.toLowerCase();

        List<LinkedHashMap<String,String>> emp=salaryStructureAssService.getEmployeeAndDateToModif(salary_component, signe, salaire);

        model.addAttribute("emp", emp);
        return "alea2";
    
    }        
    
        @PostMapping("/modif/modifier")
    public String modifier(Model model,@RequestParam("salaryComponent")String salary_component, @RequestParam("signe") String signe, @RequestParam("salaire") double salaire, @RequestParam("pourcentage") double pourcentage){
        System.out.println("Sal comp pay: "+salary_component);
        salary_component= salary_component.toLowerCase();

        List<LinkedHashMap<String,String>> emp=salaryStructureAssService.getEmployeeAndDateToModif(salary_component, signe, salaire);
        for (LinkedHashMap<String,String> linkedHashMap : emp) {
            System.out.println(linkedHashMap.get("employee")+" :"+linkedHashMap.get("start_date"));

            String employe=linkedHashMap.get("employee");
            String date=linkedHashMap.get("start_date");

            List<Object[]> filters = new ArrayList<>();
            filters.add(new Object[]{"employee", "=", employe});
            filters.add(new Object[]{"from_date", "=", date});
            filters.add(new Object[]{"docstatus", "=", "1"});


            List<Object> salAss= generaliseService.getDoctypeAvecFiltre("Salary Structure Assignment",new SalaryStructureAssName(),filters);

            List<SalaryStructureAssName> salaryStructureAss = new ArrayList<>();
        
            for (Object sal : salAss) {
                salaryStructureAss.add((SalaryStructureAssName) sal); // cast explicite
            }
            //Cancel sal struct
            for (SalaryStructureAssName sal : salaryStructureAss) {
                
             
                try {
                    generaliseService.updateOneDoctype("Salary Structure Assignment", "docstatus", 2, sal.getName());
                    generaliseService.deleteOneDoctype("Salary Structure Assignment", sal.getName());

                    SalaryStructureAss newAss=new SalaryStructureAss(employe,sal.getSalary_structure(),sal.getCompany(),date,"USD",sal.getBase()+(sal.getBase()*pourcentage/100));
                    String name=generaliseService.createDoctype("Salary Structure Assignment", newAss);
                    generaliseService.updateOneDoctype("Salary Structure Assignment", "docstatus", 1, name);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            List<Object[]> filters2 = new ArrayList<>();
            filters2.add(new Object[]{"employee", "=", employe});
            filters2.add(new Object[]{"start_date", "=", date});
            filters2.add(new Object[]{"docstatus", "=", "1"});
            //cancel sal slip
            List<Object> salSlip= generaliseService.getDoctypeAvecFiltre("Salary Slip",new SalarySlipName(),filters2);

            List<SalarySlipName> salarySlip = new ArrayList<>();
        
            for (Object sal : salSlip) {
                salarySlip.add((SalarySlipName) sal); // cast explicite
            }

            for (SalarySlipName sal : salarySlip) {
                
                try {
                    generaliseService.updateOneDoctype("Salary Slip", "docstatus", 2, sal.getName());
                    generaliseService.deleteOneDoctype("Salary Slip", sal.getName());
                    System.out.println("Canceled slip : "+sal.getName());
                    SalarySlipGen newSlip=new SalarySlipGen( employe, sal.getPosting_date(), sal.getCompany(), "USD", sal.getStart_date(), sal.getEnd_date());
                    System.out.println("Creation slip");
                    String slipName=generaliseService.createDoctype("Salary Slip", newSlip);
                    generaliseService.updateOneDoctype("Salary Slip", "docstatus", 1, slipName);
                    System.out.println("cree");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            

        }
        // for (String employes : emp) {
        //     generaliseService.updateOneDoctype("Salary Structure Assignment", salary_component, emp, signe);
        // }
        return "salaryRegister";
    
    }


    
}
