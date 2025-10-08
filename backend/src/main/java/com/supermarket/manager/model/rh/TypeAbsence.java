package com.supermarket.manager.model.rh;

public enum TypeAbsence {
    CONGE_PAYE("Congé Payé", true),
    CONGE_SANS_SOLDE("Congé Sans Solde", false),
    MALADIE("Arrêt Maladie", true),
    MATERNITE("Congé Maternité", true),
    PATERNITE("Congé Paternité", true),
    FORMATION("Formation", true),
    EVENEMENT_FAMILIAL("Événement Familial", true),
    RTT("RTT", true),
    ABSENCE_INJUSTIFIEE("Absence Injustifiée", false);
    
    private final String libelle;
    private final boolean remunere;
    
    TypeAbsence(String libelle, boolean remunere) {
        this.libelle = libelle;
        this.remunere = remunere;
    }
    
    public String getLibelle() {
        return libelle;
    }
    
    public boolean estRemunere() {
        return remunere;
    }
}

