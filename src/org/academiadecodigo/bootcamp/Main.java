package org.academiadecodigo.bootcamp;

import org.academiadecodigo.bootcamp.deck.Deck;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        Deck test = new Deck();
        House house = null;
        try {
            house = new House();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            house.init();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}
