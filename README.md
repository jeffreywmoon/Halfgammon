# Halfgammon
A backgammon implementation with a half-sized board and a modified rule set

#Summary
This was developed for my CS445: Artificial Intelligence final project

The backgammon engine itself is very simple. It takes user input, and decides whether or not the move is valid. If the move is valid, the move is made, if not, the move is rejected. It also manages both players’ pieces, and the current turn.

The AI opponent, however, is a bit more complicated. Given the current board setup, the AI manages a minimax tree, as well as performs alpha-beta pruning during tree creation in order to limit the size and increase efficiency of the tree. Given a tree, the AI opponent will choose the path that is, heuristically speaking, the best choice.

While traversing the minimax tree, the AI opponent is attempting to minimize its heuristic-score with its moves. The heuristic used was a combination of the AI’s current out-of-home-zone pieces, as well as the resulting board setup. As the very base, the number of spaces away from the home zone of both entered, and waiting to enter, is calculated. Any spaces on which the AI opponent has multiple checkers reduce the overall heuristic-score of that move. Likewise, a moderate amount is subtracted from heuristic if the move was a valid hit by the AI opponent. Once all of the pieces are in the home zone, 1 is subtracted for each piece that has been beared off. This heuristic is admissible because it will never overestimate the number of spaces the opponent has yet to move.

#How to Use
In Windows, the jar file “JeffMoonBackgammon.jar” can be run using the command “java -jar JeffMoonBackgammon.jar”. There should be a similar command to use on Macs. If running outside of Eclipse, make sure your buffer size is set fairly large, as well as the console window. Otherwise, clipping on output may occur. Otherwise, the project included can be imported into Eclipse. By navigating to the “Console.java” class, and selecting Run As -> Java Application, the project can be executed through Eclipse’s console. There are four main commands that can be used: 

1.  enter [DieValue] : performs a valid enter onto the board using a die.
2.  move [StartSpace] [DieValue] : performs a valid move from StartSpace using Die.
3.  bear [StartSpace] [DieValue] : performs a valid bear from StartSpace using Die.
4.  setboard [BoardScenario] : sets the board as one of the predefined scenarios. This is used for demonstration and debugging. Scenarios include:
  - newgame
  - midgame
  - killshot
  - bearing
  - victory
