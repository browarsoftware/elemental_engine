/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Forces;

import Engine.GaussianSmooth;
import java.util.ArrayList;
import java.util.Random;
import org.jtransforms.fft.DoubleFFT_2D;

/**
 *
 * @author STUDENT
 */
public class ForceCalculator {
    public int[][][]map;
    public int[][][]forceMap;
    public int[][]heighMap;
    Random random = new Random();
    //number of iterations in each step
    int iterationsCount = 1;
    //gaussian kernel size
    int kernelSize = 20;
    double theta = 5;
    //
    int medianFilterWidth = 9; 
    int medianFilteHeight = 9;
    
    public ForceCalculator(int width, int height)
    {
        map = new int[width][][];
        for (int a = 0; a < width; a++)
        {
            map[a] = new int[height][];
            for (int b = 0; b <height; b++)
            {
                map[a][b] = new int[4];
                for (int c = 0; c < 4; c++)
                {
                    map[a][b][c] = 64;
                }
            }
        }
        
        forceMap = new int[width][][];
        for (int a = 0; a < width; a++)
        {
            forceMap[a] = new int[height][];
            for (int b = 0; b <height; b++)
            {
                forceMap[a][b] = new int[4];
                for (int c = 0; c < 4; c++)
                {
                    forceMap[a][b][c] = 0;
                }
            }
        }
        
        heighMap = new int[width][height];
    }
    
    public void Calculate(ArrayList<ForceDescription>forces)
    {
        for (int a = 0; a < forceMap.length; a++)
        {
            for (int b = 0; b < forceMap[0].length; b++)
            {
                for (int c = 0; c < forceMap[0][0].length; c++)
                {
                    forceMap[a][b][c] = 0;
                    //map[a][b][c] = 0;
                }
            }
        }
        for (int a = 0; a < forces.size(); a++)
        {
            ForceDescription fd = forces.get(a);
            fd.PutForceOnForceMap(forceMap);
        }
        for (int a = 0; a < iterationsCount; a++)
        {
            calculateMapChange(map, forceMap, 0);
            calculateMapChange(map, forceMap, 1);
            calculateMapChange(map, forceMap, 2);
            calculateMapChange(map, forceMap, 3);
        }
        calculateHeightMap(heighMap, map);
    }
    
    void CorrectHeightMap(int[][]heighMap)
    {
        int bX = heighMap.length -1;
        for (int a = 0; a < heighMap.length; a++)
        {
            if (heighMap[a][0] != heighMap[a][bX])
                heighMap[a][bX] = heighMap[a][0];
        }
        int bY = heighMap[0].length -1;
        for (int a = 0; a < heighMap[0].length; a++)
        {
            if (heighMap[0][a] != heighMap[bY][a])
                heighMap[bY][a] = heighMap[0][a];
        }
            //for (int b = 0; b < heighMap[0].length; b++)
            //{
                
            //}
    }
    
    void calculateHeightMap(int[][]heighMap, int[][][]map)
    {
        for (int a = 0; a < heighMap.length; a++)
            for (int b = 0; b < heighMap[0].length; b++)
            {
                int value = (int)(128.0 - (map[a][b][0]/2.0) + (map[a][b][1]/2.0) - (map[a][b][2]/2.0) + (map[a][b][3]/2.0));
                if (value < 0)
                    value = 0;
                if (value > 255)
                    value = 255;
                heighMap[a][b] = value;
            }
        //heighMap = GaussianReturnInt(heighMap);
        //int[][]heighMapHelp = medianFilter(heighMap, medianFilterWidth, medianFilteHeight);
        int[][]heighMapHelp = MedianFilter.Calculate(heighMap, heighMap.length, heighMap[0].length,medianFilterWidth, medianFilteHeight);
        GaussFilter.Calculate(heighMapHelp, heighMap, theta);
        for (int a = 0; a < heighMap.length; a++)
            for (int b = 0; b < heighMap[0].length; b++)
            {
                //heighMap[a][b] = heighMapHelp[a][b];
                if (heighMap[a][b] < 0)
                    heighMap[a][b] = 0;
                if (heighMap[a][b] > 255)
                    heighMap[a][b] = 255;
            }
        CorrectHeightMap(heighMap);
    }
    
