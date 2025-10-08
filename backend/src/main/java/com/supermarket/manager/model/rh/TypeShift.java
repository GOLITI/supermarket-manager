package com.supermarket.manager.model.rh;

public enum TypeShift {
    MATIN("Matin", "06:00-14:00"),
    APRES_MIDI("Après-midi", "14:00-22:00"),
    NUIT("Nuit", "22:00-06:00"),
    JOURNEE_COMPLETE("Journée complète", "08:00-17:00");
    
    private final String libelle;
    private final String horaires;
    
    TypeShift(String libelle, String horaires) {
        this.libelle = libelle;
        this.horaires = horaires;
    }
    
    public String getLibelle() {
        return libelle;
    }
    
    public String getHoraires() {
        return horaires;
    }
}

