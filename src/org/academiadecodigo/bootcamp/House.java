package org.academiadecodigo.bootcamp;

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



    public House() {
        playerList = new LinkedList<>();
    }



    public LinkedList<PlayerHandler> getPlayerList() {
        return playerList;
    }



    public void init() throws IOException {

        ExecutorService fixedPool = Executors.newFixedThreadPool(1500);
        serverSocket = new ServerSocket(myPort);


        while (true) {

            Socket clientSocket =serverSocket.accept();

            PlayerHandler playerHandler = new PlayerHandler(clientSocket);
            fixedPool.submit(playerHandler);
            playerList.add(playerHandler);

        }
    }
}
