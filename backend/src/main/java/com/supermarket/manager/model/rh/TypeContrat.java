package com.supermarket.manager.model.rh;

public enum TypeContrat {
    CDI("Contrat à Durée Indéterminée"),
    CDD("Contrat à Durée Déterminée"),
    INTERIM("Intérim"),
    STAGE("Stage"),
    APPRENTISSAGE("Apprentissage"),
    TEMPS_PARTIEL("Temps Partiel");
    
    private final String libelle;
    
    TypeContrat(String libelle) {
        this.libelle = libelle;
    }
    
    public String getLibelle() {
        return libelle;
    }
}