    void calculateMapChangeGradientInPoint(int[][][]map, int[][][]forceMap, int x, int y, double[][][]gradient, int z)
    {
        int x1 = x - 1;
        int x2 = x + 1;
        int y1 = y - 1;
        int y2 = y + 1;
        
        if (x1 < 0)
            x1 = map.length -1;
        if (y1 < 0)
            y1 = map[0].length -1;
        if (x2 >= map.length)
            x2 = 0;
        if (y2 >= map.length)
            y2 = 0;

        gradient[x][y][0] = (double)(forceMap[x2][y][z] - forceMap[x1][y][z]) / 2.0;
        gradient[x][y][1] = (double)(forceMap[x][y2][z] - forceMap[x][y1][z]) / 2.0;
    }
    
    void calculateMapChangeGradientInNeighberhood(int[][][]map, int[][][]forceMap, int x, int y, double[][][]gradient, int z)
    {
        for (int a = x-1; a <= x + 1; a++)
            for (int b = y - 1; b <= y + 1; b++)
            {
                //if (!(a == 0 && b == 0))
                {
                    int ai = a;
                    int bi = b;
                    if (ai < 0)
                        ai = map.length -1;
                    if (bi < 0)
                        bi = map[0].length -1;
                    if (ai >= map.length)
                        ai = 0;
                    if (bi >= map.length)
                        bi = 0;
                    calculateMapChangeGradientInPoint(map, forceMap, ai, bi, gradient, z);
                }
            }
    }
    
