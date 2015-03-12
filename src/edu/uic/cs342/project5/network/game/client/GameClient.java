package edu.uic.cs342.project5.network.game.client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import edu.uic.cs342.project5.models.RummyModel;
import edu.uic.cs342.project5.network.chat.common.NodeInfo;
import edu.uic.cs342.project5.network.game.common.GameUtils;
import edu.uic.cs342.project5.views.BoardView;

public class GameClient {
	private int clientId;
	private int playerCount;
	private boolean isConnected;
	private Socket clientSocket;
	private NodeInfo serverInfo;
	private String hostname;
	BoardView boardView;
	
	public GameClient(String hostname, BoardView boardView) throws ClassNotFoundException, IOException {
		this.hostname = hostname;
		this.boardView = boardView;
		isConnected = false;
		connection();
	}
	
	public void connection() throws IOException, ClassNotFoundException {
		if (!isConnected) {
			try {
				isConnected = true;
				clientSocket = new Socket();   
				clientSocket.connect(new InetSocketAddress(hostname, GameUtils.DEFAULT_PORT), GameUtils.TIMEOUT);
				
				ObjectOutputStream writer = new ObjectOutputStream(
						clientSocket.getOutputStream());
				ObjectInputStream reader = new ObjectInputStream(
						clientSocket.getInputStream());
				serverInfo = new NodeInfo(0, writer, reader);
				clientId = (Integer)reader.readObject();
				playerCount = (Integer)reader.readObject();
				boardView.setRummyModel((RummyModel)reader.readObject());

				// Connection with Server
				Thread t = new Thread(new ServerHandler(serverInfo, this));
				t.start();
			} catch (EOFException e) {
				//showMessageInBox("\nClient Terminated Connection");
			}
		}
	}

	/**
	 * @return the clientId
	 */
	public int getClientId() {
		return clientId;
	}

	/**
	 * @return the playerCount
	 */
	public int getPlayerCount() {
		return playerCount;
	}
	
	public void disconnect() {
		try {
			serverInfo.getObjectOutputStream().close();
			serverInfo.getObjectInputStream().close();
			clientSocket.close();
			boardView.setTitle("Rummy Card Network Game - Disconnected");
		} catch (IOException e) {}
	}

	/**
	 * @return the serverInfo
	 */
	public NodeInfo getServerInfo() {
		return serverInfo;
	}
}
