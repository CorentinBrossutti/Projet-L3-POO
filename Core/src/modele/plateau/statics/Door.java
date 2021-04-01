package modele.plateau.statics;

import modele.plateau.Player;
import modele.plateau.Room;
import modele.plateau.items.Key;

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
        if(locked)
            return true;
        room.terminate();
        return false;
    }
}
