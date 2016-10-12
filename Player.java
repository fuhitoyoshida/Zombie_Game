///////////////////////////////////////////////////////////////////////////////
// Main Class File:  TheGame.java
// File:             Player.java
// Semester:         CS367 Fall 2015
//
// Author:           Fuhito Yoshida
// CS Login:         fuhito
// Lecturer's Name:  Jim Skrentny 
//
///////////////////////////////////////////////////////////////////////////////
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This class represents the player in the game. Also player can collect 
 * items that can be stored in magicSack.
 *
 * @author Fuhito Yoshida
 */
public class Player {
	// player name
	private String name;
	// the magic sack held by the player that contains all his/her items
	private Set<Item> magicSack;
	//Do not add anymore private data members

	/**
	 * Constructor for Player class. 
	 * @param (name) name of player.
	 * @param (startingItems) items that you have from the beginging of 
	 * the game.
	 */
	public Player(String name, Set<Item> startingItems){
		if(name == null || startingItems == null) throw new 
		IllegalArgumentException();
		this.name = name;
		this.magicSack = startingItems;
	}

	/**
	 * Returns name of the player.
	 * @return returns name of player.
	 */
	public String getName(){
		return this.name;
	}

	//Returns a String consisting of the items in the sack
	//DO NOT MODIFY THIS METHOD
	public String printSack(){
		//neatly printed items in sack
		StringBuilder sb = new StringBuilder();
		sb.append("Scanning contents of your magic sack");
		sb.append(System.getProperty("line.separator"));
		for(Item itm : magicSack){
			sb.append(itm.getName());
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}

	/**
	 * Returns active items in the magicSack.
	 * @return set of items activated.
	 */
	public Set<Item> getActiveItems(){
		//store active items
		Set<Item> activeItems = new HashSet<Item>();

		Iterator<Item> itr = magicSack.iterator();
		while(itr.hasNext()){
			Item magicItem = itr.next();
			if(magicItem.activated()) activeItems.add(magicItem);
		}
		return activeItems;	
	}

	/**
	 * Find item that is passed in.
	 * @param (item) name of the item you are looking for.
	 * @return return item if finds it, otherwise return null.
	 */
	public Item findItem(String item){
		if(item == null) throw new IllegalArgumentException();
		Iterator<Item> itr = magicSack.iterator();
		while(itr.hasNext()){
			Item magicItem = itr.next();
			if(magicItem.getName().equals(item)) return magicItem;
		}
		return null;
	}

	/**
	 * Check whether the item is in your magicSack.
	 * @param (item) item you are looking for.
	 * @return return true if you found it, otherwise return false.
	 */
	public boolean hasItem(Item item){
		if(item == null) throw new IllegalArgumentException();
		//use findItem() method to determine existence
		if(findItem(item.getName()) == null) return false;
		return true;
	}

	/**
	 * Add item to the magic sack if you don't have it.
	 * @param (item) item you are trying to add into.
	 * @return true if there is no duplicate of item in the magicSack, 
	 * otherwise return false.
	 */
	public boolean addItem(Item item){
		if(item == null) throw new IllegalArgumentException();
		return magicSack.add(item);
	}

	/**
	 * remove item from the magicSack if you have it.
	 * @param (item) item you are trying to remove.
	 * @return true if you have successfully remove it, otherwise return false.
	 */
	public boolean removeItem(Item item){
		if(item == null) throw new IllegalArgumentException();
		return magicSack.remove(item);
	}
}
