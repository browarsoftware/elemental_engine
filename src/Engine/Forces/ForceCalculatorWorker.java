/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Forces;

import Engine.Forces.ForceCalculator;
import java.util.ArrayList;

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
        do
        {
            if (count == 0)
                return;
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
