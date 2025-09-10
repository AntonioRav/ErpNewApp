package com.eval.newApp.service.Generalise;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.eval.newApp.service.Login.LoginService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Service
public class  GeneraliseService{

    String ERP_BASE_URL = "http://127.0.0.1:8000/api/resource/";
    LoginService loginService;
    RestTemplate restTemplate = new RestTemplate();
    public GeneraliseService(LoginService loginService) {
        this.loginService = loginService;
    }

    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", loginService.getSessionCookie());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return headers;
    }
    
    public String createDoctype(String doctype, Object object) throws Exception {
        String url = ERP_BASE_URL + doctype;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", loginService.getSessionCookie());
        headers.setContentType(MediaType.APPLICATION_JSON);

        Class<?> clazz = object.getClass();

        Map<String, Object> data = new HashMap<>();
        data.put("doctype",doctype );

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true); // permet d'accéder aux champs privés
            Object valeur = field.get(object); // récupère la valeur du champ
            System.out.println(field.getName() + " = " + valeur);
            data.put(field.getName(), valeur);
        }

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(data, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> body = response.getBody();
            Map<String, Object> returnedData = (Map<String, Object>) body.get("data");
            return returnedData.get("name").toString(); // ✅ retourne le name
        }
    
        return null;
    }
    public List<Object> getDoctypes(String doctype, Object objet) {
        try {
            // Étape 1 : tsy mila encodage zanyy
            String urlBase = ERP_BASE_URL + doctype;
            System.out.println("🔹 Doctype encodé: " + doctype);
    
            // Étape 2 : Construction des champs
            Class<?> clazz = objet.getClass();
            StringBuilder fieldsParam = new StringBuilder("[");
            Field[] fields = clazz.getDeclaredFields();
            System.out.println("🔹 Champs détectés dans la classe " + clazz.getSimpleName() + " :");
            for (Field f : fields) {
                System.out.println("   - " + f.getName());
            }
    
            for (int i = 0; i < fields.length; i++) {
                fieldsParam.append("\"").append(fields[i].getName()).append("\"");
                if (i < fields.length - 1) {
                    fieldsParam.append(",");
                }
            }
            fieldsParam.append("]");
    
            String url = urlBase + "?fields=" + fieldsParam.toString();

            System.out.println("🔹 URL finale utilisée : " + url);
    
            // Étape 3 : Préparation des headers
            HttpHeaders headers = new HttpHeaders();
            headers.set("Cookie", loginService.getSessionCookie());
            headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
            System.out.println("🔹 Headers envoyés : " + headers);
    
            // Étape 4 : Appel HTTP
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);
    
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("✅ Requête réussie, données reçues.");
            } else {
                System.out.println("❌ Requête échouée avec code : " + response.getStatusCode());
            }
    
            Object dataObj = response.getBody().get("data");
            if (!(dataObj instanceof List)) {
                System.out.println("❗ Format inattendu de 'data' : " + dataObj);
                return Collections.emptyList();
            }
    
            List<Map<String, Object>> datas = (List<Map<String, Object>>) dataObj;
    
            // Étape 5 : Création des objets dynamiques
            List<Object> objects = new ArrayList<>();
            for (Map<String, Object> data : datas) {
                Object newInstance = clazz.getDeclaredConstructor().newInstance();
    
                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    String attribut = entry.getKey();
                    Object valeur = entry.getValue();
    
                    String setterName = "set" + Character.toUpperCase(attribut.charAt(0)) + attribut.substring(1);
    
                    boolean setterFound = false;
                    for (Method method : clazz.getMethods()) {
                        if (method.getName().equals(setterName) && method.getParameterCount() == 1) {
                            try {
                                method.invoke(newInstance, valeur);
                                setterFound = true;
                            } catch (IllegalArgumentException e) {
                                Object casted = casterValeur(valeur, method.getParameterTypes()[0]);
                                if (casted != null) {
                                    method.invoke(newInstance, casted);
                                    setterFound = true;
                                } else {
                                    System.out.println("⚠️ Type incompatible pour " + attribut + ": " + valeur + " attendu=" + method.getParameterTypes()[0]);
                                }
                            }
                            break;
                        }
                    }
    
                    if (!setterFound) {
                        System.out.println("⚠️ Aucun setter trouvé pour : " + attribut);
                    }
                }
    
                objects.add(newInstance);
            }
    
            System.out.println("✅ " + objects.size() + " objets construits avec succès.");
            return objects;
    
        } catch (Exception e) {
            System.out.println("❌ ERREUR GLOBALE attrapée :");
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    


public List<Object> getDoctypeAvecFiltre(String doctype, Object objet, List<Object[]> advancedFilters) {
    try {
        // Étape 1 : Construction de l'URL sans encodage
        StringBuilder url = new StringBuilder(ERP_BASE_URL + doctype);
        System.out.println("🔹 Doctype utilisé : " + doctype);

        // Étape 2 : Construction des champs
        Class<?> clazz = objet.getClass();
        StringBuilder fieldsParam = new StringBuilder("[");
        Field[] fields = clazz.getDeclaredFields();
        System.out.println("🔹 Champs détectés dans la classe " + clazz.getSimpleName() + " :");
        for (int i = 0; i < fields.length; i++) {
            String fieldName = fields[i].getName();
            fieldsParam.append("\"").append(fieldName).append("\"");
            if (i < fields.length - 1) {
                fieldsParam.append(",");
            }
            System.out.println("   - " + fieldName);
        }
        fieldsParam.append("]");

        url.append("?fields=").append(fieldsParam);

        // Étape 3 : Ajout des filtres si fournis
        if (advancedFilters != null && !advancedFilters.isEmpty()) {
            StringBuilder filtersJson = new StringBuilder("[");
            for (int i = 0; i < advancedFilters.size(); i++) {
                Object[] condition = advancedFilters.get(i);
                filtersJson.append("[\"")
                        .append(condition[0]).append("\",\"")
                        .append(condition[1]).append("\",");

                Object value = condition[2];
                if (value instanceof Number || value instanceof Boolean) {
                    filtersJson.append(value);
                } else if (value instanceof Collection) {
                    filtersJson.append("[");
                    Iterator<?> it = ((Collection<?>) value).iterator();
                    while (it.hasNext()) {
                        Object v = it.next();
                        filtersJson.append("\"").append(v).append("\"");
                        if (it.hasNext()) filtersJson.append(",");
                    }
                    filtersJson.append("]");
                } else {
                    filtersJson.append("\"").append(value).append("\"");
                }
                filtersJson.append("]");
                if (i < advancedFilters.size() - 1) {
                    filtersJson.append(",");
                }
            }
            filtersJson.append("]");
            url.append("&filters=").append(filtersJson);
        }

        System.out.println("🔹 URL finale utilisée : " + url);

        // Étape 4 : Préparation des headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", loginService.getSessionCookie());
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        System.out.println("🔹 Headers envoyés : " + headers);

        // Étape 5 : Appel HTTP
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url.toString(), HttpMethod.GET, entity, Map.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            System.out.println("✅ Requête réussie, données reçues.");
        } else {
            System.out.println("❌ Requête échouée avec code : " + response.getStatusCode());
        }

        Object dataObj = response.getBody().get("data");
        if (!(dataObj instanceof List)) {
            System.out.println("❗ Format inattendu de 'data' : " + dataObj);
            return Collections.emptyList();
        }

        List<Map<String, Object>> datas = (List<Map<String, Object>>) dataObj;

        // Étape 6 : Création des objets dynamiques
        List<Object> objects = new ArrayList<>();
        for (Map<String, Object> data : datas) {
            Object newInstance = clazz.getDeclaredConstructor().newInstance();

            for (Map.Entry<String, Object> entry : data.entrySet()) {
                String attribut = entry.getKey();
                Object valeur = entry.getValue();
                String setterName = "set" + Character.toUpperCase(attribut.charAt(0)) + attribut.substring(1);
                boolean setterFound = false;

                for (Method method : clazz.getMethods()) {
                    if (method.getName().equals(setterName) && method.getParameterCount() == 1) {
                        try {
                            method.invoke(newInstance, valeur);
                            setterFound = true;
                        } catch (IllegalArgumentException e) {
                            Object casted = casterValeur(valeur, method.getParameterTypes()[0]);
                            if (casted != null) {
                                method.invoke(newInstance, casted);
                                setterFound = true;
                            } else {
                                System.out.println("⚠️ Type incompatible pour " + attribut + ": " + valeur + " attendu=" + method.getParameterTypes()[0]);
                            }
                        }
                        break;
                    }
                }

                if (!setterFound) {
                    System.out.println("⚠️ Aucun setter trouvé pour : " + attribut);
                }
            }

            objects.add(newInstance);
        }

        System.out.println("✅ " + objects.size() + " objets construits avec succès.");
        return objects;

    } catch (Exception e) {
        System.out.println("❌ ERREUR GLOBALE attrapée :");
        e.printStackTrace();
        return Collections.emptyList();
    }
}



    private Object casterValeur(Object valeur, Class<?> targetType) {
        if (valeur == null) return null;
        try {
            if (targetType == Integer.class || targetType == int.class) return ((Number) valeur).intValue();
            if (targetType == Long.class || targetType == long.class) return ((Number) valeur).longValue();
            if (targetType == Double.class || targetType == double.class) return ((Number) valeur).doubleValue();
            if (targetType == Float.class || targetType == float.class) return ((Number) valeur).floatValue();
            if (targetType == String.class) return valeur.toString();
            if (targetType == Boolean.class || targetType == boolean.class) return Boolean.valueOf(valeur.toString());
        } catch (Exception ex) {
            return null;
        }
        return null;
    }


    public boolean updateDoctype(String doctype, Object object,String name) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", loginService.getSessionCookie());
        headers.setContentType(MediaType.APPLICATION_JSON);
    
        Class<?> clazz = object.getClass();
    
        Map<String, Object> data = new HashMap<>();
    
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Object valeur = field.get(object);
            data.put(field.getName(), valeur); // mettre à jour ce champ
            
        }
    
        if (name == null) {
            throw new IllegalArgumentException("Champ 'name' obligatoire pour faire une mise à jour.");
        }
    
        String url = ERP_BASE_URL + doctype + "/" + name;
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(data, headers);
    
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Map.class);
    
        return response.getStatusCode().is2xxSuccessful();
    }
    
    public boolean updateOneDoctype(String doctype, String champ, Object valeur, String name) throws Exception {
        String url = ERP_BASE_URL + doctype + "/" + name;
    
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", loginService.getSessionCookie());
        headers.setContentType(MediaType.APPLICATION_JSON);
    
        // Corps de la requête : un seul champ à mettre à jour
        Map<String, Object> data = new HashMap<>();
        data.put(champ, valeur);
    
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(data, headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Map.class);
    
        return response.getStatusCode().is2xxSuccessful();
    }

    public boolean deleteOneDoctype(String doctype, String name) throws Exception {
        // Exemple : http://127.0.0.1:8000/api/resource/Salary Slip/Sal Slip/HR-EMP-00002/00002
        String url = ERP_BASE_URL + doctype + "/" + name;
    
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cookie", loginService.getSessionCookie());
    
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, Map.class);
    
        return response.getStatusCode().is2xxSuccessful();
    }
    
    public String formatString(String pay) {
        if (pay == null) return null; // pour éviter NullPointerException
        pay = pay.toLowerCase();             // mettre en minuscules
        pay = pay.replace(" ", "_");         // remplacer les espaces par des underscores
        return pay;
    }
    

}    
