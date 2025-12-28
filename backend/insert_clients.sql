-- Script pour ajouter les clients de test
-- À exécuter manuellement dans la base de données

-- Insertion des clients de test avec profils variés
INSERT INTO clients (
    numero_client, nom, prenom, email, telephone, adresse, ville, code_postal,
    date_naissance, date_inscription, statut, segment,
    numero_carte_fidelite, points_fidelite, niveau_fidelite,
    total_achats, nombre_achats, panier_moyen, dernier_achat,
    accepte_email, accepte_sms, accepte_notifications,
    date_creation, date_modification
)
VALUES
    -- Client VIP - Niveau Diamant
    ('CLI1730000001', 'Kouassi', 'Adjoua', 'adjoua.kouassi@email.ci', '+225 0701020304',
     '12 Boulevard de la République', 'Abidjan', '225',
     '1980-05-15', CURRENT_DATE - INTERVAL '3 years', 'ACTIF', 'VIP',
     'CF1730000001', 3500, 'DIAMANT',
     850000.00, 120, 7083.33, CURRENT_DATE - INTERVAL '2 days',
     true, true, true,
     CURRENT_DATE - INTERVAL '3 years', NOW()),

    -- Client Premium - Niveau Or
    ('CLI1730000002', 'Traoré', 'Mamadou', 'mamadou.traore@email.ci', '+225 0702030405',
     '45 Avenue Houphouët-Boigny', 'Abidjan', '225',
     '1985-08-22', CURRENT_DATE - INTERVAL '2 years', 'ACTIF', 'PREMIUM',
     'CF1730000002', 1800, 'OR',
     420000.00, 75, 5600.00, CURRENT_DATE - INTERVAL '5 days',
     true, false, true,
     CURRENT_DATE - INTERVAL '2 years', NOW()),

    -- Client Régulier - Niveau Argent
    ('CLI1730000003', 'N''Guessan', 'Aya', 'aya.nguessan@email.ci', '+225 0703040506',
     '78 Rue du Commerce', 'Abidjan', '225',
     '1992-03-10', CURRENT_DATE - INTERVAL '1 year', 'ACTIF', 'REGULIER',
     'CF1730000003', 850, 'ARGENT',
     180000.00, 45, 4000.00, CURRENT_DATE - INTERVAL '1 week',
     true, true, true,
     CURRENT_DATE - INTERVAL '1 year', NOW()),

    -- Client Occasionnel - Niveau Bronze
    ('CLI1730000004', 'Bamba', 'Issouf', 'issouf.bamba@email.ci', '+225 0704050607',
     '23 Quartier Biétry', 'Abidjan', '225',
     '1995-11-28', CURRENT_DATE - INTERVAL '6 months', 'ACTIF', 'OCCASIONNEL',
     'CF1730000004', 250, 'BRONZE',
     65000.00, 12, 5416.67, CURRENT_DATE - INTERVAL '3 weeks',
     false, false, true,
     CURRENT_DATE - INTERVAL '6 months', NOW()),

    -- Client Nouveau - Bronze
    ('CLI1730000005', 'Yao', 'Célestine', 'celestine.yao@email.ci', '+225 0705060708',
     '56 Cocody Angré', 'Abidjan', '225',
     '1988-07-14', CURRENT_DATE - INTERVAL '1 month', 'ACTIF', 'OCCASIONNEL',
     'CF1730000005', 150, 'BRONZE',
     25000.00, 3, 8333.33, CURRENT_DATE - INTERVAL '5 days',
     true, true, true,
     CURRENT_DATE - INTERVAL '1 month', NOW()),

    -- Client Premium actif - Niveau Or
    ('CLI1730000006', 'Koné', 'Ibrahim', 'ibrahim.kone@email.ci', '+225 0706070809',
     '89 Plateau Rue des Affaires', 'Abidjan', '225',
     '1978-12-03', CURRENT_DATE - INTERVAL '4 years', 'ACTIF', 'PREMIUM',
     'CF1730000006', 2200, 'OR',
     520000.00, 95, 5473.68, CURRENT_DATE - INTERVAL '1 day',
     true, true, true,
     CURRENT_DATE - INTERVAL '4 years', NOW()),

    -- Client Régulier - Argent
    ('CLI1730000007', 'Diabaté', 'Mariam', 'mariam.diabate@email.ci', '+225 0707080910',
     '34 Marcory Zone 4', 'Abidjan', '225',
     '1990-02-18', CURRENT_DATE - INTERVAL '18 months', 'ACTIF', 'REGULIER',
     'CF1730000007', 950, 'ARGENT',
     195000.00, 52, 3750.00, CURRENT_DATE - INTERVAL '4 days',
     true, false, true,
     CURRENT_DATE - INTERVAL '18 months', NOW()),

    -- Client VIP - Diamant
    ('CLI1730000008', 'Ouattara', 'Aïssata', 'aissata.ouattara@email.ci', '+225 0708091011',
     '12 Deux Plateaux Vallon', 'Abidjan', '225',

