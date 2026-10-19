import java.io.FileNotFoundException;
import java.io.File;
import java.util.Scanner;

public class CardGame {
    // Global as they need to be used by the player class as well as this class
    public static volatile boolean gameWon = false;
    public static volatile int winningPlayer = -1;
    public static void main(String[] args) throws Exception {
        // Takes user input from Terminal to generate num of players + set of cards
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Please enter the number of Players:");
        int totalPlayers = Integer.parseInt(scanner.nextLine());

        System.out.println("Please enter the location of pack to load:");
        String packFile = scanner.nextLine();

        // Closes scanner to prevent resource leak
        scanner.close();

        // Generates arrays for CardDeck and Player instances
        CardDeck[] decks = new CardDeck[totalPlayers];
        Player[] players = new Player[totalPlayers];
        
        // Populates decks array with deck class instances
        for (int i = 0; i < totalPlayers; i++) {
            decks[i] = new CardDeck(i + 1);
        }

        // Populates players array with player class instances 
        for (int i = 0; i < totalPlayers; i++) {
            // Needs separate loop otherwise NullPointerException
            if ((i + 1) == totalPlayers) {
                // The final players deposit deck loops back around to be the 1st deck
                players[i] = new Player(i + 1, decks[i], decks[0]);
            } else {
                players[i] = new Player(i + 1, decks[i], decks[i + 1]);
            }
        }

        // Distributes cards from pack in round robin form
        try (Scanner reader = new Scanner(new File(packFile))) {
            // Adds cards to players hands
            for (int n = 0; n < 4; n++) {
                for (int i = 0; i < totalPlayers; i++) {
                    players[i].addCardToHand(Integer.parseInt(reader.nextLine()));
                }
            }
            // Adds remaining cards to decks    
            for (int n = 0; n < 4; n++) {
                // Same num of players as decks so can use totalPlayers
                for (int i = 0; i < totalPlayers; i++) {
                    decks[i].addCard(Integer.parseInt(reader.nextLine()));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            e.printStackTrace();
        }

        // Initialises each player on their own Thread
        Thread[] playerThreads = new Thread[totalPlayers];
        for (int i = 0; i < totalPlayers; i++) {
            playerThreads[i] = new Thread(players[i]);
            playerThreads[i].start();
        }
        
        // Waits for all threads to finish before exiting main
        for (int i = 0; i < totalPlayers; i++) {
            try {
                playerThreads[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }
}
