package org.academiadecodigo.bootcamp.deck;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Hand {

//    private Queue<Card> cards = new PriorityQueue<Card>();
//
//    /**
//     * Adds a carrd to the collection to be added
//     */
//
//    public void addCard(Card card) {
//        cards.add(card);
//    }
//
//    /**
//     * returns the total value of this hand in a Blackjack game.
//     * If there are aces, returns the highest value under 22.
//     */
//    public int getTotal() {
//
//        List<Integer> totals = new ArrayList<Integer>();
//
//        totals.add(0);
//
//        for(Card card : cards) {
//            for (int i = 0; i < totals.size(); i++) {
//                totals.add(i, totals.get(i).intValue() + card.getValue());
//                totals.remove(i+1);
//            }
//            if (card.getRank() == 1) totals.add(totals.get(totals.size() - 1) - 10);
//        }
//        for (Integer total : totals) {
//            if (total.intValue() <= 21) return total;
//        }
//        return totals.get(totals.size()-1).intValue();
//    }
//
//    /**
//     * return a containing the cards in this
//     */
//    public List<Card> getCards() {
//        return new ArrayList<>(cards); // defensive copy and conversion to list
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder("\nHand:\n");
//        for (Card card : cards) {
//            sb.append(card).append("\n");
//        }
//        sb.deleteCharAt(sb.length() - 1); // remove last new line
//        return sb.toString();
//    }
//}
}
