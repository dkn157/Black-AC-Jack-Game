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

public class House {

    private ServerSocket serverSocket;
    private int myPort = 8080;
    private LinkedList<PlayerHandler> playerList;
    private Deck deck;
    private int tableMoney;
    private ExecutorService fixedPool;
    private boolean gameOver;
    private int roundCounter;
    private boolean readyToPlay;
    private int rounds;


    public House() {

        tableMoney = 0;
        playerList = new LinkedList<>();
        deck = new Deck();
        gameOver = false;
        roundCounter = 0;
        rounds = 2;

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

        String winnersNames = new String();

        for (int i = 0; i < playerList.size(); i++) {

            if (playerList.get(i).getHandValue() != maxScore) {

                for (int j = 0; j < winners.size(); j++) {
                    winnersNames += winners.get(j).getName() + " ";
                }

            }
        }
        playerList.get(0).messageToEveryoneEvenMe("\n------------- Hand Winner: " + winnersNames + "-------------\n");

        for (int i = 0; i < winners.size(); i++) {

            winners.get(i).pay(-tableMoney);

            winners.get(i).messageToSelf("\n$$$$$$$$$$$$$    You have won the round!!    $$$$$$$$$$$$$$\n");
        }

        tableMoney = 0;
    }

    public void clearLinkedList(LinkedList whatToClear) {
        for (int i = 0; i < whatToClear.size(); i++) {
            whatToClear.removeLast();
        }
    }

