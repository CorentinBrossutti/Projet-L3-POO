package model.board.statics;

import model.board.Player;
import model.board.Room;

public class Wall extends StaticEntity {
    public Wall(Room _room) { super(_room); }

    @Override
    public boolean collide(Player character) {
        return true;
    }
}
