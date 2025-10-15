- Création avec numéro unique
- Utilisation lors des achats
- Calcul et crédit automatique de points
- Changement automatique de niveau
# Diagramme de Séquence - Supermarket Manager

## 1. Séquence: Effectuer une Transaction de Vente

```plantuml
@startuml
actor Caissier
participant "Interface Caisse" as UI
participant TransactionController
participant TransactionService
participant StockService
participant CarteFideliteService
participant PaiementService
database Database

Caissier -> UI: Scanner produit
UI -> TransactionController: ajouterProduit(produitId, quantite)
TransactionController -> TransactionService: ajouterLigneTransaction(transactionId, produitId, quantite)

TransactionService -> StockService: verifierDisponibilite(produitId, quantite)
StockService -> Database: SELECT stock WHERE produitId
Database --> StockService: stock
alt Stock suffisant
    StockService --> TransactionService: OK
    TransactionService -> Database: INSERT ligne_transaction
    TransactionService --> TransactionController: LigneTransactionDTO
    TransactionController --> UI: Afficher ligne
    UI --> Caissier: Produit ajouté
else Stock insuffisant
    StockService --> TransactionService: StockInsuffisantException
    TransactionService --> TransactionController: Exception
    TransactionController --> UI: Erreur
    UI --> Caissier: Stock insuffisant
end

Caissier -> UI: Finaliser transaction
UI -> TransactionController: finaliserTransaction(transactionId, paiements)

TransactionController -> TransactionService: calculerTotal(transactionId)
TransactionService -> Database: SELECT lignes_transaction
TransactionService --> TransactionController: montantTotal

loop Pour chaque paiement
    TransactionController -> PaiementService: traiterPaiement(paiement)
    PaiementService -> Database: INSERT paiement
    PaiementService --> TransactionController: PaiementDTO
end

alt Client avec carte fidélité
    TransactionController -> CarteFideliteService: crediterPoints(clientId, montant)
    CarteFideliteService -> Database: UPDATE carte_fidelite
    CarteFideliteService --> TransactionController: Points crédités
end

TransactionController -> StockService: deduireStock(lignesTransaction)
loop Pour chaque ligne
    StockService -> Database: UPDATE stock SET quantite = quantite - ?
end
StockService --> TransactionController: Stock mis à jour

TransactionController -> TransactionService: validerTransaction(transactionId)
TransactionService -> Database: UPDATE transaction SET statut = 'COMPLETEE'
TransactionService --> TransactionController: TransactionDTO

TransactionController --> UI: Transaction validée
UI --> Caissier: Imprimer reçu

@enduml
```

## 2. Séquence: Réapprovisionnement Automatique

```plantuml
@startuml
participant StockAlertScheduler
participant StockService
participant CommandeService
participant EmailService
participant FournisseurService
database Database

StockAlertScheduler -> StockService: verifierAlertes()
activate StockService

StockService -> Database: SELECT stocks WHERE quantite <= seuil
Database --> StockService: List<Stock>

loop Pour chaque stock en alerte
    StockService -> StockService: calculerQuantiteRecommandee()
    
    StockService -> FournisseurService: trouverMeilleurFournisseur(produitId)
    FournisseurService -> Database: SELECT fournisseurs
    FournisseurService --> StockService: Fournisseur
    
    StockService -> CommandeService: creerCommandeAutomatique(produit, fournisseur, quantite)
    CommandeService -> Database: INSERT commande
    CommandeService -> Database: INSERT ligne_commande
    CommandeService --> StockService: CommandeDTO
    
    StockService -> EmailService: envoyerNotificationAlerte(stock, commande)
    EmailService -> EmailService: composerEmail()
    EmailService --> StockService: Email envoyé
end

StockService --> StockAlertScheduler: Alertes traitées
deactivate StockService

@enduml
```

## 3. Séquence: Gestion des Absences

