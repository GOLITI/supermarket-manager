package com.supermarket.manager.model.caisse;

public enum MethodePaiement {
    ESPECES("Espèces"),
    CARTE_BANCAIRE("Carte Bancaire"),
    CARTE_CREDIT("Carte de Crédit"),
    CHEQUE("Chèque"),
    MOBILE_PAYMENT("Paiement Mobile"),
    TICKET_RESTAURANT("Ticket Restaurant"),
    BON_ACHAT("Bon d'Achat");

    private final String libelle;

    MethodePaiement(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}

