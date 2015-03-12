/**
 * ChatHandler.java
 * CS 342 Project 4 - Networked Chat Application
 */
package edu.uic.cs342.project5.network.chat.server;

import edu.uic.cs342.project5.network.chat.common.ChatData;
import edu.uic.cs342.project5.network.chat.common.ChatUtils;
import edu.uic.cs342.project5.network.chat.common.NodeInfo;

/**
 * Maintains a connection with a client and handles data received
 * from the client.
 */
public class ClientHandler implements Runnable {
	private NodeInfo client;
	private ChatServer chatServer;
	
	/**
	 * Constructor for ClientHandler retains connection information
	 * @param clientInfo Node for client
	 * @param chatServer The server that instantiated this class
	 */
	public ClientHandler(NodeInfo clientInfo, ChatServer chatServer) {
		this.chatServer = chatServer;
		this.client = clientInfo;

	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		ChatData chatData;
		try {
			while ( (chatData = (ChatData)client.getObjectInputStream().readObject() ) != null) {         
				//System.out.println("read " + chatData.getMessage());
				String clientName;
				if (chatData.getRecipients() == null)
					clientName = ChatUtils.getName(client.getId());
				else
					clientName = ChatUtils.getWhisperName(client.getId());
				chatData.setMessage(clientName + " " + chatData.getMessage());
				chatServer.broadcast(chatData);
			}
		} catch(Exception ex) {
			//disconnected
			chatServer.removeClient(client);
			//ex.printStackTrace();
		}
	}
}