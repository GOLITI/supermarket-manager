-- Script d'initialisation de la base de données pour le système de gestion des stocks
-- Scénario: Alerte de stock pour la farine T45

-- Insertion des entrepôts
INSERT INTO entrepots (code, nom, adresse, capacite_max, actif, date_creation, date_modification)
VALUES
    ('ENT-001', 'Entrepôt Principal', '123 Avenue Commerciale, Abidjan', 10000.0, true, NOW(), NOW()),
    ('ENT-002', 'Entrepôt Secondaire', '456 Boulevard Industriel, Abidjan', 5000.0, true, NOW(), NOW());

-- Insertion des catégories
INSERT INTO categories (nom, description, type, date_creation, date_modification)
VALUES
    ('Farines et Céréales', 'Produits de boulangerie et céréales', 'ALIMENTAIRE_SEC', NOW(), NOW()),
    ('Produits Laitiers', 'Lait, yaourt, fromages', 'ALIMENTAIRE_FRAIS', NOW(), NOW()),
    ('Fruits et Légumes', 'Produits frais', 'ALIMENTAIRE_FRAIS', NOW(), NOW()),
    ('Boissons', 'Boissons gazeuses et jus', 'BOISSON', NOW(), NOW()),
    ('Produits Surgelés', 'Viandes et légumes surgelés', 'SURGELE', NOW(), NOW());

-- Insertion des fournisseurs
INSERT INTO fournisseurs (nom, code, type, adresse, telephone, email, contact_principal, delai_livraison_jours, conditions_paiement, actif, notes, date_creation, date_modification)
VALUES
    ('Meunerie Locale', 'FOUR-001', 'PRODUCTEUR_LOCAL', 'Zone Industrielle, Abidjan', '0123456789', 'contact@meunerie-locale.ci', 'M. Koné', 3, 'Paiement à 30 jours', true, 'Fournisseur principal de farines', NOW(), NOW()),
    ('Grands Moulins d''Abidjan', 'FOUR-002', 'GROSSISTE_NATIONAL', 'Port d''Abidjan', '0198765432', 'commercial@gma.ci', 'Mme Traoré', 5, 'Paiement à 45 jours', true, 'Fournisseur de céréales et farines', NOW(), NOW()),
    ('Distributeur Nestlé CI', 'FOUR-003', 'DISTRIBUTEUR_MARQUE', 'Plateau, Abidjan', '0145678912', 'nestle@nestle.ci', 'M. Kouassi', 2, 'Paiement à 30 jours', true, 'Produits laitiers et boissons', NOW(), NOW());

-- Insertion des produits
INSERT INTO produits (code, nom, description, categorie_id, fournisseur_id, prix_achat, prix_vente, unite, code_barres, actif, date_peremption_requis, date_creation, date_modification)
VALUES
    -- Farine T45 (le produit du scénario)
    ('PROD-001', 'Farine T45 1kg', 'Farine de blé type 45 pour pâtisserie fine',
     (SELECT id FROM categories WHERE nom = 'Farines et Céréales'),
     (SELECT id FROM fournisseurs WHERE code = 'FOUR-001'),
     1500.00, 2000.00, 'kg', '3245678901234', true, false, NOW(), NOW()),

    -- Autres produits
    ('PROD-002', 'Farine T55 1kg', 'Farine de blé type 55 pour pain et pizza',
     (SELECT id FROM categories WHERE nom = 'Farines et Céréales'),
     (SELECT id FROM fournisseurs WHERE code = 'FOUR-001'),
     1400.00, 1800.00, 'kg', '3245678901235', true, false, NOW(), NOW()),

    ('PROD-003', 'Lait Entier 1L', 'Lait frais entier pasteurisé',
     (SELECT id FROM categories WHERE nom = 'Produits Laitiers'),
     (SELECT id FROM fournisseurs WHERE code = 'FOUR-003'),
     800.00, 1200.00, 'L', '3245678901236', true, true, NOW(), NOW()),

    ('PROD-004', 'Riz Basmati 5kg', 'Riz Basmati qualité supérieure',
     (SELECT id FROM categories WHERE nom = 'Farines et Céréales'),
     (SELECT id FROM fournisseurs WHERE code = 'FOUR-002'),
     5000.00, 7500.00, 'kg', '3245678901237', true, false, NOW(), NOW());

