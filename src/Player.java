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
            writer.flush();
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
            writer.write("\nplayer " + playerIndex + " draws a " + drawnCard + " from deck " + drawDeck.getID());
            writer.flush();
        } catch (IOException e) {
            System.out.println("Failed to write to file");
            e.printStackTrace();
        }

        // Picks random card from hand to discard
        boolean discardFlag = false;
        while (discardFlag != true) {
            int randomNum = (int)(Math.random() * hand.size());
            int cardToDiscard = hand.get(randomNum);
            if (cardToDiscard != playerIndex) {
                // Removes random non card from
                depositDeck.addCard(cardToDiscard);
                hand.remove(randomNum);
                hand.add(drawnCard);
                // Logs value of deposited card to .txt file
                try {
                    writer.write("\nplayer " + playerIndex + " discards a " + cardToDiscard + " to deck " + depositDeck.getID());
                    writer.write("\nplayer " + playerIndex + " current hand " + hand);
                    writer.flush();
                } catch (IOException e) {
                    System.out.println("Fsiled to write to file");
                    e.printStackTrace();
                }

                discardFlag = true;
            }
        }
    }

    @Override 
    public void run() {
        try {
            writer.write("\nplayer " + playerIndex + " initial hand " + hand);
            writer.flush();
        } catch (IOException e) {
            System.out.println("Failed to write to file");
            e.printStackTrace();
        }

        if (checkHand()) {
            System.out.println("Player " + playerIndex + " wins");
            try {
                writer.write("\nplayer " + playerIndex + " wins");
                writer.write("\nplayer " + playerIndex + " exits");
                writer.write("\nplayer " + playerIndex + " final hand " + hand);
                writer.close();
            } catch (IOException e) {
                System.out.println("Failed to write to file");
                e.printStackTrace();
            }
            CardGame.gameWon = true;
            CardGame.winningPlayer = playerIndex;
            return;
        }

        while (!CardGame.gameWon) {
            move();

            if (checkHand()) {
                System.out.println("Player " + playerIndex + " wins");
                try {
                    writer.write("\nplayer " + playerIndex + " wins");
                    writer.write("\nplayer " + playerIndex + " exits");
                    writer.write("\nplayer " + playerIndex + " final hand " + hand);
                    writer.close();
                } catch (IOException e) {
                    System.out.println("Fsiled to write to file");
                    e.printStackTrace();
                }
                CardGame.gameWon = true;
                CardGame.winningPlayer = playerIndex;
                break;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        if (CardGame.winningPlayer != playerIndex && CardGame.winningPlayer != -1) {
            try {
                writer.write("\nplayer " + CardGame.winningPlayer + " has informed player " + playerIndex + " that player  "+ CardGame.winningPlayer + " has won");
                writer.write("\nplayer " + playerIndex + " exits");
                writer.write("\nplayer " + playerIndex + " hand: " + hand);
                writer.close();
            } catch (IOException e) {
                System.out.println("Fsiled to write to file");
                e.printStackTrace();
            }
        }

    }
}
