/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package asteroids.Features;

/**
 *
 * @author Jouni
 */
public interface IShooting {

    public boolean isCooldown();

    public void setCooldown(boolean cooldown);

    public void shoot();

}
