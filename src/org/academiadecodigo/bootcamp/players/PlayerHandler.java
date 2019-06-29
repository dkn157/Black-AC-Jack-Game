package org.academiadecodigo.bootcamp.players;

import org.academiadecodigo.bootcamp.House;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.deck.Card;
import org.academiadecodigo.bootcamp.scanners.integer.IntegerInputScanner;
import org.academiadecodigo.bootcamp.scanners.integer.IntegerRangeInputScanner;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
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
            makeIntroduction();

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
        System.out.println(getName()+ " card draw " + playerHand.get(amountOfCardsHeld - 1).getThisCard());
        System.out.println(getName()+ "hand value: " + getHandValue());
        messageToSelf(getName()+ " card draw " + playerHand.get(amountOfCardsHeld - 1).getThisCard()+"\n");
        messageToSelf(getName()+ "hand value: " + getHandValue()+"\n");
    }

    public void makeIntroduction() {

        StringInputScanner nameQuestion = new StringInputScanner();
        nameQuestion.setMessage("What is your name?\n");
        setName(prompt.getUserInput(nameQuestion));

        IntegerInputScanner ageQuestion = new IntegerRangeInputScanner(18, 100);
        ageQuestion.setMessage("\nHow old are you? Keep in mind no minors are allowed here.\n");
        setAge(prompt.getUserInput(ageQuestion));

        String[] readyMenu = {"Let´s play!","Exit"};
        MenuInputScanner scanner = new MenuInputScanner(readyMenu);
        scanner.setMessage("Do you want to play some BlackJack?????");
        int answerChoice = prompt.getUserInput(scanner);

        switch (answerChoice) {

            case 1:
                readyToPlay = true;
                break;
            case 2:

                break;
        }

        getStartingMoney();
    }

    public void makeBet() {

        int currentBet = 1;
        bet(currentBet);
        house.setTableMoney(currentBet);

    }

    public void messageToAll(String whatToSay) throws IOException {

        synchronized (house.getPlayerList()) {

            for (int i = 0; i < house.getPlayerList().size(); i++) {

                if (house.getPlayerList().get(i).clientSocket.getOutputStream() != clientSocket.getOutputStream()) {

                    house.getPlayerList().get(i).clientSocket.getOutputStream().write(whatToSay.getBytes());

                }
            }
        }
    }

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
        System.out.println(getName()+"current balance: " + getMoney());
        messageToSelf(getName()+"current balance: " + getMoney()+"\n");
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
}


