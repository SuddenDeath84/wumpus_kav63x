package com.wumpus;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class WumpusApp extends Application {
    private static final int TILE_SIZE = 100;
    private static final int SIZE = 4;

    private World world;
    private Player player;
    private Canvas canvas;

    @Override
    public void start(Stage primaryStage) {
        world = new World();
        player = new Player();

        canvas = new Canvas(SIZE * TILE_SIZE, SIZE * TILE_SIZE);
        drawWorld();

        // Gombok
        Button up = new Button("⬆");
        Button down = new Button("⬇");
        Button left = new Button("⬅");
        Button right = new Button("➡");
        Button shoot = new Button("🎯");

        up.setOnAction(e -> { player.move(2); afterMove(); });
        down.setOnAction(e -> { player.move(3); afterMove(); });
        left.setOnAction(e -> { player.move(1); afterMove(); });
        right.setOnAction(e -> { player.move(0); afterMove(); });
        shoot.setOnAction(e -> {
            boolean hit = world.shootArrow(player.getX(), player.getY());
            System.out.println("You shot an arrow! Hit? " + hit);
            drawWorld();
        });

        HBox controls = new HBox(10, left, up, down, right, shoot);
        controls.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setCenter(canvas);
        root.setBottom(controls);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Hunt the Wumpus - JavaFX Text Render");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void afterMove() {
        if (world.hasWumpus(player.getX(), player.getY()) || world.hasPit(player.getX(), player.getY())) {
            System.out.println("You died!");
        } else if (world.hasGold(player.getX(), player.getY())) {
            System.out.println("You found the gold!");
        }
        drawWorld();
    }

    private void drawWorld() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFont(new Font(40));

        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                gc.setFill(Color.BEIGE);
                gc.fillRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                gc.setStroke(Color.GRAY);
                gc.strokeRect(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

                if (world.hasPit(x, y)) {
                    gc.setFill(Color.BLUE);
                    gc.fillText("H", x * TILE_SIZE + 35, y * TILE_SIZE + 60);
                }
                if (world.hasGold(x, y)) {
                    gc.setFill(Color.GOLD);
                    gc.fillText("G", x * TILE_SIZE + 35, y * TILE_SIZE + 60);
                }
                if (world.hasWumpus(x, y)) {
                    gc.setFill(Color.RED);
                    gc.fillText("W", x * TILE_SIZE + 35, y * TILE_SIZE + 60);
                }
            }
        }

        // Player
        gc.setFill(Color.GREEN);
        gc.fillText("P", player.getX() * TILE_SIZE + 35, player.getY() * TILE_SIZE + 60);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
