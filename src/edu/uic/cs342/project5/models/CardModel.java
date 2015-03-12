/**
 * CardModel.java
 * CS 342 Project 5 - Networked Rummy Game
 */

package edu.uic.cs342.project5.models;

import java.io.Serializable;

/**
 * This class is used to store information about a card
 * and perform common operations for comparing.
 */
public class CardModel implements Serializable 
{
	private static final long serialVersionUID = -6151529248414325917L;

	/**
	 * This enum contains all possible suits.
	 * 
	 */
	public static enum Suit {
		CLUB, DIAMOND, HEART, SPADE
	}
	
	/**
	 * This enum contains all possible ranks.
	 * 
	 */
	public static enum Rank {
		ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN,
		EIGHT, NINE, TEN, JACK, QUEEN, KING
	}
	
	private final char[] UNICODE_SUIT = { '\u2663', '\u2666', '\u2665', '\u2660' };
		
	private Suit cardSuit;
	private Rank cardRank;
	private int matchCount;
	
	/**
	 * Constructor for creating a card based on
	 * suit and rank.
	 * 
	 * @param cardSuit Suit of card
	 * @param cardRank Rank of card
	 */
	public CardModel(Suit cardSuit, Rank cardRank) {
		super();
		this.cardSuit = cardSuit;
		this.cardRank = cardRank;
		this.matchCount = 0;
	}
	
	/**
	 * Copy constructor
	 * @param sourceCard Card to copy
	 * @return 
	 */
	public CardModel(CardModel sourceCard) {
		this(sourceCard.getCardSuit(), sourceCard.getCardRank());
	}

	/**
	 * Compares the suit of current card with another card.
	 * @param otherCard Card to compare to
	 * @return Result of comparison
	 */
	public boolean sameSuit(CardModel otherCard) {
		if (this.getCardSuit() == otherCard.getCardSuit())
			return true;
		else
			return false;
	}
	
	/**
	 * Compares the rank of current card with another card.
	 * @param otherCard Card to compare to
	 * @return Result of comparison
	 */
	public boolean sameRank(CardModel otherCard) {
		if (this.getCardRank() == otherCard.getCardRank())
			return true;
		else
			return false;
	}
	
	/**
	 * Compares the current card rank with the rank of another card.
	 * 
	 * @param otherCard Card to compare to
	 * @return the difference between the cards
	 */
	public int compareTo(CardModel otherCard) {
		return (this.getCardRank().compareTo(otherCard.getCardRank()));
	}

	/**
	 * Getter for card suit.
	 * @return the cardSuit
	 */
	public Suit getCardSuit() {
		return cardSuit;
	}

	/**
	 * Getter for card rank.
	 * @return the cardRank
	 */
	public Rank getCardRank() {
		return cardRank;
	}
	
	/**
	 * The card suit and card rank information
	 */
	public String toString() {
		String outputString = new String();
		int rank = this.getCardRank().ordinal(); 
		switch(rank) {
		case 0:
			outputString += "A";
			break;	
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
			outputString += rank + 1;
			break;
		case 10:
			outputString += "J";
			break;
		case 11:
			outputString += "Q";
			break;
		case 12:
			outputString += "K";
			break;
		default:
			return null;
		}
		outputString+= UNICODE_SUIT[getCardSuit().ordinal()];
		return outputString;
	}

	/**
	 * Getter for matchCount
	 * @return the matchCount
	 */
	public int getMatchCount() {
		return matchCount;
	}

	/**
	 * Setter for matchCount
	 * @param matchCount the matchCount to set
	 */
	public void setMatchCount(int matchCount) {
		this.matchCount = matchCount;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object anObject) {
		boolean result = false;
	    if (anObject instanceof CardModel) {
	    	CardModel that = (CardModel) anObject;
	        result = (this.getCardRank() == that.getCardRank() &&
	        		this.getCardSuit() == that.getCardSuit());
	    }
	    return result;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override 
	public int hashCode() {
		return cardSuit.ordinal() * Rank.values().length + cardRank.ordinal();
    }
	
	/**
	 * Gets the point value for a card for Rummy
	 * @return Point value of card
	 */
	public int getPoints() {
		if (cardRank == Rank.ACE)
			return 1;
		else if (cardRank.ordinal() > Rank.TEN.ordinal())
			return 10;
		else
			return cardRank.ordinal() + 2;
	}
}
