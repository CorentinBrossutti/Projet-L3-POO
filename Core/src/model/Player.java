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
    public final Inventory inventory = new Inventory();
    /**
     * Contrôle les mouvements et interactions du joueur
     */
    protected final Map<String, PlayerController> controllers = new HashMap<>();
    protected Position position;
    protected Orientation orientation = Orientation.RIGHT;

    public Player(Game game, int x, int y) {
        this(game, new Position(x, y));
    }

    public Player(Game game, Position position) {
        this.game = game;
        this.position = position;

        controllers.put("core", new CoreController(this));
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position pos) {
        this.position = pos;
    }

    public PlayerController getController(String plugin) {
        return controllers.get(plugin);
    }

    public <T extends PlayerController> T getController(String plugin, Class<T> dummy){
        return (T) getController(plugin);
    }

    public CoreController getCoreController(){
        return (CoreController) getController("core");
    }

    public void addController(String controllerName, PlayerController controller){
        controllers.put(controllerName, controller);
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

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
        private final double rotation;

        Orientation(double rotation) {
            this.rotation = rotation;
        }

        /**
         *
         * @return Rotation en degrés liée à l'orientation
         */
        public double getDegrees() {
            return rotation;
        }

        /**
         *
         * @return Rotation en radians liée à l'orientation
         */
        public double getRadians() {
            return Math.toRadians(rotation);
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
            Position pos = direction.getNextPos(player.getPosition());
            StaticEntity se = player.room().getStatic(pos);

            if (!se.collide(player)) {
                for (Plugin p : Main.plugins){
                    if(!p.events.playerMoves(player, player.getPosition(), pos, direction))
                        return;
                }
                // On obtient la case actuelle et on éxécute l'événement pour la quitter
                player.room().getStatic(player.getPosition()).leave(player);
                player.setPosition(pos);
                se.enter(player);
            }
            player.setOrientation(direction);
        }

        /**
         * Tente de faire utiliser un objet au joueur sur la case qu'il regarde, et le retire de son inventaire.
         *
         * @param item L'objet à utiliser
         */
        public void use(Item item) {
            StaticEntity target = player.room().getStatic(player.getOrientation().getNextPos(player.getPosition()));
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
