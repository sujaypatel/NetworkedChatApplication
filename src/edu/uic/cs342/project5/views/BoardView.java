package edu.uic.cs342.project5.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import edu.uic.cs342.project5.controllers.ButtonController;
import edu.uic.cs342.project5.controllers.ButtonController.ButtonCommand;
import edu.uic.cs342.project5.controllers.MenuController;
import edu.uic.cs342.project5.controllers.MenuController.MenuCommand;
import edu.uic.cs342.project5.models.CardModel;
import edu.uic.cs342.project5.models.CardPileModel;
import edu.uic.cs342.project5.models.RummyModel;
import edu.uic.cs342.project5.network.game.client.GameClient;

public class BoardView extends JFrame {
	private static final long serialVersionUID = 1L;
	private JPanel playerPane, meldPane, DisplayPanel, ChatPanel, CardPanel;
	private JButton SendButton, SendToSelected, MeldingButton, LayingButton, 
	SkipButton, DiscardButton; 
	private JLabel MessageLabel,SpaceLabel, SpaceLabel2, SpaceLabel3, ArrowLabel; 
	private JTextField MessageField;
	private int playerId;
	private JMenu Menu1Submenu;
	private JMenuItem ClientMenu;
	private GameClient gameClient;
	private RummyModel rummyModel;
	public JPanel Panel;
	public JList<CardModel> PlayerHandArea;
	public JList<CardPileModel> MeldsArea;
	public JLabel PhaseLabel, PlayerTurnLabel;
	public CardView discardCardView;
	public DefaultListModel<CardModel> HandModel;
	public DefaultListModel<CardPileModel> MeldModel;
	private JTextField[] pointsField;
	private JTextField[] cardsRemainingField;

	public BoardView() {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} catch (Exception e) {}

		// Set up Menu Bar
		JMenuBar bar = new JMenuBar();
		Menu1Submenu = new JMenu("Server");
		JMenu Menu1 = new JMenu("Game");
		Menu1.setMnemonic(KeyEvent.VK_G);

		JMenuItem ExitMenu = new JMenuItem("Exit");
		ExitMenu.addActionListener(new MenuController(
				this,MenuCommand.EXIT));

		JMenu Menu2 = new JMenu("Help");
		Menu2.setMnemonic(KeyEvent.VK_H);

		JMenuItem HelpMenu = new JMenuItem("Help");
		HelpMenu.addActionListener(new MenuController(
				this, MenuCommand.HELP));

		JMenuItem AboutMenu = new JMenuItem("About");
		AboutMenu.addActionListener(new MenuController(
				this,MenuCommand.ABOUT));
		
		JMenuItem ServerSubmenu1 = new JMenuItem("2 Player");
		ServerSubmenu1.addActionListener(new MenuController(
				this,MenuCommand.TWO_PLAYER));
		
		JMenuItem ServerSubmenu2 = new JMenuItem("3 Player");
		ServerSubmenu2.addActionListener(new MenuController(
				this,MenuCommand.THREE_PLAYER));
		
		JMenuItem ServerSubmenu3 = new JMenuItem("4 Player");
		ServerSubmenu3.addActionListener(new MenuController(
				this,MenuCommand.FOUR_PLAYER));
		
		JMenuItem ServerSubmenu4 = new JMenuItem("5 Player");
		ServerSubmenu4.addActionListener(new MenuController(
				this,MenuCommand.FIVE_PLAYER));
		
		JMenuItem ServerSubmenu5 = new JMenuItem("6 Player");
		ServerSubmenu5.addActionListener(new MenuController(
				this,MenuCommand.SIX_PLAYER));
		
		ClientMenu = new JMenuItem("Client");
		ClientMenu.addActionListener(new MenuController(
				this,MenuCommand.CLIENT));
		
		JMenuItem disconnectMenu = new JMenuItem("Disconnect");
		disconnectMenu.addActionListener(new MenuController(
				this,MenuCommand.DISCONNECT));
		
