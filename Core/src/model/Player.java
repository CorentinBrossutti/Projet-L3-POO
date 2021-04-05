/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import meta.Main;
import meta.Plugin;
import model.board.items.Item;
import model.board.statics.StaticEntity;
import util.Position;

import java.util.HashMap;
import java.util.Map;

/**
 * Héros du jeu
 */
public class Player {
    public final Game game;
    /**
     * L'inventaire du joueur
     */
    public final Inventory inventory = new Inventory();
    /**
     * La position du joueur
     */
    public Position position;
    /**
     * L'orientation du joueur (de sa vue)
     */
    public Orientation orientation = Orientation.RIGHT;
    /**
     * Contrôlent les mouvements et interactions du joueur.
     * En clé, le nom du plugin lié, en valeur le contrôleur.
     */
    protected final Map<String, PlayerController> controllers = new HashMap<>();

    public Player(Game game, int x, int y) {
        this(game, new Position(x, y));
    }

    public Player(Game game, Position position) {
        this.game = game;
        this.position = position;

        // Ajout du contrôleur base
        controllers.put("core", new CoreController(this));
    }

    /**
     *
     * @param handle Le nom du contrôleur, fréquemment le nom du plugin qui le possède
     * @return Le contrôleur lié
     */
    public <T extends PlayerController> T getController(String handle) {
        return (T) controllers.get(handle);
    }

    /**
     * Evite un cast en passant par un générique
     * @param plugin Le nom du contrôleur, fréquemment le nom du plugin qui le possède
     * @param dummy Classe du contrôleur que l'on cherche à obtenir, évite un cast via un générique
     * @param <T> Générique
     * @return Le contrôleur lié
     */
    public <T extends PlayerController> T getController(String plugin, Class<T> dummy){
        return getController(plugin);
    }

    /**
     *
     * @return Le contrôleur basique du joueur "core"
     */
    public CoreController getCoreController(){
        return getController("core");
    }

    /**
     * Si un contrôleur existe avec la même clé, c'est ignoré
     * @param handle La clé, nom du contrôleur
     * @param controller Le contrôleur
     */
    public void addController(String handle, PlayerController controller){
        controllers.putIfAbsent(handle, controller);
    }

    /**
     *
     * @return La salle actuelle du joueur
     */
    public Room room(){
        return game.currentRoom();
    }

    /**
     * Définit l'orientation du joueur (haut, bas, gauche, droite).
     * Pour chaque élément de l'énumération, il faut spécifier les degrés de la rotation.
     */
    public enum Orientation {
        UP(270),
        LEFT(180),
        DOWN(90),
        RIGHT(0);

        /**
         * Rotation en degrés
         */
        public final double degrees;
        /**
         * Rotation en radians
         */
        public final double radians;

        Orientation(double degrees) {
            this.degrees = degrees;
            this.radians = Math.toRadians(degrees);
        }

        /**
         *
         * @return L'orientation opposée à celle actuelle
         */
        public Orientation opposite(){
            return switch (this) {
                case UP -> DOWN;
                case DOWN -> UP;
                case LEFT -> RIGHT;
                case RIGHT -> LEFT;
            };
        }

        /**
         *
         * @param pos Position de départ
         * @return La position suivante en fonction de l'orientation
         */
        public Position getNextPos(Position pos) {
            return getNextPos(pos, 1);
        }

        /**
         *
         * @param pos Position de départ
         * @param offset Distance en cases
         * @return La position suivante en fonction de l'orientation du nombre de cases
         */
        public Position getNextPos(Position pos, int offset) {
            return switch (this) {
                case UP -> new Position(pos.x, pos.y - offset);
                case LEFT -> new Position(pos.x - offset, pos.y);
                case DOWN -> new Position(pos.x, pos.y + offset);
                case RIGHT -> new Position(pos.x + offset, pos.y);
            };
        }
    }

    /**
     * Classe "core" pour les mouvements de base du joueur
     */
    public static class CoreController extends PlayerController{
        public CoreController(Player player) {
            super(player);
        }

        /**
         * Tente de faire avancer le joueur d'une case
         *
         * @param direction Direction du mouvement, définira aussi l'orientation du joueur
         */
        public void move(Player.Orientation direction) {
            Position pos = direction.getNextPos(player.position);
            StaticEntity se = player.room().getStatic(pos);

            if (!se.collide(player)) {
                for (Plugin p : Main.plugins){
                    if(!p.events.playerMoves(player, player.position, pos, direction))
                        return;
                }
                // On obtient la case actuelle et on éxécute l'événement pour la quitter
                player.room().getStatic(player.position).leave(player);
                player.position = pos;
                se.enter(player);
            }
            player.orientation = direction;
        }

        /**
         * Tente de faire utiliser un objet au joueur sur la case qu'il regarde, et le retire de son inventaire.
         *
         * @param item L'objet à utiliser
         */
        public void use(Item item) {
            StaticEntity target = player.room().getStatic(player.orientation.getNextPos(player.position));
            for(Plugin p : Main.plugins){
                if(!p.events.playerUses(player, target, item))
                    return;
            }
            if (target.use(player, item))
                player.inventory.remove(item);
        }

        /**
         * Tente de faire utiliser un certain type d'objet au joueur sur la case qu'il regarde.
         * Nécessite que le joueur ait au moins un exemplaire du type d'objet dans son inventaire, et lui retire après utilisation.
         *
         * @param type Type d'objet à utiliser
         */
        public void use(Class<? extends Item> type) {
            if (!player.inventory.has(type))
                return;

            use(player.inventory.firstOf(type));
        }
    }

}
