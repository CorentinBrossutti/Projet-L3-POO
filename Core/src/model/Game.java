/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import meta.Main;
import meta.Plugin;
import model.board.items.Item;
import model.board.items.ItemContainer;
import model.board.statics.StaticEntity;
import util.Position;
import util.Util;

import java.util.*;

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


    public Gen gen;
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
    }

    public Player getPlayer() {
        return player;
    }

    public Room currentRoom() {
        return rooms[currentRoomIndex];
    }

    public void init(){
        gen = new Gen();
    }

    public void start(){
        ClassLoader ct = getClass().getClassLoader();

        // On génère les salles
        for (int i = 0; i < ROOM_COUNT; i++) {
            // Si l'on traite la première salle, alors la position sera (-1, -1) et donc choisi aléatoirement à la génération de la salle.
            // Sinon, la position de départ de la salle actuelle correspondra à la position à côté de la porte dans la salle précédente.
            Position spos = gen.getSlotNextToDoor(i == 0 ? new Position(-1, -1) : rooms[i - 1].exit);
            rooms[i] = new Room(spos, new Position(-1, -1), i == ROOM_COUNT - 1);
        }
        player = new Player(this, rooms[0].start);
        Main.plugins.forEach(
                plugin -> player.addController(plugin.name, plugin.model.customController(player))
        );
        Main.plugins.forEach(
                plugin -> plugin.events.playerChangesRoom(player, null, rooms[0])
        );

        new Thread(this).start();
    }

    public void run() {
        while (true) {
            Main.plugins.forEach(Plugin::tick);

            setChanged();
            notifyObservers();
            // Si la salle actuelle vient d'être terminée
            if (currentRoom().isDone()) {
                // On envoie l'event de changement de salle pour les extensions, tout en mettant à jour l'indice
                Main.plugins.forEach(
                        plugin -> plugin.events.playerChangesRoom(player, rooms[currentRoomIndex], rooms[currentRoomIndex + 1])
                );
                currentRoomIndex++;
                // On change la position du joueur pour celle de départ de la salle
                player.setPosition(currentRoom().start);
                player.setOrientation(player.getOrientation().opposite());
            }

            try {
                Thread.sleep(PAUSE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Classe utilitaire de génération aléatoire pour les entités statiques et les objets.
     * Cette classe ne s'instancie pas et s'utilise de façon statique.
     */
    public final class Gen {
        private final Random rand = new Random();

        public final List<Class<? extends StaticEntity>> staticPicker = new ArrayList<>();
        public final List<Class<? extends Item>> itemPicker = new ArrayList<>();

        public final short itemDistinctCount, staticDistinctCount;

        public final Set<Class<? extends StaticEntity>> baseStatics = new HashSet<>();
        public final Set<Class<? extends Item>> baseItems = new HashSet<>();

        private Gen(){
            Main.plugins.forEach(
                    plugin -> {
                        staticPicker.addAll(plugin.model.staticSupplier.supply());
                        baseStatics.addAll(plugin.model.staticSupplier.baseList());
                    }
            );
            Main.plugins.forEach(
                    plugin -> {
                        itemPicker.addAll(plugin.model.itemSupplier.supply());
                        baseItems.addAll(plugin.model.itemSupplier.baseList());
                    }
            );

            itemDistinctCount = Long.valueOf(staticPicker.stream().distinct().count()).shortValue();
            staticDistinctCount = Long.valueOf(itemPicker.stream().distinct().count()).shortValue();
        }

        /**
         *
         * @param room Salle parente de l'entité statique
         * @return Une entité statique aléatoire, avec les poids pris en compte
         */
        public StaticEntity pickStatic(Room room) {
            return Util.Reflections.instantiate(staticPicker.get(rand.nextInt(staticPicker.size())), room);
        }

        /**
         *
         * @return Un objet aléatoire, avec les poids pris en compte
         */
        public Item pickItem() {
            Class<? extends Item> itemType = itemPicker.get(rand.nextInt(itemPicker.size()));
            return ItemContainer.class.isAssignableFrom(itemType) ?
                    Util.Reflections.instantiate(itemType, gen) :
                    Util.Reflections.instantiate(itemType);
        }

        /**
         * Utile pour savoir où faire apparaître le joueur dans la salle suivante.
         * @param doorPos Position de la porte
         * @return Retourne la position d'entrée / sortie de la porte
         */
        public Position getSlotNextToDoor(Position doorPos) {
            switch (doorPos.x) {
                case 0:
                    return new Position(1, doorPos.y);
                case Room.SIZE_X - 1:
                    return new Position(Room.SIZE_X - 2, doorPos.y);
                default:
                    switch (doorPos.y) {
                        case 0:
                            return new Position(doorPos.x, 1);
                        case Room.SIZE_Y - 1:
                            return new Position(doorPos.x, Room.SIZE_Y - 2);
                    }
            }
            return doorPos;
        }
    }
}
