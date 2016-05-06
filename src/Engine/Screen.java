package Engine;

import Engine.Lua.PlayerLua;
import Frames.DialogJFrame;
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.JFrame;

import javax.swing.JPanel;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Screen extends JPanel implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener{
	
	//ArrayList of all the 3D polygons - each 3D polygon has a 2D 'PolygonObject' inside called 'DrawablePolygon'
	static ArrayList<DPolygon> DPolygons = new ArrayList<DPolygon>();
	
	//static ArrayList<Cube> Cubes = new ArrayList<Cube>();
	//static ArrayList<Prism> Prisms = new ArrayList<Prism>();
	//static ArrayList<Pyramid> Pyramids = new ArrayList<Pyramid>();
	
        ArrayList<IRenderableGameObject> gameObject = new ArrayList<IRenderableGameObject>();
	//The polygon that the mouse is currently over
	static PolygonObject PolygonOver = null;
        
	//Used for keeping mouse in center
	Robot r;

	/*static double[] ViewFrom = new double[] { 15, 5, 10},	
					ViewTo = new double[] {0, 0, 0},
					LightDir = new double[] {1, 1, 1};*/

	//static double[] ViewFrom = new double[] { 0, 0, 100};	
	//static double[] ViewTo = new double[] {0, 0, 0},
        static double[] LightDir = new double[] {1, 1, 1};
        
	//The smaller the zoom the more zoomed out you are and visa versa, although altering too far from 1000 will make it look pretty weird
	static double zoom = 1000, MinZoom = 500, MaxZoom = 2500, MouseX = 0, MouseY = 0, MovementSpeed = 100, JumpHeight = 25, HorRotSpeedKeybord = 2;
	
	//FPS is a bit primitive, you can set the MaxFPS as high as u want
	double drawFPS = 0, MaxFPS = 60, SleepTime = 1000.0/MaxFPS, LastRefresh = 0, StartTime = System.currentTimeMillis(), LastFPSCheck = 0, Checks = 0;
	//VertLook goes from 0.999 to -0.999, minus being looking down and + looking up, HorLook takes any number and goes round in radians
	//aimSight changes the size of the center-cross. The lower HorRotSpeed or VertRotSpeed, the faster the camera will rotate in those directions
	double VertLook = 0, HorLook = 0, aimSight = 4, HorRotSpeed = 900, VertRotSpeed = 2200, SunPos = 0;

	//will hold the order that the polygons in the ArrayList DPolygon should be drawn meaning DPolygon.get(NewOrder[0]) gets drawn first
	int[] NewOrder;

	static boolean OutLines = false;
	boolean[] Keys = new boolean[10];
	boolean[] PrevKeys = new boolean[10];
        
	long repaintTime = 0;
	
        
        WorldMap worldMap = new WorldMap();
        JFrame parent = null;
        Player player = null;
        Game game = null;
        
        public boolean gamePaused = false;
        //BiHashMap<Integer,Integer,IRenderableGameObject> gameObjectsHashMap;
        ArrayList<Projectile> Projectiles = new ArrayList<Projectile>();
	public Screen(JFrame parent)
	{		
            game = new Game();
            player = game.AddNewPlayer(new double[] { 0, 0, 100}, new double[] {0, 0, 0}, -1);
            player.Name = "Tomuś :-)";
            gameObject.add(player);
            worldMap.updateObjectPosition(-1, -1, (int)(player.GetPosition().getX() / GenerateTerrain.Size), (int)(player.GetPosition().getY()  / GenerateTerrain.Size), player);
            
                //this.gameObjectsHashMap = new BiHashMap<Integer,Integer,IRenderableGameObject>();
                this.parent = parent;
		this.addKeyListener(this);
		setFocusable(true);		
		
		parent.addMouseListener(this);
		parent.addMouseMotionListener(this);
		parent.addMouseWheelListener(this);
		
		//invisibleMouse();	
		new GenerateTerrain();
                /*
		Cubes.add(new Cube(0, -5, 0, 2, 2, 2, Color.red));
		Prisms.add(new Prism(6, -5, 0, 2, 2, 2, Color.green));
		Pyramids.add(new Pyramid(12, -5, 0, 2, 2, 2, Color.blue));
		Cubes.add(new Cube(18, -5, 0, 2, 2, 2, Color.red));
		Cubes.add(new Cube(20, -5, 0, 2, 2, 2, Color.red));
		Cubes.add(new Cube(22, -5, 0, 2, 2, 2, Color.red));
		Cubes.add(new Cube(20, -5, 2, 2, 2, 2, Color.red));
		Prisms.add(new Prism(18, -5, 2, 2, 2, 2, Color.green));
		Prisms.add(new Prism(22, -5, 2, 2, 2, 2, Color.green));
		Pyramids.add(new Pyramid(20, -5, 4, 2, 2, 2, Color.blue));*/
                
                /*
                for (int a = 4; a < 8; a++)
                    for (int b = 4; b < 8; b++)
                        for (int c = 0; c < 1; c++)
                        {
                            Cube cc = new Cube(a * 8, b * 8, c * 8 -20, 4, 4, 4 + 20, Color.red, null);
                            //Cube cc = new Cube(a * 4, b * 4, c * 4, 2, 2, 2, Color.red, null);
                            gameObject.add(cc);
                            worldMap.updateObjectPosition(-1, -1, (int)(cc.x / GenerateTerrain.Size), (int)(cc.y  / GenerateTerrain.Size), cc);
                        }
                */
                //Cube cc = new Cube(14 * GenerateTerrain.Size, 14 * GenerateTerrain.Size, 8 -20, 16, 16, 4 + 20, Color.red, null);
                /*
                Cube cc = new Cube(8 * GenerateTerrain.Size + 7, 8 * GenerateTerrain.Size + 7, 8 -24, 16, 16, 4 + 24, Color.red, null);
                            //Cube cc = new Cube(a * 4, b * 4, c * 4, 2, 2, 2, Color.red, null);
                            gameObject.add(cc);
                            worldMap.updateObjectPosition(-1, -1, (int)(cc.x / GenerateTerrain.Size), (int)(cc.y  / GenerateTerrain.Size), cc);
                
                  */          
            //Player p = game.AddNewPlayer(new double[]{8 * GenerateTerrain.Size + 4,8 * GenerateTerrain.Size + 4,8}, new double[]{0,0,0}, -1);
                
                
                Player p = game.AddNewPlayer(new double[]{8 * GenerateTerrain.Size,8 * GenerateTerrain.Size + 4,8}, new double[]{0,0,0}, -1);
                p.Name = "Another player";
                gameObject.add(p);
                worldMap.updateObjectPosition(-1, -1, (int)(p.GetPosition().getX() / GenerateTerrain.Size), (int)(p.GetPosition().getY()  / GenerateTerrain.Size), p);
                
                /*
                p = game.AddNewPlayer(new double[]{10,8 * GenerateTerrain.Size + 24,8}, new double[]{0,0,0}, -1);
                p.Name = "Another player 2";
                gameObject.add(p);
                worldMap.updateObjectPosition(-1, -1, (int)(p.GetPosition().getX() / GenerateTerrain.Size), (int)(p.GetPosition().getY()  / GenerateTerrain.Size), p);
                */
                
                Cube cc = new Cube(8 * GenerateTerrain.Size,8 * GenerateTerrain.Size, -16, 8*2, 8*2, 8*4, Color.red, null);
                //Cube cc = new Cube(a * 4, b * 4, c * 4, 2, 2, 2, Color.red, null);
                gameObject.add(cc);
                worldMap.updateObjectPosition(-1, -1, (int)(cc.x / GenerateTerrain.Size), (int)(cc.y  / GenerateTerrain.Size), cc);
                /*
                Cube cc = new Cube(8 * GenerateTerrain.Size,8 * GenerateTerrain.Size, -16, 8, 8, 8, Color.red, null);
                //Cube cc = new Cube(a * 4, b * 4, c * 4, 2, 2, 2, Color.red, null);
                gameObject.add(cc);
                worldMap.updateObjectPosition(-1, -1, (int)(cc.x / GenerateTerrain.Size), (int)(cc.y  / GenerateTerrain.Size), cc);
                */
                /*Cube cc = new Cube(2, 10, 0, 1, 1, 1, Color.red);
                Cube c1 = new Cube(4, 10, 0, 1, 1, 1, Color.pink);
                Cube c2 = new Cube(6, 15, 0, 1, 1, 1, Color.yellow);
                gameObject.add(cc);
                gameObject.add(c1);
                gameObject.add(c2);
                worldMap.updateObjectPosition(-1, -1, 5, 10, cc);
                worldMap.updateObjectPosition(-1, -1, 15, 10, c1);
                worldMap.updateObjectPosition(-1, -1, 15, 15, c2);*/
                   
                /*gameObjectsHashMap.put(5, 10, cc);*/
	}	
	
        void shoot(Vector3D position, Vector3D shootVector, Player pl)
        {
            Projectile p = new Projectile(null, new Vector3D(5.0, 5.0, 5.0),position, new Vector3D[]{shootVector.normalize().scalarMultiply(100)}, true, this, new ShootProjectileBehaviour(), 2, new Object[]{pl});
            Projectiles.add(p);
            gameObject.add(p);
            worldMap.updateObjectPosition(-1, -1, (int)(p.getX() / GenerateTerrain.Size), (int)(p.getY()  / GenerateTerrain.Size), p);
            //gameObject.add(new Cube(5, 10, 0, 5, 5, 5, Color.red));
        }
        
        Color BackgroundColor = new Color(0, 0, 0);
        boolean CanTalk = false;
	public void paintComponent(Graphics g)
	{
            //long timeSLU = (long) (System.currentTimeMillis() - LastRefresh); 
            //Clear screen and draw background color
            g.setColor(new Color(140, 180, 180));
            //g.setColor(BackgroundColor);
            g.fillRect(0, 0, (int)parent.getWidth(), (int)parent.getHeight());

            CameraMovement();
            
            if (!gamePaused)
            {
                for (int a = 0; a < gameObject.size(); a++)
                {
                    if (gameObject.get(a) instanceof Player && player != gameObject.get(a))
                    {
                        Player p = (Player)gameObject.get(a);
                        int oldX = (int)(p.ViewFrom[0] / GenerateTerrain.Size);
                        int oldY = (int)(p.ViewFrom[1] / GenerateTerrain.Size);
                        double x = 0;
                        double y = 0;


                        p.calculateMoveInterval += timeSLU;
                        if (p.calculateMoveInterval > p.calculateMoveIntervalMax)
                        {
                            p.ApproachPlayer(player, this.worldMap);
                            //p.AttackPlayer(player, worldMap, this);
                            p.calculateMoveInterval = 0;
                        }
                        double MovementSpeedTime = MovementSpeed * ((double)timeSLU / 1000.0) / 4;
                        if (p.MoveDirection != null)
                        {
                            x = p.MoveDirection.getX() * MovementSpeedTime;
                            y = p.MoveDirection.getY() * MovementSpeedTime;
                        }

                        IRenderableGameObject collision = MoveTo2(p.ViewFrom[0] + x, p.ViewFrom[1] + y, p.ViewFrom[2], p, this);
                        worldMap.updateObjectPosition(oldX, oldY, (int)(p.GetPosition().getX() / GenerateTerrain.Size), (int)(p.GetPosition().getY()  / GenerateTerrain.Size), p);
                        //if (collision != null) collision = MoveTo2(p.ViewFrom[0] + x, p.ViewFrom[1], p.ViewFrom[2], p, this);
                        //else if (collision != null) collision = MoveTo2(p.ViewFrom[0], p.ViewFrom[1] + y, p.ViewFrom[2], p, this);
                        if (collision != null && p.jumpProjectile == null && !(collision instanceof Player)) 
                        {
                            Vector3D[] v = new Vector3D[1];
                            v[0] = new Vector3D(0,0,JumpHeight);
                            p.jumpProjectile = new Projectile(null,Vector3D.ZERO, new Vector3D(p.ViewFrom[0], p.ViewFrom[1], p.ViewFrom[2]), v, true, this, null, 8, new Object[]{p});
                            //MoveTo2(p.ViewFrom[0], p.ViewFrom[1], p.ViewFrom[2], p, this);
                            System.out.println("Jump!");
                            //MoveTo2(p.ViewFrom[0], p.ViewFrom[1], p.ViewFrom[2], p, this);
                            //worldMap.updateObjectPosition(oldX, oldY, (int)(p.GetPosition().getX() / GenerateTerrain.Size), (int)(p.GetPosition().getY()  / GenerateTerrain.Size), p);
                        }
                        

                        
                    }
                }
            }
            //Calculated all that is general for this camera position
            Calculator.SetPrederterminedInfo(player);

            if (!gamePaused)
            {
                ControlSunAndLight();
            }
            //Updates each polygon for this camera position
            //for(int i = 0; i < DPolygons.size(); i++)
            //	DPolygons.get(i).updatePolygon();

            //rotate and update shape examples
            /*Cubes.get(0).rotation+=.01;
            Cubes.get(0).updatePoly();

            Prisms.get(0).rotation+=.01;
            Prisms.get(0).updatePoly();

            Pyramids.get(0).rotation+=.01;
            Pyramids.get(0).updatePoly();
            */
            //Set drawing order so closest polygons gets drawn last

            setOrder();
            //Player p = getAim();
            /*for(int i = 0; i < OrderedPolygons.size(); i++)
                    OrderedPolygons.get(i).updatePolygon();*/
            //Set the polygon that the mouse is currently over
            setPolygonOver();

            //draw polygons in the Order that is set by the 'setOrder' function

            for(int i = 0; i < OrderedPolygons.size(); i++)
            {
                    OrderedPolygons.get(i).DrawablePolygon.drawPolygon(g);
            }

            /*
            for(int i = 0; i < NewOrder.length; i++)
            {
                    DPolygons.get(NewOrder[i]).DrawablePolygon.drawPolygon(g);
            }*/
            //draw the cross in the center of the screen
            drawMouseAim(g);			

            //FPS display
            g.drawString("FPS: " + (int)drawFPS + " (Benchmark)", 40, 40);
            /*g.drawString(" " + (player.ViewFrom[0] / GenerateTerrain.Size) % worldMap.getMapSize() + " " 
                    + (player.ViewFrom[1] / GenerateTerrain.Size) % worldMap.getMapSize()  + " " 
                    + (player.ViewFrom[2] / GenerateTerrain.Size) % worldMap.getMapSize() ,
                    40, 80);


            g.drawString(" " + player.ViewFrom[0] + " " 
                    + player.ViewFrom[1]  + " " 
                    + player.ViewFrom[2] ,
                    40, 120);
            */
            double distanceToPolygonOver = 0;
            CanTalk = false;
            if (PolygonOver != null)
            {
                if (PolygonOver.OwnerId != null)
                    if (PolygonOver.OwnerId[0] instanceof Player)
                    {
                        Player playerOver = (Player)PolygonOver.OwnerId[0];
                        //g.drawString(" " + playerOver.Name + " " + DistanceBetweenDoubleTables(playerOver.ViewFrom, player.ViewFrom),
                        distanceToPolygonOver = AlgebraUtils.DistanceBetweenDoubleTablesMirror(playerOver.ViewFrom, player.ViewFrom, this.worldMap);
                        g.drawString(" " + playerOver.Name + " " + distanceToPolygonOver,
                        40, 160);
                        if (distanceToPolygonOver < 24)
                        {
                            CanTalk = true;
                        }
                    }
            }
            if (CanTalk)
            {
                g.drawString("Press 'T' to talk to '" + ((Player)PolygonOver.OwnerId[0]).Name + "'",
                    40, 180);
            }
//		repaintTime = System.currentTimeMillis() - repaintTime; 
//		System.out.println(repaintTime);

            if (worldMap.miniMapImage != null)
            {
                worldMap.player_xx = (int)this.player.getX();
                worldMap.player_yy = (int)this.player.getY();
                g.drawImage(worldMap.miniMapImage, 0, 200, this);
            }
            SleepAndRefresh();
                
	}
	
        
        private void getAim()
        {
            //player.ViewFrom
        }
        
        public class CustomComparator implements Comparator<DPolygon> {
            @Override
            public int compare(DPolygon o1, DPolygon o2) {
                if (o1.AvgDist == o2.AvgDist) return 0;
                if (o1.AvgDist > o2.AvgDist) return -1;
                return 1;
            }
        }

        ArrayList<DPolygon>OrderedPolygons = new ArrayList<DPolygon>();
	void setOrder()
	{
            OrderedPolygons.clear();
            ArrayList<RenderableGameBojectsHelper>gameObjectsToDraw = GenerateTerrain.GenerateVisualization(worldMap, player.ViewFrom, this);
            for (int a = 0; a < DPolygons.size(); a++)
                DPolygons.get(a).updatePolygon(player);
            OrderedPolygons.addAll(DPolygons);
            
            //OrderedPolygons.addAll(gameObjectsToDraw);
            for (int a = 0; a < gameObjectsToDraw.size(); a++)
            {
                RenderableGameBojectsHelper rgo = gameObjectsToDraw.get(a);
                
                if (rgo.rgo instanceof Cube)
                {
                    Cube aa = (Cube)rgo.rgo;
                    Cube nc = new Cube(rgo.x, rgo.y, aa.z, aa.width, aa.length, aa.height, aa.c, null);
                    for (int b = 0; b < nc.GetPolygons().length; b++)
                    {
                        DPolygon polygon = nc.GetPolygons()[b];
                        polygon.updatePolygon(player);
                        OrderedPolygons.add(polygon);
                    }
                }
                if (rgo.rgo instanceof Player && rgo.rgo != player)
                {
                    Player p = (Player)rgo.rgo;
                    /*
                    Cube nc = new Cube(rgo.x, rgo.y, p.ViewFrom[2]-8, 2, 2, 10, Color.CYAN, null);
                    for (int b = 0; b < nc.GetPolygons().length; b++)
                    {
                        DPolygon polygon = nc.GetPolygons()[b];
                        polygon.updatePolygon(player);
                        OrderedPolygons.add(polygon);
                    }*/
                    //DPolygon polygon = p.GetPolygons();
                    /*for (int b = 0; b < p.GetPolygons(rgo.x, rgo.y).length; b++)
                    {
                        DPolygon polygon = p.GetPolygons(rgo.x, rgo.y)[b];
                        polygon.updatePolygon(player);
                        OrderedPolygons.add(polygon);
                    }*/
                    
                    //if (p.ViewFrom[2] - )
                    
                    //MoveTo2(p.ViewFrom[0], p.ViewFrom[1], p.ViewFrom[2], p, this);
                
                    
                    ArrayList<DPolygon>al = p.GetPolygons(rgo.x, rgo.y);
                    for (int b = 0; b < al.size(); b++)
                    {
                        DPolygon polygon = al.get(b);
                        polygon.updatePolygon(player);
                        OrderedPolygons.add(polygon);
                    }
                    
                    /*Cube aa = (Cube)rgo.rgo;
                    Cube nc = new Cube(rgo.x, rgo.y, aa.z, aa.width, aa.length, aa.height, aa.c, null);
                    for (int b = 0; b < nc.GetPolygons().length; b++)
                    {
                        DPolygon polygon = nc.GetPolygons()[b];
                        polygon.updatePolygon(player);
                        OrderedPolygons.add(polygon);
                    }*/
                }
                else if (rgo.rgo instanceof Projectile)
                {
                    Cube nc = null;
                    Projectile p = (Projectile)rgo.rgo;
                    if (p.size != null)
                    {
                        nc = new Cube(rgo.x - (p.size.getX() / 2.0),rgo.y - (p.size.getY() / 2.0), p.actualPosition.getZ() - (p.size.getZ() / 2.0), 
                            p.size.getX(), p.size.getY(), p.size.getZ(), Color.PINK, null);
                    }
                    else
                    {
                        nc = new Cube(p.actualPosition.getX() - 0.5, p.actualPosition.getY() - 0.5, p.actualPosition.getZ() - 0.5, 
                            1, 1, 1, Color.PINK, null);
                    }

                    for (int b = 0; b < nc.GetPolygons().length; b++)
                    {
                        DPolygon polygon = nc.GetPolygons()[b];
                        polygon.updatePolygon(player);
                        polygon.seeThrough = true;
                        OrderedPolygons.add(polygon);
                    }
                }
            }
            /*
            for (int a = 0; a < Projectiles.size(); a++)
            {
                Projectile p = Projectiles.get(a);
                Cube nc = null;
                if (p.size != null)
                {
                    nc = new Cube(p.actualPosition.getX() - (p.size.getX() / 2.0), p.actualPosition.getY() - (p.size.getY() / 2.0), p.actualPosition.getZ() - (p.size.getZ() / 2.0), 
                        p.size.getX(), p.size.getY(), p.size.getZ(), Color.PINK);
                }
                else
                {
                    nc = new Cube(p.actualPosition.getX() - 0.5, p.actualPosition.getY() - 0.5, p.actualPosition.getZ() - 0.5, 
                        1, 1, 1, Color.PINK);
                }
                
                for (int b = 0; b < nc.GetPolygons().length; b++)
                {
                    DPolygon polygon = nc.GetPolygons()[b];
                    polygon.updatePolygon();
                    polygon.seeThrough = true;
                    OrderedPolygons.add(polygon);
                }
            }*/
            Collections.sort(OrderedPolygons, new CustomComparator());
	}
		
	void invisibleMouse()
	{
		 Toolkit toolkit = Toolkit.getDefaultToolkit();
		 BufferedImage cursorImage = new BufferedImage(1, 1, BufferedImage.TRANSLUCENT); 
		 Cursor invisibleCursor = toolkit.createCustomCursor(cursorImage, new Point(0,0), "InvisibleCursor");        
		 setCursor(invisibleCursor);
	}
	
	void drawMouseAim(Graphics g)
	{
		g.setColor(Color.black);
		//g.drawLine((int)(DDDTutorial.ScreenSize.getWidth()/2 - aimSight), (int)(DDDTutorial.ScreenSize.getHeight()/2), (int)(DDDTutorial.ScreenSize.getWidth()/2 + aimSight), (int)(DDDTutorial.ScreenSize.getHeight()/2));
		//g.drawLine((int)(DDDTutorial.ScreenSize.getWidth()/2), (int)(DDDTutorial.ScreenSize.getHeight()/2 - aimSight), (int)(DDDTutorial.ScreenSize.getWidth()/2), (int)(DDDTutorial.ScreenSize.getHeight()/2 + aimSight));			
                g.drawLine((int)(parent.getWidth()/2 - aimSight), (int)(parent.getHeight()/2), (int)(parent.getWidth()/2 + aimSight), (int)(parent.getHeight()/2));
		g.drawLine((int)(parent.getWidth()/2), (int)(parent.getHeight()/2 - aimSight), (int)(parent.getWidth()/2), (int)(parent.getHeight()/2 + aimSight));			
	}

        long timeSLU = 100000;
        
	void SleepAndRefresh()
	{
		//long timeSLU = (long) (System.currentTimeMillis() - LastRefresh); 
                timeSLU = (long) (System.currentTimeMillis() - LastRefresh); 

		Checks ++;			
		if(Checks >= 15)
		{
			drawFPS = Checks/((System.currentTimeMillis() - LastFPSCheck)/1000.0);
			LastFPSCheck = System.currentTimeMillis();
			Checks = 0;
		}
		if (!gamePaused)
                {
                    Point p = MouseInfo.getPointerInfo().getLocation();
                    MouseX = p.getX();
                    MouseY = p.getY();

                    MouseMovement(timeSLU / 1000.0);
                    CenterMouse();
                    //jump
                    for (int a = 0; a < gameObject.size(); a++)
                    {
                        if (gameObject.get(a) instanceof Player)
                        {
                            Player pp = (Player)gameObject.get(a);
                            if (pp.jumpProjectile != null)
                            {
                                pp.jumpProjectile.CalculatePosition(timeSLU / 1000.0, this);
                            }
                        }
                    }
                    for (int a = 0; a < Projectiles.size(); a++)
                    {
                        int old_x = (int)(Projectiles.get(a).actualPosition.getX() / GenerateTerrain.Size);
                        int old_y = (int)(Projectiles.get(a).actualPosition.getY() / GenerateTerrain.Size);
                        Object o = Projectiles.get(a).CalculatePosition(timeSLU / 1000.0, this);
                        if (o != null)
                        {
                            System.out.println("Projectile hit object " + o.toString());
                        }
                        //worldMap.CorrectPosition(ViewFrom, this);
                        if (Projectiles.get(a).Remove)
                        {
                            gameObject.remove(Projectiles.get(a));
                            worldMap.removeObject(old_x, old_y, Projectiles.get(a));
                            Projectiles.remove(Projectiles.get(a));

                            //a++;
                            a--;
                        }
                        else
                        {
                            worldMap.updateObjectPosition(old_x, old_y, 
                                    (int)(Projectiles.get(a).getX() / GenerateTerrain.Size), (int)(Projectiles.get(a).getY()  / GenerateTerrain.Size), 
                                    Projectiles.get(a));
                        }
                    }

                    if(timeSLU < 1000.0/MaxFPS)
                    {
                            try {
                                    Thread.sleep((long) (1000.0/MaxFPS - timeSLU));
                            } catch (InterruptedException e) {
                                    e.printStackTrace();
                            }	
                    }
                }
		LastRefresh = System.currentTimeMillis();
		
		repaint();
	}
	
	void ControlSunAndLight()
	{
		SunPos += 0.005;		
		double mapSize = GenerateTerrain.mapSize * GenerateTerrain.Size;
		LightDir[0] = mapSize/2 - (mapSize/2 + Math.cos(SunPos) * mapSize * 10);
		LightDir[1] = mapSize/2 - (mapSize/2 + Math.sin(SunPos) * mapSize * 10);
		LightDir[2] = -200;
	}
	
        double eps = 0.01;
        //Projectile jumpProjectile = null;
        //double headPosition = 2.0 * GenerateTerrain.Size;
	void CameraMovement()
	{
		Vector ViewVector = new Vector(player.ViewTo[0] - player.ViewFrom[0], player.ViewTo[1] - player.ViewFrom[1], player.ViewTo[2] - player.ViewFrom[2]);
		double xMove = 0, yMove = 0, zMove = 0;
		Vector VerticalVector = new Vector (0, 0, 1);
		Vector SideViewVector = ViewVector.CrossProduct(VerticalVector);
		
                double groundPosition = worldMap.GetGroundPosition(player.ViewFrom, this.worldMap) + player.headPosition;
                if (!gamePaused)
                {
                    if(Keys[0])
                    {
                            xMove += ViewVector.x ;
                            yMove += ViewVector.y ;
                            //zMove += ViewVector.z ;
                            PrevKeys[0] = true;
                    }

                    if(Keys[2])
                    {
                            xMove -= ViewVector.x ;
                            yMove -= ViewVector.y ;
                            //zMove -= ViewVector.z ;
                            PrevKeys[2] = true;
                    }

                    if(Keys[1])
                    {
                            xMove += SideViewVector.x ;
                            yMove += SideViewVector.y ;
                            //zMove += SideViewVector.z ;
                            PrevKeys[1] = true;
                    }

                    if(Keys[3])
                    {
                            xMove -= SideViewVector.x ;
                            yMove -= SideViewVector.y ;
                            //zMove -= SideViewVector.z ;
                            PrevKeys[3] = true;
                    }
                    if(Keys[4])
                    {
                            //if (Math.abs(player.ViewFrom[2] - groundPosition) < eps & player.jumpProjectile == null)
                            if (player.jumpProjectile == null)
                            {
                                Vector3D[] v = new Vector3D[1];
                                v[0] = new Vector3D(0,0,JumpHeight);
                                player.jumpProjectile = new Projectile(null,Vector3D.ZERO, new Vector3D(player.ViewFrom[0], player.ViewFrom[1], player.ViewFrom[2]), v, true, this, null, 8, new Object[]{player});
                            }
                            PrevKeys[4] = true;
                    }

                    if(Keys[5])
                    {
                        if (PrevKeys[5] == false)
                        {
                            shoot(new Vector3D(player.ViewFrom[0], player.ViewFrom[1], player.ViewFrom[2]), new Vector3D(ViewVector.x, ViewVector.y, ViewVector.z), player);
                        }
                            PrevKeys[5] = true;
                    }
                    if(Keys[6])
                    {
                            HorLook -= ((double)timeSLU / 1000.0) * HorRotSpeedKeybord;

                            if(VertLook>0.999)
                                    VertLook = 0.999;

                            if(VertLook<-0.999)
                                    VertLook = -0.999;

                            if(VertLook>0.999)
                                    VertLook = 0.999;

                            if(VertLook<-0.999)
                                    VertLook = -0.999;
                            updateView();
                            PrevKeys[6] = true;
                    }
                    if(Keys[7])
                    {
                            HorLook += ((double)timeSLU / 1000.0) * HorRotSpeedKeybord;

                            if(VertLook>0.999)
                                    VertLook = 0.999;

                            if(VertLook<-0.999)
                                    VertLook = -0.999;

                            if(VertLook>0.999)
                                    VertLook = 0.999;

                            if(VertLook<-0.999)
                                    VertLook = -0.999;
                            updateView();
                            PrevKeys[7] = true;

                    }
                    if (Keys[8])
                    {
                        if (CanTalk && PrevKeys[8] == false)
                        {
                            if (PolygonOver != null)
                            {
                                Player playerOver = (Player)PolygonOver.OwnerId[0];
                                BeginConversation(playerOver);
                            }
                        }
                        PrevKeys[8] = true;
                    }
                }
                if (Keys[9])
                {
                    if (PrevKeys[9] == false)
                    {
                        gamePaused = !gamePaused;
                    }
                    PrevKeys[9] = true;
                }
		Vector MoveVector = new Vector(xMove, yMove, zMove);               
		//MoveTo2(player.ViewFrom[0] + MoveVector.x * MovementSpeed, player.ViewFrom[1] + MoveVector.y * MovementSpeed, player.ViewFrom[2] + MoveVector.z * MovementSpeed, player, this);
                double MovementSpeedTime = MovementSpeed * ((double)timeSLU / 1000.0);
                //MovementSpeedTime = 0.5;
                int oldX = (int)(player.ViewFrom[0] / GenerateTerrain.Size);
                int oldY = (int)(player.ViewFrom[1] / GenerateTerrain.Size);
                IRenderableGameObject collision = MoveTo2(player.ViewFrom[0] + MoveVector.x * MovementSpeedTime, player.ViewFrom[1] + MoveVector.y * MovementSpeedTime, player.ViewFrom[2], player, this);
                //if (collision != null) MoveTo2(player.ViewFrom[0] + MoveVector.x * MovementSpeedTime, player.ViewFrom[1], player.ViewFrom[2], player, this);
                //else if (collision != null) MoveTo2(player.ViewFrom[0], player.ViewFrom[1] + MoveVector.y * MovementSpeedTime, player.ViewFrom[2], player, this);
                
                worldMap.updateObjectPosition(oldX, oldY, (int)(player.GetPosition().getX() / GenerateTerrain.Size), (int)(player.GetPosition().getY()  / GenerateTerrain.Size), player);
                
                
                
                updateView();
	}

        DialogJFrame dialogFrame = null;
        
        public void DisposeDialog()
        {
            dialogFrame.dispose();
            dialogFrame = null;
            CenterMouse();
            //releas all keys - we loose focus on old window
            for (int a = 0; a < PrevKeys.length; a++)
            {
                PrevKeys[a] = false;
                Keys[a] = false;
            }
            gamePaused = false;
        }
        
        public void BeginConversation(Player p)
        {
            gamePaused = true;
            //PlayerLua c1 = new PlayerLua("e:\\Projects\\java 3d tutorial\\Game\\src\\Scripts\\PlayerTemplate.lua");
            PlayerLua c1 = new PlayerLua("PlayerTemplate.lua");
            PlayerStats[]ps = new PlayerStats[2];
            ps[0] = new PlayerStats();
            ps[1] = new PlayerStats();
            dialogFrame = new DialogJFrame(c1, this, "bleble", true, ps);
            dialogFrame.setLocationRelativeTo(this);
            dialogFrame.setAlwaysOnTop(true);
            dialogFrame.setVisible(true);
            
            
            /*DialogJDialog df = new DialogJDialog(c1, parent, "bleble", true);

            df.setVisible(true);
            df.dispose();*/
            
            
        }
     
        private IRenderableGameObject CoolideWithGameObjectsOnMap(Screen screen, BoundingBox playerBox, Player player)
        {
            int x = (int)(player.ViewFrom[0] / GenerateTerrain.Size);
            int y = (int)(player.ViewFrom[1] / GenerateTerrain.Size);
            for (int a = x - Configurations.MaximalObjectSize; a <=  x + Configurations.MaximalObjectSize; a++)
                for (int b = y - Configurations.MaximalObjectSize; b <= y + Configurations.MaximalObjectSize; b++)
                {
                    int a1 = a;
                    int b1 = b;
                    int aCorrection = 0;
                    int bCorrection = 0; 
                    if (a1 < 0) 
                    {
                        a1 = screen.worldMap.ObjectsMap.length + a1;
                        aCorrection = a;
                    }
                    if (a1 >= screen.worldMap.ObjectsMap.length)
                    {
                        a1 -= screen.worldMap.ObjectsMap.length;
                        aCorrection = a;
                    }
                    if (b1 < 0) 
                    {
                        b1 = screen.worldMap.ObjectsMap[0].length + b1;
                        bCorrection = b;
                    }
                    if (b1 >= screen.worldMap.ObjectsMap[0].length)
                    {
                        b1 -= screen.worldMap.ObjectsMap[0].length;
                        bCorrection = b;
                    }
                    if (a1 < screen.worldMap.ObjectsMap.length)
                    {
                        if (b1 < screen.worldMap.ObjectsMap[a1].length)
                        {
                            for (int c = 0; c < screen.worldMap.ObjectsMap[a1][b1].size(); c++)
                            {
                                if (screen.worldMap.ObjectsMap[a1][b1].get(c) != player)
                                {
                                    if (!(screen.worldMap.ObjectsMap[a1][b1].get(c) instanceof Projectile))
                                    {
                                        BoundingBox bb = screen.worldMap.ObjectsMap[a1][b1].get(c).getBoundingBox();
                                        IRenderableGameObject irgo = screen.worldMap.ObjectsMap[a1][b1].get(c);
                                        if (irgo instanceof  Player)
                                        {
                                            int zz = 0;
                                            zz++;
                                        }
                                        //if (a < 0 || b < 0 || a >= screen.worldMap.ObjectsMap.length || b >= screen.worldMap.ObjectsMap[0].length)
                                        if (aCorrection != 0 || bCorrection != 0)
                                        {
                                            bb = new BoundingBox(new Vector3D(bb.bottomLeftFront.getX() + (aCorrection * GenerateTerrain.Size), bb.bottomLeftFront.getY() + (bCorrection * GenerateTerrain.Size), bb.bottomLeftFront.getZ()),
                                                    new Vector3D(bb.topRightBack.getX() + (aCorrection * GenerateTerrain.Size), bb.topRightBack.getY() + (bCorrection * GenerateTerrain.Size), bb.topRightBack.getZ()));
                                        }
                                        if (playerBox.CollideAABB(bb)
                                        || bb.CollideAABB(playerBox))
                                        {
                                            IRenderableGameObject ir = screen.worldMap.ObjectsMap[a1][b1].get(c);
                                            return screen.worldMap.ObjectsMap[a1][b1].get(c);
                                            /*if (highestThatCollide == null)
                                            {
                                                return screen.worldMap.ObjectsMap[a][b].get(c);
                                            }
                                            else if (highestThatCollide.getBoundingSphere().z + highestThatCollide.getBoundingSphere().r > 
                                                    screen.worldMap.ObjectsMap[a][b].get(c).getBoundingSphere().z + screen.worldMap.ObjectsMap[a][b].get(c).getBoundingSphere().r)
                                            {
                                                highestThatCollide = screen.worldMap.ObjectsMap[a][b].get(c);
                                            }*/
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            return null;
        }
        
        
        private IRenderableGameObject ReturnsHighestObjectsOnMapBelowP(Screen screen, BoundingBox playerBox, Player player)
        {
            IRenderableGameObject highestThatCollide = null;
            int x = (int)(player.ViewFrom[0] / GenerateTerrain.Size);
            int y = (int)(player.ViewFrom[1] / GenerateTerrain.Size);
            for (int a = x - Configurations.MaximalObjectSize; a <=  x + Configurations.MaximalObjectSize; a++)
                for (int b = y - Configurations.MaximalObjectSize; b <= y + Configurations.MaximalObjectSize; b++)
                {
                    int a1 = a;
                    int b1 = b;
                    int aCorrection = 0;
                    int bCorrection = 0; 
                    if (a1 < 0) 
                    {
                        a1 = screen.worldMap.ObjectsMap.length + a1;
                        aCorrection = a;
                    }
                    if (a1 >= screen.worldMap.ObjectsMap.length)
                    {
                        a1 -= screen.worldMap.ObjectsMap.length;
                        aCorrection = a;
                    }
                    if (b1 < 0) 
                    {
                        b1 = screen.worldMap.ObjectsMap[0].length + b1;
                        bCorrection = b;
                    }
                    if (b1 >= screen.worldMap.ObjectsMap[0].length)
                    {
                        b1 -= screen.worldMap.ObjectsMap[0].length;
                        bCorrection = b;
                    }
                    
                    if (a1 < screen.worldMap.ObjectsMap.length)
                    {
                        if (b1 < screen.worldMap.ObjectsMap[a1].length)
                        {
                            for (int c = 0; c < screen.worldMap.ObjectsMap[a1][b1].size(); c++)
                            {
                                if (screen.worldMap.ObjectsMap[a1][b1].get(c) != player)
                                {
                                    if (!(screen.worldMap.ObjectsMap[a1][b1].get(c) instanceof Projectile))
                                    {
                                        BoundingBox bb = screen.worldMap.ObjectsMap[a1][b1].get(c).getBoundingBox();
                                        //if (a < 0 || b < 0 || a >= screen.worldMap.ObjectsMap.length || b >= screen.worldMap.ObjectsMap[0].length)
                                        if (aCorrection != 0 || bCorrection != 0)
                                        {
                                            bb = new BoundingBox(new Vector3D(bb.bottomLeftFront.getX() + (aCorrection * GenerateTerrain.Size), bb.bottomLeftFront.getY() + (bCorrection * GenerateTerrain.Size), bb.bottomLeftFront.getZ()),
                                                    new Vector3D(bb.topRightBack.getX() + (aCorrection * GenerateTerrain.Size), bb.topRightBack.getY() + (bCorrection * GenerateTerrain.Size), bb.topRightBack.getZ()));
                                        }
                                         if ((playerBox.CollideAABB2D(bb) 
                                                || bb.CollideAABB2D(playerBox)) && playerBox.bottomLeftFront.getZ() + 0.01 > bb.topRightBack.getZ())
                                        {
                                            if (highestThatCollide == null)
                                            {
                                                highestThatCollide = screen.worldMap.ObjectsMap[a1][b1].get(c);
                                            }
                                            else if (highestThatCollide.getBoundingBox().topRightBack.getZ() < 
                                                    screen.worldMap.ObjectsMap[a1][b1].get(c).getBoundingBox().topRightBack.getZ())
                                            {
                                                highestThatCollide = screen.worldMap.ObjectsMap[a1][b1].get(c);
                                            }
                                        /*
                                        if (playerBox.CollideAABB2D(screen.worldMap.ObjectsMap[a1][b1].get(c).getBoundingBox()) 
                                                || screen.worldMap.ObjectsMap[a1][b1].get(c).getBoundingBox().CollideAABB2D(playerBox))
                                        {
                                            if (highestThatCollide == null)
                                            {
                                                highestThatCollide = screen.worldMap.ObjectsMap[a1][b1].get(c);
                                            }
                                            else if (highestThatCollide.getBoundingBox().topRightBack.getZ() < 
                                                    screen.worldMap.ObjectsMap[a1][b1].get(c).getBoundingBox().topRightBack.getZ())
                                            {
                                                highestThatCollide = screen.worldMap.ObjectsMap[a1][b1].get(c);
                                            }
                                        }*/
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            return highestThatCollide;
        }
        
        private IRenderableGameObject ReturnsHighestObjectsOnMap(Screen screen, BoundingBox playerBox, Player player)
        {
            IRenderableGameObject highestThatCollide = null;
            int x = (int)(player.ViewFrom[0] / GenerateTerrain.Size);
            int y = (int)(player.ViewFrom[1] / GenerateTerrain.Size);
            for (int a = x - Configurations.MaximalObjectSize; a <=  x + Configurations.MaximalObjectSize; a++)
                for (int b = y - Configurations.MaximalObjectSize; b <= y + Configurations.MaximalObjectSize; b++)
                {
                    int a1 = a;
                    int b1 = b;
                    int aCorrection = 0;
                    int bCorrection = 0; 
                    if (a1 < 0) 
                    {
                        a1 = screen.worldMap.ObjectsMap.length + a1;
                        aCorrection = a;
                    }
                    if (a1 >= screen.worldMap.ObjectsMap.length)
                    {
                        a1 -= screen.worldMap.ObjectsMap.length;
                        aCorrection = a;
                    }
                    if (b1 < 0) 
                    {
                        b1 = screen.worldMap.ObjectsMap[0].length + b1;
                        bCorrection = b;
                    }
                    if (b1 >= screen.worldMap.ObjectsMap[0].length)
                    {
                        b1 -= screen.worldMap.ObjectsMap[0].length;
                        bCorrection = b;
                    }
                    
                    if (a1 < screen.worldMap.ObjectsMap.length)
                    {
                        if (b1 < screen.worldMap.ObjectsMap[a1].length)
                        {
                            for (int c = 0; c < screen.worldMap.ObjectsMap[a1][b1].size(); c++)
                            {
                                if (screen.worldMap.ObjectsMap[a1][b1].get(c) != player)
                                {
                                    if (!(screen.worldMap.ObjectsMap[a1][b1].get(c) instanceof Projectile))
                                    {
                                        BoundingBox bb = screen.worldMap.ObjectsMap[a1][b1].get(c).getBoundingBox();
                                        //if (a < 0 || b < 0 || a >= screen.worldMap.ObjectsMap.length || b >= screen.worldMap.ObjectsMap[0].length)
                                        if (aCorrection != 0 || bCorrection != 0)
                                        {
                                            bb = new BoundingBox(new Vector3D(bb.bottomLeftFront.getX() + (aCorrection * GenerateTerrain.Size), bb.bottomLeftFront.getY() + (bCorrection * GenerateTerrain.Size), bb.bottomLeftFront.getZ()),
                                                    new Vector3D(bb.topRightBack.getX() + (aCorrection * GenerateTerrain.Size), bb.topRightBack.getY() + (bCorrection * GenerateTerrain.Size), bb.topRightBack.getZ()));
                                        }
                                         if (playerBox.CollideAABB2D(bb) 
                                                || bb.CollideAABB2D(playerBox))
                                        {
                                            if (highestThatCollide == null)
                                            {
                                                highestThatCollide = screen.worldMap.ObjectsMap[a1][b1].get(c);
                                            }
                                            else if (highestThatCollide.getBoundingBox().topRightBack.getZ() < 
                                                    screen.worldMap.ObjectsMap[a1][b1].get(c).getBoundingBox().topRightBack.getZ())
                                            {
                                                highestThatCollide = screen.worldMap.ObjectsMap[a1][b1].get(c);
                                            }
                                        /*
                                        if (playerBox.CollideAABB2D(screen.worldMap.ObjectsMap[a1][b1].get(c).getBoundingBox()) 
                                                || screen.worldMap.ObjectsMap[a1][b1].get(c).getBoundingBox().CollideAABB2D(playerBox))
                                        {
                                            if (highestThatCollide == null)
                                            {
                                                highestThatCollide = screen.worldMap.ObjectsMap[a1][b1].get(c);
                                            }
                                            else if (highestThatCollide.getBoundingBox().topRightBack.getZ() < 
                                                    screen.worldMap.ObjectsMap[a1][b1].get(c).getBoundingBox().topRightBack.getZ())
                                            {
                                                highestThatCollide = screen.worldMap.ObjectsMap[a1][b1].get(c);
                                            }
                                        }*/
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            return highestThatCollide;
        }
       
        double stepUpEps = 0.1 * GenerateTerrain.Size;
        double collisionEps = 0;
        
        IRenderableGameObject MoveTo2(double x, double y, double z, Player p, Screen screen)
	{
            double[]pp = new double[]{x,y,z};
            
            worldMap.CorrectPosition(pp, this);
            
            double groundPosition = worldMap.GetGroundPosition(pp, this.worldMap);
            double soilidGroundPosition = groundPosition;
            //BoundingSphere bs = new BoundingSphere(pp[0], pp[1], pp[2] - (player.headPosition / 2.0) + stepUpEps, player.boundingSphere.r);
            
            //BoundingBox bb = new BoundingBox(pp[0] - 0.5 - collisionEps, pp[1] - 0.5 - collisionEps, pp[2] - p.headPosition + stepUpEps, 
              //          pp[0] + 0.5 + collisionEps, pp[1] + 0.5 + collisionEps, pp[2] + stepUpEps, true);
            BoundingBox bb = new BoundingBox(pp[0] - 2 - collisionEps, pp[1] - 2 - collisionEps, pp[2] - p.headPosition + stepUpEps, 
                        pp[0] + 2 + collisionEps, pp[1] + 2 + collisionEps, pp[2] + stepUpEps, true);
            
            IRenderableGameObject collision = CoolideWithGameObjectsOnMap(screen, bb, p);
            if (collision != null)
            {
                bb = new BoundingBox(p.ViewFrom[0] - 2 + collisionEps, p.ViewFrom[1] - 2 + collisionEps, p.ViewFrom[2] - p.headPosition + stepUpEps, 
                            p.ViewFrom[0] + 2 - collisionEps, p.ViewFrom[1] + 2 - collisionEps, p.ViewFrom[2] + stepUpEps, true);
            }
            //IRenderableGameObject highestThatCollide = ReturnsHighestObjectsOnMap(screen, bb, p);
            
            IRenderableGameObject highestThatCollide = ReturnsHighestObjectsOnMapBelowP(screen, bb, p);
            
            if (highestThatCollide != null)
                //if (groundPosition < highestThatCollide.getBoundingSphere().z + highestThatCollide.getBoundingSphere().r)
                if (groundPosition < highestThatCollide.getBoundingBox().topRightBack.getZ())
                    groundPosition = highestThatCollide.getBoundingBox().topRightBack.getZ();
            
            
            if (collision != null)
                System.out.println("Collision:" + collision.getX() + " " + collision.getY());
            //groundPosition += player.headPosition;
            if (p.jumpProjectile != null)
                {
                    p.ViewFrom[2] = p.jumpProjectile.actualPosition.getZ();
                    if (p.ViewFrom[2] < groundPosition + p.headPosition)
                    {
                        p.ViewFrom[2] = groundPosition + p.headPosition;
                        if (p.jumpProjectile.moveDirection.getZ() < 0)
                            p.jumpProjectile = null;
                    }
                    if (collision == null)
                    {
                        p.ViewFrom[0] = pp[0];
                        p.ViewFrom[1] = pp[1];
                    }
                }
            else
            {
                if (collision == null)
                {
                    p.ViewFrom[0] = pp[0];
                    p.ViewFrom[1] = pp[1];
                    //free fall
                    if (p.ViewFrom[2] + 0.01 > groundPosition + p.headPosition)
                    {
                        Vector3D[] v = new Vector3D[1];
                        v[0] = new Vector3D(0,0,0);
                        p.jumpProjectile = new Projectile(null,Vector3D.ZERO, new Vector3D(p.ViewFrom[0], p.ViewFrom[1], p.ViewFrom[2]), v, true, this, null, 8, new Object[]{p});
                    }
                    else
                    {
                        p.ViewFrom[2] = groundPosition + p.headPosition;
                    }
                }
            }

            p.boundingBox = new BoundingBox(p.ViewFrom[0] - 2, p.ViewFrom[1] - 2, p.ViewFrom[2] - p.headPosition, 
                        p.ViewFrom[0] + 2, p.ViewFrom[1] + 2, p.ViewFrom[2], true);
            if (collision != null)
                System.out.print("__Collision:" + collision.getX() + " " + collision.getY());
            return collision;
	}
        
        
	void MoveTo(double x, double y, double z, Player player)
	{
		player.ViewFrom[0] = x;
		player.ViewFrom[1] = y;
		player.ViewFrom[2] = z;
                
                worldMap.CorrectPosition(player.ViewFrom, this);
                
                double groundPosition = worldMap.GetGroundPosition(player.ViewFrom, this.worldMap) + (2.0 * GenerateTerrain.Size);
                if (player.jumpProjectile != null)
                {
                    player.ViewFrom[2] = player.jumpProjectile.actualPosition.getZ();
                    if (player.ViewFrom[2] < groundPosition)
                    {
                        player.ViewFrom[2] = groundPosition;
                        if (player.jumpProjectile.moveDirection.getZ() < 0)
                            player.jumpProjectile = null;
                    }
                }
                else
                {
                    player.ViewFrom[2] = worldMap.GetGroundPosition(player.ViewFrom, this.worldMap) + player.headPosition;
                }
                
                
		updateView();
	}

	void setPolygonOver()
	{
		PolygonOver = null;
		for(int i = OrderedPolygons.size()-1; i >= 0; i--)
			if(OrderedPolygons.get(i).DrawablePolygon.MouseOver() && OrderedPolygons.get(i).draw 
					&& OrderedPolygons.get(i).DrawablePolygon.visible)
			{
				PolygonOver = OrderedPolygons.get(i).DrawablePolygon;
				return;
			}
	}

        double OldMouseX = Double.NaN;
        double OldMouseY = Double.NaN;
        
        void MouseMovement(double time)
        {
            MouseMovement(MouseX, MouseY, time);
        }
	void MouseMovement(double NewMouseX, double NewMouseY, double time)
	{
            /*double difX2 = (double)(parent.getSize().getWidth() / 2) - NewMouseX;
            double difY2 = (double)(parent.getSize().getHeight() / 2) - NewMouseY;
            if (Math.abs(difX2) > parent.getSize().getWidth() * 0.05)
            {
                HorLook -= Math.signum(difX2) * time * 1.0;
            }
            if (Math.abs(difY2) > parent.getSize().getHeight() * 0.05)
            {
                VertLook += Math.signum(difY2) * time * 1.0;
            }
            
            if(VertLook>0.999)
                VertLook = 0.999;

            if(VertLook<-0.999)
                VertLook = -0.999;
			updateView();
            boolean bb = true;
            if (bb)
                return;*/
            
            
            if (Double.isNaN(OldMouseX))
            {
                OldMouseX = NewMouseX;
                OldMouseY = NewMouseY;        
            }

            
                        
                        /*
                        double difX = (OldMouseX - NewMouseX) / (double)(parent.getSize().getWidth() / 2);
                        double difY = (OldMouseY - NewMouseY) / (double)(parent.getSize().getHeight() / 2);
                        
                        System.out.println(difX + " " + difY);
                        VertLook += difY;
			HorLook -= difX;
                        
                        double safeBorderX = (double)(parent.getSize().getWidth() * 0.025);
                        double safeBorderY = (double)(parent.getSize().getHeight()* 0.025);
                        
                        OldMouseX = NewMouseX;
                        OldMouseY = NewMouseY;
                        if (OldMouseX < safeBorderX)
                            OldMouseX += 2;
                        if (OldMouseX > parent.getSize().getWidth() - safeBorderX)
                            OldMouseX -= 2;
                        
                        
                        if (OldMouseY < safeBorderY)
                            OldMouseY += 2;
                        if (OldMouseY > parent.getSize().getHeight()- safeBorderY)
                            OldMouseY -= 2;
                        */
                        
            double difX = (NewMouseX - (parent.getSize().getWidth()/2 + parent.getX()));
                        double difY = (NewMouseY - (parent.getSize().getHeight()/2 + parent.getY()));
                        //VertLook += difY;
                        //HorLook -= difX;
                        
			//difY *= 6 - Math.abs(VertLook) * 5;
			VertLook -= difY  / VertRotSpeed;
			HorLook += difX / HorRotSpeed;
                        
			if(VertLook>0.999)
				VertLook = 0.999;
	
			if(VertLook<-0.999)
				VertLook = -0.999;
			
                        if(VertLook>0.999)
				VertLook = 0.999;
	
			if(VertLook<-0.999)
				VertLook = -0.999;
			updateView();
	}
	
	void updateView()
	{
		double r = Math.sqrt(1 - (VertLook * VertLook));
		player.ViewTo[0] = player.ViewFrom[0] + r * Math.cos(HorLook);
		player.ViewTo[1] = player.ViewFrom[1] + r * Math.sin(HorLook);		
		player.ViewTo[2] = player.ViewFrom[2] + VertLook;
	}
	
        private boolean correctMouse = false;
	void CenterMouse() 
	{
            
            try {
                    r = new Robot();
                    //r.mouseMove((int)DDDTutorial.ScreenSize.getWidth()/2, (int)DDDTutorial.ScreenSize.getHeight()/2);
                    Point p = new Point((int)parent.getSize().getWidth()/2 + parent.getX(), (int)parent.getSize().getHeight()/2 + parent.getY());
                    //SwingUtilities.convertPointToScreen(p, parent);
                    //SwingUtilities.convertPointFromScreen(p, parent);
                    //correctMouse = true;
                    //moveMouse(p);
                    
                    //this.removeMouseListener(this);
                    //this.removeMouseMotionListener(this);
                    //r.mouseMove(p.x+8, p.y+29);
                    if (parent.getFocusOwner() == this)
                        r.mouseMove(p.x, p.y);
                    
                    //correctMouse = false;
                    //this.addMouseListener(this);
                    //this.addMouseMotionListener(this);
                    
            } catch (AWTException e) {
                    e.printStackTrace();
            }
	}
	

        
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_W)
			Keys[0] = true;
		if(e.getKeyCode() == KeyEvent.VK_A)
			Keys[1] = true;
		if(e.getKeyCode() == KeyEvent.VK_S)
			Keys[2] = true;
		if(e.getKeyCode() == KeyEvent.VK_D)
			Keys[3] = true;
		if(e.getKeyCode() == KeyEvent.VK_O)
			OutLines = !OutLines;
                if(e.getKeyCode() == KeyEvent.VK_SPACE)
			Keys[4] = true;
                if(e.getKeyCode() == KeyEvent.VK_CONTROL)
			Keys[5] = true;
                if(e.getKeyCode() == KeyEvent.VK_Q)
			Keys[6] = true;
                if(e.getKeyCode() == KeyEvent.VK_E)
			Keys[7] = true;
                if(e.getKeyCode() == KeyEvent.VK_T)
			Keys[8] = true;
                if(e.getKeyCode() == KeyEvent.VK_P)
			Keys[9] = true;
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
			System.exit(0);
                
	}

	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_W)
                {
			Keys[0] = false;
                        PrevKeys[0] = false;
                }
		if(e.getKeyCode() == KeyEvent.VK_A)
                {
			Keys[1] = false;
                        PrevKeys[1] = false;
                }
		if(e.getKeyCode() == KeyEvent.VK_S)
                {
			Keys[2] = false;
                        PrevKeys[2] = false;
                }
		if(e.getKeyCode() == KeyEvent.VK_D)
                {
			Keys[3] = false;
                        PrevKeys[3] = false;
                }
                if(e.getKeyCode() == KeyEvent.VK_SPACE)
                {
			Keys[4] = false;
                        PrevKeys[4] = false;
                }
                if(e.getKeyCode() == KeyEvent.VK_CONTROL)
                {
			Keys[5] = false;
                        PrevKeys[5] = false;
                }
                if(e.getKeyCode() == KeyEvent.VK_Q)
                {
			Keys[6] = false;
                        PrevKeys[6] = false;
                }
                if(e.getKeyCode() == KeyEvent.VK_E)
                {
			Keys[7] = false;
                        PrevKeys[7] = false;
                }
                if(e.getKeyCode() == KeyEvent.VK_T)
                {
			Keys[8] = false;
                        PrevKeys[8] = false;
                }
                if(e.getKeyCode() == KeyEvent.VK_P)
                {
			Keys[9] = false;
                        PrevKeys[9] = false;
                }
	}

	public void keyTyped(KeyEvent e) {
	}

	public void mouseDragged(MouseEvent arg0) {
            /*if (correctMouse)
            {
                correctMouse = false;
                return;
            }*/
		//MouseMovement(arg0.getX(), arg0.getY(),0);
		//MouseX = arg0.getX();
		//MouseY = arg0.getY();
		//CenterMouse();
	}
	
	public void mouseMoved(MouseEvent arg0) {
            /*if (correctMouse)
            {
                correctMouse = false;
                return;
            }*/
		//MouseMovement(arg0.getX(), arg0.getY(),0);
		//MouseX = arg0.getX();
		//MouseY = arg0.getY();
		//CenterMouse();
	}
	
	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
		if(arg0.getButton() == MouseEvent.BUTTON1)
			if(PolygonOver != null)
				PolygonOver.seeThrough = false;

		if(arg0.getButton() == MouseEvent.BUTTON3)
			if(PolygonOver != null)
				PolygonOver.seeThrough = true;
	}

	public void mouseReleased(MouseEvent arg0) {
	}

	public void mouseWheelMoved(MouseWheelEvent arg0) {
            if(arg0.getUnitsToScroll()>0)
            {
                if(zoom > MinZoom)
                        zoom -= 25 * arg0.getUnitsToScroll();
            }
            else
            {
                if(zoom < MaxZoom)
                        zoom -= 25 * arg0.getUnitsToScroll();
            }	
	}
}