-- Insertion des stocks (avec alerte pour la farine T45)
INSERT INTO stocks (produit_id, entrepot_id, quantite, seuil_reapprovisionnement, quantite_maximale, quantite_recommandee_commande, date_derniere_entree, alerte_active, date_creation, date_modification)
VALUES
    -- Farine T45 avec stock bas (50 unités, seuil 100) - ALERTE ACTIVE
    ((SELECT id FROM produits WHERE code = 'PROD-001'),
     (SELECT id FROM entrepots WHERE code = 'ENT-001'),
     50, 100, 1000, 500, NOW() - INTERVAL '10 days', true, NOW(), NOW()),

    -- Farine T55 avec stock normal
    ((SELECT id FROM produits WHERE code = 'PROD-002'),
     (SELECT id FROM entrepots WHERE code = 'ENT-001'),
     250, 100, 800, 400, NOW() - INTERVAL '5 days', false, NOW(), NOW()),

    -- Lait avec stock critique (10 unités, seuil 50) - ALERTE ACTIVE
    ((SELECT id FROM produits WHERE code = 'PROD-003'),
     (SELECT id FROM entrepots WHERE code = 'ENT-001'),
     10, 50, 200, 100, NOW() - INTERVAL '2 days', true, NOW(), NOW()),

    -- Riz avec stock normal
    ((SELECT id FROM produits WHERE code = 'PROD-004'),
     (SELECT id FROM entrepots WHERE code = 'ENT-001'),
     150, 80, 500, 200, NOW() - INTERVAL '15 days', false, NOW(), NOW());

-- Insertion d'une commande automatique exemple (en brouillon)
INSERT INTO commandes (numero_commande, fournisseur_id, statut, date_commande, date_livraison_prevue, montant_total, notes, commande_automatique, date_creation, date_modification)
VALUES
    ('CMD-20250105-0001',
     (SELECT id FROM fournisseurs WHERE code = 'FOUR-001'),
     'BROUILLON',
     CURRENT_DATE,
     CURRENT_DATE + INTERVAL '3 days',
     750000.00,
     'Commande automatique générée suite à alerte de stock pour Farine T45',
     true,
     NOW(),
     NOW());

-- Insertion des lignes de commande
INSERT INTO lignes_commande (commande_id, produit_id, quantite, quantite_recue, prix_unitaire, notes)
VALUES
    ((SELECT id FROM commandes WHERE numero_commande = 'CMD-20250105-0001'),
     (SELECT id FROM produits WHERE code = 'PROD-001'),
     500,
     0,
     1500.00,
     'Recommandé automatiquement - Stock actuel: 50');

-- Mise à jour du montant total de la commande
UPDATE commandes
SET montant_total = (
    SELECT SUM(quantite * prix_unitaire)
    FROM lignes_commande
    WHERE commande_id = (SELECT id FROM commandes WHERE numero_commande = 'CMD-20250105-0001')
)
WHERE numero_commande = 'CMD-20250105-0001';

-- Affichage des stocks en alerte
SELECT
    p.code AS code_produit,
    p.nom AS nom_produit,
    s.quantite AS quantite_actuelle,
    s.seuil_reapprovisionnement AS seuil,
    s.quantite_recommandee_commande AS quantite_recommandee,
    f.nom AS fournisseur,
    f.delai_livraison_jours AS delai_livraison,
    CASE
        WHEN s.quantite = 0 THEN 'CRITIQUE'
        WHEN s.quantite <= s.seuil_reapprovisionnement / 2 THEN 'CRITIQUE'
        WHEN s.quantite <= s.seuil_reapprovisionnement THEN 'MOYEN'
        ELSE 'BAS'
    END AS niveau_alerte
FROM stocks s
JOIN produits p ON s.produit_id = p.id
JOIN fournisseurs f ON p.fournisseur_id = f.id
WHERE s.quantite <= s.seuil_reapprovisionnement
ORDER BY s.quantite ASC;

-- ========================================
-- MODULE RESSOURCES HUMAINES (RH)
-- ========================================

