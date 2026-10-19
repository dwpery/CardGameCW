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
        
        // Creates corresponding output file
        try {
            writer = new FileWriter("player" + playerIndex + "_output.txt");
            writer.flush();
        } catch (IOException e) {
            System.out.println("Failed to create file");
            e.printStackTrace();
        }
    }

    // Appends card value to hand
    public void addCardToHand(int cardValue) {
        hand.add(cardValue);
    }

    // Returns the players hand
    public List<Integer> getHand() {
        return hand;
    }

    // Checks hand's state to verify wether all cards are equal
    public boolean checkHand() {
        boolean winFlag = true;
        
        // Compares all values in hand to the 1st, if one doesnt match then hand doesn't win
        int firstCard = hand.get(0);
        for (int i = 0; i < hand.size(); i++) {
            if (firstCard != hand.get(i)) {
                winFlag = false;
            }
        }
        
        return winFlag;
    }

    // Main method that contains the players atomic movement logic for their WHOLE turn 
    // Synchronised to make it thread-safe
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
        // Loops until card is finally discarded
        while (discardFlag != true) {
            int randomNum = (int)(Math.random() * hand.size());
            int cardToDiscard = hand.get(randomNum);
            // Checks randomly picked card isnt this players prefered denomination
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
                    System.out.println("Failed to write to file");
                    e.printStackTrace();
                }

                discardFlag = true;
            }
        }
    }

    // 
    @Override 
    public void run() {
        // Prints initial hand at start of each game
        try {
            writer.write("\nplayer " + playerIndex + " initial hand " + hand);
            writer.flush();
        } catch (IOException e) {
            System.out.println("Failed to write to file");
            e.printStackTrace();
        }

        // Checks if players starting hand meets win criteria
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

            // Updates global vars to inform other players this player has won 
            CardGame.gameWon = true;
            CardGame.winningPlayer = playerIndex;
            return;
        }

        // Continues taking turns until any player has won
        while (!CardGame.gameWon) {
            // Performs main move logic during turn
            move();

            // Checks if players hand meets win criteria after they've drawn card
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

                // Updates global vars to inform other players this player has won
                CardGame.gameWon = true;
                CardGame.winningPlayer = playerIndex;
                break;
            }

            try {
                // Pausing threads paces gameplay allows screen writers to finish before thread is joined
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        // Checks wether game is won but NOT by this player
        if (CardGame.winningPlayer != playerIndex && CardGame.winningPlayer != -1) {
            try {
                writer.write("\nplayer " + CardGame.winningPlayer + " has informed player " + playerIndex + " that player  "+ CardGame.winningPlayer + " has won");
                writer.write("\nplayer " + playerIndex + " exits");
                writer.write("\nplayer " + playerIndex + " hand: " + hand);
                writer.close();
            } catch (IOException e) {
                System.out.println("Failed to write to file");
                e.printStackTrace();
            }
        }

    }
}
