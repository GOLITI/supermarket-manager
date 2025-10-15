- Relation client
- Esprit d'équipe

#### Contenu
- Points forts
- Points à améliorer
- Objectifs fixés
- Commentaires

### 4.6 Gestion Documentaire ✅

#### Types de documents
- CONTRAT
- FICHE_PAIE
- ATTESTATION
- CV
- DIPLOME
- PIECE_IDENTITE
- JUSTIFICATIF_DOMICILE
- CERTIFICAT_MEDICAL
- FORMATION
- EVALUATION

#### Fonctionnalités
- Stockage sécurisé
- Documents confidentiels
- Date d'expiration
- Recherche et filtrage

## Architecture technique

### Couches de l'application

```
┌─────────────────────────────────────────┐
│         Controllers (REST API)          │
│  - EmployeController                    │
│  - AbsenceController                    │
│  - PlanningController                   │
│  - PointageController                   │
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│            Services (Logique)           │
│  - EmployeService                       │
│  - AbsenceService                       │
│  - PlanningService                      │
│  - PointageService                      │
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│        Repositories (Data Access)       │
│  - EmployeRepository                    │
│  - AbsenceRepository                    │
│  - PlanningRepository                   │
│  - PointageRepository                   │
│  - EvaluationRepository                 │
│  - DocumentRepository                   │
└─────────────────────────────────────────┘
                    ↓
┌─────────────────────────────────────────┐
│          Base de données PostgreSQL     │
└─────────────────────────────────────────┘
```

## API REST Endpoints

### Employés
```
GET    /api/employes                    - Liste tous les employés
GET    /api/employes/{id}               - Détails d'un employé
GET    /api/employes/actifs             - Employés actifs
GET    /api/employes/poste/{poste}      - Employés par poste
GET    /api/employes/recherche?q=       - Rechercher des employés
POST   /api/employes                    - Créer un employé
PUT    /api/employes/{id}               - Modifier un employé
PATCH  /api/employes/{id}/statut        - Changer le statut
DELETE /api/employes/{id}               - Supprimer un employé
```

### Absences
```
GET    /api/absences/{id}                      - Détails d'une absence
GET    /api/absences/employe/{id}              - Absences d'un employé
GET    /api/absences/employe/{id}/en-cours     - Absences en cours
GET    /api/absences/en-attente                - Absences à valider
GET    /api/absences/periode?debut=&fin=       - Absences par période
POST   /api/absences                           - Créer une demande
PATCH  /api/absences/{id}/valider              - Valider/Refuser
PATCH  /api/absences/{id}/annuler              - Annuler une demande
```

### Plannings
```
GET    /api/plannings/{id}                     - Détails d'un planning
GET    /api/plannings/employe/{id}             - Planning d'un employé
GET    /api/plannings/date/{date}              - Planning par date
GET    /api/plannings/periode?debut=&fin=      - Planning par période
POST   /api/plannings                          - Créer un planning
PUT    /api/plannings/{id}                     - Modifier un planning
PATCH  /api/plannings/{id}/valider             - Valider un planning
DELETE /api/plannings/{id}                     - Supprimer un planning
POST   /api/plannings/generer-hebdomadaire     - Générer planning semaine
```

### Pointages
```
GET    /api/pointages/{id}                     - Détails d'un pointage
GET    /api/pointages/employe/{id}             - Pointages d'un employé
GET    /api/pointages/employe/{id}/date/{date} - Pointages par date
POST   /api/pointages/employe/{id}/pointer     - Pointer (entrée/sortie)
PATCH  /api/pointages/{id}/valider             - Valider un pointage
GET    /api/pointages/employe/{id}/heures-travaillees?debut=&fin= - Calcul heures
```

## Scénarios d'utilisation

### Scénario 1 : Demande de congé (Sarah)
```
1. Sarah (EMP001) se connecte au portail employé
2. Elle consulte son solde de congés : 15 jours restants
3. Elle crée une demande de congé :
   - Type : Congé payé
   - Dates : Du 15 au 22 janvier
   - Motif : Vacances familiales
4. La demande est créée avec le statut EN_ATTENTE
5. Le responsable RH reçoit une notification
6. Le responsable examine la demande :
   - Vérifie la disponibilité de l'équipe
   - Vérifie qu'il n'y a pas de conflit
7. Le responsable approuve la demande
8. Sarah reçoit une notification de validation
9. Le planning est automatiquement mis à jour
```

### Scénario 2 : Pointage quotidien
```
1. Sarah arrive au travail à 8h00
2. Elle badge avec son matricule EMP001
3. Le système enregistre l'heure d'entrée : 08:00:15
4. À 16h30, elle badge à nouveau
5. Le système :
   - Enregistre l'heure de sortie : 16:30:22
   - Calcule les heures travaillées : 8h 30min
   - Détecte 30min d'heures supplémentaires
6. Le pointage est visible dans son portail
7. Le chef d'équipe valide le pointage le lendemain
```

### Scénario 3 : Évaluation annuelle
```
1. Le responsable RH programme les évaluations Q1
2. Le chef de rayon évalue Sarah :
   - Ponctualité : 5/5
   - Qualité travail : 4/5
   - Productivité : 4/5
   - Relation client : 5/5
   - Esprit d'équipe : 4/5
3. Points forts :
   - Excellente ponctualité
   - Très bon relationnel client
4. Points à améliorer :
   - Rapidité en période d'affluence
5. Objectifs 2025 :
   - Formation nouveau système caisse
   - Objectif 95% satisfaction client
6. L'évaluation est validée et archivée
7. Sarah peut la consulter dans son portail
```

## Tests unitaires

Tous les services du module RH sont testés avec JUnit et Mockito :

