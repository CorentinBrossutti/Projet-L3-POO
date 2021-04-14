package base.model.board.statics;

import base.model.board.items.Key;
import model.Character;
import model.Collideable;
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

        locked = false;
    }

    @Override
    public boolean usable() {
        return !locked;
    }

    @Override
    public void mark(boolean usable) {
        locked = !usable;
    }

    @Override
    public boolean collide(Collideable collideable) {
        // Si la porte est verrouillée, alors il y a collision
        if (collideable instanceof Player && ((Player) collideable).inventory.has(Key.class)) {
            ((Player) collideable).inventory.removeOneOf(Key.class);
            locked = false;
        }

        return collideable.askCollision(this, locked, Boolean::logicalAnd);
    }

    @Override
    public void enter(Character character) {
        // Si la porte est empruntée, on quitte la pièce
        if (character instanceof Player)
            room.terminate();
    }
}
