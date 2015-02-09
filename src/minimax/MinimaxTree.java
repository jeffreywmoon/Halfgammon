/** The main (and slighly poorly-commented) minimax tree class. Contains all methods to
 * 	build, traverse, and maintain the minimax tree. Reading through the addpossibilities methods are
 * 	like a "Choose Your Own Adventure" novel.
 * 
 *  Jeffrey Moon
 *  Fall 2014
 */

package minimax;

import engine.Board;
import engine.Engine;

public class MinimaxTree {
	private BoardNode root;
	private int maxDepth;
	private final int myTurn=2;
	public MinimaxTree(Board b, int turn, int maxDepth){
		root = new BoardNode(b, turn);
		this.maxDepth = maxDepth;
		buildTree(root, 0);

	}

	public int buildTree(BoardNode parent, int depth){
		//System.out.println(boardToString(parent.getBoard())+"\n");
		if(depth<maxDepth){	
			parent.visited();
			// add roll nodes to root node
			for(int die1 = 1; die1<=3; die1++){
				for(int die2 = die1+1; die2<=3; die2++){
					RollNode rn = new RollNode(new int[]{die1, die2});
					addPossibilities(rn, parent.getBoard(), parent.getTurn());
					parent.addRollNode(rn);
					for(int i=0;i<rn.getNumBoards(); i++){
						//System.out.printf("TURN: %d    DEPTH: %d   DICE: %d %d   H: %d\n%s\n\n%s\n\n",
						//	parent.getTurn(), depth, die1, die2, rn.getBoard(i).getH(), boardToString(parent.getBoard()), boardToString(rn.getBoard(i).getBoard()));
						
						buildTree(rn.getBoard(i), depth+1);
						
					}
					
				}
				RollNode rn = new RollNode(new int[]{die1, die1, die1, die1});
				
				addPossibilitiesDubs(rn, parent.getBoard(), parent.getTurn(), die1);
				parent.addRollNode(rn);
				for(int i=0; i<rn.getNumBoards(); i++){
					//System.out.printf("TURN: %d    DEPTH: %d   DICE: %d %d %d %d   H: %d\n%s\n\n%s\n\n",
					//	parent.getTurn(), depth, die1, die1, die1, die1, rn.getBoard(i).getH(), boardToString(parent.getBoard()), boardToString(rn.getBoard(i).getBoard()));

					buildTree(rn.getBoard(i), depth+1);
				}
				
			}
			
		
		}
		return 0;
	}
	
	public int getBestPath(int[] dice){
		for(int i=0; i<root.getRollsSize();i++){
			if(compArray(dice, root.getRollNode(i).getDice())){
				int minPath = -1;
				int min = 100;
				RollNode rn = root.getRollNode(i);
				for(int j=0;j<rn.getNumBoards(); j++){
					if(rn.getBoard(j).getH() < min){
						min = rn.getBoard(j).getH();
						minPath = j;
					}
				}
	
				return minPath;
			}
		}
		return -1;
	}
	
