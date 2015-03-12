/**
 * ServerHandler.java
 * CS 342 Project 4 - Networked Chat Application
 */
package edu.uic.cs342.project5.network.chat.client;

import java.io.IOException;

import edu.uic.cs342.project5.network.chat.common.ChatData;
import edu.uic.cs342.project5.network.chat.common.NodeData;
import edu.uic.cs342.project5.network.chat.common.NodeInfo;

/**
 * Maintains a connection with the server and handles data
 * received from the server
 */
public class ServerHandler implements Runnable {
	private NodeInfo server;
	private ChatClient chatClient;
	
	/**
	 * Constructor retains connection information
	 * @param serverInfo node for server
	 * @param chatClient ChatClient that instantiated the class
	 */
	public ServerHandler(NodeInfo serverInfo, ChatClient chatClient) {
		this.chatClient = chatClient;
		this.server = serverInfo;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		ChatData chatData;
		try {
			while ((chatData = (ChatData) server.getObjectInputStream().readObject()) != null) {
				if (chatData.getClients() != null) {
					for (NodeData cd : chatData.getClients()) {
						if (chatClient.listModel.contains(cd)) continue;
						chatClient.listModel.addElement(cd);
					}

					for (Object cd : chatClient.listModel.toArray() ) {
						if (chatData.getClients().contains(cd)) continue;
						chatClient.listModel.removeElement(cd);
					}
					
				} else {
					chatClient.showMessageInBox("\n" + chatData.getMessage());
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			//disconnected
			chatClient.disconnect();
			//e.printStackTrace();
		}
	}
}
