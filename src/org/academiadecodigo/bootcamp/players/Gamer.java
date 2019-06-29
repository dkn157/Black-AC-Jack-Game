package org.academiadecodigo.bootcamp.players;

import org.academiadecodigo.bootcamp.deck.Card;

import java.util.LinkedList;

public abstract class Gamer {

    private int money;
    private String name;
    private int age;
    private int handValue;


    public void getStartingMoney() {
        money = 10;
    }

    public void increaseHandValue(int cardValue){
        handValue+=cardValue;
    }

    public int getMoney() {
        return money;
    }

    public String getName() {
        return name;
    }

    public int getHandValue() {
        return handValue;
    }

    public void bet(int bet) {
        money-= bet;
    }

    public void gatherWinnings(int winnings) {
        money+=winnings;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void resetHand() {
        handValue = 0;
    }
}