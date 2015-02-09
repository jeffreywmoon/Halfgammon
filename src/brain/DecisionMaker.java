package brain;

import minimax.MinimaxTree;
import engine.Board;
import engine.Engine;

public class DecisionMaker {
	Engine engineInstance = Engine.getEngine();
	
	public Board chooseMove(Board b, int[] dice){
	
		MinimaxTree tree = new MinimaxTree(b ,2, 4);
		tree.minimax();
		return tree.getBestPathBoard(dice).getBoard();
	}
}
