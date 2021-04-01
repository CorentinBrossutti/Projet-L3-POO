/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

import modele.plateau.items.WaterCap;
import util.Position;

import java.util.Observable;

public class Game extends Observable implements Runnable {
    private static final int PAUSE = 100; // période de rafraichissement
    private static final int ROOM_COUNT = 3;

    private Player player;
    private Room[] rooms = new Room[ROOM_COUNT];
    private int currentRoomIndex;


    public Player getPlayer() {
        return player;
    }

    public Game() {
        for (int i = 0; i < ROOM_COUNT; i++){
            Position spos = Room.Gen.getSlotNextToDoor(i == 0 ? new Position(-1, -1) : rooms[i - 1].getExit());
            rooms[i] = new Room(i == ROOM_COUNT - 1, spos);
        }
        player = new Player(this, rooms[0].getStart());
    }

    public Room currentRoom(){
        return rooms[currentRoomIndex];
    }

    public void start() {
        new Thread(this).start();
    }

    public void run() {

        while(true) {
            setChanged();
            notifyObservers();
            if(currentRoom().isDone()){
                currentRoomIndex++;
                player.setPosition(currentRoom().getStart());
                player.getInventory().remomoveAllOf(WaterCap.class);
            }

            try {
                Thread.sleep(PAUSE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