    public void joinGame() throws IOException {

        Socket clientSocket = serverSocket.accept();
        PlayerHandler playerHandler = new PlayerHandler(clientSocket, this);
        playerList.add(playerHandler);
        playerHandler.messageToSelf("" +
                "                                 _______                                \n" +
                "                        _,.--==###\\_/=###=-.._                         \n" +
                "                    ..-'     _.--\\\\_//---.    `-..                     \n" +
                "                 ./'    ,--''     \\_/     `---.   `\\.                  \n" +
                "               ./ \\ .,-'      _,,......__      `-. / \\.                \n" +
                "             /`. ./\\'    _,.--'':_:'\"`:'`-..._    /\\. .'\\              \n" +
                "            /  .'`./   ,-':\":._.:\":._.:\"+._.:`:.  \\.'`.  `.            \n" +
                "          ,'  //    .-''\"`:_:'\"`:_:'\"`:_:'\"`:_:'`.     \\   \\           \n" +
                "         /   ,'    /'\":._.:\":._.:\":._.:\":._.:\":._.`.    `.  \\          \n" +
                "        /   /    ,'`:_:'\"`:_:'\"`:_:'\"`:_:'\"`:_:'\"`:_\\     \\  \\         \n" +
                "       ,\\\\ ;     /_.:\":._.:\":._.:\":._.:\":._.:\":._.:\":\\     ://,        \n" +
                "       / \\\\     /'\"`:_:'\"`:_:'\"`:_:'\"`:_:'\"`:_:'\"`:_:'\\    // \\.       \n" +
                "      |//_ \\   ':._.:\":._.+\":._.:\":._.:\":._.:\":._.:\":._\\  / _\\\\ \\      \n" +
                "     /___../  /_:'\"`:_:'\"`:_:'\"`:_:'\"`:_:'\"`:_:'\"`:_:'\"'. \\..__ |      \n" +
                "      |  |    '\":._.:\":._.:\":._.:\":._.:\":._.:\":._.:\":._.|    |  |      \n" +
                "      |  |    |\":._.:\":._.:\":._.:\":._.:\":._.+\":._.:\":._.|    |  |      \n" +
                "      |  :    |_:'\"`:_:'\"`:_+'\"`:_:'\"`:_:'\"`:_:'\"`:_:'\"`|    ; |       \n" +
                "      |   \\   \\.:._.:\":._.:\":._.:\":._.:\":._.:\":._.:\":._|    /  |       \n" +
                "       \\   :   \\:'\"`:_:'\"`:_:'\"`:_:'\"`:_:'\"`:_:'\"`:_:'.'   ;  |        \n" +
                "        \\  :    \\._.:\":._.:\":._.:\":._.:\":._.:\":._.:\":,'    ;  /        \n" +
                "        `.  \\    \\..--:'\"`:_:'\"`:_:'\"`:_:'\"`:_:'\"`-../    /  /         \n" +
                "         `__.`.'' _..+'._.:\":._.:\":._.:\":._.:\":.`+._  `-,:__`          \n" +
                "      .-''    _ -' .'| _________________________ |`.`-.     `-.._      \n" +
                "_____'   _..-|| :.' .+/;;';`;`;;:`)+(':;;';',`\\;\\|. `,'|`-.      `_____\n" +
                "      .-'   .'.'  :- ,'/,',','/ /./|\\.\\ \\`,`,-,`.`. : `||-.`-._        \n" +
                "          .' ||.-' ,','/,' / / / + : + \\ \\ \\ `,\\ \\ `.`-||  `.  `-.     \n" +
                "       .-'   |'  _','<', ,' / / // | \\\\ \\ \\ `, ,`.`. `. `.   `-.       \n" +
                "                                                                       \n" +
                "                               WELCOME TO:                             \n" +

                "  ____     ____    _   _        _    ____    _____            \n" +
                " |  _ \\   / __ \\  | \\ | |      | |  / __ \\  |_   _|     /\\    \n" +
                " | |_) | | |  | | |  \\| |      | | | |  | |   | |      /  \\   \n" +
                " |  _ <  | |  | | | . ` |  _   | | | |  | |   | |     / /\\ \\  \n" +
                " | |_) | | |__| | | |\\  | | |__| | | |__| |  _| |_   / ____ \\ \n" +
                " |____/   \\____/  |_| \\_|  \\____/   \\____/  |_____| /_/    \\_\\\n" +
                "                                                              \n" +
                "                                                              \n" +

                "  ____    _                  _                    _____          _                  _    \n" +
                " |  _ \\  | |                | |          /\\      / ____|        | |                | |   \n" +
                " | |_) | | |   __ _    ___  | | __      /  \\    | |             | |   __ _    ___  | | __\n" +
                " |  _ <  | |  / _` |  / __| | |/ /     / /\\ \\   | |         _   | |  / _` |  / __| | |/ /\n" +
                " | |_) | | | | (_| | | (__  |   <     / ____ \\  | |____    | |__| | | (_| | | (__  |   < \n" +
                " |____/  |_|  \\__,_|  \\___| |_|\\_\\   /_/    \\_\\  \\_____|    \\____/   \\__,_|  \\___| |_|\\_\\\n" +
                "                                                                                         \n" +
                "                                                                                         \n" +

                "                            .-------------------.\n" +
                "                            |  W A R N I N G !  |\n" +
                "                            |                   |\n" +
                "                            |  It may contain   |\n" +
                "                            |     nudity and    |\n" +
                "                            |  violent content  |\n" +
                "                            |                   |\n" +
                "                            |        18+        |\n" +
                "                            `-------------------'\n\n"
        );

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

        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).getMoney() == overallMaxMoney) {
                thisWillBeReturned += "\n" + playerList.get(i).getName() + " finished the game with: " + playerList.get(i).getMoney() + "\n";
            }
        }

        return thisWillBeReturned;
    }

    public void letsBegin() throws IOException, InterruptedException {


        for (int i = 0; i < playerList.size(); i++) {
            if (!playerList.get(i).isReadyToPlay()) {
                readyToPlay = false;
                return;
            }
            readyToPlay = true;
        }


        if (playerList.size() > 1 && readyToPlay == true) {


            while (!gameOver) {

                for (int i = 1; i < playerList.size(); i++) {

                    playerList.get(i).messageToSelf("\nWaiting for your turn!!\n");
                }

                startRound();
                checkWhoWon();
                roundCounter++;
                if (roundCounter == rounds) {
                    for (int i = 0; i < playerList.size(); i++) {
                        playerList.get(i).setReadyToPlay(false);
                    }

                    gameOver = true;
                }
                /*try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            }
            endingMessages();

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            restardMethod();


        }

    }

    private void restardMethod() throws IOException {
        gameOver = false;
        for (int i = 0; i < playerList.size(); i++) {
            playerList.get(i).setMoney(0);
        }
        roundCounter = 0;

        //LinkedList<PlayerHandler> tempPlayerList = playerList;
        //clearLinkedList(playerList);
        for (int i = 0; i < playerList.size(); i++) {
            fixedPool.submit(playerList.get(i));
        }
        /*int howManyPlayers = playerList.size();
        for (int i = 0; i < howManyPlayers; i++) {
            playerList.get(i).readyMenu();
        }*/


    }

    public void endingMessages() throws IOException, InterruptedException {
        playerList.get(0).messageToEveryoneEvenMe(podiumMessage());

        for (int i = 0; i < playerList.size(); i++) {
            playerList.get(i).messageToSelf("\nYou finished the game with: " + playerList.get(i).getMoney() + ".\n");
            playerList.get(i).messageToSelf("\nSo, as you might have noticed, not everything here is exactly legal." +
                    "The police shows up, AGAIN, and everyone has to CLEAR OUT. Go home, rest, and if you feel like it, come back tomorrow.\n");
        }
        Thread.sleep(6000);
    }

    public void removePlayer(PlayerHandler playerHandler) {
        playerList.remove(playerHandler);
    }

} // the end










