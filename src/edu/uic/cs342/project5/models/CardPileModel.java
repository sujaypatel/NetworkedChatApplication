/**
 * CardPile.java
 * CS 342 Project 1 - Poker Game
 */
package edu.uic.cs342.project5.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * This class is create, store, and manipulate a collection of cards.
 * It also provides sorting and rule checking methods for poker.
 */
public class CardPileModel implements Serializable 
{	
	private static final long serialVersionUID = -4677154265862448845L;
	
	private List<CardModel> cardPile = new ArrayList<CardModel>();	//internal collection to store cards
	protected int index;
	
	/**
	 * Copy constructor
	 * @param sourcePile
	 */
	public CardPileModel(CardPileModel sourcePile) {
		for(CardModel cm : sourcePile.cardPile) {
			cardPile.add(new CardModel(cm));
		}
	}
	
	/**
	 * Default constructor
	 */
	public CardPileModel() {
		super();
	}

	/**
	 * Adds a full deck of cards to the card pile and shuffles the cards.
	 */
	public void addDeck() {
		for (CardModel.Suit suit: CardModel.Suit.values()) {		//for each suit
			for (CardModel.Rank rank: CardModel.Rank.values()) {	//for each rank
				cardPile.add(new CardModel(suit, rank));
			}
		}
		this.shuffle();
	}
	
	/**
	 * Shuffles the card pile.
	 */
	private void shuffle()
	{
		long seed = System.nanoTime();
		Collections.shuffle(cardPile, new Random(seed));
		index = 0;
	}
		
	/**
	 * Draws a card from the pile.
	 */
	public boolean draw(CardPileModel sourcePile)
	{
		if (sourcePile.size() > 0) {
			CardModel cardDraw = sourcePile.cardPile.get(0);
			this.cardPile.add(cardDraw);
			sourcePile.cardPile.remove(0);
			return true;
		} else {
			System.err.println("No more cards left to draw!");
			return false;
		}
	}
	
	/**
	 * Removes a card at a given index from the CardPile
	 * @param cardIndex Index of the card to be removed
	 */
	public void discard(int cardIndex, CardPileModel discardPile) {
		discardPile.cardPile.add(this.cardPile.remove(cardIndex));
	}
	
	/**
	 * Returns the size of the given CardPile
	 * @return size of the CardPile 
	 */
	public int size() {
		return cardPile.size();
	}
	
	/**
	 * Sorts the card pile.
	 */
	public void sort() {
		Collections.sort(cardPile, new CardCompareDesc());
		this.countMatches();
		Collections.sort(cardPile, new CardMatchCompareDesc());	
	}
	
	/**
	 * Counts the number of matches for each card in the card pile
	 * and stores result in each card. 
	 */
	private void countMatches() {
		for (int i=0; i< this.size(); i++) 		//ensure match count is reset
			this.getCard(i).setMatchCount(0);	//before counting
		
		for (int i=0; i < this.size(); i++) {
			for (int j=(i+1); j < this.size(); j++) {
				if (this.getCard(i).getCardRank() == this.getCard(j).getCardRank()) {
					this.getCard(i).setMatchCount(this.getCard(i).getMatchCount()+1);
					this.getCard(j).setMatchCount(this.getCard(j).getMatchCount()+1);
				}
			}
		}
	}
	
	/**
	 * CardCompareDesc class provides the comparator for sorting a card pile
	 * in descending order.
	 */
	private class CardCompareDesc implements Comparator<CardModel> {
		/**
		 * Compares two cards
		 */
	    @Override
	     public int compare(CardModel card1, CardModel card2) {
	        return card2.compareTo(card1);
	    }
	}
	
	
	/**
	 * CardCompareDesc class provides the comparator for sorting a card pile
	 * in descending order.
	 */
	private class CardMatchCompareDesc implements Comparator<CardModel> {
		/**
		 * Compares two cards
		 */
	    @Override
	     public int compare(CardModel card1, CardModel card2) {
	        return card2.getMatchCount() - card1.getMatchCount();
	    }
	}

	/**
	 * Returns a card at a given index
	 * @param i Index of the card
	 * @return Card object that points to the card requested
	 */
	public CardModel getCard(int i) {
		if (i<cardPile.size() && i>=0)
			return cardPile.get(i);
		else {
			return null;
		}
	}
	
	/**
	 * Checks if a given card rank is present in the card pile
	 * @param cardRank	The card rank
	 * @return true if the card rank is in the car ile
	 */
	public boolean contains(CardModel.Rank cardRank) {
		for (int i=0; i<this.size(); i++)
			if (this.getCard(i).getCardRank() == cardRank)
				return true;
		return false;
	}
	
	/**
	 * Checks every card in the card pile to see if it is a sequence
	 * This uses the overload by specifying default values for start and stop.
	 * 
	 * @return true if the card pile is a sequence
	 */
	public boolean isSequence() {
		int loopStart = 0;
		int loopEnd = size() - 1;		
		return isSequence(loopStart, loopEnd);
	}
	
	/**
	 * Checks every card in the card pile to see if it is a sequence
	 * 
	 * @param startIndex Starting index for checking sequence
	 * @param stopIndex Ending index for checking sequence
	 * @return true if the card pile is a sequence
	 */
	public boolean isSequence(int startIndex, int stopIndex) {
		for (int i=startIndex; i<=stopIndex - 1; i++) {
			if (getCard(i).getCardRank().ordinal() !=(getCard(i+1).getCardRank().ordinal()+1))
				return false;
		}
		return true;
	}
	
	/**
	 * Checks every card in the card pile to see if it has the same suit
	 * @return true Card pile is same suit
	 */
	public boolean isSameSuit() {
		for (int i=0; i<size() - 1; i++) {
			if (getCard(i).getCardSuit() != getCard(i+1).getCardSuit())
				return false;
		}
		return true;
	}

	/**
	 * @return the cardPile
	 */
	public List<CardModel> getCardPile() {
		return cardPile;
	}

	/**
	 * Checks for cards in sequence and same suit
	 * @return if card pile is a run
	 */
	public boolean isRun() {
		sort();
		return (isSameSuit() && isSequence() && size()>=3 );
	}

	/**
	 * Checks for cards being the same rank
	 * @return
	 */
	public boolean isSet() {
		for (int i=0; i<size() - 1; i++) {
			if (getCard(i).getCardRank() != getCard(i+1).getCardRank())
				return false;
		}
		return size()>=3;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return cardPile.toString().replace(",", "").replace("[", "").replace("]", "");
	}
}