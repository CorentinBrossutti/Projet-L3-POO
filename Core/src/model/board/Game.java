/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.board;

import model.board.items.WaterCap;
import util.Position;

import java.util.Observable;

public class Game extends Observable implements Runnable {
    /**
     * Valeur de tick, délai de rafraîchissement
     */
    public static final int PAUSE = 50;
    /**
     * Nombre de salles à franchir pour gagner
     */
    public static final int ROOM_COUNT = 3;
    /**
     * Le nombre de capsules lorsque l'on rentre dans une salle
     */
    public static int WCAP_COUNT = 1;

    private Player player;
    /**
     * Grille des salles
     */
    private Room[] rooms = new Room[ROOM_COUNT];
    /**
     * Indice de salle actuelle dans le tableau
     */
    private int currentRoomIndex;

    public Game() {
        // On génère les salles
        for (int i = 0; i < ROOM_COUNT; i++) {
            // Si l'on traite la première salle, alors la position sera (-1, -1) et donc choisi aléatoirement à la génération de la salle.
            // Sinon, la position de départ de la salle actuelle correspondra à la position à côté de la porte dans la salle précédente.
            Position spos = Gen.getSlotNextToDoor(i == 0 ? new Position(-1, -1) : rooms[i - 1].getExit());
            rooms[i] = new Room(i == ROOM_COUNT - 1, spos);
        }
        player = new Player(this, rooms[0].getStart());
        // On ajoute les capsules d'eau au joueur
        player.getInventory().add(WaterCap.class, WCAP_COUNT);
    }

    public Player getPlayer() {
        return player;
    }

    public Room currentRoom() {
        return rooms[currentRoomIndex];
    }

    public void start() {
        new Thread(this).start();
    }

    public void run() {
        while (true) {
            setChanged();
            notifyObservers();
            // Si la salle actuelle vient d'être terminée
            if (currentRoom().isDone()) {
                // On passe à la salle suivante
                currentRoomIndex++;
                // On change la position du joueur pour celle de départ de la salle
                player.setPosition(currentRoom().getStart());
                // On enlève ses capsules...
                player.getInventory().remomoveAllOf(WaterCap.class);
                // ...et on lui ajoute le nombre par défaut
                player.getInventory().add(WaterCap.class, WCAP_COUNT);
            }

            try {
                Thread.sleep(PAUSE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
