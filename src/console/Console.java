/**
 * Main driver class. Maintains the engine instance, the AI-instance, and manages user input
 * 
 * Jeffrey Moon
 * Fall 2014
 */
package console;

import java.util.Scanner;

import brain.DecisionMaker;
import engine.Board;
import engine.Engine;

public class Console {
	private static Engine engine;
	private static DecisionMaker ai;

	public static void main(String[] args) {
		engine = Engine.getEngine();
		ai = new DecisionMaker();
		start();
		mainLoop();
	}
	
	// initiates roll to see who will go first
	public static void start(){
		int p1roll = engine.startRoll();
		int p2roll = engine.startRoll();
		if(p1roll>p2roll) engine.setTurn(1);
		else engine.setTurn(2);
	
	}
	// main loop, continues until victory
	public static void mainLoop(){
		Scanner console = new Scanner(System.in);
		System.out.println("Half-Backgammon with Heuristically Driven AI Opponent");
		System.out.println("By: Jeffrey Moon");
		System.out.println("Fall 2014");
		System.out.println("\n\n");
		System.out.println("Press <enter> to begin");
		console.nextLine();
		clearConsole();
		String sysmessage= String.valueOf(engine.getTurn()) + " has won the start roll!";
		
		// MAIN LOOP
		while(true){
			if(!engine.diceLeft())
				engine.nextTurn();	
			if(engine.getTurn()==1){
				engine.printBoard(sysmessage);
				System.out.print("> ");
				String response = console.nextLine();
				clearConsole();
				if(!handleResponse(response)){
					sysmessage = "Invalid move";
				}else{
					if(engine.victoryCheck(engine.getBoard())>0){
						victory(engine.victoryCheck(engine.getBoard()));
						break;
					}
				sysmessage = "Please make a move";
				}
			}else{
				engine.printBoard("Opponent is determining next move");
				Board b = new Board(ai.chooseMove(engine.getBoard(), engine.getDice()));
				engine.setBoard(b);
				sysmessage="Opponent has made their move";
				engine.printBoard(sysmessage);
				console.nextLine();
				clearConsole();
				engine.nextTurn();
			}
			if(engine.victoryCheck(engine.getBoard())>0){
				victory(engine.victoryCheck(engine.getBoard()));
				break;
			}

		}
		console.close();
	}

	//MOVE MANAGEMENT
	public static boolean handleResponse(String response){
		String args[] = parseArgs(response);
		for(int i=0; i<args.length; i++){
			args[i].toLowerCase();
		}
		if(args[0].equals("move") && args.length==3)
			return engine.move(args);
		else if(args[0].equals("enter") && args.length==2)
			return engine.enter(args);
			
		else if(args[0].equals("bear") && args.length==3)
			return engine.bear(args);
		
		else if(args[0].equals("pass") && args.length==1)
			return engine.pass(args);
		
		else if(args[0].equals("setboard"))
			return engine.loadScenario(args[1]);
		else
			return false;	
	}
	

	public static String[] parseArgs(String response){
		return response.split(" ");
		
	}
	
	public static void victory(int player){
		clearConsole();
		System.out.println("PLAYER " + player +" IS VICTORIOUS!");
	}
	
	
	
	//CONSOLE MANAGEMENT
	
	public static void clearConsole(){
		for(int i=0;i<45;i++)
			System.out.println();
	}
	
}
