package org.academiadecodigo.bootcamp.players;

import org.academiadecodigo.bootcamp.House;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.deck.Card;
import org.academiadecodigo.bootcamp.scanners.integer.IntegerInputScanner;
import org.academiadecodigo.bootcamp.scanners.integer.IntegerRangeInputScanner;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.LinkedList;


public class PlayerHandler extends Gamer implements Runnable {

    private Socket clientSocket;
    private Prompt prompt;
    private boolean roundIsRunning;
    private House house;
    private LinkedList<Card> playerHand;
    private int amountOfCardsHeld;
    private boolean stillWantToBuy;
    private boolean readyToPlay;


    public PlayerHandler(Socket clientSocket, House house) {
        synchronized (house.getPlayerList()) {
            getStartingMoney();
            this.clientSocket = clientSocket;
            roundIsRunning = true;
            this.house = house;
            playerHand = new LinkedList<>();
            amountOfCardsHeld = 0;
            try {
                prompt = new Prompt(clientSocket.getInputStream(), new PrintStream(clientSocket.getOutputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void run() {

        synchronized (house) {

            try {
                idQuestion();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //todo colocar menu entrar/sair
            resetHand();
            //todo dar um jeito de só deixar entrar com round começando
        }
    }

    private void playerMakeChoice() throws IOException {

        String[] options = {"Hit me", "Stand"};

        MenuInputScanner scanner = new MenuInputScanner(options);
        scanner.setMessage("Do you want to buy a card?");
        int answerChoice = prompt.getUserInput(scanner);

        switch (answerChoice) {
            case 1:
                drawCard();
                messageToAll(getName() + " has bought a card.\n");
                if (getHandValue() > 21) {
                    stillWantToBuy = false;
                    messageToAll(getName() + " will not buy more cards.\n");
                    messageToSelf("You have been busted.\n");
                    setHandValue(0);

                }
                break;
            case 2:
                stillWantToBuy = false;
                messageToAll(getName() + " will not buy more cards.\n");
                break;
        }

    }

    public void resetPlayerCards() {

        int initialSize = playerHand.size();
        for (int i = initialSize; i >= 1; i--) {
            playerHand.removeLast();
        }

    }

    public void drawCard() throws IOException {

        amountOfCardsHeld++;
        playerHand.add(house.givePlayerCard());
        increaseHandValue(playerHand.get(amountOfCardsHeld - 1).getRank().getValue());

        System.out.println(getName() + " card draw " + playerHand.get(amountOfCardsHeld - 1).getThisCard());
        System.out.println(getName() + "hand value: " + getHandValue());
        System.out.println("\n _______________________________________");
        messageToSelf(getName() + " card draw " + playerHand.get(amountOfCardsHeld - 1).getThisCard() + "\n");
        messageToSelf(getName() + "hand value: " + getHandValue() + "\n");
    }

    public synchronized void idQuestion() throws IOException {

        StringInputScanner nameQuestion = new StringInputScanner();
        nameQuestion.setMessage("What is your name?\n");
        setName(prompt.getUserInput(nameQuestion));

        IntegerInputScanner ageQuestion = new IntegerRangeInputScanner(18, 100);
        ageQuestion.setMessage("\nHow old are you? Keep in mind no minors are allowed here.\n");
        setAge(prompt.getUserInput(ageQuestion));

        readyMenu();
    }

    public void readyMenu() throws IOException {
        String[] readyMenu = {"Let´s play!", "Exit"};
        MenuInputScanner scanner = new MenuInputScanner(readyMenu);
        scanner.setMessage("Do you want to play some BlackJack?????");
        int answerChoice = prompt.getUserInput(scanner);

        switch (answerChoice) {

            case 1:
                readyToPlay = true;
                house.letsBegin();
                break;
            case 2:

                break;
        }

        getStartingMoney();
    }

    public void makeBet() {

        int currentBet = 1;
        pay(currentBet);
        house.setTableMoney(currentBet);
    }

    // sends message to all players
    public void messageToAll(String whatToSay) throws IOException {

        synchronized (house.getPlayerList()) {

            for (int i = 0; i < house.getPlayerList().size(); i++) {

                if (house.getPlayerList().get(i).clientSocket.getOutputStream() != clientSocket.getOutputStream()) {

                    house.getPlayerList().get(i).clientSocket.getOutputStream().write(whatToSay.getBytes());

                }
            }
        }
    }

    public void messageToEveryoneEvenMe(String whatToSay) throws IOException {

        synchronized (house.getPlayerList()) {

            for (int i = 0; i < house.getPlayerList().size(); i++) {

                house.getPlayerList().get(i).clientSocket.getOutputStream().write(whatToSay.getBytes());
            }
        }
    }

    // sends message to self player
    public void messageToSelf(String whatToSay) throws IOException {

        clientSocket.getOutputStream().write(whatToSay.getBytes());
    }

    public void playerRound() throws IOException {

        //feita a aposta, dinheiro na mesa
        //jogador recebe 2 cartas, que são ao mesmo tempo tiradas do deck, tem seus valores adicionados à mão
        amountOfCardsHeld = 0;
        resetPlayerCards();
        resetHand();

        makeBet();
        System.out.println(getName() + "current balance: " + getMoney());
        messageToSelf(getName() + "current balance: " + getMoney() + "\n");
        drawCard();
        drawCard();


        stillWantToBuy = true;

        while (stillWantToBuy) {
            try {
                playerMakeChoice();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //bloco de testes pra ver se a mecanica do draw esta ok
        //System.out.println("card draw "+playerHand.get(0).getThisCard());
        //System.out.println("card draw "+playerHand.get(1).getThisCard());


        //resetPlayerCards();

    }


    public boolean isReadyToPlay() {
        return readyToPlay;
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
                messageToSelf("With animal instinct, you left the table with that sweet pie. " +
                        "However, she makes you pay for huncka huncka, plus the room");
                messageToAll(getName() + " left the table with a fine real woman and a bump in his pants");
                readyToPlay = false;
                int roomCost = 3;
                int girlCost = 3;
                pay(roomCost + girlCost);
                messageToSelf("You had some bills to pay: " + "Room: " + roomCost + " / Girl: " + girlCost);
                messageToSelf("Your current balance is: " + getMoney());


            case 2:
                messageToSelf("No thanks bitch! I'm here to make money!");
                messageToSelf("Girl: You will regret this, I could make all your dreams come true");
                messageToAll("Bitches are crawling around the table... Hold your pants boys! "
                        + getName() + " can be a disguised pussy!");
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
                readyToPlay = false;
                int earnedCoins = 10;
                this.setMoney(earnedCoins);
                messageToSelf("You chose, literally, the hard way... Brave soldier");
                messageToAll(getName() + " leaves the table, heading into a dark room with some suspicious shemales");

        }

    }


} // the end
