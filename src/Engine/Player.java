/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import Engine.AStar.AStar;
import Engine.AStar.AreaMap;
import Engine.AStar.DiagonalHeuristic;
import Engine.AStar.IAStarHeuristic;
import java.awt.Color;
import java.awt.Point;
import java.awt.TexturePaint;
import java.util.ArrayList;
import org.apache.commons.math3.geometry.euclidean.threed.Vector3D;

/**
 *
 * @author Tomus
 */
public class Player implements IRenderableGameObject{
    	double[] ViewFrom = new double[] { 0, 0, 100};
        double[] ViewTo = new double[] {0, 0, 0};
        Object []PlayerId = null;
        //public BoundingSphere boundingSphere = null;
        public BoundingBox boundingBox = null;
        public String Name = "Player";
        
        public Projectile jumpProjectile = null;
        double headPosition = 2.0 * GenerateTerrain.Size;
        
        public long calculateMoveInterval = 0;
        public long calculateMoveIntervalMax = 500;
        
        public Vector3D GetPosition()
        {
            return new Vector3D(ViewFrom[0],ViewFrom[1], ViewFrom[2] - headPosition);
        }
        
        public Vector3D MoveDirection = null;
        public Vector3D PositionIFace = null;
        
        public void AttackPlayer(Player p, WorldMap worldMap, Screen screen)
        {
            Vector3D vf = new Vector3D(ViewFrom[0], ViewFrom[1], ViewFrom[2]);
            Vector3D pp = AlgebraUtils.ReturnClosestWectorMirror(p.ViewFrom, ViewFrom, worldMap);
            //Vector3D pp = new Vector3D(p.ViewFrom[0], p.ViewFrom[1], p.ViewFrom[2]);
            Vector3D sv = pp.subtract(vf);
            screen.shoot(vf, sv, this);
        }
        
        public void ApproachPlayer(Player p, WorldMap worldMap)
        {
            MoveDirection = null;
            //PositionIFace = new Vector3D(p.ViewFrom[0], p.ViewFrom[1], p.ViewFrom[2]);
            PositionIFace = AlgebraUtils.ReturnClosestWectorMirror(p.ViewFrom, ViewFrom, worldMap);
            int algorithmSize = Math.min(GenerateTerrain.mapSize, worldMap.mapSize);
            int[][]obstacleMap = new int[algorithmSize][];
            int halfSize = algorithmSize / 2;
            int goalX = halfSize;
            int goalY = halfSize;
            for (int a = 0; a < obstacleMap.length; a++)
            {
                obstacleMap[a] = new int[algorithmSize];
            }
            for (int a = -halfSize + (int)(ViewFrom[0]  / GenerateTerrain.Size), aa = 0; a < halfSize + (int)(ViewFrom[0] / GenerateTerrain.Size) -1; a++, aa++)
                for (int b = -halfSize + (int)(ViewFrom[1]  / GenerateTerrain.Size), bb = 0; b < halfSize + (int)(ViewFrom[1] / GenerateTerrain.Size) -1; b++, bb++)
                {
                    int MapIdX = a;
                    int MapIdY = b;
                
                    MapIdX %= (worldMap.getMapSize());
                    MapIdY %= (worldMap.getMapSize());
                    if (MapIdX < 0)
                        MapIdX = worldMap.getMapSize() + MapIdX;
                    if (MapIdY < 0)
                        MapIdY = worldMap.getMapSize() + MapIdY;
                    if (worldMap.ObjectsMap[MapIdX][MapIdY].size() > 0)
                    {
                        if (worldMap.ObjectsMap[MapIdX][MapIdY].contains(p))
                        {
                            obstacleMap[aa][bb] = 0;
                            goalX = aa;
                            goalY = bb;
                        }
                        else
                            obstacleMap[aa][bb] = 1;
                    }
                    else
                        obstacleMap[aa][bb] = 0;
                }
            
            AreaMap map = new AreaMap(algorithmSize, algorithmSize, obstacleMap);
            IAStarHeuristic heuristic = new DiagonalHeuristic();
            AStar aStar = new AStar(map, heuristic);
            ArrayList<Point> shortestPath = aStar.calcShortestPath(halfSize, halfSize, goalX, goalY);
            if (shortestPath != null)
                if (shortestPath.size() > 1)
                {
                    MoveDirection = new Vector3D(shortestPath.get(0).x - halfSize, shortestPath.get(0).y - halfSize,0);
                }
            ////////////////
            //REMOVE
            //MoveDirection = Vector3D.ZERO;
        }
        
