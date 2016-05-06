package Engine;

import java.awt.Color;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

public class Cube implements IRenderableGameObject{
	double x, y, z, width, length, height, rotation = Math.PI*0.75;
	double[] RotAdd = new double[4];
	Color c;
	double x1, x2, x3, x4, y1, y2, y3, y4;
	DPolygon[] Polys = new DPolygon[6];
	double[] angle;
	
        public DPolygon[] GetPolygons()
        {
            /*
            DPolygon[]re = new DPolygon[Polys.length];
            for (int a = 0; a < Polys.length; a++)
            {
                re[a] = new DPolygon(new double[]{0,0,0}, new double[]{0,0,0}, Polys[a].z, Polys[a].c, Polys[a].seeThrough);
                
                double[]aa = new double[]{Polys[a].x[0], Polys[a].y[0]};
                screen.worldMap.CorrectPosition(aa, screen);
                ra[a].
                
            }*/
            return Polys;
        }
        
        //BoundingSphere boundingSphere = null;
        public BoundingBox boundingBox = null;
        public Object[]ownerId = null;
        
	public Cube(double x, double y, double z, double width, double length, double height, Color c, Object[]ownerId)
	{
		Polys[0] = new DPolygon(new double[]{x, x+width, x+width, x}, new double[]{y, y, y+length, y+length},  new double[]{z, z, z, z}, c, false, ownerId);
		//Screen.DPolygons.add(Polys[0]);
		Polys[1] = new DPolygon(new double[]{x, x+width, x+width, x}, new double[]{y, y, y+length, y+length},  new double[]{z+height, z+height, z+height, z+height}, c, false, ownerId);
		//Screen.DPolygons.add(Polys[1]);
		Polys[2] = new DPolygon(new double[]{x, x, x+width, x+width}, new double[]{y, y, y, y},  new double[]{z, z+height, z+height, z}, c, false, ownerId);
		//Screen.DPolygons.add(Polys[2]);
		Polys[3] = new DPolygon(new double[]{x+width, x+width, x+width, x+width}, new double[]{y, y, y+length, y+length},  new double[]{z, z+height, z+height, z}, c, false, ownerId);
		//Screen.DPolygons.add(Polys[3]);
		Polys[4] = new DPolygon(new double[]{x, x, x+width, x+width}, new double[]{y+length, y+length, y+length, y+length},  new double[]{z, z+height, z+height, z}, c, false, ownerId);
		//Screen.DPolygons.add(Polys[4]);
		Polys[5] = new DPolygon(new double[]{x, x, x, x}, new double[]{y, y, y+length, y+length},  new double[]{z, z+height, z+height, z}, c, false, ownerId);
		//Screen.DPolygons.add(Polys[5]);
		
		this.c = c;
		this.x = x;
		this.y = y;
		this.z = z;
		this.width = width;
		this.length = length;
		this.height = height;
                this.ownerId = ownerId;
		
                //boundingSphere = new BoundingSphere(x - (width / 2.0), y - (length / 2.0), z - (height / 2.0), Math.max(Math.max(width, length), height));
                //boundingSphere = new BoundingSphere(x + (width / 2.0), y + (length / 2.0), z + (height / 2.0), Math.max(Math.max(width / 2.0, length / 2.0), height / 2.0));
                boundingBox = new BoundingBox(x, y, z, x+width, y+length, z+height, true);
                        
		setRotAdd();
		updatePoly();
	}
	
        private static double rotatePointX(double xx, double yy, double originX, double originY, double rotation)
        {
            double xx2 = (Math.cos(rotation) * (xx - originX)) - (Math.sin(rotation) * (yy - originY)) + originX;
            return xx2;
        }
        
        private static double rotatePointY(double xx, double yy, double originX, double originY, double rotation)
        {
            double yy2 = (Math.sin(rotation) * (xx - originX)) + (Math.cos(rotation) * (yy - originY)) + originY;
            return yy2;
        }
        
        private static double[]rotatePoint(double xx, double yy, double originX, double originY, double rotation)
        {
            double xx2 = (Math.cos(rotation) * (xx - originX)) - (Math.sin(rotation) * (yy - originY)) + originX;
            double yy2 = (Math.sin(rotation) * (xx - originX)) + (Math.cos(rotation) * (yy - originY)) + originY;
            return new double[]{xx2, yy2};
        }
        
