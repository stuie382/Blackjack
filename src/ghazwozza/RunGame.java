package ghazwozza;

import ghazwozza.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RunGame {

    private static final int dealerStandsOn = 17;
    private static final long tableMinBet = 10;

    public static void main(String[] args)
        throws Exception {

        try (BufferedReader in = new BufferedReader(new InputStreamReader(System.in))) {
            PrintStream out = System.out;

            PlayerIF userPlayer = new HumanPlayer(in, out);
            
            while (userPlayer.getMoney() >= tableMinBet) {
                playRound(out, Arrays.asList(userPlayer));
            }
        }
    }

    private static void playRound(PrintStream out, List<PlayerIF> players) {
        Deck deck = new Deck(8);

        // deal for dealer
        List<Card> dealerCards = new ArrayList<>(Arrays.asList(deck.take()));

        // deal for each player
        List<BettingPosition> bps = new ArrayList<>(players.size());
        for (PlayerIF player : players) {
            List<Card> cards = Arrays.asList(deck.take(), deck.take());

            long bet = player.decideInitialBet(cards, dealerCards, tableMinBet);
            player.takeMoney(bet);
            BettingPosition bp = new BettingPosition(player, cards, bet);
            bps.add(bp);
        }

        printDealersHand(out, dealerCards);

        // players play
        for (BettingPosition bp : bps) {
            for (boolean nextPlayer = false; !nextPlayer;) {
                PlayerDecision dec = bp.getPlayer().decideDecision(bp.getCards(),
                                                                   dealerCards);
                switch (dec) {
                    case HIT:
                        bp.addCard(deck.take());
                        if (GameUtils.isBust(bp.getCards())) {
                            bp.setBust();
                            nextPlayer = true;
                        }
                        break;
                    case STAND:
                        nextPlayer = true;
                        break;
                }
            }
        }
        
        if (!allBust(bps)) {
            // dealer plays
            out.println();
            out.println("Dealer has " + StringUtils.join(dealerCards, ", "));
            while (!dealerMustStand(dealerCards)) {
                Card dealersCard = deck.take();
                dealerCards.add(dealersCard);
                out.println("Dealer hits, receives " + dealersCard);
            }
            out.println(GameUtils.isBust(dealerCards)
                    ? "Dealer busts!"
                    : "Dealer stands on " + GameUtils.getDisplayTotal(dealerCards));
            
    
            // sort out winnings
            for (BettingPosition bp : bps) {
                if (bp.isStillIn()) {
                    long bet = bp.getBet();
                    int compare = GameUtils.compareHands(dealerCards, bp.getCards());
                    if (compare == 0) {
                        bp.getPlayer().addMoney(bet);
                    } else if (GameUtils.isBlackjack(bp.getCards()) && compare != 0) {
                        long winnings = (bet * 3) / 2;
                        bp.getPlayer().addMoney(bet + winnings);
                    } else if (compare < 0) {
                        bp.getPlayer().addMoney(bet * 2);
                    }
                }
            }
        }
    }
    
    static boolean dealerMustStand(Collection<Card> dealersCards) {
        return GameUtils.isBust(dealersCards) || Collections.max(GameUtils.nonBustTotals(dealersCards)) >= dealerStandsOn;
    }

    private static void printDealersHand(PrintStream out, Collection<Card> cards) {
        GameUtils.printHand(out, "Dealer", cards);
    }
    
    private static boolean allBust(Collection<BettingPosition> bps) {
        for (BettingPosition bp : bps) {
            if (bp.isStillIn()) {
                return false;
            }
        }
        return true;
    }

    private static class BettingPosition {
        private final PlayerIF player;
        private final List<Card> cards;
        private final long bet;
        private boolean stillIn = true;

        private BettingPosition(PlayerIF player, List<Card> cards, long bet) {
            super();
            this.player = player;
            this.cards = new ArrayList<>(cards);
            this.bet = bet;
        }

        public PlayerIF getPlayer() {
            return player;
        }

        public List<Card> getCards() {
            return cards;
        }

        public long getBet() {
            return bet;
        }

        public void addCard(Card card) {
            cards.add(card);
        }

        public boolean isStillIn() {
            return stillIn;
        }

        public void setBust() {
            player.notifyBusted(cards);
            this.stillIn = false;
        }
    }
}
