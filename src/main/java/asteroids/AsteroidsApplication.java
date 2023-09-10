package asteroids;

import asteroids.GameObjects.Ammo;
import asteroids.GameObjects.Asteroid;
import asteroids.GameObjects.GameObject;
import asteroids.GameObjects.Ship;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AsteroidsApplication extends Application {

    public static int WIDTH = 1280;
    public static int HEIGHT = 720;

    private Pane screen;
    private Scene scene;
    private Stage stage;

    private Map<KeyCode, Boolean> playerInputs;

    private Ship ship;
    private List<Asteroid> asteroids;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        this.stage = stage;

        createGameWindow();
        createGameObjects();
        displayGameObjects();
        setupPlayerInput();
        stage.show();

        new AnimationTimer() {
            @Override
            public void handle(long now) {

                processPlayerInput();
                moveGameObjects();
                killHitAsteroids();
                stopOnShipCollision();
                removeDeadGameObjects();
                generateAsteroids();

            }

            private void generateAsteroids() {
                if (Math.random() < 0.00) {
                    Asteroid asteroid = new Asteroid(WIDTH, HEIGHT);
                    if (!asteroid.collidedWith(ship)) {
                        asteroids.add(asteroid);
                        screen.getChildren().add(asteroid.getShape());
                    }
                }
            }

            private void removeDeadGameObjects() {
                removeDeadGameObjects(ship.bullets);
                removeDeadGameObjects(asteroids);
            }
            private <T extends GameObject> void removeDeadGameObjects(List<T> gameObjects) {
                gameObjects.stream()
                        .filter(o -> !o.isAlive())
                        .forEach(o -> screen.getChildren().remove(o.getShape()));

                gameObjects.removeAll(gameObjects.stream()
                        .filter(o -> !o.isAlive())
                        .collect(Collectors.toList()));
            }

            private void moveGameObjects() {
                moveGameObjects(asteroids);
                ship.move();
            }
            private <T extends GameObject> void moveGameObjects(List<T> gameObjects) {
                gameObjects.forEach(GameObject::move);
            }

            private void processPlayerInput() {

                if (playerInputs.getOrDefault(KeyCode.UP, false)) {
                    ship.accelerate();
                }

                if (playerInputs.getOrDefault(KeyCode.LEFT, false)) {
                    ship.turnLeft();
                }

                if (playerInputs.getOrDefault(KeyCode.RIGHT, false)) {
                    ship.turnRight();
                }

                if (playerInputs.getOrDefault(KeyCode.SPACE, false)) {

                    if (ship.canShoot()) {
                        Ammo bullet = ship.shoot();
                        screen.getChildren().add(bullet.getShape());
                    }

                }
            }

            private void stopOnShipCollision() {
                asteroids.forEach(asteroid -> {
                    if (ship.collidedWith(asteroid)) {
                        this.stop();
                    }
                });
            }

            private void killHitAsteroids() {
                ship.bullets.forEach(ammo -> {
                    asteroids.forEach(asteroid -> {
                        if (ammo.collidedWith(asteroid)) {
                            ammo.setAlive(false);
                            asteroid.setAlive(false);
                        }
                    });
                });
            }

        }.start();

    }

    private void createGameObjects() {
        ship = new Ship(150, 100);
        asteroids = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Random rnd = new Random();
            Asteroid asteroid = new Asteroid(rnd.nextInt(100), rnd.nextInt(100));
            asteroids.add(asteroid);
        }
    }

    private void displayGameObjects() {
        asteroids.forEach(asteroid -> screen.getChildren().add(asteroid.getShape()));
        screen.getChildren().add(ship.getShape());
    }

    private void setupPlayerInput() {
        playerInputs = new HashMap<>();
        scene.setOnKeyPressed(event -> {
            playerInputs.put(event.getCode(), Boolean.TRUE);
        });
        scene.setOnKeyReleased(event -> {
            playerInputs.put(event.getCode(), Boolean.FALSE);
        });
    }

    private void createGameWindow() {
        screen = new Pane();
        screen.setPrefSize(WIDTH, HEIGHT);
        scene = new Scene(screen);
        this.stage.setScene(scene);
        this.stage.setTitle("Asteroids");
    }
}
