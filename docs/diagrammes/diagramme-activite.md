# Diagramme d'Activité - Supermarket Manager

## 1. Activité: Processus de Vente Complète

```plantuml
@startuml
start

:Caissier ouvre séance de caisse;
:Déclarer fond de caisse;

repeat
  :Scanner un produit;
  
  if (Produit trouvé?) then (oui)
    if (Stock disponible?) then (oui)
      :Ajouter à la transaction;
      :Afficher prix;
    else (non)
      :Afficher erreur stock;
    endif
  else (non)
    :Rechercher manuellement;
  endif
  
  :Demander si autre produit?;
repeat while (Autre produit?) is (oui)
->non;

:Calculer total;

if (Client a carte fidélité?) then (oui)
  :Scanner carte;
  :Appliquer réductions fidélité;
  :Calculer points à gagner;
endif

if (Promotion applicable?) then (oui)
  :Appliquer promotion;
  :Recalculer total;
endif

:Afficher montant total;

partition "Paiement" {
  repeat
    :Saisir mode de paiement;
    
    if (Type paiement?) then (Espèces)
      :Saisir montant reçu;
      :Calculer monnaie;
      :Valider paiement;
    elseif (Carte bancaire)
      :Insérer/Taper carte;
      :Autorisation bancaire;
      if (Autorisé?) then (oui)
        :Valider paiement;
      else (non)
        :Annuler paiement;
        :Demander autre mode;
      endif
    elseif (Mobile Money)
      :Composer code USSD;
      :Attendre confirmation;
      :Valider paiement;
    endif
    
  repeat while (Montant total payé?) is (non)
  ->oui;
}

:Déduire stock;

if (Client fidélité?) then (oui)
  :Créditer points;
  :Enregistrer achat;
endif

:Imprimer reçu;
:Remettre reçu au client;

:Demander transaction suivante?;

if (Fin de journée?) then (oui)
  :Fermer séance;
  :Compter caisse;
  :Déclarer montant réel;
  
  if (Écart détecté?) then (oui)
    :Générer rapport d'écart;
    :Notifier responsable;
  endif
  
  stop
else (non)
  :Continuer;
  stop
endif

@enduml
```

## 2. Activité: Gestion de Commande Fournisseur

```plantuml
@startuml
start

:Système détecte stock faible;

fork
  :Générer alerte email;
fork again
  :Afficher notification dashboard;
end fork

:Gestionnaire consulte alertes;

repeat
  :Sélectionner produit;
  :Consulter historique ventes;
  :Calculer quantité recommandée;
  
  if (Fournisseur habituel actif?) then (oui)
    :Sélectionner fournisseur habituel;
  else (non)
    :Rechercher fournisseurs;
    :Comparer prix et délais;
    :Sélectionner meilleur fournisseur;
  endif
  
  :Ajouter à la commande;
  
repeat while (Autre produit?) is (oui)
->non;

:Calculer montant total commande;

:Réviser commande;

if (Valider commande?) then (oui)
  :Enregistrer commande;
  :Générer bon de commande;
  
  fork
    :Envoyer email au fournisseur;
  fork again
    :Notifier service comptabilité;
  end fork
  
  :Attendre livraison;
  
  if (Livraison reçue?) then (oui)
    :Vérifier conformité;
    
    if (Conforme?) then (oui)
      :Réceptionner produits;
      :Mettre à jour stock;
      :Valider livraison;
      :Clôturer commande;
      stop
    else (non)
      :Signaler non-conformité;
      :Contacter fournisseur;
      
      if (Remplacer produits?) then (oui)
        :Planifier nouvelle livraison;
      else (non)
        :Demander avoir/remboursement;
      endif
      
      stop
    endif
  endif
else (non)
  :Sauvegarder brouillon;
  stop
endif

@enduml
```

## 3. Activité: Planification Hebdomadaire

