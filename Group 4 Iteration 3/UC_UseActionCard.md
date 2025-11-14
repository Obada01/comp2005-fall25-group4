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
4.	The user chooses to use the "perform an extra jump" action card.
5.	The system enters the "use extra jump card" use case.
6.	The system successfully completes and exits the "use extra jump card" use case.
7.	The system gives the user the opportunity to move one of their frogs, place a bridge, or exit the use case.
8.	The user chooses to move their frog.
9.	The system enters the "move a frog" use case.
10.	The system exits the use case.
## Postconditions
- None
## Alternative Flows
4a: The user chooses to exit the use case:
- 1: The system exits the use case.

4b: The user chooses to use the "parachute" action card:  
- 1: The system enters the "use parachute card" use case.
- 2: The system successfully completes and exits the "use parachute card" use case.
- 3: The system returns to step 7 of the main success scenario.

4c: The user chooses to use the "extra bridge" action card:  
- 1: The system enters the "use extra bridge card" use case.
- 2: The system successfully completes and exits the "use extra bridge card" use case.
- 3: The system returns to step 7 of the main success scenario.

4d: The user chooses to use the "bridge removal" action card:  
- 1: The system enters the "use bridge removal card" use case.
- 2: The system successfully completes and exits the "use bridge removal card" use case.
- 3: The system returns to step 7 of the main success scenario.

8a: The user chooses to place a bridge:  
- 1: The system enters the "place a bridge" use case.
- 2: The system exits the use case.
 
8b: The user chooses to exit the use case:  
- 1: The system enters the "take a turn" use case.
- 2: The system exits the use case.
## Exceptions
1a: The system is unable to retrieve a list of action cards, as the user has no usable actions cards left:
- 1: The system exits the use case.

4a: The user chooses to use the "parachute" action card, but the system is unable to complete the "use parachute card" use case:
- 1: The system informs the user of the failure to complete the use case, giving an explicit reason why it was unable to execute.
- 2: The system exits the use case.

4b: The user chooses to use the "extra bridge" action card, but the system is unable to complete the "use extra bridge card" use case:
- 1: The system informs the user of the failure to complete the use case, giving an explicit reason why it was unable to execute.
- 2: The system exits the use case.

4c: The user chooses to use the "bridge removal" action card, but the system is unable to complete the "bridge removal" use case:
- 1: The system informs the user of the failure to complete the use case, giving an explicit reason why it was unable to execute.
- 2: The system exits the use case.

6a: The system fails to complete the "use extra jump card" use case:
- 1: The system informs the user of the failure to complete the use case, giving an explicit reason why it was unable to execute.
- 2: The system exits the use case.
## Special Requirements
- Due to the young audience of the game, the cards for Hooop! should be intuitive and have an easy to understand description attached to them.
- Hooop should be accessible to anyone, text descriptions for cards should reflect this by having a big font size, and an easy to read font.
- Each card should be balanced appropriately. If a card is too powerful, it could entirely disrupt the balance of the game.
## Open Issues
- How can we balance the cards to make sure they are powerful, but not too powerful?
- How can we check that the user's card is usable in their current situation?
- How can we make sure computer opponents use cards? How do we program their logic?
- What font size and type is optimal for user comprehension, regardless of external factors?
