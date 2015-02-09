/**
 * The backgammon game engine. Contains methods to check validity of moves on board, execute moves on board, as well as
 * general management of game board and rules. Also manages player-turn and dice.
 * 
 * Jeffrey Moon
 * Fall 2014
 */
package engine;

import scenariomanager.BoardScenario;
import scenariomanager.ScenarioSelector;

public class Engine {
	private static Engine e = new Engine();
	private Board board;
	private int turn = 0;
	private int[] dice;
	private int diceLeft;
	
	public Engine() { 

	}
	public static Engine getEngine(){
		if(e.board==null)
			e.board = new Board();
		return e;
	}
	// RULE MANAGEMENT
	public static boolean isValidMove(Board b, int turn, int startSpace, int die){
		int endSpace;
		// calculate end space based on turn
		if(turn==1)
			endSpace=startSpace+die;
		else
			endSpace=startSpace-die;
		
		// if turn has checkers waiting
		if(b.waiting(turn)>0)	
			return false;
		// if turn made a move that falls out of bounds
		else if(startSpace>11 || startSpace<0 || endSpace>11 || endSpace<0)
			return false;
		// if opponentOf(turn) has more than 1 checker at end space
		else if(b.getPieces(opponentOf(turn))[endSpace] > 1)
			return false;
		// if turn has no pieces at start space
		else if(b.getPieces(turn)[startSpace] < 1)
			return false;
		// if opponentOf(turn) only has 1 checker at end, but turn has no checkers home
		else if(b.getPieces(opponentOf(turn))[endSpace]==1 && !b.isHome(turn))
			return false;
		
		// otherwise move is valid
		return true;
	}
	public static boolean isValidEnter(Board b, int turn, int die){
		int endSpace;
		
		if(turn==1)
			endSpace = die-1;
		else
			endSpace = 12-die;
		// if turn has no checkers waiting
		if(b.waiting(turn) < 1)
			return false;
		// if opponent has checkers blocking enter
		else if(b.getPieces(opponentOf(turn))[endSpace] > 1)
			return false;
		// if opponent has 1 checker blocking enter, but turn has no checkers home
		else if(b.getPieces(opponentOf(turn))[endSpace] ==1 && !b.isHome(turn))
			return false;
		
		// else is valid enter
		return true;
	}
	
	public static boolean isValidBear(Board b, int turn, int startSpace, int die, int[] dice){
		
		// exact space count to bear a checker off 
		int spaces;
		if(turn==1)
			spaces=12-startSpace;
		else
			spaces = startSpace+1;
		
		// if turn owns no checks at start space
		if(b.getPieces(turn)[startSpace] < 1)
			return false;
		//if not all of turn's checkers are in home zone
		else if(!b.allHome(turn))
			return false;
		// if the die is less than spaces desired
		else if(die<spaces)
			return false;
		
		// if the die  are using is larger than the spaces requested, and can bring in a checker farther back
		else if(die>spaces && !b.isFarthestBack(turn, dice, startSpace))
			return false;
		else
			return true;
			
	}

	public boolean isValidPass(Board b, int turn, int[] dice){
		for(int i=0; i<dice.length; i++){
			if(dice[i]!=0){
				if(turn==1 && b.waiting(1)>0){
					if(isValidEnter(b, turn, dice[i]))
						return false;
				}else if(turn==2 && b.waiting(2)>0){
					if(isValidEnter(b, turn, dice[i]))
						return false;
				}
			
				for(int j=0; j<11; j++){
					if(turn==1 && isValidMove(b, turn, j, dice[i]))
						return false;
					else if(turn==2 && isValidMove(b, turn, j, dice[i]))
						return false;
				}
			
				for(int j=0; j<=2; j++){
					if(turn==1 && isValidBear(b, turn, 11-j, dice[i], dice))
						return false;
					else if(turn==2 && isValidBear(b, turn, j, dice[i], dice))
						return false;
				}
			}
		}
		return true;
	}
	public int victoryCheck(Board b){
		if(b.getBear(1)==8)
			return 1;
		else if(b.getBear(2)==8)
			return 2;
		else 
			return 0;
	}
		
	// TURN MANAGEMENT		
	public void setTurn(int player){
		e.turn=player;
		roll();
	}
	public void nextTurn(){
		if(e.turn==1)
			e.turn=2;
		else
			e.turn=1;
		
		roll();
	}
	public int getTurn(){
		return e.turn;
	}
	
	private static int opponentOf(int turn){
		if(turn==1)
			return 2;
		else
			return 1;
		
	}
	
	// DICE MANAGEMENT
	/**
	 * 
	 * @return Integer array containing remaining dice
	 */
	public int[] getDice(){
		return e.dice;
	}
	
	/**
	 * 
	 * @return True if there are remaining dice
	 */
	public boolean diceLeft(){
		if(e.diceLeft > 0)
			return true;
		return false;
	}
	
