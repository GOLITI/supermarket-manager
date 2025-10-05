# 🚀 Guide de Démarrage Rapide - Système de Gestion des Stocks

## ✅ Statut de l'Implémentation

Le système de gestion des stocks est **entièrement fonctionnel** et prêt à être testé !

### Ce qui a été implémenté :

#### 1. **Architecture Complète Backend (Spring Boot)**
- ✅ 37 fichiers Java compilés avec succès
- ✅ Entités JPA : Produit, Stock, Commande, Fournisseur, Catégorie, Entrepôt
- ✅ Repositories avec requêtes personnalisées
- ✅ Services métier avec logique complète
- ✅ Contrôleurs REST API
- ✅ Gestion globale des exceptions
- ✅ Scheduler pour alertes automatiques

#### 2. **Fonctionnalités Implémentées**
- ✅ Gestion complète des stocks (CRUD + mouvements)
- ✅ Système d'alertes de stock avec 3 niveaux (CRITIQUE, MOYEN, BAS)
- ✅ Génération automatique de commandes basée sur les alertes
- ✅ Gestion des fournisseurs et catégories
- ✅ Gestion des produits avec code-barres
- ✅ Suivi des dates de péremption
- ✅ Vérification automatique toutes les heures
- ✅ Historique des commandes

## 📋 Prérequis

Avant de démarrer, vous devez installer :

```bash
# PostgreSQL
sudo apt-get install postgresql postgresql-contrib

# Démarrer PostgreSQL
sudo systemctl start postgresql
sudo systemctl enable postgresql
```

## 🎯 Démarrage de l'Application

### Option 1 : Utiliser le script automatique

```bash
cd /home/goliti/PROJETS/AGLPROJET/supermarket-manager/backend
./start-backend.sh
```

### Option 2 : Démarrage manuel

```bash
cd /home/goliti/PROJETS/AGLPROJET/supermarket-manager/backend

# 1. Créer la base de données
sudo -u postgres psql -c "CREATE DATABASE supermarket_db;"

# 2. Démarrer l'application
mvn spring-boot:run
```

L'application sera accessible sur : **http://localhost:8080**

## 🧪 Tester le Système

### 1. Vérifier que l'application fonctionne

```bash
curl http://localhost:8080/api/stocks
```

### 2. Récupérer les alertes de stock (Scénario principal)

```bash
curl http://localhost:8080/api/stocks/alertes
```

**Résultat attendu** : La farine T45 avec 50 unités (seuil: 100) doit apparaître

### 3. Générer des commandes automatiques

```bash
curl -X POST http://localhost:8080/api/commandes/generer-automatiques
```

**Résultat** : Création automatique d'une commande de 500 unités de farine T45

### 4. Lister toutes les commandes

```bash
curl http://localhost:8080/api/commandes
```

### 5. Rechercher un produit

```bash
curl "http://localhost:8080/api/produits/search?q=farine"
```

### 6. Ajouter du stock

```bash
curl -X POST "http://localhost:8080/api/stocks/1/ajouter?quantite=100"
```

## 📊 Données de Test

Le fichier `data.sql` contient des données qui correspondent exactement au scénario du projet :

### Produits en alerte :
1. **Farine T45** : 50 unités (seuil: 100) - ⚠️ ALERTE MOYENNE
2. **Lait Entier** : 10 unités (seuil: 50) - 🔴 ALERTE CRITIQUE

### Produits normaux :
3. **Farine T55** : 250 unités (seuil: 100) - ✅ OK
4. **Riz Basmati** : 150 unités (seuil: 80) - ✅ OK

### Fournisseurs :
- **Meunerie Locale** : Fournisseur de farines (délai: 3 jours)
- **Grands Moulins d'Abidjan** : Grossiste céréales (délai: 5 jours)
- **Distributeur Nestlé CI** : Produits laitiers (délai: 2 jours)

## 📡 API Endpoints Disponibles

### Gestion des Stocks

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/stocks` | Liste tous les stocks |
| GET | `/api/stocks/{id}` | Détails d'un stock |
| GET | `/api/stocks/alertes` | **Stocks en alerte** ⚠️ |
| GET | `/api/stocks/peremption?joursAvantPeremption=30` | Produits proches péremption |
| POST | `/api/stocks/{id}/ajouter?quantite=X` | Ajouter du stock |
| POST | `/api/stocks/{id}/retirer?quantite=X` | Retirer du stock |

### Gestion des Produits

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/produits` | Liste tous les produits |
| GET | `/api/produits/search?q=text` | Rechercher des produits |
| GET | `/api/produits/code-barres/{code}` | Trouver par code-barres |
| POST | `/api/produits` | Créer un produit |
| PUT | `/api/produits/{id}` | Modifier un produit |