	public BoardNode getBestPathBoard(int[] dice){
		int minPath = -2;
		for(int i=0; i<root.getRollsSize();i++){
			if(compArray(dice, root.getRollNode(i).getDice())){
				minPath = -1;
				int min = 100;
				RollNode rn = root.getRollNode(i);
				for(int j=0;j<rn.getNumBoards(); j++){
					if(rn.getBoard(j).getH() < min){
						min = rn.getBoard(j).getH();
						minPath = j;
					}
				}
				if(minPath>-1)
					return rn.getBoard(minPath);
				else
					return root;
			}
		}
		if(minPath<0) {
			System.out.println("UHOH " + printArray(dice));
		}
		return null;
	}
	public String printArray(int[] a){
		String j="";
		for(int i=0; i<a.length; i++){
			j+=a[i] + " ";
		}
		return j;
	}
	
	
	public int minimax(){
		return minimax(root);
		
	// performs a minimax search on the tree
	}
	public int minimax(BoardNode bn){
		if(bn.isLeaf()){
			return bn.getH();
		}else{
			int h;
			if(bn.getTurn()==1)
				h=0;
			else
				h=100;
			
			for(int i=0; i<bn.getRollsSize(); i++){
				RollNode rn = bn.getRollNode(i);
				int total=0;
				int chance = rn.getNumBoards();
				for(int j =0; j<rn.getNumBoards(); j++){
					h = minimax(rn.getBoard(j));
					//rn.setH = average of these boards
					total += h;
				}
				if(chance>0){
					total = total/chance;
					rn.setH(total);
				}
				if(bn.getTurn()==1){
					if(rn.getH()>h){
						h=rn.getH();
					}
				}else{
					if(rn.getH()<h){
						h=rn.getH();
					}
				}
			}
			bn.setH(h);
			return h;
		}
	}
	// adds all of the possibilities of boards given a rollnode, incorporates alpha-beta pruning
	public int addPossibilities(RollNode rn, Board b, int turn){
		int boards=0;
		int die1 = rn.getDice()[0];
		int die2 = rn.getDice()[1];
		// if we have more than 1 waiting
		if(b.waiting(turn)>1){
			boolean e1=false, e2=false;
			if(Engine.isValidEnter(b, turn, die1))
				e1=true;
			if(Engine.isValidEnter(b, turn, die2))
				e2=true;
			
			
			Board newboard = new Board(b);
			// if both enters are valid
			if(e1&&e2)
				enter(newboard, turn, new int[]{die1, die2});
			else if(e1)
				enter(newboard, turn, new int[]{die1, 0});
			else if(e2)
				enter(newboard, turn, new int[]{0, die2});
			else{
				//pass
			}
			
			int h;
			if((h = generateHeuristic(b, newboard)) < rn.getMinH() && turn==myTurn){
				BoardNode bn = new BoardNode(newboard, getOpponent(turn));
				rn.setMinH(h, rn.getNumBoards());
				bn.setH(h);
				rn.addBoardNode(bn);
				boards++;
			}else if(turn==getOpponent(myTurn) && (h = generateHeuristic(b, newboard)) > rn.getMaxH()){
				rn.setMaxH(h, boards);
				BoardNode bn = new BoardNode(newboard, getOpponent(turn));
				bn.setH(h);
				rn.addBoardNode(bn);
				boards++;
			}
		
		// if turn only has 1 checker waiting
		}else if(b.waiting(turn) == 1){
			//check first die, if valid enter, get all possibilities of second die moves
			if(Engine.isValidEnter(b, turn, die1)){
				Board baseNewBoard = new Board(b);
				baseNewBoard.enter(turn, die1);
				//goes through every space to test for possible moves
				for(int i=0;i<=11;i++){
					// if it is a valid move, add to possibilites
					if(Engine.isValidMove(baseNewBoard, turn, i, die2)){
						Board newboard = new Board(baseNewBoard);
						newboard.move(turn, i, die2);
						int h;
						if((h = generateHeuristic(b, newboard)) < rn.getMinH() && turn==myTurn){
							BoardNode bn = new BoardNode(newboard, getOpponent(turn));
							rn.setMinH(h, rn.getNumBoards());
							bn.setH(h);
							rn.addBoardNode(bn);
							boards++;
						}else if(turn==getOpponent(myTurn) && (h = generateHeuristic(b, newboard)) > rn.getMaxH()){
							rn.setMaxH(h, boards);
							BoardNode bn = new BoardNode(newboard, getOpponent(turn));
							bn.setH(h);
							rn.addBoardNode(bn);
							boards++;
						}
					}
				}
				// if we didn't have any valid moves, add the board with just the enter
				if(boards==0){
					BoardNode bn = new BoardNode(baseNewBoard, getOpponent(turn));
					rn.addBoardNode(bn);
					boards++;
				}
			}
			
			//check second die, if valid enter, get all possibilites of first die moves
			if(Engine.isValidEnter(b, turn, die2)){
				Board baseNewBoard = new Board(b);
				baseNewBoard.enter(turn, die2);
				for(int i=0;i<=11;i++){
					// if it is a valid move, add to possibilites
					if(Engine.isValidMove(baseNewBoard, turn, i, die2)){
						Board newboard = new Board(baseNewBoard);
						newboard.move(turn, i, die2);
						int h;
						if((h = generateHeuristic(b, newboard)) < rn.getMinH() && turn==myTurn){
							BoardNode bn = new BoardNode(newboard, getOpponent(turn));
							rn.setMinH(h, rn.getNumBoards());
							bn.setH(h);
							rn.addBoardNode(bn);
							boards++;
						}else if(turn==getOpponent(myTurn) && (h = generateHeuristic(b, newboard)) > rn.getMaxH()){
							rn.setMaxH(h, boards);
							BoardNode bn = new BoardNode(newboard, getOpponent(turn));
							bn.setH(h);
							rn.addBoardNode(bn);
							boards++;
						}
					}
				}
				//if we still haven't found any possibilities, add board with just the enter
				if(boards==0){
					BoardNode bn = new BoardNode(baseNewBoard, getOpponent(turn));
					bn.setH(generateHeuristic(b, baseNewBoard));
					rn.addBoardNode(bn);
					boards++;
				}
			}
		// if all of the pieces are home, we need a bearing strategy
	
		}else if(b.allHome(turn)){
			int stspace, endspace, mod;
			if(turn==1){
				stspace=9;
				endspace=12;
				mod=1;
			}else{
				stspace=2;
				endspace=-1;
				mod=-1;
			}
			Board newboard, base;
			for(int i=stspace; i!=endspace; i+=mod){
				base = new Board(b);	
				if(Engine.isValidBear(base, turn, i, die2, new int[]{die2, 0})){
					base.bear(turn, i);
					for(int j=stspace;j!=endspace;j+=mod){
						newboard = new Board(base);
						if(Engine.isValidBear(newboard, turn, j, die1, new int[]{die1, 0})){
							newboard.bear(turn, j);
							int h = generateHeuristic(b, newboard);
							BoardNode bn = new BoardNode(newboard, getOpponent(turn));
							bn.setH(h);
							rn.addBoardNode(bn);
							boards++;
						}
					}
				}
			}
			if(boards==0){
				for(int i=stspace; i!=endspace; i+=mod){
					if(Engine.isValidBear(b, turn, i, die1, new int[]{die1, die2})){
						newboard = new Board(b);
						newboard.bear(turn, i);
						int h = generateHeuristic(b, newboard);
						BoardNode bn = new BoardNode(newboard, getOpponent(turn));
						bn.setH(h);
						rn.addBoardNode(bn);
					}else if(Engine.isValidBear(b, turn, i, die2, new int[]{die1, die2})){
						newboard = new Board(b);
						newboard.bear(turn, i);
						int h = generateHeuristic(b, newboard);
						BoardNode bn = new BoardNode(newboard, getOpponent(turn));
						bn.setH(h);
						rn.addBoardNode(bn);
					}
				}
			}
		// else if there are 0 waiting, and now ready to move/bear
		}else{
			// compute cartesian product of moves each die can make
			for(int i=0;i<=11;i++){
				// if valid move from first die
				if(Engine.isValidMove(b, turn, i, die1) || Engine.isValidBear(b, turn, i, die1, new int[]{die1, 0})){
					Board baseNewBoard = new Board(b);
					
					if(Engine.isValidMove(b, turn, i, die1))
						baseNewBoard.move(turn, i, die1);
					else
						baseNewBoard.bear(turn, i);
					
					// check all other moves with other die
					for(int j=0;j<=11;j++){
						// if both are valid moves, add to list
						if(Engine.isValidMove(baseNewBoard, turn, j, die2) || Engine.isValidBear(b, turn, j, die2, new int[]{0, die2})){
							Board newboard = new Board(baseNewBoard);
							
							if(Engine.isValidMove(baseNewBoard, turn, j, die2))
								newboard.move(turn, j, die2);
							else
								newboard.bear(turn, j);
							
							int h = generateHeuristic(b, newboard);
							if(turn==myTurn && h<rn.getMinH()){
								rn.setMinH(h, boards);
								BoardNode bn = new BoardNode(newboard, getOpponent(turn));
								bn.setH(h);
								rn.addBoardNode(bn);
								boards++;
							}else if(turn==getOpponent(myTurn) && h>rn.getMaxH()){
								rn.setMaxH(h, boards);
								BoardNode bn = new BoardNode(newboard, getOpponent(turn));
								bn.setH(h);
								rn.addBoardNode(bn);
								boards++;
							}
							
						}
					}				
				}
				//if valid move from second die
				if(Engine.isValidMove(b, turn, i, die2) || Engine.isValidBear(b, turn, i, die2, new int[]{die1, die2})){
					Board baseNewBoard = new Board(b);
					
					if(Engine.isValidMove(b, turn, i, die2))
						baseNewBoard.move(turn, i, die2);
					else{
						baseNewBoard.bear(turn, i);
					}
					// check all other moves with other die
					
					for(int j=0;j<=11;j++){
						// if both are valid moves, add to list
						if(Engine.isValidMove(baseNewBoard, turn, j, die1) || Engine.isValidBear(b, turn, j, die1, new int[]{0, die1})){
							Board newboard = new Board(baseNewBoard);
							
							if(Engine.isValidMove(baseNewBoard, turn, j, die1))
								newboard.move(turn, j, die1);
							else
								newboard.bear(turn, j);
							
							BoardNode bn = new BoardNode(newboard, getOpponent(turn));
							bn.setH(generateHeuristic(b, newboard));
							rn.addBoardNode(bn);
							boards++;
						}
					}
				}

			}
			
			// if we didn't find any possible boards, check the one-die possibilities
			if(boards==0){
				for(int i=0;i<=11;i++){
					if(Engine.isValidMove(b, turn, i, die1)){
						Board newboard = new Board(b);
						newboard.move(turn, i, die1);
						BoardNode bn = new BoardNode(newboard, getOpponent(turn));
						bn.setH(generateHeuristic(b, newboard));
						rn.addBoardNode(bn);
						boards++;
					}
					if(Engine.isValidMove(b, turn, i, die2)){
						Board newboard = new Board(b);
						newboard.move(turn, i, die2);
						BoardNode bn = new BoardNode(newboard, getOpponent(turn));
						bn.setH(generateHeuristic(b, newboard));
						rn.addBoardNode(bn);
						boards++;
					}
				}
			}
		
		}
		return boards;
	}
	
