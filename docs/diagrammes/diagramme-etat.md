- **En Cours**: Préparation par fournisseur
- **Livrée**: Marchandise reçue
- **Complétée**: Vérifiée et stock mis à jour

### Séance de Caisse
- **Fermée**: Caisse inactive
- **Ouverte**: Opérationnelle pour transactions
- **Suspendue**: Pause temporaire
- **En Clôture**: Comptage et fermeture

### Demande Absence
- **Brouillon**: Saisie en cours
- **En Attente**: Soumise, attente validation
- **Approuvée**: Validée par RH
- **Confirmée**: Absence en cours
- **Terminée/Rejetée/Annulée**: États finaux

### Carte Fidélité
- **Active**: Utilisable avec niveaux (Bronze → Platine)
- **Proche Expiration**: Rappel avant expiration
- **Expirée**: Nécessite renouvellement
- **Bloquée**: Temporairement désactivée
- **Annulée**: Fermée définitivement

### Stock
- **Normal**: Quantité suffisante
- **En Alerte**: Sous le seuil de réapprovisionnement
- **Critique**: Très faible (< 10% seuil)
- **Rupture**: Stock épuisé
- **Proche Péremption/Périmé**: États liés à la date

### Planning
- **Brouillon**: Création/Modification
- **En Validation**: Contrôles automatiques
- **Validé**: Approuvé, prêt à publier
- **Publié**: Visible par les employés
- **En Cours**: Semaine active
- **Terminé**: Semaine passée
- **Archivé**: Historique conservé
# Diagramme d'État - Supermarket Manager

## 1. États d'une Transaction

```plantuml
@startuml
[*] --> Initialisee : Créer transaction

Initialisee : Aucun produit ajouté
Initialisee : Montant = 0

Initialisee --> EnCours : Ajouter premier produit

EnCours : Produits ajoutés
EnCours : Calcul en cours
EnCours --> EnCours : Ajouter/Retirer produit
EnCours --> EnCours : Appliquer promotion
EnCours --> EnAttenteClient : Scanner carte fidélité

EnAttenteClient : Attente client
EnAttenteClient --> EnCours : Client identifié
EnAttenteClient --> EnCours : Continuer sans carte

EnCours --> EnAttentePaiement : Finaliser (montant > 0)

EnAttentePaiement : Montant total calculé
EnAttentePaiement : En attente paiement
EnAttentePaiement --> EnAttentePaiement : Paiement partiel
EnAttentePaiement --> Validee : Paiement complet

Validee : Transaction payée
Validee : Stock déduit
Validee : Points crédités
Validee --> [*]

EnCours --> Annulee : Annuler
EnAttentePaiement --> Annulee : Timeout/Annuler
Annulee : Transaction annulée
Annulee : Stock restauré
Annulee --> [*]

note right of EnAttentePaiement
  Timeout automatique
  après 5 minutes
end note

@enduml
```

## 2. États d'une Commande Fournisseur

```plantuml
@startuml
[*] --> Brouillon : Créer commande

Brouillon : Édition en cours
Brouillon : Montant provisoire
Brouillon --> Brouillon : Ajouter/Modifier ligne
Brouillon --> Validee : Valider commande
Brouillon --> Annulee : Supprimer

Validee : Bon de commande généré
Validee : Email envoyé au fournisseur
Validee --> EnCours : Confirmer réception fournisseur

EnCours : Commande en préparation
EnCours : Délai de livraison actif
EnCours --> EnCoursRetard : Dépasser date prévue

EnCoursRetard : Retard détecté
EnCoursRetard : Notification envoyée
EnCoursRetard --> EnCours : Mise à jour délai
EnCoursRetard --> PartiellementLivree : Livraison partielle
EnCoursRetard --> Livree : Livraison complète

EnCours --> PartiellementLivree : Livraison partielle

PartiellementLivree : Produits reçus partiellement
PartiellementLivree : Attente reste
PartiellementLivree --> Livree : Livraison complète
PartiellementLivree --> Annulee : Annuler reste

EnCours --> Livree : Livraison complète

Livree : Tous produits reçus
Livree --> EnVerification : Vérifier conformité

EnVerification : Contrôle qualité
EnVerification : Contrôle quantité
EnVerification --> Completee : Conforme
EnVerification --> EnLitige : Non conforme

EnLitige : Litige ouvert
EnLitige : Contact fournisseur
EnLitige --> Completee : Litige résolu
EnLitige --> Annulee : Retour marchandise

Completee : Stock mis à jour
Completee : Facture validée
Completee --> [*]

Annulee : Commande annulée
Annulee --> [*]

@enduml
```

