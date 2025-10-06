# Use Case Description: Set up a Game
## Preconditions
- None
## Main Success Scenario
1.  The system presents the user with the opportunity to start a new game, load an existing game, or to exit the use case.
2.	The user confirms that they wish to start a new game.
3.	The system provides the user with the opportunity to choose whether they wish to start a 2-player game or a 4-player game, or to exit the use case.
4.	The user chooses to play a 2-player game.
5.	The system records the user’s choice to play a 2-player game.
6.	The system provides the user with the opportunity to play against a computer opponent or against another player opponent.
7.	The user chooses to play against a computer opponent.
8.	The system records the user’s preference of opponent.
9.	The system provides the user with the opportunity to play on either easy or hard mode.
10.	The user chooses a difficulty to play on.
11.	The system records the user’s difficulty preference.
12.	The system displays 8 different images to the user. Each image is of the same board, but with colour differences to appeal to colour vision deficiencies.
13.	The system provides the user the opportunity to select which board colour is the clearest.
14.	The user selects the clearest board colour.
15.	The system records the user’s board colour preference.
16.	The system presents the user with the opportunity to read a brief tutorial.
17.	The user opts to read the brief tutorial.
18.	The system displays brief text instructions of the rules of the game, along with images for these text instructions.
19.	The system gives the user the opportunity to exit these instructions whenever they wish.
20.	The user chooses to exit the instructions once they understand the rules of the game.
21.	The system randomly chooses whether the user or their computer opponent will go first.
22.	The system displays the board layout for the user using the user’s colour preference. This includes the lilypads, the bridges between lilypads, the frogs in their starting positions, the 4 special cards, and the text displaying the current turn (either the user or the computer opponent).

## Postconditions
- The system enters the "take a turn" use case.
- The system exits the use case.

## Alternative Flows
1a: The user wishes to load an existing game:
- 1: The system provides the user the opportunity to upload a file with data from a previous session, or to exit the flow.  
  - 1a: The user exits the flow:
      - 1: The system exits the flow and returns to step 1 of the main success scenario.
  - 1b: The user attempts to upload an invalid file:  
      - 1: The system signals error.  
      - 2: The system presents the user with the opportunity to select another file or to exit the flow.  
        - 2a: The user exits the flow:  
          - 1: The system exits the flow and returns to step 1 of the main success scenario.  
        - 2b: The user chooses to select another file:  
          - 1: The systems returns to the beginning of flow 1a.  
  - 1c: The user uploads a valid file  
      - 1: The system stores the contents of the file.  
      - 2: The system displays the user's previous board layout, including the lilypads, the bridges between lilypads, the frogs in their starting positions, the 4 special cards, and the text displaying the current turn (either the user or the computer opponent).  
