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
public class ForceDescription {
    int x = 0;
    int y = 0;
    double []values;
    int []maxD;
    public static int MaxValue = 255;
    double maxDistance = 0;
    static double log(double x, double base)
    {
        return Math.log(x) / Math.log(base);
    }
    
    public ForceDescription clone()
    {
        return new ForceDescription(x, y, values, maxDistance, maxD);
    }
    
    public ForceDescription(int x, int y, double[]forceValues, double maxDistance, int[]maxRadius)
    {
        this.x = x;
        this.y = y;
        values = new double[4];
        maxD = new int[4];
        values[0] = forceValues[0];
        values[1] = forceValues[1];
        values[2] = forceValues[2];
        values[3] = forceValues[3];
        
        double aaa = Math.log10(2) * Math.log10(2);
        aaa = Math.log10(10) * Math.log10(2);
        this.maxDistance = maxDistance;
        for (int a = 0; a < maxD.length; a++)
        {
            maxD[a] = 1;
            if (maxRadius != null)
            {
                while (forceCalculatr(values[a], maxD[a]) > 1 && maxD[a] <= maxDistance && maxD[a] <= maxRadius[a])
                {
                    maxD[a]++;
                }
                maxD[a]--;
            }
            else
            {
                while (forceCalculatr(values[a], maxD[a]) > 1 && maxD[a] <= maxDistance)
                {
                    maxD[a]++;
                }
                maxD[a]--;
            }
        }
        int z = 0;
        z++;
    }
    
    public ForceDescription(int x, int y, int[]forceValues, int width, int height, int[]maxRadius)
    {
        this.x = x;
        this.y = y;
        values = new double[4];
        maxD = new int[4];
        values[0] = forceValues[0];
        values[1] = forceValues[1];
        values[2] = forceValues[2];
        values[3] = forceValues[3];
        
        double aaa = Math.log10(2) * Math.log10(2);
        aaa = Math.log10(10) * Math.log10(2);
        maxDistance = Math.sqrt((double)(width * width) + (double)(height + height));
        for (int a = 0; a < maxD.length; a++)
        {
            maxD[a] = 1;
            if (maxRadius != null)
            {
                while (forceCalculatr(values[a], maxD[a]) > 1 && maxD[a] <= maxDistance && maxD[a] <= maxRadius[a])
                {
                    maxD[a]++;
                }
                maxD[a]--;
            }
            else
            {
                while (forceCalculatr(values[a], maxD[a]) > 1 && maxD[a] <= maxDistance)
                {
                    maxD[a]++;
                }
                maxD[a]--;
            }
        }
        int z = 0;
        z++;
    }
    
    double forceCalculatr(double value, double distance)
    {
        //return value / (log(distance + 1, 2) * distance);
        //return value / distance;
        //double returnValue = value / log(distance + 2, 2);
        double returnValue = value / (distance + 1);
        if (returnValue > MaxValue)
            returnValue = MaxValue;
        return returnValue;
    }
    
    public void PutForceOnForceMap(int[][][]forceMap)
    {
        int w = forceMap.length;
        int h = forceMap[0].length;
        int d = forceMap[0][0].length;
        
        for (int c = 0; c < maxD.length; c++)
            for (int a = x - maxD[c]; a <= x + maxD[c]; a++)
                for (int b = y - maxD[c]; b <= y + maxD[c]; b++)
                {
                    int idA = a;
                    int idB = b;
                    //if (a >= 0 && a < forceMap.length)
                      //  if (b >= 0 && b < forceMap[0].length)
                    if (idA < 0)
                    {
                        idA = idA % forceMap.length;
                        idA = forceMap.length + idA;
                    }
                    if (idB < 0)
                    {
                        idB = idB % forceMap[0].length;
                        idB = forceMap[0].length + idB;
                    }
                    if (idA >= forceMap.length)
                    {
                        idA = idA % forceMap.length;
                    }
                    if (idB >= forceMap[0].length)
                    {
                        idB = idB % forceMap[0].length;
                    }
                        {
                            double distance = Math.sqrt(((x - a) * (x - a)) + ((b - y) * (b - y)));
                            if (distance <= maxD[c])
                            {
                                /*if (idA >= 128 || idA >= 128)
                                {
                                    int z = 0;
                                    z++;
                                }*/
                                double value = forceCalculatr(values[c], distance);
                                if (forceMap[idA][idB][c] < value)
                                    forceMap[idA][idB][c] = (int)value;
                                //System.out.println(a + " " + b + " " + idA + " " + idB);
                            }   
                                    
                        }
                }
        
        /*for (int a = 0; a < forceMap.length; a++)
        {
            for (int b = 0; b < forceMap[a].length; b++)
            {
                for (int c = 0; c < forceMap[a][b].length; c++)
                {
                
                }
            }
        }*/
    }
}
