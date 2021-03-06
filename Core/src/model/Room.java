package model;

import meta.Main;
import model.board.statics.StaticEntity;
import util.Position;
import util.Util;

import java.util.function.BiConsumer;

/**
 * Une salle
 */
public class Room {
    public static final short SIZE_X = 30, SIZE_Y = 15;

    public final Position start, exit;

    private final StaticEntity[][] grid = new StaticEntity[SIZE_X][SIZE_Y];
    /**
     * Est-ce que la salle est terminée (sortie atteinte) ?
     */
    private boolean done;

    public Room() {
        this(
                new Position(-1, -1),
                new Position(-1, -1),
                false
        );
    }

    public Room(Position start, Position exit, boolean noDoor){
        this.start = start;
        this.exit = exit;

        Main.plugins.forEach(
                plugin -> plugin.model.generateRoom(this, noDoor)
        );
    }

    /**
     *
     * @param x Position X
     * @param y Position Y
     * @return L'entité statique à la position spécifiée
     */
    public StaticEntity getStatic(int x, int y) {
        if (x < 0 || x >= SIZE_X || y < 0 || y >= SIZE_Y) {
            // L'entité demandée est en-dehors de la grille
            return null;
        }
        return grid[x][y];
    }

    /**
     *
     * @param pos Position
     * @return L'entité statique à la position spécifiée
     */
    public StaticEntity getStatic(Position pos) {
        return getStatic(pos.x, pos.y);
    }

    public boolean isDone() {
        return done;
    }

    /**
     * Ajoute une entité statique d'un type donné à la position donnée
     * @param type Type d'entité statique à ajouter
     * @param position Position
     */
    public void addStatic(Class<? extends StaticEntity> type, Position position) {
        addStatic(Util.Reflections.instantiate(type, this), position);
    }

    /**
     * Ajoute une entité statique à une position donnée
     * @param entity Entité à ajouter
     * @param position Position
     */
    public void addStatic(StaticEntity entity, Position position) {
        addStatic(entity, position.x, position.y);
    }

    /**
     * Ajoute une entité statique à une position donnée
     * @param entity Entité à ajouter
     * @param x Position x
     * @param y Position y
     */
    public void addStatic(StaticEntity entity, int x, int y) {
        grid[x][y] = entity;
    }

    /**
     * Applique une fonction à chaque slot de la salle, arguments de la fonction : posX, posY.
     * @param func Fonction à appliquer.
     */
    public void forEachSlot(BiConsumer<Short, Short> func){
        for (short x = 0; x < SIZE_X; x++) {
            for (short y = 0; y < SIZE_Y; y++)
                func.accept(x, y);
        }
    }

    /**
     * Marque la salle comme terminée
     */
    public void terminate() {
        done = true;
    }

}
