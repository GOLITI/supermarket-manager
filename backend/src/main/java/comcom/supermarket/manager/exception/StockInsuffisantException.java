package comcom.supermarket.manager.exception;

public class StockInsuffisantException extends RuntimeException {

    public StockInsuffisantException(String message) {
        super(message);
    }

    public StockInsuffisantException(String produit, Integer quantiteDemandee, Integer quantiteDisponible) {
        super(String.format("Stock insuffisant pour le produit '%s'. Demandé: %d, Disponible: %d",
            produit, quantiteDemandee, quantiteDisponible));
    }
}

