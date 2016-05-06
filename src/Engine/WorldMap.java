/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import Engine.Forces.ForceCalculator;
import Engine.Forces.ForceCalculatorWorker;
import Engine.Forces.MoveForcesHelperWorker;
import Engine.Forces.ForceDescription;
import Engine.Forces.IForcesHolder;
import Engine.Forces.MapDrawer;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 *
 * @author Tomus
 */
public class WorldMap implements IForcesHolder{
    public double[][]Map = null;
    public ArrayList<IRenderableGameObject>[][]ObjectsMap = null;
    
    public double flatlness = 32;
    public double pickHeights = 32;
    private int heightMapToMapDevideFactor = 16;
    public int mapSize = 16;//4096;//16;
    public byte[] LUT = null;
    
        ArrayList<ForceDescription>forces = new ArrayList<ForceDescription>();
    Random random = new Random();
    int[][] heightMap = null;
    public int[][][]map;
    public int[][][]forceMap;
    int width = mapSize / heightMapToMapDevideFactor;//256
    int height = mapSize / heightMapToMapDevideFactor;//256
    int imageSize = width * height;

    int sourcesCount = 24;//25;
    //max ranodm value of force source
    int maxRandom = 1024;
    //max raius of fource source
    int maxRadius = 256;
    boolean startForceCalculator = false;
    
    public int getMapSize()
    {
        return (mapSize -1);
    }
    public Color G = new Color(10, 255, 10);
    private double cooling = 5;
   
    
    
    public void removeObject(int X, int Y, IRenderableGameObject o)
    {
        ObjectsMap[X][Y].remove(o);
    }
    
    public void updateObjectPosition(int oldX, int oldY, int newX, int newY, IRenderableGameObject o)
    {
        if (oldX == -1)
        {
            ObjectsMap[newX][newY].add(o);
        }
        else if (oldX != newX || oldY != newY)
        {
            ObjectsMap[oldX][oldY].remove(o);
            ObjectsMap[newX][newY].add(o);
        }
    }
    
    public int GetGroundLevel(double[]position)
    {
        return 0;
    }
    
    public static double DoubleModulo(double a, double b)
    {
        return (a - Math.floor(a / b) * b);
    }
    
    static public double linear(double x, double x0, double x1, double y0, double y1)
    {
        if ((x1 - x0) == 0)
        {
            return (y0 + y1) / 2;
        }
        return y0 + (x - x0) * (y1 - y0) / (x1 - x0);
    }
    
    
    double sign(double[]p1, double[] p2, double[] p3)
    {
        return (p1[0] - p3[0]) * (p2[1] - p3[1]) - (p2[0] - p3[0]) * (p1[1] - p3[1]);
    }

    boolean PointInTriangle (double[] pt, double[] v1, double[] v2, double[] v3)
    {
    boolean b1, b2, b3;

    b1 = sign(pt, v1, v2) < 0.0f;
    b2 = sign(pt, v2, v3) < 0.0f;
    b3 = sign(pt, v3, v1) < 0.0f;

    return ((b1 == b2) && (b2 == b3));
}
    public double GetGroundPosition(Vector3D position, WorldMap worldMap)
    {
        return GetGroundPosition(new double[]{position.getX(),position.getY(), position.getZ()}, worldMap);
    }
    
    public double GetGroundPosition(double[]position, WorldMap worldMap)
    {
        double retX = 0;
        double retY = 0;
        double x = (int)position[0]/GenerateTerrain.Size;
        double y = (int)position[1]/GenerateTerrain.Size;
        
        double[]p11 = {x,y, (double)worldMap.Map[(int)x][(int)y]};
        double[]p12 = {x,y+1, (double)worldMap.Map[(int)x][(int)y+1]};
        double[]p21 = {x+1,y, (double)worldMap.Map[(int)x+1][(int)y]};
        double[]p22 = {x+1,y+1, (double)worldMap.Map[(int)x+1][(int)y+1]};
        if (PointInTriangle(position, p11, p12, p22))
        {
            Vector3D v1 = new Vector3D(p12[0] - p11[0], p12[1] - p11[1],p12[2] - p11[2]);
            Vector3D v2 = new Vector3D(p22[0] - p11[0], p22[1] - p11[1],p22[2] - p11[2]);
            Vector3D n = Vector3D.crossProduct(v1, v2);
            double z = ((n.getX() * x) + (n.getY() * y) + ((-n.getX() * p11[0]) + (-n.getY() * p11[1]) + (-n.getZ() * p11[2]))) / (-n.getZ());
            return z;
            //return (z3(x-x1)(y-y2) + z1(x-x2)(y-y3) + z2(x-x3)(y-y1) - z2(x-x1)(y-y3) - z3(x-x2)(y-y1) - z1(x-x3)(y-y2))
              //  / (  (x-x1)(y-y2) +   (x-x2)(y-y3) +   (x-x3)(y-y1) -   (x-x1)(y-y3) -   (x-x2)(y-y1) -   (x-x3)(y-y2));
        }
        else
        {
            Vector3D v1 = new Vector3D(p21[0] - p11[0], p21[1] - p11[1],p21[2] - p11[2]);
            Vector3D v2 = new Vector3D(p22[0] - p11[0], p22[1] - p11[1],p22[2] - p11[2]);
            Vector3D n = Vector3D.crossProduct(v1, v2);
            double z = ((n.getX() * x) + (n.getY() * y) + ((-n.getX() * p11[0]) + (-n.getY() * p11[1]) + (-n.getZ() * p11[2]))) / (-n.getZ());
            return z;
        }
        //screen.worldMap.Map[x][y];
    }
    
