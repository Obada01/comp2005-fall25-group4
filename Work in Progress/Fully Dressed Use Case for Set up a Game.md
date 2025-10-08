# Use Case Description: Set up a Game
## Primary Actor
- The player(s)
## Stakeholders and Interests
- Children: Children are mostly interested in playing the game, and want the setup process of the game to be quick and easy so they can start playing right away.
- Parents: Given that Hooop is a game primarily targeted at children, parents want the setup of Hooop to be easy to understand and accessible so their children can play it without issue.
- Teachers: Teachers want the setup for Hooop to be quick and informative, allowing students to learn how to play the game in a way that helps them build logic and reasoning skills.
## Preconditions
- None
## Main Success Scenario
1.  The system presents the user with the opportunity to start a new game, load an existing game, or to exit the use case.
2.	The user confirms that they wish to start a new game.
3.	The system provides the user with the opportunity to choose whether they wish to start a 2-player game or a 4-player game, or to go back to the new game, load game, and exit choice.
4.	The user chooses their player quantity preference.
5.	The system records the user’s player quantity preference.
6.	The system provides the user with the opportunity to play against a computer opponent or against another player opponent, or to go back to the 2-player/4-player choice.
7.	The user chooses to play against a computer opponent.
8.	The system records the user’s preference of opponent.
9.	The system provides the user with the opportunity to play on either easy or hard mode or to go back to the opponent preference choice.
10.	The user chooses an opponent difficulty preference.
11.	The system records the user’s difficulty preference.
12.	The system displays 8 different images to the user. Each image is of the same board, but with colour differences to appeal to colour vision deficiencies.
13.	The system provides the user the opportunity to select which board colour is the clearest, or to go back to the difficulty preference choice.
14.	The user selects their prefered board colour.
15.	The system records the user’s board colour preference.
16.	The system presents the user with the opportunity to read a brief tutorial.
17.	The user opts to read the brief tutorial.
18.	The system displays brief text instructions of the rules of the game, along with images for these text instructions.
19.	The system gives the user the opportunity to exit these instructions whenever they wish.
20.	The user chooses to exit the instructions once they understand the rules of the game.
21.	The system randomly chooses which frog colour will go first.
22.	The system displays the board layout for the user using the user’s colour preference. This includes the lilypads, the bridges between lilypads, the coloured frogs in their starting positions, the 4 special cards, and the text displaying the current turn (either the user or the computer opponent).
## Postconditions
- The system enters the "take a turn" use case.
## Alternative Flows
2a: The user wishes to load an existing game:
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
  - 1c: The user uploads a valid file:  
      - 1: The system stores the contents of the file.  
      - 2: The system displays the user's previous board layout, including the lilypads, the bridges between lilypads, the frogs in their starting positions, the 4 special cards, and the text displaying the current turn (either the user or the computer opponent).  

4a: The user chooses to return to the new game, load game, or exit choice:  
- 1: The system returns to step 1 of the main success scenario.  

7a: The user chooses to play against (a) player opponent(s):  
- 1: The system records the user's preference to play against a real person or people.  
- 2: The system goes to step 12 of the main success scenario.  

7b: The user chooses to go back to the 2-player/4-player choice:  
- 1: The system returns to step 3 of the main success scenario.  

10a: The user chooses to return to the human or computer opponent(s) choice:  
- 1: The system returns to step 6 of the main success scenario.  

13a: The user chooses to go back to the opponent difficulty preference choice:  
- 1: The system returns to step 9 of the main success scenario.  

17a: The user opts not to read the brief tutorial:  
- 1: The system goes to step 21 of the main success scenario.
## Exceptions
2b: The user chooses to exit the use case:  
- 1: The system exits the use case.
## Special Requirements
- Hooop is a game primarily targeted at children, the setup process should be easy to understand, but not in a way that is condescending or that downplays the intelligence of the children.
- Hooop should be accessible to anyone, regardless of any colour vision deficiencies they may have, and the setup process should reflect this.
- Fonts should be large enough and text should be well-spaced enough to make it easy to read and understand for any audience.
## Open Issues
- How do we handle saving and loading data for alternative flow 2a?
- How can the game board be coloured to help with colour vision defficiencies?
- How can we make the computer opponent more or less difficult depending on the user's choice of difficulty.
