///////////////////////////////////////////////////////////////////////////////
// Main Class File:  TheGame.java
// File:             Item.java
// Semester:         CS367 Fall 2015
//
// Author:           Fuhito Yoshida
// CS Login:         fuhito
// Lecturer's Name:  Jim Skrentny 
//
///////////////////////////////////////////////////////////////////////////////
/**
 * This class represents the item that players can interact. 
 * i.e. pick up, use, drop
 *
 * @author Fuhito Yoshida
 */
public class Item {
	// name of item
	private String	name;
	// description of item
	private String	description;
	//activation status
	private boolean activated;
	//message for item usage
	private String message;
	//one time usage of item
	private boolean oneTimeUse;
	//on_usedString
	private String usedString;
	
	/**
	 * Constructs an Item object.  
	 * @param (name) name of item.
	 * @param (description) description of item.
	 * @param (activated) whether item is activated.
	 * @param (message) if item is used, we send message to room.
	 * @param (oneTimeUse) whether item is one time usage.
	 * @param (usedString) if item is used, message will be send.
	 */
	public Item(String name, String description, boolean activated, 
			String message,boolean oneTimeUse, String usedString){
    	if(name == null || description == null || message == null || 
    			usedString == null) throw new IllegalArgumentException();
    	this.name = name;
    	this.description = description;
    	this.activated = activated;
    	this.message = message;
    	this.oneTimeUse = oneTimeUse;
    	this.usedString = usedString;
	}
	
	/**
	 * Returns this item name.
	 * @return Returns this item name.
	 */
	public String getName(){
    	return this.name;
	}
	
	/**
	 * Return description of item.
	 * @return return description of item.
	 */
	public String getDescription(){
    	return this.description;
	}
	
	/**
	 * Return activation status.
	 * @return return activation status.
	 */
	public boolean activated(){
    	return activated;
	}
	
	/**
	 * Return message. 
	 * @return return message.
	 */
	public String on_use(){
    	return message;
	}

	/**
	 * Make activation true.
	 */
	public void activate(){
    	this.activated = true;
	}
	
	/**
	 * Return on_useString.
	 * @return return on_useString.
	 */
	public String on_useString(){
    	return this.usedString;
	}
	
	/**
	 * Return one time status.
	 * @return return one time status. 
	 */
	public boolean isOneTimeUse(){
    	return oneTimeUse;
	}

	@Override
	//This returns a String consisting of the name and description of the Item
	//This has been done for you.
	//DO NOT MODIFY
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("Item Name: " + this.name);
		sb.append(System.getProperty("line.separator"));
		sb.append("Description: " + this.description);
		return sb.toString();
	}
}
