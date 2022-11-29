package com.company;

import java.util.LinkedList;
import java.util.List;

import static java.lang.Math.abs;

public class AgentRed {
    private Board board;
    private byte playerTurn;
    public AgentRed(Board board){
        this.board = board;
    }

    public Move doMinMax(Tile[][] tiles, byte playerTurn) {
        Pair temp = max(tiles, playerTurn, (byte) (0), Integer.MIN_VALUE, Integer.MAX_VALUE);
        this.playerTurn = playerTurn;
        return temp.move;
    }

//    private Pair max(Tile[][] currentBoard, byte currentColor, byte depth) {
//
//        if (checkTerminal(currentBoard))
//            return new Pair(null, Integer.MIN_VALUE);
//
//
//        List<Move> possibleMoves = createPossibleMoves(currentBoard, currentColor);
//
//        if (depth == Halma.maxDepth) return new Pair(possibleMoves.get(0), evaluate(currentBoard, currentColor));
//
//
//
//        Move bestMove = null;
//        int valueMax = Integer.MIN_VALUE;
//        for (Move move : possibleMoves) {
//            Tile[][] clone = this.board.cloneBoard(currentBoard);
//            clone = this.board.doMove(move, clone);
//            if(Halma.states.containsKey(Halma.hash(clone)))
//                continue;
//            currentColor = (byte)(3 - currentColor);
//            Pair p = min(clone, currentColor, (byte)(depth + 1));
//            if(p.value > valueMax){
//                valueMax = p.value;
//                bestMove = move;
//            }
//        }
//        return new Pair(bestMove, valueMax);
//    }

    private Pair max(Tile[][] currentBoard, byte currentColor, byte depth, int alpha, int beta) {

        if (checkTerminal(currentBoard))
            return new Pair(null, Integer.MIN_VALUE);

        // check depth here
        List<Move> possibleMoves = createPossibleMoves(currentBoard, currentColor);
//        System.out.println(possibleMoves.size());
        if (depth == Halma.redDepth) return new Pair(possibleMoves.get(0), evaluate(currentBoard, currentColor));


        // write your codes here
        Move bestMove = null;
        int valueMax = Integer.MIN_VALUE;
        for (Move move : possibleMoves) {
            Tile[][] clone = this.board.cloneBoard(currentBoard);
            clone = this.board.doMove(move, clone);
            int key = Halma.zobrisHash(clone, depth);
            if(Halma.states.containsKey(key)) {
                Tranposition now = Halma.states.get(key);
                if(beta<now.value)return new Pair(null,Integer.MAX_VALUE);
                if(now.value > valueMax){
                    valueMax = now.value;
                    bestMove = move;
                }
                continue;
            }
//          update best value by checking hashmap
            currentColor = (byte)(3 - currentColor);
            Pair p = min(clone, currentColor, (byte)(depth + 1), Math.max(valueMax, alpha), beta);
            if(beta<p.value)return new Pair(null,Integer.MAX_VALUE);
            if(p.value > valueMax){
                valueMax = p.value;
                bestMove = move;
            }
//            key = Halma.zobrisHash(clone, depth);
            Tranposition tey = new Tranposition(p.value, depth);
            Halma.states.put(key, tey);
        }
        // return pair(move, value)
//        System.out.println(valueMax);
        return new Pair(bestMove, valueMax);
    }

//    private Pair min(Tile[][] currentBoard, byte currentColor, byte depth) {
//
//        if (checkTerminal(currentBoard))
//            return new Pair(null, Integer.MAX_VALUE);
//
//        List<Move> possibleMoves = createPossibleMoves(currentBoard, currentColor);
//
//
//        if (depth == Halma.maxDepth) return new Pair(possibleMoves.get(0), evaluate(currentBoard, currentColor));
//
//
//
//
//
//        Move bestMove = null;
//        int valueMin = Integer.MAX_VALUE;
//        for (Move move : possibleMoves) {
//            Tile[][] clone = this.board.cloneBoard(currentBoard);
//            clone = this.board.doMove(move, clone);
//            if(Halma.states.containsKey(Halma.hash(clone)))
//                continue;
//            currentColor = (byte)(3 - currentColor);
//            Pair p = max(clone, currentColor, (byte)(depth + 1));
//            if(p.value < valueMin){
//                valueMin = p.value;
//                bestMove = move;
//            }
//        }
//        return new Pair(bestMove, valueMin);
//    }

    private Pair min(Tile[][] currentBoard, byte currentColor, byte depth, int alpha, int beta) {

        if (checkTerminal(currentBoard))
            return new Pair(null, Integer.MAX_VALUE);

        List<Move> possibleMoves = createPossibleMoves(currentBoard, currentColor);
//        System.out.println(possibleMoves.size());

        if (depth == Halma.redDepth) return new Pair(possibleMoves.get(0), evaluate(currentBoard, currentColor));

        // write your codes here


        // write your codes here
        Move bestMove = null;
        int valueMin = Integer.MAX_VALUE;
        for (Move move : possibleMoves) {
            Tile[][] clone = this.board.cloneBoard(currentBoard);
            clone = this.board.doMove(move, clone);
            int key = Halma.zobrisHash(clone, depth);
            if(Halma.states.containsKey(key)) {
                Tranposition now = Halma.states.get(key);
                if(alpha>now.value)return new Pair(null,Integer.MIN_VALUE);
                if(now.value > valueMin){
                    valueMin = now.value;
                    bestMove = move;
                }
                continue;
            }
            currentColor = (byte)(3 - currentColor);
            Pair p = max(clone, currentColor, (byte)(depth + 1), alpha, Math.min(valueMin, beta));
            if(alpha>p.value)return new Pair(null,Integer.MIN_VALUE);
            if(p.value < valueMin){
                valueMin = p.value;
                bestMove = move;
            }
            Tranposition tey = new Tranposition(p.value, depth);
            Halma.states.put(key, tey);
        }
        // return pair(move, value)
//        System.out.println(valueMin);
        return new Pair(bestMove, valueMin);
    }

    private int evaluate(Tile[][] currentBoard, byte currentColor) {

        short score = 0;
        for (byte i = 0; i < currentBoard.length; i++) {
            for (byte j = 0; j < currentBoard.length; j++) {
                if (abs(i - j) > 4) {
                    score-= abs(i - j);
                }
                if (currentBoard[i][j].color == playerTurn) {
                    score += (7 - i);
                    score += (7 - j);
                } else if (currentBoard[i][j].color == (3 - playerTurn)) {

                    score -= i;
                    score -= j;

                }

                if(currentBoard[i][j].zone == (3 - playerTurn) && currentBoard[i][j].color == playerTurn) {
                    score++;
                }
                else if (currentBoard[i][j].zone == playerTurn && currentBoard[i][j].color == playerTurn){
                    score--;
                }
            }
        }

        if(Halma.redDepth == 3) {
            score*=10;
        }else {
            score *= 16;
        }
        List<Move> moves = createPossibleMoves(currentBoard, playerTurn);
        for(Move move : moves) {
            if(move.startPos.x + move.startPos.y > 3 && move.finalPos.x + move.finalPos.y <= 3){
                score += 5;
            }
            else if(move.startPos.x + move.startPos.y >  move.finalPos.x + move.finalPos.y) {
                score += 4;
            } else if (move.startPos.x + move.startPos.y <  move.finalPos.x + move.finalPos.y) {
                score -= 3;
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