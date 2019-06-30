package org.academiadecodigo.bootcamp.deck;

import java.util.*;

public class Deck {

    // todo linkedlist e array list sao perguntas de entrevista

    private LinkedList<Card> deck;

    public Deck() {

        deck = new LinkedList<>();
        shuffleDeck();

    }

    public Card drawCard() {

        int whichCard = (int)(Math.random()*deck.size());
        Card cardDrawned = deck.get(whichCard);
        deck.remove(whichCard);
        return cardDrawned;

    }

    public void shuffleDeck() {

        for ( int i = 0; i< Suits.values().length;i++) {

            for ( int j=0; j<Ranks.values().length;j++) {

                deck.add(new Card(Suits.values()[i],Ranks.values()[j]));
                //System.out.println(Suits.values()[i].toString()+Ranks.values()[j].toString());

            }
        }

    }
//
//    /**
//     * returns a LinkedList containing all cards in this deck.
//     */
//    public List<Card> getCards() {
//        return new ArrayList<>(deck);
//    }
//
//    /**
//     * rework of the draw a card method
//     */
//
//    public Card drawNextCard() {
//
//        try {
//            return deck.remove();
//
//        } catch (NoSuchElementException e) {
//            fillDeckWithAllCards();
//            return drawNextCard();
//        }
//    }
//
//    /**
//     * Fills up the LinkedList with 4 reference to each Card rank and shuffles the deck.
//     */
//
//    private void fillDeckWithAllCards() {
//        for(int i = 0; i < 4; i++) {
//            for(Card card : Card.values()) {
//                deck.add(card);
//            }
//        }
//        shuffle();
//    }
//
//    /**
//     * Shuffles the deck.
//     */
//
//    public void shuffle() {
//        Collections.shuffle(deck);
//    }

}
