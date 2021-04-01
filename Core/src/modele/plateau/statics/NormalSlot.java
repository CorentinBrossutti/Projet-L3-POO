package modele.plateau.statics;

import modele.plateau.Player;
import modele.plateau.Room;
import modele.plateau.items.Item;
import modele.plateau.items.NoItem;
import modele.plateau.items.Pickable;

public class NormalSlot extends StaticEntity {
    public Item item;

    public NormalSlot(Room _room) { super(_room); }

    @Override
    public boolean collide(Player character) {
        if(item instanceof Pickable) {
            ((Pickable) item).pickup(character);
            item = new NoItem();
        }

        return false;
    }
}
