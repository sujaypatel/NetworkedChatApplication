/**
 * ChatUtils.java
 * CS 342 Project 4 - Networked Chat Application
 */
package edu.uic.cs342.project5.network.chat.common;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;

/**
 * Static class that provides utility functions and constants
 * for the client and server classes
 */
public class ChatUtils {
	private static final String[] prefix = { "<", "[" };	
	private static final String[] suffix = {">", " whispers]:"};
	
	public static final String CLIENT_NAME = "Player #";
	public static final String SERVER_NAME = "SERVER";
	public static final String CLIENT_TITLE = "Chat Client";		
	public static final String SERVER_TITLE = "Chat Server";
	
	public static final String FONT_NAME = "Tahoma";
	public static final int FONT_SMALL = 11;
	public static final int FONT_MEDIUM = 12;
	public static final int FONT_LARGE = 13;

	public static final int TIMEOUT = 1000;
	public static final int DEFAULT_PORT = 12345;
	public static final String DEFAULT_IP = "127.0.0.1";
	
	/**
	 * Creates a string formatted with client name
	 * @param id Client ID
	 * @return Formatted string
	 */
	public static final String getName(int id) {
		return new String(prefix[0] + CLIENT_NAME + id + suffix[0]);
	}
	
	/**
	 * Creates a string formatted with client name
	 * when used to send a private message
	 * @param id Client ID
	 * @return Formatted string
	 */
	public static final String getWhisperName(int id) {
		return new String(prefix[1] + CLIENT_NAME + id + suffix[1]);
	}
	
	/**
	 * Creates a string formatted with Server Name
	 * @return Formatted string
	 */
	public static final String getServerName() {
		return new String(prefix[0] + SERVER_NAME + suffix[0]);
	}
	
	/**
	 * Copies list of nodes without stream information
	 * @param source Source list
	 * @return Copied list
	 */
	public static List<NodeData> ClientDataNoStreams(List<NodeData> source) {
		List<NodeData> newClients = new ArrayList<NodeData>();
		for (NodeData cd : source) {
			newClients.add(new NodeInfo(cd.getId(), null, null));			
		}
		return newClients;
	}
	
	/**
	 * Shallow copies a list to a list model
	 * @param source Source list
	 * @return DefaultListModel
	 */
	public static DefaultListModel<NodeData> ClientDataToListModel(List<NodeData> source) {
		DefaultListModel<NodeData> listModel = new DefaultListModel<NodeData>();
		for (NodeData cd : source) {
			listModel.addElement(cd);			
		}
		return listModel;
	}
}
