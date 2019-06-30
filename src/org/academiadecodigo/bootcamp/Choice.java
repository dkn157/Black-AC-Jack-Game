package org.academiadecodigo.bootcamp;

import org.academiadecodigo.bootcamp.players.PlayerHandler;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;

import java.io.IOException;


public class Choice {


    private Prompt prompt;
    private PlayerHandler playerHandler;

    public Choice(PlayerHandler playerHandler) {
        this.playerHandler=playerHandler;
    }


    // Hooker method. Should be prompt to player if money is high
    public void girlAppears() throws IOException {

        String[] options = {"Go with her", "Hell no! I'm gambling"};

        MenuInputScanner scanner = new MenuInputScanner(options);
        scanner.setMessage("A girl appears next to you, inviting you to check-in into a room" +
                "       .-'\"\"\"''---.___\n" +
                "           .'               \"'-.___\n" +
                "         _'              _'-\"'\"\"\"  \"\"\"-\n" +
                "        /    7        .'\"              \"->\n" +
                "       .    .|     _-'                   '.\n" +
                "      .  .'\"  :   '.         _.------._  ''\n" +
                "     .  -      . .'       .-'  \"-   .' \\ :\n" +
                "     | '        >       .'.''\"\\\"-   .'\\\"_'\n" +
                "     |'        <      .'   :__/  : :_.':'\n" +
                "  .--'-._      :   .--:     -._.'  '._.'\n" +
                " '>      '.     '. | '              .' :\n" +
                "'.        :'     '-'.____        .__.  '\n" +
                " /         :             :.          .'\n" +
                " \\.       /              | '\"-_  __-'\n" +
                "   \\.'-'\"'         .'\"\":''    :-\"\"\"\"'.\n" +
                "                  :   :               .\n" +
                "                  |  :                :\n" +
                "                  | :           .''.  :\n" +
                "                  |.'.        _.:   '.:\n" +
                "                  |    '---'\"\"  :    :\"\n" +
                "                  |     '      :     :\n" +
                "                  .'.___:._   .'    .\n" +
                "                   .  '    '\"'.     '\n" +
                "                   |   :      '    :\n" +
                "                   :    .    :    ' :\n" +
                "     _.-'\"'--..__   :   : . :    .  _:\n" +
                "  .'      .       \"-:   :   '   ..-\"  :\n" +
                " (         '-       :   :._:   /   _.--\"\"\"--._\n" +
                "  '          '. _....:  : .   / .-'           '.\n" +
                "   '           :  .\"\" \"\"'-'  /-\"               )\n" +
                "    '._        :  :     ..-'                  /\n" +
                "       '-._     \\'' _ .'\"\"\"\"\"\"'-.         _.-\n" +
                "          .'--.__ .' '           - ____.-'\n" +
                "         :         \"\":-.._______.'\n" +
                "          '-.....-'''                Hello honey, wanna party?");
        int answerChoice = prompt.getUserInput(scanner);

        switch (answerChoice) {
            case 1:
                playerHandler.messageToSelf("With animal instinct, you left the table with that sweet pie. " +
                        "However, she makes you pay for huncka huncka, plus the room");
                playerHandler.messageToAll(playerHandler.getName() + " left the table with a fine real woman and a bump in his pants");
                playerHandler.readyToPlay = false;
                int roomCost = 3;
                int girlCost = 3;
                playerHandler.pay(roomCost + girlCost);
                playerHandler.messageToSelf("You had some bills to pay: " + "Room: " + roomCost + " / Girl: " + girlCost);
                playerHandler.messageToSelf("Your current balance is: " + playerHandler.getMoney());


            case 2:
                playerHandler.messageToSelf("No thanks bitch! I'm here to make money!");
                playerHandler.messageToSelf("Girl: You will regret this, I could make all your dreams come true");
                playerHandler.messageToAll("Bitches are crawling around the table... Hold your pants boys! "
                        + playerHandler.getName() + " can be a disguised pussy!");
                break;
        }
    }

    //earn money method. should be prompt to player if money is low
    public void earnMoney() throws IOException {

        String[] options = {"Fuck it. I really need money to gamble", "My dignity is more important"};

        MenuInputScanner scanner = new MenuInputScanner(options);
        scanner.setMessage("Since you are low on coins, a group of big boys approaches you with a proposition." +
                " Do you want to join them in the dark room?" +
                "      ..ooo*\"\"\"**ooooo .oo*\"\"*ooo..\n" +
                "             .  oo*\"           \"*o.oo*\"           \"*o.\n" +
                "            . o\"                   'o\"                  \"o\n" +
                "             o                      o                     *o\n" +
                "           .o                       o                        'o\n" +
                "           o                        o                          o.\n" +
                "          o                          o                          o\n" +
                "         o                          \\o/                         o\n" +
                "         o                         --O--                         o\n" +
                "         o.                         /o\\                         .o\n" +
                "         \"o                          o                           o\n" +
                "          oo                         o                          oo\n" +
                "          oo.                       oo                        oo\n" +
                "           'ooo.                  .oo.                     ooo\n" +
                "            \"o \"\"oo,,        ,,oO-'Oo,       ,,,,,,..oo\"o\n" +
                "             o.         \"\"\"\"\"\"    oo       \"\"\"\"\"        .o\n" +
                "             'o                    oo                    o'\n" +
                "             *o                    oo                    o\n" +
                "              'o                    o                    o\n" +
                "              o                     o                   o\n" +
                "               o                    o                  o\n" +
                "               o                    o                 o\n" +
                "               o                    o                 o\n" +
                "                o                    o                 o\n" +
                "                o                    o                 o\n");

        int answerChoice = prompt.getUserInput(scanner);

        switch (answerChoice) {
            case 1:
                int earnedCoins = 10;
                this.playerHandler.setMoney(earnedCoins);
                playerHandler.messageToSelf("You chose, literally, the hard way... Brave soldier");
                playerHandler.messageToAll(playerHandler.getName() + " leaves the table, heading into a dark room with some suspicious shemales");

        }
    }
}

