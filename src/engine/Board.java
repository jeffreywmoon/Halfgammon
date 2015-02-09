/**
 * The board. Contains all methods to effectively manage the half-backgammon board.
 * 
 * Jeffrey Moon
 * Fall 2014
 */
package engine;

public class Board {
	private int p1pieces[] = new int[12];
	private int p2pieces[] = new int[12];
	
	private int p1wait;
	private int p2wait;
	
	private int p1bear;
	private int p2bear;
	
	private boolean p1home;
	private boolean p2home;
	
	public Board(Board b){
		for(int i=0;i<=11;i++){
			this.p1pieces[i] = b.getPieces(1)[i];
			this.p2pieces[i] = b.getPieces(2)[i];
		}
		this.p1wait = b.waiting(1);
		this.p2wait = b.waiting(2);
		this.p1bear = b.getBear(1);
		this.p2bear = b.getBear(2);
	}
	public Board(){
		for(int i=0; i<12; i++){
			p1pieces[i] = 0;
			p2pieces[i] = 0;
		}
		
		p1wait = 8;
		p2wait = 8;
		p1bear = 0;
		p2bear = 0;
		p1home = false;
		p2home = false;
	}
	
	/**
	 * Executes a valid move on the current board setup.
	 * @param player
	 * @param startSpace
	 * @param endSpace
	 */
	public void move(int player, int startSpace, int die){
		int endSpace;
		if(player==1){
			endSpace = startSpace+die;
			p1pieces[startSpace]--;
			p1pieces[endSpace]++;
			if(p2pieces[endSpace]==1){
				p2pieces[endSpace]--;
				p2wait++;
			}
		}else{
			endSpace = startSpace-die;
			p2pieces[startSpace]--;
			p2pieces[endSpace]++;
			if(p1pieces[endSpace]==1){
				p1pieces[endSpace]--;
				p1wait++;
			}
		}
	}
	
	public void enter(int player, int die){
		if(player==1){
			p1wait--;
			p1pieces[die-1]++;
			if(p2pieces[die-1]==1){
				p2pieces[die-1]--;
				p2wait++;
			}
		}else{
			p2wait--;
			p2pieces[12-die]++;
			if(p1pieces[12-die]==1){
				p1pieces[12-die]--;
				p1wait++;
			}
		}
	}
	
	public void bear(int player, int startSpace){
		if(player==1){
			p1bear++;
			p1pieces[startSpace]--;
		}else{
			p2bear++;
			p2pieces[startSpace]--;
		}
	}
	/**
	 * 
	 * @param player
	 * @return Integer array of current board setup for given player.
	 */
	public int getBear(int player){
		if(player==1)
			return p1bear;
		else
			return p2bear;
	}
	public void setBear(int player, int bear){
		if(player==1)
			p1bear = bear;
		else
			p2bear = bear;
	}
	
	public int[] getPieces(int player){
		if (player==1)
			return p1pieces;
		else 
			return p2pieces;
	}
	
	public void setPieces(int[] p1, int[] p2, int p1wait, int p2wait){
		this.p1pieces = p1;
		this.p2pieces = p2;

		System.out.println();
		this.p1wait = p1wait;
		this.p2wait = p2wait;
	}
	
	public void setBoard(Board b){
		this.p1pieces = b.getPieces(1);
		this.p2pieces = b.getPieces(2);
		this.p1wait = b.waiting(1);
		this.p2wait = b.waiting(2);
		this.p1bear = b.getBear(1);
		this.p2bear = b.getBear(2);
	}
	public Board getBoard(){
		Board b = new Board();
		b.setPieces(p1pieces, p2pieces, p1wait, p2wait);
		return new Board(b);
	}
	/**
	 * Checks to see if either player controls a checker in home region
	 */
	public void checkHome(){
		for(int i=0; i<=2; i++){
			if(p2pieces[i] > 0){ 
				p2home=true;
				break;
			}
			if(i==2) p2home=false;
		}
		for(int i=9; i<=11; i++){
			if(p1pieces[i] > 0){ 
				p1home=true;
				break;
			}
			if(i==11) p2home=false;
		}
	}
	
