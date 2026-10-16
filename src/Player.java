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
   boolean gameWon = false;
    
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
            writer.write("player " + playerIndex + " draws a " + drawnCard + " from deck " + drawDeck.getID());
        } catch (IOException e) {
            System.out.println("Fsiled to write to file");
            e.printStackTrace();
        }

        // Picks random card from hand to discard
        boolean discardFlag = false;
        while (discardFlag != true) {
            int randomNum = (int)(Math.random() * hand.size());
            int cardToDiscard = hand.get(randomNum);
            if (cardToDiscard != playerIndex) {
                depositDeck.addCard(cardToDiscard);
                hand.remove(randomNum);

                // Logs value of deposited card to .txt file
                try {
                    writer.write("player " + playerIndex + " discards a " + cardToDiscard + " to deck " + depositDeck.getID());
                    writer.write("player " + playerIndex + " current hand " + hand);
                } catch (IOException e) {
                    System.out.println("Fsiled to write to file");
                    e.printStackTrace();
                }

                discardFlag = true;
            }
        }

        
        
        hand.add(drawnCard);

        
    }

    @Override 
    public void run() {
        if (checkHand()) {
            System.out.println("Player " + playerIndex + " wins");
            gameWon = true;
            return;
        }

        while (!gameWon) {
            move();

            if (checkHand()) {
                System.out.println("Player " + playerIndex + " wins");
                gameWon = true;
                break;
            }
        }

    }
}
