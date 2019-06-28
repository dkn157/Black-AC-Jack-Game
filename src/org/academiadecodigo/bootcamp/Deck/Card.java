package org.academiadecodigo.bootcamp.Deck;

public class Card {

    private String rank;

    public Card(Suits whichSuit, Ranks whichRank){

        rank = whichRank.toString()+" of "+whichSuit.toString();
        //System.out.println(rank);

    }
}
