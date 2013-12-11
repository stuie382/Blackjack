package ghazwozza;

public interface DeckIF {

    /**
     * Return's the top card on the deck and removes it from the deck.
     * 
     * @return
     */
    public Card take();
    
    /**
     * Returns the top card from the deck but doesn't remove it.
     * Will return {@code null} if the deck is empty.
     * @return
     */
    public Card peek();
    
    /**
     * Number of cards remaining in the deck.
     * @return
     */
    public int remaining();
    
    /**
     * Shuffles the deck.
     */
    public void shuffle();

}