        public Cube(double x, double y, double z, double width, double length, double height, Color c, Object[]ownerId, double originX, double originY, double rotation)
	{
            double []xy1 = rotatePoint(x, y, originX, originY, rotation);
            double []xy2 = rotatePoint(x +width, y, originX, originY, rotation);
            double []xy3 = rotatePoint(x, y+length, originX, originY, rotation);
            double []xy4 = rotatePoint(x +width, y+length, originX, originY, rotation);
		Polys[0] = new DPolygon(new double[]{xy1[0], xy2[0], xy4[0], xy3[0]}, new double[]{xy1[1], xy2[1], xy4[1], xy3[1]},  new double[]{z, z, z, z}, c, false, ownerId);
		//Screen.DPolygons.add(Polys[0]);
		Polys[1] = new DPolygon(new double[]{xy1[0], xy2[0], xy4[0], xy3[0]}, new double[]{xy1[1], xy2[1], xy4[1], xy3[1]},  new double[]{z+height, z+height, z+height, z+height}, c, false, ownerId);
		//Screen.DPolygons.add(Polys[1]);
		Polys[2] = new DPolygon(new double[]{xy1[0], xy1[0], xy2[0], xy2[0]}, new double[]{xy1[1], xy1[1], xy2[1], xy2[1]},  new double[]{z, z+height, z+height, z}, c, false, ownerId);
		//Screen.DPolygons.add(Polys[2]);
		Polys[3] = new DPolygon(new double[]{xy2[0], xy2[0], xy4[0], xy4[0]}, new double[]{xy2[1], xy2[1], xy4[1], xy4[1]},  new double[]{z, z+height, z+height, z}, c, false, ownerId);
		//Screen.DPolygons.add(Polys[3]);
		Polys[4] = new DPolygon(new double[]{xy3[0], xy3[0], xy4[0], xy4[0]}, new double[]{xy3[1], xy3[1], xy4[1], xy4[1]},  new double[]{z, z+height, z+height, z}, c, false, ownerId);
		//Screen.DPolygons.add(Polys[4]);
		Polys[5] = new DPolygon(new double[]{xy1[0], xy1[0], xy3[0], xy3[0]}, new double[]{xy1[1], xy1[1], xy3[1], xy3[1]},  new double[]{z, z+height, z+height, z}, c, false, ownerId);
		//Screen.DPolygons.add(Polys[5]);
		
		this.c = c;
		this.x = x;
		this.y = y;
		this.z = z;
		this.width = width;
		this.length = length;
		this.height = height;
                this.ownerId = ownerId;
		
                //boundingSphere = new BoundingSphere(x - (width / 2.0), y - (length / 2.0), z - (height / 2.0), Math.max(Math.max(width, length), height));
                //boundingSphere = new BoundingSphere(x + (width / 2.0), y + (length / 2.0), z + (height / 2.0), Math.max(Math.max(width / 2.0, length / 2.0), height / 2.0));
                boundingBox = new BoundingBox(x, y, z, x+width, y+length, z+height, true);
                        
		setRotAdd();
		updatePoly();
	}
        
	void setRotAdd()
	{
		angle = new double[4];
		
		double xdif = - width/2 + 0.00001;
		double ydif = - length/2 + 0.00001;
		
		angle[0] = Math.atan(ydif/xdif);
		
		if(xdif<0)
			angle[0] += Math.PI;
		
/////////
		xdif = width/2 + 0.00001;
		ydif = - length/2 + 0.00001;
		
		angle[1] = Math.atan(ydif/xdif);
		
		if(xdif<0)
			angle[1] += Math.PI;
/////////
		xdif = width/2 + 0.00001;
		ydif = length/2 + 0.00001;
		
		angle[2] = Math.atan(ydif/xdif);
		
		if(xdif<0)
			angle[2] += Math.PI;
		
/////////
		xdif = - width/2 + 0.00001;
		ydif = length/2 + 0.00001;
		
		angle[3] = Math.atan(ydif/xdif);
		
		if(xdif<0)
			angle[3] += Math.PI;	
		
		RotAdd[0] = angle[0] + 0.25 * Math.PI;
		RotAdd[1] =	angle[1] + 0.25 * Math.PI;
		RotAdd[2] = angle[2] + 0.25 * Math.PI;
		RotAdd[3] = angle[3] + 0.25 * Math.PI;

	}
	
