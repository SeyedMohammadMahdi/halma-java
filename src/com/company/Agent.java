package com.company;

import java.util.LinkedList;
import java.util.List;

public class Agent {
    private Board board;
    private byte playerTurn;
    public Agent(Board board){
        this.board = board;
    }

    public Move doMinMax(Tile[][] tiles, byte playerTurn) {
        Pair temp = max(tiles, playerTurn, (byte) (0));
        this.playerTurn = playerTurn;
        return temp.move;
    }

    public Move doMinMax2(Tile[][] tiles, byte playerTurn) {
        Pair temp = max2(tiles, playerTurn, (byte) (0),Integer.MAX_VALUE);
        this.playerTurn = playerTurn;
        return temp.move;
    }
    private Pair max(Tile[][] currentBoard, byte currentColor, byte depth) {

        if (checkTerminal(currentBoard))
            return new Pair(null, Integer.MIN_VALUE);

        // check depth here
        List<Move> possibleMoves = createPossibleMoves(currentBoard, currentColor);
//        System.out.println(possibleMoves.size());
        if (depth == Halma.maxDepth) return new Pair(possibleMoves.get(0), evaluate(currentBoard, currentColor));


        // write your codes here
        Move bestMove = null;
        int valueMax = Integer.MIN_VALUE;
        for (Move move : possibleMoves) {
            Tile[][] clone = this.board.cloneBoard(currentBoard);
            clone = this.board.doMove(move, clone);
            currentColor = (byte)(3 - currentColor);
            Pair p = min(clone, currentColor, (byte)(depth + 1));
            if(p.value > valueMax){
                valueMax = p.value;
                bestMove = move;
            }
        }
        // return pair(move, value)
//        System.out.println(valueMax);
        return new Pair(bestMove, valueMax);
    }

    private Pair min(Tile[][] currentBoard, byte currentColor, byte depth) {

        if (checkTerminal(currentBoard))
            return new Pair(null, Integer.MAX_VALUE);

        List<Move> possibleMoves = createPossibleMoves(currentBoard, currentColor);
//        System.out.println(possibleMoves.size());

        if (depth == Halma.maxDepth) return new Pair(possibleMoves.get(0), evaluate(currentBoard, currentColor));

        // write your codes here


        // write your codes here
        Move bestMove = null;
        int valueMin = Integer.MAX_VALUE;
        for (Move move : possibleMoves) {
            Tile[][] clone = this.board.cloneBoard(currentBoard);
            clone = this.board.doMove(move, clone);
            currentColor = (byte)(3 - currentColor);
            Pair p = max(clone, currentColor, (byte)(depth + 1));
            if(p.value < valueMin){
                valueMin = p.value;
                bestMove = move;
            }
        }
        // return pair(move, value)
//        System.out.println(valueMin);
        return new Pair(bestMove, valueMin);
    }

    private Pair max2(Tile[][] currentBoard, byte currentColor, byte depth,int loc) {

        if (checkTerminal(currentBoard))
            return new Pair(null, Integer.MIN_VALUE);

        // check depth here
        List<Move> possibleMoves = createPossibleMoves(currentBoard, currentColor);
//        System.out.println(possibleMoves.size());
        if (depth == Halma.minDepth) return new Pair(possibleMoves.get(0), -evaluate(currentBoard, currentColor));


        // write your codes here
        Move bestMove = null;
        int valueMax = Integer.MIN_VALUE;
        for (Move move : possibleMoves) {
            Tile[][] clone = this.board.cloneBoard(currentBoard);
            clone = this.board.doMove(move, clone);
            currentColor = (byte)(3 - currentColor);
            Pair p = min2(clone, currentColor, (byte)(depth + 1),valueMax);
            if(loc<p.value)return new Pair(null,Integer.MAX_VALUE);
            if(p.value > valueMax){
                valueMax = p.value;
                bestMove = move;
            }
        }
        // return pair(move, value)
//        System.out.println(valueMax);
        return new Pair(bestMove, valueMax);
    }

    private Pair min2(Tile[][] currentBoard, byte currentColor, byte depth,int loc) {

        if (checkTerminal(currentBoard))
            return new Pair(null, Integer.MAX_VALUE);

        List<Move> possibleMoves = createPossibleMoves(currentBoard, currentColor);
//        System.out.println(possibleMoves.size());

        if (depth == Halma.minDepth) return new Pair(possibleMoves.get(0), -evaluate(currentBoard, currentColor));

        // write your codes here


        // write your codes here
        Move bestMove = null;
        int valueMin = Integer.MAX_VALUE;
        for (Move move : possibleMoves) {
            Tile[][] clone = this.board.cloneBoard(currentBoard);
            clone = this.board.doMove(move, clone);
            currentColor = (byte)(3 - currentColor);
            Pair p = max2(clone, currentColor, (byte)(depth + 1),valueMin);
            if(loc>p.value)return new Pair(null,Integer.MIN_VALUE);
            if(p.value < valueMin){
                valueMin = p.value;
                bestMove = move;
            }
        }
        // return pair(move, value)
//        System.out.println(valueMin);
        return new Pair(bestMove, valueMin);
    }

    private int evaluate(Tile[][] currentBoard, byte currentColor) {
        short score = 0;
        for (byte i = 0; i < currentBoard.length; i++) {
            for (byte j = 0; j < currentBoard.length; j++) {
                if (currentBoard[i][j].color == playerTurn) {
                    if (playerTurn == 2 && i + j > 3) {
                        score += (3 - i);
                        score += (3 - j);
                    }
                    else {
                        score += (7 - i);
                        score += (7 - j);
                    }
                } else if (currentBoard[i][j].color == (3 - playerTurn)) {

                    score -= i;
                    score -= j;

                }
            }
        }

        if (playerTurn == 2 && playerTurn == currentColor) {
            List<Move> moves  = createPossibleMoves(currentBoard, currentColor);
            for (Move move : moves) {
                if (move.startPos.x + move.startPos.y > move.finalPos.x + move.finalPos.y) {
                    score += (move.startPos.x + move.startPos.y) - (move.finalPos.x + move.finalPos.y);
                }
            }
        }
        return score;
    }

    public List<Move> createPossibleMoves(Tile[][] newBoard, int currentColor) {
        List<Move> possibleMoves = new LinkedList<>();
        for (byte i = 0; i < 8; i++)
            for (byte j = 0; j < 8; j++)
                if (newBoard[i][j].color == currentColor) {
                    List<Tile> legalTiles = new LinkedList<>();
                    board.findPossibleMoves(newBoard, newBoard[i][j], legalTiles, newBoard[i][j], true);
                    for (Tile tile : legalTiles)
                        possibleMoves.add(new Move(newBoard[i][j], tile));
                }
        return possibleMoves;
    }


    public boolean checkTerminal(Tile[][] currentTiles) {

        byte redCounter = 0;
        byte blueCounter = 0;

        for (byte x = 0; x < 8; x++) {
            for (byte y = 0; y < 8; y++) {
                if (currentTiles[x][y].zone == 1) {
                    if (currentTiles[x][y].color == 2) {
                        redCounter++;
                        if (redCounter >= 10) {
                            return true;
                        }
                    }
                } else if (currentTiles[x][y].zone == 2) {
                    if (currentTiles[x][y].color == 1) {
                        blueCounter++;
                        if (blueCounter >= 10) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}