package org.academiadecodigo.bootcamp.deck;

public class Card {

    private String thisCard;
    private Suits suit;
    private Ranks rank;

    public Card(Suits whichSuit, Ranks whichRank){

        thisCard = whichRank.toString()+" of "+whichSuit.toString();
        //System.out.println(rank);
        suit = whichSuit;
        rank = whichRank;
    }

    public Ranks getRank() {
        return rank;
    }

    public String getThisCard() {
        return thisCard;
    }

    public String getCardArt() {

        return suit.getSuitArt();
    }
}

//public enum Card implements Comparable<Card> {
//
//    ACE    (1,11),
//    KING       (13,10),
//    QUEEN  (12,10),
//    JACK       (11,10),
//    TEN    (10,10),
//    NINE       (9,9),
//    EIGHT  (8,8),
//    SEVEN  (7,7),
//    SIX    (6,6),
//    FIVE       (5,5),
//    FOUR   (4,4),
//    THREE  (3,3),
//    TWO    (2,2);
//
//    private final int rank;
//    private final int value;
//
//    private static final String[] CARD_NAMES = {
//            "ace", "two", "three",
//            "four", "five", "six", "seven",
//            "eight", "nine", "ten", "jack",
//            "queen", "king"
//    };
//
//    /**
//     * rank an int to signify the rank the value of this rank in a Blackjack game.
//     */

//    private Card(final int rank, final int value) {
//        this.rank = rank;
//        this.value = value;
//    }
//
//    /**
//     * return an int to signify the rank
//     */

//    public int getRank() {
//        return rank;
//    }
//
//    /**
//     * return the value of this rank in a Blackjack game.
//     * The value of 1 for an Ace is excluded here and should be treated as a special case elsewhere.
//     */

//    public int getValue() {
//        return value;
//    }
//
//    @Override
//    public String toString() {
//        return CARD_NAMES[rank] + " value: " + value;
//    }
//}