## 3. États d'une Séance de Caisse

```plantuml
@startuml
[*] --> Fermee : État initial

Fermee : Caisse fermée
Fermee : Aucune transaction
Fermee --> Ouverte : Ouvrir séance\n(déclaration fond)

Ouverte : Caisse opérationnelle
Ouverte : Transactions possibles
Ouverte --> Ouverte : Effectuer transaction
Ouverte --> Suspendue : Suspendre (pause)
Ouverte --> EnCloture : Fermer séance

Suspendue : Pause active
Suspendue : Transactions bloquées
Suspendue --> Ouverte : Reprendre
Suspendue --> EnCloture : Forcer fermeture

EnCloture : Comptage en cours
EnCloture : Calcul écart
EnCloture --> Fermee : Clôture validée

Fermee --> Fermee : Générer rapport
Fermee --> [*] : Fin de journée

note right of EnCloture
  Écart calculé automatiquement
  Alerte si écart > 1%
end note

@enduml
```

## 4. États d'une Demande d'Absence

```plantuml
@startuml
[*] --> Brouillon : Commencer demande

Brouillon : Saisie en cours
Brouillon --> Brouillon : Modifier informations
Brouillon --> EnAttente : Soumettre
Brouillon --> [*] : Abandonner

EnAttente : Demande soumise
EnAttente : Attente validation RH
EnAttente : Notification RH envoyée

EnAttente --> EnExamen : RH commence examen

EnExamen : Examen par RH
EnExamen : Vérifications en cours
EnExamen --> EnAttente : Demander informations
EnExamen --> Approuvee : Approuver
EnExamen --> Rejetee : Rejeter

Approuvee : Absence validée
Approuvee : Planning mis à jour
Approuvee : Solde déduit
Approuvee --> Confirmee : Date début atteinte

Confirmee : Absence en cours
Confirmee --> Terminee : Date fin atteinte

Terminee : Absence terminée
Terminee : Employé de retour
Terminee --> [*]

Rejetee : Demande refusée
Rejetee : Motif communiqué
Rejetee --> [*]

Approuvee --> Annulee : Annuler (employé/RH)
Confirmee --> Annulee : Retour anticipé

Annulee : Absence annulée
Annulee : Planning restauré
Annulee --> [*]

note right of Approuvee
  Email de confirmation
  envoyé à l'employé
end note

@enduml
```

## 5. États d'une Carte de Fidélité

```plantuml
@startuml
[*] --> EnCreation : Demander carte

EnCreation : Saisie informations
EnCreation : Génération numéro
EnCreation --> Active : Activer carte

Active : Carte utilisable
Active : Niveau = BRONZE
Active : Points = 0

Active --> Active : Créditer points (achat)
Active --> Active : Débiter points (récompense)
Active --> Active : Upgrade niveau

state Active {
  [*] --> Bronze
  Bronze : 0-499 points
  Bronze --> Argent : >= 500 points
  
  Argent : 500-1499 points
  Argent --> Or : >= 1500 points
  
  Or : 1500-4999 points
  Or --> Platine : >= 5000 points
  
  Platine : >= 5000 points
}

Active --> ProcheExpiration : 30 jours avant expiration

ProcheExpiration : Notification envoyée
ProcheExpiration : Rappel client
ProcheExpiration --> Active : Renouveler
ProcheExpiration --> Expiree : Date expiration atteinte

Expiree : Carte expirée
Expiree : Transactions bloquées
Expiree : Points gelés
Expiree --> Active : Renouveler (90 jours max)
Expiree --> Archivee : Dépasser délai

Active --> Bloquee : Bloquer (fraude/demande)

Bloquee : Carte désactivée
Bloquee : Enquête en cours
Bloquee --> Active : Débloquer
Bloquee --> Annulee : Fermer définitivement

Annulee : Carte annulée
Annulee : Points perdus
Annulee --> [*]

Archivee : Carte archivée
Archivee : Historique conservé
Archivee --> [*]

note right of ProcheExpiration
  Email de rappel envoyé
  30 jours avant expiration
end note

@enduml
```