	public int countBoard(int player){
		int total=0;
		for(int i=0; i<=11;i++){
			total+=this.getPieces(player)[i];
		}
		return total;
	}
	public boolean allHome(int player){
	
		if(player==1){
			for(int i=0; i<=8; i++){
				if(p1pieces[i] > 0)
					return false;
			}
			if(p1wait==0)
				return true;
			
		}else if(player==2){

			for(int i=3; i<=11; i++){
				if(p2pieces[i] > 0){
					return false;
				}
			}
			if(p2wait==0)
				return true;
		}
		return false;
	}
	
	public boolean isFarthestBack(int player, int[] dice, int startSpace){
		if(player==1){
			for(int i=9; i<startSpace; i++)
				//if there is a checker behind current
				if(p1pieces[i] > 0){
					for(int j=0; j<dice.length; j++){
						//and we have a die that can get that checker in
						if(dice[j] >= (12-startSpace))
							return false;
					}
				}
			return true;
		}else{
			for(int i=2; i>startSpace; i--)
				//if there is a checker behind current
				if(p2pieces[i] > 0){
					for(int j=0; j<dice.length; j++){
						//and we have a die that can get that checker in
						if(dice[j] >= (startSpace+1))
							return false;
					}
				}
			return true;
		}
	}
	/**
	 * 
	 * @param player 
	 * @return True if player controls a checker in home region
	 */
	public boolean isHome(int player){
		if (player==1)
			return p1home;
		else
			return p2home;
	}
	
	/**
	 * 
	 * @param player
	 * @return Integer value of checkers player is currently has waiting for entry.
	 */
	public int waiting(int player){
		if (player==1)
			return p1wait;
		else
			return p2wait;
	}
	
	/**
	 * 
	 * @param player
	 * @return Integer value of checkers player has beared off.
	 */
	public int bear(int player){
		if (player==1)
			return p1bear;
		else
			return p2bear;
	}
	
	/**
	 * Prints the current board setup to the console.
	 * @param message System message to pass information to players.
	 */
	
	// METABOARD MANAGEMENT
	
	
	// PRINT BOARD

	public void printBoard(String message, int turn, int[] dice){

		for(int i=0; i<6; i++){
			if(p1pieces[i]>0){
				System.out.print("P1:"+p1pieces[i]);
			}else if(p2pieces[i]>0){
				System.out.print("P2:"+p2pieces[i]);
			}else{
				System.out.print("    ");
			}
				System.out.print("\t\t");
		}
		
		System.out.println("\n  \t\t  \t\t  \t|\t  \t\t  \t\t  ");
		System.out.println("__\t\t__\t\t__\t|\t__\t\t__\t\t__");
		
		for(int i=0; i<6; i++)
				System.out.print(i+" \t\t");
		
		System.out.println("\n\n\n\n\n");
		
		for(int i=11; i>=6; i--){
			
			if(p1pieces[i]>0){
				System.out.print("P1:"+p1pieces[i]);
			}else if(p2pieces[i]>0){
				System.out.print("P2:"+p2pieces[i]);
			}else{
				System.out.print("    ");
			}
			
			if(i==9)
				System.out.print("\t|\t");
			else
				System.out.print("\t\t");
		}
		
		System.out.println();
		System.out.println("__\t\t__\t\t__\t|\t__\t\t__\t\t__");
		for(int i=11; i>=6; i--)
			System.out.print(i+" \t\t");
		
		String[] homeZone = {" ", " "};

		for(int i=0; i<2; i++){
			if(isHome(i+1))
				homeZone[i]="*";
		}
		System.out.println("\n\nPlayer "+turn+"'s Turn\n");
	
		System.out.printf("%sPlayer 1 ready/beared: %d/%d\n%sPlayer 2 ready/beared: %d/%d\n", 
				homeZone[0],p1wait, p1bear, homeZone[1], p2wait, p2bear);
		System.out.print("Dice:");
		for(int i=0; i<dice.length; i++){
			if(dice[i] != 0)
				System.out.print(" " + dice[i]);
		}
		System.out.println();
		System.out.println(message);
	}

/**	
	public void printBoard(String message, int turn, int[] dice){

		for(int i=0; i<11; i++)
			System.out.printf("%d/%d\t", p1pieces[i], p2pieces[i]);
		System.out.println();
		System.out.printf("P1:%d/%d\tP2:%d/%d\n", p1wait, p1bear, p2wait, p2bear);
		for(int i=0; i<dice.length; i++)
			System.out.printf("dice:%d  ", dice[i]);
		System.out.println(turn);
			
		
	}
*/	
}
