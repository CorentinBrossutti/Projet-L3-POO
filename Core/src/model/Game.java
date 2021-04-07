/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import meta.events.Events;
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
     * Valeur de tick, délai de rafraîchissement (ms)
     */
    public static final short PAUSE = 50;
    /**
     * Nombre de salles à franchir pour gagner
     */
    public static final byte ROOM_COUNT = 3;


    public Gen gen;
    public final Set<Character> characters = new HashSet<>();
    protected Player player;
    /**
     * Grille des salles
     */
    private Room[] rooms = new Room[ROOM_COUNT];
    /**
     * Indice de salle actuelle dans le tableau
     */
    private byte currentRoomIndex;

    public Game() {
        player = new Player(this, Position.nullPos);
    }

    public Player getPlayer() {
        return player;
    }

    /**
     *
     * @return La salle actuelle
     */
    public Room currentRoom() {
        return rooms[currentRoomIndex];
    }

    /**
     * Initialisation précoce du jeu
     */
    public void init(){
        gen = new Gen();
        characters.add(player);
    }

    /**
     * Démarrage du jeu
     */
    public void start(){
        // On génère les salles
        for (int i = 0; i < ROOM_COUNT; i++) {
            // Si l'on traite la première salle, alors la position sera (-1, -1) et donc choisi aléatoirement à la génération de la salle.
            // Sinon, la position de départ de la salle actuelle correspondra à la position à côté de la porte dans la salle précédente.
            Position spos = gen.getSlotNextToDoor(i == 0 ? new Position(-1, -1) : rooms[i - 1].exit);
            // Similairement, la dernière salle n'a pas de porte, pour l'instant...
            // TODO jeu gagné
            rooms[i] = new Room(spos, new Position(-1, -1), i == ROOM_COUNT - 1);
        }
        // On définit la position du joueur sur celle de départ de la première salle
        player.position = rooms[0].start;
        // On assigne les contrôleurs de plugin
        Main.plugins.forEach(
                plugin -> player.addController(plugin.name, plugin.model.customPlayerController(player))
        );
        Events.callVoid(Main.plugins, Events.PLAYER_CHANGES_ROOM, player, null, rooms[0]);

        new Thread(this).start();
    }

    public void run() {
        while (true) {
            // Tick des plugins
            Main.plugins.forEach(Plugin::tick);

            setChanged();
            notifyObservers();
            // Si la salle actuelle vient d'être terminée
            if (currentRoom().isDone()) {
                // On envoie l'event de changement de salle pour les extensions, tout en mettant à jour l'indice
                Events.callVoid(Main.plugins, Events.PLAYER_CHANGES_ROOM, player, rooms[currentRoomIndex], rooms[++currentRoomIndex]);
                // On change la position du joueur pour celle de départ de la salle
                player.position = currentRoom().start;
                // Et son orientation (pour qu'il ne regarde pas le mur)
                player.orientation = player.orientation.opposite();
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
     */
    public class Gen {
        private final Random rand = new Random();

        /**
         * Liste à poids des entités statiques.
         * Prendre un élément aléatoire reviendra à utiliser le système de poids.
         */
        public final List<Class<? extends StaticEntity>> staticPicker = new ArrayList<>();
        /**
         * Liste à poids des objets.
         * Prendre un élément aléatoire reviendra à utiliser le système de poids.
         */
        public final List<Class<? extends Item>> itemPicker = new ArrayList<>();

        /**
         * Le nombre d'objets distincts (poids non pris en compte)
         */
        public final short itemDistinctCount;
        /**
         * Le nombre d'entités statiques distinctes (poids non pris en compte)
         */
        public final short staticDistinctCount;

        /**
         * La liste des entités statiques distinctes générables, poids non pris en compte
         */
        public final Set<Class<? extends StaticEntity>> baseStatics = new HashSet<>();
        /**
         * La liste des objets distincts générables, poids non pris en compte
         */
        public final Set<Class<? extends Item>> baseItems = new HashSet<>();

        private Gen(){
            // Pour chaque plugin, on obtient et on fusionne les objets et entités statiques qu'il souhaite pouvoir générer
            Main.plugins.forEach(
                    plugin -> {
                        staticPicker.addAll(plugin.model.staticSupplier.supply());
                        baseStatics.addAll(plugin.model.staticSupplier.baseList());
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

        /**
         *
         * @return Une orientation aléatoire.
         */
        public Character.Orientation randomOrientation(){
            return Character.Orientation.values()[rand.nextInt(Character.Orientation.values().length)];
        }

        /**
         * Concrètement, il y a 1 chance sur odds que la fonction retourne vrai.
         * @param odds Valeur de chance
         * @return Vrai si le comportement conditionné par l'aléatoire doit avoir lieu.
         */
        public boolean should(int odds){
            return rand.nextInt(odds) == 0;
        }
    }
}
