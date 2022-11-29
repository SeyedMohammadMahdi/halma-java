package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.swing.*;
import java.util.Hashtable;
import java.lang.Math;


public class Halma {

    private Board board;

    public static HashMap<Integer, Tranposition> states;
    private final Tile[][] tiles;

    public final static byte redDepth = 4;
    public final static byte blueDepth = 4;

    private byte playerTurn;
    private short totalMoves = 0;
    private AgentRed agentRed;
    private AgentBlue agentBlue;
    private byte firstX, firstY, secondX, secondY;

    public static int[][][] table;


    GUI gameUI = new GUI();

    public Halma() {
        tiles = new Tile[8][8];
        playerTurn = 1;
        states = new HashMap<>();
        assignCoordinates();
        table = new int[8][8][2];

        zobrist(table);
    }

    private void assignCoordinates() {
        for (byte i = 0; i < 8; i++) {
            for (byte j = 0; j < 8; j++) {
                tiles[i][j] = new Tile(i, j);

                if ((i + j) <= 3) {
                    tiles[i][j].color = 1;
                    tiles[i][j].zone = 1;
                } else if ((i + j) >= 11) {
                    tiles[i][j].color = 2;
                    tiles[i][j].zone = 2;
                }
            }
        }
    }

    private void startGame() {

        checkWinner();

        if (playerTurn == 1) {
//            doRandomAction(playerTurn);
            var move = agentBlue.doMinMax(tiles, playerTurn);
            movePiece(move);
        }
        else {
            var move = agentRed.doMinMax(tiles,playerTurn);
            if(move != null) {
                movePiece(move);
//                states.put(hash(tiles), true);
            }
            else
                doRandomAction(playerTurn);
        }

        startGame();
    }

    private void checkWinner() {
        if (agentRed.checkTerminal(tiles)) {
            gameUI.printText("\n Game has ended! \n");
            try {
                TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
            } catch (Exception ignored) {}
        }
    }

    private void doRandomAction(int playerTurn) {

        var possibleMoves = agentRed.createPossibleMoves(tiles, playerTurn);
        var random = new Random().nextInt(possibleMoves.size() - 1);
        firstX = possibleMoves.get(random).startPos.x;
        firstY = possibleMoves.get(random).startPos.y;
        secondX = possibleMoves.get(random).finalPos.x;
        secondY = possibleMoves.get(random).finalPos.y;
        movePiece(possibleMoves.get(random));
    }



    public void changeTurn(short player) {
        gameUI.printText("Player %d has ended their turn.\n", player, player);
        playerTurn = (byte) (3 - player);
    }

    public void movePiece(Move move) {
        firstX = move.startPos.x;
        firstY = move.startPos.y;
        secondX = move.finalPos.x;
        secondY = move.finalPos.y;
        tiles[secondX][secondY].color = tiles[firstX][firstY].color;
        tiles[firstX][firstY].color = 0;
        changeTurn(playerTurn);
        gameUI.updateGUI(tiles);
    }



    public void runGame() {
        board = new Board();
        agentRed = new AgentRed(board);
        agentBlue = new AgentBlue(board);
        GUI jk = new GUI();
        jk.createBoard();
        jk.createTextBoxArea();
        gameUI = jk;
        setUpGame();
        createLayout(jk);
    }

    private void createLayout(GUI jk) {
        jk.setTitle("Halma");
        jk.setVisible(true);
        jk.pack();
        jk.setSize(648, 800);
        jk.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // closes frame
        jk.setLocationRelativeTo(null);
        jk.setVisible(true); // makes HalmaBoard visible
        startGame();
    }

    public void setUpGame() {

        gameUI.setCampColors();
        gameUI.addMarbles();
        gameUI.addFrame();
    }

    public static int zobrisHash(Tile[][] tile, int depth) {
        int xorResult = 0;
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                int c = tile[i][j].color;
                if(c == 0)
                    continue;
                xorResult ^= table[i][j][c-1];
            }
        }
        xorResult ^= depth;
//        System.out.println(xorResult);
//        android.util.Log.d(TAG, "zobrisHash: ");
        return xorResult;
    }



    public static void zobrist(int [][][] table){
        for(int i=0;i<8;i++){
            for(int j=0;j<8;j++){
                int maxx = 100000000;
                int minx = 0 ;
                int x = (int)(Math.random()*(maxx-minx+1)+minx);
                int y = (int)(Math.random()*(maxx-minx+1)+minx);
                table[i][j][0] = x;
                table[i][j][1] = y;
//                System.out.println(table[i][j][1]);
            }
        }
    }

}
