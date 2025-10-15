# Documentation Complète des Diagrammes UML - Supermarket Manager

## Vue d'ensemble

Ce document récapitule tous les diagrammes UML créés pour le projet Supermarket Manager. Les diagrammes ont été conçus en utilisant la syntaxe PlantUML pour faciliter leur génération et leur maintenance.

## Liste des Diagrammes

### 1. Diagramme de Cas d'Utilisation
**Fichier**: `cas-utilisation.md`

Décrit les interactions entre les acteurs et le système :
- **6 Acteurs principaux** : Gestionnaire Stock, Caissier, Responsable RH, Responsable Marketing, Manager, Responsable Fournisseurs
- **6 Modules fonctionnels** : Stocks, Caisses, RH, Clients, Dashboard, Fournisseurs
- **23 Cas d'utilisation** détaillés avec préconditions et postconditions

### 2. Diagramme de Classes
**Fichier**: `diagramme-classes.md`

Représente la structure complète du système :
- **Module Produits et Stocks** : Produit, Stock, Entrepot, Categorie
- **Module Caisses** : Transaction, SeanceCaisse, Paiement, LigneTransaction
- **Module RH** : Employe, Planning, Shift, Absence
- **Module Clients** : Client, CarteFidelite, HistoriqueAchat, Promotion
- **Module Fournisseurs** : Fournisseur, Commande, LigneCommande
- **Module Dashboard** : Dashboard, RapportVente, StatistiqueVente

**Patterns utilisés** : Repository, Service, DTO, Builder, Observer

### 3. Diagramme de Séquence
**Fichier**: `diagramme-sequence.md`

6 séquences détaillées montrant les interactions dynamiques :

1. **Effectuer une Transaction de Vente**
   - Scan produits → Vérification stock → Paiement → Fidélité → Déduction stock

2. **Réapprovisionnement Automatique**
   - Détection stock faible → Commande automatique → Notification

3. **Gestion des Absences**
   - Demande employé → Validation RH → Mise à jour planning

4. **Ouverture/Fermeture de Caisse**
   - Déclaration fond → Transactions → Comptage → Calcul écart

5. **Consultation Dashboard**
   - Requêtes parallèles → Agrégation données → KPIs temps réel

6. **Gestion Carte Fidélité**
   - Création → Utilisation → Crédit points → Upgrade niveau

### 4. Diagramme d'Activité
**Fichier**: `diagramme-activite.md`

6 processus métier détaillés avec conditions et parallélisme :

1. **Processus de Vente Complète**
   - Ouverture séance → Scan produits → Paiements → Fermeture

2. **Gestion Commande Fournisseur**
   - Alerte stock → Sélection fournisseur → Commande → Livraison → Réception

3. **Planification Hebdomadaire**
   - Analyse besoins → Affectation employés → Validation → Notification

4. **Traitement Demande Absence**
   - Soumission → Vérification conflits → Validation/Rejet → Notification

5. **Gestion Carte Fidélité**
   - Création → Utilisation achat → Calcul points → Upgrade

6. **Contrôle Stock et Inventaire**
   - Comptage → Comparaison → Écarts → Ajustements → Rapport

### 5. Diagramme d'État
**Fichier**: `diagramme-etat.md`

7 machines à états représentant les cycles de vie :

1. **Transaction**
   - États : Initialisée → En Cours → En Attente Paiement → Validée/Annulée

2. **Commande Fournisseur**
   - États : Brouillon → Validée → En Cours → Livrée → Complétée/Annulée

3. **Séance de Caisse**
   - États : Fermée → Ouverte → Suspendue → En Clôture → Fermée

4. **Demande d'Absence**
   - États : Brouillon → En Attente → Approuvée/Rejetée → Confirmée → Terminée

5. **Carte de Fidélité**
   - États : Active (Bronze/Argent/Or/Platine) → Expirée → Archivée/Annulée

6. **Stock**
   - États : Normal → En Alerte → Critique → Rupture
   - Parallèle : Proche Péremption → Périmé

7. **Planning**
   - États : Brouillon → Validé → Publié → En Cours → Terminé → Archivé

## Comment Utiliser ces Diagrammes

### Génération des images avec PlantUML

#### Option 1 : En ligne
1. Aller sur http://www.plantuml.com/plantuml/uml/
2. Copier le code PlantUML depuis les fichiers
3. Visualiser et télécharger l'image

#### Option 2 : Local (recommandé)
```bash
# Installation
sudo apt-get install plantuml graphviz

# Génération d'un diagramme
plantuml diagramme-classes.md

# Génération de tous les diagrammes
plantuml docs/diagrammes/*.md
```

#### Option 3 : Extension VS Code
1. Installer l'extension "PlantUML"
2. Ouvrir un fichier .md contenant du code PlantUML
3. Utiliser Alt+D pour prévisualiser

