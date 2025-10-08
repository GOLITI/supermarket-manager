package com.supermarket.manager.model.rh;

public enum StatutDemande {
    EN_ATTENTE("En attente"),
    APPROUVEE("Approuvée"),
    REFUSEE("Refusée"),
    ANNULEE("Annulée");
    
    private final String libelle;
    
    StatutDemande(String libelle) {
        this.libelle = libelle;
    }
    
    public String getLibelle() {
        return libelle;
    }
}