```plantuml
@startuml
start

:Responsable RH démarre planning;
:Sélectionner semaine;

:Charger template semaine;
:Consulter absences prévues;

fork
  :Analyser heures de pointe historiques;
fork again
  :Consulter disponibilités employés;
end fork

repeat
  :Sélectionner jour;
  
  repeat
    :Sélectionner créneau horaire;
    
    if (Heure de pointe?) then (oui)
      :Augmenter nombre de postes;
    endif
    
    :Calculer besoin en personnel;
    
    repeat
      :Sélectionner poste;
      :Lister employés disponibles;
      
      if (Employé qualifié trouvé?) then (oui)
        :Affecter employé;
        
        if (Dépasse 35h/semaine?) then (oui)
          :Avertissement heures sup;
          
          if (Confirmer?) then (non)
            :Chercher autre employé;
          endif
        endif
        
      else (non)
        :Signaler manque de personnel;
      endif
      
    repeat while (Tous postes remplis?) is (non)
    ->oui;
    
  repeat while (Tous créneaux remplis?) is (non)
  ->oui;
  
repeat while (Tous jours planifiés?) is (non)
->oui;

:Vérifier contraintes légales;

if (Contraintes respectées?) then (oui)
  :Valider planning;
  
  fork
    :Notifier tous employés;
  fork again
    :Publier sur tableau d'affichage;
  fork again
    :Envoyer par email;
  end fork
  
  stop
else (non)
  :Identifier problèmes;
  :Ajuster planning;
  stop
endif

@enduml
```

## 4. Activité: Traitement Demande Absence

```plantuml
@startuml
|Employé|
start
:Accéder formulaire absence;
:Remplir demande;
:Sélectionner dates;
:Choisir type absence;
:Justifier motif;
:Soumettre demande;

|Système|
:Vérifier dates valides;

if (Dates valides?) then (non)
  |Employé|
  :Corriger dates;
  stop
endif

:Vérifier conflits planning;

if (Conflits détectés?) then (oui)
  |Employé|
  :Afficher conflits;
  :Modifier dates ou abandonner;
  stop
else (non)
  :Enregistrer demande;
  :Statut = EN_ATTENTE;
  
  fork
    :Notifier RH par email;
  fork again
    :Notifier sur dashboard RH;
  end fork
endif

|Responsable RH|
:Consulter demandes en attente;
:Sélectionner demande;
:Examiner détails;

fork
  :Consulter historique absences;
fork again
  :Vérifier impact sur planning;
fork again
  :Consulter solde congés;
end fork

if (Décision?) then (Approuver)
  |Système|
  :Mettre à jour statut = APPROUVEE;
  :Déduire du solde congés;
  :Bloquer créneaux dans planning;
  
  fork
    :Notifier employé (approuvé);
  fork again
    :Mettre à jour planning;
  fork again
    :Notifier équipe du remplacement;
  end fork
  
  |Employé|
  :Recevoir confirmation;
  stop
  
else (Rejeter)
  |Système|
  :Mettre à jour statut = REJETEE;
  :Saisir motif refus;
  
  :Notifier employé (rejeté);
  
  |Employé|
  :Recevoir notification rejet;
  
  if (Reformuler demande?) then (oui)
    :Nouvelle demande;
    stop
  else (non)
    stop
  endif
endif

@enduml
```

## 5. Activité: Gestion Carte Fidélité

