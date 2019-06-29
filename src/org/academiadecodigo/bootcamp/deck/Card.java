package org.academiadecodigo.bootcamp.deck;

public class Card {

    private String thisCard;
    private Ranks rank;

    public Card(Suits whichSuit, Ranks whichRank){

        thisCard = whichRank.toString()+" of "+whichSuit.toString();
        //System.out.println(rank);
        rank = whichRank;
    }

    public Ranks getRank() {
        return rank;
    }

    public String getThisCard() {
        return thisCard;
    }
}
