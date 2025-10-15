# Diagramme de Cas d'Utilisation - Supermarket Manager

## Acteurs

### 1. Gestionnaire de Stock
- Consulter les stocks
- Ajouter/Retirer des quantites
- Gerer les alertes de stock
- Consulter les produits proches de peremption
- Passer des commandes fournisseurs
- Gerer les entrepots

### 2. Caissier
- Ouvrir/Fermer une seance de caisse
- Effectuer des transactions
- Appliquer des promotions
- Gerer les paiements (especes, carte, mobile)
- Imprimer les recus
- Consulter les transactions

### 3. Responsable RH
- Gerer les employes (CRUD)
- Creer/Modifier les plannings
- Gerer les absences/conges
- Consulter les performances
- Valider les demandes d'absence

### 4. Responsable Marketing
- Gerer les clients
- Gerer les cartes de fidelite
- Consulter l'historique des achats
- Creer des promotions
- Analyser les comportements clients

### 5. Manager/Directeur
- Consulter le dashboard
- Analyser les ventes
- Consulter les rapports financiers
- Analyser les produits les plus vendus
- Consulter les heures de pointe
- Gerer les marges par categorie

### 6. Responsable Fournisseurs
- Gerer les fournisseurs (CRUD)
- Passer des commandes
- Suivre les livraisons
- Gerer les relations fournisseurs

## Cas d'Utilisation Principaux

### Module Gestion des Stocks
1. **Consulter les stocks**
   - Acteur: Gestionnaire de Stock
   - Precondition: Etre authentifie
   - Postcondition: Liste des stocks affichee

2. **Gerer les alertes de stock**
   - Acteur: Gestionnaire de Stock
   - Description: Recevoir des alertes quand stock < seuil

3. **Passer une commande fournisseur**
   - Acteur: Gestionnaire de Stock, Responsable Fournisseurs
   - Include: Selectionner fournisseur, Ajouter produits

### Module Caisses
4. **Effectuer une transaction**
   - Acteur: Caissier
   - Precondition: Seance de caisse ouverte
   - Include: Scanner produits, Calculer total, Traiter paiement

5. **Gerer une seance de caisse**
   - Acteur: Caissier
   - Description: Ouvrir/Fermer la caisse, Declarer fond de caisse

### Module RH
6. **Gerer les plannings**
   - Acteur: Responsable RH
   - Description: Creer/Modifier plannings hebdomadaires

7. **Gerer les absences**
   - Acteur: Responsable RH, Employe
   - Description: Demander/Valider conges et absences

### Module Clients et Fidelite
8. **Gerer carte de fidelite**
   - Acteur: Responsable Marketing, Caissier
   - Description: Creer carte, Crediter/Debiter points

9. **Analyser comportements clients**
   - Acteur: Responsable Marketing
   - Description: Consulter historique, Identifier preferences

### Module Dashboard et Reporting
10. **Consulter le dashboard**
    - Acteur: Manager
    - Description: Vue d'ensemble des KPIs

11. **Generer des rapports**
    - Acteur: Manager
    - Description: Rapports ventes, stocks, RH

### Module Fournisseurs
12. **Gerer les fournisseurs**
    - Acteur: Responsable Fournisseurs
    - Description: CRUD fournisseurs, Suivre commandes

## Relations entre Cas d'Utilisation

### Relations Include
- "Effectuer une transaction" **include** "Calculer total"
- "Effectuer une transaction" **include** "Traiter paiement"
- "Passer commande fournisseur" **include** "Selectionner fournisseur"
- "Gerer carte fidelite" **include** "Consulter client"

### Relations Extend
- "Appliquer promotion" **extend** "Effectuer une transaction"
- "Generer alerte stock" **extend** "Consulter stocks"
- "Imprimer recu" **extend** "Effectuer une transaction"

## Diagramme PlantUML

```plantuml
@startuml
left to right direction
skinparam packageStyle rectangle

actor "Gestionnaire Stock" as GS
actor "Caissier" as C
actor "Responsable RH" as RH
actor "Responsable Marketing" as RM
actor "Manager" as M
actor "Responsable Fournisseurs" as RF

rectangle "Systeme Supermarket Manager" {
  
  package "Module Stocks" {
    usecase "Consulter stocks" as UC1
    usecase "Gerer alertes stock" as UC2
    usecase "Ajouter/Retirer quantite" as UC3
    usecase "Consulter peremption" as UC4
  }
  
  package "Module Caisses" {
    usecase "Effectuer transaction" as UC5
    usecase "Gerer seance caisse" as UC6
    usecase "Appliquer promotion" as UC7
    usecase "Traiter paiement" as UC8
  }
  
  package "Module RH" {
    usecase "Gerer employes" as UC9
    usecase "Gerer plannings" as UC10
    usecase "Gerer absences" as UC11
    usecase "Consulter performances" as UC12
  }
  
  package "Module Clients" {
    usecase "Gerer clients" as UC13
    usecase "Gerer carte fidelite" as UC14
    usecase "Consulter historique achats" as UC15
    usecase "Analyser comportements" as UC16
  }
  
  package "Module Dashboard" {
    usecase "Consulter dashboard" as UC17
    usecase "Analyser ventes" as UC18
    usecase "Generer rapports" as UC19
    usecase "Consulter KPIs" as UC20
  }
  
  package "Module Fournisseurs" {
    usecase "Gerer fournisseurs" as UC21
    usecase "Passer commande" as UC22
    usecase "Suivre livraisons" as UC23
  }
}

' Relations Gestionnaire Stock
GS --> UC1
GS --> UC2
GS --> UC3
GS --> UC4
GS --> UC22

' Relations Caissier
C --> UC5
C --> UC6
C --> UC7
C --> UC14

' Relations Responsable RH
RH --> UC9
RH --> UC10
RH --> UC11
RH --> UC12

' Relations Responsable Marketing
RM --> UC13
RM --> UC14
RM --> UC15
RM --> UC16

' Relations Manager
M --> UC17
M --> UC18
M --> UC19
M --> UC20

' Relations Responsable Fournisseurs
RF --> UC21
RF --> UC22
RF --> UC23

' Relations Include/Extend
UC5 ..> UC8 : <<include>>
UC7 ..> UC5 : <<extend>>

@enduml
```

## Flux Principaux

### Flux 1: Transaction de vente
1. Caissier ouvre seance
2. Caissier scanne produits
3. Systeme calcule total
4. Client paye
5. Systeme met a jour stock
6. Systeme credite points fidelite
7. Systeme imprime recu

### Flux 2: Reapprovisionnement
1. Systeme detecte stock faible
2. Systeme genere alerte
3. Gestionnaire consulte alertes
4. Gestionnaire passe commande
5. Fournisseur livre
6. Gestionnaire met a jour stock

### Flux 3: Gestion absence
1. Employe demande conge
2. Systeme notifie RH
3. RH valide/refuse
4. Systeme met a jour planning
5. Systeme notifie employe

