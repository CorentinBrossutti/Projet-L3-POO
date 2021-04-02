package model.board.statics;

import model.board.Player;
import model.board.Room;
import model.board.items.Key;

/**
 * Une porte, verrouillable
 */
public class Door extends StaticEntity implements Lockable {
    protected boolean locked;

    public Door(Room _room) {
        super(_room);
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    @Override
    public void unlock(Key key) {
        locked = false;
    }

    @Override
    public boolean collide(Player character) {
        // Si la porte est verrouillée, alors il y a collision
        if (locked)
            return true;
        // Sinon on quitte la pièce
        room.terminate();
        return false;
    }
}