    void calculateMapChange(int[][][]map, int[][][]forceMap, int z)
    {
        double[][][]gradient = new double[map.length][][];
        for (int a = 0; a < map.length; a++)
        {
            gradient[a] = new double[map[0].length][];
            for (int b = 0; b < map[0].length; b++)
            {
                gradient[a][b] = new double[2];
            }
        }
        
        for (int a = 0; a < map.length; a++)
        {
            for (int b = 0; b < map[0].length; b++)
            {
                //if (forceMap[a][b][0] > map[a][b][0])
                if (forceMap[a][b][z] > 0)
                    calculateMapChangeGradientInNeighberhood(map, forceMap, a, b, gradient, z);
                /*for (int c = 0; c < map[0][0].length; c++)
                {
                    if (forceMap[a][b][c] > map[a][b][c])
                    gradient[a][b][0] = map[a+1][b][0] - map[a-1][b][0];
                }*/
            }
        }
        /*
        DoubleFFT_2D fft = new DoubleFFT_2D(gradient.length, gradient[0].length);

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
        
        fft.complexForward(gg);
        fft.complexForward(gk);
        for (int a = 0; a < gg.length; a++)
            for (int b = 0; b < gg[0].length / 2; b++)
            {
                gg[a][b * 2] = (gg[a][b * 2] * gk[a][b * 2]) - (gg[a][b * 2 + 1] * gk[a][b * 2 + 1]);
                gg[a][b * 2 + 1] = (gg[a][b * 2 + 1] * gk[a][b * 2]) + (gg[a][b * 2] * gk[a][b * 2 + 1]);
            }
        
        fft.complexInverse(gg, true);
        for (int a = 0; a < gradient.length; a++)
            for (int b = 0; b < gradient[0].length; b++)
            {
                gradient[a][b][0] = Math.sqrt((gg[a][2 * b]*gg[a][2 * b]) + (gg[a][(2 * b) + 1] * gg[a][(2 * b) + 1]));
            }
        
        gg = new double[gradient.length][2 * gradient[0].length];
        for (int a = 0; a < gradient.length; a++)
            for (int b = 0; b < gradient[0].length; b++)
            {
                gg[a][2 * b] = gradient[a][b][1];
                //gg[a][2 * b + 1] = 0;
            }
        fft.complexForward(gg);
        for (int a = 0; a < gg.length; a++)
            for (int b = 0; b < gg[0].length / 2; b++)
            {
                gg[a][b * 2] = (gg[a][b * 2] * gk[a][b * 2]) - (gg[a][b * 2 + 1] * gk[a][b * 2 + 1]);
                gg[a][b * 2 + 1] = (gg[a][b * 2 + 1] * gk[a][b * 2]) + (gg[a][b * 2] * gk[a][b * 2 + 1]);
            }
        
        fft.complexInverse(gg, true);
        for (int a = 0; a < gradient.length; a++)
            for (int b = 0; b < gradient[0].length; b++)
            {
                gradient[a][b][1] = Math.sqrt((gg[a][2 * b]*gg[a][2 * b]) + (gg[a][(2 * b) + 1] * gg[a][(2 * b) + 1]));
            }*/
        
        Gaussian(gradient, 0);
        Gaussian(gradient, 1);

        for (int a = 0; a < map.length; a++)
            for (int b = 0; b < map[0].length; b++)
            {
                double dx = gradient[a][b][0];
                double dy = gradient[a][b][1];

                int idx = (int)(Math.round(dx * 2 + (double)a));
                int idy = (int)(Math.round(dy * 2 + (double)b));
                
                //idx = a + idx;
                //idy = b + idy;

                if (idx < 0)
                    idx = map.length -1;
                if (idy < 0)
                    idy = map[0].length -1;
                if (idx >= map.length)
                    idx = 0;
                if (idy >= map.length)
                    idy = 0;
                
                //if map has nonzero value
                if (map[idx][idy][z] > 0&& (Math.abs(dx) > 0.5 || Math.abs(dy) > 0.5))
                {
                    //"suck" the value
                    map[idx][idy][z]--;
                    map[a][b][z]++;
                    //return some random value
                    int whichToReturn = random.nextInt(4);
                    int trials = 10; 
                    while (whichToReturn == z && map[idx][idy][whichToReturn] > 0 && trials > 0)
                    {
                        whichToReturn = random.nextInt(4);
                        trials--;
                    }
                    if (trials == 0)
                        whichToReturn = 0;

                    //return to have constance sum
                    map[idx][idy][whichToReturn]++;
                    map[a][b][whichToReturn]--;
                }
            }
        
    }
    
    private int[][]GaussianReturnInt(int[][]array)
    {
        double[][]gg = new double[array.length][array[0].length];
        for (int a = 0; a < array.length; a++)
            for (int b = 0; b < array[0].length; b++)
            {
                gg[a][b] = array[a][b];
            }
        gg = GaussianSmooth.smooth_in_round_table(gg, array.length, array[0].length, kernelSize, theta);
        int[][]gg2 = new int[array.length][array[0].length];
        for (int a = 0; a < array.length; a++)
            for (int b = 0; b < array[0].length; b++)
            {
                gg2[a][b] = (int)gg[a][b];
                if (gg2[a][b] < 0)
                    gg2[a][b] = 0;
                if (gg2[a][b] > 255)
                    gg2[a][b] = 255;
            }
        return gg2;
    }
    
    private double[][]Gaussian(int[][][] array, int z)
    {
        double[][]gg = new double[array.length][array[0].length];
        for (int a = 0; a < array.length; a++)
            for (int b = 0; b < array[0].length; b++)
            {
                gg[a][b] = array[a][b][z];
            }
        
        //return Convolution.convolution2DPadded(gg, array.length, array[0].length, getGaussianKernel(), 5, 5);
        //return GaussianSmooth.smooth(gg, array.length, array[0].length, kernelSize, theta);
        return GaussianSmooth.smooth_in_round_table(gg, array.length, array[0].length, kernelSize, theta);
    }
    
