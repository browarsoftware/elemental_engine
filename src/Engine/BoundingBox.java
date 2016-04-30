/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import java.awt.Color;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 *
 * @author STUDENT
 */
public class BoundingBox {
    public Vector3D bottomLeftFront = null;
    public Vector3D topRightBack = null;
    
    public BoundingBox(Vector3D bottomLeftFront, Vector3D topRightBack)
    {  
        this.bottomLeftFront = bottomLeftFront;
        this.topRightBack = topRightBack;
    }
    
    public BoundingBox(double x, double y, double z, double x1, double y1, double z1, boolean useDiagonals)
    {  
        if (useDiagonals)
        {
            double sideXLen = x1 - x;
            double sideYLen = y1 - y;
            double halfDiagonal = Math.sqrt((sideXLen * sideXLen) + (sideYLen * sideYLen)) / 2;
            double centerX = x + (sideXLen / 2.0);
            double centerY = y + (sideYLen / 2.0);
            this.bottomLeftFront = new Vector3D(centerX - halfDiagonal, centerY - halfDiagonal, z);
            this.topRightBack = new Vector3D(centerX + halfDiagonal, centerY + halfDiagonal, z1);
        }
        else
        {
            this.bottomLeftFront = new Vector3D(x, y, z);
            this.topRightBack = new Vector3D(x1, y1, z1);
        }
    }
    
    private boolean CollideAABBHelper(double x, double y, double z)
    {
        if (this.bottomLeftFront.getX() <= x && this.topRightBack.getX() >= x
            && this.bottomLeftFront.getY() <= y && this.topRightBack.getY() >= y
            && this.bottomLeftFront.getZ() <= z && this.topRightBack.getZ() >= z)
        {
            return true;
        }
        return false;
    }
    
    public boolean CollideAABB(BoundingBox boundingBox)
    {
        if (CollideAABBHelper(boundingBox.bottomLeftFront.getX(), boundingBox.bottomLeftFront.getY(), boundingBox.bottomLeftFront.getZ())) return true;
        if (CollideAABBHelper(boundingBox.bottomLeftFront.getX(), boundingBox.bottomLeftFront.getY(), boundingBox.topRightBack.getZ())) return true;
        if (CollideAABBHelper(boundingBox.bottomLeftFront.getX(), boundingBox.topRightBack.getY(), boundingBox.bottomLeftFront.getZ())) return true;
        if (CollideAABBHelper(boundingBox.bottomLeftFront.getX(), boundingBox.topRightBack.getY(), boundingBox.topRightBack.getZ())) return true;
        if (CollideAABBHelper(boundingBox.topRightBack.getX(), boundingBox.bottomLeftFront.getY(), boundingBox.bottomLeftFront.getZ())) return true;
        if (CollideAABBHelper(boundingBox.topRightBack.getX(), boundingBox.bottomLeftFront.getY(), boundingBox.topRightBack.getZ())) return true;
        if (CollideAABBHelper(boundingBox.topRightBack.getX(), boundingBox.topRightBack.getY(), boundingBox.bottomLeftFront.getZ())) return true;
        if (CollideAABBHelper(boundingBox.topRightBack.getX(), boundingBox.topRightBack.getY(), boundingBox.topRightBack.getZ())) return true;
        return false;
    }
    
    private boolean CollideAABB2DHelper(double x, double y)
    {
        if (this.bottomLeftFront.getX() <= x && this.topRightBack.getX() >= x
            && this.bottomLeftFront.getY() <= y && this.topRightBack.getY() >= y)
        {
            return true;
        }
        return false;
    }
    
    public boolean CollideAABB2D(BoundingBox boundingBox)
    {
        if (CollideAABB2DHelper(boundingBox.bottomLeftFront.getX(), boundingBox.bottomLeftFront.getY())) return true;
        if (CollideAABB2DHelper(boundingBox.bottomLeftFront.getX(), boundingBox.topRightBack.getY())) return true;
        if (CollideAABB2DHelper(boundingBox.topRightBack.getX(), boundingBox.bottomLeftFront.getY())) return true;
        if (CollideAABB2DHelper(boundingBox.topRightBack.getX(), boundingBox.topRightBack.getY())) return true;

        return false;
    }
}
