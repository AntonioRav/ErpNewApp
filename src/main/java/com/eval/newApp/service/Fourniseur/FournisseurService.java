// src/main/java/com/example/erpnext/service/ErpNextService.java
package com.eval.newApp.service.Fourniseur;

import com.eval.newApp.model.Fournisseur;
import com.eval.newApp.model.FournisseurResponse;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.eval.newApp.service.Login.LoginService;
import java.util.ArrayList;
import java.util.Map;
import com.eval.newApp.model.Devis;
import java.util.List;
import com.eval.newApp.model.Item;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import com.eval.newApp.model.Commande;

/**
 * Service pour la gestion des fournisseurs
 */
@Service
public class FournisseurService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final LoginService loginService;

    public FournisseurService(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * Récupère la liste des fournisseurs
     * @return Liste des fournisseurs
     */
    public List<Fournisseur> getFournisseurs() {
        String url = "http://127.0.0.1:8000/api/resource/Supplier";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", loginService.getSessionCookie()); // Utilise le cookie stocké

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<FournisseurResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                FournisseurResponse.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody().getData();
        } else {
            throw new RuntimeException("Erreur appel fournisseur : " + response.getStatusCode());
        }
    }

    /**
     * Récupère la liste des devis pour un fournisseur donné
     * @param fournisseurNom Nom du fournisseur
     * @return Liste des devis
     */
    public List<Devis> getDevisParFournisseur(String fournisseurNom) {
        String url = "http://127.0.0.1:8000/api/resource/Supplier Quotation"+"?fields=[\"name\",\"title\",\"status\",\"currency\"]" + "&filters=[[\"supplier\",\"=\",\"" + fournisseurNom + "\"]]";
        
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", loginService.getSessionCookie());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
        
        List<Devis> devisList = new ArrayList<>();
        List<Map<String, Object>> data = (List<Map<String, Object>>) response.getBody().get("data");

        for (Map<String, Object> devisData : data) {
            Devis devis = new Devis();
            devis.setName((String) devisData.get("name"));
            devis.setTitle((String) devisData.get("title"));
            devis.setStatus((String) devisData.get("status"));
            devis.setCurrency((String) devisData.get("currency"));
            devisList.add(devis);
        }

        return devisList;
    }

    /**
     * Récupère la liste des items pour un devis donné
     * @param devisNom Nom du devis
     * @return Liste des items
     */
    public List<Item> getItemParDevis(String devisNom) {
        try {
            String url = "http://127.0.0.1:8000/api/resource/Supplier Quotation/" + devisNom;
            
            System.out.println("URL de la requête: " + url);

            HttpHeaders headers = new HttpHeaders();
            headers.set("Cookie", loginService.getSessionCookie());
            headers.set("Accept", "application/json");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            
            // Debug: afficher la réponse complète
            System.out.println("Réponse complète: " + response.getBody());
            
            if (response.getBody() != null) {
                // Debug: afficher toutes les clés disponibles
                System.out.println("Clés disponibles dans la réponse: " + response.getBody().keySet());
                
                Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
                if (data != null && data.get("items") != null) {
                    List<Map<String, Object>> items = (List<Map<String, Object>>) data.get("items");
                    System.out.println("Items trouvés: " + items.size());
                    
                    List<Item> itemList = new ArrayList<>();
                    for (Map<String, Object> itemData : items) {
                        System.out.println("Données de l'item: " + itemData);
                        
                        Item item = new Item();
                        item.setItem_code((String) itemData.get("item_code"));
                        item.setItem_name((String) itemData.get("item_name"));
                        
                        // Conversion sûre des nombres
                        Object qtyObj = itemData.get("qty");
                        if (qtyObj != null) {
                            if (qtyObj instanceof Integer) {
                                item.setQty((Integer) qtyObj);
                            } else if (qtyObj instanceof Double) {
                                item.setQty(((Double) qtyObj).intValue());
                            }
                        }

                        Object rateObj = itemData.get("rate");
                        if (rateObj != null) {
                            if (rateObj instanceof Double) {
                                item.setRate((Double) rateObj);
                            } else if (rateObj instanceof Integer) {
                                item.setRate(((Integer) rateObj).doubleValue());
                            }
                        }

                        Object amountObj = itemData.get("amount");
                        if (amountObj != null) {
                            if (amountObj instanceof Double) {
                                item.setAmount((Double) amountObj);
                            } else if (amountObj instanceof Integer) {
                                item.setAmount(((Integer) amountObj).doubleValue());
                            }
                        }

                        itemList.add(item);
                    }
                    return itemList;
                } else {
                    System.out.println("Pas d'items trouvés dans data");
                }
            } else {
                System.out.println("Réponse body est null");
            }
            return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des items: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void modifierPrixItem(String devisId, String itemCode, double newRate) {
        try {
            String url = "http://127.0.0.1:8000/api/resource/Supplier Quotation/" + devisId;
            
            // Récupérer d'abord le devis actuel
            HttpHeaders headers = new HttpHeaders();
            headers.set("Cookie", loginService.getSessionCookie());
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            HttpEntity<String> getEntity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, getEntity, Map.class);
            
            if (response.getBody() != null && response.getBody().get("data") != null) {
                Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
                List<Map<String, Object>> items = (List<Map<String, Object>>) data.get("items");
                
                // Mettre à jour le prix de l'item spécifique
                for (Map<String, Object> item : items) {
                    if (item.get("item_code").equals(itemCode)) {
                        item.put("rate", newRate);
                        // Recalculer le montant
                        double qty = ((Number) item.get("qty")).doubleValue();
                        item.put("amount", qty * newRate);
                        break;
                    }
                }
                
                // Préparer la requête PUT
                Map<String, Object> updateData = new HashMap<>();
                updateData.put("items", items);
                
                HttpEntity<Map<String, Object>> putEntity = new HttpEntity<>(updateData, headers);
                restTemplate.exchange(url, HttpMethod.PUT, putEntity, Map.class);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la modification du prix: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la modification du prix", e);
        }
    }

    public List<Commande> getCommandesParFournisseur(String fournisseurNom) 
    {

        String url = "http://127.0.0.1:8000/api/resource/Purchase Order?fields=[\"name\",\"supplier\",\"status\",\"transaction_date\"]"+"&filters=[[\"supplier\",\"=\",\""+fournisseurNom+"\"]]";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie",loginService.getSessionCookie());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url,HttpMethod.GET,entity,Map.class);
        
        List<Commande> commandeList = new ArrayList<>();
        List<Map<String, Object>>data= (List<Map<String, Object>>) response.getBody().get("data");
        
        for (Map<String, Object> commandeData : data) {
            Commande commande = new Commande();
            commande.setName((String) commandeData.get("name"));
            commande.setSupplier((String) commandeData.get("supplier"));
            commande.setStatus((String) commandeData.get("status"));
            commande.setTransaction_date((String) commandeData.get("transaction_date"));
            commandeList.add(commande);
        }
        return commandeList;
    }

    public List<Commande> getCommandeByStatus(String status) {
        String url = "http://127.0.0.1:8000/api/resource/Purchase Order?fields=[\"name\",\"supplier\",\"status\",\"transaction_date\"]"+"&filters=[[\"status\",\"=\",\""+status+"\"]]";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie",loginService.getSessionCookie());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url,HttpMethod.GET,entity,Map.class);
        
        List<Commande> commandeList = new ArrayList<>();
        List<Map<String, Object>>data= (List<Map<String, Object>>) response.getBody().get("data");
        
        for (Map<String, Object> commandeData : data) {
            Commande commande = new Commande();
            commande.setName((String) commandeData.get("name"));
            commande.setSupplier((String) commandeData.get("supplier"));
            commande.setStatus((String) commandeData.get("status"));
            commande.setTransaction_date((String) commandeData.get("transaction_date"));
            commandeList.add(commande);
        }
        return commandeList;
    }    
}
