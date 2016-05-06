/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Forces;

/**
 *
 * @author user
 */
import Engine.GaussianSmooth;
import static Engine.GenerateTerrain.img;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;

public class MapDrawer extends JFrame implements IForcesHolder
{
    //map size
    int width = 256;
    int height = 256;
    int imageSize = width * height;

    int sourcesCount = 48;//24;
    //max ranodm value of force source
    int maxRandom = 1024;
    //max raius of fource source
    int maxRadius = 256;
    
    int[][] heightMap = null;
    public int[][][]map;
    public int[][][]forceMap;
    JPanel panel = null;
    ArrayList<ForceDescription>forces = new ArrayList<ForceDescription>();
    Random random = new Random();
    
    byte[]LUT = null;

    boolean randomForces = true;
    boolean moveForces = false;
    String saveFileOfGreyScale = null;//"e:\\Publikacje\\incos 2016\\tekst\\INCOS 2\\image.bmp";
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
    
    public MapDrawer()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BufferedImage bufferedImage;
        try {
            //bufferedImage = ImageIO.read(new File("e:\\Projects\\java 3d tutorial\\Game\\LUT-1.bmp"));
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            InputStream is = classloader.getResourceAsStream("./Images/LUT-1.bmp");
            
            ///img = ImageIO.read(new File("/Images/face.jpg"));
            bufferedImage = ImageIO.read(is);
            is.close();
            
            //bufferedImage = ImageIO.read(new File("Images\\LUT-1.bmp"));
            LUT = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
        } catch (IOException ex) {
            Logger.getLogger(MapDrawer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        ForceCalculator fc = new ForceCalculator(width, height);

        if (!randomForces)
                random.setSeed(1);
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

        if (panel == null)
        {
            panel = new JPanel();
            getContentPane().add( panel );
        }

        map = fc.map;
        forceMap = fc.forceMap;
        heightMap = fc.heighMap;
        visualizeData();

        ForceCalculatorWorker fcw = new ForceCalculatorWorker(fc, this);
        fcw.start();
        if (moveForces)
        {
            MoveForcesHelperWorker mfhw = new MoveForcesHelperWorker(forces, width, height);
            mfhw.start();
        }
        pack();
    }

    
    public void visualizeData()
    {
        int[] pixels = new int[imageSize * 3];
        //calculateMapChange(int[][][]map, int[][][]forceMap, int stepSize)
        /*for (int a = 0; a < map.length; a++)
            for (int b = 0; b < map[0].length; b++)
            {
                int zz = 0;
                for (int c = 0; c < map[a][b].length; c++)
                {
                    zz += map[a][b][c];
                }
                if (zz != 256)
                    zz--;
            }*/
        if (saveFileOfGreyScale != null)
        {
            
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
                pixels[i] = heightMap[id1][id2];
                pixels[i+1] = heightMap[id1][id2];
                pixels[i+2] = heightMap[id1][id2];
            }
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            WritableRaster raster = image.getRaster();
            raster.setPixels(0, 0, width, height, pixels);
            File outputfile = new File(saveFileOfGreyScale);
            try {
                ImageIO.write(image, "bmp", outputfile);
            } catch (IOException ex) {
                Logger.getLogger(MapDrawer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
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
        if (labelsArray.size() < 1)
            panel.add( createImageLabel(pixels) );
        else
        {
            updateImageLabel(pixels, 0);
        }

        
        for (int c = 0; c < 4; c++)
        {
            //double[][]mapG = Gaussian(map, c);
            for (int i = 0, j = 0; i < imageSize * 3; i += 3, j++)
            {
                int id2 = (int)(j / height);
                int id1 = (int)(j % width);
                //int color = map[id1][id2][c];
                if (c == 0)
                {
                    pixels[i] = (int)map[id1][id2][c];
                    pixels[i+1] = 0;
                    pixels[i+2] = 0;
                }
                if (c == 1)
                {
                    pixels[i] = 0;
                    pixels[i+1] = (int)map[id1][id2][c];
                    pixels[i+2] = (int)map[id1][id2][c];
                }
                if (c == 2)
                {
                    pixels[i] = (int)map[id1][id2][c];
                    pixels[i+1] = 0;
                    pixels[i+2] = (int)map[id1][id2][c];
                }
                if (c == 3)
                {
                    pixels[i] = (int)map[id1][id2][c];
                    pixels[i+1] = (int)map[id1][id2][c];
                    pixels[i+2] = 0;
                }
            }
            if (labelsArray.size() < c + 2)
                panel.add( createImageLabel(pixels) );
            else
            {
                updateImageLabel(pixels, c + 1);
            }
        }

        for (int c = 0; c < 4; c++)
        {
            
            for (int i = 0, j = 0; i < imageSize * 3; i += 3, j++)
            {
                int id2 = (int)(j / height);
                int id1 = (int)(j % width);
                //int color = map[id1][id2][c];
                if (c == 0)
                {
                    pixels[i] = forceMap[id1][id2][c];
                    pixels[i+1] = 0;
                    pixels[i+2] = 0;
                }
                if (c == 1)
                {
                    pixels[i] = 0;
                    pixels[i+1] = forceMap[id1][id2][c];
                    pixels[i+2] = forceMap[id1][id2][c];
                }
                if (c == 2)
                {
                    pixels[i] = forceMap[id1][id2][c];
                    pixels[i+1] = 0;
                    pixels[i+2] = forceMap[id1][id2][c];
                }
                if (c == 3)
                {
                    pixels[i] = forceMap[id1][id2][c];
                    pixels[i+1] = forceMap[id1][id2][c];
                    pixels[i+2] = 0;
                }
            }
            if (labelsArray.size() < c + 1 + 5)
                panel.add( createImageLabel(pixels) );
            else
            {
                updateImageLabel(pixels, c + 5);
            }
            //panel.add( createImageLabel(pixels) );
        }
    }

    ArrayList<JLabel> labelsArray = new ArrayList<JLabel>();
    
    private JLabel createImageLabel(int[] pixels)
    {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = image.getRaster();
        raster.setPixels(0, 0, width, height, pixels);
        JLabel label = new JLabel( new ImageIcon(image) );
        labelsArray.add(label);
        return label;
    }
    
    private void updateImageLabel(int[] pixels, int labelId)
    {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = image.getRaster();
        raster.setPixels(0, 0, width, height, pixels);
        JLabel label = labelsArray.get(labelId);
        label.setIcon(new ImageIcon(image));
    }

    @Override
    public void setForceMap(int[][][] forceMap) {
        this.forceMap = forceMap;
    }

    @Override
    public void setMap(int[][][] map) {
        this.map = map;
    }

    @Override
    public void setHeightMap(int[][] heightMap) {
        this.heightMap = heightMap;
    }

    @Override
    public void VisualizeData() {
        visualizeData();
    }
    
}