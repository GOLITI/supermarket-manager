  
  class StatistiqueVente {
    -produitId: Long
    -produitNom: String
    -quantiteVendue: Integer
    -chiffreAffaires: BigDecimal
    -nombreVentes: Integer
  }
  
  enum TypeRapport {
    JOURNALIER
    HEBDOMADAIRE
    MENSUEL
    ANNUEL
  }
}

' ==================== RELATIONS INTER-MODULES ====================
Employe "1" -- "n" Transaction : effectue >
Client "1" -- "n" Transaction : achete >
Transaction "n" -- "1" Produit : contient >

@enduml
```

## Description des Classes Principales

### 1. Produit
Représente un article vendu dans le supermarché
- Attributs: nom, prix, référence, catégorie
- Méthodes: calculer marge, appliquer promotion

### 2. Stock
Gère les niveaux de stock par entrepôt
- Alertes automatiques quand quantité < seuil
- Traçabilité des entrées/sorties

### 3. Transaction
Représente une vente en caisse
- Lignes de transaction avec produits
- Paiements multiples possibles
- Statuts: EN_COURS, COMPLETEE, ANNULEE

### 4. Client et CarteFidelite
Gestion de la fidélisation
- Points de fidélité
- Niveaux (Bronze, Argent, Or, Platine)
- Historique des achats

### 5. Employe et Planning
Gestion des ressources humaines
- Plannings hebdomadaires
- Gestion des absences
- Postes et salaires

### 6. Commande et Fournisseur
Gestion des approvisionnements
- Commandes aux fournisseurs
- Suivi des livraisons
- Gestion des relations fournisseurs

## Patterns de Conception Utilisés

### 1. Repository Pattern
- Interface entre la couche métier et la base de données
- Ex: ProductRepository, StockRepository

### 2. Service Pattern
- Logique métier dans les services
- Ex: StockService, TransactionService

### 3. DTO Pattern
- Transfer d'objets entre couches
- Ex: ProduitDTO, TransactionDTO

### 4. Builder Pattern (Lombok)
- Construction d'objets complexes
- Utilisé via @Builder

### 5. Observer Pattern
- Alertes de stock automatiques
- Notifications d'absences

## Relations Importantes

### Cardinalités
- Un Produit peut avoir plusieurs Stocks (un par entrepôt)
- Un Client a au plus une CarteFidelite
- Une Transaction a plusieurs LignesTransaction
- Un Employe peut avoir plusieurs Shifts dans un Planning
- Une Commande concerne un seul Fournisseur

### Héritage
Pas d'héritage complexe, préférence pour la composition

### Agrégation vs Composition
- **Composition**: LigneTransaction fait partie de Transaction
- **Agrégation**: Produit est lié à Stock mais existe indépendamment
# Diagramme de Classes - Supermarket Manager

## Vue d'ensemble du système

Le système est organisé en plusieurs modules principaux :
- Gestion des Produits et Stocks
- Gestion des Caisses et Transactions
- Gestion des Ressources Humaines
- Gestion des Clients et Fidélité
- Gestion des Fournisseurs et Commandes
- Dashboard et Reporting

## Diagramme de Classes PlantUML

```plantuml
@startuml

' ==================== MODULE PRODUITS ET STOCKS ====================
package "Module Produits et Stocks" {
  
  class Produit {
    -id: Long
    -nom: String
    -description: String
    -reference: String
    -prixAchat: BigDecimal
    -prixVente: BigDecimal
    -tva: BigDecimal
    -actif: Boolean
    -dateCreation: LocalDateTime
    -dateModification: LocalDateTime
    +calculerMarge(): BigDecimal
    +appliquerPromotion(promo: Promotion): void
  }
  
  class Categorie {
    -id: Long
    -nom: String
    -description: String
    -code: String
    -actif: Boolean
  }
  
  class Stock {
    -id: Long
    -quantite: Integer
    -seuilReapprovisionnement: Integer
    -quantiteMaximale: Integer
    -datePeremption: LocalDate
    -alerteActive: Boolean
    -dateCreation: LocalDateTime
    +ajouterQuantite(qte: Integer): void
    +retirerQuantite(qte: Integer): void
    +verifierAlerte(): Boolean
  }
  
  class Entrepot {
    -id: Long
    -code: String
    -nom: String
    -adresse: String
    -capaciteMax: Double
    -actif: Boolean
  }
  
  enum TypeProduit {
    ALIMENTAIRE
    NON_ALIMENTAIRE
    FRAIS
    SURGELE
    BOISSON
  }
  
  Produit "1" *-- "1" Categorie
  Produit "1" -- "1" TypeProduit
  Stock "n" -- "1" Produit
  Stock "n" -- "1" Entrepot
}

