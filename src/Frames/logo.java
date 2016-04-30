package Frames;

import java.awt.Graphics;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class logo extends JPanel implements Runnable
	{
	private Thread timer;
	private Random losowacz;//;-)
	Color [] paleta;
	byte []ekran[];
	int szerokosc;
	int wysokosc;
	Font font;
	float fade = 6.01f;
	int delay = 20;
	private int unsignedByte(byte liczba)
		{
		return liczba & 0xff;
		}
	private void ustawPalete()
		{
		paleta = new Color[256];

		/*paleta[0] = new Color(0,0,0);
		paleta[64] = new Color(0,0,0);
		paleta[128] = new Color(0,0,255);
		paleta[192] = new Color(255,255,255);*/

		paleta[0] = new Color(0,0,0);
		paleta[64] = new Color(0,0,0);
		paleta[128] = new Color(255,255,0);
		paleta[192] = new Color(255,0,0);
		for (int a  = 1; a < 64; a++)
			{
			paleta[a + 0] = new Color((int)0,(int)0,(int)0);
			paleta[a + 64] = new Color((int)(a * 4 - 1),(int)0,(int)0);
			paleta[a + 128] = new Color((int)255, 255 - (int)(a * 4 - 1), 0);
			paleta[a + 192] = new Color(255 - (int)(a * 4 - 1),0,0);
			/*
			paleta[a] = new Color(0,0,(int)(a * 4 - 1));
			paleta[a + 64] = new Color(0,(int)(a * 4 - 1),(int)255);
			paleta[a + 128] = new Color((int)(a * 4 - 1),(int)255,(int)255);
			paleta[a + 192] = new Color((int)255,(int)255,(int)255);*/
			}

		szerokosc = this.getWidth() / 4;
		wysokosc = this.getHeight() / 4;

		ekran = new byte[szerokosc][];
		for (int a = 0; a < szerokosc; a++)
			ekran[a] = new byte[wysokosc + 3];
		//WYSOKOSC + 3!!
		for (int a = 0; a < szerokosc; a++)
			for (int b = 0; b < wysokosc + 3; b++)
				ekran[a][b] = (byte)0;

		font = new Font(null,Font.BOLD,46);
		
		}
	public void paintComponent(Graphics comp)
		{
		Color kolor;// = Color.RED;
		int a,b,x,y,pomoc;
                
                BufferedImage bi = new BufferedImage((wysokosc) * 4 ,szerokosc * 4, BufferedImage.TYPE_INT_RGB);
                Graphics comp2Dpomoc = bi.getGraphics();
		//Graphics comp2Dpomoc = comp.create();
		kolor = paleta[0];
		//comp2Dpomoc.fillRect(0,(wysokosc - 1) * 4 ,szerokosc * 4, this.getHeight() - (wysokosc - 1) * 4);
		comp2Dpomoc.fillRect(0,(wysokosc) * 4 ,szerokosc * 4, 4);

		comp2Dpomoc.setFont(font);
		//comp2D.clearRect(0,0,this.getWidth(),this.getHeight());


		for (a = 0; a < szerokosc; a++)
			ekran[a][wysokosc + 2] = (byte)(losowacz.nextInt(256-32) + 32);

		for (a = 1; a < szerokosc - 1; a++)
			for (b = wysokosc + 1; b > 1; b--)
				{
				pomoc = (int)((unsignedByte(ekran[a][b + 1])
					+ unsignedByte(ekran[a + 1][b])
					+ unsignedByte(ekran[a - 1][b])
					+ unsignedByte(ekran[a][b])
					+ unsignedByte(ekran[a - 1][b + 1])
					+ unsignedByte(ekran[a + 1][b + 1]))
					/ fade);
				ekran[a][b] = (byte) pomoc;

				ekran[a][b-1] = ekran[a][b];
				}
		for (y = 0; y < wysokosc; y++)
			{
			for (x = 0; x < szerokosc; x++)
				{
				kolor = null;
				kolor = paleta[unsignedByte(ekran[x][y])];
				comp2Dpomoc.setColor(kolor);
				comp2Dpomoc.fillRect(x*4,y*4,4,4);
				}
			}
                comp.drawImage(bi, 0, 0, this.getWidth(), this.getHeight(), 0, 0, (wysokosc) * 4 ,szerokosc * 4,this);
		//comp = comp2Dpomoc;
		//comp.setColor(Color.WHITE);
		//comp.drawString("BASEMENT", 40, 100);
		}
	public void paint(Graphics comp)
		{
                Color kolor;// = Color.RED;
		int a,b,x,y,pomoc;
                
                BufferedImage bi = new BufferedImage(szerokosc * 4, (wysokosc) * 4 , BufferedImage.TYPE_INT_RGB);
                Graphics comp2Dpomoc = bi.getGraphics();
		//Graphics comp2Dpomoc = comp.create();
		kolor = paleta[0];
		//comp2Dpomoc.fillRect(0,(wysokosc - 1) * 4 ,szerokosc * 4, this.getHeight() - (wysokosc - 1) * 4);
		comp2Dpomoc.fillRect(0, 0,(wysokosc) * 4 ,szerokosc * 4);

		comp2Dpomoc.setFont(font);
		//comp2D.clearRect(0,0,this.getWidth(),this.getHeight());


		for (a = 0; a < szerokosc; a++)
			ekran[a][wysokosc + 2] = (byte)(losowacz.nextInt(256-32) + 32);

		for (a = 1; a < szerokosc - 1; a++)
			for (b = wysokosc + 1; b > 1; b--)
				{
				pomoc = (int)((unsignedByte(ekran[a][b + 1])
					+ unsignedByte(ekran[a + 1][b])
					+ unsignedByte(ekran[a - 1][b])
					+ unsignedByte(ekran[a][b])
					+ unsignedByte(ekran[a - 1][b + 1])
					+ unsignedByte(ekran[a + 1][b + 1]))
					/ fade);
				ekran[a][b] = (byte) pomoc;

				ekran[a][b-1] = ekran[a][b];
				}
		for (y = 0; y < wysokosc; y++)
			{
			for (x = 0; x < szerokosc; x++)
				{
				kolor = null;
				kolor = paleta[unsignedByte(ekran[x][y])];
				comp2Dpomoc.setColor(kolor);
				comp2Dpomoc.fillRect(x*4,y*4,4,4);
				}
			}
                //comp = bi.getGraphics();
                comp.drawImage(bi, 0, 0, this.getWidth(), this.getHeight(), 0, 0, szerokosc * 4 ,wysokosc * 4,this);
                //comp.drawImage(bi, 0, 0, this);
		}

	public void start()
		{
		losowacz = new Random(System.currentTimeMillis());
		ustawPalete();
		timer = new Thread(this);
		timer.start();
		}
	public void stop()
		{
		timer = null;
		}
	public void run()
		{
		Thread me = Thread.currentThread();
		while (timer == me)
			{
			try
				{
				Thread.currentThread().sleep(delay);
				}
			catch (InterruptedException e)
				{
				}
			repaint();
			}
		}
        public static void main(String args[])
        {
            JFrame F = new JFrame();
            F.getContentPane().setLayout(new BorderLayout());
            
            logo l = new logo();
            //F.add(l);
            F.getContentPane().add(l, BorderLayout.CENTER);
            //F.setSize(new Dimension(1024, 768));
            Dimension ScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
            F.setUndecorated(true);
            F.setSize(ScreenSize);
            
            
            F.setVisible(true);
            F.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            l.start();
            
        }
}