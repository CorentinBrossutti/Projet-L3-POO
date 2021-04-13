package model;

import util.Position;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static java.lang.Math.PI;

public abstract class Character {
    public final Game game;
    /**
     * La position du personnage
     */
    public Position position;
    /**
     * L'orientation du personnage (de sa vue)
     */
    public Player.Orientation orientation = Orientation.RIGHT;
    /**
     * Le personnage est-il mort ?
     */
    public boolean dead = false;
    /**
     * Contrôlent les mouvements et interactions du personnage.
     * En clé, le nom du plugin lié, en valeur le contrôleur.
     */
    protected final Map<String, CharacterController> controllers = new HashMap<>();
    protected final UUID characterId = UUID.randomUUID();

    public Character(Game game, int x, int y) {
        this(game, new Position(x, y));
    }

    public Character(Game game, Position position) {
        this.game = game;
        this.position = position;
    }

    /**
     *
     * @param handle Le nom du contrôleur, fréquemment le nom du plugin qui le possède
     * @return Le contrôleur lié
     */
    public <T extends CharacterController> T getController(String handle) {
        return (T) controllers.get(handle);
    }

    /**
     * Evite un cast en passant par un générique
     * @param plugin Le nom du contrôleur, fréquemment le nom du plugin qui le possède
     * @param dummy Classe du contrôleur que l'on cherche à obtenir, évite un cast via un générique
     * @param <T> Générique
     * @return Le contrôleur lié
     */
    public <T extends CharacterController> T getController(String plugin, Class<T> dummy) {
        return getController(plugin);
    }

    /**
     * Si un contrôleur existe avec la même clé, c'est ignoré
     * @param handle La clé, nom du contrôleur
     * @param controller Le contrôleur
     */
    public void addController(String handle, CharacterController controller){
        controllers.putIfAbsent(handle, controller);
    }

    /**
     *
     * @return La salle actuelle du personnage
     */
    public Room room(){
        return game.currentRoom();
    }

    /**
     * Tue le personnage
     */
    public void kill(){
        dead = true;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Character && ((Character) o).characterId.equals(characterId);
    }

    @Override
    public int hashCode() {
        return characterId.hashCode();
    }

    /**
     * Définit l'orientation du personnage (haut, bas, gauche, droite).
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

        /**
         *
         * @param start Position de début
         * @param end Position de fin
         * @return L'orientation la plus proche du vecteur reliant les deux points
         */
        public static Orientation resolve(Position start, Position end){
            int vx = end.x - start.x, vy = end.y - start.y;

            double vmag = Math.sqrt(Math.pow(vx, 2) + Math.pow(vy, 2));
            // Angle en radians entre le vecteur début-fin et le vecteur unitaire X, ainsi que le vecteur unitaire Y
            double angx = Math.acos(vx / vmag), angy = Math.acos(vy / vmag);

            if(angx <= PI / 4 && angy >= PI / 4)
                return Orientation.RIGHT;
            else if(angx >= PI / 4 && angx <= PI / 2 && angy >= PI / 2)
                return Orientation.UP;
            else if(angx >= PI / 2 && angy >= PI / 4)
                return Orientation.LEFT;
            else if(angx >= PI / 4 && angx <= PI / 2 && angy <= PI / 2)
                return Orientation.DOWN;

            return null;
        }
    }
}
