package comcom.supermarket.manager.model.client;

public enum StatutClient {
    ACTIF("Actif"),
    INACTIF("Inactif"),
    SUSPENDU("Suspendu"),
    BLOQUE("Bloqué");

    private final String libelle;

    StatutClient(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}

