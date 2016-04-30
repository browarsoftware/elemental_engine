/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Forces;

import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class MoveForcesHelperWorker extends Thread
    {
        ArrayList<ForceDescription> forces = null;
        Random r = new Random();
        int[]dirX;
        int[]dirY;
        int width = 0;
        int height = 0;
        public MoveForcesHelperWorker(ArrayList<ForceDescription> forces, int width, int height)
        {
            this.forces = forces;
            dirX = new int[forces.size()];
            dirY = new int[forces.size()];
            for (int a = 0; a < forces.size(); a++)
            {
                dirX[a] = r.nextInt(3) -1;
                dirY[a] = r.nextInt(3) -1;
            }
            this.width = width;
            this.height = height;
        }
        
        public void run()
        {
            do
            {
                MoveForcesHelper();
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MapDrawer.class.getName()).log(Level.SEVERE, null, ex);
                }
            } while(true);
        }
        
        private void MoveForcesHelper()
        {
            synchronized(forces)
            {
                for (int a = 0; a < forces.size(); a++)
                {
                    forces.get(a).x += dirX[a];
                    forces.get(a).y += dirY[a];
                    if (forces.get(a).x >= width)
                        forces.get(a).x = 0;
                    if (forces.get(a).y >= height)
                        forces.get(a).y = 0;
                    if (forces.get(a).x < 0)
                        forces.get(a).x = width - 1;
                    if (forces.get(a).y < 0)
                        forces.get(a).y = height - 1;
                }

            }
        }
    }