        public Player(double[] ViewFrom, double[] ViewTo)
        {
            this.ViewFrom = ViewFrom;
            this.ViewTo = ViewTo;
            this.PlayerId= new Object[]{this};
            //boundingBox = new BoundingBox(ViewFrom[0] - 0.5, ViewFrom[1] - 0.5, ViewFrom[2] - headPosition, ViewFrom[0] + 0.5, ViewFrom[1] + 0.5, ViewFrom[2], true);
            
            boundingBox = new BoundingBox(ViewFrom[0] - 1, ViewFrom[1] - 1, ViewFrom[2] - headPosition, ViewFrom[0] + 1, ViewFrom[1] + 1, ViewFrom[2], true);
            
            //boundingSphere = new BoundingSphere(ViewFrom[0], ViewFrom[1], ViewFrom[2] + (headPosition / 2.0), (headPosition / 2.0));
        }
    
        public void RenderPlayer()
        {
            
        }
    

    @Override
    public DPolygon[] GetPolygons() {
        //Cube head = new Cube(ViewFrom[0] - halfHeadSize, ViewFrom[1] - headSize, ViewFrom[2] - 2 * headSize, headSize * 2.0, headSize * 2.0, headSize * 2.0, Color.YELLOW, PlayerId);
        //return head.GetPolygons();
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private void AddArrayToArrayList(ArrayList<DPolygon>arrayList, DPolygon[]array)
    {
        for (int a = 0; a < array.length; a++)
        {
            arrayList.add(array[a]);
        }
    }
    
    double rot = 0;
    
            double halfHeadSize = 1;
        double headSize = 2;
        double halfNeckSize = 0.5;
        double neckSize = 1;
        double halfCorupsXSize = 2;
        double halfCorupsYSize = 1;

        double corupsYSize = 1;
        double halfCorpusZSize = 3;
        double corpusZSize = 6;
        double halfLegZSize = 3.75;
        //double legZSize = 7;
        //double halfLegSizeY = 0.7;
        //double legSizeY = 1.4;
        //double legSizeX = 1.3;
        
        double legZSize = 7;
        double halfLegSizeY = 1;
        //double legSizeY = 1.4;
        double halfLegSizeX = 1.5;
        
        double halfHandsYSize = 0.8;
        double halfHandsXSize = 3;
        double handsZSize = 6;
        

        double epsBody = 0.01;
    public ArrayList<DPolygon> GetPolygons(double x, double y) {
        /*Cube head = new Cube(- halfHeadSize + x, - halfHeadSize + y, ViewFrom[2] - halfHeadSize + epsBody,
            halfHeadSize * 2.0, halfHeadSize * 2.0, halfHeadSize * 2.0, Color.YELLOW, PlayerId);
        head.Polys[2].DrawablePolygon.texture = GenerateTerrain.img;
        
        
        Cube neck = new Cube(- halfNeckSize + x, - halfNeckSize + y, ViewFrom[2] - headSize + halfNeckSize,
                halfNeckSize * 2.0, halfNeckSize * 2.0, halfNeckSize, Color.PINK, PlayerId);
        Cube corpus = new Cube(- halfCorupsXSize + x, - halfCorupsYSize + y, ViewFrom[2] - corpusZSize - headSize + halfNeckSize - epsBody,
                halfCorupsXSize * 2.0, halfCorupsYSize * 2.0, 2 * halfCorpusZSize, Color.BLUE, PlayerId);
        
        Cube legs = new Cube(- halfLegSizeX + x, - halfLegSizeY + y, ViewFrom[2] - legZSize - corpusZSize - headSize + halfNeckSize - epsBody - epsBody,
                2 * halfLegSizeX, 2 * halfLegSizeY, legZSize, Color.MAGENTA, PlayerId);
       
        Cube hands = new Cube(- halfHandsXSize + x, - halfHandsYSize + y, ViewFrom[2] - handsZSize - headSize + halfNeckSize - epsBody,
                halfHandsXSize * 2.0, halfHandsYSize * 2.0, handsZSize, Color.CYAN, PlayerId);
        
        

        ArrayList<DPolygon> all = new ArrayList<DPolygon>();
        
        rot += 0.01;
        
        head.rotation = rot;
        head.setRotAdd();
        head.updatePoly();
        
        neck.rotation = rot;
        neck.updatePoly();
        
        corpus.rotation = rot;
        corpus.updatePoly();
        
        legs.rotation = rot;
        legs.updatePoly();
        
        hands.rotation = rot;
        hands.updatePoly();

        
        AddArrayToArrayList(all, head.GetPolygons());
        AddArrayToArrayList(all, neck.GetPolygons());
        AddArrayToArrayList(all, corpus.GetPolygons());
        AddArrayToArrayList(all, hands.GetPolygons());
        AddArrayToArrayList(all, legs.GetPolygons());*/
    
        
        
        
        //rot = Math.PI*0.75;
        //rot += 0.01;
        
        /*if (MoveDirection != null)
        {
            //0
            if (Math.abs(MoveDirection.getX()) < 0.1 && MoveDirection.getY() > 0.1) rot = 0;
            //1/4
            if (MoveDirection.getX() > 0.1 && MoveDirection.getY() > 0.1) rot = -Math.PI / 4.0;
            //1/2
            if (MoveDirection.getX() > 0.1 && Math.abs(MoveDirection.getY()) < 0.1) rot = -Math.PI / 2.0;
            //3/4
            if (MoveDirection.getX() > 0.1 && MoveDirection.getY() < 0.1) rot = -3.0 * Math.PI / 4.0;
            //1
            if (Math.abs(MoveDirection.getX()) < 0.1 && MoveDirection.getY() < 0.1) rot = -Math.PI;
            //5/4
            if (MoveDirection.getX() < 0.1 && MoveDirection.getY() < 0.1) rot = -5.0 * Math.PI / 4.0;
            //3/2
            if (MoveDirection.getX() < 0.1 && Math.abs(MoveDirection.getY()) < 0.1) rot = -3.0 * Math.PI / 2.0;
            //7/4
            if (MoveDirection.getX() < 0.1 && MoveDirection.getY() > 0.1) rot = -7.0 * Math.PI / 4.0;
            
            rot += 2 *  Math.PI / 4;
        }*/
        if (PositionIFace != null)
        {
            rot = Math.PI / 4.0;
            
            Vector3D v1 = new Vector3D(PositionIFace.getX(), PositionIFace.getY(), 0);
            Vector3D v2 = new Vector3D(ViewFrom[0], ViewFrom[1], 0);
            Vector3D v3 = (v1.subtract(v2)).normalize();
            
            Vector3D cross = Vector3D.crossProduct(v3, new Vector3D(1, 0, 0));
            double dot = Vector3D.dotProduct(v3, new Vector3D(1, 0, 0));
            
            double angle = Math.atan2(cross.getZ(), dot);
            rot += -angle;
        }
        
        double xx = - halfHeadSize + x;
        double yy = - halfHeadSize + y;
        double[]r = rotatePoint(xx, yy, xx, yy, rot - Math.PI*0.75);
        
        Cube head = new Cube(r[0], r[1], ViewFrom[2] - halfHeadSize + epsBody,
            halfHeadSize * 2.0, halfHeadSize * 2.0, halfHeadSize * 2.0, Color.YELLOW, PlayerId);

        head.Polys[2].DrawablePolygon.texture = GenerateTerrain.img;
        
        
        Cube neck = new Cube(- halfNeckSize + x, - halfNeckSize + y, ViewFrom[2] - headSize + halfNeckSize,
                halfNeckSize * 2.0, halfNeckSize * 2.0, halfNeckSize, Color.PINK, PlayerId);
        Cube corpus = new Cube(- halfCorupsXSize + x, - halfCorupsYSize + y, ViewFrom[2] - corpusZSize - headSize + halfNeckSize - epsBody,
                halfCorupsXSize * 2.0, halfCorupsYSize * 2.0, 2 * halfCorpusZSize, Color.BLUE, PlayerId);
        
        Cube legs = new Cube(- halfLegSizeX + x, - halfLegSizeY + y, ViewFrom[2] - legZSize - corpusZSize - headSize + halfNeckSize - epsBody - epsBody,
                2 * halfLegSizeX, 2 * halfLegSizeY, legZSize, Color.MAGENTA, PlayerId);
       

        xx = - halfHandsXSize + x;
        yy = - halfHandsYSize + y;//- halfHandsYSize + (halfHandsYSize / 2.0) + y;
        r = rotatePoint(xx, yy, x - 0.5, y - halfHandsYSize, rot - Math.PI*0.75);
        
        Cube handLeft = new Cube(r[0], r[1], ViewFrom[2] - handsZSize - headSize + halfNeckSize - epsBody,
                1, halfHandsYSize * 2.0, handsZSize, Color.CYAN, PlayerId);
                        
        xx = halfHandsXSize + x -1;
        yy = - halfHandsYSize + y;//- halfHandsYSize + (halfHandsYSize / 2.0) + y;
        r = rotatePoint(xx, yy, x - 0.5, y - halfHandsYSize, rot - Math.PI*0.75);
        
        Cube handRight = new Cube(r[0], r[1], ViewFrom[2] - handsZSize - headSize + halfNeckSize - epsBody,
                1, halfHandsYSize * 2.0, handsZSize, Color.CYAN, PlayerId);
        
        
        xx = - 2 + x;
        yy = - halfLegSizeY + y;//- halfHandsYSize + (halfHandsYSize / 2.0) + y;
        r = rotatePoint(xx, yy, x - 0.75, y - halfLegSizeY, rot - Math.PI*0.75);
        
        Cube legLeft = new Cube(r[0], r[1], ViewFrom[2] - legZSize - corpusZSize - headSize + halfNeckSize - epsBody - epsBody,
                1.5, halfLegSizeY * 2.0, legZSize, Color.CYAN, PlayerId);
        
        
        xx = 2 + x - 1.5;
        yy = - halfLegSizeY + y;//- halfHandsYSize + (halfHandsYSize / 2.0) + y;
        r = rotatePoint(xx, yy, x - 0.75, y - halfLegSizeY, rot - Math.PI*0.75);
        
        Cube legRight = new Cube(r[0], r[1], ViewFrom[2] - legZSize - corpusZSize - headSize + halfNeckSize - epsBody - epsBody,
                1.5, halfLegSizeY * 2.0, legZSize, Color.CYAN, PlayerId);
        //Cube legs = new Cube(- halfLegSizeX + x, - halfLegSizeY + y, ViewFrom[2] - legZSize - corpusZSize - headSize + halfNeckSize - epsBody - epsBody,
          //      2 * halfLegSizeX, 2 * halfLegSizeY, legZSize, Color.MAGENTA, PlayerId);

         ArrayList<DPolygon> all = new ArrayList<DPolygon>();
        
        //rot += 0.00;
        
        head.rotation = rot;
        head.setRotAdd();
        head.updatePoly();
        
        neck.rotation = rot;
        neck.updatePoly();
        
        corpus.rotation = rot;
        corpus.updatePoly();
        
        legs.rotation = rot;
        legs.updatePoly();
        
        handLeft.rotation = rot;
        handLeft.updatePoly();
        
        handRight.rotation = rot;
        handRight.updatePoly();
        
        legLeft.rotation = rot;
        legLeft.updatePoly();
        
        legRight.rotation = rot;
        legRight.updatePoly();

        AddArrayToArrayList(all, head.GetPolygons());
        AddArrayToArrayList(all, neck.GetPolygons());
        AddArrayToArrayList(all, corpus.GetPolygons());
        AddArrayToArrayList(all, handLeft.GetPolygons());
        AddArrayToArrayList(all, handRight.GetPolygons());
        AddArrayToArrayList(all, legLeft.GetPolygons());
        AddArrayToArrayList(all, legRight.GetPolygons());
        
        /*
        
        
        
        
        
        //rot = Math.PI*0.75;
        rot += 0.01;
        double xx = - halfHeadSize + x;
        double yy = - halfHeadSize + y;
        double[]r = rotatePoint(xx, yy, xx, yy, rot - Math.PI*0.75);
        
        Cube head = new Cube(r[0], r[1], ViewFrom[2] - halfHeadSize + epsBody,
            halfHeadSize * 2.0, halfHeadSize * 2.0, halfHeadSize * 2.0, Color.YELLOW, PlayerId, x, y, rot - Math.PI*0.75);

        head.Polys[2].DrawablePolygon.texture = GenerateTerrain.img;
        
        
        Cube neck = new Cube(- halfNeckSize + x, - halfNeckSize + y, ViewFrom[2] - headSize + halfNeckSize,
                halfNeckSize * 2.0, halfNeckSize * 2.0, halfNeckSize, Color.PINK, PlayerId, x, y, rot - Math.PI*0.75);
        Cube corpus = new Cube(- halfCorupsXSize + x, - halfCorupsYSize + y, ViewFrom[2] - corpusZSize - headSize + halfNeckSize - epsBody,
                halfCorupsXSize * 2.0, halfCorupsYSize * 2.0, 2 * halfCorpusZSize, Color.BLUE, PlayerId, x, y, rot - Math.PI*0.75);
        
        Cube legs = new Cube(- halfLegSizeX + x, - halfLegSizeY + y, ViewFrom[2] - legZSize - corpusZSize - headSize + halfNeckSize - epsBody - epsBody,
                2 * halfLegSizeX, 2 * halfLegSizeY, legZSize, Color.MAGENTA, PlayerId, x, y, rot - Math.PI*0.75);
       
        //xx = - halfHandsXSize + 0.5 + x;
        //yy = - halfHandsYSize + (halfHandsYSize / 2.0) + y;
        
        Cube hands = new Cube(- halfHandsXSize + x, - halfHandsYSize + y, ViewFrom[2] - handsZSize - headSize + halfNeckSize - epsBody,
                1, halfHandsYSize * 2.0, handsZSize, Color.CYAN, PlayerId, x, y, rot - Math.PI*0.75);

         ArrayList<DPolygon> all = new ArrayList<DPolygon>();
        
        AddArrayToArrayList(all, head.GetPolygons());
        AddArrayToArrayList(all, neck.GetPolygons());
        AddArrayToArrayList(all, corpus.GetPolygons());
        AddArrayToArrayList(all, hands.GetPolygons());
        AddArrayToArrayList(all, legs.GetPolygons());*/
        return all;
    }
    private static double[]rotatePoint(double xx, double yy, double originX, double originY, double rotation)
    {
        double xx2 = (Math.cos(rotation) * (xx - originX)) - (Math.sin(rotation) * (yy - originY)) + originX;
        double yy2 = (Math.sin(rotation) * (xx - originX)) + (Math.cos(rotation) * (yy - originY)) + originY;
        return new double[]{xx2, yy2};
    }
/*

            double halfHeadSize = 1;
        double headSize = 2;
        double halfNeckSize = 0.5;
        double neckSize = 1;
        double halfCorupsXSize = 2;
        double halfCorupsYSize = 1;

        double corupsYSize = 1;
        double halfCorpusZSize = 3;
        double corpusZSize = 6;
        double halfLegZSize = 3.75;
        //double legZSize = 7;
        double halfLegSizeY = 0.7;
        //double legSizeY = 1.4;
        double legSizeX = 1.3;
        
        double legZSize = 7;
        //double halfLegSizeY = 1;
        double legSizeY = 1.4;
        double halfLegSizeX = 1.5;
        
        double halfHandsYSize = 0.8;
        double halfHandsXSize = 3;
        double handsZSize = 6;
        
        double halfHandSizeY = 0.6;
        double handZSize = 7;
        double handSizeX = 0.75;
        double epsBody = 0.01;
    
public ArrayList<DPolygon> GetPolygons(double x, double y) {
        
        //dodać epsilony!
        Cube head = new Cube(ViewFrom[0] - halfHeadSize + x, ViewFrom[1] - halfHeadSize + y, ViewFrom[2] - halfHeadSize + epsBody,
                halfHeadSize * 2.0, halfHeadSize * 2.0, halfHeadSize * 2.0, Color.YELLOW, PlayerId);
        Cube neck = new Cube(ViewFrom[0] - halfNeckSize + x, ViewFrom[1] - halfNeckSize + y, ViewFrom[2] - headSize + halfNeckSize,
                halfNeckSize * 2.0, halfNeckSize * 2.0, halfNeckSize, Color.PINK, PlayerId);
        
        Cube corpus = new Cube(ViewFrom[0] - halfCorupsXSize + x, ViewFrom[1] - halfCorupsYSize + y, ViewFrom[2] - corpusZSize - headSize + halfNeckSize - epsBody,
                halfCorupsXSize * 2.0, halfCorupsYSize * 2.0, 2 * halfCorpusZSize, Color.BLUE, PlayerId);
        
        Cube leftLeg = new Cube(ViewFrom[0] - halfCorupsXSize + x, ViewFrom[1] - halfLegSizeY + y, ViewFrom[2] - legZSize - corpusZSize - headSize + halfNeckSize - epsBody - epsBody,
                legSizeX, legSizeY, legZSize, Color.MAGENTA, PlayerId);
        
        Cube rightLeg = new Cube(ViewFrom[0] + halfCorupsXSize - legSizeX + x, ViewFrom[1] - halfLegSizeY + y, ViewFrom[2] - legZSize - corpusZSize - headSize + halfNeckSize - epsBody - epsBody,
                legSizeX, legSizeY, legZSize, Color.MAGENTA, PlayerId);
        
        Cube leftHand = new Cube(ViewFrom[0] - halfCorupsXSize - handSizeX + x - epsBody, ViewFrom[1] - halfHandSizeY + y, ViewFrom[2] - handZSize - headSize,
                handSizeX, 2 * halfHandSizeY, handZSize, Color.CYAN, PlayerId);
        Cube rightHand = new Cube(ViewFrom[0] + halfCorupsXSize + x + epsBody, ViewFrom[1] - halfHandSizeY + y, ViewFrom[2] - handZSize - headSize,
                handSizeX, 2 * halfHandSizeY, handZSize, Color.CYAN, PlayerId);
        
        
        //Cube leftLeg = new Cube(ViewFrom[0] - headSize + x, ViewFrom[1] - headSize + y, ViewFrom[2] - headSize, headSize * 2.0, headSize * 2.0, headSize * 2.0, Color.YELLOW, PlayerId);
        ArrayList<DPolygon> all = new ArrayList<DPolygon>();
        

        //corpus.setRotAdd();
        //corpus.updatePoly();
        
        //leftLeg.rotation = Math.PI / 4;
        //leftLeg.UpdateDirection(ViewFrom[0], ViewFrom[1]);
        //leftLeg.updatePoly( ViewFrom[0] + x, ViewFrom[1] + y);
        //leftLeg.updatePoly();
        

        
        //AddArrayToArrayList(all, head.GetPolygons());
        //AddArrayToArrayList(all, neck.GetPolygons());
        rot += 0.01;
        //corpus.rotation = rot;
        //leftLeg.rotation = rot;
        //corpus.updatePoly();
        //leftLeg.updatePoly();
        
        //corpus.updatePoly2(ViewFrom[0] - halfCorupsXSize + x, ViewFrom[1] - halfCorupsYSize + y, ViewFrom[0] - halfCorupsXSize + x, ViewFrom[1] - halfCorupsYSize + y);
        //leftLeg.updatePoly2(ViewFrom[0] - halfCorupsXSize - legSizeX + x, ViewFrom[1] - halfCorupsYSize + y, ViewFrom[0] - halfCorupsXSize + x, ViewFrom[1] - halfCorupsYSize + y);
        
        
        //Cube aaa = new Cube(x-2, y-2, ViewFrom[2]-8, 4, 4, 10, Color.CYAN, null);
        head = new Cube(x - halfHeadSize, y - halfHeadSize, ViewFrom[2] - halfHeadSize + epsBody,
                halfHeadSize * 2.0, halfHeadSize * 2.0, halfHeadSize * 2.0, Color.YELLOW, PlayerId);
        neck = new Cube(x - halfNeckSize, y - halfNeckSize, ViewFrom[2] - headSize + halfNeckSize,
                halfNeckSize * 2.0, halfNeckSize * 2.0, halfNeckSize, Color.PINK, PlayerId);
        corpus = new Cube(x - halfCorupsXSize, y - halfCorupsYSize, ViewFrom[2] - corpusZSize - headSize + halfNeckSize - epsBody,
                halfCorupsXSize * 2.0, halfCorupsYSize * 2.0, 2 * halfCorpusZSize, Color.BLUE, PlayerId);
        leftLeg = new Cube(x - halfCorupsXSize, y - halfLegSizeY, ViewFrom[2] - legZSize - corpusZSize - headSize + halfNeckSize - epsBody - epsBody,
                legSizeX, legSizeY, legZSize, Color.MAGENTA, PlayerId);
        rightLeg = new Cube(x + halfCorupsXSize - legSizeX, y - halfLegSizeY, ViewFrom[2] - legZSize - corpusZSize - headSize + halfNeckSize - epsBody - epsBody,
                legSizeX, legSizeY, legZSize, Color.MAGENTA, PlayerId);
        
        leftHand = new Cube(x - halfCorupsXSize - handSizeX - epsBody, y - halfHandSizeY, ViewFrom[2] - handZSize - headSize,
                handSizeX, 2 * halfHandSizeY, handZSize, Color.CYAN, PlayerId);
        rightHand = new Cube(x + halfCorupsXSize + epsBody, y - halfHandSizeY, ViewFrom[2] - handZSize - headSize,
                handSizeX, 2 * halfHandSizeY, handZSize, Color.CYAN, PlayerId);
        
        //rot = 0.75 * Math.PI;
        corpus.rotation = rot;
        leftLeg.rotation = rot;
        corpus.updatePoly2(x, y, x, y);
        leftLeg.updatePoly2(x, y, x, y);
        //leftLeg.updatePoly2(x - halfCorupsXSize, y - halfLegSizeY,x,y);
        //leftLeg.updatePoly(x, y);
        
        //aaa.updatePoly();
        //AddArrayToArrayList(all, aaa.GetPolygons());
        //AddArrayToArrayList(all, head.GetPolygons());
        //AddArrayToArrayList(all, neck.GetPolygons());
        AddArrayToArrayList(all, corpus.GetPolygons());
        AddArrayToArrayList(all, leftLeg.GetPolygons());
        //AddArrayToArrayList(all, rightLeg.GetPolygons());
        //AddArrayToArrayList(all, leftHand.GetPolygons());
        //AddArrayToArrayList(all, rightHand.GetPolygons());
        
        
        
        
        
        
        return all;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
*/
    @Override
    public double getX() {
        return ViewFrom[0];
    }

    @Override
    public double getY() {
        return ViewFrom[1];
    }

    @Override
    public double getDistance(double fromX, double fromY, double fromz, double x, double y, double z) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
/*
    @Override
    public BoundingSphere getBoundingSphere() {
        return boundingSphere;
    }*/

    @Override
    public boolean Collide(IRenderableGameObject iRenderableGameObject) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object[] getOwnerId() {
        return PlayerId;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return boundingBox;
    }
}
