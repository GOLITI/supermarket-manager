package com.supermarket.manager.model.rh;

public enum TypeDocument {
    CONTRAT("Contrat de Travail"),
    FICHE_PAIE("Fiche de Paie"),
    ATTESTATION("Attestation"),
    CV("Curriculum Vitae"),
    DIPLOME("Diplôme"),
    PIECE_IDENTITE("Pièce d'Identité"),
    JUSTIFICATIF_DOMICILE("Justificatif de Domicile"),
    CERTIFICAT_MEDICAL("Certificat Médical"),
    FORMATION("Document de Formation"),
    EVALUATION("Évaluation"),
    AUTRE("Autre");

    private final String libelle;

    TypeDocument(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}

