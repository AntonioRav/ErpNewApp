# Java Maps : Guide Complet

## 1. Qu'est-ce qu'une `Map` en Java ?

Une `Map` est une structure de données qui associe une clé à une valeur. Chaque clé est unique et permet d'accéder rapidement à sa valeur correspondante.

**Interface principale :**

```java
java.util.Map<K, V>
```

## 2. Principales implémentations

### a. `HashMap`

* Pas d'ordre garanti.
* Autorise `null` comme clé ou valeur.
* Performante en accès/ajout.

```java
Map<String, Integer> map = new HashMap<>();
```

### b. `LinkedHashMap`

* Conserve l'ordre d'insertion.

```java
Map<String, Integer> map = new LinkedHashMap<>();
```

### c. `TreeMap`

* Trie les clés selon l'ordre naturel ou un comparateur.

```java
Map<String, Integer> map = new TreeMap<>();
```

## 3. Opérations courantes

### a. Ajouter une entrée

```java
map.put("nom", 25);
```

### b. Récupérer une valeur

```java
int age = map.get("nom");
```

### c. Vérifier une clé ou valeur

```java
map.containsKey("nom");
map.containsValue(25);
```

### d. Supprimer une entrée

```java
map.remove("nom");
```

### e. Itération

```java
for (Map.Entry<String, Integer> entry : map.entrySet()) {
    System.out.println(entry.getKey() + " => " + entry.getValue());
}
```

## 4. Astuces utiles

* Utiliser `getOrDefault` :

```java
int age = map.getOrDefault("prenom", 0);
```

* Mettre à jour une valeur existante :

```java
map.put("nom", map.get("nom") + 1);
```

* Boucle Java 8 avec lambda :

```java
map.forEach((k, v) -> System.out.println(k + ": " + v));
```

## 5. Cas d'utilisation fréquents

* Compter les fréquences d'éléments
* Regrouper des données par clé
* Mémoriser des paires clé-valeur rapidement accessibles

---

Pour approfondir : [Documentation Java SE](https://docs.oracle.com/en/java/javase/)
