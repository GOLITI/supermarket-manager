package com.supermarket.manager.model.client;

public enum TypeOffre {
    REDUCTION_POURCENTAGE("Réduction en %"),
    REDUCTION_MONTANT("Réduction en FCFA"),
    POINTS_BONUS("Points bonus"),
    CADEAU("Cadeau offert"),
    LIVRAISON_GRATUITE("Livraison gratuite");

    private final String libelle;

    TypeOffre(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
