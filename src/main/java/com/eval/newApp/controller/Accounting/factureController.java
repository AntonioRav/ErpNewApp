package com.eval.newApp.service.Accounting;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import com.eval.newApp.service.Accounting.factureService;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import com.eval.newApp.model.Facture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import com.eval.newApp.model.*;
import com.eval.newApp.service.Accounting.factureService;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.text.SimpleDateFormat;


@Controller
public class factureController {

    private final factureService factureService;

    public factureController(factureService factureService) {
        this.factureService = factureService;
    }

    
    @GetMapping("/factures")
    public String listFactures(Model model) {
        List<Facture> factures = factureService.getFactures();
        model.addAttribute("factures",factures);

        return "liste_factures";
    }

    @GetMapping("/factures/{factureNom}/detailsFacture")
    public String voirDetailsFacture(@PathVariable String factureNom, Model model) {
        DetailsFacture detailsFacture = factureService.getDetailsFacture(factureNom);
        if (detailsFacture != null) {
            model.addAttribute("detailsFactures", detailsFacture);
            return "details_facture";
        } else {
            // Gérer l'erreur ici
            return "redirect:/error";
        }
    }

    @PostMapping("/factures/{factureNom}/payer")
    public ResponseEntity<?> payerFacture(@PathVariable String factureNom, @RequestParam Double amount) {
        boolean success = factureService.payerFacture(factureNom, amount);
        if (success) {
            return ResponseEntity.ok().body(Map.of("message", "Paiement effectué avec succès"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("message", "Erreur lors du paiement"));
        }
    }

@GetMapping("/factures/{factureNom}/exporterPdf")
public ResponseEntity<byte[]> exporterPdf(@PathVariable String factureNom) {
    try {
        byte[] pdfBytes = factureService.generateFacturePdf(factureNom);
        if (pdfBytes == null || pdfBytes.length == 0) {
            return ResponseEntity.badRequest().build();
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("attachment")
            .filename(factureNom + ".pdf")
            .build());
        
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.internalServerError().build();
    }
}


}