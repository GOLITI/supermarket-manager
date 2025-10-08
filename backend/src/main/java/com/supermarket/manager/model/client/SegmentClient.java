package com.supermarket.manager.model.client;

public enum SegmentClient {
    VIP("VIP - Plus de 500 000 FCFA"),
    PREMIUM("Premium - Plus de 200 000 FCFA"),
    REGULIER("Régulier - Au moins 5 achats"),
    OCCASIONNEL("Occasionnel");

    private final String description;

    SegmentClient(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