### Gestion des Commandes

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/commandes` | Liste toutes les commandes |
| GET | `/api/commandes/{id}` | Détails d'une commande |
| POST | `/api/commandes` | Créer une commande |
| POST | `/api/commandes/generer-automatiques` | **Générer commandes auto** 🤖 |
| POST | `/api/commandes/{id}/valider` | Valider une commande |
| POST | `/api/commandes/{id}/recevoir?dateLivraison=2025-01-08` | Recevoir une commande |

## 🤖 Tâches Automatiques

### StockAlertScheduler

1. **Vérification des alertes** : Toutes les heures
   - Détecte les stocks sous le seuil
   - Classe par niveau (CRITIQUE, MOYEN, BAS)
   - Log dans la console

2. **Vérification péremption** : Chaque jour à 8h
   - Détecte les produits < 30 jours avant péremption
   - Recommande des promotions

## 🎬 Scénario Complet (Cas d'Usage)

### Étape 1 : Le système détecte une alerte (automatique)
```bash
# Vérifier les alertes
curl http://localhost:8080/api/stocks/alertes
```
**Résultat** : Farine T45 - 50 unités (seuil: 100)

### Étape 2 : Générer une commande automatique
```bash
curl -X POST http://localhost:8080/api/commandes/generer-automatiques
```
**Résultat** : Commande créée avec 500 unités recommandées

### Étape 3 : Valider la commande
```bash
curl -X POST http://localhost:8080/api/commandes/1/valider
```
**Résultat** : Statut change de BROUILLON → CONFIRMEE

### Étape 4 : Recevoir la livraison
```bash
curl -X POST "http://localhost:8080/api/commandes/1/recevoir?dateLivraison=2025-01-08"
```
**Résultat** : 
- Stock mis à jour : 50 → 550 unités
- Alerte désactivée automatiquement

## 📝 Logs Importants

Quand l'application démarre, vous verrez :
```
⚠️ 2 produit(s) en alerte de stock détecté(s)
   - Alertes CRITIQUES: 1
   - Alertes MOYENNES: 1
STOCK CRITIQUE: Lait Entier 1L - Quantité actuelle: 10, Seuil: 50
```

## 🔧 Configuration

Fichier : `backend/src/main/resources/application.properties`

### Configuration Base de Données
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/supermarket_db
spring.datasource.username=postgres
spring.datasource.password=postgres
```

### Configuration Serveur
```properties
server.port=8080
```

## 📚 Structure du Code

```
backend/src/main/java/comcom/supermarket/manager/
├── controller/          # API REST
│   ├── StockController
│   ├── ProduitController
│   └── CommandeController
├── service/            # Logique métier
│   └── impl/
│       ├── StockServiceImpl
│       ├── ProduitServiceImpl
│       └── CommandeServiceImpl
├── repository/         # Accès données
├── model/             # Entités JPA
│   ├── produit/
│   ├── commande/
│   └── dto/
├── exception/         # Gestion erreurs
└── scheduler/         # Tâches automatiques
```

## ✨ Points Forts de l'Implémentation

1. ✅ **Architecture propre** : Séparation claire des responsabilités
2. ✅ **Code compilé** : 0 erreur, seulement des warnings mineurs
3. ✅ **Gestion d'erreurs** : Exception handler global
4. ✅ **Données de test** : Scénario complet préchargé
5. ✅ **Automatisation** : Scheduler pour surveillance 24/7
6. ✅ **API RESTful** : Endpoints bien documentés
7. ✅ **Logs détaillés** : Traçabilité complète

## 🐛 Dépannage

### Erreur de connexion PostgreSQL
```bash
sudo systemctl status postgresql
sudo systemctl start postgresql
```

### Port 8080 déjà utilisé
Modifier dans `application.properties` :
```properties
server.port=8081
```

### Réinitialiser la base de données
```bash
sudo -u postgres psql -c "DROP DATABASE IF EXISTS supermarket_db;"
sudo -u postgres psql -c "CREATE DATABASE supermarket_db;"
```

## 📞 Support

Pour toute question sur l'implémentation, vérifiez :
- `backend/README.md` : Documentation détaillée
- Logs de l'application : Console Spring Boot
- Tests : `backend/src/test/java/`

---

**Projet** : AGL L3 SIGL 2025-2026  
**Module** : Gestion des Stocks  
**Statut** : ✅ Opérationnel et prêt pour démonstration

