/**
 * ConnectionHandler.java
 * CS 342 Project 4 - Networked Chat Application
 */
package edu.uic.cs342.project5.network.chat.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import edu.uic.cs342.project5.network.chat.common.ChatData;
import edu.uic.cs342.project5.network.chat.common.ChatUtils;
import edu.uic.cs342.project5.network.chat.common.NodeInfo;

/**
 * This class keeps listening for new connections and handles
 * the new client connections.
 */
public class ConnectionHandler implements Runnable {
	private ChatServer chatServer;

	/**
	 * Constructor for ConnectionHandler
	 * @param chatServer The server that instantiated this class
	 */
	public ConnectionHandler(ChatServer chatServer) {
		this.chatServer = chatServer;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub

		try {
			chatServer.serverSock = new ServerSocket(Integer.parseInt(chatServer.PortNumber.getText()));
			chatServer.showMessageInBox(ChatUtils.getServerName()
					+ " Chat Server started.");
			while (true) {
				Socket clientSocket = chatServer.serverSock.accept();
				chatServer.showMessageInBox(ChatUtils.getServerName()
						+ " Got a Connection from "
						+ ChatUtils.getName(chatServer.clientId));

				ObjectOutputStream writer = new ObjectOutputStream(
						clientSocket.getOutputStream());
				ObjectInputStream reader = new ObjectInputStream(
						clientSocket.getInputStream());
				writer.writeInt(chatServer.clientId);

				NodeInfo nextClient = new NodeInfo(chatServer.clientId++,
						writer, reader);
				chatServer.clients.add(nextClient);
				chatServer.listModel.addElement(nextClient);

				chatServer.broadcast(new ChatData(ChatUtils
						.ClientDataNoStreams(chatServer.clients)));
				Thread t = new Thread(new ClientHandler(nextClient, chatServer));
				t.start();
			}

		} catch (Exception ex) {
			//ex.printStackTrace();			
			chatServer.disconnect();
			chatServer.showMessageInBox(ChatUtils.getServerName()
					+ " Chat Server stopped.");
		}
	}

}
