package edu.uic.cs342.project5.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class RummyModel implements Serializable {
	private static final long serialVersionUID = -5470762780103826980L;

	/**
	 * Four phases of gameplay as enum
	 */
	public static enum Phase {
		DRAW, MELDING, LAYING_OFF, DISCARDING;
		
    	/**
    	 * Increments Phase enum
    	 * @return next enum
    	 */
    	private Phase next() {
            if (this==DISCARDING) return DRAW;	//wrap around
            return values()[ordinal() + 1];
        }
		
	}
	
	public static final int[][] HAND_SIZE = { { 2, 4, 6 }, { 10, 7, 6 } };
	private int handSize;
	private CardPileModel deck = new CardPileModel();
	private CardPileModel discardPile = new CardPileModel();
	private List<CardPileModel> meldPiles = new ArrayList<CardPileModel>();
	private List<PlayerModel> players = new ArrayList<PlayerModel>();
	
	private Phase currentPhase;
	private int currentPlayerId;
	private boolean isGameOver;
	private int winnerId;
	
	/**
	 * Constructor setups all objects needed for game
	 * @param numOfPlayers Number of players
	 */
	public RummyModel(int numOfPlayers) {
		//set hand size based of number of players in game
		for (int i=0; i<HAND_SIZE[0].length; i++) {
			if (numOfPlayers <= HAND_SIZE[0][i]) {
				handSize = HAND_SIZE[1][i];
				break;
			}
		}
		
		for (int i=0; i<numOfPlayers; i++)
			players.add(new PlayerModel(i));	
		
		deck.addDeck();
		dealCards();
		
		for (int i=0; i<numOfPlayers; i++)
			players.get(i).getCardHand().sort();
		
		currentPlayerId = 0;
		currentPhase = Phase.DRAW;
	}

	/**
	 * Draw a card if it is the draw phase and the player's turn
	 * @param playerId Player that is drawing
	 * @return valid card draw
	 */
	public boolean drawCard(int playerId, CardPileModel source) {
		if (playerId == currentPlayerId && currentPhase == Phase.DRAW) {
			if (players.get(playerId).getCardHand().draw(source)) {
				nextPhase();
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks to see if card pile is a valid meld
	 * @param playerId
	 * @param cards
	 * @return valid meld
	 */
	public boolean meldCards(int playerId, CardPileModel cards) {
		if (playerId == currentPlayerId && currentPhase == Phase.MELDING) {
			if (cards.isRun() || cards.isSet()) {
				meldPiles.add(cards);
				for (int i=0; i<cards.size(); i++) {
					List<CardModel> playerCards = players.get(playerId).getCardHand().getCardPile();
					playerCards.remove(playerCards.indexOf(cards.getCard(i)));
				}
				checkWinner(playerId);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Attempts to create a new meld
	 * @param playerId Player attempting meld
	 * @param meldId Meld to lay cards on
	 * @param cards Cards to lay off
	 * @return if cards laid off successfully
	 */
	public boolean layOffCards(int playerId, int meldId, CardPileModel cards) {
		if (playerId == currentPlayerId && currentPhase == Phase.LAYING_OFF) {
			CardPileModel selectedMeld = meldPiles.get(meldId);
			CardPileModel newMeld = new CardPileModel(selectedMeld);
			for (CardModel cm : cards.getCardPile())
				newMeld.getCardPile().add(new CardModel(cm));			
			if (newMeld.isRun() || newMeld.isSet()) {
				meldPiles.remove(selectedMeld);
				meldPiles.add(meldId, newMeld);
				for (int i=0; i<cards.size(); i++) {
					List<CardModel> playerCards = players.get(playerId).getCardHand().getCardPile();
					playerCards.remove(playerCards.indexOf(cards.getCard(i)));
				}
				checkWinner(playerId);
				return true;
			}
		}
		return false;	
	}
	
	/**
	 * Discards a card from hand to discard pile
	 * @param playerId player discarding a card
	 * @param cards card being discarded
	 * @return if card was successfully discarded
	 */
	public boolean discardCard(int playerId, CardPileModel cards) {
		if (playerId == currentPlayerId && currentPhase == Phase.DISCARDING && cards.getCardPile().size()==1) {
			List<CardModel> playerCards = players.get(playerId).getCardHand().getCardPile();
			discardPile.getCardPile().add(0, cards.getCard(0));
			playerCards.remove(cards.getCard(0));
			checkWinner(playerId);
			nextPhase();
			nextTurn();
			return true;
		}
		return false;
	}
	
	/**
	 * Check winning condition for player
	 * @param playerId
	 */
	private void checkWinner(int playerId) {
		if (players.get(playerId).getCardHand().size() == 0 && !isGameOver) {
			isGameOver = true;
			winnerId = playerId;
		}
	}

	/**
	 * Deal cards to all players, one at a time
	 */
	private void dealCards() {
		for (int i=0; i<handSize; i++) {
			for (int j=0; j<players.size(); j++) {
				players.get(j).getCardHand().draw(deck);
			}
		}
	}

	/**
	 * @return the isGameOver
	 */
	public boolean isGameOver() {
		return isGameOver;
	}

	/**
	 * @return the winnerId
	 */
	public int getWinnerId() {
		return winnerId;
	}

	/**
	 * Set next phase
	 * @return 
	 */
	private void nextPhase() {
		currentPhase = currentPhase.next();
	}
	
	public boolean skipPhase(int playerId) {
		if(playerId == currentPlayerId && 
				currentPhase!=Phase.DRAW && currentPhase!=Phase.DISCARDING) {
			this.nextPhase();
			return true;
		}
		else
			return false;
	}
	
	/**
	 * Set next turn
	 */
	public void nextTurn() {
		currentPlayerId++;
		if (currentPlayerId == players.size())
			currentPlayerId = 0;
	}
	
	/**
	 * @return the currentPhase
	 */
	public Phase getCurrentPhase() {
		return currentPhase;
	}

	/**
	 * @param currentPhase the currentPhase to set
	 */
	public void setCurrentPhase(Phase currentPhase) {
		this.currentPhase = currentPhase;
	}

	/**
	 * @return the currentTurn
	 */
	public int getCurrentPlayerId() {
		return currentPlayerId;
	}

	/**
	 * @param currentPlayer the currentTurn to set
	 */
	public void setCurrentPlayerId(int currentPlayerId) {
		this.currentPlayerId = currentPlayerId;
	}

	/**
	 * @return the handSize
	 */
	public int getHandSize() {
		return handSize;
	}

	/**
	 * @return the deck
	 */
	public CardPileModel getDeck() {
		return deck;
	}

	/**
	 * @return the discardPile
	 */
	public CardPileModel getDiscardPile() {
		return discardPile;
	}

	/**
	 * @return the meldPiles
	 */
	public List<CardPileModel> getMeldPiles() {
		return meldPiles;
	}

	/**
	 * @return the players
	 */
	public List<PlayerModel> getPlayers() {
		return players;
	}
}