		Menu1Submenu.add(ServerSubmenu1);
		Menu1Submenu.add(ServerSubmenu2);
		Menu1Submenu.add(ServerSubmenu3);
		Menu1Submenu.add(ServerSubmenu4);
		Menu1Submenu.add(ServerSubmenu5);
		Menu1.add(Menu1Submenu);
		Menu1.add(ClientMenu);
		Menu1.addSeparator();
		Menu1.add(disconnectMenu);
		Menu1.addSeparator();
		Menu1.add(ExitMenu);
		Menu2.add(HelpMenu);
		Menu2.addSeparator();
		Menu2.add(AboutMenu);
		bar.add(Menu1);
		bar.add(Menu2);
		setJMenuBar(bar);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Rummy Card Network Game");
		setSize(800, 600);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	public void addView(GameClient gameClient){
		this.playerId = gameClient.getClientId();
		this.gameClient = gameClient;
		int playerCount = rummyModel.getPlayers().size();
		
		setTitle("Rummy Card Network Game - Player #" + (playerId+1));
		Panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		GridBagConstraints c1 = new GridBagConstraints();

		//Player Label
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = .5;
		c.gridy = 0;
		for (int i=0; i<playerCount; i++) {
			JLabel Player = new JLabel(" " + rummyModel.getPlayers()
					.get(i).getName());
			Player.setHorizontalAlignment(JLabel.CENTER);
			Player.setForeground(Color.MAGENTA);
			c.gridx = 0 + i;
			Panel.add(Player, c);
		}
		// Points Label
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 1;
		for (int i=0; i<playerCount; i++) {
			JLabel Pointp1 = new JLabel("Points : " );
			Pointp1.setHorizontalAlignment(JLabel.CENTER);
			Pointp1.setForeground(Color.BLUE);
			c.gridx = 0 + i;
			Panel.add(Pointp1, c);
		}
		// Point Field
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 2;
		pointsField = new JTextField[playerCount];
		for (int i=0; i<playerCount; i++) {
			pointsField[i] = new JTextField();
			pointsField[i].setHorizontalAlignment(JTextField.CENTER);
			pointsField[i].setEditable(false);
			c.gridx = 0 + i;
			Panel.add(pointsField[i], c);
		}
		// Card Left Label
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 3;
		for (int i=0; i<playerCount; i++) {
			JLabel Cardp1 = new JLabel("Cards Remaining : ");
			Cardp1.setHorizontalAlignment(JLabel.CENTER);
			Cardp1.setForeground(Color.BLUE);
			c.gridx = 0 + i;
			Panel.add(Cardp1, c);
		}
		// Card Field
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 4;
		cardsRemainingField = new JTextField[playerCount];
		for (int i=0; i<playerCount; i++) {
			cardsRemainingField[i] = new JTextField();
			cardsRemainingField[i].setHorizontalAlignment(JTextField.CENTER);		
			cardsRemainingField[i].setEditable(false);
			c.gridx = 0 + i;
			Panel.add(cardsRemainingField[i], c);
		}
		
		CardView deckCardView = new CardView();
		discardCardView = new CardView(rummyModel.getDiscardPile().getCard(0), CardView.XLARGE);
		MeldingButton = new JButton("Meld");
		LayingButton = new JButton("Lay Off");
		SkipButton = new JButton("Next Phase");
		DiscardButton = new JButton("Discard");
		
	    deckCardView.addActionListener(new ButtonController(this,ButtonCommand.DRAW_FROM_DECK));
	    discardCardView.addActionListener(new ButtonController(this,ButtonCommand.DRAW_FROM_DISCARD));
		MeldingButton.addActionListener(new ButtonController(this,ButtonCommand.MELDING));
		LayingButton.addActionListener(new ButtonController(this,ButtonCommand.LAYING));
		SkipButton.addActionListener(new ButtonController(this,ButtonCommand.SKIP));		
		DiscardButton.addActionListener(new ButtonController(this,ButtonCommand.DISCARD));

		SpaceLabel = new JLabel(" ");
		SpaceLabel2 = new JLabel(" ");
		SpaceLabel3 = new JLabel(" ");
		ArrowLabel = new JLabel("<<< Choose One >>>");
		PhaseLabel = new JLabel();
		updatePhaseLabel();
		PlayerTurnLabel = new JLabel();
		updateTurnLabel();
		
		PlayerTurnLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
		PhaseLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
		ArrowLabel.setFont(new Font("Times New Roman", Font.BOLD, 14));
		MeldingButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
		LayingButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
		SkipButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
        DiscardButton.setFont(new Font("Times New Roman", Font.BOLD, 14));
        
		// Set up GUI Layout
		Container container = getContentPane();
		container.setLayout(new BorderLayout());

		DisplayPanel = new JPanel(new GridBagLayout());
		ChatPanel = new JPanel(new GridLayout(0, 4));
		CardPanel = new JPanel(new GridLayout(2, 0));
		
		HandModel = new DefaultListModel<CardModel>();
		MeldModel = new DefaultListModel<CardPileModel>();
		MeldsArea = new JList<CardPileModel>(MeldModel);
		PlayerHandArea = new JList<CardModel>(HandModel);
		
		//Add Main Player's Hand to JList Panel 
		playerPane = new JPanel();
		playerPane.add(new JLabel("Your Hand"));
		playerPane.setLayout(new BoxLayout(playerPane, BoxLayout.Y_AXIS));
		JScrollPane playerPaneScroller = new JScrollPane(playerPane);
		playerPaneScroller.setViewportView(PlayerHandArea);
		playerPane.add(playerPaneScroller);
		
		//Add card to JList Panel for Meld card
		meldPane = new JPanel();
		meldPane.add(new JLabel("Meld Piles"));
		meldPane.setLayout(new BoxLayout(meldPane, BoxLayout.Y_AXIS));
		JScrollPane meldPaneScroller = new JScrollPane(meldPane);		
		meldPaneScroller.setViewportView(MeldsArea);
		meldPane.add(meldPaneScroller);

		MessageLabel = new JLabel(" Type Message Here: ", JLabel.LEFT);
		SendButton = new JButton("Send");
		SendToSelected = new JButton("Send to selected");
		MessageField = new JTextField();

		PhaseLabel.setForeground(Color.blue);
		DisplayPanel.setBackground(new Color(200, 240, 120));
		Panel.setBackground(new Color(150, 225, 255));
		CardPanel.setBackground(new Color(200, 240, 120));
		Dimension leftPanelSize = new Dimension(160, 200);
		meldPane.setPreferredSize(leftPanelSize);
		playerPane.setPreferredSize(leftPanelSize);
		
		c1.fill = GridBagConstraints.VERTICAL;
		c1.weightx = 0.0;
		c1.gridx = 0;
		c1.gridy = 1;
		DisplayPanel.add(deckCardView, c1);
		c1.gridx = 1;
		c1.gridy = 2;
		DisplayPanel.add(SpaceLabel, c1);
		c1.gridx = 1;
		c1.gridy = 3;
		DisplayPanel.add(PhaseLabel, c1);
		c1.gridx = 1;
		c1.gridy = 4;
		DisplayPanel.add(PlayerTurnLabel, c1);
		c1.gridx = 1;
		c1.gridy = 5;
		DisplayPanel.add(SpaceLabel2, c1);
		c1.gridx = 0;
		c1.gridy = 6;
		DisplayPanel.add(MeldingButton,c1);
		c1.gridx = 1;
		c1.gridy = 6;
		DisplayPanel.add(LayingButton,c1);
		c1.gridx = 2;
		c1.gridy = 6;
		DisplayPanel.add(DiscardButton,c1);
		c1.gridx = 1;
		c1.gridy = 7;
		DisplayPanel.add(SpaceLabel3,c1);
		c1.gridx = 1;
		c1.gridy = 8;
		DisplayPanel.add(SkipButton,c1);
		c1.gridx = 1;
		c1.gridy = 1;
		DisplayPanel.add(ArrowLabel, c1);
		c1.gridx = 2;
		c1.gridy = 1;
		DisplayPanel.add(discardCardView, c1);

		CardPanel.add(playerPane);
		CardPanel.add(meldPane);
		
		ChatPanel.add(MessageLabel);
		ChatPanel.add(MessageField);
		ChatPanel.add(SendButton);
		ChatPanel.add(SendToSelected);

		container.add(DisplayPanel, BorderLayout.CENTER);
		container.add(Panel, BorderLayout.NORTH);
		container.add(CardPanel, BorderLayout.WEST);
		
		Panel.updateUI();
		this.updateUI();
	}

