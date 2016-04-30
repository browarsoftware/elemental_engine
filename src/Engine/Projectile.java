/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import java.awt.Color;
import java.util.concurrent.Callable;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 *
 * @author Tomus
 */
public class Projectile implements IRenderableGameObject{
    Vector3D forces[] = null;
    Vector3D intialPosition = null;
    Vector3D actualPosition = null;
    Vector3D velocities[] = null;
    Vector3D size = null;
    public double time = 0;
    public static Vector3D gravity = new Vector3D(0,0,-10);
    public static double magicMultiplayer = 1;
    public Screen screen = null;
    public boolean Remove = false;
    
    
    public Vector3D moveDirection = Vector3D.NaN;
    IProjectileFunction projectileFunction = null;
    
    //public BoundingSphere boundingSphere = null;
    public BoundingBox boundingBox = null;
    public Object[]ownerId = null;
    
    public Projectile(Vector3D []forces, Vector3D size, Vector3D intialPosition, Vector3D velocities[], boolean affectedByGravity, Screen screen, IProjectileFunction projectileFunction, double magicMultiplayer, Object[]ownserId)
    {
        this.ownerId = ownserId;
        this.magicMultiplayer = magicMultiplayer;
        this.intialPosition = new Vector3D(intialPosition.getX(), intialPosition.getY(), intialPosition.getZ());
        this.actualPosition = new Vector3D(intialPosition.getX(), intialPosition.getY(), intialPosition.getZ());
        
        //this. boundingSphere = new BoundingSphere(actualPosition.getX(),actualPosition.getY(), actualPosition.getZ(), Math.max(Math.max(size.getX(), size.getY()), size.getZ()));
        this.boundingBox = new BoundingBox(intialPosition.getX() - (size.getX() / 2.0), intialPosition.getY() - (size.getY() / 2.0), intialPosition.getZ() - (size.getZ() / 2.0),
                intialPosition.getX() + (size.getX() / 2.0), intialPosition.getY() + (size.getY() / 2.0), intialPosition.getZ() + (size.getZ() / 2.0), true);
        
        this.screen = screen;
        this.size = size;
        this.projectileFunction = projectileFunction;
        if (velocities != null)
        {
            this.velocities = new Vector3D[velocities.length];
            for (int a = 0; a < velocities.length; a++)
                this.velocities[a] = new Vector3D(velocities[a].getX(), velocities[a].getY(), velocities[a].getZ());
        }
        int forcesCount = 0;
        if (forces != null)
        {
            forcesCount += forces.length;
            if (affectedByGravity)
                forcesCount++;
            this.forces = new Vector3D[forcesCount];
            for (int a = 0; a < forces.length; a++)
                this.forces[a] = new Vector3D(forces[a].getX(), forces[a].getY(), forces[a].getZ());
            if (affectedByGravity)
                this.forces[forcesCount -1] = new Vector3D(gravity.getX(), gravity.getY(), gravity.getZ());
        }
        else
        {
            if (affectedByGravity)
            {
                forcesCount++;
                this.forces = new Vector3D[forcesCount];
                this.forces[forcesCount -1] = new Vector3D(gravity.getX(), gravity.getY(), gravity.getZ());
            }
        }
    }
    
    
    private IRenderableGameObject CoolideWithGameObjectsOnMap(Screen screen)
    {
        int x = (int)(actualPosition.getX() / GenerateTerrain.Size);
        int y = (int)(actualPosition.getY() / GenerateTerrain.Size);
        for (int a = x - Configurations.MaximalObjectSize; a <=  x + Configurations.MaximalObjectSize; a++)
            for (int b = y - Configurations.MaximalObjectSize; b <= y + Configurations.MaximalObjectSize; b++)
            {
                int a1 = a;
                int b1 = b;
                int aCorrection = 0;
                int bCorrection = 0; 
                if (a1 < 0) 
                {
                    a1 = screen.worldMap.ObjectsMap.length + a1;
                    aCorrection = a;
                }
                if (a1 >= screen.worldMap.ObjectsMap.length)
                {
                    a1 -= screen.worldMap.ObjectsMap.length;
                    aCorrection = a;
                }
                if (b1 < 0) 
                {
                    b1 = screen.worldMap.ObjectsMap[0].length + b1;
                    bCorrection = b;
                }
                if (b1 >= screen.worldMap.ObjectsMap[0].length)
                {
                    b1 -= screen.worldMap.ObjectsMap[0].length;
                    bCorrection = b;
                }
                if (a1 < screen.worldMap.ObjectsMap.length)
                {
                    //System.out.println(a + " " + b + "," + screen.worldMap.ObjectsMap.length + " " + screen.worldMap.ObjectsMap[0].length);

                    if (b1 < screen.worldMap.ObjectsMap[a1].length)
                    {
                        for (int c = 0; c < screen.worldMap.ObjectsMap[a1][b1].size(); c++)
                        {
                            if (screen.worldMap.ObjectsMap[a1][b1].get(c) != this && !isOwner(screen.worldMap.ObjectsMap[a1][b1].get(c)))
                            {
                                BoundingBox bb = screen.worldMap.ObjectsMap[a1][b1].get(c).getBoundingBox();
                                IRenderableGameObject rgo = screen.worldMap.ObjectsMap[a1][b1].get(c);
                                //if (a < 0 || b < 0 || a >= screen.worldMap.ObjectsMap.length || b >= screen.worldMap.ObjectsMap[0].length)
                                if (aCorrection != 0 || bCorrection != 0)
                                {
                                    bb = new BoundingBox(new Vector3D(bb.bottomLeftFront.getX() + (aCorrection * GenerateTerrain.Size), bb.bottomLeftFront.getY() + (bCorrection * GenerateTerrain.Size), bb.bottomLeftFront.getZ()),
                                            new Vector3D(bb.topRightBack.getX() + (aCorrection * GenerateTerrain.Size), bb.topRightBack.getY() + (bCorrection * GenerateTerrain.Size), bb.topRightBack.getZ()));
                                }
                                if (Collide(bb))
                                    return screen.worldMap.ObjectsMap[a1][b1].get(c);
                            }
                        }
                    }
                }
            }
        return null;
    }
    
