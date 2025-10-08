package comcom.supermarket.manager.model.client;

public enum TypeCampagne {
    EMAIL("Email"),
    SMS("SMS"),
    NOTIFICATION("Notification Push"),
    MULTI_CANAL("Multi-canal");

    private final String libelle;

    TypeCampagne(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}

