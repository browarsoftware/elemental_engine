package Engine;

import java.awt.Color;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;

public class GenerateTerrain {

    public static TexturePaint tp = null;
    public static BufferedImage img = null;
    Random r;
    //static double roughness = 4;
    //static double pickHeights = 4;
    static int mapSize = 48;
	static double Size = 8;
	static Color G = new Color(10, 255, 10);
//	static Color G = new Color(120, 100, 80);

      /*  
    private double NoiseHelper(double x, double y)
    {
        return SimplexNoise.noise((x + 0.5) / ((double)mapSize / roughness ),(y + 0.5) / ((double)mapSize / roughness)) * pickHeights;
    }*/
    
        public static Object syncObj = new Object();
        
    public static ArrayList<RenderableGameBojectsHelper> GenerateVisualization(WorldMap worldMap, double[]ViewForm, Screen screen)
    {
        int x = (int)(screen.player.ViewFrom[0]/ GenerateTerrain.Size);
        int y = (int)(screen.player.ViewFrom[1]/ GenerateTerrain.Size);
        int i = 0;
        ArrayList<RenderableGameBojectsHelper>gameObjectsToDraw = new ArrayList<RenderableGameBojectsHelper>();
        int screenSize = mapSize / 2;
        
        synchronized(syncObj)
        {
        for (int xx = -screenSize, ix = 0; xx <= screenSize - 1; xx++, ix++)
            for (int yy = -screenSize, iy = 0; yy <= screenSize - 1; yy++, iy++)
            {
                int compX = x + xx;
                int compY = y + yy;
                /*if (compX < 0)
                    compX *= -1;
                if (compY < 0)
                    compY *= -1;*/

                //int index = (compX * GenerateTerrain.mapSize * 2) + (compY * 2);
                int index = (ix * GenerateTerrain.mapSize * 2) + (iy * 2);
                
                int MapIdX = compX;
                int MapIdY = compY;
                
                MapIdX %= (worldMap.getMapSize());
                MapIdY %= (worldMap.getMapSize());
                if (MapIdX < 0)
                    MapIdX = worldMap.getMapSize() + MapIdX;
                if (MapIdY < 0)
                    MapIdY = worldMap.getMapSize() + MapIdY;
                
                if (worldMap.ObjectsMap[MapIdX][MapIdY].size() > 0)
                {
                    /*
                    int[] idx = new int[2];
                    idx[0] = MapIdX;
                    idx[1] = MapIdY;
                    gameObjectsToDraw.add(idx);*/
                    for (int a = 0; a < worldMap.ObjectsMap[MapIdX][MapIdY].size(); a++)
                    {
                        RenderableGameBojectsHelper rgoh = new RenderableGameBojectsHelper();
                        IRenderableGameObject irgo = worldMap.ObjectsMap[MapIdX][MapIdY].get(a);
                        //rgoh.x =  compX - worldMap.ObjectsMap[MapIdX][MapIdY].get(a).getX();
                        //rgoh.y = compY - worldMap.ObjectsMap[MapIdX][MapIdY].get(a).getY();
                        
                        /*double[]p = new double[2];
                        p[0] = irgo.getX();
                        p[1] = irgo.getY();
                        worldMap.CorrectPosition(p, screen);
                        rgoh.x = p[0];
                        rgoh.y = p[1];*/
                        
                        //rgoh.x =  compX * Size + ((int)irgo.getX() % (compX * Size));
                        //rgoh.y = compY  * Size + ((int)irgo.getY() % (compY * Size));
                        rgoh.x =  compX * Size ;
                        rgoh.y = compY  * Size;
                        
                        double ddx = ((int)irgo.getX() % Size);
                        double ddy = ((int)irgo.getY() % Size);
                        if (!Double.isNaN(ddx))
                        {
                            rgoh.x += ddx + irgo.getX() - (int)irgo.getX();
                        }
                        if (!Double.isNaN(ddy))
                        {
                            rgoh.y += ddy + irgo.getY() - (int)irgo.getY();
                        }
                        rgoh.rgo =  irgo;
                        //if (rgoh.rgo)
                        gameObjectsToDraw.add(rgoh);
                    }
                    //gameObjectsToDraw.addAll(worldMap.ObjectsMap[MapIdX][MapIdY]);
                }
                
                //if (compX >= 0 && compY >= 0 && compX < worldMap.mapSize -1 && compY < worldMap.mapSize -1)
                {
                    DPolygon dp = Screen.DPolygons.get(index);
                    dp.x[0] = compX * Size;
                    dp.x[1] = compX * Size;
                    dp.x[2] = Size + (compX * Size);
                    
                    dp.y[0] = compY * Size;
                    dp.y[1] = Size + (compY * Size);
                    dp.y[2] = Size + (compY * Size);
                    
                    dp.z[0] = worldMap.Map[MapIdX][MapIdY];
                    dp.z[1] = worldMap.Map[MapIdX][MapIdY + 1];
                    dp.z[2] = worldMap.Map[MapIdX + 1][MapIdY + 1];
                    dp.setColor(worldMap.getColor(MapIdX, MapIdY));
                    //dp.setColor(Color.YELLOW);
                    
                    dp = Screen.DPolygons.get(index + 1);
                    dp.x[0] = compX * Size;
                    dp.x[1] = Size + (compX * Size);
                    dp.x[2] = Size + (compX * Size);
                    
                    dp.y[0] = compY * Size;
                    dp.y[1] = Size + (compY * Size);
                    dp.y[2] = compY * Size;
                    
                    dp.z[0] = worldMap.Map[MapIdX][MapIdY];
                    dp.z[1] = worldMap.Map[MapIdX + 1][MapIdY + 1];
                    dp.z[2] = worldMap.Map[MapIdX + 1][MapIdY];
                    
                    dp.setColor(worldMap.getColor(MapIdX, MapIdY));
                    //dp.setColor(Color.YELLOW);
                }
            }
        
        }
        return gameObjectsToDraw;
    }
    
    public GenerateTerrain() {
        for (int x = 0; x < mapSize; x++) {
            for(int y = 0; y < mapSize; y++) {
                //here give names to ground types
                Screen.DPolygons.add(new DPolygon(new double[]{(Size * x), (Size * x), Size + (Size * x)}, new double[]{(Size * y), Size + (Size * y), Size + (Size * y)}, new double[]{0, 0, 0}, G, false, null));
                Screen.DPolygons.add(new DPolygon(new double[]{(Size * x), Size + (Size * x), Size + (Size * x)}, new double[]{(Size * y), Size + (Size * y), (Size * y)}, new double[]{0, 0, 0}, G, false, null));
            }
        }
        
        try {
            //img = ImageIO.read(new File("e:\\Projects\\java 3d tutorial\\JavaApplication8\\Face.jpg"));
            //URL url = getClass().getResource("/Images/face.jpg");
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("./Images/face.jpg");
            
            ///img = ImageIO.read(new File("/Images/face.jpg"));
            img = ImageIO.read(is);
            is.close();
            
        } catch (IOException e) {
        }

        Rectangle2D r2d = new Rectangle2D.Double(0,0,img.getWidth(), img.getHeight());
        tp = new TexturePaint(img, r2d);

    }
}
