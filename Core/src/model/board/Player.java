/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.board;

import model.board.items.Item;
import model.board.statics.StaticEntity;
import model.board.statics.Wall;
import util.Position;

/**
 * Héros du jeu
 */
public class Player {
    private final Game game;
    private final Inventory inventory = new Inventory();
    /**
     * Contrôle les mouvements et interactions du joueur
     */
    private final Controller controller = new Controller();
    private Position position;
    private Orientation orientation;

    public Player(Game game, int x, int y) {
        this(game, new Position(x, y));
    }

    public Player(Game game, Position position) {
        this.game = game;
        this.position = position;
        // Orientation à droite par défaut
        this.orientation = Orientation.RIGHT;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position pos) {
        this.position = pos;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Controller getController() {
        return controller;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public void setOrientation(Orientation orientation) {
        this.orientation = orientation;
    }

    public void setPosition(int x, int y) {
        setPosition(new Position(x, y));
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
     * Classe contrôlant le mouvement du joueur
     */
    public class Controller {
        /**
         * Tente de faire avancer le joueur d'une case
         * @param direction Direction du mouvement, définira aussi l'orientation du joueur
         */
        public void move(Orientation direction) {
            Position pos = direction.getNextPos(position);
            if (!game.currentRoom().getStatic(pos).collide(Player.this)) {
                // On obtient la case actuelle et on éxécute l'événement pour la quitter
                game.currentRoom().getStatic(position).leave(Player.this);
                setPosition(pos);
            }
            setOrientation(direction);
        }

        /**
         * Tente de faire utiliser un objet au joueur sur la case qu'il regarde, et le retire de son inventaire.
         * @param item L'objet à utiliser
         */
        public void use(Item item) {
            if (game.currentRoom().getStatic(orientation.getNextPos(position)).use(Player.this, item))
                inventory.remove(item);
        }

        /**
         * Tente de faire utiliser un certain type d'objet au joueur sur la case qu'il regarde.
         * Nécessite que le joueur ait au moins un exemplaire du type d'objet dans son inventaire, et lui retire après utilisation.
         * @param type Type d'objet à utiliser
         */
        public void use(Class<? extends Item> type) {
            if (!inventory.has(type))
                return;

            Item item = inventory.firstOf(type);
            if (game.currentRoom().getStatic(orientation.getNextPos(position)).use(Player.this, item))
                inventory.remove(item);
        }

        /**
         * Tente de faire sauter le joueur deux cases plus loin
         */
        public void jump() {
            Position target = orientation.getNextPos(position, 2);
            StaticEntity from = game.currentRoom().getStatic(orientation.getNextPos(position)),
                    to = game.currentRoom().getStatic(target);
            if (!(from instanceof Wall) && !to.collide(Player.this)) {
                from.leave(Player.this);
                setPosition(target);
            }
        }
    }
}
