package edu.uic.cs342.project5.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import edu.uic.cs342.project5.models.CardModel;

public class CardView extends JButton {
	private static final long serialVersionUID = 696317283046618652L;
	//private JLabel cardLabel;
	private Dimension cardDim;
	
	public static final Dimension SMALL = new Dimension(30, 35);
	public static final Dimension LARGE = new Dimension(40, 50);
	public static final Dimension XLARGE = new Dimension(70, 95);
	
	/**
	 * This constructor is for the back of a card
	 */
	public CardView() {
		this.setText(" Deck ");
		this.cardDim = XLARGE;

		this.setForeground(Color.BLUE);
		
		this.setFont(new Font("Times New Roman", Font.BOLD, 20));
		this.setBackground(new Color(230, 204, 255));
		setViewProperties();
	}

	/**
	 * Creates a card view based on a card model
	 * @param cardModel Source card model
	 * @param cardDim Card size
	 */
	public CardView(CardModel cardModel, Dimension cardDim) {
		this.cardDim = cardDim;
		this.cardDim = XLARGE;
		this.setCard(cardModel);
		setViewProperties();
	}
	
	/**
	 * Sets the properties of the card view 
	 */
	public void setViewProperties() {
		this.setBorder(BorderFactory.createRaisedBevelBorder());	
		this.setCardSize(cardDim);
	}
	
	public void setCard(CardModel cardModel) {
		if(cardModel != null) {	
			this.setText(cardModel.toString());
			
			this.setForeground(cardModel.getCardSuit() == CardModel.Suit.HEART
					|| cardModel.getCardSuit() == CardModel.Suit.DIAMOND ? Color.red
							: Color.black);
			this.setBackground(Color.white);
		}
		else {
			this.setText("");
			this.setBackground(new Color(160, 200, 80));
		}
	}
	
	public void setCardSize(Dimension cardDim) {
		this.cardDim = cardDim;
		this.setPreferredSize(this.cardDim);
		this.setMaximumSize(this.cardDim);
		this.setMinimumSize(this.cardDim);
	}
}