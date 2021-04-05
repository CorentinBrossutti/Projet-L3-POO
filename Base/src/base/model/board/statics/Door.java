package base.model.board.statics;

import model.Player;
import model.Room;
import model.board.statics.StaticEntity;
import model.board.statics.Usable;

/**
 * Une porte, verrouillable
 */
public class Door extends StaticEntity implements Usable {
    protected boolean locked;

    public Door(Room _room) {
        super(_room);
    }

    @Override
    public boolean usable() {
        return locked;
    }

    @Override
    public void mark(boolean usable) {
        locked = usable;
    }

    @Override
    public boolean collide(Player character) {
        // Si la porte est verrouillée, alors il y a collision
        return locked;
    }

    @Override
    public void enter(Player character) {
        // Si la porte est empruntée, on quitte la pièce
        room.terminate();
    }
}
