/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

/**
 *
 * @author Tomus
 */
public class ShootProjectileBehaviour implements IProjectileFunction{

    @Override
    public void CallAfterGroundHit(Projectile projectile) {
        projectile.Remove = true;
    }
    
}
