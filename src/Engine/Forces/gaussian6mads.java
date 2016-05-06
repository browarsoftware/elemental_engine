/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine.Forces;

/**
 *
 * @author Tomus
 */
public class gaussian6mads {
    public static double[][]calculate(double[][]imd, double sigma)
    {
        int tempx,tempy; 
        int ii,jj; 
        double q = 1.31564 * (Math.sqrt(1 + 0.490811 * sigma*sigma) - 1);
        double qq = q*q;
        double qqq = qq*q;
        double b0 = 1.0/(1.57825 + 2.44413*q + 1.4281*qq + 0.422205*qqq);
        double b1 = (2.44413*q + 2.85619*qq + 1.26661*qqq)*b0;
        double b2 = (-1.4281*qq - 1.26661*qqq)*b0;
        double b3 = 0.422205*qqq*b0;
        double B = 1.0 - (b1 + b2 + b3);
        double bc;
        double[][]tempd, tempd1, tempd2, in_scand, out_scand;

        int imy = imd.length;
        int imx = imd[0].length;
        if ((imx>3)&&(imy>3))
        {
           tempx=imx;                // 2 soli ingressi: nessun padding
           tempy=imy;
           tempd = new double[tempy][];
           tempd1 = new double[tempy][];
           tempd2 = new double[tempy][];

           in_scand = new double[tempy][];
           out_scand = new double[tempy][];

            for (jj=0;jj<imy;jj++)
            {
                tempd[jj] = new double[imx];
                tempd1[jj] = new double[imx];
                tempd2[jj] = new double[imx];
                in_scand[jj] = new double[imx];
                out_scand[jj] = new double[imx];
            }

            for (jj=0;jj<imy;jj++)
            {
                for (ii=0;ii<imx;ii++)
                {
                    tempd[jj][ii] = imd[jj][ii];
                }
            }


            // ora tempd punta ad immagine 2D (paddata o meno con zeri)  
            for (jj=0;jj<tempy;jj++)
            {
                bc= tempd[jj][0];
                tempd1[jj][0]=B * tempd[jj][0]+b1*bc+b2*bc+b3*bc;
                tempd1[jj][1]=B * tempd[jj][1]+b1*bc+b2*bc+b3*bc;
                tempd1[jj][2]=B * tempd[jj][2]+b1*bc+b2*bc+b3*bc;

                for (ii=3;ii<tempx;ii++)
                {
                    tempd1[jj][ii]= B * tempd[jj][ii] + b1 * tempd1[jj][ii-1] + b2 * tempd1[jj][ii-2] + b3 * tempd1[jj][ii-3];
                }

                bc=tempd1[jj][tempx-1];
                tempd2[jj][tempx-1-0]= B * tempd1[jj][tempx-1-0]+b1*bc+b2*bc+b3*bc;
                tempd2[jj][tempx-1-1] = B * tempd1[jj][tempx-1-1]+b1*bc+b2*bc+b3*bc;
                tempd2[jj][tempx-1-2] = B * tempd1[jj][tempx-1-2]+b1*bc+b2*bc+b3*bc;

                for (ii=tempx-4;ii>=0;ii--)
                {
                    tempd2[jj][ii] = B * tempd1[jj][ii] + b1 * tempd2[jj][ii+1] + b2 * tempd2[jj][ii+2] + b3 * tempd2[jj][ii+3];  
                }

            }


            for (ii=0;ii<tempx;ii++)
            {
                bc = tempd2[0][ii];
                tempd1[0][ii]= B * tempd2[0][ii]+b1*bc+b2*bc+b3*bc;
                tempd1[1][ii]= B * tempd2[1][ii]+b1*bc+b2*bc+b3*bc;
                tempd1[2][ii]= B * tempd2[2][ii]+b1*bc+b2*bc+b3*bc;

                for (jj=3;jj<tempy;jj++)
                {
                    tempd1[jj][ii] = B * tempd2[jj][ii] + b1 * tempd1[jj-1][ii] + b2 * tempd1[jj-2][ii] + b3 * tempd1[jj-3][ii];
                }


                bc= tempd1[tempy-1][ii];
                tempd2[tempy-1-0][ii] = B * tempd1[tempy-1-0][ii]+b1*bc+b2*bc+b3*bc;
                tempd2[tempy-1-1][ii] = B * tempd1[tempy-1-1][ii]+b1*bc+b2*bc+b3*bc;
                tempd2[tempy-1-2][ii] = B * tempd1[tempy-1-2][ii]+b1*bc+b2*bc+b3*bc;


                for (jj=tempy-4;jj>=0;jj--)
                {
                    tempd2[jj][ii] = B * tempd1[jj][ii] + b1 * tempd2[jj+1][ii] + b2 * tempd2[jj+2][ii] + b3 * tempd2[jj+3][ii];
                }
            }


            return tempd2;  
        }
        else
        {
            return null;
        }
    }
}

