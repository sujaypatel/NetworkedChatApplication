/**
 * ChatServer.java
 * CS 342 Project 4 - Networked Chat Application
 */
package edu.uic.cs342.project5.network.chat.server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import edu.uic.cs342.project5.network.chat.common.ChatData;
import edu.uic.cs342.project5.network.chat.common.ChatUtils;
import edu.uic.cs342.project5.network.chat.common.NodeData;
import edu.uic.cs342.project5.network.chat.common.NodeInfo;

/**
 * ChatServer provides the GUI for the chat server
 * and also setups the connection for the clients
 */
public class ChatServer extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1464970643121756976L;
	private Container container;
	private boolean running;
	private JLabel serverInfo;
	private JLabel portInfo;
	private JPanel serverPanel;
	private JTextArea printChat;
	private JPanel listPanel;
	private JLabel listLabel;
	private JButton ConnectButton;
	private JList<NodeData> clientListDisp;
	private Color lightYellow, lightGreen, lightPink;

	JTextField PortNumber;
	List<NodeData> clients;
	DefaultListModel<NodeData> listModel;
	ServerSocket serverSock;
	int clientId;
	
	/**
	 * Constructor sets up the GUI
	 */
	public ChatServer(String IPAddress) {
		init();
		go();
	}
	
	
	public ChatServer() {
		container = getContentPane();
		running = false;

		portInfo = new JLabel("Port #: ");
		PortNumber = new JTextField(Integer.toString(ChatUtils.DEFAULT_PORT), 5);
		
		try {
			serverInfo = new JLabel("IP #: " + java.net.InetAddress.getLocalHost().getHostAddress() + "   ");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		listModel = new DefaultListModel<NodeData>();
		clientListDisp = new JList<NodeData>(listModel);
		clientListDisp
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		clientListDisp.setSelectedIndex(0);
		clientListDisp.setVisibleRowCount(-1);
		JScrollPane listScrollPane = new JScrollPane(clientListDisp);
		Dimension d = clientListDisp.getPreferredSize();
		d.width = 130;
		listScrollPane.setPreferredSize(d);
		listLabel = new JLabel("Connected Clients:");
		ConnectButton = new JButton();

		serverPanel = new JPanel(new FlowLayout());
		serverPanel.setComponentOrientation(
                ComponentOrientation.LEFT_TO_RIGHT);
		listPanel = new JPanel(new GridLayout(1, 1));
		printChat = new JTextArea();

		lightYellow = new Color(240, 245, 150);
		lightGreen = new Color(180, 245, 170);
		lightPink = new Color(255, 170, 250);
		portInfo.setFont(new Font(ChatUtils.FONT_NAME, Font.BOLD, ChatUtils.FONT_LARGE));
		serverInfo.setFont(new Font(ChatUtils.FONT_NAME, Font.BOLD, ChatUtils.FONT_LARGE));
		listLabel.setFont(new Font(ChatUtils.FONT_NAME, Font.BOLD, ChatUtils.FONT_LARGE));
		printChat.setFont(new Font(ChatUtils.FONT_NAME, Font.PLAIN, ChatUtils.FONT_MEDIUM));
		ConnectButton.setFont(new Font(ChatUtils.FONT_NAME, Font.BOLD, ChatUtils.FONT_MEDIUM));
		PortNumber.setBackground(lightGreen);
		printChat.setBackground(lightYellow);
		ConnectButton.setBackground(lightPink);
		printChat.setEditable(false);
		ConnectButton.addActionListener(this);

		container.setLayout(new BorderLayout());
		container.add(new JScrollPane(printChat), BorderLayout.CENTER);
		container.add(serverPanel, BorderLayout.NORTH);
		container.add(new JLabel("Connected:"), BorderLayout.EAST);
		container.add(listPanel, BorderLayout.EAST);
		serverPanel.add(serverInfo);
		serverPanel.add(portInfo);
		serverPanel.add(PortNumber);
		serverPanel.add(ConnectButton);
		serverPanel.add(listLabel);
		listPanel.add(listScrollPane);
	
		setTitle(ChatUtils.SERVER_TITLE);
		setSize(600, 400);		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		init();
		go();
//		setVisible(true);
	}
	
	/**
	 * Prepares the server
	 */
	private void init() {
		clientId = 1;
		clients = new ArrayList<NodeData>();
		running = false;
		ConnectButton.setText("Start Server");
		((DefaultListModel<NodeData>)clientListDisp.getModel()).removeAllElements();
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (running == false){
			go();
			ConnectButton.setText("Stop Server");
		}
		else {
			disconnect();
		}
	}

	/**
	 * Displays message in server output
	 * @param display The message
	 */
	void showMessageInBox(final String display) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				printChat.append(display + "\n");
			}
		});
	}

	/**
	 * Starts the server
	 */
	public void go() {
		if (running) return;
		running = true;
		Thread t = new Thread(new ConnectionHandler(this));
		t.start();
	}

	/**
	 * Sends data to clients. Data is sent to all clients if chatData has
	 * recipients set to null. Data is sent to selected clients if chatData has
	 * recipients set to a client list
	 * 
	 * @param chatData
	 *            The data to broadcast
	 */
	public void broadcast(ChatData chatData) {
		if (clients.isEmpty())
			return;

		Iterator<NodeData> it;
		if (chatData.getRecipients() == null)
			it = clients.iterator();
		else
			it = chatData.getRecipients().iterator();

		if (chatData.getMessage() != null)
			showMessageInBox(chatData.getMessage());
		else
			showMessageInBox(ChatUtils.getServerName()
					+ " Sending clients list");

		while (it.hasNext()) {
			try {
				NodeData client = it.next();
				ObjectOutputStream writer = clients.get(clients.indexOf(client)).getObjectOutputStream();
				writer.writeObject(chatData);
				writer.flush();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Removes a client from the list and broadcasts the change
	 * @param client
	 */
	public void removeClient(NodeInfo client) {
		if (!running) return;
		int clientIndex = clients.indexOf(client);
		if (clientIndex != -1) clients.remove(clientIndex);
		listModel.remove(listModel.indexOf(client));
		showMessageInBox(ChatUtils.getServerName() + " " + client
				+ " has disconnected.");
		broadcast(new ChatData(ChatUtils.ClientDataNoStreams(clients)));
	}

	/**
	 * Shuts down the server gracefully
	 */
	public void disconnect() {
		if (!running) return;
		running = false;
		try {
			disconnectAllClients();
			serverSock.close();
		} catch (IOException e) { } 
		init();
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
}
