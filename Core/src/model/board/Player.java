/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.board;

import model.board.items.Item;
import model.board.statics.Hole;
import model.board.statics.StaticEntity;
import util.Position;

/**
 * Héros du jeu
 */
public class Player {
    private final Game game;
    private final Inventory inventory = new Inventory();
    private final Controller controller = new Controller();
    private Position position;
    private Orientation orientation;

    public Player(Game game, int x, int y) {
        this(game, new Position(x, y));
    }

    public Player(Game game, Position position) {
        this.game = game;
        this.position = position;
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

    private boolean collide(Position pos) {
        StaticEntity entity = game.currentRoom().getStatic(pos);

        return entity == null || entity.collide(this);
    }

    public enum Orientation {
        UP(270),
        LEFT(180),
        DOWN(90),
        RIGHT(0);

        // Degrés
        private final double rotation;

        Orientation(double rotation) {
            this.rotation = rotation;
        }

        public double getDegrees() {
            return rotation;
        }

        public double getRadians() {
            return Math.toRadians(rotation);
        }

        public Position getNextPos(Position pos) {
            return getNextPos(pos, 1);
        }

        public Position getNextPos(Position pos, int offset) {
            return switch (this) {
                case UP -> new Position(pos.x, pos.y - offset);
                case LEFT -> new Position(pos.x - offset, pos.y);
                case DOWN -> new Position(pos.x, pos.y + offset);
                case RIGHT -> new Position(pos.x + offset, pos.y);
            };
        }
    }

    public class Controller {
        public void move(Orientation direction) {
            Position pos = direction.getNextPos(position);
            if (!collide(pos)) {
                // On obtient la case actuelle et on éxécute l'événement pour la quitter
                game.currentRoom().getStatic(position).leave(Player.this);
                setPosition(pos);
            }
            setOrientation(direction);
        }

        public void use(Item item) {
            if (game.currentRoom().getStatic(orientation.getNextPos(position)).use(Player.this, item))
                inventory.remove(item);
        }

        public void use(Class<? extends Item> type) {
            if (!inventory.has(type))
                return;
            Item item = inventory.firstOf(type);
            if (game.currentRoom().getStatic(orientation.getNextPos(position)).use(Player.this, item))
                inventory.remove(item);
        }

        public void jump() {
            Position target = orientation.getNextPos(position, 2);
            StaticEntity from = game.currentRoom().getStatic(orientation.getNextPos(position)),
                    to = game.currentRoom().getStatic(target);
            if (from instanceof Hole && !to.collide(Player.this)) {
                from.leave(Player.this);
                setPosition(target);
            }
        }
    }
}
