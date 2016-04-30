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
public interface IRenderableGameObject {
    public DPolygon[] GetPolygons();
    public double getX();
    public double getY();
    public double getDistance(double fromX, double fromY, double fromz, double x, double y, double z);
    //public BoundingSphere getBoundingSphere();
    public BoundingBox getBoundingBox();
    public boolean Collide(IRenderableGameObject iRenderableGameObject);
    public Object[]getOwnerId();
}
