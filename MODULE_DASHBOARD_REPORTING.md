
### Tests d'Intégration
- `DashboardControllerIntegrationTest` : Tests des endpoints REST
  - Test de récupération du dashboard complet
  - Test de récupération des ventes par produit
  - Test de récupération des produits en baisse
  - Test de récupération des heures de pointe
  - Test de récupération des marges par catégorie
  - Test de récupération de l'évolution d'un produit
  - Test de récupération des statistiques de fréquentation

## 🚀 Exécution des Tests

```bash
# Tests unitaires du module Dashboard
cd backend
./mvnw test -Dtest=DashboardServiceTest

# Tests d'intégration
./mvnw test -Dtest=DashboardControllerIntegrationTest

# Tous les tests
./mvnw test
```

## 📊 Modèle de Données

### VenteProduit
```java
@Entity
@Table(name = "ventes_produit")
public class VenteProduit {
    private Long id;
    private Produit produit;
    private Integer quantiteVendue;
    private BigDecimal montantVente;
    private BigDecimal coutAchat;
    private BigDecimal margeBeneficiaire;
    private LocalDateTime dateVente;
    private Integer heureVente;      // 0-23
    private String jourSemaine;      // MONDAY, TUESDAY, etc.
}
```

### StatistiqueFrequentation
```java
@Entity
@Table(name = "statistiques_frequentation")
public class StatistiqueFrequentation {
    private Long id;
    private LocalDate dateStat;
    private LocalTime heureDebut;
    private LocalTime heureFin;
    private Integer nombreClients;
    private Integer nombreTransactions;
    private String jourSemaine;
    private Boolean estHeurePointe;
}
```

## 🔄 Calculs Automatiques

### Evolution Pourcentage
```
evolution = ((valeur_actuelle - valeur_precedente) / valeur_precedente) * 100
```

### Panier Moyen
```
panier_moyen = chiffre_affaires / nombre_ventes
```

### Pourcentage de Marge
```
pourcentage_marge = (marge_beneficiaire / chiffre_affaires) * 100
```

### Intensité (Heure de Pointe)
```
intensite = (nombre_clients_plage / max_clients_toutes_plages) * 100
```

## ✅ Fonctionnalités Implémentées

- ✅ Dashboard global avec tous les indicateurs
- ✅ Analyse des ventes par produit et catégorie
- ✅ Détection automatique des produits en baisse
- ✅ Identification des heures de pointe
- ✅ Calcul des marges bénéficiaires par catégorie
- ✅ Evolution temporelle des ventes
- ✅ Statistiques de fréquentation
- ✅ Alertes de stock intégrées
- ✅ Période configurable (jour, semaine, mois, année)
- ✅ Tests unitaires et d'intégration complets

## 📝 Notes Techniques

- Les données de ventes sont calculées en temps réel à partir des transactions
- Les statistiques de fréquentation doivent être alimentées par le système de caisse
- Les heures de pointe sont identifiées automatiquement en fonction du seuil de clients
- Le système compare automatiquement avec la période précédente pour calculer l'évolution
- Les marges sont calculées automatiquement lors de l'enregistrement d'une vente

## 🎯 Utilisation Pratique

1. **Consultation Quotidienne** : Le manager consulte le dashboard chaque matin pour voir les ventes de la veille
2. **Analyse Hebdomadaire** : Analyse des tendances de la semaine et ajustement des stocks
3. **Revue Mensuelle** : Analyse approfondie des performances et planification du mois suivant
4. **Alertes en Temps Réel** : Le système envoie des alertes pour les produits en baisse ou les ruptures de stock

## 🔮 Évolutions Futures

- Export des rapports en PDF/Excel
- Graphiques et visualisations avancées
- Prévisions de ventes basées sur l'historique
- Comparaison multi-périodes
- Alertes personnalisables par email
- Dashboard en temps réel avec WebSocket
# Module Tableau de Bord et Reporting

## 📊 Description

