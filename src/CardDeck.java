import java.util.ArrayList;
import java.util.List;

public class CardDeck {
    private List<Integer> cards = new ArrayList<>();
    private int deckID;

    public CardDeck(int deckNum) {
        this.deckID = deckNum;
    }

    // Used to populate cards list during setup AND when player discards card
    public void insertCard(int cardValue) {
        cards.add(cardValue);
    }

    // Used by player to take card from deck for their hand
    public int drawCard() {
        return cards.remove(0);
    }

    public List<Integer> getContents() {
        return new ArrayList<>(cards);
    }
}
