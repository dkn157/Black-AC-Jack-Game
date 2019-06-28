package org.academiadecodigo.bootcamp.deck;

import java.util.LinkedList;

public class Deck {

    // todo linkedlist e array list sao perguntas de entrevista

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
