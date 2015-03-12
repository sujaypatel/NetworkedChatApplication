package edu.uic.cs342.project5.network.game.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.uic.cs342.project5.network.chat.common.NodeData;
import edu.uic.cs342.project5.views.BoardView;

public class GameServer {	
	private boolean isRunning;
	int clientId;
	ServerSocket serverSock;
	public List<NodeData> clients;
	private int playerCount;
	BoardView boardView;
	
	public GameServer(int playerCount, BoardView boardView) {
		clientId = 0;
		clients = new ArrayList<NodeData>();
		this.playerCount = playerCount;
		this.boardView = boardView;
		isRunning = false;
		this.go();
	}

	/**
	 * Starts the server
	 */
	public void go() {
		if (isRunning) return;
		isRunning = true;
		Thread t = new Thread(new ConnectionHandler(this));
		t.start();
	}

	/**
	 * Sends data to clients. Data is sent to all clients if chatData has
	 * recipients set to null. Data is sent to selected clients if chatData has
	 * recipients set to a client list
	 * 
	 * @param sendData
	 *            The data to broadcast
	 */
	public void broadcast(Object sendData) {
		if (clients.isEmpty())
			return;

		Iterator<NodeData> it;
		it = clients.iterator();

		while (it.hasNext()) {
			try {
				NodeData client = it.next();
				ObjectOutputStream writer = clients.get(clients.indexOf(client)).getObjectOutputStream();
				writer.writeObject(sendData);
				writer.flush();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	
	/**
	 * Shuts down the server gracefully
	 */
	public void disconnect() {
		if (!isRunning) return;
		isRunning = false;
		try {
			disconnectAllClients();
			serverSock.close();
		} catch (IOException e) { } 
	}

	/**
	 * Closes connection with all clients
	 */
	private void disconnectAllClients() {
		if (clients.isEmpty())
			return;

		Iterator<NodeData> it;
		it = clients.iterator();
		
		while (it.hasNext()) {
			try {
				NodeData client = it.next();
				client.getObjectOutputStream().close();
				client.getObjectInputStream().close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * @return the playerCount
	 */
	public int getPlayerCount() {
		return playerCount;
	}

}
