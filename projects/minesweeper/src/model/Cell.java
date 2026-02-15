package model; 

public class Cell {
    private boolean isMine;
    private boolean isRevealed;
    private boolean isFlagged;
    private int adjacentMines;

    public Cell() {
        this.isMine = false;
        this.isRevealed = false;
        this.isFlagged = false;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine() {
        this.isMine = true;
    }

    public boolean isRevealed() {
        return this.isRevealed;
    }


    public void setRevealed() {
        this.isRevealed = true;
    }

    public void setFlagged() {
        this.isFlagged = true;
    }

    public boolean isFlagged() {
        return this.isFlagged;
    }

    public void toggleFlagged() {
        if (this.isRevealed) {
            return;
        }
        this.isFlagged = !this.isFlagged;
    }

    public int getAdjacentMines() {
        return this.adjacentMines;
    }

    public void setAdjacentMines(int count) {
        this.adjacentMines = count;
    }
 }

