package com.company;

import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.abs;

public class AgentBlue {
    private Board board;
    private byte playerTurn;
    public AgentBlue(Board board){
        this.board = board;
    }

    public Move doMinMax(Tile[][] tiles, byte playerTurn) {
        Pair temp = max(tiles, playerTurn, (byte) (0));
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

    private int evaluate(Tile[][] currentBoard, byte currentColor) {
//        System.out.println(playerTurn);
        short score = 0;
        for (byte i = 0; i < currentBoard.length; i++) {
            for (byte j = 0; j < currentBoard.length; j++) {
                if (currentBoard[i][j].color == playerTurn) {
                    score += i;
                    score += j;
                } else if (currentBoard[i][j].color == (3 - playerTurn)) {

                    score -= (7 - i);
                    score -= (7 - j);

                }
            }
        }

//        score *= 10;
//        List<Move> moves = createPossibleMoves(currentBoard, playerTurn);
//        for(Move move : moves) {
//            if(move.startPos.x + move.startPos.y > 3 && move.finalPos.x + move.finalPos.y <= 3){
//                score += 5;
//            }
//            else if(move.startPos.x + move.startPos.y >  move.finalPos.x + move.finalPos.y) {
//                score += 4;
//            } else if (move.startPos.x + move.startPos.y <  move.finalPos.x + move.finalPos.y) {
//                score -= 3;
//            }
//        }
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