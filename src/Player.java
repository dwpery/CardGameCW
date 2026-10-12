import java.util.ArrayList;
import java.util.List;

public class Player {
   private int playerIndex;
   private CardDeck drawDeck;
   private CardDeck depositDeck;
   private List<Integer> hand = new ArrayList<>();
    
    public Player(int currentPlayer, int totalPlayer) {
        this.playerIndex = currentPlayer;
        //this.drawDeck = ;
        
        // Allocates deposit deck value
        if (currentPlayer == totalPlayer) {
            // Loops back to Deck 1
            //depositDeck = 0;
        } else {
            //depositDeck = currentPlayer + 1;
        }
    }

    public void move() {

    }
}
