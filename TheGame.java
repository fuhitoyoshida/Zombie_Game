///////////////////////////////////////////////////////////////////////////////
// Title:            The Game
// Files:            TheGame.java, Room.java, Item.java, Player.java, 
//					 DirectedGraph.java
// Semester:         CS367 Fall 2015
//
// Author:           Fuhito Yoshida
// Email:            fyoshida@wisc.edu
// CS Login:         fuhito
// Lecturer's Name:  Jim Skrentny
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ////////////////////
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * The class that is responsible for reading the input file and processing 
 * player commands. The game is initialized by passing the input file as 
 * a command line argument.
 *
 * @author Fuhito Yoshida
 */
public class TheGame {
	private static String gameIntro; // initial introduction to the game
	private static String winningMessage; //winning message of game
	private static String gameInfo; //additional game info
	private static boolean gameWon = false; //state of the game
	private static Scanner scanner = null; //for reading files
	private static Scanner ioscanner = null; //for reading standard input
	private static Player player; //object for player of the game
	private static Room location; //current room in which player is located
	private static Room winningRoom; //Room which player must reach to win
	private static Item winningItem; //Item which player must find
	private static DirectedGraph<Room> layout; //Graph structure of the Rooms
	public static Set<Item> startingItems; //Starting items

	/**
	 * Main method for the program. It accepts the input text file and
	 * process it by calling initilizeGame method.
	 *
	 * @param (args) input file for initialization.
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("Bad invocation! Correct usage: "
					+ "java AppStore <gameFile>");
			System.exit(1);
		}

		boolean didInitialize = initializeGame(args[0]);

		if (!didInitialize) {
			System.err.println("Failed to initialize the application!");
			System.exit(1);
		}

		System.out.println(gameIntro); // game intro

		processUserCommands();
	}

	/**
	 * Reads the input file and initializes all variables for the game to run
	 * smoothly. It also allows users to input his name.
	 *
	 * @param (gameFile) input file for initialization.
	 */
	private static boolean initializeGame(String gameFile) {

		try {
			// reads player name
			System.out.println("Welcome worthy squire! What might be your name?");
			ioscanner = new Scanner(System.in);
			String playerName = ioscanner.nextLine();

			//create layout of rooms
			layout = new DirectedGraph<Room>();
			File file = new File(gameFile);
			//reads in file
			scanner = new Scanner(file);
			boolean intro = true;
			boolean startingLocation = true;

			String inputStr = null;			
			while(scanner.hasNextLine()){
				if(intro){
					gameIntro = scanner.nextLine();
					winningMessage = scanner.nextLine();
					gameInfo = scanner.nextLine();
					intro = false;

					inputStr = scanner.nextLine();
				}

				Set<Item> items = new HashSet<Item>();
				String state = null;

				String roomName = null;
				String roomDescription = null;
				boolean visibility = false;
				boolean habitability = false;
				String habMsg = null;

				if(inputStr.contains("#player")){
					state = "#player";
					inputStr = scanner.nextLine().trim();

				}else if(inputStr.contains("#room:")){
					state = "#room";
					if(inputStr.contains("#room:#win")){
						state = "#room:#win";
					}
					roomName = scanner.nextLine().trim();
					roomDescription = scanner.nextLine().trim();
					visibility = scanner.nextLine().trim().toLowerCase().equals("true") ? true : false;
					habitability = scanner.nextLine().trim().toLowerCase().equals("true") ? true : false;
					if(!habitability){
						habMsg = scanner.nextLine().trim(); 
					}
					inputStr = scanner.nextLine().trim();

				}else if(inputStr.contains("#locked")){
					state = "#locked";

				}else if(inputStr.contains("#Adjacency")){
					state = "#Adjacency";

				}else{
					break;
				}

				//input items
				while(inputStr.contains("#item:")){

					String itemName = scanner.nextLine().trim();
					String itemDescription = scanner.nextLine().trim();
					boolean activated = scanner.nextLine().trim().toLowerCase().equals("true") ? true : false;
					String message = scanner.nextLine().trim();

					boolean oneTimeUse = scanner.nextLine().trim().toLowerCase().equals("true") ? true : false;
					String usedString = scanner.nextLine().trim();

					Item item = new Item(itemName, itemDescription, activated, message, oneTimeUse, usedString);
					items.add(item);

					//winning item
					if(inputStr.contains("#item:#win")){
						winningItem = item;
					}

					inputStr = scanner.nextLine().trim();
				}

				if(state.equals("#player")){
					startingItems = items;
					player = new Player(playerName, items);
				}

				//input rooms
				if(state.equals("#room") || state.equals("#room:#win")){
					List<MessageHandler> handlers = new ArrayList<MessageHandler>();

					while(inputStr.contains("#messageHandler:")){
						String msg = scanner.nextLine().trim();
						String type = null;
						String room = null;
						String guess = scanner.nextLine().trim();
						if(guess.equals("room")){
							room = scanner.nextLine().trim();
						}
						type = guess;

						MessageHandler msgHandler = new MessageHandler(msg, type, room);
						handlers.add(msgHandler);
						inputStr = scanner.nextLine().trim();
					}

					Room room = new Room(roomName, roomDescription, visibility, habitability, habMsg, items, handlers);
					layout.addVertex(room);

					if(startingLocation){
						location = room;
						startingLocation = false;
					}

					//winning room
					if(state.equals("#room:#win")){
						winningRoom = room;
					}
				}

				if(state.equals("#locked")){
					String[] rooms = scanner.nextLine().split(" ");
					while(!rooms[0].contains("#Adjacency")){
						Set<Room> searchV = layout.getAllVertices(); 
						Iterator<Room> itr = searchV.iterator();

						Room roomA = null;
						Room roomB = null;
						while(itr.hasNext()){
							Room room = itr.next();
							if(room.getName().equals(rooms[0])) roomA = room;
							if(room.getName().equals(rooms[1])) roomB = room;
						}
						roomA.addLockedPassage(roomB, scanner.nextLine());						
						inputStr = scanner.nextLine();
						rooms = inputStr.split(" ");
					}
				}

				if(state.equals("#Adjacency")){
					String[] rooms = scanner.nextLine().trim().split(" ");
					Set<Room> searchV = layout.getAllVertices();
					Iterator<Room> itr = searchV.iterator();

					if(rooms.length > 1){
						Room roomA = null;
						Room roomB = null;
						while(itr.hasNext()){
							Room room = itr.next();
							if(room.getName().equals(rooms[0])) roomA = room;
							if(room.getName().equals(rooms[1])) roomB = room;
						}
						layout.addEdge(roomA, roomB);
					}
				}

			}

			scanner.close();
			//reads in successfully
			return true;


		} catch (FileNotFoundException e) {
			e.printStackTrace();
			//reads in failure
			return false;
		} catch (Exception e){
			//reads in failure
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Reads in user command and process the command.
	 * 
	 */
	private static void processUserCommands() {
		String command = null;
		do {

			System.out.print("\nPlease Enter a command ([H]elp):");
			command = ioscanner.next();
			switch (command.toLowerCase()) {
			case "p": // pick up
				processPickUp(ioscanner.nextLine().trim());
				goalStateReached();
				break;
			case "d": // put down item
				processPutDown(ioscanner.nextLine().trim());
				break;
			case "u": // use item
				processUse(ioscanner.nextLine().trim());
				break;
			case "lr":// look around
				processLookAround();
				break;
			case "lt":// look at
				processLookAt(ioscanner.nextLine().trim());
				break;
			case "ls":// look at sack
				System.out.println(player.printSack());
				break;
			case "g":// goto room
				processGoTo(ioscanner.nextLine().trim());
				goalStateReached();
				break;
			case "q":
				System.out.println("You Quit! You, " + player.getName() + ", are a loser!!");
				break;
			case "i":
				System.out.println(gameInfo);
				break;
			case "h":
				System.out
				.println("\nCommands are indicated in [], and may be followed by \n"+
						"any additional information which may be needed, indicated within <>.\n"
						+ "\t[p]  pick up item: <item name>\n"
						+ "\t[d]  put down item: <item name>\n"
						+ "\t[u]  use item: <item name>\n"
						+ "\t[lr] look around\n"
						+ "\t[lt] look at item: <item name>\n"
						+ "\t[ls] look in your magic sack\n"
						+ "\t[g]  go to: <destination name>\n"
						+ "\t[q]  quit\n"
						+ "\t[i]  game info\n");
				break;
			default:
				System.out.println("Unrecognized Command!");
				break;
			}
		} while (!command.equalsIgnoreCase("q") && !gameWon);
		ioscanner.close();
	}

	/**
	 *Look around which room exits and which can possibly get into.
	 *
	 */
	private static void processLookAround() {
		System.out.print(location.toString());
		for(Room rm : layout.getNeighbors(location)){
			System.out.println(rm.getName());
		}
	}

	/**
	 * Look at the item.
	 *
	 * @param (item) item you try to look at.
	 */
	private static void processLookAt(String item) {
		Item itm = player.findItem(item);
		if(itm == null){
			itm = location.findItem(item);
		}
		if(itm == null){
			System.out.println(item + " not found");
		}
		else
			System.out.println(itm.toString());
	}

	/**
	 * Pick up an item. If you have it, you don't pick up.
	 * @param (item) item you are trying to pickup.
	 */
	private static void processPickUp(String item) {
		if(player.findItem(item) != null){
			System.out.println(item + " already in sack");
			return;
		}
		Item newItem = location.findItem(item);
		if(newItem == null){
			System.out.println("Could not find " + item);
			return;
		}
		player.addItem(newItem);
		location.removeItem(newItem);
		System.out.println("You picked up ");
		System.out.println(newItem.toString());
	}

	/**Put down the item. If you don't have it, you print out a line.
	 * 
	 * @param (item) item you are trying to put down.
	 */
	private static void processPutDown(String item) {
		if(player.findItem(item) == null){
			System.out.println(item + " not in sack");
			return;
		}
		Item newItem = player.findItem(item);
		location.addItem(newItem);
		player.removeItem(newItem);
		System.out.println("You put down " + item);
	}

	/**
	 * Use the item if you have it.
	 * @param (item) item you are trying to use.
	 */
	private static void processUse(String item) {
		Item newItem = player.findItem(item);
		if(newItem == null){
			System.out.println("Your magic sack doesn't have a " + item);
			return;
		}
		if (newItem.activated()) {
			System.out.println(item + " already in use");
			return;
		}
		if(notifyRoom(newItem)){
			if (newItem.isOneTimeUse()) {
				player.removeItem(newItem);
			}
		}
	}

	/**
	 * You try to go to the destination, if the room's neighbor has the 
	 * destination.
	 * @param (destination) room you are trying to get to.
	 */
	private static void processGoTo(String destination) {
		Room dest = findRoomInNeighbours(destination);
		if(dest == null) {
			for(Room rm : location.getLockedPassages().keySet()){
				if(rm.getName().equalsIgnoreCase(destination)){
					System.out.println(location.getLockedPassages().get(rm));
					return;
				}
			}
			System.out.println("Cannot go to " + destination + " from here");
			return;
		}
		Room prevLoc = location;
		location = dest;
		if(!player.getActiveItems().isEmpty())
			System.out.println("The following items are active:");
		for(Item itm:player.getActiveItems()){
			notifyRoom(itm);
		}
		if(!dest.isHabitable()){
			System.out.println("Thou shall not pass because");
			System.out.println(dest.getHabitableMsg());
			location = prevLoc;
			return;
		}

		System.out.println();
		processLookAround();
	}

	/**
	 * Notify user a message.
	 * @param (item) item.
	 * @return return true if you are successfully notified.
	 */
	private static boolean notifyRoom(Item item) {
		Room toUnlock = location.receiveMessage(item.on_use());
		if (toUnlock == null) {
			if(!item.activated())
				System.out.println("The " + item.getName() + " cannot be used here");
			return false;
		} else if (toUnlock == location) {
			System.out.println(item.getName() + ": " + item.on_useString());
			item.activate();
		} else {
			// add edge from location to to Unlock
			layout.addEdge(location, toUnlock);
			if(!item.activated())
				System.out.println(item.on_useString());
			item.activate();
		}
		return true;
	}

	/**
	 * Look for neighbors of the room.
	 * @param (room) room's neighbor is what we are looking for.
	 * @return return the neighbor.
	 */
	private static Room findRoomInNeighbours(String room) {
		Set<Room> neighbours = layout.getNeighbors(location);
		for(Room rm : neighbours){
			if(rm.getName().equalsIgnoreCase(room)){
				return rm;
			}
		}
		return null;
	}

	/**
	 * Tell whether you have reached the goal.
	 * @return return true if you have reached goal.
	 */
	private static void goalStateReached() {
		if ((location == winningRoom && player.hasItem(winningItem)) 
				|| (location == winningRoom && winningItem == null)){
			System.out.println("Congratulations, " + player.getName() + "!");
			System.out.println(winningMessage);
			System.out.println(gameInfo);
			gameWon = true;
		}
	}

}
