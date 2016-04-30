package Engine;

import java.awt.Color;

public class DPolygon {
	public Color c;
	public double[] x, y, z;
	boolean draw = true, seeThrough = false;
	double[] CalcPos, newX, newY;
	PolygonObject DrawablePolygon;
	double AvgDist = 0;
	
        public void setColor(Color c)
        {
            this.c = c;
            DrawablePolygon.c = c;
        }
        
	public DPolygon(double[] x, double[] y,  double[] z, Color c, boolean seeThrough, Object[]ownerId)
	{
		this.x = x;
		this.y = y;
		this.z = z;		
		this.c = c;
		this.seeThrough = seeThrough; 
		createPolygon(ownerId);
	}
	
	void createPolygon(Object[]ownerId)
	{
		DrawablePolygon = new PolygonObject(new double[x.length], new double[x.length], c, Screen.DPolygons.size(), seeThrough, ownerId);
	}
	
        boolean willBeDrawn(double x_corr, double y_corr, Player player)
        {
            boolean drawHelp = true;
            newX = new double[x.length];
            newY = new double[x.length];
            double foo = 0;
            for(int i=0; i<x.length; i++)
            {
                    CalcPos = Calculator.CalculatePositionP(player.ViewFrom, player.ViewTo, -x[i] + x_corr, -y[i] + y_corr, z[i]);
                    foo = (DDDTutorial.ScreenSize.getWidth()/2 - Calculator.CalcFocusPos[0]) + CalcPos[0] * Screen.zoom;
                    foo = (DDDTutorial.ScreenSize.getHeight()/2 - Calculator.CalcFocusPos[1]) + CalcPos[1] * Screen.zoom;			
                    if(Calculator.t < 0)
                        drawHelp = false;
            }
            return drawHelp;
        }
        
        boolean willBeDrawn(Player player)
        {
            boolean drawHelp = true;
            newX = new double[x.length];
            newY = new double[x.length];
            double foo = 0;
            for(int i=0; i<x.length; i++)
            {
                    CalcPos = Calculator.CalculatePositionP(player.ViewFrom, player.ViewTo, x[i], y[i], z[i]);
                    foo = (DDDTutorial.ScreenSize.getWidth()/2 - Calculator.CalcFocusPos[0]) + CalcPos[0] * Screen.zoom;
                    foo = (DDDTutorial.ScreenSize.getHeight()/2 - Calculator.CalcFocusPos[1]) + CalcPos[1] * Screen.zoom;			
                    if(Calculator.t < 0)
                        drawHelp = false;
            }
            return drawHelp;
        }
        
        double GetDist(double x_corr, double y_corr, Player player)
	{
		double total = 0;
		for(int i=0; i<x.length; i++)
			total += GetDistanceToP(i, x_corr, y_corr, player);
		return total / x.length;
	}
        
        double GetDistanceToP(int i, double x_corr, double y_corr, Player player)
	{
		return Math.sqrt((player.ViewFrom[0]+x[i]-x_corr)*(player.ViewFrom[0]+x[i]-x_corr) + 
						 (player.ViewFrom[1]+y[i]-y_corr)*(player.ViewFrom[1]+y[i]-y_corr) +
						 (player.ViewFrom[2]-z[i])*(player.ViewFrom[2]-z[i]));
	}
        
        void updatePolygon(double x_corr, double y_corr, Player player)
	{		
		newX = new double[x.length];
		newY = new double[x.length];
		draw = true;
		for(int i=0; i<x.length; i++)
		{
			CalcPos = Calculator.CalculatePositionP(player.ViewFrom, player.ViewTo, -x[i] + x_corr, -y[i] + y_corr, z[i]);
			newX[i] = (DDDTutorial.ScreenSize.getWidth()/2 - Calculator.CalcFocusPos[0]) + CalcPos[0] * Screen.zoom;
			newY[i] = (DDDTutorial.ScreenSize.getHeight()/2 - Calculator.CalcFocusPos[1]) + CalcPos[1] * Screen.zoom;			
			if(Calculator.t < 0)
				draw = false;
		}
		
		calcLighting();
		
		DrawablePolygon.draw = draw;
		DrawablePolygon.updatePolygon(newX, newY);
		AvgDist = GetDist(player);
	}
        
	void updatePolygon(Player player)
	{		
		newX = new double[x.length];
		newY = new double[x.length];
		draw = true;
		for(int i=0; i<x.length; i++)
		{
			CalcPos = Calculator.CalculatePositionP(player.ViewFrom, player.ViewTo, x[i], y[i], z[i]);
			newX[i] = (DDDTutorial.ScreenSize.getWidth()/2 - Calculator.CalcFocusPos[0]) + CalcPos[0] * Screen.zoom;
			newY[i] = (DDDTutorial.ScreenSize.getHeight()/2 - Calculator.CalcFocusPos[1]) + CalcPos[1] * Screen.zoom;			
			if(Calculator.t < 0)
				draw = false;
		}
		
		calcLighting();
		
		DrawablePolygon.draw = draw;
		DrawablePolygon.updatePolygon(newX, newY);
		AvgDist = GetDist(player);
	}
	
	void calcLighting()
	{
		Plane lightingPlane = new Plane(this);
		double angle = Math.acos(((lightingPlane.NV.x * Screen.LightDir[0]) + 
			  (lightingPlane.NV.y * Screen.LightDir[1]) + (lightingPlane.NV.z * Screen.LightDir[2]))
			  /(Math.sqrt(Screen.LightDir[0] * Screen.LightDir[0] + Screen.LightDir[1] * Screen.LightDir[1] + Screen.LightDir[2] * Screen.LightDir[2])));
		
		DrawablePolygon.lighting = 0.2 + 1 - Math.sqrt(Math.toDegrees(angle)/180);

		if(DrawablePolygon.lighting > 1)
			DrawablePolygon.lighting = 1;
		if(DrawablePolygon.lighting < 0)
			DrawablePolygon.lighting = 0;
	}
		
	double GetDist(Player player)
	{
		double total = 0;
		for(int i=0; i<x.length; i++)
			total += GetDistanceToP(i, player);
		return total / x.length;
	}
	
	double GetDistanceToP(int i, Player player)
	{
		return Math.sqrt((player.ViewFrom[0]-x[i])*(player.ViewFrom[0]-x[i]) + 
						 (player.ViewFrom[1]-y[i])*(player.ViewFrom[1]-y[i]) +
						 (player.ViewFrom[2]-z[i])*(player.ViewFrom[2]-z[i]));
	}
}
