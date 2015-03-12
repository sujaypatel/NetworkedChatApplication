/**
 * PlayerModel.java
 * CS 342 - Project 5
 */

package edu.uic.cs342.project5.models;

import java.io.Serializable;

/**
 * Player class defines the attributes of a player, including the name, cardHand and the Rank of the hand
 */
public class PlayerModel implements Serializable {	
	private static final long serialVersionUID = -8590789273827275371L;
	
	private CardPileModel cardHand = new CardPileModel();
	private int id;
		
		/**
	 * Sets the name of player upon creation of instance
	 * @param name
	 */
	public PlayerModel(int id) {
		this.id = id;
	}
	
	/**
	 * The player's hand printed neatly as a string with index values
	 */
	public String toString() {
		String outputString = new String();
		for (int i=0; i<cardHand.size(); i++) 
			outputString += "[" + (i+1) +"] " + cardHand.getCard(i).toString() + "  ";
		return outputString;
	}
	
	/**
	 * Counts all card's points from player's hand
	 * @return Point value
	 */
	public int getHandPoints() {
		int sum = 0;
		for (int i=0; i<cardHand.size(); i++)
			sum += cardHand.getCard(i).getPoints();
		return sum;
	}
	
	/**
	 * Discards a card from the Deck it is called upon and draws a card
	 * @param cardIndex Index of the card to be removed
	 * @param sourceDeck Deck from which to draw a card
	 * @param discardPile Deck to which cards will be discarded to
	 * @param amount The amount of cards to discard and draw
	 */
	public void discardAndDrawCard(int cardIndex, CardPileModel sourceDeck, CardPileModel discardPile, int amount) {
		for (int i=0; i<amount; i++)
			cardHand.discard(cardIndex, discardPile);
		for (int i=0; i<amount; i++)
			cardHand.draw(sourceDeck);
	}

	/**
	 * @return the cardHand
	 */
	public CardPileModel getCardHand() {
		return cardHand;
	}
	
	/**
	 * Wrapper function to sort the given cardHand
	 */
	public void sort()
	{
		cardHand.sort();
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return "Player #" + (id+1);
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
}

