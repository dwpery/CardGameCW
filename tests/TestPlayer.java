import java.io.File;
import org.junit.BeforeClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;

public class TestPlayer {
    // Used in multiple tests do declaring at class level
    CardDeck drawDeck;
    CardDeck depositDeck;
    Player player;
    
    // Generates new decks and player class for the start of each test
    @Before
    public void setUp() {
        drawDeck = new CardDeck(1);
        depositDeck = new CardDeck(2);
        player = new Player(1, drawDeck, depositDeck);
    }
    
    @Test
    public void testImmediateWinDetection() {
        player.addCardToHand(1);
        player.addCardToHand(1);
        player.addCardToHand(1);
        player.addCardToHand(1);

        assertTrue("Hand full of 4 identical cards should return true when checked if its won", player.checkHand());
    }

    @Test
    public void testRandomHandLoses() {
        player.addCardToHand(4);
        player.addCardToHand(1);
        player.addCardToHand(7);
        player.addCardToHand(1);

        assertFalse("Hand full of 4 different cards should return false when checked if its won", player.checkHand());
    }
}
