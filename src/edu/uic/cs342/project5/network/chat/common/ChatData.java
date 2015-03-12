/**
 * ChatData.java
 * CS 342 Project 4 - Networked Chat Application
 */
package edu.uic.cs342.project5.network.chat.common;

import java.io.Serializable;
import java.util.List;

/**
 * ChatData is the object that the server and client sends/receives
 * It contains lists and messages.
 */
public class ChatData implements Serializable {	
	private static final long serialVersionUID = 5490817390807959380L;
	private List<NodeData> clients;
	private List<NodeData> recipients;
	private String message;
	
	/**
	 * Constructor for ChatData when used as a way to update
	 * the connected list for each client. 
	 * @param clients List of clients connected
	 */
	public ChatData(List<NodeData> clients) {
		this.clients = clients;
	}
	
	/**
	 * Constructor used for ChatData when trying to send a message
	 * to clients
	 * @param recipients Whom the message should be sent to
	 * @param message The message
	 */
	public ChatData(List<NodeData> recipients, String message) {
		this.recipients = recipients;
		this.message = message;
	}

	/**
	 * Getter for clients
	 * @return list of connected clients
	 */
	public List<NodeData> getClients() {
		return clients;
	}

	/**
	 * Getter for recipients
	 * @return the recipients
	 */	
	public List<NodeData> getRecipients() {
		return recipients;
	}

	/**
	 * Getter for message
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Setter for message
	 * @param message The message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	

}