```plantuml
@startuml
actor Employe
actor ResponsableRH
participant AbsenceController
participant AbsenceService
participant PlanningService
participant NotificationService
database Database

Employe -> AbsenceController: demanderAbsence(absenceRequest)
activate AbsenceController

AbsenceController -> AbsenceService: creerDemandeAbsence(employeId, demandeDTO)
activate AbsenceService

AbsenceService -> Database: SELECT employe WHERE id = ?
Database --> AbsenceService: Employe

AbsenceService -> AbsenceService: verifierDisponibilite(dates)
AbsenceService -> PlanningService: verifierConflits(employeId, dateDebut, dateFin)
PlanningService -> Database: SELECT shifts WHERE employe_id AND date BETWEEN ? AND ?
PlanningService --> AbsenceService: List<Shift>

alt Pas de conflits
    AbsenceService -> Database: INSERT absence
    Database --> AbsenceService: Absence créée
    
    AbsenceService -> NotificationService: notifierRH(absence)
    NotificationService -> Database: SELECT responsableRH
    NotificationService -> NotificationService: envoyerEmail(RH, absence)
    NotificationService --> AbsenceService: Notification envoyée
    
    AbsenceService --> AbsenceController: AbsenceDTO
    AbsenceController --> Employe: Demande enregistrée
else Conflits détectés
    AbsenceService --> AbsenceController: ConflitPlanningException
    AbsenceController --> Employe: Erreur: conflits
end

deactivate AbsenceService
deactivate AbsenceController

== Validation par RH ==

ResponsableRH -> AbsenceController: validerAbsence(absenceId, decision)
activate AbsenceController

AbsenceController -> AbsenceService: validerAbsence(absenceId, approuve)
activate AbsenceService

AbsenceService -> Database: SELECT absence WHERE id = ?
Database --> AbsenceService: Absence

alt Approuvée
    AbsenceService -> Database: UPDATE absence SET statut = 'APPROUVEE'
    AbsenceService -> PlanningService: supprimerShifts(employeId, dateDebut, dateFin)
    PlanningService -> Database: DELETE shifts
    PlanningService --> AbsenceService: Shifts supprimés
else Rejetée
    AbsenceService -> Database: UPDATE absence SET statut = 'REJETEE'
end

AbsenceService -> NotificationService: notifierEmploye(employe, decision)
NotificationService -> NotificationService: envoyerEmail(employe, decision)
NotificationService --> AbsenceService: Notification envoyée

AbsenceService --> AbsenceController: AbsenceDTO
AbsenceController --> ResponsableRH: Absence validée

deactivate AbsenceService
deactivate AbsenceController

@enduml
```

## 4. Séquence: Ouverture/Fermeture de Caisse

```plantuml
@startuml
actor Caissier
participant SeanceCaisseController
participant SeanceCaisseService
participant CaisseService
database Database

== Ouverture de Séance ==

Caissier -> SeanceCaisseController: ouvrirSeance(caisseId, fondCaisse)
activate SeanceCaisseController

SeanceCaisseController -> SeanceCaisseService: ouvrirSeance(caisseId, employeId, fondCaisse)
activate SeanceCaisseService

SeanceCaisseService -> CaisseService: verifierDisponibilite(caisseId)
CaisseService -> Database: SELECT seances WHERE caisse_id AND statut = 'OUVERTE'
Database --> CaisseService: List<SeanceCaisse>

alt Caisse disponible
    CaisseService --> SeanceCaisseService: OK
    
    SeanceCaisseService -> Database: INSERT seance_caisse
    Database --> SeanceCaisseService: Séance créée
    
    SeanceCaisseService --> SeanceCaisseController: SeanceCaisseDTO
    SeanceCaisseController --> Caissier: Séance ouverte
else Caisse déjà ouverte
    CaisseService --> SeanceCaisseService: CaisseDejaOuverteException
    SeanceCaisseService --> SeanceCaisseController: Exception
    SeanceCaisseController --> Caissier: Erreur
end

deactivate SeanceCaisseService
deactivate SeanceCaisseController

== Fermeture de Séance ==

Caissier -> SeanceCaisseController: fermerSeance(seanceId, montantReel)
activate SeanceCaisseController

SeanceCaisseController -> SeanceCaisseService: fermerSeance(seanceId, montantReel)
activate SeanceCaisseService

SeanceCaisseService -> Database: SELECT seance WHERE id = ?
Database --> SeanceCaisseService: SeanceCaisse

SeanceCaisseService -> Database: SELECT SUM(montant_total) FROM transactions WHERE seance_id = ?
Database --> SeanceCaisseService: montantAttendu

SeanceCaisseService -> SeanceCaisseService: calculerEcart(montantAttendu, montantReel)

SeanceCaisseService -> Database: UPDATE seance_caisse SET statut = 'FERMEE', montant_reel = ?, ecart = ?
Database --> SeanceCaisseService: Séance fermée

alt Écart significatif
    SeanceCaisseService -> SeanceCaisseService: genererAlerte(ecart)
    SeanceCaisseService -> Database: INSERT alerte_ecart_caisse
end

SeanceCaisseService --> SeanceCaisseController: SeanceCaisseDTO avec écart
SeanceCaisseController --> Caissier: Séance fermée (Écart: X)

deactivate SeanceCaisseService
deactivate SeanceCaisseController

@enduml
```

## 5. Séquence: Consultation Dashboard