    public void CorrectPosition(double[]position, Screen screen)
    {
        double x = (double)(position[0]);
        double y = (double)(position[1]);


        x = DoubleModulo(x,(screen.worldMap.mapSize -1)* GenerateTerrain.Size);
        y = DoubleModulo(y,(screen.worldMap.mapSize -1)* GenerateTerrain.Size);

        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (x >= (screen.worldMap.mapSize -1)* GenerateTerrain.Size)
            x = (screen.worldMap.mapSize -1)* GenerateTerrain.Size - 0.01;
        if (y >= (screen.worldMap.mapSize -1)* GenerateTerrain.Size)
            y = (screen.worldMap.mapSize -1)* GenerateTerrain.Size - 0.01;
        
        position[0] = x;
        position[1] = y;
    }
    
        public Vector3D CorrectPosition(Vector3D position, Screen screen)
    {
        double x = (double)(position.getX());
        double y = (double)(position.getY());


        x = DoubleModulo(x,(screen.worldMap.mapSize -1)* GenerateTerrain.Size);
        y = DoubleModulo(y,(screen.worldMap.mapSize -1)* GenerateTerrain.Size);

        return new Vector3D(x,y, position.getZ());
    }
    

        
    public WorldMap()
    {
        Map = new double[mapSize][];
        
        for (int a = 0; a < mapSize; a++)
        {
            Map[a] = new double[mapSize];
        }
        //z jakiegoś powodu tu było -1, ale już nie wiem dlaczego
        /*
        ObjectsMap = new ArrayList[mapSize-1][];
        for (int a = 0; a < mapSize-1; a++)
        {
            ObjectsMap[a] = new ArrayList[mapSize-1];
        }
        for (int x = 0; x < mapSize-1; x++) {
	            for(int y = 0; y < mapSize-1; y++) {
                        ObjectsMap[x][y] = new ArrayList<IRenderableGameObject>();
                    }
        }*/
        ObjectsMap = new ArrayList[mapSize][];
        for (int a = 0; a < mapSize; a++)
        {
            ObjectsMap[a] = new ArrayList[mapSize];
        }
        for (int x = 0; x < mapSize; x++) {
	            for(int y = 0; y < mapSize; y++) {
                        ObjectsMap[x][y] = new ArrayList<IRenderableGameObject>();
                    }
        }
        
        for (int x = 0; x < mapSize; x++) {
	            for(int y = 0; y < mapSize; y++) {
                        //ObjectsMap[x][y] = new ArrayList<IRenderableGameObject>();
                        Map[x][y] = NoiseHelper(x, y);
                        double min = Math.min(Math.abs(mapSize - x - 1), Math.abs(x));
                        min = Math.min(min, Math.abs(mapSize - y - 1));
                        min = Math.min(min, Math.abs(y));
                        if (min < cooling)
                        {
                            Map[x][y] *= min / cooling; 
                        }
                    }
                }
        /*
        Map[1][1] = 10;
        Map[3][1] = 20;
        Map[5][1] = 30;
        Map[7][1] = 40;
        Map[9][1] = 50;
        Map[11][1] = 60;*/
        
        
        
        if (startForceCalculator)
        {
            BufferedImage bufferedImage;
            try {
                ClassLoader classloader = Thread.currentThread().getContextClassLoader();
                InputStream is = classloader.getResourceAsStream("./Images/LUT-1.bmp");
            
                ///img = ImageIO.read(new File("/Images/face.jpg"));
                bufferedImage = ImageIO.read(is);
                is.close();
                
                
                //bufferedImage = ImageIO.read(new File("e:\\Projects\\java 3d tutorial\\Game\\LUT-1.bmp"));
                LUT = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
            } catch (IOException ex) {
                Logger.getLogger(MapDrawer.class.getName()).log(Level.SEVERE, null, ex);
            }


            ForceCalculator fc = new ForceCalculator(width, height);

            for (int a = 0; a < sourcesCount; a++)
            {
                int x = random.nextInt(width);
                int y = random.nextInt(height);
                int[]fv = new int[4];
                int which = random.nextInt(4);
                fv[which] = random.nextInt(maxRandom);
                //fv[1] = random.nextInt(maxRandom);
                //fv[2] = random.nextInt(maxRandom);
                //fv[3] = random.nextInt(maxRandom);
                int[]maxR = new int[4];
                for (int b = 0; b < maxR.length; b++)
                    maxR[b] = maxRadius;
                ForceDescription fd = new ForceDescription(x, y, fv, width, height, maxR);
                forces.add(fd);
            }

            map = fc.map;
            forceMap = fc.forceMap;
            heightMap = fc.heighMap;

            HeightMapToMap();

            ForceCalculatorWorker fcw = new ForceCalculatorWorker(fc, this);
            fcw.start();
            MoveForcesHelperWorker mfhw = new MoveForcesHelperWorker(forces, width, height);
            mfhw.start();
        }
    }
    
