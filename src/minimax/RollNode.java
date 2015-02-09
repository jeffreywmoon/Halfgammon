/**
 * A roll node of a minimax tree. Manages a linkedlist of all of the possibilities of boards using this roll (after
 * alpha-beta pruning)
 * 
 * Jeffrey Moon
 * Fall 2014
 */
package minimax;

import java.util.LinkedList;

public class RollNode {
	private int[] dice;
	private int[] hMax;
	private int[] hMin;
	private int h;
	private int path;
	private LinkedList<BoardNode> boards;
	
	public RollNode(int[] dice){
		this.dice = dice;
		boards = new LinkedList<BoardNode>();
		hMax = new int[]{0, -1};
		hMin = new int[]{100,-1};
	}
	
	public int[] getDice(){
		return this.dice;
	}
	
	public int addBoardNode(BoardNode bn){
		boards.add(bn);
		return boards.size();
	}
	
	public void setBoardNodes(LinkedList<BoardNode> boards){
		this.boards = boards;
	}
	
	public int getNumBoards(){
		return this.boards.size();
	}
	
	public BoardNode getBoard(int boardNum){
		return boards.get(boardNum);
	}
	
	public int getMaxH(){
		return hMax[0];
	}
	
	public int getMinH(){
		return hMin[0];
	}
	
	public void setH(int h){
		this.h = h;
	}
	
	public int getH(){
		return this.h;
	}
	
	public void setPath(int p){
		this.path = p;
	}
	public int getPath(){
		return this.path;
	}
	public void setMaxH(int h, int path){
		this.hMax[0] = h;
		this.hMax[1] = path;
	}
	public void setMinH(int h, int path){
		this.hMin[0] = h;
		this.hMin[1] = path;
	}
}
