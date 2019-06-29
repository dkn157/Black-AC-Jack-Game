package org.academiadecodigo.bootcamp.players;

import org.academiadecodigo.bootcamp.House;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.deck.Card;
import org.academiadecodigo.bootcamp.scanners.integer.IntegerInputScanner;
import org.academiadecodigo.bootcamp.scanners.integer.IntegerRangeInputScanner;
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


    public PlayerHandler(Socket clientSocket, House house) {

        this.clientSocket = clientSocket;
        roundIsRunning = true;
        this.house = house;
        playerHand = new LinkedList<>();
        amountOfCardsHeld = 0;
    }

    @Override
    public void run() {

        try {
            prompt = new Prompt(clientSocket.getInputStream(), new PrintStream(clientSocket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringInputScanner nameQuestion = new StringInputScanner();
        nameQuestion.setMessage("What is your name?\n");
        setName(prompt.getUserInput(nameQuestion));

        IntegerInputScanner ageQuestion = new IntegerRangeInputScanner(18, 100);
        ageQuestion.setMessage("\nHow old are you? Keep in mind no minors are allowed here.\n");
        setAge(prompt.getUserInput(ageQuestion));

        getStartingMoney();
        resetHand();

        //todo dar um jeito de só deixar entrar com round começando

        while (roundIsRunning) {

            IntegerInputScanner betQuestion = new IntegerRangeInputScanner(1,getMoney());
            betQuestion.setMessage("\nHow much do you want to bet?\n");
            int currentBet = prompt.getUserInput(betQuestion);
            bet(currentBet);
            house.setTableMoney(currentBet);


            //feita a aposta, dinheiro na mesa
            //jogador recebe 2 cartas, que são ao mesmo tempo tiradas do deck, tem seus valores adicionados à mão

            drawCard();
            drawCard();

            //bloco de testes pra ver se a mecanica do draw esta ok
            System.out.println("card draw"+playerHand.get(0).getThisCard());
            System.out.println("card draw"+playerHand.get(1).getThisCard());
            System.out.println("hand value: " + getHandValue());
            System.out.println("current balance: " + getMoney());

            resetPlayerCards();

        }

    }

    public void resetPlayerCards() {

        int initialSize = playerHand.size();
        for (int i = initialSize; i >= 1; i--) {
            playerHand.removeLast();
        }

    }

    public void drawCard() {
        amountOfCardsHeld++;
        playerHand.add(house.givePlayerCard());
        increaseHandValue(playerHand.get(amountOfCardsHeld-1).getRank().getValue());
    }


}

