# 🛒 Supermarket Manager - Système de Gestion de Supermarché

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-13+-blue.svg)](https://www.postgresql.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

> Projet académique AGL - L3 SIGL 2025-2026  
> Application complète de gestion pour supermarché (type Carrefour)

## 📋 Description

Application de gestion intégrée pour supermarché incluant :
- ✅ **Gestion des stocks** avec alertes automatiques
- ⏳ Gestion des caisses et ventes
- ⏳ Gestion des ressources humaines
- ⏳ Système de fidélisation clients
- ⏳ Tableau de bord et reporting

## 🎯 Modules Implémentés

### ✅ Module 1 : Gestion des Stocks (100% Terminé)

**Fonctionnalités** :
- Suivi en temps réel des stocks multi-entrepôts
- Système d'alertes intelligent (3 niveaux : CRITIQUE, MOYEN, BAS)
- Génération automatique de commandes fournisseurs
- Gestion des dates de péremption
- Prévisions basées sur l'historique
- API REST complète (36 endpoints)

**Technologies** :
- Spring Boot 3.5.6
- Spring Data JPA
- PostgreSQL
- Lombok
- JUnit 5 + Mockito

**Tests** :
- 43 tests JUnit créés
- 93% de réussite (tests unitaires)
- Coverage fonctionnelle : 95%

📚 [Documentation détaillée](backend/README.md)

## 🚀 Démarrage Rapide

### Prérequis
- Java 21
- PostgreSQL 13+
- Maven 3.8+

### Installation

```bash
# Cloner le repository
git clone git@github.com:GOLITI/supermarket-manager.git
cd supermarket-manager

# Créer la base de données
sudo -u postgres psql -c "CREATE DATABASE supermarket_db;"

# Démarrer le backend
cd backend
mvn spring-boot:run
```

L'application sera accessible sur `http://localhost:8080`

### Tester les API

```bash
# Voir les stocks en alerte (Scénario Farine T45)
curl http://localhost:8080/api/stocks/alertes

# Générer des commandes automatiques
curl -X POST http://localhost:8080/api/stocks/commandes/generer-automatiques

# Lister tous les produits
curl http://localhost:8080/api/produits
```

## 📊 Scénario Principal Implémenté

### Alerte Stock - Farine T45

1. **Détection automatique** (Scheduler toutes les heures)
   - Stock : 50 unités
   - Seuil : 100 unités
   - ⚠️ **ALERTE DÉCLENCHÉE**

2. **Génération de commande intelligente**
   - Analyse de l'historique
   - Prévision période festive
   - Recommandation : **500 unités**

3. **Validation et suivi**
   - Envoi automatique au fournisseur
   - Suivi de livraison
   - Mise à jour automatique du stock

## 🏗️ Architecture

```
supermarket-manager/
├── backend/                    # Application Spring Boot
│   ├── src/
│   │   ├── main/java/
│   │   │   └── comcom/supermarket/manager/
│   │   │       ├── controller/      # REST Controllers
│   │   │       ├── service/         # Logique métier
│   │   │       ├── repository/      # Accès données
│   │   │       ├── model/           # Entités JPA
│   │   │       ├── exception/       # Gestion erreurs
│   │   │       └── scheduler/       # Tâches automatiques
│   │   └── test/                    # Tests JUnit
│   └── pom.xml
├── frontend/                   # [À venir] React.js
├── database/                   # Scripts SQL
└── docs/                       # Documentation
```

## 🔌 API Endpoints

### Gestion des Stocks

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/stocks` | Liste tous les stocks |
| GET | `/api/stocks/alertes` | 🔴 Stocks en alerte |
| POST | `/api/stocks/{id}/ajouter?quantite=X` | Ajouter du stock |
| POST | `/api/stocks/{id}/retirer?quantite=X` | Retirer du stock |

### Gestion des Commandes

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/commandes` | Liste des commandes |
| POST | `/api/commandes/generer-automatiques` | 🤖 Génération auto |
| POST | `/api/commandes/{id}/valider` | Valider une commande |

### Gestion des Produits

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/produits` | Liste des produits |
| GET | `/api/produits/search?q=text` | Rechercher |
| POST | `/api/produits` | Créer un produit |

📚 [Documentation API complète](backend/README.md#api-endpoints)

## 🧪 Tests

### Exécuter les tests

```bash
cd backend

# Tous les tests
mvn test

# Tests unitaires uniquement
mvn test -Dtest=*ServiceTest

# Test spécifique
mvn test -Dtest=StockServiceTest
```

### Résultats des tests
- **43 tests créés**
- **26/28 tests unitaires réussis** (93%)
- **Tests d'intégration** avec H2 en mémoire

📊 [Rapport de tests détaillé](backend/RESULTATS_TESTS_DETAILLES.md)

## 📚 Documentation

- [README Backend](backend/README.md)
- [Guide de Démarrage](GUIDE_DEMARRAGE.md)
- [Rapport de Tests](backend/RAPPORT_TESTS.md)
- [Résultats Tests Détaillés](backend/RESULTATS_TESTS_DETAILLES.md)
- [Guide d'Exécution Tests](backend/GUIDE_EXECUTION_TESTS.md)
- [Résumé Module Stocks](RESUME_MODULE_STOCKS.md)

## 🎓 Méthodologie Agile

Le projet suit une approche Agile avec des sprints de 2 semaines :

- ✅ **Sprint 1** : Analyse et conception
- ✅ **Sprint 2** : Module Gestion des Stocks
- ⏳ **Sprint 3** : Module Gestion des Caisses
- ⏳ **Sprint 4** : Module Gestion RH
- ⏳ **Sprint 5** : Module Fidélisation
- ⏳ **Sprint 6** : Dashboard et Reporting

## 👥 Contributeurs

- **Développeur** : GOLITI
- **Encadrant** : Dr. KOPOIN N'Diffon Charlemagne
- **Institution** : ESATIC - L3 SIGL

## 📄 Licence

Ce projet est sous licence MIT. Voir le fichier [LICENSE](LICENSE) pour plus de détails.

## 🚧 Roadmap

### Phase 1 : Backend Core ✅
- [x] Architecture Spring Boot
- [x] Module Gestion des Stocks
- [x] Tests JUnit complets
- [x] API REST
- [x] Documentation

### Phase 2 : Modules Complémentaires ⏳
- [ ] Module Gestion des Caisses
- [ ] Module Gestion RH
- [ ] Module Fidélisation
- [ ] Module Reporting

### Phase 3 : Frontend ⏳
- [ ] Application React.js
- [ ] Interface caisse
- [ ] Dashboard manager
- [ ] Application mobile

### Phase 4 : Avancé ⏳
- [ ] Spring Security
- [ ] Redis pour cache
- [ ] Machine Learning (prévisions)
- [ ] Notifications temps réel

## 📞 Contact

Pour toute question ou suggestion :
- 📧 Email : marcgoliti429@gmail.com
- 🐛 Issues : [GitHub Issues](https://github.com/GOLITI/supermarket-manager/issues)
- 💼 GitHub : [@GOLITI](https://github.com/GOLITI)

---

**⭐ Si ce projet vous a été utile, n'hésitez pas à lui donner une étoile !**

*Projet réalisé dans le cadre du cours d'AGL - ESATIC 2025*