	// same as the addpossibilities method, however adds the possibilities given a roll of doubles,
	// also incorporates some alpha-beta pruning
	public int addPossibilitiesDubs(RollNode rn, Board b, int turn, int die){
		int boards=0;
		if(b.waiting(turn)>3){
			if(Engine.isValidEnter(b, turn, die)){
				Board newboard = new Board(b);
				for(int i=0;i<4;i++){
					newboard.enter(turn, die);
				}
				BoardNode bn = new BoardNode(newboard, getOpponent(turn));
				int h = generateHeuristic(b, newboard);
				if(turn==myTurn && h<rn.getMinH()){
					rn.setMinH(h, rn.getNumBoards());
					bn.setH(h);
					rn.addBoardNode(bn);
					boards++;
				}else if(turn==getOpponent(myTurn) && h>rn.getMaxH()){
					rn.setMaxH(h, rn.getNumBoards());
					bn.setH(h);
					rn.addBoardNode(bn);
					boards++;
				}
			}
		}else if(b.allHome(turn)){
			int sthome, endhome, mod;
			if(turn==1){
				sthome=9;
				endhome=12;
				mod=1;
			}else{
				sthome=2;
				endhome=-1;
				mod=-1;
			}	
			Board base;
			Board newboard;
			for(int i=sthome;i!=endhome;i+=mod){
				base = new Board(b);
				newboard = new Board(base);
				// if first die at space i is bearable
				if(Engine.isValidBear(newboard, turn, i, die, new int[]{die, die, die, die})){
					// set the bear
					newboard.bear(turn, i);
					// if there are still pieces on the board
					if(b.countBoard(turn) > 1){
						// check each home space
						base = new Board(newboard);
						for(int j=sthome;j!=endhome;j+=mod){
							newboard = new Board(base);
							// if second die at space j is bearable, with first die being bearable
							if(Engine.isValidBear(newboard, turn, j, die, new int[]{die, die, die, die})){
								newboard.bear(turn, j);
							
								// if still pieces on board
								if(b.countBoard(turn)>2){
									base = new Board(newboard);
									for(int k=sthome; k!=endhome; k+=mod){
										//if third die at space k is bearable, with previous 2 dice bearable
										if(Engine.isValidBear(newboard, turn, k, die, new int[]{die, die, die, die})){
											newboard.bear(turn, k);
											base = new Board(newboard);
												// if still pieces on board (means at least 4 were on board to start)
												if(b.countBoard(turn)>3){
													//check each home space
													base = new Board(newboard);
													for(int l=sthome; l!=endhome; l+=mod){
														// if 4th space is bearable
														newboard = new Board(base);
														if(Engine.isValidBear(newboard, turn, l, die, new int[]{die, die, die, die})){
															newboard.bear(turn, l);
															BoardNode bn = new BoardNode(newboard, getOpponent(turn));
															int h = generateHeuristic(base, newboard);
															bn.setH(h);
															rn.addBoardNode(bn);
															boards++;
														}
													}
												}else{
													//else if no more spaces on board
													BoardNode bn = new BoardNode(newboard, getOpponent(turn));
													int h = generateHeuristic(base, newboard);
													bn.setH(h);
													rn.addBoardNode(bn);
													boards++;
												}
											}
										}
									}else{
										//else if no more spaces on board
										BoardNode bn = new BoardNode(newboard, getOpponent(turn));
										int h = generateHeuristic(base, newboard);
										bn.setH(h);
										rn.addBoardNode(bn);
										boards++;
									}
								}
							}
						}else{
							//else if no more spaces on board
							BoardNode bn = new BoardNode(newboard, getOpponent(turn));
							int h = generateHeuristic(base, newboard);
							bn.setH(h);
							rn.addBoardNode(bn);
							boards++;
						}
				}
			}
			
		}else{
			int diceLeft=4;
			Board newboardbase = new Board(b);
			if(Engine.isValidEnter(b, turn, die)){
				while(newboardbase.waiting(turn)>0){
					newboardbase.enter(turn, die);
					diceLeft--;
				}
			}
			Board newboard;
			//check all spaces for first space
			// WE ARE ABOUT TO GO ON AN ADVENTURE. HOPE YOU HAVE A LOT OF MEMORY :D
			// we will be finding every possible move from doubles where waiting<=3
			for(int i=0;i<=11;i++){
				Board newboardbase2 = new Board(newboardbase);
				//if moving to space i is a valid move
				if(Engine.isValidMove(newboardbase, turn, i, die)|| Engine.isValidBear(newboardbase, turn, i, die, new int[]{die,0,0,0})){	
					newboard = new Board(newboardbase);
					if(Engine.isValidMove(newboard, turn, i, die))
						newboard.move(turn, i, die);
					else
						newboard.bear(turn, i);
					//if theres 2 dice left, 
					if(diceLeft>1){
						newboardbase2= new Board(newboard);
						for(int j=i;j<=11;j++){
							//if moving to space j is a valid move
							newboard = new Board(newboardbase2);
							if(Engine.isValidMove(newboard, turn, j, die) || Engine.isValidBear(newboard, turn, j, die, new int[]{die,0,0,0})){
								
								if(Engine.isValidMove(newboard, turn, j, die))
									newboard.move(turn, j, die);
								else
									newboard.bear(turn, j);
	
								if(diceLeft>2){
									newboardbase2 = new Board(newboard);
									for(int k=j;k<=11;k++){
										newboard = new Board(newboardbase2);
										if(Engine.isValidMove(newboard, turn, k, die) || Engine.isValidBear(newboard, turn, k, die, new int[]{die,0,0,0})){
											
											if(Engine.isValidMove(newboard, turn, k, die))
												newboard.move(turn, k, die);
											else
												newboard.bear(turn, k);
											
											// IF WE HAVE MORE THAN 3 DICE LEFT
											if(diceLeft>3){
												newboardbase2 = new Board(newboard);
												for(int l=k; l<=11;l++){
													newboard = new Board(newboardbase2);
													if(Engine.isValidMove(newboard, turn, l, die) || Engine.isValidBear(newboard, turn, l, die, new int[]{die,0,0,0})){
														if(Engine.isValidMove(newboard, turn, l, die))
															newboard.move(turn, l, die);
														else
															newboard.bear(turn, l);
														
														int h = generateHeuristic(b, newboard);
														if(turn==myTurn && h<rn.getMinH()){
															rn.setMinH(h, rn.getNumBoards()+1);
															BoardNode bn = new BoardNode(newboard, getOpponent(turn));
															bn.setH(h);
															rn.addBoardNode(bn);
															boards++;
														}else if(turn==getOpponent(myTurn) && h>rn.getMaxH()){
															rn.setMaxH(h, rn.getNumBoards()+1);
															BoardNode bn = new BoardNode(newboard, getOpponent(turn));
															bn.setH(h);
															rn.addBoardNode(bn);
															boards++;
														}
													}
												}
											}else{
												int h = generateHeuristic(b, newboard);
												if(turn==myTurn && h<rn.getMinH()){
													rn.setMinH(h, rn.getNumBoards()+1);
													BoardNode bn = new BoardNode(newboard, getOpponent(turn));
													bn.setH(h);
													rn.addBoardNode(bn);
													boards++;
												}else if(turn==getOpponent(myTurn) && h>rn.getMaxH()){
													rn.setMaxH(h, rn.getNumBoards()+1);
													BoardNode bn = new BoardNode(newboard, getOpponent(turn));
													bn.setH(h);
													rn.addBoardNode(bn);
													boards++;
												}
											}
										}
									}
								}else{
									int h = generateHeuristic(b, newboard);
									if(turn==myTurn && h<rn.getMinH()){
										rn.setMinH(h, rn.getNumBoards()+1);
										BoardNode bn = new BoardNode(newboard, getOpponent(turn));
										bn.setH(h);
										rn.addBoardNode(bn);
										boards++;
									}else if(turn==getOpponent(myTurn) && h>rn.getMaxH()){
										rn.setMaxH(h, rn.getNumBoards()+1);
										BoardNode bn = new BoardNode(newboard, getOpponent(turn));
										bn.setH(h);
										rn.addBoardNode(bn);
										boards++;
									}
								}//end dice==3 if
							}//end valid move for 2 dice
						}
					}else{
						int h = generateHeuristic(b, newboard);
						if(turn==myTurn && h<rn.getMinH()){
							rn.setMinH(h, rn.getNumBoards()+1);
							BoardNode bn = new BoardNode(newboard, getOpponent(turn));
							bn.setH(h);
							rn.addBoardNode(bn);
							boards++;
						}else if(turn==getOpponent(myTurn) && h>rn.getMaxH()){
							rn.setMaxH(h, rn.getNumBoards()+1);
							BoardNode bn = new BoardNode(newboard, getOpponent(turn));
							bn.setH(h);
							rn.addBoardNode(bn);
							boards++;
						}
					} //End dice==1 if
				}
			}
		}
		return boards;
	}
	
	
	
