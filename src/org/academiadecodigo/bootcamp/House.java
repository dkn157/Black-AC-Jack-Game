package org.academiadecodigo.bootcamp;

import org.academiadecodigo.bootcamp.deck.Card;
import org.academiadecodigo.bootcamp.deck.Deck;
import org.academiadecodigo.bootcamp.players.PlayerHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class House {

    private ServerSocket serverSocket;
    private int myPort = 8080;
    private LinkedList<PlayerHandler> playerList;
    private Deck deck;
    private int tableMoney;
    private ExecutorService fixedPool;



    public House() {

        tableMoney = 0;
        playerList = new LinkedList<>();
        deck = new Deck();

    }

    public void init() throws IOException {

        fixedPool = Executors.newFixedThreadPool(1500);
        serverSocket = new ServerSocket(myPort);

        while (true) {

            listening();


            synchronized (this) {
                synchronized (playerList) {

                    while (playerList.size() > 1) {

                        doARound();

                    }
                }
            }
        }
    }

    public void shuffleDeck() {
        deck.shuffleDeck();
    }

    public int getTableMoney() {
        return tableMoney;
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

            winners.get(i).bet(-tableMoney);
            winners.get(i).messageToSelf("You have won the round, and that gives you the right to receive "+tableMoney+"\n");
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

        for ( int i = 0; i < playerList.size(); i++) {
            if (!playerList.get(i).isReadyToPlay()) {
                theyAreReady = false;
            }
        }

        return theyAreReady;

    }

    public void listening() throws IOException {
        Socket clientSocket = serverSocket.accept();

        PlayerHandler playerHandler = new PlayerHandler(clientSocket, this);
        fixedPool.submit(playerHandler);
        playerList.add(playerHandler);
    }

    public void doARound() throws IOException {
        for (int i = 0; i < playerList.size(); i++) {
            playerList.get(i).playerRound();
        }

        checkWhoWon();
        shuffleDeck();
    }
}
