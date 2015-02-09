/**
 * BoardNode on minimax tree. Contains all information about a given board state, including heuristic and turn
 * 
 * Jeffrey Moon
 * Fall 2014
 */
package minimax;

import java.util.LinkedList;

import engine.Board;

public class BoardNode {
	private int h;
	private Board b;
	private int turn;
	private boolean isLeaf;
	private LinkedList<RollNode> rolls;
	
	public BoardNode(Board b, int turn){
		
		this.b = b;
		this.turn = turn;
		rolls = new LinkedList<RollNode>();
		isLeaf=true;
	}
	
	public int getTurn(){
		return turn;
	}
	public Board getBoard(){
		return b;
	}
	public boolean isLeaf(){
		return isLeaf;
	}
	
	public void visited(){
		this.isLeaf = false;
	}
	public void addRollNode(RollNode rn){
		rolls.add(rn);
	}
	
	public int getH(){
		return h;
	}
	
	public void setH(int h){
		this.h = h;
	}
	public int getRollsSize(){
		return rolls.size();
	}
	
	public RollNode getRollNode(int i){
		return rolls.get(i);
	}
	
}
	
