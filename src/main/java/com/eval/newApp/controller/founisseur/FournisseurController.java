package com.eval.newApp.service.Fourniseur;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.*;
import com.eval.newApp.service.Login.LoginService;
import com.eval.newApp.model.Fournisseur;
import com.eval.newApp.model.FournisseurResponse;
import org.springframework.stereotype.Controller;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.eval.newApp.service.Fourniseur.FournisseurService;
import org.springframework.ui.Model;
import com.eval.newApp.model.Devis;
import com.eval.newApp.model.Item;
import java.util.Map;
import java.util.HashMap;
import com.eval.newApp.model.Commande;

@Controller
public class FournisseurController {
    private final FournisseurService fournisseurService;

    public FournisseurController(FournisseurService fournisseurService) {
        this.fournisseurService = fournisseurService;
    }

    @GetMapping("/fournisseurs")
    public String getFournisseurs(Model model) {
        List<Fournisseur> fournisseurs = fournisseurService.getFournisseurs();
        model.addAttribute("fournisseurs", fournisseurs);
        return "accueil";
    }

    @GetMapping("/fournisseurs/{nom}/devis")
    public String voirDevisParFournisseur(@PathVariable String nom, Model model) {
        List<Devis> devis = fournisseurService.getDevisParFournisseur(nom);
        Map<String, List<Item>> itemsParDevis = new HashMap<>();
        
        // Pour chaque devis, récupérer ses items
        for (Devis d : devis) {
            List<Item> items = fournisseurService.getItemParDevis(d.getName());
            itemsParDevis.put(d.getName(), items);
            System.out.println("Items pour le devis " + d.getName() + " : " + items);
        }
        
        model.addAttribute("devis", devis);
        model.addAttribute("itemsParDevis", itemsParDevis);
        model.addAttribute("nomFournisseur", nom);
        return "liste_devis";
    }

    @PostMapping("/fournisseurs/devis/{devisId}/items/{itemCode}/updatePrice")
    @ResponseBody
    public ResponseEntity<String> updateItemPrice(
            @PathVariable String devisId,
            @PathVariable String itemCode,
            @RequestParam Double newPrice) {
        try {
            fournisseurService.modifierPrixItem(devisId, itemCode, newPrice);
            return ResponseEntity.ok("Prix mis à jour avec succès");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la mise à jour du prix: " + e.getMessage());
        }
    }

    @GetMapping("/fournisseurs/{nom}/commandes")
    public String voirCommandesParFournisseur(@PathVariable String nom, Model model) {
        List<Commande> commandes = fournisseurService.getCommandesParFournisseur(nom);
        model.addAttribute("commandes", commandes);
        model.addAttribute("nomFournisseur", nom);
        return "liste_commandes";
    }

    @PostMapping("/filtre")
    public String voirCommandesParStatus(@RequestParam String status, Model model) {
        List<Commande> commandes = fournisseurService.getCommandeByStatus(status);
        model.addAttribute("commandes", commandes);
        model.addAttribute("status", status);
        return "liste_commandes";
    }
}
