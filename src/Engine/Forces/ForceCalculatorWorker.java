/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Forces;

import java.util.ArrayList;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

/**
 *
 * @author STUDENT
 */
public class ForceCalculatorWorker extends Thread{
    ForceCalculator forceCalculator = null;
    boolean end = false;
    IForcesHolder parent = null;
    public ForceCalculatorWorker(ForceCalculator forceCalculator, IForcesHolder parent)
    {
        this.forceCalculator = forceCalculator;
        this.parent = parent;
    }
    public void run()
    {
        int count = 0;
        /*StandardDeviation sd = new StandardDeviation();
        Mean mm = new Mean();
        double[] values = new double[20];
        double startTime = System.currentTimeMillis();
        int vv = 0;*/
        do
        {
            /*if (count == 1000)
            {
                count = 0;
                values[vv] = System.currentTimeMillis() - startTime;
                vv++;
                
                int ww = forceCalculator.forceMap.length;
                int hh = forceCalculator.forceMap[0].length;
                forceCalculator.forceMap = new int[ww][][];
                
                
                forceCalculator.map = new int[ww][][];
                for (int a = 0; a < ww; a++)
                {
                    forceCalculator.map[a] = new int[hh][];
                    for (int b = 0; b <hh; b++)
                    {
                        forceCalculator.map[a][b] = new int[4];
                        for (int c = 0; c < 4; c++)
                        {
                            forceCalculator.map[a][b][c] = 64;
                        }
                    }
                }
                
                
                for (int a = 0; a < ww; a++)
                {
                    forceCalculator.forceMap[a] = new int[hh][];
                    for (int b = 0; b < hh; b++)
                    {
                        forceCalculator.forceMap[a][b] = new int[4];
                        for (int c = 0; c < 4; c++)
                        {
                            forceCalculator.forceMap[a][b][c] = 0;
                        }
                    }
                }
                
                
                startTime = System.currentTimeMillis();
                if (vv == values.length)
                {
                    System.out.println(mm.evaluate(values));
                    System.out.println(sd.evaluate(values));
                    return;
                }
            }*/
            long time = System.currentTimeMillis(); 
            ArrayList<ForceDescription>forces = parent.getForces();
            if (forceCalculator != null && forces != null)
                forceCalculator.Calculate(forces);
            parent.setForceMap(forceCalculator.forceMap);
            parent.setMap(forceCalculator.map);
            parent.setHeightMap(forceCalculator.heighMap);
            parent.VisualizeData();
            count++;
            time = System.currentTimeMillis() - time;
            System.out.println("Iterations: " + count + ", time elapsed (m.sec): " + time);
        }
        while (!end);
    }
}
