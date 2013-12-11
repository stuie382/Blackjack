package ghazwozza;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck
    implements DeckIF {

    List<Card> cards;

    public Deck() {
        this(1);
    }

    public Deck(int numStandardDecks) {
        cards = new ArrayList<>(Suit.values().length * Rank.values().length * numStandardDecks);
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                Card card = new Card(suit, rank);
                for (int i = 0; i < numStandardDecks; i++) {
                    cards.add(card);
                }
            }
        }
        shuffle();
    }

    @Override
    public Card take() {
        if (cards.isEmpty()) {
            throw new RuntimeException("Out of cards");
        }
        return cards.remove(0);
    }

    @Override
    public Card peek() {
        return cards.isEmpty()
                ? null
                : cards.get(0);
    }

    @Override
    public int remaining() {
        return cards.size();
    }

    @Override
    public void shuffle() {
        Collections.shuffle(cards);
    }

}
