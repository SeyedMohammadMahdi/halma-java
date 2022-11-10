package com.company;

public class Move {

    public Move(Tile startPos,Tile finalPos){
        this.startPos = startPos;
        this.finalPos = finalPos;
    }
    public Tile startPos;
    public Tile finalPos;

    public int distance() {
        return (this.finalPos.x + this.finalPos.y) - (this.startPos.x + this.startPos.y);
    }
}
