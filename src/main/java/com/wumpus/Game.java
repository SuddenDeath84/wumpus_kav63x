package com.wumpus;

public class Game {
    private World world;
    private Player player;
    private WumpusAI ai;

    public Game() {
        this.world = new World();
        this.player = new Player();
        this.ai = new WumpusAI();
    }

    public void play() {
        int step = 0;
        while (!player.isDead() && !player.hasWon() && step < 30) {
            double[] sensors = world.getSensors(player.getX(), player.getY());

            int action;
            if (Math.random() < 0.7) {
                action = ai.chooseAction(sensors); // AI dönt
            } else {
                action = (int) (Math.random() * 5); // véletlen mozgás/lövés
            }

            ai.collectTrainingExample(sensors, action); // példagyűjtés

            if (action == 4) {
                boolean hit = world.shootArrow(player.getX(), player.getY());
                System.out.println("Step " + step + ": Shot arrow. Hit? " + hit);
            } else {
                player.move(action);
                if (world.hasWumpus(player.getX(), player.getY()) || world.hasPit(player.getX(), player.getY())) {
                    player.setDead(true);
                    System.out.println("Player died!");
                    break;
                }
                if (world.hasGold(player.getX(), player.getY())) {
                    player.setHasWon(true);
                    System.out.println("Player found the gold!");
                    break;
                }
                System.out.println("Step " + step + ": Action = " + action);
            }
            step++;
        }

        // Tanítás a gyűjtött példákból
        ai.trainNetwork();
    }
}
