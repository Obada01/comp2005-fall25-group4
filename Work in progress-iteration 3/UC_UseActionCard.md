# Use Case Description: Use an Action Card
## Primary Actor
- The player(s)
## Stakeholders and Interests
- Children: Children are interested in winning the game, and want action cards to be easy to use and simple in their function.
- Parents: Parents want Hooop! action cards to be easy to learn so their children can focus on playing the game above all else.
- Teachers: Teachers want the action cards to challenge students who play Hooop! to use logical reasoning and critical thinking skills for learning purposes.
## Preconditions
- It must be the user's turn.
- The user must be currently engaged in a game of Hooop!
## Main Success Scenario
1.  The system retrieves a list of action cards that the user can use on their turn.
2.  The system displays a list of usable action cards for the user.
3.  The system provides the user with the opportunity to select one of their remaining action cards to be used, or to exit the use case.
4.	The user confirms that they wish to use an action card. (Go to Alternative flows 4b-4e for specific action card types and associated flows)
5.	The system successfully follows the appropriate alternative flow for the action card of the user's choice.
6.	The system displays the changes to the board for the user depending on their action card type and its associated alternative flow.
7.	The system gives the user the opportunity to move one of their frogs, place a bridge, or exit the use case.
8.	The user chooses to move their frog.
9.	The system enters the move a frog use case.
10.	The system exits the use an action card use case.
## Postconditions
- None
## Alternative Flows
4a: The user chooses to exit the use case:
- 1: The system exits the use case.

4b: The user chooses to use the Extra Jump action card:  
- 1: The system gives the user the opportunity to select one of their frogs that is not currently on an opponent's home lilipad, or to exit the use case.
    - 2a: The user selects one of their frogs.  

4c: The user chooses to use the Parachute action card:  
- 1:

4d: The user chooses to use the Extra Bridge action card:  
- 1:

4e: The user chooses to use the Bridge Removal action card:  
- 1:

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
1a: The system is unable to retrieve a list of action cards, as the user has no usable actions cards left:
- 1: The system exits the use case.
## Special Requirements
- Hooop is a game primarily targeted at children, the setup process should be easy to understand, but not in a way that is condescending or that downplays the intelligence of the children.
- Hooop should be accessible to anyone, regardless of any colour vision deficiencies they may have, and the setup process should reflect this.
- Fonts should be large enough and text should be well-spaced enough to make it easy to read and understand for any audience.
## Open Issues
- How do we handle saving and loading data for alternative flow 2a?
- How can the game board be coloured to help with colour vision defficiencies?
- How can we make the computer opponent more or less difficult depending on the user's choice of difficulty.
