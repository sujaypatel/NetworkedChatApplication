package edu.uic.cs342.project5.controllers;

/**
 * MenuController.java
 * CS 342 Project 5 - Networked Card Game
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

import edu.uic.cs342.project5.models.RummyModel;
import edu.uic.cs342.project5.network.chat.client.ChatClient;
import edu.uic.cs342.project5.network.chat.server.ChatServer;
import edu.uic.cs342.project5.network.game.client.GameClient;
import edu.uic.cs342.project5.network.game.server.GameServer;
import edu.uic.cs342.project5.views.BoardView;

/**
 * Controller for menus. Controls logic of what happens when menu items are clicked
 */
public class MenuController implements ActionListener {
	private MenuCommand menuCommand;
	private BoardView boardView;
	private static ChatClient chatClient;

	public enum MenuCommand {
		TWO_PLAYER,
		THREE_PLAYER,
		FOUR_PLAYER,
		FIVE_PLAYER,
		SIX_PLAYER,
		CLIENT,
		EXIT,
		HELP,
		ABOUT,
		DISCONNECT
	}

	public MenuController(BoardView boardView, MenuCommand menuCommand) {
		this.boardView = boardView;
		this.menuCommand = menuCommand;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (menuCommand) {
		case TWO_PLAYER:
			launchServers(2);
			break;
		case THREE_PLAYER:
			launchServers(3);
			break;
		case FOUR_PLAYER:
			launchServers(4);
			break;
		case FIVE_PLAYER:
			launchServers(5);
			break;
		case SIX_PLAYER:
			launchServers(6);
			break;
		case CLIENT:
			launchClient(getIPAddress());
			break;
		case EXIT:
			System.exit(0);
		case HELP:
			JOptionPane.showMessageDialog(boardView, "HOW TO PLAY RUMMY GAME?\n\n"
					+ "User is presented with a choice to either be a client or server.\n"
					+ "User has to select the number of players they want to play against (2-6)\n"
					+ "In the gameboard, the user select the set of cards to create melds.\n"
					+ "The game board will also indicate whose turn it is.\n"
					+ "There will also be a button to indicate if the user is done with their turn.\n"
					+ "In the chat view, the user can send messages to all users\n"
					+ "or selected users by using control/command + click to select recipients.\n"
					+ "In the server view, you can only view data being passed through.\n" ,
					"Help", JOptionPane.INFORMATION_MESSAGE);
		    break;
		case ABOUT:
 			JOptionPane.showMessageDialog(boardView, "CS 342 Project 5 - Networked Card Game\n"
 					+ "Spring 2014\nUniversity of Illinois at Chicago" + 
 					"\n\nAuthors:\nAnthony Colon\nacolon8@uic.edu\n\nTianniu Lei\n"
 					+ "tlei2@uic.edu\n\nSujay Patel\nspate292@uic.edu\n\n" + 
 					"Sujal Patel\nspate290@uic.edu\n\nAbhinav Saini\nasaini5@uic.edu"
 					,"About", JOptionPane.INFORMATION_MESSAGE);
 			break;
		case DISCONNECT:
			if (boardView.getGameClient() != null) {
				boardView.getGameClient().disconnect();
				chatClient.disconnect();
				JOptionPane.showMessageDialog(boardView, "You have been disconnected from the game\n"
						+ "and the chat room."
	 					,"Disconnected", JOptionPane.ERROR_MESSAGE);
			}
			break;
		default:
			break;
		}

	}

	public String getServerIP(){
		String s = null;
		try {
			s = java.net.InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException ex) {
			ex.printStackTrace();
		}
		return s;
	}
	
	public void launchServers(int playerCount) {
		RummyModel rummyModel = new RummyModel(playerCount);
		boardView.setRummyModel(rummyModel);		
		
		new ChatServer();
		new GameServer(playerCount, boardView);
		launchClient("localhost");
		
		JOptionPane.showMessageDialog(null, "Server IP is: " + getServerIP() +"\n\n" + 
				"You are Player #" + (boardView.getPlayerId()+1), 
				"Server IP Address", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public void launchClient(String ipAddress) {
		GameClient gameClient;
		try {
			gameClient = new GameClient(ipAddress, boardView);
		} catch (ClassNotFoundException | IOException e) {
			JOptionPane.showMessageDialog(boardView, "Can't connect to " + ipAddress,
					"Connection Timed Out", JOptionPane.ERROR_MESSAGE);
			return;
		}

		boardView.addView(gameClient);
		boardView.disableServerClientMenus();
		chatClient = new ChatClient(ipAddress);

		if (!ipAddress.equals("localhost"))
			JOptionPane.showMessageDialog(boardView, "You are Player #" + (boardView.getPlayerId()+1),
					"Player #" + (boardView.getPlayerId()+1), JOptionPane.INFORMATION_MESSAGE);

	}
	
	public String getIPAddress() {
		return (String)JOptionPane.showInputDialog(
				boardView,
				"Please input the IP Address of the server:",
				"Customized Dialog",
				JOptionPane.PLAIN_MESSAGE,
				null,
				null,
				"127.0.0.1");
	}
}
