package org.academiadecodigo.bootcamp.deck;

public enum Suits {

    SPADES("spades"),
    HEARTS("hearts"),
    DIAMONDS("diamonds"),
    CLUBS("clubs"),
    ;

    private final String spades = "       /\\\n" +
                                  "     .'  `.\n" +
                                  "    '      `.\n" +
                                  " .'          `.\n" +
                                  "{              }\n" +
                                  " ~-...-||-...-~\n" +
                                  "       ||\n" +
                                  "      '--`";

    private final String diamonds = "     /\\\n" +
                                    "   .'  `.\n" +
                                    "  '      `.\n" +
                                    "<          >\n" +
                                    " `.      .'\n" +
                                    "   `.  .'\n" +
                                    "     \\/";


    private final String clubs = "       /\\\n" +
                                 "     .'  `.\n" +
                                 "    '      `.\n" +
                                 " .'          `.\n" +
                                 "{              }\n" +
                                 " ~-...-||-...-~\n" +
                                 "       ||\n" +
                                 "      '--`";


    private final String hearts = " .-~~~-__-~~~-.\n" +
                                  "{              }\n" +
                                  " `.          .'\n" +
                                  "   `.      .'\n" +
                                  "     `.  .'\n" +
                                  "       \\/\n";


    private final String suitType;

    Suits(String suitType) {
        this.suitType = suitType;
    }

    public String getSuitArt(){

        String typeOfSuit = "";

        if(suitType.equals("spades")) {
            typeOfSuit = spades;
        }

        if(suitType.equals("clubs")) {
            typeOfSuit = clubs;
        }

        if(suitType.equals("hearts")) {
            typeOfSuit = hearts;
        }

        if(suitType.equals("diamonds")) {
            typeOfSuit=  diamonds;
        }

        return typeOfSuit;
    }
}
