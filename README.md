# Halfgammon
A backgammon implementation with a half-sized board and a modified rule set

##Summary
This was developed for my CS445: Artificial Intelligence final project

The backgammon engine itself is very simple. It takes user input, and decides whether or not the move is valid. If the move is valid, the move is made, if not, the move is rejected. It also manages both players’ pieces, and the current turn.

The AI opponent, however, is a bit more complicated. Given the current board setup, the AI manages a minimax tree, as well as performs alpha-beta pruning during tree creation in order to limit the size and increase efficiency of the tree. Given a tree, the AI opponent will choose the path that is, heuristically speaking, the best choice.

While traversing the minimax tree, the AI opponent is attempting to minimize its heuristic-score with its moves. The heuristic used was a combination of the AI’s current out-of-home-zone pieces, as well as the resulting board setup. As the very base, the number of spaces away from the home zone of both entered, and waiting to enter, is calculated. Any spaces on which the AI opponent has multiple checkers reduce the overall heuristic-score of that move. Likewise, a moderate amount is subtracted from heuristic if the move was a valid hit by the AI opponent. Once all of the pieces are in the home zone, 1 is subtracted for each piece that has been beared off. This heuristic is admissible because it will never overestimate the number of spaces the opponent has yet to move.

##Changes from Real Backgammon
This implementation doesn't exaclty follow the Backgammon rulebook exactly. The purpose of this project was to demonstrate the use of Minimax Trees with Alpha-Beta pruning, and so most changes were to make the building of the tree more manageable. Thus, the main changes are:
- Board is half-sized.
- Roll two 3-sided dice, instead of two 6-sided dice
- Cannot hit (take out) an opponent checker until you have a checker in your home space
- All checkers start off the board

Given the depth of the tree I used, the AI agent still processes hundreds-of-thousands of potential board scenarios every turn, and so there is still some 'thinking' time spent by AI.

#How to Use
##How to run Halfgammon.jar
In Windows, the jar file “JeffMoonBackgammon.jar” can be run using the command “java -jar JeffMoonBackgammon.jar”. There should be a similar command to use on Macs. If running outside of Eclipse, make sure your buffer size is set fairly large, as well as the console window. Otherwise, clipping on output may occur. Otherwise, the project included can be imported into Eclipse. By navigating to the “Console.java” class, and selecting Run As -> Java Application, the project can be executed through Eclipse’s console. 

###Console Commands
There are four main commands that can be used: 

1.  enter [DieValue] : performs a valid enter onto the board using a die.
2.  move [StartSpace] [DieValue] : performs a valid move from StartSpace using a die with DieValue.
3.  bear [StartSpace] [DieValue] : performs a valid bear from StartSpace using a die with DieValue.
4.  setboard [BoardScenario] : sets the board as one of the predefined scenarios. This is used for demonstration and debugging. Scenarios include:
  - newgame
  - midgame
  - killshot
  - bearing
  - victory

###Sample Use
Scenario: Entering a piece onto the board using a die with the value 2:
`enter 2`

Scenario: Moving a piece from spot 4 to spot 6 using a die with the value 2:
`move 4 2`

Scenario: Bearing off a piece that has made it all the way around using a die with the value of 3 (recall: all pieces must be in home zone to start bearing):
`bear 10 3`

Scenario: Starting a new game (or using any of the board scenarios):
`setboard newgame`