-- Insertion des employés
INSERT INTO employes (matricule, nom, prenom, email, telephone, date_naissance, adresse, date_embauche, poste, statut, type_contrat, salaire_base, numero_securite_sociale, iban)
VALUES
    ('EMP001', 'Dupont', 'Sarah', 'sarah.dupont@supermarket.com', '0707010101', '1995-05-15', '123 Rue de la Paix, Abidjan', '2020-01-15', 'CAISSIER', 'ACTIF', 'CDI', 180000.00, '1950523456789', 'CI93000000000000000000001'),
    ('EMP002', 'Koné', 'Jean', 'jean.kone@supermarket.com', '0707020202', '1988-03-22', '456 Avenue de la République, Abidjan', '2018-06-01', 'CHEF_CAISSIER', 'ACTIF', 'CDI', 250000.00, '1880322345678', 'CI93000000000000000000002'),
    ('EMP003', 'Traoré', 'Marie', 'marie.traore@supermarket.com', '0707030303', '1992-08-10', '789 Boulevard Latrille, Abidjan', '2019-03-10', 'VENDEUR', 'ACTIF', 'CDI', 200000.00, '1920810234567', 'CI93000000000000000000003'),
    ('EMP004', 'Yao', 'Pierre', 'pierre.yao@supermarket.com', '0707040404', '1985-12-05', '321 Rue du Commerce, Abidjan', '2017-09-01', 'CHEF_RAYON', 'ACTIF', 'CDI', 280000.00, '1851205123456', 'CI93000000000000000000004'),
    ('EMP005', 'Bamba', 'Fatou', 'fatou.bamba@supermarket.com', '0707050505', '1990-07-18', '654 Avenue du Marché, Abidjan', '2021-02-01', 'MAGASINIER', 'ACTIF', 'CDI', 190000.00, '1900718012345', 'CI93000000000000000000005'),
    ('EMP006', 'Coulibaly', 'Ahmed', 'ahmed.coulibaly@supermarket.com', '0707060606', '1987-11-25', '987 Rue de la Gare, Abidjan', '2016-04-15', 'RESPONSABLE_STOCK', 'ACTIF', 'CDI', 320000.00, '1871125901234', 'CI93000000000000000000006'),
    ('EMP007', 'Ouattara', 'Aminata', 'aminata.ouattara@supermarket.com', '0707070707', '1993-02-14', '147 Boulevard Houphouët, Abidjan', '2022-01-10', 'COMPTABLE', 'ACTIF', 'CDI', 350000.00, '1930214890123', 'CI93000000000000000000007'),
    ('EMP008', 'Diallo', 'Moussa', 'moussa.diallo@supermarket.com', '0707080808', '1980-09-30', '258 Avenue Franchet, Abidjan', '2015-05-01', 'RESPONSABLE_RH', 'ACTIF', 'CDI', 400000.00, '1800930789012', 'CI93000000000000000000008'),
    ('EMP009', 'Sanogo', 'Christine', 'christine.sanogo@supermarket.com', '0707090909', '1975-04-12', '369 Rue Lecoeur, Abidjan', '2010-01-15', 'DIRECTEUR', 'ACTIF', 'CDI', 600000.00, '1750412678901', 'CI93000000000000000000009'),
    ('EMP010', 'Kamara', 'Ibrahima', 'ibrahima.kamara@supermarket.com', '0707101010', '1991-06-20', '741 Avenue Nogues, Abidjan', '2023-03-01', 'AGENT_SECURITE', 'ACTIF', 'CDD', 170000.00, '1910620567890', 'CI93000000000000000000010');

-- Insertion des plannings (semaine en cours)
INSERT INTO plannings (employe_id, date, heure_debut, heure_fin, type_shift, poste_assigne, notes, valide)
VALUES
    -- Lundi
    ((SELECT id FROM employes WHERE matricule = 'EMP001'), CURRENT_DATE, '08:00:00', '16:00:00', 'JOURNEE_COMPLETE', 'CAISSIER', 'Caisse principale', true),
    ((SELECT id FROM employes WHERE matricule = 'EMP002'), CURRENT_DATE, '08:00:00', '16:00:00', 'JOURNEE_COMPLETE', 'CHEF_CAISSIER', 'Supervision caisses', true),
    ((SELECT id FROM employes WHERE matricule = 'EMP003'), CURRENT_DATE, '14:00:00', '22:00:00', 'APRES_MIDI', 'VENDEUR', 'Rayon fruits et légumes', true),

    -- Mardi
    ((SELECT id FROM employes WHERE matricule = 'EMP001'), CURRENT_DATE + INTERVAL '1 day', '14:00:00', '22:00:00', 'APRES_MIDI', 'CAISSIER', 'Caisse 2', true),
    ((SELECT id FROM employes WHERE matricule = 'EMP003'), CURRENT_DATE + INTERVAL '1 day', '08:00:00', '16:00:00', 'JOURNEE_COMPLETE', 'VENDEUR', 'Rayon épicerie', true),

    -- Mercredi
    ((SELECT id FROM employes WHERE matricule = 'EMP001'), CURRENT_DATE + INTERVAL '2 days', '08:00:00', '16:00:00', 'JOURNEE_COMPLETE', 'CAISSIER', 'Caisse principale', false),
    ((SELECT id FROM employes WHERE matricule = 'EMP005'), CURRENT_DATE + INTERVAL '2 days', '08:00:00', '16:00:00', 'JOURNEE_COMPLETE', 'MAGASINIER', 'Réception marchandises', false);

