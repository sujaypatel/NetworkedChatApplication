package edu.uic.cs342.project5.network.game.server;
/**
 * ChatHandler.java
 * CS 342 Project 4 - Networked Chat Application
 */

import edu.uic.cs342.project5.models.RummyModel;
import edu.uic.cs342.project5.network.chat.common.NodeInfo;

/**
 * Maintains a connection with a client and handles data received
 * from the client.
 */
public class ClientHandler implements Runnable {
	private NodeInfo client;
	private GameServer gameServer;
	
	/**
	 * Constructor for ClientHandler retains connection information
	 * @param clientInfo Node for client
	 * @param chatServer The server that instantiated this class
	 */
	public ClientHandler(NodeInfo clientInfo, GameServer chatServer) {
		this.gameServer = chatServer;
		this.client = clientInfo;

	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		Object receivedObject;
		try {
			while ( (receivedObject = client.getObjectInputStream().readObject() ) != null) {
				if (receivedObject instanceof RummyModel) {
					gameServer.broadcast(receivedObject);
				} else {
					
				}
			}
		} catch(Exception ex) {
			gameServer.disconnect();
		}
	}
}