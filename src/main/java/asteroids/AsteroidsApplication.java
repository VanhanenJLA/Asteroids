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

    public static int WIDTH = 300;
    public static int HEIGHT = 200;

    private Pane screen;
    private Scene scene;
    private Stage stage;

    private Map<KeyCode, Boolean> playerInputs;

    private Ship ship;
    private List<Asteroid> asteroids;
    private List<Ammo> ammo;

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
                if (Math.random()
                        < 0.01) {
                    Asteroid asteroid = new Asteroid(WIDTH, HEIGHT);
                    if (!asteroid.collidedWith(ship)) {
                        asteroids.add(asteroid);
                        screen.getChildren().add(asteroid.getShape());
                    }
                }
            }

            private void removeDeadGameObjects() {
                removeDeadAmmo();
                removeDeadAsteroids();
            }

            private void removeDeadAmmo() {
                ammo.stream()
                        .filter(ammo -> !ammo.isAlive())
                        .forEach(ammo -> screen.getChildren().remove(ammo.getShape()));
                ammo.removeAll(ammo.stream()
                        .filter(ammo -> !ammo.isAlive())
                        .collect(Collectors.toList()));
            }

            private void removeDeadAsteroids() {
                asteroids.stream()
                        .filter(asteroid -> !asteroid.isAlive())
                        .forEach(asteroid -> screen.getChildren().remove(asteroid.getShape()));
                asteroids.removeAll(asteroids.stream()
                        .filter(asteroid -> !asteroid.isAlive())
                        .collect(Collectors.toList()));
            }

            private void moveGameObjects() {
                ammo.forEach(GameObject::move);
                asteroids.forEach(Asteroid::move);
                ship.move();
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

                if (playerInputs.getOrDefault(KeyCode.SPACE, false) && ammo.size() < 3) {
                    // ammutaan
                    Ammo ammus = new Ammo((int) ship.getShape().getTranslateX(),
                            (int) ship.getShape().getTranslateY());
                    ammus.getShape().setRotate(ship.getShape().getRotate());
                    ammo.add(ammus);
                    ammus.accelerate();
                    ammus.setVelocity(ammus.getVelocity().normalize().multiply(3));
                    screen.getChildren().add(ammus.getShape());
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
                ammo.forEach(ammo -> {
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
        ammo = new ArrayList<>();
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
