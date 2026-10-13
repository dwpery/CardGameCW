import java.util.ArrayList;
import java.util.List;

public class Player {
   private int playerIndex;
   private CardDeck drawDeck;
   private CardDeck depositDeck;
   private List<Integer> hand = new ArrayList<>();
    
    public Player(int currentPlayer, CardDeck drawDeck, CardDeck depositDeck) {
        this.playerIndex = currentPlayer;
        this.drawDeck = drawDeck;
        this.depositDeck = depositDeck;
    }

    public void move() {

    }
}
