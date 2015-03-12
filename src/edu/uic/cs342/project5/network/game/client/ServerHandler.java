/**
 * ServerHandler.java
 * CS 342 Project 4 - Networked Chat Application
 */

package edu.uic.cs342.project5.network.game.client;

import java.io.IOException;

import edu.uic.cs342.project5.models.RummyModel;
import edu.uic.cs342.project5.network.chat.common.NodeInfo;

/**
 * Maintains a connection with the server and handles data
 * received from the server
 */
public class ServerHandler implements Runnable {
	private NodeInfo server;
	private GameClient gameClient;
	
	/**
	 * Constructor retains connection information
	 * @param serverInfo node for server
	 * @param gameClient GameClient that instantiated the class
	 */
	public ServerHandler(NodeInfo serverInfo, GameClient gameClient) {
		this.gameClient = gameClient;
		this.server = serverInfo;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		Object receivedData;
		try {
			while ((receivedData = server.getObjectInputStream().readObject()) != null) {
				if (receivedData instanceof RummyModel) {
					gameClient.boardView.setRummyModel((RummyModel)receivedData);
					gameClient.boardView.updateUI();	
				} else {
					
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			//disconnected
			gameClient.disconnect();
			//e.printStackTrace();
		}
	}
}