```plantuml
@startuml
actor Manager
participant DashboardController
participant DashboardService
participant TransactionService
participant StockService
participant EmployeService
database Database

Manager -> DashboardController: getDashboard(periode)
activate DashboardController

DashboardController -> DashboardService: genererDashboard(dateDebut, dateFin)
activate DashboardService

par Chiffre d'affaires
    DashboardService -> TransactionService: calculerCA(dateDebut, dateFin)
    TransactionService -> Database: SELECT SUM(montant_total) FROM transactions
    TransactionService --> DashboardService: chiffreAffaires
and Nombre de transactions
    DashboardService -> TransactionService: compterTransactions(dateDebut, dateFin)
    TransactionService -> Database: SELECT COUNT(*) FROM transactions
    TransactionService --> DashboardService: nombreTransactions
and Produits les plus vendus
    DashboardService -> TransactionService: getProduitsLesPlusVendus(dateDebut, dateFin, limit)
    TransactionService -> Database: SELECT produit, SUM(quantite) ... GROUP BY produit ORDER BY ... LIMIT ?
    TransactionService --> DashboardService: List<ProduitVente>
and Alertes stock
    DashboardService -> StockService: getStocksEnAlerte()
    StockService -> Database: SELECT * FROM stocks WHERE quantite <= seuil
    StockService --> DashboardService: List<StockAlerte>
and Présences employés
    DashboardService -> EmployeService: getTauxPresence(dateDebut, dateFin)
    EmployeService -> Database: SELECT COUNT(*) FROM shifts ...
    EmployeService --> DashboardService: tauxPresence
end

DashboardService -> DashboardService: construireDashboardDTO()
DashboardService --> DashboardController: DashboardDTO

DashboardController --> Manager: Afficher dashboard

deactivate DashboardService
deactivate DashboardController

@enduml
```

## 6. Séquence: Gestion Carte Fidélité

```plantuml
@startuml
actor Client
actor Caissier
participant CarteFideliteController
participant CarteFideliteService
participant ClientService
database Database

== Création carte ==

Client -> Caissier: Demande carte fidélité
Caissier -> CarteFideliteController: creerCarteFidelite(clientId)
activate CarteFideliteController

CarteFideliteController -> ClientService: verifierClient(clientId)
ClientService -> Database: SELECT client WHERE id = ?
ClientService --> CarteFideliteController: Client

CarteFideliteController -> CarteFideliteService: creerCarte(clientId)
activate CarteFideliteService

CarteFideliteService -> CarteFideliteService: genererNumeroCarte()
CarteFideliteService -> Database: INSERT carte_fidelite
Database --> CarteFideliteService: Carte créée

CarteFideliteService --> CarteFideliteController: CarteFideliteDTO
CarteFideliteController --> Caissier: Carte créée
Caissier --> Client: Carte remise

deactivate CarteFideliteService
deactivate CarteFideliteController

== Utilisation lors d'un achat ==

Client -> Caissier: Présente carte (transaction en cours)
Caissier -> CarteFideliteController: scannerCarte(numeroCarte)
activate CarteFideliteController

CarteFideliteController -> CarteFideliteService: getCarte(numeroCarte)
activate CarteFideliteService

CarteFideliteService -> Database: SELECT carte WHERE numero = ?
Database --> CarteFideliteService: CarteFidelite

alt Carte valide et active
    CarteFideliteService --> CarteFideliteController: CarteFideliteDTO
    CarteFideliteController --> Caissier: Client identifié (points: X)
    
    note over Caissier: Transaction finalisée
    
    Caissier -> CarteFideliteController: crediterPoints(carteId, montantAchat)
    CarteFideliteController -> CarteFideliteService: crediterPoints(carteId, points)
    
    CarteFideliteService -> CarteFideliteService: calculerPoints(montantAchat)
    CarteFideliteService -> Database: UPDATE carte_fidelite SET points = points + ?
    CarteFideliteService -> Database: INSERT historique_achat
    
    CarteFideliteService -> CarteFideliteService: verifierChangementNiveau()
    alt Changement de niveau
        CarteFideliteService -> Database: UPDATE carte_fidelite SET niveau = ?
    end
    
    CarteFideliteService --> CarteFideliteController: Nouveau solde points
    CarteFideliteController --> Caissier: Points crédités
    Caissier --> Client: Vous avez X points
else Carte expirée
    CarteFideliteService --> CarteFideliteController: CarteExpireeException
    CarteFideliteController --> Caissier: Carte expirée
    Caissier --> Client: Carte à renouveler
end

deactivate CarteFideliteService
deactivate CarteFideliteController

@enduml
```

## Description des Séquences

### 1. Transaction de Vente
- Scan de produits avec vérification stock
- Application de promotions
- Traitement de paiements multiples
- Crédit de points fidélité
- Déduction automatique du stock

### 2. Réapprovisionnement
- Vérification automatique programmée (scheduler)
- Détection des stocks sous le seuil
- Création automatique de commandes
- Notification par email

### 3. Gestion Absences
- Demande employé avec vérification conflits
- Notification RH
- Validation/Rejet
- Mise à jour planning

### 4. Ouverture/Fermeture Caisse
- Déclaration fond de caisse
- Calcul écart attendu/réel
- Génération d'alertes si écart important

### 5. Dashboard
- Requêtes parallèles pour performance
- Agrégation de données multiples
- Calculs de KPIs en temps réel

### 6. Carte Fidélité

