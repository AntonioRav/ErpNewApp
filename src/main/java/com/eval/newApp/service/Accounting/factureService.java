package com.eval.newApp.service.Accounting;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.eval.newApp.service.Login.LoginService;
import java.util.ArrayList;
import java.util.Map;
import com.eval.newApp.model.Devis;
import java.util.List;
import com.eval.newApp.model.Item;
import java.util.Date;
import com.eval.newApp.model.Commande;
import com.eval.newApp.model.Facture;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.eval.newApp.model.Fournisseur;
import com.eval.newApp.model.FournisseurResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.eval.newApp.model.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import java.text.SimpleDateFormat;
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


@Service
public class factureService {

    private final LoginService loginService;
    private final RestTemplate restTemplate = new RestTemplate();

    public factureService(LoginService loginService) {
        this.loginService = loginService;
    }

    public List<Facture> getFactures()
    {
        String url = "http://127.0.0.1:8000/api/resource/Purchase Invoice?fields=[\"name\",\"supplier_name\",\"posting_date\",\"status\",\"outstanding_amount\"]";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie",loginService.getSessionCookie());
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url,HttpMethod.GET,entity,Map.class);

        List<Facture> factureList = new ArrayList<>();
        List<Map<String, Object>>data= (List<Map<String, Object>>) response.getBody().get("data");

