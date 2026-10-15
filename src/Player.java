import java.util.ArrayList;
import java.util.List;

public class Player implements Runnable {
   private int playerIndex;
   private CardDeck drawDeck;
   private CardDeck depositDeck;
   private List<Integer> hand = new ArrayList<>();
    
    public Player(int currentPlayer, CardDeck drawDeck, CardDeck depositDeck) {
        this.playerIndex = currentPlayer;
        this.drawDeck = drawDeck;
        this.depositDeck = depositDeck;
    }

    public void addCardToHand(int cardValue) {
        hand.add(cardValue);
    }

    public List<Integer> getHand() {
        return hand;
    }

    private boolean checkHand() {
        boolean winFlag = true;
        
        int firstCard = hand.get(0);
        for (int i = 0; i < hand.size(); i++) {
            if (firstCard != hand.get(i)) {
                winFlag = false;
            }
        }
        
        return winFlag;
    }

    @Override 
    public void run() {
        if (checkHand()) {
            System.out.println("Player " + playerIndex + " wins");
        }
    }
}
