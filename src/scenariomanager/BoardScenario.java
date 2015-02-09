/** 
 * A board scenario to be used with the setboard command
 * 
 * Jeffrey Moon
 * Fall 2014
 */
package scenariomanager;

import engine.Board;

public class BoardScenario {
	private int turn;
	private Board b;
	
	public Board getBoard(){
		return this.b;
	}
	public void setBoard(Board b){
		this.b = b;
	}
	
	public void setTurn(int turn){
		this.turn = turn;
	}
	public int getTurn(){
		return this.turn;
	}
	
	

}
