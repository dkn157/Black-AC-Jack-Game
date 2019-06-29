package org.academiadecodigo.bootcamp.deck;

// TODO: 29/06/2019 Player decide ACE value 1
public enum Ranks {
    ACE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    JACK(10),
    QUEEN(10),
    KING(10);


    private int value;

    Ranks(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
