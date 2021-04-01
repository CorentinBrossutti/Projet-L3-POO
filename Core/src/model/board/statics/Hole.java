package model.board.statics;

import model.board.Player;
import model.board.Room;

public class Hole extends StaticEntity {
    public Hole(Room _room) {
        super(_room);
    }

    @Override
    public boolean collide(Player character) {
        return true;
    }
}