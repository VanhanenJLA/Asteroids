package asteroids.GameObjects;

import asteroids.Features.IShooting;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.shape.Polygon;

public class Ship extends GameObject implements IShooting {

    public List<Ammo> bullets;

    public Ship(int x, int y) {
        super(new Polygon(-5, -5, 10, 0, -5, 5), x, y);
        bullets = new ArrayList<>();
    }

    @Override
    public boolean isCooldown() {
        return false;
    }

    @Override
    public Ammo shoot() {
        Ammo bullet = new Ammo((int) getShape().getTranslateX(),
                (int) getShape().getTranslateY());
        this.bullets.add(bullet);
        bullet.getShape().setRotate(getShape().getRotate());
        bullet.accelerate();
        bullet.setVelocity(bullet.getVelocity().normalize().multiply(3));
        return bullet;
    }

    @Override
    public void move() {
        super.move();
        bullets.stream().forEach(b -> b.move());
    }

    public boolean canShoot() {
        if (isCooldown()) {
            return false;
        }
        if (bullets.size() > 999) {
            return false;
        }
        return true;
    }

}
