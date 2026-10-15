import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Player implements Runnable {
   private int playerIndex;
   private CardDeck drawDeck;
   private CardDeck depositDeck;
   private List<Integer> hand = new ArrayList<>();
   private FileWriter writer;
    
    public Player(int currentPlayer, CardDeck drawDeck, CardDeck depositDeck) {
        this.playerIndex = currentPlayer;
        this.drawDeck = drawDeck;
        this.depositDeck = depositDeck;
        
        // Creates file
        try {
            writer = new FileWriter("player" + playerIndex + "_output.txt");
        } catch (IOException e) {
            System.out.println("Failed to create file");
            e.printStackTrace();
        }
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

    public synchronized void move() {
        // Takes card from deck
        int drawnCard = drawDeck.drawCard();

        // Logs value of drawn card to .txt file
        try {
            writer.write("player " + playerIndex + " drws a " + drawnCard + " from deck " + drawnCard);
        } catch (IOException e) {
            System.out.println("Fsiled to write to file");
            e.printStackTrace();
        }

        
        
        hand.add(drawnCard);

        
    }

    @Override 
    public void run() {
        if (checkHand()) {
            System.out.println("Player " + playerIndex + " wins");
        }
    }
}
