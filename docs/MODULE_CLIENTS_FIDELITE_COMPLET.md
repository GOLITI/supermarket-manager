### ClientService

**Fonctionnalités principales :**
- CRUD complet des clients
- Gestion des points de fidélité avec traçabilité
- Enregistrement et historique des achats
- Segmentation automatique des clients
- Génération de promotions personnalisées
- Recherche et filtrage avancés

### CampagneMarketingService

**Fonctionnalités principales :**
- Création et gestion du cycle de vie des campagnes
- Ciblage intelligent basé sur segments et niveaux de fidélité
- Filtrage selon les préférences de communication
- Suivi des statistiques (ouvertures, clics, conversions)
- Automatisation du démarrage et de la clôture des campagnes

---

## 🤖 Tâches automatisées

### CampagneMarketingScheduler

**Vérification des campagnes à démarrer**
- Exécution : Toutes les heures
- Démarre automatiquement les campagnes programmées

**Vérification des campagnes à terminer**
- Exécution : Toutes les heures (à 30 minutes)
- Termine automatiquement les campagnes expirées

---

## 📊 Exemples d'utilisation

### 1. Créer un nouveau client

```json
POST /api/clients
{
  "nom": "Dupont",
  "prenom": "Jean",
  "email": "jean.dupont@example.ci",
  "telephone": "+2250101020304",
  "adresse": "Cocody, Abidjan",
  "ville": "Abidjan",
  "codePostal": "01",
  "dateNaissance": "1985-06-15",
  "accepteEmail": true,
  "accepteSMS": true,
  "accepteNotifications": true,
  "categoriesPreferees": ["Fruits et Légumes", "Produits Bio"]
}
```

**Réponse :**
- Génération automatique du numéro client
- Génération de la carte fidélité
- Attribution de 100 points de bienvenue
- Statut : ACTIF
- Segment : OCCASIONNEL
- Niveau : BRONZE

### 2. Enregistrer un achat

```json
POST /api/clients/1/achats
{
  "numeroTicket": "TKT20250108001",
  "dateAchat": "2025-01-08T14:30:00",
  "montantTotal": 25000,
  "montantRemise": 2000,
  "montantPaye": 23000,
  "pointsGagnes": 250,
  "pointsUtilises": 0,
  "produitsAchetes": [
    {
      "produitId": 1,
      "nomProduit": "Tomates Bio",
      "categorie": "Fruits et Légumes",
      "quantite": 2,
      "prixUnitaire": 2500,
      "montantLigne": 5000
    }
  ],
  "numeroCaisse": "CAISSE-01",
  "nomCaissier": "Marie Kouassi"
}
```

**Actions automatiques :**
- Ajout des points de fidélité
- Mise à jour des statistiques client
- Mise à jour du segment si nécessaire
- Enregistrement dans l'historique

### 3. Créer une campagne marketing ciblée

```json
POST /api/campagnes-marketing
{
  "nom": "Promotion Fruits Bio",
  "description": "15% de réduction sur les fruits bio",
  "type": "EMAIL",
  "dateDebut": "2025-01-15",
  "dateFin": "2025-01-31",
  "segmentsCibles": ["PREMIUM", "VIP"],
  "niveauxCibles": ["OR", "DIAMANT"],
  "sujet": "🍎 Profitez de 15% sur nos fruits bio !",
  "message": "Chers clients, bénéficiez de 15% de réduction sur tous nos fruits bio jusqu'au 31 janvier !",
  "typeOffre": "REDUCTION_POURCENTAGE",
  "valeurOffre": 15,
  "codePromo": "FRUITBIO15"
}
```

**Workflow automatique :**
1. Création en statut BROUILLON
2. Calcul des cibles selon les critères
3. Lancement de la campagne
4. Envoi aux clients éligibles
5. Suivi des statistiques (ouvertures, clics, conversions)
6. Clôture automatique à la date de fin

---

## 🎯 Cas d'usage : Scénario Monsieur Dupont

**Contexte :** Monsieur Dupont est un client régulier qui utilise l'application mobile.

### 1. Consultation des points de fidélité

```
GET /api/clients/carte/CF1704715200000/fidelite
```

**Réponse :**
```json
{
  "numeroCarteFidelite": "CF1704715200000",
  "pointsFidelite": 800,
  "niveauActuel": "ARGENT",
  "procheNiveau": "OR",
  "pointsManquants": 1200,
  "reductionDisponible": 8000,
  "reductionMaximale": 500,
  "multiplicateurPoints": 1.5,
  "messageNiveau": "Vous êtes au niveau ARGENT avec 800 points. Il vous manque 1200 points pour atteindre le niveau OR."
}
```

