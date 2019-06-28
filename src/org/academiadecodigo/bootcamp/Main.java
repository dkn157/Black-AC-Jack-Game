package org.academiadecodigo.bootcamp;

import org.academiadecodigo.bootcamp.deck.Deck;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        Deck test = new Deck();
        House house = new House();
        try {
            house.init();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
