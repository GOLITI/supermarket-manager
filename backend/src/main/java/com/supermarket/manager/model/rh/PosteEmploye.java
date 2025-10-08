package com.supermarket.manager.model.rh;

public enum PosteEmploye {
    CAISSIER("Caissier", "CAISSE"),
    CHEF_CAISSIER("Chef Caissier", "CAISSE"),
    VENDEUR("Vendeur", "VENTE"),
    CHEF_RAYON("Chef de Rayon", "VENTE"),
    MAGASINIER("Magasinier", "STOCK"),
    RESPONSABLE_STOCK("Responsable Stock", "STOCK"),
    COMPTABLE("Comptable", "ADMINISTRATION"),
    RESPONSABLE_RH("Responsable RH", "ADMINISTRATION"),
    DIRECTEUR("Directeur", "DIRECTION"),
    AGENT_SECURITE("Agent de Sécurité", "SECURITE"),
    AGENT_ENTRETIEN("Agent d'Entretien", "ENTRETIEN");
    
    private final String libelle;
    private final String departement;
    
    PosteEmploye(String libelle, String departement) {
        this.libelle = libelle;
        this.departement = departement;
    }
    
    public String getLibelle() {
        return libelle;
    }
    
    public String getDepartement() {
        return departement;
    }
}