### Intégration dans la Documentation

Les diagrammes peuvent être :
- Exportés en PNG/SVG pour inclusion dans des documents
- Versionnés dans Git (le code source PlantUML)
- Mis à jour facilement en modifiant le code

## Structure des Fichiers

```
docs/
└── diagrammes/
    ├── README.md                      # Ce fichier
    ├── cas-utilisation.md            # Diagramme de cas d'utilisation
    ├── diagramme-classes.md          # Diagramme de classes
    ├── diagramme-sequence.md         # Diagrammes de séquence
    ├── diagramme-activite.md         # Diagrammes d'activité
    └── diagramme-etat.md             # Diagrammes d'état
```

## Conventions et Légendes

### Diagramme de Cas d'Utilisation
- **Rectangle** : Système
- **Bonhomme** : Acteur
- **Ellipse** : Cas d'utilisation
- **Flèche pleine** : Association
- **Flèche pointillée <<include>>** : Relation d'inclusion
- **Flèche pointillée <<extend>>** : Relation d'extension

### Diagramme de Classes
- **Classe** : Rectangle à 3 compartiments (nom, attributs, méthodes)
- **Association** : Ligne pleine avec cardinalités
- **Composition** : Losange noir
- **Agrégation** : Losange blanc
- **Héritage** : Flèche triangle vide

### Diagramme de Séquence
- **Acteur/Participant** : Rectangle
- **Message synchrone** : Flèche pleine
- **Message asynchrone** : Flèche pointillée
- **Activation** : Rectangle vertical
- **Alt/Opt/Loop** : Fragments d'interaction

### Diagramme d'Activité
- **Cercle noir** : Début
- **Cercle noir cerclé** : Fin
- **Rectangle arrondi** : Activité
- **Losange** : Décision
- **Barres épaisses** : Fork/Join (parallélisme)

### Diagramme d'État
- **Rectangle arrondi** : État
- **Flèche** : Transition
- **Cercle noir** : État initial
- **Cercle noir cerclé** : État final

## Mapping Code ↔ Diagrammes

### Classes du Diagramme → Code Java
```
Produit (diagramme) → Produit.java (backend/src/main/java/.../model/produit/)
Stock (diagramme) → Stock.java
Transaction (diagramme) → Transaction.java (backend/src/main/java/.../model/caisse/)
```

### Cas d'Utilisation → Controllers
```
"Consulter stocks" → StockController.getAllStocks()
"Effectuer transaction" → TransactionController.creerTransaction()
"Gérer plannings" → PlanningController.creerPlanning()
```

### États → Enums
```
StatutTransaction → StatutTransaction.java
StatutCommande → StatutCommande.java
NiveauFidelite → NiveauFidelite.java
```

## Maintenance des Diagrammes

### Quand Mettre à Jour

1. **Ajout de fonctionnalité**
   - Mettre à jour le diagramme de cas d'utilisation
   - Ajouter les nouvelles classes
   - Créer les séquences et activités

2. **Modification du modèle**
   - Mettre à jour le diagramme de classes
   - Vérifier les impacts sur les séquences

3. **Changement de workflow**
   - Mettre à jour les diagrammes d'activité
   - Vérifier les diagrammes d'état

### Processus de Mise à Jour

1. Modifier le code PlantUML dans le fichier .md
2. Régénérer les images
3. Versionner dans Git
4. Mettre à jour la documentation associée

## Cohérence avec l'Implémentation

### Vérifications
- ✅ Toutes les classes du diagramme existent dans le code
- ✅ Les relations (associations, compositions) sont respectées
- ✅ Les états correspondent aux enums définis
- ✅ Les séquences reflètent les appels réels entre services

### Écarts Connus
Aucun écart significatif identifié. Les diagrammes sont synchronisés avec l'implémentation actuelle.

## Ressources

- **PlantUML** : https://plantuml.com/
- **Guide PlantUML** : https://plantuml.com/guide
- **UML 2.5 Specification** : https://www.omg.org/spec/UML/
- **Documentation Projet** : ../README.md

## Exemples d'Utilisation

### Pour les Développeurs
- Comprendre l'architecture avant de coder
- Vérifier les dépendances entre modules
- Implémenter les workflows correctement

### Pour les Testeurs
- Identifier les scénarios de test
- Vérifier les transitions d'état
- Tester les cas limites

### Pour les Chefs de Projet
- Présenter le système aux parties prenantes
- Estimer la complexité des fonctionnalités
- Planifier les développements

### Pour la Documentation Utilisateur
- Illustrer les processus métier
- Expliquer les workflows
- Former les utilisateurs

---

*Dernière mise à jour : 09 Octobre 2025*
*Version : 1.0*
*Auteur : Équipe Supermarket Manager*

