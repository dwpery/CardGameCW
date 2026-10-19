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
    
    // Tests if a player starts with a winning hand they're able to detect it
    @Test
    public void testImmediateWinDetection() {
        player.addCardToHand(1);
        player.addCardToHand(1);
        player.addCardToHand(1);
        player.addCardToHand(1);

        assertTrue("Hand full of 4 identical cards should return true when checked if its won", player.checkHand());
    }

    // Tests if a player starts with a non-winning hand they're able to detect it
    @Test
    public void testRandomHandLoses() {
        player.addCardToHand(4);
        player.addCardToHand(1);
        player.addCardToHand(7);
        player.addCardToHand(1);

        assertFalse("Hand full of 4 different cards should return false when checked if its won", player.checkHand());
    }

    // Tests the player never has more or less than 4 cards in their hand
    @Test
    public void testActionsAreAtomic() {
        drawDeck.addCard(5);

        player.addCardToHand(1);
        player.addCardToHand(2);
        player.addCardToHand(3);
        player.addCardToHand(4);

        player.move();

        assertEquals("The player's hand should remain 4 cards after a move", 4, player.getHand().size());
    }

    // Tests the player does NOT discard cards with their prefered denomination
    @Test
    public void testDiscardBehaviour() {
        player.addCardToHand(1);
        player.addCardToHand(2);
        player.addCardToHand(3);
        player.addCardToHand(4);

        // Runs multiple player moves and checks 1 is never discarded
        for (int i = 0; i < 5; i++) {
            drawDeck.addCard(i + 2);
            player.move();
            assertTrue("Player 1 will not discard the 1 card from their hand", player.getHand().contains(1));
        }
    }

    // Tests cards never leave the game loop and are conserved from player to deck to player etc.
    @Test
    public void testCardConservation() {
        drawDeck.addCard(8);
        depositDeck.addCard(8);

        player.addCardToHand(1);
        player.addCardToHand(2);
        player.addCardToHand(3);
        player.addCardToHand(4);

        int totalCards = drawDeck.getContents().size() + depositDeck.getContents().size() + player.getHand().size();

        player.move();

        int finalTotalCards = drawDeck.getContents().size() + depositDeck.getContents().size() + player.getHand().size();

        assertEquals("Number of cards does not change in the game loop", totalCards, finalTotalCards);
    }

    // Tests player instances successfully initialise a corresponding .txt file for output
    @Test
    public void testFileCreation() {
      Player fileTest = new Player(5, drawDeck, depositDeck);
      java.io.File file = new java.io.File("player5_output.txt");
      
      // Completes test and cleans up file after
      assertTrue("Player output file should be created when a new player is initialised", file.exists());
      file.delete();
    }

    // Tests wether multiple threads can access the same deck without error
    @Test
    public void testDeckAccess() throws InterruptedException {
        CardDeck sharedDeck = new CardDeck(2);

        // Defines concurrency stress test as threadable task
        Runnable task = () -> {
            for (int i = 0; i < 100; i++) {
                sharedDeck.addCard(i);
                sharedDeck.drawCard();
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        // Deck remains empty as every add card should be paired with a draw card
        assertTrue("Deck should stay empty if threads interact properly", sharedDeck.getContents().isEmpty());
    }
}
