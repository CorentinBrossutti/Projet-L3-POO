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

    @Override
    public boolean equals(Object o) {
        return o instanceof Position && ((Position) o).x == x && ((Position) o).y == y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }
}
