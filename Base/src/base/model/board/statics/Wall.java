package base.model.board.statics;

import model.Collideable;
import model.Room;
import model.board.statics.StaticEntity;

/**
 * Un mur, on ne peut jamais le traverser
 */
public class Wall extends StaticEntity {
    public Wall(Room _room) {
        super(_room);
    }

    @Override
    public boolean collide(Collideable collideable) {
        return collideable.askCollision(this, true, Boolean::logicalAnd);
    }
}
