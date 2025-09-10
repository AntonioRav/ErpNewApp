Voici un document synthétique et clair qui regroupe les **fonctions utiles de JPA et Spring Data Repository**, avec des exemples concrets :

---

# 📘 **Fonctions utiles avec JPA & Spring Data JPA**

## 🔧 1. **Fonctions standards dans les Repository**

### `findById(ID id)`

* Recherche par identifiant

```java
Optional<Employee> emp = employeeRepository.findById(1L);
```

### `findAll()`

* Retourne tous les enregistrements

```java
List<Employee> list = employeeRepository.findAll();
```

### `save(entity)`

* Enregistre ou met à jour un enregistrement

```java
employeeRepository.save(new Employee("Jean", "IT"));
```

### `deleteById(ID id)`

* Supprime par identifiant

```java
employeeRepository.deleteById(1L);
```

### `delete(entity)`

* Supprime un objet

```java
employeeRepository.delete(employee);
```

### `count()`

* Retourne le nombre d’enregistrements

```java
long total = employeeRepository.count();
```

---

## 🔍 2. **Fonctions de requêtes dérivées (Derived Queries)**

Spring Data JPA permet de **créer des requêtes automatiquement** à partir du nom de la méthode.

### `findByNom(String nom)`

```java
List<Employee> list = employeeRepository.findByNom("Jean");
```

### `findByAgeGreaterThan(int age)`

```java
List<Employee> list = employeeRepository.findByAgeGreaterThan(25);
```

### `findByNomContaining(String keyword)`

* Recherche partielle (LIKE %keyword%)

```java
List<Employee> list = employeeRepository.findByNomContaining("Jean");
```

### `findByDateBetween(Date debut, Date fin)`

```java
List<Employee> list = employeeRepository.findByDateBetween(d1, d2);
```

### `findByNomAndPrenom(String nom, String prenom)`

```java
Optional<Employee> e = employeeRepository.findByNomAndPrenom("Jean", "Paul");
```

---

## 🛠️ 3. **Requêtes personnalisées avec `@Query`**

### JPQL :

```java
@Query("SELECT e FROM Employee e WHERE e.nom = :nom")
List<Employee> chercherParNom(@Param("nom") String nom);
```

### SQL natif :

```java
@Query(value = "SELECT * FROM tab_employee WHERE nom = :nom", nativeQuery = true)
List<Employee> chercherParNomSQL(@Param("nom") String nom);
```

---

## ⏳ 4. **Pagination et tri**

### Avec Pageable :

```java
Page<Employee> page = employeeRepository.findAll(PageRequest.of(0, 10));
```

### Avec tri :

```java
List<Employee> list = employeeRepository.findAll(Sort.by("nom").ascending());
```

---

## 🔄 5. **Relation entre entités (OneToMany, ManyToOne, etc.)**

### Exemple : un employé appartient à une entreprise

```java
@Entity
public class Employee {
    @ManyToOne
    private Company company;
}
```

### Cascade & Fetch :

```java
@OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
private List<Employee> employees;
```

---

## 🧪 6. **EntityManager (si besoin avancé)**

```java
@PersistenceContext
EntityManager em;

// Requête manuelle
List<Employee> result = em.createQuery("SELECT e FROM Employee e").getResultList();
```

---

## ✅ 7. **Validation automatique avec `@Valid` + contraintes**

```java
@NotNull
@Size(min = 2)
private String nom;
```

Dans le contrôleur :

```java
public ResponseEntity<?> ajouter(@Valid @RequestBody Employee e)
```

---

## 📎 Exemple de `JpaRepository`

```java
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByNom(String nom);
    
    @Query("SELECT e FROM Employee e WHERE e.nom LIKE %:motCle%")
    List<Employee> recherche(@Param("motCle") String motCle);
}
```

---

Souhaitez-vous que je vous fasse un **PDF téléchargeable** ou une **version imprimable** de ce document ?
