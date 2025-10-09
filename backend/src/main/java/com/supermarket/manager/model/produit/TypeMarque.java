package com.supermarket.manager.model.produit;

public enum TypeMarque {
    MARQUE_PROPRE("Marque Propre"),
    GRANDE_MARQUE("Grande Marque"),
    MARQUE_DISTRIBUTEUR("Marque Distributeur"),
    GENERIQUE("Générique"),
    BIO("Bio/Équitable"),
    PREMIUM("Premium");

    private final String libelle;

    TypeMarque(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}