	public void enter(Board b, int turn, int[] die){
		for(int i=0;i<die.length; i++){
			if(die[i]!=0)
				b.enter(turn, die[i]);
		}
	}

	public int getOpponent(int turn){
		if(turn==1)
			return 2;
		else
			return 1;
	}
	// HEURISTIC GENERATED HERE
	public static int generateHeuristic(Board b1, Board b2){
		int h=0;
		for(int i=3; i<=11; i++){
			h+=(b2.getPieces(2)[i] * (i-2));
			if(b2.getPieces(2)[i]>1)
				h-=2;
		}
		
		h -= b2.getBear(2);
		
		if(b2.waiting(1)>b1.waiting(1)){
			h-=5;
		}
		h+=(b2.waiting(2)*9);
		return h;
	}
	// SOME TESTING METHODS
	public static String boardToString(Board b){
		String r = "";
		for(int i=0; i<b.getPieces(1).length;i++){
			r+=b.getPieces(1)[i] + "  ";
		}
		r += "\n";
		for(int i=0; i<b.getPieces(2).length;i++){
			r+=b.getPieces(2)[i] + "  ";
		}
		return r;
	}
	
	public boolean compArray(int[] a1, int[] a2){
		if(a1.length == a2.length){
			for(int i=0;i<a1.length; i++){
				if (a1[i] != a2[i])
					return false;
			}
			return true;
		}else{
			return false;
		}
	}
	public void printRoot(){
		System.out.println(boardToString(root.getBoard().getBoard()));
	}

	
	public void printRollNodes(){
		for(int i=0; i<root.getRollsSize();i++){
			for(int j=0; j<root.getRollNode(i).getNumBoards(); j++){
				System.out.println(boardToString(root.getRollNode(i).getBoard(j).getBoard()));
			}
		}
	}
}
	
