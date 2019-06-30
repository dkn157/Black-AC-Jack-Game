package org.academiadecodigo.bootcamp.players;


public abstract class Gamer {

    private int money;
    private String name;
    private int age;
    private int handValue;


    public void getStartingMoney() {
        money = 100;
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

    public void pay(int value) {
        money-= value;
    }

    public void setMoney(int value) {
        this.money += value;
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

    public void setHandValue(int howMuch) {
        handValue = howMuch;
    }
}

