package ghazwozza;

import ghazwozza.ColUtils;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GameUtils {

    public static final int blackjack = 21;

    public static final String indent = "  ";

    public static Set<Integer> getTotals(Collection<Card> cards) {
        List<Integer> totals = new ArrayList<>();
        totals.add(0);
        for (Card card : cards) {
            int[] values = card.getRank().getValues();
            int totalsSize = totals.size();
            for (int i = 0; i < totalsSize; i++) {
                int total = totals.get(i);
                for (int j = 0; j < values.length; j++) {
                    int newTotal = total + values[j];
                    if (j == 0) {
                        totals.set(i, newTotal);
                    } else {
                        totals.add(newTotal);
                    }
                }
            }
        }
        return new HashSet<>(totals);
    }

    public static boolean isBust(Collection<Card> cards) {
        if (cards.isEmpty()) {
            return false;
        }
        int minTotal = Collections.min(getTotals(cards));
        return minTotal > blackjack;
    }

    public static boolean isBlackjack(Collection<Card> cards) {
        return cards.size() == 2 && containsAce(cards) && containsTen(cards);
    }
    
    private static boolean containsTen(Collection<Card> cards) {
        return ColUtils.contains(cards, new ColUtils.Predicate<Card>() {
            @Override
            public boolean eval(Card item) {
                for (int value : item.getRank().getValues()) {
                    if (value == 10) {
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private static boolean containsAce(Collection<Card> cards) {
        return ColUtils.contains(cards, new ColUtils.Predicate<Card>() {
            @Override
            public boolean eval(Card item) {
                return item.getRank().equals(Rank.ACE);
            }
        });
    }

    public static Set<Integer> nonBustTotals(Collection<Card> cards) {
        Set<Integer> allTotals = getTotals(cards);
        Set<Integer> nonBustTotals = new HashSet<>();
        for (int total : allTotals) {
            if (total <= blackjack) {
                nonBustTotals.add(total);
            }
        }
        return nonBustTotals;
    }

    public static int maxNonBustTotal(Collection<Card> cards) {
        Collection<Integer> nonBustTotals = nonBustTotals(cards);
        return nonBustTotals.isEmpty() ? -1 : Collections.max(nonBustTotals);
    }

    public static String getDisplayTotal(Collection<Card> cards) {
        Set<Integer> nonBustTotals = nonBustTotals(cards);
        if (nonBustTotals.isEmpty()) {
            return Collections.min(getTotals(cards)).toString();
        }
        if (isBlackjack(cards)) {
            return "Blackjack";
        }
        boolean soft = nonBustTotals.size() > 1;
        int maxTotal = Collections.max(nonBustTotals);
        return (soft ? "Soft " : "") + maxTotal;
    }

    public static int compareHands(Collection<Card> hand1,
                                   Collection<Card> hand2) {
        boolean hand1Bust = isBust(hand1);
        boolean hand1Blackjack = hand1Bust ? false : isBlackjack(hand1);
        boolean hand2Bust = isBust(hand2);
        boolean hand2Blackjack = hand2Bust ? false : isBlackjack(hand2);
        if ((hand1Bust && hand2Bust) || (hand1Blackjack && hand2Blackjack)) {
            return 0;
        }
        if (hand1Bust) {
            return -1;
        }
        if (hand1Blackjack) {
            return 1;
        }
        return Integer.compare(maxNonBustTotal(hand1), maxNonBustTotal(hand2));
    }

    public static void printHand(PrintStream out,
                                 String playerName,
                                 Collection<Card> cards) {
        out.println();
        out.println(playerName + "'s cards are:");
        for (Card card : cards) {
            out.println(GameUtils.indent + card);
        }
        out.println(playerName
                + "'s total: "
                + GameUtils.getDisplayTotal(cards));
    }
}