	void UpdateDirection(double toX, double toY)
	{
		double xdif = toX - (x + width/2) + 0.00001;
		double ydif = toY - (y + length/2) + 0.00001;
		
		double anglet = Math.atan(ydif/xdif) + 0.75 * Math.PI;

		if(xdif<0)
			anglet += Math.PI;

		rotation = anglet;
		updatePoly();		
	}

        
        
	void updatePoly()
	{
		/*for(int i = 0; i < 6; i++)
		{
			Screen.DPolygons.add(Polys[i]);
			Screen.DPolygons.remove(Polys[i]);
		}*/
		
		double radius = Math.sqrt(width*width + length*length);
		
			   x1 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[0]);
			   x2 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[1]);
			   x3 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[2]);
			   x4 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[3]);
			   
			   y1 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[0]);
			   y2 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[1]);
			   y3 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[2]);
			   y4 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[3]);
   
		Polys[0].x = new double[]{x1, x2, x3, x4};
		Polys[0].y = new double[]{y1, y2, y3, y4};
		Polys[0].z = new double[]{z, z, z, z};

		Polys[1].x = new double[]{x4, x3, x2, x1};
		Polys[1].y = new double[]{y4, y3, y2, y1};
		Polys[1].z = new double[]{z+height, z+height, z+height, z+height};
			   
		Polys[2].x = new double[]{x1, x1, x2, x2};
		Polys[2].y = new double[]{y1, y1, y2, y2};
		Polys[2].z = new double[]{z, z+height, z+height, z};

		Polys[3].x = new double[]{x2, x2, x3, x3};
		Polys[3].y = new double[]{y2, y2, y3, y3};
		Polys[3].z = new double[]{z, z+height, z+height, z};

		Polys[4].x = new double[]{x3, x3, x4, x4};
		Polys[4].y = new double[]{y3, y3, y4, y4};
		Polys[4].z = new double[]{z, z+height, z+height, z};

		Polys[5].x = new double[]{x4, x4, x1, x1};
		Polys[5].y = new double[]{y4, y4, y1, y1};
		Polys[5].z = new double[]{z, z+height, z+height, z};
		
	}
        
        void updatePoly2(double xx, double yy, double originX, double originY)
	{
		/*for(int i = 0; i < 6; i++)
		{
			Screen.DPolygons.add(Polys[i]);
			Screen.DPolygons.remove(Polys[i]);
		}*/
                //double xx2 = (Math.cos(rotation) * (xx - originX)) - (Math.sin(rotation) * (yy - originY)) + originX;
                //double yy2 = (Math.cos(rotation) * (xx - originX)) - (Math.sin(rotation) * (yy - originY)) + originX;
                
                double xx2 = 0;//(Math.cos(rotation) * (xx - originX)) - (Math.sin(rotation) * (yy - originY));
                double yy2 = 0;//(Math.sin(rotation) * (xx - originX)) + (Math.cos(rotation) * (yy - originY));
		
		double radius = Math.sqrt(width*width + length*length);
                
                           x1 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[0]) + xx2;
			   x2 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[1]) + xx2;
			   x3 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[2]) + xx2;
			   x4 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[3]) + xx2;
			   
			   y1 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[0]) + yy2;
			   y2 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[1]) + yy2;
			   y3 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[2]) + yy2;
			   y4 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[3]) + yy2;
		/*
			   x1 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[0]) + (Math.cos(rotation + RotAdd[0]) * (xx - originX)) - (Math.sin(rotation + RotAdd[0]) * (yy - originY));
			   x2 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[1]) + (Math.cos(rotation + RotAdd[1]) * (xx - originX)) - (Math.sin(rotation + RotAdd[1]) * (yy - originY));
			   x3 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[2]) + (Math.cos(rotation + RotAdd[2]) * (xx - originX)) - (Math.sin(rotation + RotAdd[2]) * (yy - originY));
			   x4 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[3]) + (Math.cos(rotation + RotAdd[3]) * (xx - originX)) - (Math.sin(rotation + RotAdd[3]) * (yy - originY));
			   
			   y1 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[0]) + (Math.sin(rotation + RotAdd[0]) * (xx - originX)) + (Math.cos(rotation + RotAdd[0]) * (yy - originY));
			   y2 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[1]) + (Math.sin(rotation + RotAdd[1]) * (xx - originX)) + (Math.cos(rotation + RotAdd[1]) * (yy - originY));
			   y3 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[2]) + (Math.sin(rotation + RotAdd[2]) * (xx - originX)) + (Math.cos(rotation + RotAdd[2]) * (yy - originY));
			   y4 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[3]) + (Math.sin(rotation + RotAdd[3]) * (xx - originX)) + (Math.cos(rotation + RotAdd[3]) * (yy - originY));
   */
		Polys[0].x = new double[]{x1, x2, x3, x4};
		Polys[0].y = new double[]{y1, y2, y3, y4};
		Polys[0].z = new double[]{z, z, z, z};

		Polys[1].x = new double[]{x4, x3, x2, x1};
		Polys[1].y = new double[]{y4, y3, y2, y1};
		Polys[1].z = new double[]{z+height, z+height, z+height, z+height};
			   
		Polys[2].x = new double[]{x1, x1, x2, x2};
		Polys[2].y = new double[]{y1, y1, y2, y2};
		Polys[2].z = new double[]{z, z+height, z+height, z};

		Polys[3].x = new double[]{x2, x2, x3, x3};
		Polys[3].y = new double[]{y2, y2, y3, y3};
		Polys[3].z = new double[]{z, z+height, z+height, z};

		Polys[4].x = new double[]{x3, x3, x4, x4};
		Polys[4].y = new double[]{y3, y3, y4, y4};
		Polys[4].z = new double[]{z, z+height, z+height, z};

		Polys[5].x = new double[]{x4, x4, x1, x1};
		Polys[5].y = new double[]{y4, y4, y1, y1};
		Polys[5].z = new double[]{z, z+height, z+height, z};
		
	}
        
        void updatePoly2(double originX, double originY)
	{
		/*for(int i = 0; i < 6; i++)
		{
			Screen.DPolygons.add(Polys[i]);
			Screen.DPolygons.remove(Polys[i]);
		}*/
                //double xx2 = (Math.cos(rotation) * (xx - originX)) - (Math.sin(rotation) * (yy - originY)) + originX;
                //double yy2 = (Math.cos(rotation) * (xx - originX)) - (Math.sin(rotation) * (yy - originY)) + originX;
                
                //double xx2 = (Math.cos(rotation) * (xx - originX)) - (Math.sin(rotation) * (yy - originY));
                //double yy2 = (Math.sin(rotation) * (xx - originX)) + (Math.cos(rotation) * (yy - originY));
		
		double radius = Math.sqrt(width*width + length*length);
                
                           x1 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[0]) + (Math.cos(rotation + RotAdd[0]) * (x1 - originX)) - (Math.sin(rotation + RotAdd[0]) * (y1 - originY));
			   x2 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[1]) + (Math.cos(rotation + RotAdd[1]) * (x2 - originX)) - (Math.sin(rotation + RotAdd[1]) * (y2 - originY));
			   x3 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[2]) + (Math.cos(rotation + RotAdd[2]) * (x3 - originX)) - (Math.sin(rotation + RotAdd[2]) * (y3 - originY));
			   x4 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[3]) + (Math.cos(rotation + RotAdd[3]) * (x4 - originX)) - (Math.sin(rotation + RotAdd[3]) * (y4 - originY));
			   
			   y1 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[0]) + (Math.sin(rotation + RotAdd[0]) * (x1 - originX)) + (Math.cos(rotation + RotAdd[0]) * (y1 - originY));
			   y2 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[1]) + (Math.sin(rotation + RotAdd[1]) * (x2 - originX)) + (Math.cos(rotation + RotAdd[1]) * (y2 - originY));
			   y3 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[2]) + (Math.sin(rotation + RotAdd[2]) * (x3 - originX)) + (Math.cos(rotation + RotAdd[2]) * (y3 - originY));
			   y4 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[3]) + (Math.sin(rotation + RotAdd[3]) * (x4 - originX)) + (Math.cos(rotation + RotAdd[3]) * (y4 - originY));
		/*
			   x1 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[0]) + (Math.cos(rotation + RotAdd[0]) * (xx - originX)) - (Math.sin(rotation + RotAdd[0]) * (yy - originY));
			   x2 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[1]) + (Math.cos(rotation + RotAdd[1]) * (xx - originX)) - (Math.sin(rotation + RotAdd[1]) * (yy - originY));
			   x3 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[2]) + (Math.cos(rotation + RotAdd[2]) * (xx - originX)) - (Math.sin(rotation + RotAdd[2]) * (yy - originY));
			   x4 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[3]) + (Math.cos(rotation + RotAdd[3]) * (xx - originX)) - (Math.sin(rotation + RotAdd[3]) * (yy - originY));
			   
			   y1 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[0]) + (Math.sin(rotation + RotAdd[0]) * (xx - originX)) + (Math.cos(rotation + RotAdd[0]) * (yy - originY));
			   y2 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[1]) + (Math.sin(rotation + RotAdd[1]) * (xx - originX)) + (Math.cos(rotation + RotAdd[1]) * (yy - originY));
			   y3 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[2]) + (Math.sin(rotation + RotAdd[2]) * (xx - originX)) + (Math.cos(rotation + RotAdd[2]) * (yy - originY));
			   y4 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[3]) + (Math.sin(rotation + RotAdd[3]) * (xx - originX)) + (Math.cos(rotation + RotAdd[3]) * (yy - originY));
   */
		Polys[0].x = new double[]{x1, x2, x3, x4};
		Polys[0].y = new double[]{y1, y2, y3, y4};
		Polys[0].z = new double[]{z, z, z, z};

		Polys[1].x = new double[]{x4, x3, x2, x1};
		Polys[1].y = new double[]{y4, y3, y2, y1};
		Polys[1].z = new double[]{z+height, z+height, z+height, z+height};
			   
		Polys[2].x = new double[]{x1, x1, x2, x2};
		Polys[2].y = new double[]{y1, y1, y2, y2};
		Polys[2].z = new double[]{z, z+height, z+height, z};

		Polys[3].x = new double[]{x2, x2, x3, x3};
		Polys[3].y = new double[]{y2, y2, y3, y3};
		Polys[3].z = new double[]{z, z+height, z+height, z};

		Polys[4].x = new double[]{x3, x3, x4, x4};
		Polys[4].y = new double[]{y3, y3, y4, y4};
		Polys[4].z = new double[]{z, z+height, z+height, z};

		Polys[5].x = new double[]{x4, x4, x1, x1};
		Polys[5].y = new double[]{y4, y4, y1, y1};
		Polys[5].z = new double[]{z, z+height, z+height, z};
		
	}
        
        void updatePoly(double originX, double originY)
	{
		/*for(int i = 0; i < 6; i++)
		{
			Screen.DPolygons.add(Polys[i]);
			Screen.DPolygons.remove(Polys[i]);
		}*/
		
		double radius = Math.sqrt(width*width + length*length);
		
                
                x1 = Polys[0].x[0];
                x2 = Polys[0].x[1];
                x3 = Polys[0].x[2];
                x4 = Polys[0].x[3];
                
                y1 = Polys[0].y[0];
                y2 = Polys[0].y[1];
                y3 = Polys[0].y[2];
                y4 = Polys[0].y[3];
                
                x1 = (Math.cos(rotation) * (x1 - originX)) - (Math.sin(rotation) * (y1 - originY)) + originX;
                x2 = (Math.cos(rotation) * (x2 - originX)) - (Math.sin(rotation) * (y2 - originY)) + originX;
                x3 = (Math.cos(rotation) * (x3 - originX)) - (Math.sin(rotation) * (y3 - originY)) + originX;
                x4 = (Math.cos(rotation) * (x4 - originX)) - (Math.sin(rotation) * (y4 - originY)) + originX;
                
                y1 = (Math.sin(rotation) * (x1 - originX)) + (Math.cos(rotation) * (y1 - originY)) + originY;
                y2 = (Math.sin(rotation) * (x2 - originX)) + (Math.cos(rotation) * (y2 - originY)) + originY;
                y3 = (Math.sin(rotation) * (x3 - originX)) + (Math.cos(rotation) * (y3 - originY)) + originY;
                y4 = (Math.sin(rotation) * (x4 - originX)) + (Math.cos(rotation) * (y4 - originY)) + originY;
                //p'x = cos(theta) * (px-ox) - sin(theta) * (py-oy) + ox

                //p'y = sin(theta) * (px-ox) + cos(theta) * (py-oy) + oy
                           /*y1 += translationY;
                           y2 += translationY;
                           y3 += translationY;
                           y4 += translationY;*/
                           
			   /*x1 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[0]) + translationX * Math.cos(rotation + RotAdd[0]);
			   x2 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[1]) + translationX * Math.cos(rotation + RotAdd[0]);
			   x3 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[2]) + translationX * Math.cos(rotation + RotAdd[0]);
			   x4 = x+width*0.5+radius*0.5*Math.cos(rotation + RotAdd[3]) + translationX * Math.cos(rotation + RotAdd[0]);
			   
			   y1 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[0]) + translationY *Math.sin(rotation + RotAdd[0]);
			   y2 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[1]) + translationY *Math.sin(rotation + RotAdd[0]);
			   y3 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[2]) + translationY *Math.sin(rotation + RotAdd[0]);
			   y4 = y+length*0.5+radius*0.5*Math.sin(rotation + RotAdd[3]) + translationY *Math.sin(rotation + RotAdd[0]);*/
   
		Polys[0].x = new double[]{x1, x2, x3, x4};
		Polys[0].y = new double[]{y1, y2, y3, y4};
		Polys[0].z = new double[]{z, z, z, z};

		Polys[1].x = new double[]{x4, x3, x2, x1};
		Polys[1].y = new double[]{y4, y3, y2, y1};
		Polys[1].z = new double[]{z+height, z+height, z+height, z+height};
			   
		Polys[2].x = new double[]{x1, x1, x2, x2};
		Polys[2].y = new double[]{y1, y1, y2, y2};
		Polys[2].z = new double[]{z, z+height, z+height, z};

		Polys[3].x = new double[]{x2, x2, x3, x3};
		Polys[3].y = new double[]{y2, y2, y3, y3};
		Polys[3].z = new double[]{z, z+height, z+height, z};

		Polys[4].x = new double[]{x3, x3, x4, x4};
		Polys[4].y = new double[]{y3, y3, y4, y4};
		Polys[4].z = new double[]{z, z+height, z+height, z};

		Polys[5].x = new double[]{x4, x4, x1, x1};
		Polys[5].y = new double[]{y4, y4, y1, y1};
		Polys[5].z = new double[]{z, z+height, z+height, z};
		
	}
/*
	void removeCube()
	{
		for(int i = 0; i < 6; i ++)
			Screen.DPolygons.remove(Polys[i]);
		Screen.Cubes.remove(this);
	}*/

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getDistance(double fromX, double fromY, double fromz, double x, double y, double z) {
        double xx = (x - (width / 2.0));
        double yy = (y - (length / 2.0));
        double zz = (z - (height / 2.0));
        xx = (-xx +x);
        yy = (-yy +y);
        zz = (-zz +z);
        return Math.sqrt((xx - fromX) * (xx - fromX) 
                + (yy - fromY) * (yy - fromY)
                + (zz - fromY) * (zz - fromY));
    }

    /*
    @Override
    public BoundingSphere getBoundingSphere() {
        return boundingSphere;
    }*/
    
    @Override
    public boolean Collide(IRenderableGameObject iRenderableGameObject) {
        //return iRenderableGameObject.getBoundingSphere().Collide(boundingSphere);
        return iRenderableGameObject.getBoundingBox().CollideAABB(boundingBox);
    }

    @Override
    public Object[] getOwnerId() {
        return ownerId;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }
    
}
