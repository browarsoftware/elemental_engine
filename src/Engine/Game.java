/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Engine;

import java.util.ArrayList;

/**
 *
 * @author Tomus
 */
public class Game {
    public ArrayList<Player> Players = new ArrayList<Player>();
    private ArrayList<Long>playersId = new ArrayList<Long>();
    public Player AddNewPlayer(double[] ViewForm, double[] ViewTo, long playerId)
    {
        //if (playerId >= 0)
          //  playerId = getNewPlayerId();
        Player newPlayer = new Player(ViewForm, ViewTo);
        playersId.add(playerId);
        return newPlayer;
    }
    
    public void RemovePlayer(Player player)
    {
        Players.remove(player);
        playersId.remove(player.PlayerId);
    }
    /*
    private long getNewPlayerId()
    {
        long newId = 0;
        for (int a = 0; a < playersId.size(); a++)
        {
            if (playersId.contains(newId))
            {
                newId++;
            }
            else
            {
                return newId;
            }
        }
        return newId;
    }*/
}
