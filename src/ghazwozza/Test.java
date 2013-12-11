package ghazwozza;

import static ghazwozza.Rank.*;
import static ghazwozza.Suit.*;
import ghazwozza.StringUtils;

import java.util.Arrays;
import java.util.Collection;

public class Test {
    public static void main(String[] args) {
        Collection<Card> blackjack = Arrays.asList(new Card(HEART, JACK),
                                                   new Card(CLUB, ACE));
        Collection<Card> twentyOne = Arrays.asList(new Card(DIAMOND, TWO),
                                                   new Card(CLUB, EIGHT),
                                                   new Card(HEART, TWO),
                                                   new Card(CLUB, THREE),
                                                   new Card(CLUB, ACE),
                                                   new Card(DIAMOND, FIVE));
        System.out.println(GameUtils.isBlackjack(blackjack));
        System.out.println(GameUtils.getDisplayTotal(blackjack));
        System.out.println(GameUtils.getDisplayTotal(twentyOne));
        System.out.println(GameUtils.compareHands(blackjack, twentyOne));
        
        System.out.println();
        
        System.out.println(StringUtils.firstNChars("", 20));
    }
}
