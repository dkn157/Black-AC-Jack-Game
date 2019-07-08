package org.academiadecodigo.bootcamp.players;

import org.academiadecodigo.bootcamp.Ascii;
import org.academiadecodigo.bootcamp.House;
import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.deck.Card;
import org.academiadecodigo.bootcamp.scanners.integer.IntegerInputScanner;
import org.academiadecodigo.bootcamp.scanners.integer.IntegerRangeInputScanner;
import org.academiadecodigo.bootcamp.scanners.menu.MenuInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;

import java.awt.*;
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
    private final int GIRL_COST = 30;
    private final int ROOM_COST = 30;
    private final int DRINK_COST = 10;
    private final int WATER_COST = 5;
    private final int SHEMALE_MONEY = 60;
    private final int BONUS_MONEY = 5;
    private final int FLOWER_COST = 5;
    private final int BOUQUET_COST = 20;
    private final int ROBBED_MONEY = 20;


    public PlayerHandler(Socket clientSocket, House house) {
        synchronized (house.getPlayerList()) {
            getStartingMoney();
            this.clientSocket = clientSocket;
            roundIsRunning = true;
            this.house = house;
            playerHand = new LinkedList<>();
            amountOfCardsHeld = 0;
            this.getStartingMoney();
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
            } catch (InterruptedException e) {
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
        scanner.setMessage("\nDo you want to buy a card?");
        int answerChoice = prompt.getUserInput(scanner);

        switch (answerChoice) {
            case 1:
                drawCard();
                messageToAll("\n" + getName() + " has bought a card.\n");
                if (getHandValue() > 21) {
                    stillWantToBuy = false;

                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    messageToAll("\n" + getName() + " will not buy more cards.\n");
                    messageToSelf("     YOU HAVE BEEN BUSTED!!\n");
                    setHandValue(0);

                }
                break;
            case 2:
                stillWantToBuy = false;
                messageToAll("\n" + getName() + " will not buy more cards.\n");
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

        System.out.println("Card draw:  " + playerHand.get(amountOfCardsHeld - 1).getThisCard() + "   Hand value:  " + getHandValue());

        System.out.println(playerHand.get(amountOfCardsHeld - 1).getCardArt());

        System.out.println("\n _______________________________________");

        messageToSelf("\n"+playerHand.get(amountOfCardsHeld - 1).getCardArt());


        messageToSelf("\nCard draw: " + playerHand.get(amountOfCardsHeld - 1).getThisCard() + "   Hand value: " + getHandValue());

        //messageToSelf(playerHand.get(amountOfCardsHeld - 1).getCardArt());
    }

    public synchronized void idQuestion() throws IOException, InterruptedException {

        StringInputScanner nameQuestion = new StringInputScanner();
        nameQuestion.setMessage("\nWhat is your name?\n");
        setName(prompt.getUserInput(nameQuestion));

        IntegerInputScanner ageQuestion = new IntegerRangeInputScanner(18, 100);
        ageQuestion.setMessage("\nHow old are you? Keep in mind no minors are allowed here.\n");
        setAge(prompt.getUserInput(ageQuestion));

        readyMenu();
    }

    public void readyMenu() throws IOException, InterruptedException {
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
                //house.letsBegin();
                playerExit();
                break;
        }

        getStartingMoney();
    }

    private void playerExit() throws IOException {
        house.removePlayer(this);
        clientSocket.close();
    }

    public void makeBet() {

        int currentBet = 10;
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

    public void playerRound() throws IOException, InterruptedException {

        //feita a aposta, dinheiro na mesa
        //jogador recebe 2 cartas, que são ao mesmo tempo tiradas do deck, tem seus valores adicionados à mão
        amountOfCardsHeld = 0;
        resetPlayerCards();
        resetHand();

        makeBet();
        System.out.println("\nCurrent balance: " + getMoney());
        messageToSelf("\nCurrent balance: " + getMoney() + "\n");
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
        int random = (int) ((Math.random()*100)+1);
        System.out.println("Random number: " + random);

        if (getMoney()<45 && random<15 || getMoney()==10) {
            earnMoney();
        }

        if(getMoney()>60 && random<15) {
            girlAppears();
        }

        if(getMoney()>0 && random<20 && random>15) {
            offerDrink();
        }

        if(getMoney()<= 9) {
            try {
                bouncerApproach();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (getMoney()>0 && random<30 && random > 20) {
            try {
                flowerSeller();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (random<30) {
            messageToAll("\nA weird smell is rising up around the table. "+getName()+ " just farted!" +
                    " Is he nervous?");
            messageToSelf("\nYou've just farted a really stinky one. Other players can smell it too.\n");
            Thread.sleep(1000);
        }
    }


    public void setReadyToPlay(boolean isItReady) {
        this.readyToPlay = isItReady;
    }

    public boolean isReadyToPlay() {
        return readyToPlay;
    }


    /**
     *
     * Ascii
     * ART
     * METHODS
     *      *
     *
     * @throws IOException
     */

    // Hooker method. Should be prompt to player if money is high
    public void girlAppears() throws IOException, InterruptedException {

        String[] options = {"Go with her", "Hell no! I'm gambling"};

        MenuInputScanner scanner = new MenuInputScanner(options);
        scanner.setMessage("A girl appears next to you, inviting you to check-in into a room\n" + Ascii.getGirl());
        int answerChoice = prompt.getUserInput(scanner);

        switch (answerChoice) {
            case 1:
                messageToSelf("\nWith animal instinct, you left the table with that sweet pie. " +
                        "\nHowever, she makes you pay for huncka\n" +
                        " \nhuncka, plus the room\n");
                messageToAll("\n" + getName() + " left the table with a fine real woman and a bump in his pants\n");
                int bill = ROOM_COST+GIRL_COST;
                pay(bill);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                messageToSelf("\nYou had some bills to pay: "+ "Total: " + bill + " (Room: " + ROOM_COST + " / Girl: " + GIRL_COST+")");
                Thread.sleep(1000);
                messageToSelf("\nYour current balance is: " + getMoney() + "\n");
                break;

            case 2:
                messageToSelf("\nNo thanks bitch! I'm here to make money!\n");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                messageToSelf("\nGirl: You will regret this, I could make all your dreams come true\n");
                messageToAll("\nBitches are crawling around the table... Hold your pants boys! "
                        + getName() + " can be a disguised pussy!\n");
                break;
        }
    }

    //earn money method. should be prompt to player if money is low
    public void earnMoney() throws IOException, InterruptedException {

        String[] options = {"\nFuck it. I really need money to gamble", "My dignity is more important\n"};

        MenuInputScanner scanner = new MenuInputScanner(options);
        scanner.setMessage("\nSince you are low on coins, a group of big boys approaches you with a proposition." +
                " Do you want to join them in the dark room?");


        int answerChoice = prompt.getUserInput(scanner);

        switch (answerChoice) {
            case 1:

                this.setMoney(getMoney() + SHEMALE_MONEY);
                messageToSelf("\nYou chose, literally, the hard way... Brave soldier\n");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                messageToSelf("\nYour dignity earned you a total of: "+SHEMALE_MONEY);
                Thread.sleep(1000);
                messageToSelf("\nYour current balance is: "+getMoney());
                Thread.sleep(1000);
                messageToSelf(Ascii.getAss());
                messageToAll("\n" +getName() + " leaves the table, heading into a dark room with some " +
                        "suspicious shemales \n");

            case 2:
                messageToSelf("\nCongrats, even though you are desperate for money" +
                        "you didn't lose your dignity, at least for now\n");
                messageToAll("\nBig applause to "+getName()+" cause he resisted a tempting " +
                        "proposal (for some)\n");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setMoney(getMoney() + BONUS_MONEY);
                messageToSelf("\nTo compensate your sad decision, you've just received "+BONUS_MONEY+"\n");
                messageToSelf("\nCurrent balance is: "+getMoney());

        }
    }

    //Drink is offered once in a while to players
    public void offerDrink() throws IOException, InterruptedException {

        String[] options = {"Whiskey - Fill it up", "A glass of tap water"};

        MenuInputScanner scanner = new MenuInputScanner(options);
        scanner.setMessage("\nWould you like something to drink?");
        int answerChoice = prompt.getUserInput(scanner);

        switch (answerChoice) {
            case 1:
                messageToSelf("\nRight away you drunk fool");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                messageToSelf(Ascii.getJack());
                Thread.sleep(1000);
                pay(DRINK_COST);
                messageToSelf("\n\nThis drink costs " + DRINK_COST + ".\n");
                Thread.sleep(1000);
                messageToSelf("Your current balance is: " + getMoney());
                messageToAll("\nThe bar is open lads, but remember.... Don't drink too much , you'll end up seeing double. ");
                Thread.sleep(1000);
                messageToAll(getName() + " is feeling a bit tipsy...\n");
                break;

            case 2:
                messageToAll("\n" + getName() + " just asked for a glass of tap water. AHAHAHHA\n");
                messageToSelf("\nYou should be ashamed of yourself...tap Water? Everyone will know how cheapskate you are\n");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                messageToSelf("\nFor being ridiculous, you will have to pay for this.\n");
                messageToSelf("\nTotal cost: " + WATER_COST + ".\n");
                pay(WATER_COST);
                Thread.sleep(1000);
                messageToSelf("\nCurrent balance:  " + getMoney() + "\n");
        }
    }

        //If you get out of money
        public void bouncerApproach() throws IOException, InterruptedException {

            String[] options = {"Become a cleaner", "Visit Shemale friend"};

            MenuInputScanner scanner = new MenuInputScanner(options);
            scanner.setMessage("\nThe Club bouncer Ricardo asks: Degenerate gambler, you have no money left to pay your debts, " +
                    "what are you you going to do?\n");
            messageToAll("\nThe Club Bouncer Ricardo is circling the table\n");


            int answerChoice = prompt.getUserInput(scanner);

            switch (answerChoice) {
                case 1:
                    earnMoney();
                    Thread.sleep(1000);
                    messageToSelf("\nThat's what happens to losers");
                    messageToAll("\n"+getName()+ " just lost his dignity\n");
                    break;

                case 2:
                    earnMoney();
                    Thread.sleep(1000);
                    messageToSelf("\nWe knew you would like more of that! Hope you can sit down during " +
                            "you next rounds\n");
                    messageToAll("\n"+getName()+" loved the dark room so much he was asking for more\n");
                    break;
            }

    }

    //Special seller
    public void flowerSeller() throws IOException, InterruptedException {

        String[] options = {"Buy a flower", "Buy a bouquet", "Send the indian away"};

        MenuInputScanner scanner = new MenuInputScanner(options);
        scanner.setMessage("\nAn Indian flower seller approaches. Quer frôôÔ? My flowers can be very special. " +
                "Chose wisely!" + Ascii.getFlower());

        messageToAll("\nQuer frôôôÔ??! An overwhelming scent of current is spreading around the room\n");

        int answerQuestion = prompt.getUserInput(scanner);

        switch (answerQuestion) {

            case 1:
                pay(FLOWER_COST);
                messageToSelf("\nYou've just bought a plastic flower, smells like curry\n");
                Thread.sleep(1000);
                messageToSelf("\nThis flowers costs: "+FLOWER_COST+".\n");
                messageToSelf("\nCurrent balance is: "+getMoney()+"\n");
                messageToAll("\n"+getName()+ " bought a smelly plastic flower");
                break;

            case 2:
                pay(BOUQUET_COST);
                messageToSelf("\nYou've just bought an incredible bouquet and you offered it to a sweetie\n");
                Thread.sleep(1000);
                messageToSelf("\nIt's free huncka huncka time\n");
                Thread.sleep(1000);
                messageToSelf(Ascii.getHooker());
                messageToAll("\n"+getName()+" went for a quickie in the bathroom\n");
                break;

            case 3:
                pay(ROBBED_MONEY);
                messageToSelf("\nYou should be nice to people. You've just got robbed for " +ROBBED_MONEY);
                Thread.sleep(1000);
                messageToSelf("\nCurrent balance: "+getMoney());
                messageToAll("\n"+getName() +" as been robbed by an Indian midget.\n");
                break;
        }
    }

} // the end