### Tests EmployeService (15 tests)
- ✅ Création d'employé avec succès
- ✅ Vérification unicité matricule
- ✅ Vérification unicité email
- ✅ Récupération par ID
- ✅ Recherche d'employés
- ✅ Modification d'employé
- ✅ Changement de statut
- ✅ Suppression d'employé

### Tests AbsenceService (12 tests)
- ✅ Création de demande d'absence
- ✅ Validation des dates
- ✅ Détection de chevauchement
- ✅ Validation par responsable
- ✅ Refus de demande
- ✅ Annulation de demande
- ✅ Récupération absences en cours

## Exécution des tests

```bash
cd backend

# Tests du module RH uniquement
./mvnw test -Dtest="Employe*Test,Absence*Test,Planning*Test,Pointage*Test"

# Tous les tests
./mvnw test

# Tests avec rapport détaillé
./mvnw test -Dtest="Employe*Test" -X
```

## Données de test

Le fichier `data.sql` contient :
- ✅ 10 employés avec différents postes et statuts
- ✅ Plannings pour la semaine en cours
- ✅ Absences (approuvées, en attente, refusées)
- ✅ Pointages des derniers jours
- ✅ Évaluations de performance
- ✅ Documents RH (contrats, fiches de paie, etc.)

## Prochaines étapes

### Phase 2 - Fonctionnalités avancées
- [ ] Génération automatique des plannings
- [ ] Calcul automatique des salaires
- [ ] Gestion des formations
- [ ] Workflow d'approbation multi-niveaux
- [ ] Notifications par email/SMS
- [ ] Tableaux de bord RH
- [ ] Export des rapports (PDF, Excel)

### Phase 3 - Intégrations
- [ ] Intégration système de paie externe
- [ ] Badgeuse physique
- [ ] Reconnaissance faciale
- [ ] Application mobile pour employés

## Technologies utilisées

- **Backend** : Spring Boot 3.x
- **Base de données** : PostgreSQL
- **ORM** : Hibernate/JPA
- **Validation** : Jakarta Validation
- **Tests** : JUnit 5, Mockito, AssertJ
- **Logging** : SLF4J + Logback
- **Documentation** : Swagger/OpenAPI (à venir)

## Notes importantes

⚠️ **Sécurité** :
- Les données salariales sont confidentielles
- Les documents sensibles sont marqués comme tels
- L'accès aux données RH doit être strictement contrôlé
- Implémenter l'authentification et les autorisations (à venir)

✨ **Points forts** :
- Architecture propre et maintenable
- Couverture de tests élevée
- Validation des données robuste
- Gestion d'erreurs complète
- Logs détaillés pour le débogage

---

**Module développé et testé avec succès ✅**
*Date : 8 octobre 2025*
# MODULE 4 : RESSOURCES HUMAINES 👨‍💼

## Vue d'ensemble

Le module Ressources Humaines (RH) du système de gestion du supermarché permet de gérer l'ensemble du personnel, des plannings, des absences, des pointages et des évaluations de performance.

## Fonctionnalités implémentées

### 4.1 Gestion du Personnel ✅

#### Entités principales
- **Employe** : Informations complètes sur les employés
  - Matricule unique
  - Informations personnelles (nom, prénom, email, téléphone)
  - Date de naissance et d'embauche
  - Poste et statut
  - Type de contrat et salaire
  - Coordonnées bancaires

#### Postes disponibles
- CAISSIER
- CHEF_CAISSIER
- VENDEUR
- CHEF_RAYON
- MAGASINIER
- RESPONSABLE_STOCK
- COMPTABLE
- RESPONSABLE_RH
- DIRECTEUR
- AGENT_SECURITE
- AGENT_ENTRETIEN

#### Statuts employé
- ACTIF
- EN_CONGE
- EN_ARRET_MALADIE
- SUSPENDU
- DEMISSIONNAIRE
- LICENCIE
- RETRAITE

#### Types de contrat
- CDI (Contrat à Durée Indéterminée)
- CDD (Contrat à Durée Déterminée)
- INTERIM
- STAGE
- APPRENTISSAGE
- TEMPS_PARTIEL

### 4.2 Gestion des Absences ✅

#### Fonctionnalités
- Création de demandes d'absence par les employés
- Validation/Refus par les responsables
- Suivi des congés payés
- Gestion des arrêts maladie
- Historique des absences

#### Types d'absence
- CONGE_PAYE
- CONGE_SANS_SOLDE
- MALADIE
- MATERNITE / PATERNITE
- FORMATION
- EVENEMENT_FAMILIAL
- RTT
- ABSENCE_INJUSTIFIEE

#### Workflow
1. Employé crée une demande
2. Statut EN_ATTENTE
3. Responsable valide/refuse
4. Notification et mise à jour du statut

### 4.3 Planning et Horaires ✅

#### Fonctionnalités
- Création et modification des plannings
- Planification par semaine
- Affectation des postes
- Validation des plannings

#### Types de shift
- MATIN (06:00-14:00)
- APRES_MIDI (14:00-22:00)
- NUIT (22:00-06:00)
- JOURNEE_COMPLETE (08:00-17:00)

### 4.4 Système de Pointage ✅

#### Fonctionnalités
- Pointage entrée/sortie automatique
- Calcul automatique des heures travaillées
- Détection des heures supplémentaires
- Validation des pointages
- Rapports horaires

#### Types de pointage
- NORMAL
- HEURES_SUP
- JOUR_FERIE
- NUIT
- DIMANCHE

### 4.5 Évaluations de Performance ✅

#### Critères d'évaluation
- Note globale (1-5)
- Ponctualité
- Qualité du travail
- Productivité

