/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Forces;

import java.util.ArrayList;

/**
 *
 * @author user
 */
public interface IForcesHolder {
    public ArrayList<ForceDescription> getForces();
    public void setForceMap(int[][][]forceMap);
    public void setMap(int[][][]map);
    public void setHeightMap(int[][]heightMap);
    public void VisualizeData();

}
