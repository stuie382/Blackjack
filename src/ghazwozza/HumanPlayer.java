package ghazwozza;

import ghazwozza.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Collection;
import java.util.List;

public class HumanPlayer
    implements PlayerIF {

    private final BufferedReader in;
    private final PrintStream out;

    private long money = 0;
    private final String name;

    public HumanPlayer(BufferedReader in, PrintStream out) {
        this.in = in;
        this.out = out;

        out.println("What's your name?");
        name = readLine();

        out.println("How much money do you have?");
        money = askForMonetaryAmount(0, 100);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getMoney() {
        return money;
    }
    
    @Override
    public void takeMoney(long amount) {
        this.money -= amount;
    }

    @Override
    public void addMoney(long amount) {
        println();
        println("You win " + amount);
        this.money += amount;
        println("Your total: " + this.money); 
    }

    @Override
    public long decideInitialBet(List<Card> playerCards,
                                 List<Card> dealerCards,
                                 long minBet) {
        println();
        println("Your total: " + getMoney());
        println("How much do you want to bet?");
        println("(Press Enter to bet minimum of " + minBet + ")");
        return askForMonetaryAmount(minBet, minBet);
    }

    @Override
    public PlayerDecision decideDecision(List<Card> playerCards,
                                         List<Card> dealerCards) {
        printHand(playerCards);
        println();
        println("Type H to hit, S to stand.");
        for (;;) {
            String input = readLine();
            switch (StringUtils.firstNChars(input, 1).toUpperCase()) {
                case "H":
                    return PlayerDecision.HIT;
                case "S":
                    return PlayerDecision.STAND;
                default:
                    println("Invalid input, try again.");
            }
        }

    }

    @Override
    public void notifyBusted(List<Card> playerCards) {
        printHand(playerCards);
        println(getName() + " busted!");
    }

    private final String readLine() {
        try {
            return in.readLine();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    private final long askForMonetaryAmount(long min,
                                            long defaultAmount) {
        while (true) {
            Long amount = askForMonetaryAmount();
            if (amount == null) {
                return defaultAmount;
            } else if (amount < min) {
                println("Not enought! Minimum is " + min);
            } else {
                return amount;
            }
        }
    }

    private final Long askForMonetaryAmount() {
        while (true) {
            String input = readLine();
            if (input == null || input.isEmpty()) {
                return null;
            }
            try {
                long amount = Long.parseLong(input);
                return amount;
            } catch (NumberFormatException ex) {
                println("Not a valid number, try again.");
                continue;
            }
        }
    }

    private void println(Object line) {
        out.println(line.toString());
    }

    private void println() {
        out.println();
    }
    
    private void printHand(Collection<Card> cards) {
        GameUtils.printHand(out, getName(), cards);
    }

}
