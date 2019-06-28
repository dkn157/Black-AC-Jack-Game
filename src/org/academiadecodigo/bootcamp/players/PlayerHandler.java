package org.academiadecodigo.bootcamp.players;

import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.integer.IntegerInputScanner;
import org.academiadecodigo.bootcamp.scanners.integer.IntegerRangeInputScanner;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;



public class PlayerHandler extends Gamer implements Runnable {

    private Socket clientSocket;
    private Prompt prompt;



    public PlayerHandler(Socket clientSocket) {

        this.clientSocket=clientSocket;
    }

    @Override
    public void run() {

        System.out.println("aqui");

        try {
            System.out.println("aqui tambem");
            prompt = new Prompt(clientSocket.getInputStream(), new PrintStream(clientSocket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringInputScanner nameQuestion = new StringInputScanner();
        nameQuestion.setMessage("What is your name?\n");
        setName(prompt.getUserInput(nameQuestion));

        IntegerInputScanner ageQuestion = new IntegerRangeInputScanner(18, 100);
        ageQuestion.setMessage("\nHow old are you?\n");
        setAge(prompt.getUserInput(ageQuestion));

        }
    }

