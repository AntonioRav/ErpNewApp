Pour manipuler les **dates** en Java avec `LocalDate`, tu peux utiliser la classe `java.time.LocalDate`, qui fait partie de l’API `java.time` (disponible depuis Java 8). Voici un guide simple :

---

### ✅ **1. Import**

```java
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
```

---

### ✅ **2. Créer une date**

```java
LocalDate today = LocalDate.now(); // aujourd'hui
LocalDate specificDate = LocalDate.of(2025, 6, 25); // 25 juin 2025
```

---

### ✅ **3. Ajouter ou soustraire des jours/mois/années**

```java
LocalDate tomorrow = today.plusDays(1);
LocalDate lastMonth = today.minusMonths(1);
```

---

### ✅ **4. Comparer des dates**

```java
boolean isBefore = today.isBefore(specificDate);
boolean isAfter = today.isAfter(specificDate);
```

---

### ✅ **5. Différence entre deux dates**

```java
long daysBetween = ChronoUnit.DAYS.between(today, specificDate);
System.out.println("Jours entre les deux : " + daysBetween);
```

---

### ✅ **6. Formater une date en `String`**

```java
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
String formatted = today.format(formatter); // "19/06/2025"
```

---

### ✅ **7. Parser un `String` vers `LocalDate`**

```java
DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
String dateStr = "01/01/2025";
LocalDate parsedDate = LocalDate.parse(dateStr, formatter);
```

---

### Exemple complet :

```java
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateDemo {
    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        LocalDate birthDate = LocalDate.of(2000, 1, 1);

        long age = ChronoUnit.YEARS.between(birthDate, today);

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        System.out.println("Aujourd'hui : " + today.format(fmt));
        System.out.println("Ton âge : " + age + " ans");
    }
}

import java.time.LocalDate;

public class Exemple {
    public static void main(String[] args) {
        LocalDate date = LocalDate.of(2025, 6, 24); // exemple : 24 juin 2025
        LocalDate datePlusUnMois = date.plusMonths(1);

        System.out.println("Date initiale : " + date);
        System.out.println("Date + 1 mois : " + datePlusUnMois);
    }
}
```

---

Souhaite-tu manipuler les dates en relation avec une base de données, ou faire des filtres dans ton app ERPNext ?
