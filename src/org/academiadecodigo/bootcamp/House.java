package org.academiadecodigo.bootcamp;

import org.academiadecodigo.bootcamp.deck.Card;
import org.academiadecodigo.bootcamp.deck.Deck;
import org.academiadecodigo.bootcamp.players.PlayerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class House implements Runnable {

    private ServerSocket serverSocket;
    private int myPort = 8080;
    private LinkedList<PlayerHandler> playerList;
    private Deck deck;
    private int tableMoney;
    private ExecutorService fixedPool;
    private boolean gameOver;
    private int roundCounter;
    private boolean readyToPlay;


    public House() {

        tableMoney = 0;
        playerList = new LinkedList<>();
        deck = new Deck();
        gameOver = false;
        roundCounter = 0;

    }

    public void init() throws IOException {

        fixedPool = Executors.newFixedThreadPool(1500);
        serverSocket = new ServerSocket(myPort);

        while (serverSocket.isBound()) {
            joinGame(); //verificar se mudou
        }

    }

    public void shuffleDeck() {
        deck.shuffleDeck();
    }


    public void setTableMoney(int betMoney) {
        this.tableMoney += betMoney;
    }

    public Card givePlayerCard() {
        return deck.drawCard();
    }

    public LinkedList<PlayerHandler> getPlayerList() {
        return playerList;
    }

    public void checkWhoWon() throws IOException {

        LinkedList<Integer> scores = new LinkedList<>();

        for (int i = 0; i < playerList.size(); i++) {

            scores.add(playerList.get(i).getHandValue());

        }

        int maxScore = 0;
        int drawChecker = 0;
        LinkedList<PlayerHandler> winners = new LinkedList<>();

        for (int i = 0; i < playerList.size(); i++) {

            if (scores.get(i) > maxScore) {
                maxScore = scores.get(i);
                drawChecker = 1;
                clearLinkedList(winners);
                winners.add(playerList.get(i));

            } else if (scores.get(i) == maxScore) {
                winners.add(playerList.get(i));
                drawChecker++;
            }
        }

        tableMoney = (int) (tableMoney / winners.size());

        for (int i = 0; i < winners.size(); i++) {

            winners.get(i).pay(-tableMoney);
            winners.get(i).messageToSelf("You have won the round!! total income is: " + tableMoney + "\n");
        }
    }

    public void clearLinkedList(LinkedList whatToClear) {
        for (int i = 0; i < whatToClear.size(); i++) {
            whatToClear.removeLast();
        }
    }

    public boolean arePlayersReady() {

        boolean theyAreReady;
        theyAreReady = true;
        System.out.println("They are ready is " + theyAreReady);
        for (int i = 0; i < playerList.size(); i++) {
            System.out.println("instancing theyareready as " + theyAreReady);
            if (!playerList.get(i).isReadyToPlay()) {
                System.out.println("atm theyareready is " + theyAreReady);
                theyAreReady = false;
                System.out.println("and it was changed to " + theyAreReady);
            }
        }

        return theyAreReady;

    }

    public void joinGame() throws IOException {

        Socket clientSocket = serverSocket.accept();
        PlayerHandler playerHandler = new PlayerHandler(clientSocket, this);
        playerList.add(playerHandler);
        playerHandler.messageToSelf(" __      __  ___ ___ .___.____     ___________    .___.__  .__                      \n" +
                "/  \\    /  \\/   |   \\|   |    |    \\_   _____/  __| _/|  | |__| ____    ____  ______\n" +
                "\\   \\/\\/   /    ~    \\   |    |     |    __)_  / __ | |  | |  |/    \\  / ___\\/  ___/\n" +
                " \\        /\\    Y    /   |    |___  |        \\/ /_/ | |  |_|  |   |  \\/ /_/  >___ \\ \n" +
                "  \\__/\\  /  \\___|_  /|___|_______ \\/_______  /\\____ | |____/__|___|  /\\___  /____  >\n" +
                "       \\/         \\/             \\/        \\/      \\/              \\//_____/     \\/ \n" +
                "__________.__                 __        ____.              __                       \n" +
                "\\______   \\  | _____    ____ |  | __   |    |____    ____ |  | __                   \n" +
                " |    |  _/  | \\__  \\ _/ ___\\|  |/ /   |    \\__  \\ _/ ___\\|  |/ /                   \n" +
                " |    |   \\  |__/ __ \\\\  \\___|    </\\__|    |/ __ \\\\  \\___|    <                    \n" +
                " |______  /____(____  /\\___  >__|_ \\________(____  /\\___  >__|_ \\                   \n" +
                "        \\/          \\/     \\/     \\/             \\/     \\/     \\/                   \n");
        fixedPool.submit(playerHandler);
        //playerHandler.idQuestion();


    }

    public void startRound() throws IOException {
        for (int i = 0; i < playerList.size(); i++) {
            playerList.get(i).playerRound();
        }
        shuffleDeck();
    }

    public String podiumMessage() {

        String thisWillBeReturned = new String();

        LinkedList<Integer> finalScores = new LinkedList<>();
        int overallMaxMoney = -1;
        for (int i = 0; i < playerList.size(); i++) {

            finalScores.add(playerList.get(i).getMoney());

            if (playerList.get(i).getMoney() > overallMaxMoney) {
                overallMaxMoney = playerList.get(i).getMoney();
            }
        }

        for ( int i = 0; i < playerList.size();i++) {
            if ( playerList.get(i).getMoney() == overallMaxMoney) {
                thisWillBeReturned += playerList.get(i).getName()+" finished the game with the highest amount of money equivalent to "+playerList.get(i).getMoney()+"\n";
            }
        }

        return thisWillBeReturned;
    }

    public void letsBegin() throws IOException {

        for (int i = 0; i < playerList.size(); i++) {
            if (!playerList.get(i).isReadyToPlay()) {
                readyToPlay = false;
                return;
            }
            readyToPlay = true;
        }

        if (playerList.size() > 1 && readyToPlay == true) {

            while (!gameOver) {
                startRound();
                checkWhoWon();
                roundCounter++;
                if (roundCounter == 1) {
                    gameOver = true;
                }
            }
            endingMessages();


        }

    }

    public void endingMessages() throws IOException {
        playerList.get(0).messageToEveryoneEvenMe(podiumMessage());

        for ( int i = 0; i < playerList.size(); i++) {
            playerList.get(i).messageToSelf("You finished the game with an amount of money equal to "+playerList.get(i).getMoney()+".\n");
        }
    }

    @Override
    public void run() {

    }
} // the end










