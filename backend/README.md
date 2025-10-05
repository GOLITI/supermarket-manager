b# Système de Gestion des Stocks - Supermarché

## 📋 Description

Système de gestion des stocks pour un supermarché, développé avec Spring Boot. Ce module implémente la fonctionnalité complète de gestion des stocks telle que décrite dans le scénario du projet.

## 🎯 Fonctionnalités Implémentées

### 1. Gestion des Stocks
- ✅ Suivi en temps réel des stocks par entrepôt
- ✅ Seuils de réapprovisionnement configurables
- ✅ Alertes automatiques pour stocks bas
- ✅ Gestion des dates de péremption
- ✅ Mouvements de stock (entrées/sorties)
- ✅ Inventaire multi-entrepôts

### 2. Système d'Alertes
- ✅ Détection automatique des stocks en alerte
- ✅ Classification des alertes (CRITIQUE, MOYEN, BAS)
- ✅ Vérification périodique (toutes les heures)
- ✅ Alerte produits proches de la péremption

### 3. Gestion des Commandes
- ✅ Création de commandes fournisseurs
- ✅ Génération automatique de commandes basée sur les alertes
- ✅ Suivi du statut des commandes
- ✅ Réception et mise à jour automatique des stocks
- ✅ Historique des commandes

### 4. Gestion des Produits
- ✅ CRUD complet des produits
- ✅ Recherche et filtrage
- ✅ Gestion des codes-barres
- ✅ Association fournisseurs/catégories

## 🏗️ Architecture

```
comcom.supermarket.manager/
├── model/              # Entités JPA
│   ├── produit/       # Produit, Stock, Categorie, Entrepot
│   ├── fournisseur/   # Fournisseur
│   ├── commande/      # Commande, LigneCommande
│   └── dto/           # Data Transfer Objects
├── repository/        # Repositories JPA
├── service/           # Services métier
│   └── impl/         # Implémentations
├── controller/        # Contrôleurs REST
├── exception/         # Gestion des exceptions
└── scheduler/         # Tâches planifiées
```

## 🚀 Démarrage

### Prérequis
- Java 21
- PostgreSQL 13+
- Redis (optionnel pour le cache)
- Maven 3.8+

### Configuration de la base de données

1. Créer la base de données PostgreSQL :
```bash
psql -U postgres
CREATE DATABASE supermarket_db;
\q
```

2. Configurer les credentials dans `application.properties` :
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/supermarket_db
spring.datasource.username=postgres
spring.datasource.password=votre_mot_de_passe
```

### Installation et démarrage

```bash
# Dans le répertoire backend
cd backend

# Installer les dépendances
./mvnw clean install

# Démarrer l'application
./mvnw spring-boot:run
```

L'application sera accessible sur `http://localhost:8080`

## 📊 Données de Test

Le fichier `data.sql` contient des données de test qui correspondent au scénario :
- **Farine T45** : 50 unités (seuil: 100) - **⚠️ ALERTE**
- **Lait Entier** : 10 unités (seuil: 50) - **⚠️ ALERTE CRITIQUE**
- **Farine T55** : 250 unités (seuil: 100) - ✅ OK
- **Riz Basmati** : 150 unités (seuil: 80) - ✅ OK

## 🔌 API Endpoints

### Stocks

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/stocks` | Liste tous les stocks |
| GET | `/api/stocks/{id}` | Récupérer un stock |
| GET | `/api/stocks/alertes` | **Stocks en alerte** |
| GET | `/api/stocks/peremption` | Produits proches péremption |
| POST | `/api/stocks/{id}/ajouter?quantite=X` | Ajouter du stock |
| POST | `/api/stocks/{id}/retirer?quantite=X` | Retirer du stock |

### Produits

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/produits` | Liste tous les produits |
| GET | `/api/produits/{id}` | Récupérer un produit |
| GET | `/api/produits/search?q=farine` | Rechercher des produits |
| POST | `/api/produits` | Créer un produit |
| PUT | `/api/produits/{id}` | Modifier un produit |

### Commandes

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/commandes` | Liste toutes les commandes |
| GET | `/api/commandes/{id}` | Récupérer une commande |
| POST | `/api/commandes` | Créer une commande |
| POST | `/api/commandes/generer-automatiques` | **Générer commandes auto** |
| POST | `/api/commandes/{id}/valider` | Valider une commande |
| POST | `/api/commandes/{id}/recevoir` | Recevoir une commande |

## 📝 Scénario d'utilisation

### Scénario : Alerte de stock pour la Farine T45

1. **Détection automatique** (scheduler toutes les heures)
   ```
   GET /api/stocks/alertes
   ```
   Retourne : Farine T45 avec 50 unités (seuil: 100)

2. **Génération automatique de commande**
   ```
   POST /api/commandes/generer-automatiques
   ```
   Crée automatiquement une commande de 500 unités chez "Meunerie Locale"

3. **Validation de la commande**
   ```
   POST /api/commandes/{id}/valider
   ```
   Change le statut en CONFIRMEE

4. **Réception de la commande**
   ```
   POST /api/commandes/{id}/recevoir?dateLivraison=2025-01-08
   ```
   Met à jour automatiquement le stock : 50 → 550 unités

## ⏰ Tâches Planifiées

### StockAlertScheduler

- **Vérification des alertes** : Toutes les heures
  - Détecte les stocks <= seuil
  - Classe par niveau (CRITIQUE, MOYEN, BAS)
  - Log les alertes critiques

- **Vérification péremption** : Chaque jour à 8h
  - Détecte les produits périssables
  - Alerte pour les produits < 30 jours avant péremption

## 🔍 Exemples de Requêtes

### Récupérer les stocks en alerte
```bash
curl -X GET http://localhost:8080/api/stocks/alertes
```

### Générer des commandes automatiques
```bash
curl -X POST http://localhost:8080/api/commandes/generer-automatiques
```

### Ajouter du stock
```bash
curl -X POST "http://localhost:8080/api/stocks/1/ajouter?quantite=100"
```

### Rechercher un produit
```bash
curl -X GET "http://localhost:8080/api/produits/search?q=farine"
```

## 🛠️ Technologies Utilisées

- **Spring Boot 3.5.6** - Framework Java
- **Spring Data JPA** - Accès aux données
- **PostgreSQL** - Base de données
- **Lombok** - Réduction du code boilerplate
- **Spring Scheduling** - Tâches planifiées
- **Maven** - Gestion des dépendances

## 📈 Prochaines Étapes

- [ ] Implémenter les notifications par email
- [ ] Ajouter un tableau de bord avec statistiques
- [ ] Intégrer l'historique des mouvements de stock
- [ ] Créer des rapports PDF/Excel
- [ ] Ajouter la prévision de demande (ML)

## 👥 Contributeurs

Projet académique - AGL L3 SIGL 2025-2026

---
**Note** : Ce système fait partie du projet complet de gestion de supermarché incluant aussi :
- Gestion des caisses
- Gestion RH
- Système de fidélisation
- Tableau de bord et reporting