    private double NoiseHelper(double x, double y)
    {
        return SimplexNoise.noise(x / flatlness ,y / flatlness) * pickHeights;
    }

    @Override
    public ArrayList<ForceDescription> getForces()
    {
        synchronized(forces)
        {
            ArrayList<ForceDescription> forcesCopy = new ArrayList<ForceDescription>();
            for (int a = 0; a < forces.size(); a++)
            {
                forcesCopy.add(forces.get(a).clone());
            }
        }
        return forces;
    }

    @Override
    public void setForceMap(int[][][] forceMap) {
        this.forceMap = forceMap;
    }

    @Override
    public void setMap(int[][][] map) {
        this.map = map;
    }

    
    void CorrectMap(double[][]heighMap)
    {
        int bX = heighMap.length -1;
        for (int a = 0; a < heighMap.length; a++)
        {
            //if (heighMap[a][0] != heighMap[a][bX])
                heighMap[a][bX] = heighMap[a][0];
                heighMap[a][bX-1] = heighMap[a][0];
        }
        
        int bY = heighMap[0].length -1;
        for (int a = 0; a < heighMap[0].length; a++)
        {
                heighMap[bY][a] = heighMap[0][a];
                heighMap[bY-1][a] = heighMap[0][a];
        }
        /*
        for (int a = 0; a < heighMap.length; a++)
        {
            if (heighMap[a][0] != heighMap[a][1])
                heighMap[a][1] = heighMap[a][0];
        }
        int bY = heighMap[0].length -1;
        for (int a = 0; a < heighMap[0].length; a++)
        {
            if (heighMap[0][a] != heighMap[bY][a])
                heighMap[bY][a] = heighMap[0][a];
        }
        for (int a = 0; a < heighMap[0].length; a++)
        {
            if (heighMap[0][a] != heighMap[1][a])
                heighMap[1][a] = heighMap[0][a];
        }*/
            //for (int b = 0; b < heighMap[0].length; b++)
            //{
                
            //}
    }
    