### 2. Promotions personnalisées

```
GET /api/clients/1/promotions-personnalisees
```

**Réponse basée sur l'historique :**
- Réduction de 15% sur les fruits bio (catégorie préférée)
- Offre exclusive niveau ARGENT : 10% sur le prochain achat
- Points bonus doublés sur les produits bio ce mois-ci

### 3. Utilisation en caisse

Lors du passage en caisse, le client peut :
- Scanner le code QR de sa carte fidélité
- Utiliser ses 800 points = 8000 FCFA de réduction
- Bénéficier de la réduction maximale de 500 FCFA (niveau ARGENT)
- Gagner de nouveaux points sur l'achat

---

## 📈 Statistiques et analyses

### Indicateurs clients
- Nombre total de clients
- Répartition par segment
- Répartition par niveau de fidélité
- Taux de clients actifs/inactifs
- Panier moyen par segment

### Indicateurs campagnes
- Taux d'ouverture moyen
- Taux de clics moyen
- Taux de conversion moyen
- ROI des campagnes
- Chiffre d'affaires généré

---

## ✅ Fonctionnalités implémentées

✅ Gestion complète des clients  
✅ Système de fidélité multi-niveaux  
✅ Gestion des points de fidélité avec traçabilité  
✅ Historique complet des achats  
✅ Segmentation automatique des clients  
✅ Promotions personnalisées  
✅ Campagnes marketing ciblées  
✅ Ciblage intelligent (segments, niveaux, préférences)  
✅ Suivi des statistiques de campagnes  
✅ Automatisation (démarrage, clôture des campagnes)  
✅ Support multi-canal (Email, SMS, Notifications)  
✅ API REST complète  
✅ Validation des données  
✅ Gestion des erreurs  

---

## 🚀 Prochaines étapes recommandées

1. **Tests unitaires et d'intégration**
   - Créer les tests JUnit pour tous les services
   - Tests des endpoints REST
   - Tests de segmentation automatique