' ==================== MODULE CAISSES ====================
package "Module Caisses" {
  
  class Caisse {
    -id: Long
    -numero: String
    -nom: String
    -actif: Boolean
    -dateCreation: LocalDateTime
  }
  
  class SeanceCaisse {
    -id: Long
    -fondCaisse: BigDecimal
    -dateOuverture: LocalDateTime
    -dateFermeture: LocalDateTime
    -montantAttendu: BigDecimal
    -montantReel: BigDecimal
    -statut: StatutSeance
    +ouvrirSeance(): void
    +fermerSeance(): void
    +calculerEcart(): BigDecimal
  }
  
  class Transaction {
    -id: Long
    -numero: String
    -dateHeure: LocalDateTime
    -montantTotal: BigDecimal
    -montantPaye: BigDecimal
    -montantRendu: BigDecimal
    -statut: StatutTransaction
    +calculerTotal(): BigDecimal
    +appliquerRemise(remise: BigDecimal): void
    +valider(): void
  }
  
  class LigneTransaction {
    -id: Long
    -quantite: Integer
    -prixUnitaire: BigDecimal
    -remise: BigDecimal
    -sousTotal: BigDecimal
    +calculerSousTotal(): BigDecimal
  }
  
  class Paiement {
    -id: Long
    -montant: BigDecimal
    -typePaiement: TypePaiement
    -reference: String
    -dateHeure: LocalDateTime
    -statut: StatutPaiement
  }
  
  enum StatutSeance {
    OUVERTE
    FERMEE
    SUSPENDUE
  }
  
  enum StatutTransaction {
    EN_COURS
    COMPLETEE
    ANNULEE
  }
  
  enum TypePaiement {
    ESPECES
    CARTE_BANCAIRE
    MOBILE_MONEY
    CHEQUE
  }
  
  enum StatutPaiement {
    EN_ATTENTE
    VALIDE
    REFUSE
    ANNULE
  }
  
  SeanceCaisse "n" -- "1" Caisse
  Transaction "n" -- "1" SeanceCaisse
  Transaction "1" *-- "n" LigneTransaction
  Transaction "1" *-- "1..*" Paiement
  LigneTransaction "n" -- "1" Produit
  Transaction "n" -- "0..1" Client : client >
}

' ==================== MODULE RH ====================
package "Module Ressources Humaines" {
  
  class Employe {
    -id: Long
    -matricule: String
    -nom: String
    -prenom: String
    -email: String
    -telephone: String
    -poste: Poste
    -dateEmbauche: LocalDate
    -salaire: BigDecimal
    -actif: Boolean
    +calculerHeuresTravaillees(): Integer
    +calculerSalaire(): BigDecimal
  }
  
  class Planning {
    -id: Long
    -semaine: Integer
    -annee: Integer
    -dateDebut: LocalDate
    -dateFin: LocalDate
    -statut: StatutPlanning
    +valider(): void
    +publier(): void
  }
  
  class Shift {
    -id: Long
    -date: LocalDate
    -heureDebut: LocalTime
    -heureFin: LocalTime
    -poste: String
    +calculerDuree(): Integer
  }
  
  class Absence {
    -id: Long
    -dateDebut: LocalDate
    -dateFin: LocalDate
    -typeAbsence: TypeAbsence
    -motif: String
    -statut: StatutDemande
    -dateValidation: LocalDateTime
    +duree(): Integer
    +valider(): void
    +rejeter(): void
  }
  
  enum Poste {
    CAISSIER
    GESTIONNAIRE_STOCK
    MANAGER
    VENDEUR
    RESPONSABLE_RAYON
  }
  
  enum TypeAbsence {
    CONGE_PAYE
    MALADIE
    CONGE_SANS_SOLDE
    FORMATION
  }
  
  enum StatutDemande {
    EN_ATTENTE
    APPROUVEE
    REJETEE
  }
  
  enum StatutPlanning {
    BROUILLON
    VALIDE
    PUBLIE
  }
  
  Planning "n" -- "1" Employe
  Planning "1" *-- "n" Shift
  Absence "n" -- "1" Employe
  SeanceCaisse "n" -- "1" Employe : caissier >
}

