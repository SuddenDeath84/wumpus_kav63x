package com.wumpus;

import java.util.Random;

public class World {
    private static final int SIZE = 4;
    private char[][] map;
    private boolean wumpusAlive = true;

    public World() {
        map = new char[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                map[i][j] = '.';

        Random rand = new Random();
        int[] w = randomEmptyCell();
        map[w[0]][w[1]] = 'W';
        int[] p = randomEmptyCell();
        map[p[0]][p[1]] = 'P';
        int[] g = randomEmptyCell();
        map[g[0]][g[1]] = 'G';
    }

    private int[] randomEmptyCell() {
        Random rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(SIZE);
            y = rand.nextInt(SIZE);
        } while (map[x][y] != '.' || (x == 0 && y == 0));
        return new int[] {x, y};
    }

    public boolean hasWumpus(int x, int y) {
        return map[x][y] == 'W' && wumpusAlive;
    }

    public boolean hasPit(int x, int y) {
        return map[x][y] == 'P';
    }

    public boolean hasGold(int x, int y) {
        return map[x][y] == 'G';
    }

    public boolean shootArrow(int x, int y) {
        for (int i = x + 1; i < SIZE; i++) {
            if (map[i][y] == 'W') {
                wumpusAlive = false;
                return true;
            }
        }
        return false;
    }

    public double[] getSensors(int x, int y) {
        double stench = 0, breeze = 0, glitter = 0;

        if (inBounds(x, y) && map[x][y] == 'G') {
            glitter = 1;
        }

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (Math.abs(dx) + Math.abs(dy) != 1) continue;
                int nx = x + dx;
                int ny = y + dy;
                if (inBounds(nx, ny)) {
                    if (map[nx][ny] == 'W' && wumpusAlive) stench = 1;
                    if (map[nx][ny] == 'P') breeze = 1;
                }
            }
        }

        return new double[] {
            stench, breeze, glitter,
            x / (double) SIZE,
            y / (double) SIZE
        };
    }

    private boolean inBounds(int x, int y) {
        return x >= 0 && y >= 0 && x < SIZE && y < SIZE;
    }
}