    private boolean isOwner(Object object)
    {
        if (ownerId == null) return false;
        else
        {
            for(int a = 0; a < ownerId.length; a++)
                if (ownerId[a] == object)
                    return true;
        }
        return false;
    }
    
    public IRenderableGameObject CalculatePosition(double dt, Screen screen)
    {
        IRenderableGameObject iRenderableGameObject = null;
        iRenderableGameObject = CoolideWithGameObjectsOnMap(screen);
        if (iRenderableGameObject != null)
        {
            Remove = true;
            return iRenderableGameObject;
        }
        dt *= magicMultiplayer;
        Vector3D acc = new Vector3D(0,0,0);
        if (forces != null)
            for (int a = 0; a < forces.length; a++)
            {
                acc = acc.add(forces[a]);
            }
        
        Vector3D vel = new Vector3D(0,0,0);
        if (velocities != null)
            for (int a = 0; a < velocities.length; a++)
            {
                vel = vel.add(velocities[a]);
            }
        time += dt;
        acc = acc.scalarMultiply(0.5 * time * time);
        vel = vel.scalarMultiply(time);
        moveDirection = acc.add(vel);
        
        Vector3D oldActualPosition = new Vector3D(1, actualPosition);
        BoundingBox oldBoundingBox = boundingBox; 
        actualPosition = intialPosition.add(acc).add(vel);
        actualPosition = screen.worldMap.CorrectPosition(actualPosition, screen);
        
        this.boundingBox = new BoundingBox(actualPosition.getX() - (size.getX() / 2.0), actualPosition.getY() - (size.getY() / 2.0), actualPosition.getZ() - (size.getZ() / 2.0),
                actualPosition.getX() + (size.getX() / 2.0), actualPosition.getY() + (size.getY() / 2.0), actualPosition.getZ() + (size.getZ() / 2.0), true);
        iRenderableGameObject = CoolideWithGameObjectsOnMap(screen);
        if (iRenderableGameObject != null)
        {
            actualPosition = oldActualPosition;
            boundingBox = oldBoundingBox;
            Remove = true;
            return iRenderableGameObject;
        } 
        
        
        if (projectileFunction != null && screen != null)
        {
            if (actualPosition.getZ() < screen.worldMap.GetGroundPosition(actualPosition, screen.worldMap))
            {
                projectileFunction.CallAfterGroundHit(this);
            }
        }
        //this. boundingSphere = new BoundingSphere(actualPosition.getX(),actualPosition.getY(), actualPosition.getZ(), Math.max(Math.max(size.getX(), size.getY()), size.getZ()));
        this.boundingBox = new BoundingBox(actualPosition.getX() - (size.getX() / 2.0), actualPosition.getY() - (size.getY() / 2.0), actualPosition.getZ() - (size.getZ() / 2.0),
                actualPosition.getX() + (size.getX() / 2.0), actualPosition.getY() + (size.getY() / 2.0), actualPosition.getZ() + (size.getZ() / 2.0), true);
        return null;
    }

    @Override
    public DPolygon[] GetPolygons() {
        Cube nc = null;
        if (size != null)
                {
                    nc = new Cube(actualPosition.getX() - (size.getX() / 2.0), actualPosition.getY() - (size.getY() / 2.0), actualPosition.getZ() - (size.getZ() / 2.0), 
                        size.getX(), size.getY(), size.getZ(), Color.PINK, null);
                }
                else
                {
                    nc = new Cube(actualPosition.getX() - 0.5, actualPosition.getY() - 0.5, actualPosition.getZ() - 0.5, 
                        1, 1, 1, Color.PINK, null);
                }
        return nc.GetPolygons();
    }

    @Override
    public double getX() {
        return actualPosition.getX();
    }

    @Override
    public double getY() {
        return actualPosition.getY();
    }

    @Override
    public double getDistance(double fromX, double fromY, double fromz, double x, double y, double z) {
        //correct size - instead of 1.0 use actual size
        double xx = (x - (1.0 / 2.0));
        double yy = (y - (1.0 / 2.0));
        double zz = (z - (1.0 / 2.0));
        xx = (-xx +x);
        yy = (-yy +y);
        zz = (-zz +z);
        return Math.sqrt((xx - fromX) * (xx - fromX) 
                + (yy - fromY) * (yy - fromY)
                + (zz - fromY) * (zz - fromY));
    }

    /*
    @Override
    public BoundingSphere getBoundingSphere() {
        return boundingSphere;
    }*/

    @Override
    public boolean Collide(IRenderableGameObject iRenderableGameObject) {
        if (iRenderableGameObject.getBoundingBox().CollideAABB(boundingBox) ||
            boundingBox.CollideAABB(iRenderableGameObject.getBoundingBox()))
                return true;
        return false;
    }
    
    public boolean Collide(BoundingBox boundingBox) {
        if (boundingBox.CollideAABB(this.boundingBox) ||
            this.boundingBox.CollideAABB(boundingBox))
                return true;
        return false;
    }
    
    @Override
    public Object[] getOwnerId() {
        return ownerId;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }
}
