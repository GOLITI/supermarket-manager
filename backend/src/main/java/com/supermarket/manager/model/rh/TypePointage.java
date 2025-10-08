package com.supermarket.manager.model.rh;

public enum TypePointage {
    NORMAL("Normal"),
    HEURES_SUP("Heures Supplémentaires"),
    JOUR_FERIE("Jour Férié"),
    NUIT("Nuit"),
    DIMANCHE("Dimanche");
    
    private final String libelle;
    
    TypePointage(String libelle) {
        this.libelle = libelle;
    }
    
    public String getLibelle() {
        return libelle;
    }
}

