package asteroids.GameObjects;

import asteroids.AsteroidsApplication;
import javafx.geometry.Point2D;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public abstract class GameObject {

    private final Polygon shape;
    private Point2D velocity;
    private boolean alive;

    public GameObject(Polygon polygon, int x, int y) {
        shape = polygon;
        shape.setTranslateX(x);
        shape.setTranslateY(y);
        alive = true;
        velocity = new Point2D(0, 0);
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }

    public Point2D getVelocity() {
        return velocity;
    }

    public Polygon getShape() {
        return shape;
    }

    public void turnLeft() {
        this.shape.setRotate(this.shape.getRotate() - 5);
    }

    public void turnRight() {
        this.shape.setRotate(this.shape.getRotate() + 5);
    }

    public boolean collidedWith(GameObject someObject) {
        Shape collisionArea = Shape.intersect(this.shape, someObject.getShape());
        return collisionArea.getBoundsInLocal().getWidth() != -1;
    }

    public void move() {
        this.shape.setTranslateX(this.shape.getTranslateX() + this.velocity.getX());
        this.shape.setTranslateY(this.shape.getTranslateY() + this.velocity.getY());
        wrapAroundIfOutOfBounds();
    }

    private void wrapAroundIfOutOfBounds() {
        if (this.shape.getTranslateX() < 0) {
            this.shape.setTranslateX(this.shape.getTranslateX() + AsteroidsApplication.WIDTH);
        }

        if (this.shape.getTranslateX() > AsteroidsApplication.WIDTH) {
            this.shape.setTranslateX(this.shape.getTranslateX() % AsteroidsApplication.WIDTH);
        }

        if (this.shape.getTranslateY() < 0) {
            this.shape.setTranslateY(this.shape.getTranslateY() + AsteroidsApplication.HEIGHT);
        }

        if (this.shape.getTranslateY() > AsteroidsApplication.HEIGHT) {
            this.shape.setTranslateY(this.shape.getTranslateY() % AsteroidsApplication.HEIGHT);
        }
    }

    public void accelerate() {
        double deltaX = Math.cos(Math.toRadians(this.shape.getRotate()));
        double deltaY = Math.sin(Math.toRadians(this.shape.getRotate()));

        deltaX *= 0.05;
        deltaY *= 0.05;

        this.velocity = this.velocity.add(deltaX, deltaY);
    }

}
