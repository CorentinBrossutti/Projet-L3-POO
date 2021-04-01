package model.board.statics;

import model.board.Player;
import model.board.Room;
import model.board.items.Key;

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
        if (locked)
            return true;
        room.terminate();
        return false;
    }
}
