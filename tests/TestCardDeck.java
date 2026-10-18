import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;

public class TestCardDeck {
    @Test
    public void testFifoOrder() {
        CardDeck deck = new CardDeck(1);
        deck.addCard(1);
        deck.addCard(6);
        deck.addCard(7);

        assertEquals("First card in should be first out to match FIFO", 1, deck.drawCard());
        assertEquals("Second card in should be second out to match FIFO", 6, deck.drawCard());
        assertEquals("Third card in should be third out to match FIFO", 7, deck.drawCard());
    }
}