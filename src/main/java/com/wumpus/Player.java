package com.wumpus;

public class Player {
    private int x = 0;
    private int y = 0;
    private boolean dead = false;
    private boolean hasWon = false;

    public void move(int direction) {
        switch (direction) {
            case 0 -> x++; // jobbra
            case 1 -> x--; // balra
            case 2 -> y--; // fel
            case 3 -> y++; //le
        }
        x = Math.max(0, Math.min(3, x));
        y = Math.max(0, Math.min(3, y));
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public boolean isDead() { return dead; }
    public void setDead(boolean dead) { this.dead = dead; }
    public boolean hasWon() { return hasWon; }
    public void setHasWon(boolean hasWon) { this.hasWon = hasWon; }
}
