import java.io.FileNotFoundException;
import java.io.File;
import java.util.Scanner;

public class CardGame {
    //
    public static volatile boolean gameWon = false;
    public static volatile int winningPlayer = -1;
    public static void main(String[] args) throws Exception {
        // Takes user input from Terminal to generate num of players + set of cards
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Please enter the number of Players:");
        int totalPlayers = Integer.parseInt(scanner.nextLine());

        System.out.println("Please enter the location of pack to load:");
        String packFile = scanner.nextLine();

        // Generates CardDeck and Player instances
        CardDeck[] decks = new CardDeck[totalPlayers];
        Player[] players = new Player[totalPlayers];
        
        // Generates a deck for each player
        for (int i = 0; i < totalPlayers; i++) {
            decks[i] = new CardDeck(i + 1);
        }

        // Generates num of player instances input in terminal
        for (int i = 0; i < totalPlayers; i++) {
            // Needs seperate loop otherwise NullPointerException
            if ((i + 1) == totalPlayers) {
                players[i] = new Player(i + 1, decks[i], decks[0]);
            } else {
                players[i] = new Player(i + 1, decks[i], decks[i + 1]);
            }
        }

        // Distributes cards from pack in round robin form
        try (Scanner reader = new Scanner(new File(packFile))) {
            // Adds caards to players hands
            for (int n = 0; n < 4; n++) {
                for (int i = 0; i < totalPlayers; i++) {
                    players[i].addCardToHand(Integer.parseInt(reader.nextLine()));
                    System.out.println(i + ": " + players[i].getHand());
                }
            }
            System.out.println("Decks");
            // Adds remaining cards to decks    
            for (int n = 0; n < 4; n++) {
                // Same num of playuers as decks so can use totalPlayers
                for (int i = 0; i < totalPlayers; i++) {
                    decks[i].addCard(Integer.parseInt(reader.nextLine()));
                   System.out.println(i + ": " + decks[i].getContents());
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

        for (int i = 0; i < totalPlayers; i++) {
            try {
                playerThreads[i].join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }
}
