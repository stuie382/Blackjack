package ghazwozza;

import java.util.List;

public interface PlayerIF {
    
    public String getName();
    
    public long getMoney();
    
    public void takeMoney(long amount);
    
    public void addMoney(long amount);
    
    public long decideInitialBet(List<Card> playerCards,
                                 List<Card> dealerCards,
                                 long minBet);
    
    public PlayerDecision decideDecision(List<Card> playerCards,
                                         List<Card> dealerCards);
    
    public void notifyBusted(List<Card> playerCards);
}