	/**
	 * @param playerCount the playerCount to set
	 */
	public void setPlayerCount(int playerCount) {
	}

	/**
	 * @return the rummyModel
	 */
	public RummyModel getRummyModel() {
		return rummyModel;
	}

	/**
	 * @param rummyModel the rummyModel to set
	 */
	public void setRummyModel(RummyModel rummyModel) {
		this.rummyModel = rummyModel;
	}

	/**
	 * @return the playerId
	 */
	public int getPlayerId() {
		return playerId;
	}
	
	/**
	 * Setter for phase label
	 */
	public void updatePhaseLabel() {
		PhaseLabel.setText("Phase : " + getRummyModel().
				getCurrentPhase().toString().replace("_", " "));
	}
	
	/**
	 * Setter for phase label
	 */
	public void updateTurnLabel() {
		PlayerTurnLabel.setText("Current Turn : Player # "
				+ (rummyModel.getCurrentPlayerId()+1));
	}
	
	public void disableServerClientMenus(){
		this.Menu1Submenu.setEnabled(false);
		this.ClientMenu.setEnabled(false);
	}
	    
	/**
	 * @return the gameClient
	 */
	public GameClient getGameClient() {
		return gameClient;
	}

	/**
	 * Updates all elements on UI to match RummyModel
	 */
	public void updateUI() {		
		for (int i=0; i<rummyModel.getPlayers().size(); i++) {
			pointsField[i].setText(String.valueOf(rummyModel.getPlayers().get(i).getHandPoints())); 
			cardsRemainingField[i].setText(String.valueOf(rummyModel.getPlayers().get(i).getCardHand().size())); 
		}
		
		discardCardView.setCard(rummyModel.getDiscardPile().getCard(0));
		updateTurnLabel();
		updatePhaseLabel();
		
		MeldModel.removeAllElements();
		for (int i=0; i<rummyModel.getMeldPiles().size(); i++)
			MeldModel.addElement(rummyModel.getMeldPiles().get(i));
		
		HandModel.removeAllElements();
		for (int i=0; i<rummyModel.getPlayers().get(playerId).getCardHand().size(); i++)
			HandModel.addElement(rummyModel.getPlayers().get(playerId).getCardHand().getCard(i));
		
		checkWinners();
	}

	/**
	 * Check for winner and disconnect
	 */
	public void checkWinners() {
		if (rummyModel.isGameOver()) {
			String winMessage = rummyModel.getWinnerId() == playerId ? 
					"You are the winner of this game!" : 
						"You lost the game!\nPlayer #" + (rummyModel.getWinnerId()+1) + " has won the game!\n\n"
								+ "You will be disconnected and the server will shut down"; 
			JOptionPane.showMessageDialog(this, winMessage, "Game Over", JOptionPane.INFORMATION_MESSAGE);
			gameClient.disconnect();
			return;
		}
	}
}