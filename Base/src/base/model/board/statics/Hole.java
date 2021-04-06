package base.model.board.statics;

import model.Character;
import model.Collideable;
import model.Room;
import model.board.statics.StaticEntity;

/**
 * Un trou dans le sol
 */
public class Hole extends StaticEntity {
    public Hole(Room _room) {
        super(_room);
    }

    @Override
    public boolean collide(Collideable collideable) {
        return collideable.askCollision(this, true, Boolean::logicalAnd);
    }

    @Override
    public void enter(Character character) {
        character.kill();
    }
}
