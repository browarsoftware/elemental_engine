/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 *
 * @author Tomus
 */
public class AlgebraUtils {
    
    public static Vector3D ReturnClosestWectorMirror(Vector3D v1, Vector3D v2, WorldMap worldMap)
    {
        return ReturnClosestWectorMirror(new double[]{v1.getX(), v1.getY(), v1.getZ()}, new double[]{v2.getX(), v2.getY(), v2.getZ()}, worldMap);
    }
    
    public static Vector3D ReturnClosestWectorMirror(double[]v1, double[] v2, WorldMap worldMap)
    {
        double x = (worldMap.mapSize -1)* GenerateTerrain.Size;
        double y = (worldMap.mapSize -1)* GenerateTerrain.Size;
        double distance = DistanceBetweenDoubleTablesMirror(v1, v2, worldMap);
        if (Math.abs(DistanceBetweenDoubleTables(new double[]{v1[0], v1[1],v1[2]}, v2) - distance) < 0.001)
            return new Vector3D(v1[0], v1[1],v1[2]);
        if (Math.abs(DistanceBetweenDoubleTables(new double[]{v1[0] + x, v1[1],v1[2]}, v2) - distance) < 0.001)
            return new Vector3D(v1[0] + x, v1[1],v1[2]);
        if (Math.abs(DistanceBetweenDoubleTables(new double[]{v1[0] + x, v1[1] + y,v1[2]}, v2) - distance) < 0.001)
            return new Vector3D(v1[0] + x, v1[1] + y,v1[2]);
        if (Math.abs(DistanceBetweenDoubleTables(new double[]{v1[0] + x, v1[1] - y,v1[2]}, v2) - distance) < 0.001)
            return new Vector3D(v1[0] + x, v1[1] - y,v1[2]);
        if (Math.abs(DistanceBetweenDoubleTables(new double[]{v1[0] - x, v1[1],v1[2]}, v2) - distance) < 0.001)
            return new Vector3D(v1[0] - x, v1[1],v1[2]);
        if (Math.abs(DistanceBetweenDoubleTables(new double[]{v1[0] - x, v1[1] + y,v1[2]}, v2) - distance) < 0.001)
            return new Vector3D(v1[0] - x, v1[1] + y,v1[2]);
        if (Math.abs(DistanceBetweenDoubleTables(new double[]{v1[0] - x, v1[1] - y,v1[2]}, v2) - distance) < 0.001)
            return new Vector3D(v1[0] - x, v1[1] - y,v1[2]);
        if (Math.abs(DistanceBetweenDoubleTables(new double[]{v1[0], v1[1] + y,v1[2]}, v2) - distance) < 0.001)
            return new Vector3D(v1[0], v1[1] + y,v1[2]);
        if (Math.abs(DistanceBetweenDoubleTables(new double[]{v1[0], v1[1] - y,v1[2]}, v2) - distance) < 0.001)
            return new Vector3D(v1[0], v1[1] - y,v1[2]);

        return Vector3D.ZERO;
    }
    
    public static double DistanceBetweenDoubleTables(Vector3D v1, Vector3D v2)
    {
        return Vector3D.distance(v1, v2);
    }
    
    public static double DistanceBetweenDoubleTablesMirror(double[]v1, double[] v2, WorldMap worldMap)
        {
            double x = (worldMap.mapSize -1)* GenerateTerrain.Size;
            double y = (worldMap.mapSize -1)* GenerateTerrain.Size;
            
            double d0 = DistanceBetweenDoubleTables(new double[]{v1[0], v1[1],v1[2]}, v2);
            
            double d1 = DistanceBetweenDoubleTables(new double[]{v1[0] + x, v1[1],v1[2]}, v2);
            double d2 = DistanceBetweenDoubleTables(new double[]{v1[0] + x, v1[1] + y,v1[2]}, v2);
            double d3 = DistanceBetweenDoubleTables(new double[]{v1[0] + x, v1[1] - y,v1[2]}, v2);
            double d4 = DistanceBetweenDoubleTables(new double[]{v1[0] - x, v1[1],v1[2]}, v2);
            
            double d5 = DistanceBetweenDoubleTables(new double[]{v1[0] - x, v1[1] + y,v1[2]}, v2);
            double d6 = DistanceBetweenDoubleTables(new double[]{v1[0] - x, v1[1] - y,v1[2]}, v2);
            double d7 = DistanceBetweenDoubleTables(new double[]{v1[0], v1[1] + y,v1[2]}, v2);
            double d8 = DistanceBetweenDoubleTables(new double[]{v1[0], v1[1] - y,v1[2]}, v2);

            return Math.min(d0, Math.min(d1, Math.min(d2, Math.min(d3, Math.min(d4, Math.min(d5, Math.min(d6, Math.min(d7, d8))))))));
        }
        
        public static double DistanceBetweenDoubleTables(double[]v1, double[] v2)
        {
            double distance = 0;
            for (int a = 0; a < v1.length; a++)
            {
                distance += (v1[a] - v2[a]) * (v1[a] - v2[a]);
            }
            return Math.sqrt(distance);
        }
}