Le module **Tableau de Bord et Reporting** permet au manager du magasin d'accéder à des indicateurs de performance clés et d'analyser l'activité du supermarché. Il offre une vue d'ensemble des ventes, des stocks, des marges bénéficiaires et de la fréquentation.

## 🎯 Fonctionnalités

### 1. Dashboard Global
- Vue d'ensemble des performances du magasin
- Indicateurs clés de performance (KPI)
- Période configurable (jour, semaine, mois, année)
- Evolution par rapport à la période précédente

### 2. Analyse des Ventes
- **Ventes par produit** : Chiffre d'affaires et quantités vendues
- **Top produits** : Les 10 produits les plus vendus
- **Produits en baisse** : Détection automatique des produits dont les ventes chutent (>10%)
- **Evolution temporelle** : Suivi des ventes d'un produit sur une période

### 3. Marges Bénéficiaires
- **Marges par catégorie** : Analyse de rentabilité par catégorie de produits
- **Taux de marge** : Pourcentage de marge sur le chiffre d'affaires
- **Coûts vs Prix de vente** : Comparaison des coûts d'achat et prix de vente

### 4. Heures de Pointe
- Identification des plages horaires les plus fréquentées
- **Nombre de clients** par heure
- **Nombre de transactions** par heure
- **Intensité** : Niveau d'affluence en pourcentage (0-100%)
- Répartition par jour de la semaine

### 5. Statistiques de Fréquentation
- Nombre total de clients sur la période
- Moyenne de clients par jour
- Jour le plus fréquenté
- Heure de pointe moyenne

### 6. Alertes Stock
- Produits en rupture de stock
- Produits en dessous du seuil d'alerte
- Niveau critique/moyen/bas

## 📁 Structure du Module

```
backend/src/main/java/comcom/supermarket/manager/
├── model/
│   ├── reporting/
│   │   ├── VenteProduit.java           # Entité des ventes
│   │   └── StatistiqueFrequentation.java # Entité des stats de fréquentation
│   └── dto/
│       ├── DashboardDTO.java           # DTO du tableau de bord complet
│       ├── VenteProduitDTO.java        # DTO des ventes produit
│       ├── HeurePointeDTO.java         # DTO des heures de pointe
│       ├── MargeBeneficiaireDTO.java   # DTO des marges par catégorie
│       ├── FrequentationDTO.java       # DTO des stats de fréquentation
│       ├── PeriodeDTO.java             # DTO de la période
│       └── VentesGlobalesDTO.java      # DTO des ventes globales
├── repository/
│   ├── VenteProduitRepository.java
│   └── StatistiqueFrequentationRepository.java
├── service/
│   ├── DashboardService.java
│   └── impl/
│       └── DashboardServiceImpl.java
└── controller/
    └── DashboardController.java
```

## 🔌 API Endpoints

### Tableau de Bord Global
```http
GET /api/dashboard?debut=2025-01-01&fin=2025-01-31
```

**Réponse** :
```json
{
  "periode": {
    "debut": "2025-01-01",
    "fin": "2025-01-31",
    "type": "MOIS"
  },
  "ventesGlobales": {
    "chiffreAffaires": 150000.00,
    "margeTotale": 45000.00,
    "nombreVentes": 1250,
    "panierMoyen": 120.00,
    "evolutionPourcentage": 8.5
  },
  "topProduits": [...],
  "produitsEnBaisse": [...],
  "alertesStock": [...],
  "heuresPointe": [...],
  "margesParCategorie": [...],
  "frequentation": {...}
}
```

### Ventes par Produit
```http
GET /api/dashboard/ventes-produits?debut=2025-01-01&fin=2025-01-31
```

**Réponse** :
```json
[
  {
    "produitId": 1,
    "nomProduit": "Glace vanille",
    "categorie": "Surgelés",
    "quantiteVendue": 150,
    "montantVente": 37500.00,
    "margeBeneficiaire": 15000.00,
    "pourcentageMarge": 40.0,
    "periode": "2025-01-01 au 2025-01-31"
  }
]
```