    private void HeightMapToMap()
    {
        for (int a = 0; a < Map.length; a++)
            for (int b = 0; b < Map[0].length; b++)
            {
                //Map[a][b] = heightMap[a/4][b/4];
                //Map[a][b] = (heightMap[a/4 + 1][b/4 + 1] + heightMap[a/4 + 1][b/4 + 1]) * ;
                int idA = a / heightMapToMapDevideFactor;
                int idB = b / heightMapToMapDevideFactor;
                double comp1 = heightMap[idA][idB];
                double comp2 = heightMap[idA][idB];
                double comp3 = heightMap[idA][idB];
                double comp4 = heightMap[idA][idB];
                if (idA + 1 < heightMap.length)
                {
                    comp2 = heightMap[idA + 1][idB];
                    comp4 = heightMap[idA + 1][idB];
                    if (idB + 1 < heightMap[0].length)
                        comp4 = heightMap[idA + 1][idB + 1];
                }
                if (idB + 1 < heightMap[0].length)
                {
                    comp3 = heightMap[idA][idB + 1];
                    comp4 = heightMap[idA][idB + 1];
                    if (idA + 1 < heightMap.length)
                        comp4 = heightMap[idA + 1][idB + 1];
                }
                Map[a][b] = (comp1 * ((double)heightMapToMapDevideFactor - (double)(a % heightMapToMapDevideFactor)) + 
                        comp2 * (double)(a % heightMapToMapDevideFactor) + 
                        comp3 * ((double)heightMapToMapDevideFactor - (double)(b % heightMapToMapDevideFactor)) + 
                        comp4 * (double)(b % heightMapToMapDevideFactor)) / (2.0 * (double)heightMapToMapDevideFactor);
            }
        CorrectMap(Map);
    }
    
    @Override
    public void setHeightMap(int[][] heightMap) {
        this.heightMap = heightMap;
        HeightMapToMap();
        CalculateMiniMap();
    }

    @Override
    public void VisualizeData() {
    }
    
    public Color getColor(int MapIdX, int MapIdY)
    {
        if (!startForceCalculator)
            return G;
        //pixels[i] = LUT[heightMap[id1][id2] * 3 + 2];
            //pixels[i+1] = LUT[heightMap[id1][id2] * 3 + 1];
            //pixels[i+2] = LUT[heightMap[id1][id2] * 3 + 0];
        /*System.out.print((int)(LUT[heightMap[MapIdX / 4][MapIdY / 4] * 3 + 2] & 0xFF)
        + " " + (int)(LUT[heightMap[MapIdX / 4][MapIdY / 4] * 3 + 1] & 0xFF)
        + " " + (int)(LUT[heightMap[MapIdX / 4][MapIdY / 4] * 3 + 0] & 0xFF));*/
        Color c = new Color((int)(LUT[(int)Map[MapIdX][MapIdY] * 3 + 2] & 0xFF),
                (int)(LUT[(int)Map[MapIdX][MapIdY] * 3 + 1] & 0xFF), 
                (int)(LUT[(int)Map[MapIdX][MapIdY] * 3] & 0xFF));
        return c;
    }
    
    public int player_xx = 0;
    public int player_yy = 0;
    
    public void CalculateMiniMap()
    {
        int[] pixels = new int[imageSize * 3];
        for (int i = 0, j = 0; i < imageSize * 3; i += 3, j++)
        {
            int id2 = (int)(j / height);
            int id1 = (int)(j % width);
            //int color = map[id1][id2][c];
            //int index = LUT[(int)heightMap[id1][id2]];
            /*pixels[i] = (int)heightMap[id1][id2];
            pixels[i+1] = (int)heightMap[id1][id2];
            pixels[i+2] = (int)heightMap[id1][id2];*/
            //(int)heightMap[id1][id2] + 2
            pixels[i] = LUT[heightMap[id1][id2] * 3 + 2];
            pixels[i+1] = LUT[heightMap[id1][id2] * 3 + 1];
            pixels[i+2] = LUT[heightMap[id1][id2] * 3 + 0];
        }
        int xx = (int)(player_xx / (GenerateTerrain.Size * heightMapToMapDevideFactor));
        int yy = (int)(player_yy / (GenerateTerrain.Size * heightMapToMapDevideFactor));
        for (int a = xx - 2; a <= xx + 2; a++)
            for (int b = yy - 2; b <= yy + 2; b++)
            {
                int xxx = a % width;
                int yyy = b % height;
                if (xxx < 0)
                    xxx = width + xxx;
                if (yyy < 0)
                    yyy = height + yyy;
                pixels[((yyy * height) + xxx) * 3] = 255;
                pixels[((yyy * height) + xxx) * 3 + 1] = 0;
                pixels[((yyy * height) + xxx) * 3 + 2] = 0;
            }
        //if (labelsArray.size() < 1)
          //  panel.add( createImageLabel(pixels) );
        //else
        {
            updateImageLabel(pixels, 0);
        }
    }
    BufferedImage miniMapImage = null;
    private void updateImageLabel(int[] pixels, int labelId)
    {
        miniMapImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = miniMapImage.getRaster();
        raster.setPixels(0, 0, width, height, pixels);
        //JLabel label = labelsArray.get(labelId);
        //label.setIcon(new ImageIcon(image));
    }
    
}
