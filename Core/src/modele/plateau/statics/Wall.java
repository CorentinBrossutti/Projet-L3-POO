package modele.plateau.statics;

import modele.plateau.Player;
import modele.plateau.Room;
import modele.plateau.statics.StaticEntity;

public class Wall extends StaticEntity {
    public Wall(Room _room) { super(_room); }

    @Override
    public boolean collide(Player character) {
        return true;
    }
}