### Produits en Baisse
```http
GET /api/dashboard/produits-en-baisse?debut=2025-01-01&fin=2025-01-31&seuil=10.0
```

**Réponse** :
```json
[
  {
    "produitId": 5,
    "nomProduit": "Glace chocolat",
    "categorie": "Surgelés",
    "quantiteVendue": 85,
    "evolutionPourcentage": -15.0,
    "periode": "2025-01-01 au 2025-01-31"
  }
]
```

### Heures de Pointe
```http
GET /api/dashboard/heures-pointe?debut=2025-01-01&fin=2025-01-31
```

**Réponse** :
```json
[
  {
    "jourSemaine": "FRIDAY",
    "plageHoraire": "18:00 - 19:00",
    "nombreClients": 180,
    "nombreTransactions": 95,
    "intensite": 100.0
  },
  {
    "jourSemaine": "SATURDAY",
    "plageHoraire": "20:00 - 21:00",
    "nombreClients": 165,
    "nombreTransactions": 88,
    "intensite": 91.7
  }
]
```

### Marges par Catégorie
```http
GET /api/dashboard/marges-categorie?debut=2025-01-01&fin=2025-01-31
```

**Réponse** :
```json
[
  {
    "categorie": "Surgelés",
    "chiffreAffaires": 50000.00,
    "coutTotal": 35000.00,
    "margeBrute": 15000.00,
    "pourcentageMarge": 30.0,
    "nombreProduits": 25
  }
]
```

### Evolution d'un Produit
```http
GET /api/dashboard/evolution-produit/1?debut=2025-01-01&fin=2025-01-31
```

**Réponse** :
```json
[
  {
    "produitId": 1,
    "quantiteVendue": 12,
    "montantVente": 3000.00,
    "periode": "2025-01-01"
  },
  {
    "produitId": 1,
    "quantiteVendue": 15,
    "montantVente": 3750.00,
    "periode": "2025-01-02"
  }
]
```

### Statistiques de Fréquentation
```http
GET /api/dashboard/frequentation?debut=2025-01-01&fin=2025-01-31
```

**Réponse** :
```json
{
  "clientsTotal": 4500,
  "clientsMoyenParJour": 145,
  "jourPlusFrequente": "SATURDAY",
  "heurePointeMoyenne": 19
}
```

## 🎬 Scénario d'Utilisation

### Scénario : Analyse des Performances Mensuelles

Le manager du magasin accède à son tableau de bord pour examiner les indicateurs de performance du mois en cours :

1. **Consultation du Dashboard**
   - Il voit que les ventes ont augmenté de 8.5% par rapport au mois précédent
   - Le chiffre d'affaires est de 150 000 € avec une marge de 45 000 €

2. **Détection d'une Baisse**
   - Le système affiche que les ventes de glaces ont chuté de 15% par rapport au mois précédent
   - Cette information apparaît dans la section "Produits en baisse"

3. **Analyse des Heures de Pointe**
   - Le manager constate que les vendredis et samedis en soirée sont les périodes les plus fréquentées
   - Il voit que l'intensité atteint 100% le vendredi entre 18h et 19h

4. **Consultation des Marges**
   - Il examine les marges bénéficiaires par catégorie
   - La catégorie "Surgelés" génère 30% de marge sur 50 000 € de CA

5. **Décision**
   - Le manager décide d'ajuster les horaires des employés pour les week-ends
   - Il augmente le stock de produits demandés le week-end
   - Il lance une promotion sur les glaces pour relancer les ventes

## 🧪 Tests

### Tests Unitaires
- `DashboardServiceTest` : Tests des services de reporting
  - Test du calcul des ventes globales
  - Test de la détection des produits en baisse
  - Test de l'identification des heures de pointe
  - Test du calcul des marges par catégorie
  - Test des statistiques de fréquentation

