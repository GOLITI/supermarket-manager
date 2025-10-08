package comcom.supermarket.manager.model.caisse;

import java.math.BigDecimal;

public enum NiveauFidelite {
    BRONZE(new BigDecimal("0.01"), new BigDecimal("0.00")),
    ARGENT(new BigDecimal("0.02"), new BigDecimal("0.02")),
    OR(new BigDecimal("0.03"), new BigDecimal("0.05")),
    PLATINE(new BigDecimal("0.05"), new BigDecimal("0.10"));

    private final BigDecimal tauxPoints;
    private final BigDecimal tauxRemise;

    NiveauFidelite(BigDecimal tauxPoints, BigDecimal tauxRemise) {
        this.tauxPoints = tauxPoints;
        this.tauxRemise = tauxRemise;
    }

    public BigDecimal getTauxPoints() {
        return tauxPoints;
    }

    public BigDecimal getTauxRemise() {
        return tauxRemise;
    }

    public BigDecimal calculerPoints(BigDecimal montant) {
        return montant.multiply(tauxPoints);
    }

    public BigDecimal calculerRemise(BigDecimal montant) {
        return montant.multiply(tauxRemise);
    }
}