	/**
	 * 
	 * @return Integer containing dice left
	 */
	public int getDiceLeft(){
		return e.diceLeft;
	}
	/**
	 * Rolls two 3-sided die
	 */
	public void roll(){
		int dice[] = new int[2];
		boolean dubs = false;
		
		dice[0] = (int)(Math.random() * 3 + 1);
		dice[1] = (int)(Math.random() * 3 + 1);
		if(dice[1] < dice[0]){
			int temp = dice[0];
			dice[0] = dice[1];
			dice[1] = temp;
		}else if(dice[0] == dice[1]){
			int doubles[] = {dice[0], dice[0], dice[0], dice[0]};
			e.dice=doubles;
			e.diceLeft = doubles.length;
			dubs = true;
		}
		
		if(!dubs){
			e.dice = dice;
			e.diceLeft = dice.length;
		}
	}
	
	/**
	 * Start roll for deciding first move
	 * @return
	 */
	
	public int startRoll(){
		return (int)(Math.random() * 3 + 1);
	}
	
	/**
	 * Uses a die and sets record in dice to 0
	 * @param die The die to use
	 * @return True if there was a die to use
	 * 
	 */
	public boolean useDie(int die){
		for(int i=0; i<e.dice.length;i++){
			if(e.dice[i] == die){
				e.dice[i] = 0;
				return true;
			}
		}
		return false;
	}
	
	// BOARD MANAGEMENT
	
	public boolean move(String args[]){
			int startSpace, die;
		try{
			startSpace = Integer.parseInt(args[1]);
			die = Integer.parseInt(args[2]);
		} catch(NumberFormatException e){
			return false;
		}
		if(!isValidMove(e.board, e.turn, startSpace, die)){
			return false;
		}
		if(!useDie(die))
			return false;
		e.board.move(e.turn, startSpace, die);
		e.diceLeft--;
		e.board.checkHome();
		return true;
	}

	public boolean enter(String args[]){
		int die, endSpace;
		
		try{
			die = Integer.parseInt(args[1]);
		} catch(NumberFormatException e){
			return false;
		}
		if(e.turn==1)
			endSpace = die-1;
		else
			endSpace = 12-die;
		
		if(!isValidEnter(e.board, e.turn, die))
			return false;
		
		if(turn==1 && useDie(endSpace+1)){
			e.board.enter(e.turn, die);
			e.diceLeft--;
			e.board.checkHome();
			return true;
		}else if(useDie(12-endSpace)){
			e.board.enter(e.turn, die);
			e.diceLeft--;
			e.board.checkHome();
			return true;
		}
		return false;

	}
	public boolean bear(String args[]){
		int startSpace, dieUsed;
		try{
			startSpace = Integer.parseInt(args[1]);
			dieUsed = Integer.parseInt(args[2]);
			
		}catch(NumberFormatException e){
			return false;
		}
		if(!isValidBear(e.board, e.turn, startSpace, dieUsed, e.dice))
			return false;
		
		if(!useDie(dieUsed))
			return false;
		
		e.board.bear(e.turn, startSpace);
		e.diceLeft--;
		e.board.checkHome();
		return true;
	}
	public boolean pass(String args[]){
		if(isValidPass(e.board, e.turn, e.dice)){	
			nextTurn();
			return true;
		}else
			return false;
		
	}
	
	public Board getBoard(){
		return e.board;
	}
	
	public BoardScenario getBoardScenario(){
		BoardScenario bs = new BoardScenario();
		bs.setBoard(e.board);
		bs.setTurn(e.turn);
		return bs;
	}
	
	public void setBoardAs(BoardScenario bs, int[] dice){
		e.turn = bs.getTurn();
		e.board = bs.getBoard();
		e.dice = dice;
		e.board.checkHome();
	}
	
	public void setBoard(Board b){
		e.board = b;
		e.board.checkHome();
	}
	public void printBoard(String message){
		e.board.printBoard(message, e.turn, e.dice);
		
	}
	
	public boolean loadScenario(String boardScenario){
		if (boardScenario.equals("midgame")){
			BoardScenario bs = ScenarioSelector.midGameScenario();
			Board b = bs.getBoard();
			e.board = b;
			e.turn = bs.getTurn();
			e.board.checkHome();
			roll();
			return true;
		}else if(boardScenario.equals("killshot")){
			BoardScenario bs = ScenarioSelector.killShotScenario();
			Board b = bs.getBoard();
			e.board = b;
			e.turn = bs.getTurn();
			e.board.checkHome();
			roll();
			return true;
		}else if(boardScenario.equals("bearing")){
			BoardScenario bs = ScenarioSelector.bearScenario();
			Board b = bs.getBoard();
			e.board = b;
			e.turn = bs.getTurn();
			e.board.checkHome();
			roll();
			return true;
		}else if(boardScenario.equals("victory")){
			BoardScenario bs = ScenarioSelector.victoryScenario();
			Board b = bs.getBoard();
			e.board = b;
			e.turn = bs.getTurn();
			e.board.checkHome();
			roll();
			return true;
		}else if(boardScenario.equals("newgame")){
			BoardScenario bs = ScenarioSelector.newScenario();
			Board b = bs.getBoard();
			e.board = b;
			e.turn = bs.getTurn();
			e.board.checkHome();
			roll();
			return true;
		}else if(boardScenario.equals("passcheck")){
			BoardScenario bs = ScenarioSelector.passScenario();
			Board b = bs.getBoard();
			e.board = b;
			e.turn = bs.getTurn();
			e.board.checkHome();
			roll();
			return true;
		}
		
		return false;
	}
		
}