        for (Map<String, Object> factureData : data) {
            Facture facture = new Facture();
            facture.setName((String) factureData.get("name"));
            facture.setSupplier_name((String) factureData.get("supplier_name"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                facture.setPosting_date(sdf.parse((String) factureData.get("posting_date")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            facture.setStatus((String) factureData.get("status"));
            facture.setOutstanding_amount((Double) factureData.get("outstanding_amount"));
            factureList.add(facture);
        }
        return factureList;   
    }

    public DetailsFacture getDetailsFacture(String factureNom) {
        try {
            String url = "http://127.0.0.1:8000/api/resource/Purchase Invoice/" + factureNom;
            
            HttpHeaders headers = new HttpHeaders();
            headers.set("Cookie", loginService.getSessionCookie());
            headers.set("Accept", "application/json");
            HttpEntity<String> entity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
            
            if (response.getBody() != null) {
                Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
                if (data != null && data.get("items") != null) {
                    List<Map<String, Object>> itemsData = (List<Map<String, Object>>) data.get("items");
                    
                    // Créer la facture
                    String name = (String) data.get("name");
                    String supplierName = (String) data.get("supplier_name");
                    String postingDateStr = (String) data.get("posting_date");
                    String status = (String) data.get("status");
                    String outstandingAmountStr = data.get("outstanding_amount").toString();
                    
                    // Conversion de la date
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date postingDate = sdf.parse(postingDateStr);
                    
                    // Conversion du montant
                    Double outstandingAmount = Double.parseDouble(outstandingAmountStr);
                    
                    // Création de l'objet Facture avec le constructeur
                    Facture facture = new Facture(name, supplierName, postingDate, status, outstandingAmount);
                    
                    // Créer la liste des items
                    List<Item> itemList = new ArrayList<>();
                    for (Map<String, Object> itemData : itemsData) {
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
                    
                    // Créer et retourner l'objet DetailsFacture
                    return new DetailsFacture(facture, itemList, data.get("grand_total").toString());
                }
            }
            
            return null;
        } catch (Exception e) {
            System.err.println("Erreur lors de la récupération des détails de la facture: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }   

    public boolean payerFacture(String factureNom, Double amount) {
        try {
            // D'abord, obtenons les détails de la facture pour avoir la société
            String factureUrl = "http://127.0.0.1:8000/api/resource/Purchase Invoice/" + factureNom;
            HttpHeaders headers = new HttpHeaders();
            headers.set("Cookie", loginService.getSessionCookie());
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> factureEntity = new HttpEntity<>(headers);
            
            ResponseEntity<Map> factureResponse = restTemplate.exchange(factureUrl, HttpMethod.GET, factureEntity, Map.class);
            Map<String, Object> factureData = (Map<String, Object>) factureResponse.getBody().get("data");
            String company = (String) factureData.get("company");
            
            // Maintenant créons le Payment Entry
            String url = "http://127.0.0.1:8000/api/resource/Payment Entry";
            
            // Create payment entry data
            Map<String, Object> paymentData = new HashMap<>();
            paymentData.put("doctype", "Payment Entry");
            paymentData.put("naming_series", "PE-.YYYY.-");
            paymentData.put("payment_type", "Pay");
            paymentData.put("party_type", "Supplier");
            paymentData.put("party", getSupplierFromFacture(factureNom));
            paymentData.put("posting_date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            paymentData.put("company", company);
            paymentData.put("paid_amount", amount);
            paymentData.put("received_amount", amount);
            
            // Champs obligatoires pour la validation
            paymentData.put("source_exchange_rate", 1.0);
            paymentData.put("target_exchange_rate", 1.0);
            
            // Définir directement le compte de paiement
            paymentData.put("paid_from", "Espèces - TC");
            paymentData.put("paid_from_account_currency", "USD");
            
            List<Map<String, Object>> references = new ArrayList<>();
            Map<String, Object> reference = new HashMap<>();
            reference.put("reference_doctype", "Purchase Invoice");
            reference.put("reference_name", factureNom);
            reference.put("allocated_amount", amount);
            references.add(reference);
            
            paymentData.put("references", references);
            
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(paymentData, headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);
            
            if (response.getStatusCode() == HttpStatus.OK) {
                Map<String, Object> responseData = (Map<String, Object>) response.getBody().get("data");
                String paymentEntryName = (String) responseData.get("name");
                String submitUrl = "http://127.0.0.1:8000/api/resource/Payment Entry/" + paymentEntryName;
                Map<String, Object> submitData = new HashMap<>();
                submitData.put("docstatus", 1); // 1 pour soumis
                HttpEntity<Map<String, Object>> submitEntity = new HttpEntity<>(submitData, headers);
                ResponseEntity<Map> submitResponse = restTemplate.exchange(submitUrl, HttpMethod.PUT, submitEntity, Map.class);
                
                return submitResponse.getStatusCode() == HttpStatus.OK;
            }
            
            return false;
        } catch (Exception e) {
            System.err.println("Erreur lors du paiement de la facture: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    private String getSupplierFromFacture(String factureNom) {
        DetailsFacture details = getDetailsFacture(factureNom);
        return details != null ? details.getFacture().getSupplier_name() : null;
    }    

public byte[] generateFacturePdf(String factureNom) {
    try {
        DetailsFacture details = getDetailsFacture(factureNom);
        if (details == null) {
            throw new RuntimeException("Facture non trouvée");
        }
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdfDoc = new PdfDocument(writer);
        Document document = new Document(pdfDoc, PageSize.A4);

        // En-tête
        document.add(new Paragraph("FACTURE")
            .setTextAlignment(TextAlignment.CENTER)
            .setFontSize(24)
            .setBold());

        document.add(new Paragraph("\n"));

        // Informations de la société
        document.add(new Paragraph("Tox Company")
            .setTextAlignment(TextAlignment.LEFT)
            .setFontSize(14)
            .setBold());

        // Informations de la facture
        document.add(new Paragraph("Facture N° : " + details.getFacture().getName())
            .setFontSize(12));
        document.add(new Paragraph("Date : " + details.getFacture().getPosting_date())
            .setFontSize(12));

        document.add(new Paragraph("\n"));

        // Informations du fournisseur
        document.add(new Paragraph("Fournisseur")
            .setFontSize(14)
            .setBold());
        document.add(new Paragraph(details.getFacture().getSupplier_name())
            .setFontSize(12));

        document.add(new Paragraph("\n"));

        // Tableau des articles
        float[] columnWidths = {200f, 100f, 100f, 100f};
        Table table = new Table(columnWidths);
        table.setWidth(520f);

        // En-têtes du tableau
        table.addCell(new Cell().add(new Paragraph("Description").setBold()));
        table.addCell(new Cell().add(new Paragraph("Quantité").setBold()));
        table.addCell(new Cell().add(new Paragraph("Prix unitaire").setBold()));
        table.addCell(new Cell().add(new Paragraph("Total").setBold()));

        // Lignes du tableau
        if (details.getItems() != null) {
            for (Item item : details.getItems()) {
                table.addCell(new Cell().add(new Paragraph(item.getItem_name())));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(item.getQty()))));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(item.getRate()))));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(item.getAmount()))));
            }
        }

        document.add(table);

        document.add(new Paragraph("\n"));

        // Résumé des montants
        Table summaryTable = new Table(new float[]{350f, 100f});
        summaryTable.setWidth(450f);
        summaryTable.setHorizontalAlignment(HorizontalAlignment.RIGHT);

        summaryTable.addCell(new Cell().add(new Paragraph("Total de la facture :").setBold()));
        summaryTable.addCell(new Cell().add(new Paragraph(String.valueOf(details.getGrand_total()))));

        document.add(summaryTable);

        // Pied de page
        document.add(new Paragraph("\n\n"));
        document.add(new Paragraph("Merci pour votre confiance!")
            .setTextAlignment(TextAlignment.CENTER)
            .setFontSize(12)
            .setItalic());

        document.close();
        return baos.toByteArray();
    } catch (Exception e) {
        e.printStackTrace();
        throw new RuntimeException("Erreur lors de la génération du PDF: " + e.getMessage());
    }
}
}