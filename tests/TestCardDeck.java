import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;

public class TestCardDeck {
    CardDeck deck;
    
    @Before
    public void setUp() {
        deck = new CardDeck(1);
    }

    // Ensures the First cards added to the deck are the first to be removed
    @Test
    public void testFifoOrder() {
        deck.addCard(1);
        deck.addCard(6);
        deck.addCard(7);

        assertEquals("First card in should be first out to match FIFO", 1, deck.drawCard());
        assertEquals("Second card in should be second out to match FIFO", 6, deck.drawCard());
        assertEquals("Third card in should be third out to match FIFO", 7, deck.drawCard());
    }

    // Test decks correctly store and return their assigned ID
    @Test 
    public void testDeckID() {
        int deckID = deck.getID();
        assertEquals("Deck ID should match the value passed into the constructor", 1, deckID);
    }

    // Tests drawing card properly removes it from deck
    @Test 
    public void testCardRemoval() {
        deck.addCard(1);
        deck.addCard(6);
        deck.addCard(7);

        int removedCard = deck.drawCard();

        assertEquals("Drawn card should equal 1", 1, removedCard);
        assertEquals("Deck should now have only 2 cards", 2, deck.getContents().size());
    } 
}