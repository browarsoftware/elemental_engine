/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Forces;

import Engine.GaussianSmooth;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.jtransforms.fft.DoubleFFT_2D;

/**
 *
 * @author Tomus
 */
public class test {
    public static void main(String[]args)
    {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("e:\\Projects\\java 3d tutorial\\Game\\lena_color.jpg"));
            byte[]LUT = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
            
            DoubleFFT_2D fft = new DoubleFFT_2D(img.getWidth(),img.getHeight());
            double[][] ii = new double[img.getWidth()][img.getHeight()];
            double[][] ii2 = new double[img.getWidth()][img.getHeight()];
            for (int a = 0; a < img.getWidth(); a++)
                for (int b = 0; b < img.getHeight(); b++)
                {
                    int argb = img.getRGB(a, b);
                    
                    int rr = (argb)&0xFF;
                    int gg = (argb>>8)&0xFF;
                    int bb = (argb>>16)&0xFF;
                    int aa = (argb>>24)&0xFF;

                    int cc = (rr + gg + bb)/3;
                    int outC = 0xff000000 + cc | (cc << 8) | (cc << 16);
                    img.setRGB(a, b, outC);
                    ii[a][b] = cc;
                }
            
            double[][]gg = new double[ii.length][2 * ii[0].length];
            double[][]gk = new double[ii.length][2 * ii[0].length];
            for (int a = 0; a < ii.length; a++)
                for (int b = 0; b < ii[0].length; b++)
                {
                    gg[a][b * 2] = ii[a][b];
                }
            
            /*
            double theta = 1;
            int kernelSize = 10;
            
            double[][]gh = GaussianSmooth.gaussian2D(theta, kernelSize);
            for (int a = 0; a < gh.length; a++)
                for (int b = 0; b < gh[0].length; b++)
                {
                    gk[a][b * 2] = gh[a][b];
                }
            
            
            
            fft.complexForward(gg);
            fft.complexForward(gk);
            
            for (int a = 0; a < gg.length; a++)
                for (int b = 0; b < gg[0].length / 2; b++)
                {
                    gg[a][b * 2] = (gg[a][b * 2] * gk[a][b * 2]) - (gg[a][b * 2 + 1] * gk[a][b * 2 + 1]);
                    gg[a][b * 2 + 1] = (gg[a][b * 2 + 1] * gk[a][b * 2]) + (gg[a][b * 2] * gk[a][b * 2 + 1]);
                }
            
            
            fft.complexInverse(gg, true);
            fft.complexInverse(gk, true);
            */
            

            GaussFilter.Calculate(ii, ii2, 5);
            
            for (int a = 0; a < img.getWidth(); a++)
                for (int b = 0; b < img.getHeight(); b++)
                {
                    //int cc = (int)(gk[a][2 * b] * 10000.0);
                    //int cc = (int)(gg[a][2 * b]);
                    int cc = (int)(ii2[a][b]);
                    //int cc = (int)(Math.sqrt(gg[a][2 * b] * gg[a][2 * b]) + (gg[a][2 * b + 1] * gg[a][2 * b + 1]));
                    int outC = 0xff000000 + cc | (cc << 8) | (cc << 16);
                    img.setRGB(a, b, outC);
                }
            /*
            double[][]gg = new double[gradient.length][2 * gradient[0].length];
        double[][]gk = new double[gradient.length][2 * gradient[0].length];
        for (int a = 0; a < gradient.length; a++)
            for (int b = 0; b < gradient[0].length; b++)
            {
                gg[a][b * 2] = gradient[a][b][0];
            }
        
        double[][]gh = GaussianSmooth.gaussian2D(theta, kernelSize);
        for (int a = 0; a < gh.length; a++)
            for (int b = 0; b < gh[0].length; b++)
            {
                gk[a][b * 2] = gh[a][b];
            }
        
        fft.complexForward(gg);*/
            
            
            JPanel mainPanel = new JPanel(new BorderLayout());
            JFrame frame = new JFrame();

            JLabel lblimage = new JLabel(new ImageIcon(img));
            //frame.getContentPane().add(lblimage, BorderLayout.CENTER);
            //frame.setSize(300, 400);
            //frame.setVisible(true);
            
            
            mainPanel.add(lblimage);
            // add more components here
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(mainPanel);
            frame.pack();
            frame.setVisible(true);

        } catch (IOException e) {
        }
    }
}

