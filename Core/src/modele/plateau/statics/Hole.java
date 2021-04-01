package modele.plateau.statics;

import modele.plateau.Player;
import modele.plateau.Room;

public class Hole extends StaticEntity {
    public Hole(Room _room) {
        super(_room);
    }

    @Override
    public boolean collide(Player character) {
        return true;
    }
}