2. **Intégrations externes**
   - Service d'envoi d'emails (SendGrid, Mailchimp)
   - Service SMS (Twilio, Africa's Talking)
   - Service de notifications push

3. **Améliorations fonctionnelles**
   - Système de parrainage client
   - Cadeaux anniversaire automatiques
   - Programme de cashback
   - Badges et récompenses
   - Gamification

4. **Analytics avancés**
   - Prédiction du churn client
   - Recommandations produits basées sur l'IA
   - Analyse RFM (Récence, Fréquence, Montant)
   - Analyse de cohorte

---

## 📝 Notes importantes

- **Devise** : Toutes les valeurs monétaires sont en Francs CFA (FCFA)
- **Conversion points** : 1 point = 10 FCFA de réduction
- **Bonus bienvenue** : 100 points offerts à l'inscription
- **Soft delete** : Les clients ne sont jamais supprimés, seulement désactivés
- **Segmentation** : Mise à jour automatique à chaque achat
- **Niveau fidélité** : Mise à jour automatique à chaque mouvement de points

---

**Date de création** : 08 Janvier 2025  
**Version** : 1.0.0  
**Statut** : ✅ Compilé avec succès
# MODULE 3 : CLIENTS ET FIDÉLITÉ - Documentation Complète

## 📋 Vue d'ensemble

Le module Clients et Fidélité offre une gestion complète de la clientèle avec un système de fidélisation avancé, permettant de suivre les achats, gérer les points de fidélité, et créer des campagnes marketing ciblées.

**Devise utilisée : Franc CFA (FCFA)** - Côte d'Ivoire

---

## 🏗️ Architecture du Module

### Modèles de données

#### 1. **Client** (`model/client/Client.java`)
Entité principale représentant un client du supermarché.

**Attributs principaux :**
- Informations personnelles (nom, prénom, email, téléphone, adresse)
- Date de naissance et date d'inscription
- Numéro client unique et numéro de carte fidélité
- Statut du client (ACTIF, INACTIF, SUSPENDU, BLOQUÉ)
- Segment client (VIP, PREMIUM, RÉGULIER, OCCASIONNEL)
- Points de fidélité et niveau (BRONZE, ARGENT, OR, DIAMANT)
- Statistiques d'achat (total achats, nombre achats, panier moyen)
- Préférences de communication (email, SMS, notifications)
- Catégories de produits préférées

**Segmentation automatique :**
- **VIP** : Plus de 500 000 FCFA d'achats
- **PREMIUM** : Plus de 200 000 FCFA d'achats
- **RÉGULIER** : Au moins 5 achats
- **OCCASIONNEL** : Moins de 5 achats

**Niveaux de fidélité :**
- **BRONZE** : 0-499 points (multiplicateur x1.0)
- **ARGENT** : 500-1999 points (multiplicateur x1.5, réduction max 500 FCFA)
- **OR** : 2000-4999 points (multiplicateur x2.0, réduction max 1000 FCFA)
- **DIAMANT** : 5000+ points (multiplicateur x2.5, réduction max 2000 FCFA)

#### 2. **HistoriqueAchat** (`model/client/HistoriqueAchat.java`)
Enregistre tous les achats effectués par un client.

**Attributs :**
- Numéro de ticket unique
- Date et heure d'achat
- Montants (total, remise, payé)
- Points gagnés et utilisés
- Liste des produits achetés (avec détails)
- Informations de caisse (numéro, caissier)

#### 3. **MouvementPoints** (`model/client/MouvementPoints.java`)
Trace tous les mouvements de points de fidélité.

**Types de mouvements :**
- GAIN_ACHAT : Points gagnés lors d'un achat
- GAIN_BONUS : Bonus offert (bienvenue, parrainage, etc.)
- GAIN_PARRAINAGE : Points de parrainage
- UTILISATION : Utilisation de points
- EXPIRATION : Expiration de points
- AJUSTEMENT : Ajustement manuel

#### 4. **CampagneMarketing** (`model/client/CampagneMarketing.java`)
Gère les campagnes marketing ciblées.

**Types de campagnes :**
- EMAIL : Campagne par email
- SMS : Campagne par SMS
- NOTIFICATION : Notification push
- MULTI_CANAL : Combinaison de canaux

**Statuts de campagne :**
- BROUILLON : En cours de création
- PROGRAMMEE : Programmée pour envoi
- EN_COURS : Campagne active
- TERMINEE : Campagne terminée
- ANNULEE : Campagne annulée

**Statistiques de campagne :**
- Nombre de cibles, envoyés, ouvertures, clics, conversions
- Taux d'ouverture, de clics, de conversion
- Chiffre d'affaires généré

---

## 🔌 API REST

### Endpoints Clients (`/api/clients`)

#### Gestion des clients

**POST /api/clients**
- Créer un nouveau client
- Génère automatiquement un numéro client et une carte fidélité
- Bonus de bienvenue : 100 points

**GET /api/clients/{id}**
- Récupérer un client par son ID

**GET /api/clients/carte/{numeroCarteFidelite}**
- Récupérer un client par son numéro de carte fidélité

**GET /api/clients**
- Récupérer tous les clients

**GET /api/clients/recherche?q={terme}**
- Rechercher des clients (nom, prénom, email, téléphone)

**GET /api/clients/segment/{segment}**
- Récupérer les clients d'un segment spécifique

**PUT /api/clients/{id}**
- Modifier les informations d'un client

**DELETE /api/clients/{id}**
- Désactiver un client (soft delete)

#### Gestion de la fidélité

**GET /api/clients/{id}/fidelite**
- Récupérer les informations de fidélité d'un client
- Retourne : points, niveau, réduction disponible, points manquants pour le prochain niveau

**POST /api/clients/{id}/points/ajouter**
- Ajouter des points de fidélité
- Paramètres : points, reference, description

**POST /api/clients/{id}/points/utiliser**
- Utiliser des points de fidélité
- Paramètres : points, reference
- Convertit les points en réduction (10 FCFA par point)

**GET /api/clients/{id}/points/historique**
- Récupérer l'historique des mouvements de points

#### Historique des achats

**GET /api/clients/{id}/achats**
- Récupérer l'historique des achats d'un client

**POST /api/clients/{id}/achats**
- Enregistrer un nouvel achat
- Met à jour automatiquement les statistiques et les points

#### Promotions personnalisées

**GET /api/clients/{id}/promotions-personnalisees**
- Récupérer les promotions personnalisées pour un client
- Basé sur l'historique d'achat et les préférences

### Endpoints Campagnes Marketing (`/api/campagnes-marketing`)

**POST /api/campagnes-marketing**
- Créer une nouvelle campagne

**GET /api/campagnes-marketing/{id}**
- Récupérer une campagne

**GET /api/campagnes-marketing**
- Récupérer toutes les campagnes

**PUT /api/campagnes-marketing/{id}**
- Modifier une campagne (seulement en brouillon)

**DELETE /api/campagnes-marketing/{id}**
- Supprimer une campagne

**POST /api/campagnes-marketing/{id}/calculer-cibles**
- Calculer le nombre de clients cibles selon les critères

**POST /api/campagnes-marketing/{id}/lancer**
- Lancer une campagne

**GET /api/campagnes-marketing/actives**
- Récupérer les campagnes actives

---

## 💼 Services métier