-- Insertion des absences
INSERT INTO absences (employe_id, type, date_debut, date_fin, motif, statut, date_validation, validateur_id, commentaire_validation)
VALUES
    -- Absence approuvée (passée)
    ((SELECT id FROM employes WHERE matricule = 'EMP001'), 'CONGE_PAYE', CURRENT_DATE - INTERVAL '10 days', CURRENT_DATE - INTERVAL '6 days', 'Congés annuels', 'APPROUVEE', CURRENT_DATE - INTERVAL '15 days', (SELECT id FROM employes WHERE matricule = 'EMP008'), 'Approuvé'),

    -- Absence en attente
    ((SELECT id FROM employes WHERE matricule = 'EMP003'), 'CONGE_PAYE', CURRENT_DATE + INTERVAL '7 days', CURRENT_DATE + INTERVAL '14 days', 'Vacances familiales', 'EN_ATTENTE', NULL, NULL, NULL),

    -- Arrêt maladie en cours
    ((SELECT id FROM employes WHERE matricule = 'EMP005'), 'MALADIE', CURRENT_DATE - INTERVAL '2 days', CURRENT_DATE + INTERVAL '1 day', 'Grippe', 'APPROUVEE', CURRENT_DATE - INTERVAL '2 days', (SELECT id FROM employes WHERE matricule = 'EMP008'), 'Avec justificatif médical'),

    -- Absence refusée
    ((SELECT id FROM employes WHERE matricule = 'EMP001'), 'CONGE_SANS_SOLDE', CURRENT_DATE + INTERVAL '3 days', CURRENT_DATE + INTERVAL '5 days', 'Raisons personnelles', 'REFUSEE', CURRENT_DATE - INTERVAL '1 day', (SELECT id FROM employes WHERE matricule = 'EMP008'), 'Période de forte activité');

-- Insertion des pointages (derniers jours)
INSERT INTO pointages (employe_id, heure_entree, heure_sortie, type, heures_travaillees, heures_supplementaires, valide)
VALUES
    -- Hier
    ((SELECT id FROM employes WHERE matricule = 'EMP001'), CURRENT_TIMESTAMP - INTERVAL '1 day' + INTERVAL '8 hours', CURRENT_TIMESTAMP - INTERVAL '1 day' + INTERVAL '16 hours', 'NORMAL', 8.0, 0.0, true),
    ((SELECT id FROM employes WHERE matricule = 'EMP002'), CURRENT_TIMESTAMP - INTERVAL '1 day' + INTERVAL '8 hours', CURRENT_TIMESTAMP - INTERVAL '1 day' + INTERVAL '17 hours', 'NORMAL', 9.0, 1.0, true),
    ((SELECT id FROM employes WHERE matricule = 'EMP003'), CURRENT_TIMESTAMP - INTERVAL '1 day' + INTERVAL '14 hours', CURRENT_TIMESTAMP - INTERVAL '1 day' + INTERVAL '22 hours', 'NORMAL', 8.0, 0.0, true),

    -- Avant-hier
    ((SELECT id FROM employes WHERE matricule = 'EMP001'), CURRENT_TIMESTAMP - INTERVAL '2 days' + INTERVAL '8 hours', CURRENT_TIMESTAMP - INTERVAL '2 days' + INTERVAL '16 hours 30 minutes', 'NORMAL', 8.5, 0.5, true),
    ((SELECT id FROM employes WHERE matricule = 'EMP004'), CURRENT_TIMESTAMP - INTERVAL '2 days' + INTERVAL '8 hours', CURRENT_TIMESTAMP - INTERVAL '2 days' + INTERVAL '18 hours', 'HEURES_SUP', 10.0, 2.0, true);

