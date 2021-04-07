package util;

/**
 * Objet "wrapper" pour une position 2d x y
 */
public class Position {
    public static final Position nullPos = new Position(-1, -1);

    public int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @param pos Position cible
     * @return La distance euclidienne entre cette position et la position cible
     */
    public double distance(Position pos){
        return Math.sqrt(Math.pow(pos.x - x, 2) + Math.pow(pos.y - y, 2));
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Position && ((Position) o).x == x && ((Position) o).y == y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }
}
