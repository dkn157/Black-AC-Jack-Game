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



    public House() {
        tableMoney = 0;
        playerList = new LinkedList<>();
        deck = new Deck();
    }



    public LinkedList<PlayerHandler> getPlayerList() {
        return playerList;
    }



    public void init() throws IOException {

        ExecutorService fixedPool = Executors.newFixedThreadPool(1500);
        serverSocket = new ServerSocket(myPort);


        while (true) {

            Socket clientSocket =serverSocket.accept();

            PlayerHandler playerHandler = new PlayerHandler(clientSocket,this);
            fixedPool.submit(playerHandler);
            playerList.add(playerHandler);

        }
    }

    public void shuffleDeck() {
        deck.shuffleDeck();
    }

    public int getTableMoney() {
        return tableMoney;
    }

    public void setTableMoney(int betMoney) {
        this.tableMoney+=betMoney;
    }

    public Card givePlayerCard() {
        return deck.drawCard();
    }
}
