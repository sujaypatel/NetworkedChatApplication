/**
 * ChatClient.java
 * CS 342 Project 4 - Networked Chat Application
 */
package edu.uic.cs342.project5.network.chat.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;
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
 * ChatClient provides the GUI for the chat client and also sets up
 * communication with the server.
 */
public class ChatClient extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	private JTextField MessageField, IPAddress;
	private JLabel MessageLabel, IPAddressLabel, PortLabel, listLabel,
			PortAddress;
	private JButton SendButton, SendToSelected, ConnectButton;
	private JPanel Panel1, Panel2, listPanel;
	private JTextArea MessageBox;
	private Socket clientSocket;
	private NodeInfo serverInfo;
	private int clientId;
	private boolean ConnectionFlag;

	JList<NodeData> clientListDisp;
	DefaultListModel<NodeData> listModel;

	/**
	 * Constructor sets up the GUI
	 */
	public ChatClient(String hostname) {
		// GUI Items
		SendButton = new JButton("Send");
		SendToSelected = new JButton("Send to selected");
		ConnectButton = new JButton("Connect");
		MessageField = new JTextField();
		// IPAddress = new JTextField(ChatUtils.DEFAULT_IP);
		IPAddress = new JTextField(hostname);
		IPAddress.setEditable(false);
		PortAddress = new JLabel(Integer.toString(ChatUtils.DEFAULT_PORT));
		MessageBox = new JTextArea();
		Panel1 = new JPanel();
		Panel2 = new JPanel();

		// Disable the SEND Button and MessageField
		MessageBox.setEditable(false);
		MessageField.setEditable(false);
		SendButton.setEnabled(false);
		SendToSelected.setEnabled(false);
		// Set to BorderLayout
		Container container = getContentPane();
		container.setLayout(new BorderLayout());

		ConnectionFlag = false;

		// Set the Design - Color and Fonts for JLabel
		MessageLabel = new JLabel(" Type Message Here: ", JLabel.LEFT);
		IPAddressLabel = new JLabel(" IP Address: ", JLabel.LEFT);
		PortLabel = new JLabel(" Port Number: ", JLabel.LEFT);

		PortLabel.setFont(new Font(ChatUtils.FONT_NAME, Font.BOLD,
				ChatUtils.FONT_LARGE));
		IPAddressLabel.setFont(new Font(ChatUtils.FONT_NAME, Font.BOLD,
				ChatUtils.FONT_LARGE));
		MessageLabel.setFont(new Font(ChatUtils.FONT_NAME, Font.BOLD,
				ChatUtils.FONT_MEDIUM));
		SendButton.setFont(new Font(ChatUtils.FONT_NAME, Font.BOLD,
				ChatUtils.FONT_LARGE));
		SendToSelected.setFont(new Font(ChatUtils.FONT_NAME, Font.BOLD,
				ChatUtils.FONT_MEDIUM));
		ConnectButton.setFont(new Font(ChatUtils.FONT_NAME, Font.BOLD,
				ChatUtils.FONT_LARGE));
		IPAddress.setFont(new Font(ChatUtils.FONT_NAME, Font.PLAIN,
				ChatUtils.FONT_MEDIUM));
		PortAddress.setFont(new Font(ChatUtils.FONT_NAME, Font.PLAIN,
				ChatUtils.FONT_MEDIUM));
		MessageField.setFont(new Font(ChatUtils.FONT_NAME, Font.PLAIN,
				ChatUtils.FONT_MEDIUM));
		MessageBox.setFont(new Font(ChatUtils.FONT_NAME, Font.PLAIN,
				ChatUtils.FONT_MEDIUM));

		Color lightYellow = new Color(240, 245, 150);
		Color lightGreen = new Color(180, 245, 170);
		Color lightPink = new Color(255, 170, 250);
		MessageBox.setBackground(lightYellow);
		SendButton.setBackground(lightPink);
		SendToSelected.setBackground(lightPink);
		ConnectButton.setBackground(lightPink);
		IPAddress.setBackground(lightGreen);
		PortAddress.setBackground(getBackground());

		listModel = new DefaultListModel<NodeData>();
		clientListDisp = new JList<NodeData>(listModel);
		clientListDisp
				.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		clientListDisp.setSelectedIndex(0);
		clientListDisp.setVisibleRowCount(-1);
		JScrollPane listScrollPane = new JScrollPane(clientListDisp);
		Dimension d = clientListDisp.getPreferredSize();
		d.width = 110;
		listScrollPane.setPreferredSize(d);
		listLabel = new JLabel("Connected Clients:");
		listPanel = new JPanel(new GridLayout(2, 0));
		listPanel.add(listLabel);
		listLabel.setFont(new Font(ChatUtils.FONT_NAME, Font.BOLD,
				ChatUtils.FONT_SMALL));
		listPanel.add(listScrollPane);

		add(new JScrollPane(MessageBox), BorderLayout.CENTER);
		Panel1.setLayout(new GridLayout(0, 4));
		Panel2.setLayout(new GridLayout(0, 5));
		container.add(Panel1, BorderLayout.SOUTH);
		container.add(Panel2, BorderLayout.NORTH);
		container.add(listPanel, BorderLayout.EAST);
		Panel1.add(MessageLabel);
		Panel1.add(MessageField);
		Panel1.add(SendButton);
		Panel1.add(SendToSelected);
		Panel2.add(IPAddressLabel);
		Panel2.add(IPAddress);
		Panel2.add(PortLabel);
		Panel2.add(PortAddress);
		// Panel2.add(ConnectButton);

		MessageField.addActionListener(this);
		SendButton.addActionListener(this);
		SendToSelected.addActionListener(this);
		// ConnectButton.addActionListener(this);

		setTitle(ChatUtils.CLIENT_TITLE);
		setSize(555, 400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);

		// start the chat client connection right away
		try {
			connection();
		} catch (ClassNotFoundException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	/**
	 * Attempts to start a connection with the server.
	 * 
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void connection() throws IOException, ClassNotFoundException {
		if (!ConnectionFlag) {
			String ServerName = null;
			int ServerPort = -1;
			showMessageInBox("Trying to Connect...\n");
			try {
				ServerName = this.IPAddress.getText();
				ServerPort = Integer.parseInt(PortAddress.getText());
				clientSocket = new Socket();
				clientSocket.connect(new InetSocketAddress(ServerName,
						ServerPort), ChatUtils.TIMEOUT);

				showMessageInBox("Connected to: "
						+ clientSocket.getInetAddress().getHostName());

				ObjectOutputStream writer = new ObjectOutputStream(
						clientSocket.getOutputStream());
				ObjectInputStream reader = new ObjectInputStream(
						clientSocket.getInputStream());
				serverInfo = new NodeInfo(0, writer, reader);
				clientId = reader.readInt();
				setTitle(ChatUtils.CLIENT_TITLE + " - " + ChatUtils.CLIENT_NAME
						+ clientId);
				showMessageInBox("\nGot Input/Output Stream\n");
				showEditable(true);
				// ConnectButton.setText("Disconnect");
				ConnectionFlag = true;

				// Connection with Server
				Thread t = new Thread(new ServerHandler(serverInfo, this));
				t.start();
			} catch (EOFException e) {
				showMessageInBox("\nClient Terminated Connection");
			} catch (IOException e) {
				showMessageInBox("Not Input/Output Connection\n");
				// System.exit(-1);
			}
		} else {
			// disconnect();
			showMessageInBox("You are already connected to a server.\n");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ConnectButton) {
			try {
				connection();
			} catch (ClassNotFoundException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		// Send Message when pressed Send Button or Enter
		if (e.getSource() == SendButton || e.getSource() == MessageField
				|| e.getSource() == SendToSelected) {
			String UserInput;
			UserInput = MessageField.getText();
			MessageField.setText("");
			try {
				List<NodeData> recipients = null;

				if (e.getSource() == SendToSelected
						&& clientListDisp.getSelectedIndices().length > 0) {
					int[] selectedIndices = clientListDisp.getSelectedIndices();
					recipients = new LinkedList<NodeData>();
					for (int i = selectedIndices.length - 1; i >= 0; i--) {
						recipients.add(listModel
								.getElementAt(selectedIndices[i]));
					}
				}
				serverInfo.getObjectOutputStream().writeObject(
						new ChatData(recipients, UserInput));
				serverInfo.getObjectOutputStream().flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Displays message in chat output
	 * 
	 * @param display
	 *            message
	 */
	void showMessageInBox(final String display) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MessageBox.append(display);
			}
		});
	}

	/**
	 * Controls state of buttons and text field
	 * 
	 * @param edit
	 *            is editable
	 */
	private void showEditable(final boolean edit) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MessageField.setEditable(edit);
				SendButton.setEnabled(edit);
				SendToSelected.setEnabled(edit);
			}
		});
	}

	/**
	 * Disconnects from server
	 */
	public void disconnect() {
		if (!ConnectionFlag)
			return;
		try {
			showMessageInBox("\nEnd of Connection\n\n");
			serverInfo.getObjectOutputStream().close();
			serverInfo.getObjectInputStream().close();
			clientSocket.close();
			showEditable(false);
			ConnectionFlag = false;
			ConnectButton.setText("Connect");
			((DefaultListModel<NodeData>) clientListDisp.getModel())
					.removeAllElements();
			setTitle(ChatUtils.CLIENT_TITLE);
		} catch (IOException e) {
			showMessageInBox("Error in closing down Socket");
		}
	}
}