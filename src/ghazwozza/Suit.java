package ghazwozza;

public enum Suit {
    SPADE("Spades"),
    CLUB("Clubs"),
    DIAMOND("Diamonds"),
    HEART("Hearts");

    private String pluralName;

    private Suit(String pluralName) {
        this.pluralName = pluralName;
    }

    /**
     * Unlike {@code toString}, name is plural.
     * @return
     */
    public String getPluralName() {
        return pluralName;
    }
}
