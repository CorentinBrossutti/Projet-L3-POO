package base.model.board.statics;

import model.Player;
import model.Room;
import model.board.statics.Lockable;
import model.board.statics.StaticEntity;

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
    public void unlock() {
        locked = false;
    }

    @Override
    public void lock() {
        locked = true;
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
