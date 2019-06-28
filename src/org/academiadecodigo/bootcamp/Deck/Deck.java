package org.academiadecodigo.bootcamp.Deck;

import sun.awt.image.ImageWatched;

import java.util.LinkedList;

public class Deck {

    // todo linkedlist e array list sao perguntas de entrevista
    //Juuuuuuust in case

    /*String[] cards = {"Ace of spades", "2 of spades", "3 of spades", "4 of spades", "5 of spades", "6 of spades",
            "7 of spades", "8 of spades", "9 of spades", "10 of spades", "Jack of spades", "Queen of spades",
            "King of spades", "Ace of hearts", "2 of hearts", "3 of hearts", "4 of hearts", "5 of hearts",
            "6 of hearts", "7 of hearts", "8 of hearts", "9 of hearts", "10 of hearts", "Jack of hearts",
            }
           */

    private LinkedList<Card> deck;

    public Deck() {

        deck = new LinkedList<>();

        for ( int i = 0; i< Suits.values().length;i++) {

            for ( int j=0; j<Ranks.values().length;j++) {

                deck.add(new Card(Suits.values()[i],Ranks.values()[j]));
                //System.out.println(Suits.values()[i].toString()+Ranks.values()[j].toString());

            }
        }

    }

}
