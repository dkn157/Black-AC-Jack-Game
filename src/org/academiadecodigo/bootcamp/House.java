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
    private boolean theyAreReady;
    private ExecutorService fixedPool;


    public House() throws IOException {

        tableMoney = 0;
        playerList = new LinkedList<>();
        deck = new Deck();
        fixedPool = Executors.newFixedThreadPool(1500);
        serverSocket = new ServerSocket(myPort);

    }

    public void init() throws IOException, InterruptedException {

        while (true) {

            listening();
            System.out.println("vamos ver se passa o playerready");

            synchronized (playerList) {
                //while (arePlayersReady()) {
                    //synchronized (this) {
                    while (!arePlayersReady()) {
                        arePlayersReady();
                    }
                    //wait();
                    if (arePlayersReady()) {
                    System.out.println("passou o allplayersready?");

                        if (playerList.size() > 1) {
                            System.out.println("AQUI PORRA FDP");

                            doARound();
                            //botar menu p ver quem quer mais

                        }

                    }

                //}
                //}
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

    public void checkWhoWon() {

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

        }

    }

    public void clearLinkedList(LinkedList whatToClear) {
        for (int i = 0; i < whatToClear.size(); i++) {
            whatToClear.removeLast();
        }
    }

    public boolean arePlayersReady() {
        synchronized (playerList) {
            System.out.println("esta testando o player ready");

            theyAreReady = true;

            System.out.println(" começa com" + theyAreReady);
            for (int i = 0; i < playerList.size(); i++) {
                if (!playerList.get(i).isReadyToPlay()) {

                    theyAreReady = false;
                    System.out.println("atualmente esta " + theyAreReady + " para " + playerList.get(i).getName());
                }
            }
            System.out.println("o stat final ficou como " + theyAreReady);
            //notifyAll();
            return theyAreReady;
        }
    }

    public void listening() throws IOException {

        Socket clientSocket = serverSocket.accept();
        PlayerHandler playerHandler = new PlayerHandler(clientSocket, this);
        playerHandler.messageToSelf("   ___   ______   ____  __           __       __           __  \n" +
                "   /   | / ____/  / __ )/ /___ ______/ /__    / /___ ______/ /__\n" +
                "  / /| |/ /      / __  / / __ `/ ___/ //_/_  / / __ `/ ___/ //_/\n" +
                " / ___ / /___   / /_/ / / /_/ / /__/ ,< / /_/ / /_/ / /__/ ,<   \n" +
                "/_/  |_\\____/  /_____/_/\\__,_/\\___/_/|_|\\____/\\__,_/\\___/_/|_|  \n" +
                "                                                                \n");
        fixedPool.submit(playerHandler);
        playerList.add(playerHandler);

    }

    public void doARound() throws IOException {

        for (int i = 0; i < playerList.size(); i++) {
            playerList.get(i).playerRound();
        }

        checkWhoWon();

    }
}
