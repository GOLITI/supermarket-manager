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

