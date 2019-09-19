package asteroids.GameObjects;

import asteroids.PolygonFactory;
import java.util.Random;

public class Asteroid extends GameObject {

    private final double angularVelocity;

    public Asteroid(int x, int y) {
        super(new PolygonFactory().createPolygon(), x, y);

        Random rand = new Random();

        super.getShape().setRotate(rand.nextInt(360));

        int accelerationCount = 1 + rand.nextInt(10);
        for (int i = 0; i < accelerationCount; i++) {
            accelerate();
        }

        this.angularVelocity = 0.5 - rand.nextDouble();
    }

    @Override
    public void move() {
        super.move();
        super.getShape().setRotate(super.getShape().getRotate() + angularVelocity);
    }
}
