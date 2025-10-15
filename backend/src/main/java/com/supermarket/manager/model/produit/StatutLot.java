package com.supermarket.manager.model.produit;

public enum StatutLot {
    ACTIF("Actif - En stock"),
    RESERVE("Réservé"),
    EPUISE("Épuisé"),
    PERIME("Périmé"),
    RAPPELE("Rappelé"),
    BLOQUE("Bloqué - Contrôle qualité"),
    ARCHIVE("Archivé");

    private final String libelle;

    StatutLot(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}

