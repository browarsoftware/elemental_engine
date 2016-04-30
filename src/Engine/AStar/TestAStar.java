package Engine.AStar;


import java.awt.Point;
import java.util.ArrayList;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.logging.Level;

public class TestAStar {
	
	private static int mapWith = 159;
	private static int mapHeight = 118;
	
	private static int[][] obstacleMap = null;
	
	private static int startX = 50;
	private static int startY = 12;
	private static int goalX = 110;
	private static int goalY = 75;
	
	
	public static void main(String[] args) {

            String content;
            try {
                content = new String(Files.readAllBytes(Paths.get("e:\\projects\\java 3d tutorial\\Game\\MAP.txt")));
                String[]rows = content.split("\r\n");
                obstacleMap = new int [rows.length][];
                for (int a = 0; a < obstacleMap.length;a++)
                {
                    String []fields = rows[a].split(",");
                    obstacleMap[a] = new int[fields.length];
                    for (int b = 0; b < fields.length; b++)
                    {
                        obstacleMap[a][b] = Integer.parseInt(fields[b]);
                    }
                }
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(TestAStar.class.getName()).log(Level.SEVERE, null, ex);
            }

		Logger log = new Logger();
		StopWatch s = new StopWatch();
		
		log.addToLog("Map initializing...");
		AreaMap map = new AreaMap(mapWith, mapHeight, obstacleMap);
		
		log.addToLog("Heuristic initializing...");
		//AStarHeuristic heuristic = new ClosestHeuristic();
		IAStarHeuristic heuristic = new DiagonalHeuristic();
		
		log.addToLog("AStar initializing...");
		AStar aStar = new AStar(map, heuristic);
		
		log.addToLog("Calculating shortest path...");
		s.start();
		ArrayList<Point> shortestPath = aStar.calcShortestPath(startX, startY, goalX, goalY);
		s.stop();
		
		log.addToLog("Time to calculate path in milliseconds: " + s.getElapsedTime());
		
		log.addToLog("Printing map of shortest path...");
		new PrintMap(map, shortestPath);
	}

}