## 6. États d'un Stock

```plantuml
@startuml
[*] --> Normal : Créer stock

Normal : Quantité > seuil
Normal : Pas d'alerte
Normal : Ventes normales

Normal --> Normal : Entrée marchandise
Normal --> Normal : Sortie (vente)

Normal --> EnAlerte : Quantité <= seuil

EnAlerte : Stock faible
EnAlerte : Alerte générée
EnAlerte : Notification envoyée

EnAlerte --> Normal : Réapprovisionner
EnAlerte --> Critique : Quantité très faible

Critique : Quantité < 10% seuil
Critique : Alerte urgente
Critique : Commande automatique

Critique --> EnAlerte : Réapprovisionner
Critique --> Rupture : Quantité = 0

Rupture : Stock épuisé
Rupture : Ventes bloquées
Rupture : Manque à gagner

Rupture --> EnAlerte : Réapprovisionner
Rupture --> [*] : Arrêt produit

Normal --> ProchePeremption : Date péremption < 30j

ProchePeremption : Péremption proche
ProchePeremption : Promotion suggérée
ProchePeremption : Vente rapide

ProchePeremption --> Normal : Écouler stock
ProchePeremption --> Perime : Date dépassée

Perime : Produit périmé
Perime : Retrait obligatoire
Perime --> [*] : Détruire

state EnAlerte {
  [*] --> AlerteEnvoyee
  AlerteEnvoyee : Email gestionnaire
  AlerteEnvoyee --> CommandeCreee : Auto-commande
  CommandeCreee : En attente livraison
}

note right of Critique
  Alerte immédiate
  Commande automatique
  si activée
end note

@enduml
```

## 7. États d'un Planning

```plantuml
@startuml
[*] --> Brouillon : Créer planning

Brouillon : Édition libre
Brouillon : Non visible employés
Brouillon --> Brouillon : Ajouter/Modifier shift
Brouillon --> EnValidation : Soumettre à validation

EnValidation : Vérifications automatiques
EnValidation : Contrôle contraintes
EnValidation --> Brouillon : Erreurs détectées
EnValidation --> Valide : Validations OK

Valide : Planning approuvé
Valide : Prêt à publier
Valide --> Brouillon : Modifier (avec raison)
Valide --> Publie : Publier

Publie : Visible employés
Publie : Notifications envoyées
Publie --> EnCours : Date début atteinte

EnCours : Semaine en cours
EnCours : Absences trackées
EnCours --> EnCours : Ajustement urgent
EnCours --> Termine : Date fin atteinte

Termine : Semaine terminée
Termine : Heures comptabilisées
Termine --> Archive : Archiver

Archive : Planning archivé
Archive : Historique conservé
Archive --> [*]

note right of Publie
  Email à tous employés
  Affichage tableau
  Notification app mobile
end note

@enduml
```

## Description des États

### Transaction
- **Initialisée**: Nouvelle transaction vide
- **En Cours**: Ajout de produits actif
- **En Attente Paiement**: Total calculé, attente paiement
- **Validée**: Transaction complète et payée
- **Annulée**: Transaction abandonnée

### Commande Fournisseur
- **Brouillon**: Édition en cours
- **Validée**: Bon de commande envoyé

