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
//http://blog.ivank.net/fastest-gaussian-blur.html
public class GaussFilter {
    private static double[] boxesForGauss(double sigma, int n)  // standard deviation, number of boxes
    {
        double wIdeal = Math.sqrt((12*sigma*sigma/n)+1);  // Ideal averaging filter width 
        double wl = Math.floor(wIdeal);  if(wl%2==0) wl--;
        double wu = wl+2;
				
        double mIdeal = (12*sigma*sigma - n*wl*wl - 4*n*wl - 3*n)/(-4*wl - 4);
        double m = Math.round(mIdeal);
    // var sigmaActual = Math.sqrt( (m*wl*wl + (n-m)*wu*wu - n)/12 );
			
        double []sizes = new double[n];
        for(int i=0; i<n; i++) sizes[i] = i<m?wl:wu;
        return sizes;
    }
    
    public static void Calculate(int[][]scl, int[][]tcl, double r)
    {
        double []sscl = new double[scl.length * scl[0].length];
        double []ttcl = new double[scl.length * scl[0].length];
        for (int a = 0; a < scl.length; a++)
            for (int b = 0; b < scl[0].length; b++)
            {
                sscl[(a * scl[0].length) + b] = scl[a][b];
            }
        gaussBlur_4(sscl, ttcl, scl.length, scl[0].length, r);
        for (int a = 0; a < scl.length; a++)
            for (int b = 0; b < scl[0].length; b++)
            {
                tcl[a][b] = (int)sscl[(a * scl[0].length) + b];
            }
    }
    
    public static void Calculate(double[][]scl, double[][]tcl, double r)
    {
        double []sscl = new double[scl.length * scl[0].length];
        double []ttcl = new double[scl.length * scl[0].length];
        for (int a = 0; a < scl.length; a++)
            for (int b = 0; b < scl[0].length; b++)
            {
                sscl[(a * scl[0].length) + b] = scl[a][b];
            }
        gaussBlur_4(sscl, ttcl, scl.length, scl[0].length, r);
        for (int a = 0; a < scl.length; a++)
            for (int b = 0; b < scl[0].length; b++)
            {
                tcl[a][b] = sscl[(a * scl[0].length) + b];
            }
    }
    
    public static void gaussBlur_4(double[]scl, double[]tcl, int w, int h, double r) {
        double[] bxs = boxesForGauss(r, 3);
        boxBlur_4(scl, tcl, w, h, (bxs[0]-1)/2);
        boxBlur_4(tcl, scl, w, h, (bxs[1]-1)/2);
        boxBlur_4(scl, tcl, w, h, (bxs[2]-1)/2);
    }
    
    private static void boxBlur_4(double[]scl, double[]tcl, int w, int h, double r) {
        for(int i=0; i<scl.length; i++) tcl[i] = scl[i];
        boxBlurH_4(tcl, scl, w, h, r);
        boxBlurT_4(scl, tcl, w, h, r);
    }

    private static void boxBlurH_4(double[]scl, double[]tcl, int w, int h, double r) {
        double iarr = 1 / (r+r+1);
        for(int i=0; i<h; i++) {
            int ti = i*w, li = ti, ri = (int)(ti+r);
            double fv = scl[ti], lv = scl[ti+w-1], val = (r+1)*fv;
            for(int j=0; j<r; j++) val += scl[ti+j];
            for(int j=0  ; j<=r ; j++) { val += scl[ri++] - fv       ;   tcl[ti++] = Math.round(val*iarr); }
            for(int j=(int)r+1; j<w-r; j++) { val += scl[ri++] - scl[li++];   tcl[ti++] = Math.round(val*iarr); }
            for(int j=w-(int)r; j<w  ; j++) { val += lv        - scl[li++];   tcl[ti++] = Math.round(val*iarr); }
        }
    }
    private static void boxBlurT_4(double[]scl, double[]tcl, int w, int h, double r) {
        double iarr = 1 / (r+r+1);
        for(int i=0; i<w; i++) {
            int ti = i, li = ti, ri = ti+(int)r*w;
            double fv = scl[ti], lv = scl[ti+w*(h-1)], val = (r+1)*fv;
            for(int j=0; j<r; j++) val += scl[ti+j*w];
            for(int j=0  ; j<=r ; j++) { val += scl[ri] - fv     ;  tcl[ti] = Math.round(val*iarr);  ri+=w; ti+=w; }
            for(int j=(int)r+1; j<h-r; j++) { val += scl[ri] - scl[li];  tcl[ti] = Math.round(val*iarr);  li+=w; ri+=w; ti+=w; }
            for(int j=h-(int)r; j<h  ; j++) { val += lv      - scl[li];  tcl[ti] = Math.round(val*iarr);  li+=w; ti+=w; }
        }
    }

}
