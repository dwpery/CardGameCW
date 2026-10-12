import java.util.Scanner;

public class CardGame {
    public static void main(String[] args) throws Exception {
        // Takes user input from Terminal to generate num of players + set of cards
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Please enter the number of Players:");
        int totalPlayers = Integer.parseInt(scanner.nextLine());

        System.out.println("Please enter the location of pack to load:");
        String packFile = scanner.nextLine();
    }
}
