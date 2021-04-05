package base.model.board.statics;

import model.Player;
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
    public boolean collide(Player character) {
        return true;
    }
}
