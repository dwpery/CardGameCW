import java.io.FileNotFoundException;
import java.io.File;
import java.util.Scanner;

public class CardGame {
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
        for (int i = 0; i < totalPlayers; i++) {
            decks[i] = new CardDeck(i + 1);

            // 
            if ((i + 1) == totalPlayers) {
                players[i] = new Player(i + 1, decks[i], decks[0]);
            } else {
                players[i] = new Player(i + 1, decks[i], decks[i + 1]);
            }
        }

        // Reads cards from pack
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
            while (reader.hasNextLine()) {
                for (int n = 0; n < 4; n++) {
                    // Same num of playuers as decks so can use totalPlayers
                    for (int i = 0; i < totalPlayers; i++) {
                        decks[i].addCard(Integer.parseInt(reader.nextLine()));
                        System.out.println(i + ": " + decks[i].getContents());
                }
            }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            e.printStackTrace();
        }

    }
}