```plantuml
@startuml
|Client|
start
:Demander carte fidélité;

|Caissier|
:Vérifier éligibilité;

if (Client déjà inscrit?) then (oui)
  :Rechercher client;
  
  if (A déjà carte?) then (oui)
    :Informer client;
    stop
  endif
else (non)
  :Créer compte client;
  :Saisir informations;
  :Valider données;
endif

|Système|
:Générer numéro carte unique;
:Définir niveau = BRONZE;
:Points = 0;
:Date expiration = +2 ans;
:Activer carte;

fork
  :Enregistrer en base;
fork again
  :Envoyer email bienvenue;
end fork

|Caissier|
:Imprimer carte;
:Expliquer fonctionnement;

|Client|
:Recevoir carte;

note right
  La carte est maintenant utilisable
  pour tous les achats
end note

== Utilisation Achat ==

|Client|
:Faire courses;
:Présenter carte en caisse;

|Caissier|
:Scanner carte;

|Système|
:Récupérer informations;
:Afficher solde points;

if (Carte valide et active?) then (oui)
  :Associer à transaction;
  
  |Caissier|
  :Finaliser vente;
  
  |Système|
  :Calculer points (1pt/100F CFA);
  :Créditer points;
  :Enregistrer historique;
  
  :Vérifier seuil niveau supérieur;
  
  if (Atteint seuil?) then (oui)
    :Upgrader niveau;
    
    fork
      :Notifier client;
    fork again
      :Envoyer email félicitations;
    end fork
  endif
  
  |Caissier|
  :Informer client des points;
  
  |Client|
  :Noter nouveau solde;
  stop
  
else (non)
  |Système|
  if (Expirée?) then (oui)
    :Proposer renouvellement;
  else (Désactivée)
    :Expliquer raison;
  endif
  
  stop
endif

@enduml
```

## 6. Activité: Contrôle Stock et Inventaire

```plantuml
@startuml
start

:Lancer inventaire;
:Sélectionner entrepôt;

if (Type inventaire?) then (Complet)
  :Lister tous produits;
else (Partiel)
  :Sélectionner catégories;
  :Lister produits sélectionnés;
endif

fork
  :Imprimer listes de comptage;
fork again
  :Bloquer mouvements stock;
end fork

partition "Phase Comptage" {
  repeat
    :Scanner/Saisir produit;
    :Saisir quantité comptée;
    :Enregistrer comptage;
    
    if (Doute sur quantité?) then (oui)
      :Marquer pour recomptage;
    endif
    
  repeat while (Tous produits comptés?) is (non)
  ->oui;
}

:Comparer avec stock théorique;

fork
  :Identifier écarts;
fork again
  :Calculer valeur écarts;
end fork

if (Écarts détectés?) then (oui)
  :Lister écarts significatifs;
  
  repeat
    :Sélectionner écart;
    :Demander recomptage;
    
    if (Écart confirmé?) then (oui)
      if (Justification?) then (Vol)
        :Créer rapport vol;
        :Notifier sécurité;
      elseif (Casse)
        :Créer rapport casse;
      elseif (Erreur saisie)
        :Corriger historique;
      else (Cause inconnue)
        :Créer rapport anomalie;
      endif
      
      :Ajuster stock;
    endif
    
  repeat while (Autres écarts?) is (oui)
  ->non;
endif

:Générer rapport inventaire;

fork
  :Débloquer mouvements;
fork again
  :Envoyer rapport direction;
fork again
  :Archiver données;
end fork

if (Écarts importants?) then (oui)
  :Planifier audit;
endif

stop

@enduml
```

## Description des Activités

### 1. Processus de Vente
- Ouverture de séance avec déclaration du fond de caisse
- Scan des produits avec vérification de stock
- Application des promotions et réductions fidélité
- Gestion de paiements multiples
- Fermeture de séance avec calcul d'écart

### 2. Commande Fournisseur
- Détection automatique des stocks faibles
- Calcul de la quantité recommandée
- Sélection du meilleur fournisseur
- Suivi de la livraison
- Vérification de conformité

### 3. Planification RH
- Analyse des besoins (heures de pointe)
- Affectation des employés selon disponibilités
- Respect des contraintes légales (35h)
- Notification de tous les employés

### 4. Demande Absence
- Soumission par l'employé
- Vérification automatique des conflits
- Validation/Rejet par RH
- Mise à jour automatique du planning

### 5. Carte Fidélité
- Création avec génération de numéro unique
- Utilisation lors des achats
- Calcul et crédit automatique de points
- Upgrade automatique de niveau

### 6. Inventaire
- Comptage physique vs stock théorique
- Identification et analyse des écarts
- Justification et ajustement
- Génération de rapports

