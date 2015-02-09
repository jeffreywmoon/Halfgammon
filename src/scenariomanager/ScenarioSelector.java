/**
 * Contains all of the preset scenarios that can be selected using setboard command
 * 
 * Jeffrey Moon
 * Fall 2014
 */
package scenariomanager;

import engine.Board;

public class ScenarioSelector {
	public static BoardScenario midGameScenario(){
		BoardScenario bs = new BoardScenario();
		Board b = new Board();
		
		int[] p1 = {0,0,3,2,1,2,0,0,0,0,0,0};
		int[] p2 = {0,0,0,0,0,0,2,1,2,3,0,0};
		
		b.setPieces(p1, p2, 0, 0);
		bs.setBoard(b);
		bs.setTurn(1);
		
		return bs;
		
	}
	
	public static BoardScenario killShotScenario(){
		BoardScenario bs = new BoardScenario();
		Board b = new Board();
		
		int[] p1 = {0,0,0,6,0,0,0,0,0,1,0,1};
		int[] p2 = {1,0,0,0,2,1,1,1,0,0,2,0};
		
		b.setPieces(p1, p2, 0, 0);
		bs.setBoard(b);
		bs.setTurn(1);
		
		return bs;
	}
	
	public static BoardScenario bearScenario(){
		BoardScenario bs = new BoardScenario();
		Board b = new Board();
		
		int[] p1 = {0,0,0,0,0,0,0,0,0,3,2,3};
		int[] p2 = {4,4,0,0,0,0,0,0,0,0,0,0};
		
		b.setPieces(p1, p2, 0, 0);
		bs.setBoard(b);
		bs.setTurn(1);
		
		return bs;
	}
	
	public static BoardScenario victoryScenario(){
		BoardScenario bs = new BoardScenario();
		Board b = new Board();
		
		int[] p1 = {0,0,0,0,0,0,0,0,0,0,1,1};
		int[] p2 = {4,4,0,0,0,0,0,0,0,0,0,0};
		
		b.setPieces(p1, p2, 0, 0);
		b.setBear(1, 6);
		bs.setBoard(b);
		bs.setTurn(1);
		
		return bs;
	}
	
	public static BoardScenario newScenario(){
		BoardScenario bs = new BoardScenario();
		Board b = new Board();
		
		int[] p1 = {0,0,0,0,0,0,0,0,0,0,0,0};
		int[] p2 = {0,0,0,0,0,0,0,0,0,0,0,0};
		
		b.setPieces(p1, p2, 8, 8);
		bs.setBoard(b);
		bs.setTurn((int)(Math.random()*2+1));
		
		return bs;
	}
	
	public static BoardScenario passScenario(){
		BoardScenario bs = new BoardScenario();
		Board b = new Board();
		
		int[] p1 = {0,0,0,1,1,6,0,0,0,0,0,0};
		int[] p2 = {0,0,0,0,0,0,6,1,1,0,0,0};
		
		b.setPieces(p1, p2, 0, 0);
		bs.setBoard(b);
		bs.setTurn((int)(Math.random()*2+1));
		
		return bs;
	}
	
}
