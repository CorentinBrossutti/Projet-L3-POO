package model.board.statics;

import model.board.Player;
import model.board.Room;

/**
 * Un mur, on ne peut jamais le traverser
 */
public class Wall extends StaticEntity {
    public Wall(Room _room) {
        super(_room);
    }

    @Override
    public boolean collide(Player character) {
        return true;
    }
}
