# ECM 2414 Software Development Continuous Assessment

## Overview

This is our multi-threaded card game solution for the ECM 2414 Software Development Continuous Assessment. It uses thread-safe `CardDeck` and `Player` classes operating in a game loop controlled by the main `CardGame` class to fully simulate the logic of a round of Spoons. The number of players and location for pack of cards is taken from user input in the terminal.

## Dependencies

<b>JDK Version:</b> 25.0.2
<br><b>Testing Framework:</b> JUnit 4.13.2

## Folder Structure

src/
* `CardGame.java` - Executable class which handles the user input, card distribution and thread initialisation and deletion
* `CardDeck.java` - Thread-safe FIFO card deck container
* `Player.java` - Thread-safe player class which handles main gameplay methods and file handling for logging movements to `.txt` file.
  
<br>tests/
* `TestCardDeck.java` - JUnit test class to verify CardDeck class is to the specs standards
* `TestPlayer.java` - JUnit test class to verify Player class is to the specs standards

## Compilation Instructions

### To run the game

Download the repository, open to the root folder then run the executable .jar:

`java -jar CardGame.jar`

 Or type the following commands to build from source code:

1. `javac -d bin src/CardGame.java src/CardDeck.java src/Player.java`
2. `java -cp bin CardGame`

The game will now run, when prompted for players enter the num you wish. A card pack for 4 players is provided.
<br><i>Note: Please include 'src/' in your file path for the card pack when asked which to load</i>

### To run tests

To run the JUnit tests first ensure you're in the root directory, then type the following commands:

1. `javac -cp "lib/*" -d bin src/*.java tests/*.java`
2. `java -cp "bin;lib/*" org.junit.runner.JUnitCore TestCardDeck TestPlayer`

The terminal will then output the time taken and number of successful tests.
