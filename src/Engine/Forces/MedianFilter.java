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
public class MedianFilter {

        public static int[][] Calculate(int[][] Tablica, int n, int m, int nW, int mW)
        {

            nW = nW - 1;
            mW = mW - 1;

            int lewonW = (int)Math.floor((double)nW / 2);
            int prawonW = (int)Math.floor((double)nW / 2) + (nW % 2);

            int lewomW = (int)Math.floor((double)mW / 2);
            int prawomW = (int)Math.floor((double)mW / 2) + (mW % 2);
            //n,m - wymiar macierzy
	        //nW, mW - wymiar okna

            int[][] Wynik = new int[n][m];

            int a = 0;
            int b = 0;
            int maxColorValue = 0;
            for (a = 0; a < n; a++)
                for (b = 0; b < m; b++)
                    if (Tablica[a][b] > maxColorValue)
                        maxColorValue = Tablica[a][b];
            int[] histogram = new int[maxColorValue + 1];
            //wyliczamy pierwszy histogram
            a = 0;
            b = 0;
            int pstartN = a - lewonW;
            int pstartM = b - lewomW;
            int pstopN = a + prawonW;
            int pstopM = b + prawomW;

            int ilosc = 0;

            int startN = pstartN;
            int startM = pstartM;
            int stopN = pstopN;
            int stopM = pstopM;

           	if (pstartN < 0) 
            {
                pstartN = 0; 
                startN = 0;
            }
	        if (pstartM < 0) 
            {
                pstartM = 0;
                startM = 0;
            }
	        if (pstopN >= n) 
            {
                pstopN = n - 1; 
                stopN = n - 1;
            }
            if (pstopM >= m)
            {
                pstopM = m - 1;
                stopM = m - 1;
            }

            int pstartNreal = -1;
            int pstopNreal = -1;
            //liczy początkowy histogram
	        for (a = 0; a <= pstopN; a++)
		        for (b = 0; b <= pstopM; b++)
			        histogram[Tablica[a][b]] = histogram[Tablica[a][b]] + 1;

            ilosc = (pstartN - pstopN) * (pstartM - pstopM);
            int wielkoscFiltra = (nW + nW + 1) * (mW + mW + 1);

            int usunN;
            int dodajN;
            int startNreal;
            int stopNreal;
            int usunM;
            int dodajM;
            for (a = 0; a < n; a++)
            {
       		    startNreal = a - lewonW;
    		    stopNreal = a + prawonW;


		        if (pstartNreal >= 0 && pstartNreal < n) usunN = pstartNreal;
                else usunN = -1;
		        if (stopNreal >= 0 && stopNreal < n) dodajN = stopNreal;
		        else dodajN = -1;

                startN = startNreal;
                stopN = stopNreal;
    	        if (startN < 0) startN = 0;
                if (startM < 0) startM = 0;
    	        if (stopN >= n) 
                    stopN = n - 1;
    	        if (stopM >= m) 
                    stopM = m -1 ;
                if (a != 0)
                    przesunHistogramWDol(histogram, Tablica, startM, stopM, usunN, dodajN, m);
                pstartNreal = startNreal;
                pstopNreal = stopNreal;
                if (a == n - 1)
                {
                    int z = 0;
                    z++;
                }
                //idziemy od lewej do prawej
                if (a % 2 == 0)
                    for (b = 0; b < m; b++)
                    {

                        startM = b - lewonW;
                        stopM = b + prawonW;

                        if (startM < 0) startM = 0;
                        if (stopM >= m) stopM = m - 1;

                        if (pstartM != startM && pstartM >= 0 && pstartM < m) 
                            usunM = pstartM;
                        else usunM = -1;

                        if (pstopM != stopM && stopM >= 0 && stopM < m) 
                            dodajM = stopM;
                        else dodajM = -1;

                        //przeliczenie histogramu
                        ilosc = (stopN - startN + 1) * (stopM - startM + 1);
                        //ilosc = (stopN - startN) * (stopM - startM);
                        liczHistogram(histogram, Tablica, startN, stopN, usunM, dodajM, wielkoscFiltra, ilosc, m);

                        int aa = liczMediane(histogram, ilosc);
                        Wynik[a][b] = liczMediane(histogram, ilosc);

                        pstartM = startM;
                        pstopM = stopM;
                    }
                else
                {
                    //od prawej do lewej
                    for (b = m -1; b >=0; b--)
                    {

                        startM = b - lewonW;
                        stopM = b + prawonW;

                        if (startM < 0) startM = 0;
                        if (stopM >= m) stopM = m - 1;
                
                        if (pstartM != startM && pstartM >= 0 && pstartM < m) 
                            dodajM = startM;
                        else dodajM = -1;
                        if (pstopM != stopM  && pstopM >= 0 && pstopM < m) 
                            usunM = pstopM;
                        else usunM = -1;

                        //przeliczenie histogramu
                        ilosc = (stopN - startN + 1) * (stopM - startM + 1);
                        liczHistogram(histogram, Tablica, startN, stopN, usunM, dodajM, wielkoscFiltra, ilosc,m);
                
                        int aa = liczMediane(histogram, ilosc);
                        Wynik[a][b] = liczMediane(histogram, ilosc);
                
                        pstartM = startM;
                        pstopM = stopM;
                    }
                }
            }
            return Wynik;
        }





        private static void przesunHistogramWDol(int [] histogram, int [][]tablica, int startM, int stopM, int usunN, int dodajN, int m)
        {
	        //dodanie zer
	        if (usunN >= 0)
		        for (int a = startM; a <= stopM; a++) 
                    histogram[tablica[usunN][a]] = histogram[tablica[usunN][a]] - 1;
        	if (dodajN >= 0)
                for (int a = startM; a <= stopM; a++) 
                    histogram[tablica[dodajN][a]] = histogram[tablica[dodajN][a]] + 1;
        }

        private static int liczMediane(int[] histogram, int ilosc)
        {
            double median = ilosc / 2;
            for (int c = 0; c < histogram.length; c++)
            {
                ilosc = ilosc - histogram[c];
                if (ilosc <= median)
                {
                    return c;
                }
            }
            return 0;
        }

        private static void liczHistogram(int []histogram, int [][]tablica, int startN, int stopN, int usunM, int dodajM, int wielkoscFiltra, int ilosc, int m)
        {
            //dodanie zer
            if (usunM >= 0)
                for (int a = startN; a <= stopN; a++)
                    histogram[tablica[a][usunM]] = histogram[tablica[a][usunM]] - 1;
            
            if (dodajM >= 0)
                for (int a = startN; a <= stopN; a++)
                    histogram[tablica[a][dodajM]] = histogram[tablica[a][dodajM]] + 1;
        }

}