    private void Gaussian(double[][][] array, int z)
    {
        double[][]gg = new double[array.length][array[0].length];
        for (int a = 0; a < array.length; a++)
            for (int b = 0; b < array[0].length; b++)
            {
                gg[a][b] = array[a][b][z];
            }
        
        //return Convolution.convolution2DPadded(gg, array.length, array[0].length, getGaussianKernel(), 5, 5);
        //gg = GaussianSmooth.smooth(gg, array.length, array[0].length, 10, 1);
        
        //gg = GaussianSmooth.smooth_in_round_table(gg, array.length, array[0].length, kernelSize, theta);
        double[][]resultGG = new double[array.length][array[0].length];
        GaussFilter.Calculate(gg, resultGG, 5);
        
        for (int a = 0; a < array.length; a++)
            for (int b = 0; b < array[0].length; b++)
            {
                array[a][b][z] = resultGG[a][b];
            }
    }
    
    /**
     * Median filtering. Requires width and height of image, array of image image[], width and height of filtering window.
     * @param width
     * @param height
     * @param image
     * @param widthAr
     * @param heightAr
     * @param array
     * @return
     */
	public static int[][] medianFilter (int [][]image,
			int widthAr, int heightAr) {
		int elHeight = heightAr;
		int elWidth = widthAr;

		int vCenter = elWidth / 2;
		int hCenter = elHeight / 2;

		int indRow, indCol;
		
		int [] medianHelp = new int[widthAr * heightAr];
		int [][] imageArray = image;
		int [][] destImageArray = new int[image.length][image[0].length];

		MedianValue mv = null;
		String aaa = new String();
		int ileEle = 0;
		for (int row = 0; row < image.length; row++) {
			for (int col = 0; col < image[0].length; col++) {
				ileEle = 0;
				for (int i = 0; i < elHeight; i++) {
					for (int j = 0; j < elWidth; j++) {
						indRow = row + i - vCenter;
						indCol = col + j - hCenter;

                                                if (indRow < 0)
                                                {
                                                    indRow = indRow % image.length;
                                                    indRow = image.length + indRow;
                                                }
                                                if (indCol < 0)
                                                {
                                                    indCol = indCol % image[0].length;
                                                    indCol = image[0].length + indCol;
                                                }
                                                if (indRow >= image.length)
                                                {
                                                    indRow = indRow % image.length;
                                                }
                                                if (indCol >= image[0].length)
                                                {
                                                    indCol = indCol % image[0].length;
                                                }
                                                
						//if (indRow >= 0 && indRow < image.length &&
						//		indCol >= 0 && indCol < image[0].length) 
                                                {
								ileEle++;
								if (mv == null) {
									mv = new MedianValue();
									mv.value = imageArray[indRow][indCol];
								}
								//else mv = AddToList(mv, imageArray[indRow * width + indCol]);
								medianHelp[i * elWidth + j] = imageArray[indRow][indCol];
						}
					}
				}
				/*(for (int a = 0; a < (ileEle) / 2; a++) {
						mv = mv.next;
				}*/
					SortingAlgorithms.quickSort(medianHelp);
				//destImageArray[row * width + col] = mv.value;
					destImageArray[row][col] = medianHelp[medianHelp.length / 2];
			}
		}
	return destImageArray;
	}
	
	private static MedianValue AddToList(MedianValue mv, int value) {
		MedianValue help = mv, help2 = null;
		while (help != null && help.value < value) {
			help2 = help;
			help = help.next;
		}
		if (help != mv) {
			help2.next = new MedianValue();
			help2.next.value = value;
			help2.next.next = help;
			return mv;
		}
		else {
			help2 = new MedianValue();
			help2.next = mv;
			help2.value = value;
			return help2;
		}
	}
}
