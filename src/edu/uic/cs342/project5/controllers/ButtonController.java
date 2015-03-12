package edu.uic.cs342.project5.controllers;

/**
 * ButtonController.java
 * CS 342 Project 5 - Networked Card Game
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import edu.uic.cs342.project5.models.CardPileModel;
import edu.uic.cs342.project5.models.RummyModel.Phase;
import edu.uic.cs342.project5.views.BoardView;

/**
 * Provides a controller for the buttons on top of the board view.
 */
public class ButtonController implements ActionListener {
	private BoardView boardView;
	private ButtonCommand ButtonCommand;
	
	/**
	 * Possible menu commands
	 */
	public enum ButtonCommand {
		MELDING,
		LAYING,
		SKIP,
		DISCARD,
		DRAW_FROM_DECK,
		DRAW_FROM_DISCARD
	}
	
	/**
	 * Constructor
	 * 
	 * @param boardView view the controller is associated with
	 * @param ButtonCommand associated with button command
	 */
	public ButtonController(BoardView boardView, ButtonCommand ButtonCommand) {
		this.boardView = boardView;
		this.ButtonCommand = ButtonCommand;
	}

	/**
	 *  Handler for clicking on button
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (!isPlayerTurn() || boardView.getRummyModel().isGameOver()) return;
		
		switch (ButtonCommand){
		case DRAW_FROM_DECK:
			if (isPhase(Phase.DRAW)) drawCard(boardView.getRummyModel().getDeck());
			break;
		case DRAW_FROM_DISCARD:
			if (isPhase(Phase.DRAW)) drawCard(boardView.getRummyModel().getDiscardPile());
			break;
		case MELDING:
			if (isPhase(Phase.MELDING)) meldingCard(); 
			break;
		case LAYING:
			if (isPhase(Phase.LAYING_OFF)) layingOff();
			break;
		case DISCARD:
			if (isPhase(Phase.DISCARDING)) discardCard();
			break;
		case SKIP:
			skipPhase();
			break;
		default:
			break;
		}
	}

	private void drawCard(CardPileModel source) {
		if (boardView.getRummyModel().drawCard(boardView.getPlayerId(), source)) {
			int cardIndex = boardView.getRummyModel().getPlayers().
					get(boardView.getPlayerId()).getCardHand().size(); 
			boardView.HandModel.addElement(
					boardView.getRummyModel().getPlayers().get(
							boardView.getPlayerId()).getCardHand().getCard(cardIndex-1));
			boardView.updatePhaseLabel();
			boardView.discardCardView.setCard(boardView.getRummyModel().getDiscardPile().getCard(0));
			sendRummyModel();
		}
	}

	public void meldingCard() {
		CardPileModel newMelding = new CardPileModel();
		int[] selectedIndices = boardView.PlayerHandArea.getSelectedIndices();
		for (int i = selectedIndices.length - 1; i >= 0; i--) {
			newMelding.getCardPile().add(boardView.HandModel.getElementAt(selectedIndices[i]));
		}
		if(boardView.getRummyModel().meldCards(boardView.getPlayerId(), newMelding)){
			for(int i=0; i< newMelding.size(); i++) {
				boardView.HandModel.removeElement(newMelding.getCard(i));
			}
			boardView.MeldModel.addElement(newMelding);
			sendRummyModel();
		}
		else {
			JOptionPane.showMessageDialog(boardView, "This is not a valid meld.",
					"Invalid Meld", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void layingOff() {
		int[] selectedIndices = boardView.MeldsArea.getSelectedIndices();
		if (selectedIndices.length > 1 || selectedIndices.length==0) {
			JOptionPane.showMessageDialog(boardView, "You may only lay off to one meld.",
					"Invalid Meld", JOptionPane.ERROR_MESSAGE);
			return;
		} 
		
		int meldId = boardView.MeldsArea.getSelectedIndex();
		CardPileModel newLaying = new CardPileModel();
		selectedIndices = boardView.PlayerHandArea.getSelectedIndices();
		
		for (int i = selectedIndices.length - 1; i >= 0; i--) {
			newLaying.getCardPile().add(boardView.HandModel.getElementAt(selectedIndices[i]));
		}
		
		if (boardView.getRummyModel().layOffCards(boardView.getPlayerId(), meldId, newLaying)) {
			for(int i=0; i< newLaying.size(); i++) {
				boardView.HandModel.removeElement(newLaying.getCard(i));
			}
			boardView.MeldModel.removeElement(boardView.MeldModel.get(meldId));
			boardView.MeldModel.addElement(boardView.getRummyModel().getMeldPiles().get(meldId));
			sendRummyModel();
		} else {
			JOptionPane.showMessageDialog(boardView, "This is not a valid Lay Off",
					"Invalid Lay Off", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public void discardCard() {
		int[] selectedIndices = boardView.PlayerHandArea.getSelectedIndices();
		if (selectedIndices.length > 1 || selectedIndices.length==0) {
			JOptionPane.showMessageDialog(boardView, "You may only discard one card.");
		} 
		else {
			CardPileModel newDiscard = new CardPileModel();
			newDiscard.getCardPile().add(boardView.HandModel.getElementAt(selectedIndices[0]));
			if (boardView.getRummyModel().discardCard(boardView.getPlayerId(), newDiscard)) {
				boardView.HandModel.removeElement(newDiscard.getCard(0));
				boardView.discardCardView.setCard(newDiscard.getCard(0));
				boardView.updatePhaseLabel();
				boardView.PlayerTurnLabel.setText("Current Turn : Player # "
						+ (boardView.getRummyModel().getCurrentPlayerId()+1));
				sendRummyModel();
			}
		}
	}
	
	private void sendRummyModel() {
		try {
			boardView.getGameClient().getServerInfo().getObjectOutputStream()
			.writeObject(boardView.getRummyModel());
		} catch (IOException e) { e.printStackTrace(); }
	}

	private void skipPhase() {
		if(boardView.getRummyModel().skipPhase(boardView.getPlayerId())){
			boardView.updatePhaseLabel();
			sendRummyModel();
		}
		else {
			JOptionPane.showMessageDialog(boardView, "You cannot skip the " + 
					boardView.getRummyModel().getCurrentPhase().toString().
					replace("_", " ") + " phase!", "Skip Not Allowed", 
					JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private boolean isPlayerTurn() {
		if (boardView.getPlayerId() != boardView.getRummyModel().getCurrentPlayerId()) {
			JOptionPane.showMessageDialog(boardView, "It is not your turn!", "Wrong Turn",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
	private boolean isPhase(Phase phase) {
		if (boardView.getRummyModel().getCurrentPhase() != phase) {			
			JOptionPane.showMessageDialog(boardView, "It is not the " + 
					phase.toString().replace("_", " ") + " phase!", "Wrong Phase",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
}
