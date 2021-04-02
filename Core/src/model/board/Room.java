package model.board;

import model.board.statics.Door;
import model.board.statics.NormalSlot;
import model.board.statics.StaticEntity;
import model.board.statics.Wall;
import util.Position;
import util.Util;

import java.util.Random;

/**
 * Une salle
 */
public class Room {
    public static final int SIZE_X = 30;
    public static final int SIZE_Y = 15;

    private final StaticEntity[][] grid = new StaticEntity[SIZE_X][SIZE_Y];
    private Position start, exit = new Position(-1, -1);
    /**
     * Est-ce que la salle est terminée (sortie atteinte) ?
     */
    private boolean done;

    public Room() {
        this(false);
    }

    /**
     *
     * @param noDoor Vrai si la salle ne doit pas contenir de porte, faux sinon
     */
    public Room(boolean noDoor) {
        this(noDoor, new Position(-1, -1));
    }

    /**
     *
     * @param noDoor Vrai si la salle ne doit pas contenir de porte, faux sinon
     * @param start Position de départ prédéfinie, (-1, -1) = indéfinie
     */
    public Room(boolean noDoor, Position start) {
        this.start = start;
        // Si la position de départ est invalide, on y ajoute une case normale
        if (start.x >= 0 && start.y >= 0)
            addStatic(new NormalSlot(this), start);

        // Murs extérieurs horizontaux
        for (int x = 0; x < SIZE_X; x++) {
            addStatic(new Wall(this), x, 0);
            addStatic(new Wall(this), x, SIZE_Y - 1);
        }

        // Murs extérieurs verticaux
        for (int y = 1; y < SIZE_Y; y++) {
            addStatic(new Wall(this), 0, y);
            addStatic(new Wall(this), SIZE_X - 1, y);
        }

        // Placement aléatoire d'entités statiques
        StaticEntity temp;
        for (int x = 1; x < SIZE_X - 1; x++) {
            for (int y = 1; y < SIZE_Y - 1; y++) {
                // Si on traite actuellement la case de départ, on l'ignore (traitée plus haut)
                if (this.start.x == x && this.start.y == y)
                    continue;

                // On ajoute une entité statique aléatoire, voire classe "Gen", à la position actuelle
                addStatic((temp = Gen.pickStatic(this)), x, y);
                // Si la case est normale
                if (temp instanceof NormalSlot) {
                    // On y ajoute un objet aléatoire (ou pas, si la fonction de sélection retourne NoItem)
                    ((NormalSlot) temp).item = Gen.pickItem();
                    // Si la position de départ n'est pas encore valide, alors c'est celle-ci (première case valide)
                    if (this.start.x < 0 || this.start.y < 0)
                        this.start = new Position(x, y);
                }
            }
        }

        // Si on souhaite ajouter une porte
        if (!noDoor) {
            Random rand = new Random();
            // La porte se trouve elle sur les rangées horizontales ou verticales ?
            // Après, on choisit aléatoirement, de façon à ce que la porte ne soit pas dans un coin non plus
            if (rand.nextInt(2) == 0)
                grid[exit.x = rand.nextInt(2) == 0 ? 0 : SIZE_X - 1][exit.y = 1 + rand.nextInt(SIZE_Y - 2)] = new Door(this);
            else
                grid[exit.x = 1 + rand.nextInt(SIZE_X - 2)][exit.y = rand.nextInt(2) == 0 ? 0 : SIZE_Y - 1] = new Door(this);

            // Puis on s'assure que la case à côté de la porte est accessible
            Position ns = Gen.getSlotNextToDoor(exit);
            if (!(grid[ns.x][ns.y] instanceof NormalSlot))
                grid[ns.x][ns.y] = new NormalSlot(this);
        }
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

    public Position getStart() {
        return start;
    }

    public Position getExit() {
        return exit;
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
     * Marque la salle comme terminée
     */
    public void terminate() {
        done = true;
    }

}