-- Insertion des évaluations
INSERT INTO evaluations (employe_id, evaluateur_id, date_evaluation, periode_evaluee, note_globale, note_ponctualite, note_qualite_travail, note_productivite, note_relation_client, note_esprit_equipe, points_forts, points_ameliorer, objectifs, commentaires, valide)
VALUES
    ((SELECT id FROM employes WHERE matricule = 'EMP001'),
     (SELECT id FROM employes WHERE matricule = 'EMP002'),
     CURRENT_DATE - INTERVAL '30 days',
     'Q4 2024',
     4,
     5, 4, 4, 5, 4,
     'Excellente ponctualité, très bon relationnel client, polyvalente',
     'Améliorer la rapidité de traitement en période de forte affluence',
     'Formation sur le nouveau système de caisse, objectif de 95% de satisfaction client',
     'Employée modèle, très appréciée de l''équipe et des clients',
     true),

    ((SELECT id FROM employes WHERE matricule = 'EMP003'),
     (SELECT id FROM employes WHERE matricule = 'EMP004'),
     CURRENT_DATE - INTERVAL '45 days',
     'Q4 2024',
     3,
     3, 4, 3, 4, 3,
     'Bonne connaissance produits, serviable',
     'Ponctualité à améliorer, mieux gérer les conflits',
     'Arriver 15 minutes avant le début du service, formation gestion de conflits',
     'Bon potentiel, nécessite un encadrement pour progresser',
     true);

-- Insertion des documents RH
INSERT INTO documents (employe_id, titre, type, chemin_fichier, nom_fichier, taille_fichier, type_mime, description, date_ajout, confidentiel, date_expiration)
VALUES
    ((SELECT id FROM employes WHERE matricule = 'EMP001'), 'Contrat CDI - Sarah Dupont', 'CONTRAT', '/documents/rh/contrats/emp001_contrat.pdf', 'emp001_contrat.pdf', 245678, 'application/pdf', 'Contrat de travail CDI signé le 15/01/2020', NOW() - INTERVAL '5 years', true, NULL),
    ((SELECT id FROM employes WHERE matricule = 'EMP001'), 'Fiche de Paie Décembre 2024', 'FICHE_PAIE', '/documents/rh/paies/emp001_paie_122024.pdf', 'emp001_paie_122024.pdf', 89456, 'application/pdf', 'Bulletin de salaire décembre 2024', NOW() - INTERVAL '1 month', true, NULL),
    ((SELECT id FROM employes WHERE matricule = 'EMP005'), 'Certificat Médical', 'CERTIFICAT_MEDICAL', '/documents/rh/medical/emp005_certificat.pdf', 'emp005_certificat.pdf', 156789, 'application/pdf', 'Arrêt maladie du ' || TO_CHAR(CURRENT_DATE - INTERVAL '2 days', 'DD/MM/YYYY'), NOW() - INTERVAL '2 days', false, NULL),
    ((SELECT id FROM employes WHERE matricule = 'EMP003'), 'CV - Marie Traoré', 'CV', '/documents/rh/cv/emp003_cv.pdf', 'emp003_cv.pdf', 567890, 'application/pdf', 'Curriculum Vitae', NOW() - INTERVAL '6 years', false, NULL),
    ((SELECT id FROM employes WHERE matricule = 'EMP001'), 'Attestation Formation Caisse', 'FORMATION', '/documents/rh/formations/emp001_formation_caisse.pdf', 'emp001_formation_caisse.pdf', 123456, 'application/pdf', 'Formation système de caisse - Janvier 2024', NOW() - INTERVAL '11 months', false, NULL);

-- Requête de vérification des données RH
SELECT
    'Employés actifs' AS categorie,
    COUNT(*) AS nombre
FROM employes
WHERE statut = 'ACTIF'
UNION ALL
SELECT
    'Absences en attente',
    COUNT(*)
FROM absences
WHERE statut = 'EN_ATTENTE'
UNION ALL
SELECT
    'Plannings validés cette semaine',
    COUNT(*)
FROM plannings
WHERE valide = true
AND date BETWEEN CURRENT_DATE AND CURRENT_DATE + INTERVAL '7 days'
UNION ALL
SELECT
    'Pointages non validés',
    COUNT(*)
FROM pointages
WHERE valide = false;