' ==================== MODULE CLIENTS ====================
package "Module Clients et Fidélité" {
  
  class Client {
    -id: Long
    -nom: String
    -prenom: String
    -email: String
    -telephone: String
    -dateNaissance: LocalDate
    -dateInscription: LocalDateTime
    -actif: Boolean
    +calculerTotalAchats(): BigDecimal
  }
  
  class CarteFidelite {
    -id: Long
    -numero: String
    -points: Integer
    -niveauFidelite: NiveauFidelite
    -dateCreation: LocalDateTime
    -dateExpiration: LocalDate
    -active: Boolean
    +crediterPoints(pts: Integer): void
    +debiterPoints(pts: Integer): void
    +verifierExpiration(): Boolean
  }
  
  class HistoriqueAchat {
    -id: Long
    -montantTotal: BigDecimal
    -pointsGagnes: Integer
    -dateAchat: LocalDateTime
    -nombreArticles: Integer
  }
  
  class Promotion {
    -id: Long
    -nom: String
    -description: String
    -typePromotion: TypePromotion
    -valeurRemise: BigDecimal
    -dateDebut: LocalDate
    -dateFin: LocalDate
    -actif: Boolean
    +estValide(): Boolean
    +calculerRemise(montant: BigDecimal): BigDecimal
  }
  
  enum NiveauFidelite {
    BRONZE
    ARGENT
    OR
    PLATINE
  }
  
  enum TypePromotion {
    POURCENTAGE
    MONTANT_FIXE
    DEUX_POUR_UN
    POINTS_DOUBLES
  }
  
  Client "1" -- "0..1" CarteFidelite
  Client "1" *-- "n" HistoriqueAchat
  Promotion "n" -- "n" Produit
  Transaction "n" -- "0..1" Promotion
}

' ==================== MODULE FOURNISSEURS ====================
package "Module Fournisseurs et Commandes" {
  
  class Fournisseur {
    -id: Long
    -nom: String
    -email: String
    -telephone: String
    -adresse: String
    -typeFournisseur: TypeFournisseur
    -actif: Boolean
    +calculerTotalCommandes(): BigDecimal
  }
  
  class Commande {
    -id: Long
    -numero: String
    -dateCommande: LocalDateTime
    -dateLivraisonPrevue: LocalDate
    -dateLivraisonReelle: LocalDate
    -montantTotal: BigDecimal
    -statut: StatutCommande
    +calculerTotal(): BigDecimal
    +valider(): void
    +livrer(): void
  }
  
  class LigneCommande {
    -id: Long
    -quantite: Integer
    -prixUnitaire: BigDecimal
    -sousTotal: BigDecimal
    +calculerSousTotal(): BigDecimal
  }
  
  enum TypeFournisseur{
    ALIMENTAIRE
    NON_ALIMENTAIRE
    MIXTE
  }
  
  enum StatutCommande {
    BROUILLON
    VALIDEE
    EN_COURS
    LIVREE
    ANNULEE
  }
  
  Commande "n" -- "1" Fournisseur
  Commande "1" *-- "n" LigneCommande
  LigneCommande "n" -- "1" Produit
  Produit "n" -- "n" Fournisseur
}

' ==================== MODULE DASHBOARD ====================
package "Module Dashboard et Reporting" {
  
  class Dashboard {
    -periode: String
    -chiffreAffaires: BigDecimal
    -nombreTransactions: Integer
    -panierMoyen: BigDecimal
    +calculerCA(): BigDecimal
    +calculerPanierMoyen(): BigDecimal
  }
  
  class RapportVente {
    -id: Long
    -dateDebut: LocalDate
    -dateFin: LocalDate
    -chiffreAffaires: BigDecimal
    -nombreVentes: Integer
    -typeRapport: TypeRapport
    +generer(): void
  }

