package com.supermarket.manager.model.client;

import java.math.BigDecimal;

public enum NiveauFidelite {
    BRONZE("Bronze", 0, new BigDecimal("1.0"), new BigDecimal("0")),
    ARGENT("Argent", 500, new BigDecimal("1.5"), new BigDecimal("500")),
    OR("Or", 2000, new BigDecimal("2.0"), new BigDecimal("1000")),
    DIAMANT("Diamant", 5000, new BigDecimal("2.5"), new BigDecimal("2000"));

    private final String libelle;
    private final int seuilPoints;
    private final BigDecimal multiplicateurPoints;
    private final BigDecimal reductionMaximale; // en FCFA

    NiveauFidelite(String libelle, int seuilPoints, BigDecimal multiplicateurPoints, BigDecimal reductionMaximale) {
        this.libelle = libelle;
        this.seuilPoints = seuilPoints;
        this.multiplicateurPoints = multiplicateurPoints;
        this.reductionMaximale = reductionMaximale;
    }

    public String getLibelle() {
        return libelle;
    }

    public int getSeuilPoints() {
        return seuilPoints;
    }

    public BigDecimal getMultiplicateurPoints() {
        return multiplicateurPoints;
    }

    public BigDecimal getReductionMaximale() {
        return reductionMaximale;
    }

    public static NiveauFidelite determinerNiveau(int points) {
        if (points >= DIAMANT.seuilPoints) return DIAMANT;
        if (points >= OR.seuilPoints) return OR;
        if (points >= ARGENT.seuilPoints) return ARGENT;
        return BRONZE;
    }
}

