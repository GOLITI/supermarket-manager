package comcom.supermarket.manager.model.client;

public enum StatutCampagne {
    BROUILLON("Brouillon"),
    PROGRAMMEE("Programmée"),
    EN_COURS("En cours"),
    TERMINEE("Terminée"),
    ANNULEE("Annulée");

    private final String libelle;

    StatutCampagne(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}

