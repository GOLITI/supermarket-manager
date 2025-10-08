package comcom.supermarket.manager.model.client;

public enum TypeMouvement {
    GAIN_ACHAT("Gain par achat"),
    GAIN_BONUS("Bonus offert"),
    GAIN_PARRAINAGE("Gain par parrainage"),
    UTILISATION("Utilisation de points"),
    EXPIRATION("Expiration de points"),
    AJUSTEMENT("Ajustement manuel");

    private final String libelle;

    TypeMouvement(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}

