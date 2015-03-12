/**
 * ConnectionHandler.java
 * CS 342 Project 4 - Networked Chat Application
 */

package edu.uic.cs342.project5.network.game.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import edu.uic.cs342.project5.network.chat.common.NodeInfo;
import edu.uic.cs342.project5.network.game.common.GameUtils;

/**
 * This class keeps listening for new connections and handles
 * the new client connections.
 */
public class ConnectionHandler implements Runnable {
	private GameServer gameServer;

	/**
	 * Constructor for ConnectionHandler
	 * @param gameServer The server that instantiated this class
	 */
	public ConnectionHandler(GameServer gameServer) {
		this.gameServer = gameServer;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub

		try {
			gameServer.serverSock = new ServerSocket(GameUtils.DEFAULT_PORT);

			while (true) {
				Socket clientSocket = gameServer.serverSock.accept();
				ObjectOutputStream writer = new ObjectOutputStream(
						clientSocket.getOutputStream());
				ObjectInputStream reader = new ObjectInputStream(
						clientSocket.getInputStream());
				writer.writeObject(new Integer(gameServer.clientId));
				writer.writeObject(new Integer(gameServer.getPlayerCount()));
				writer.writeObject(gameServer.boardView.getRummyModel());

				NodeInfo nextClient = new NodeInfo(gameServer.clientId++,
						writer, reader);
				gameServer.clients.add(nextClient);

				Thread t = new Thread(new ClientHandler(nextClient, gameServer));
				t.start();
			}

		} catch (Exception ex) {
			//ex.printStackTrace();	
			//Problem with connection, end game and disconnect all clients
			gameServer.disconnect();
		}
	}

}
