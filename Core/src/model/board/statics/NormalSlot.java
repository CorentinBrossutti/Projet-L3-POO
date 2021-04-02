package model.board.statics;

import model.board.Player;
import model.board.Room;
import model.board.items.Item;
import model.board.items.NoItem;
import model.board.items.Pickable;

/**
 * Une case normale
 */
public class NormalSlot extends StaticEntity {
    /**
     * La case peut supporter un objet quelconque
     */
    public Item item;

    public NormalSlot(Room _room) {
        super(_room);
    }

    @Override
    public boolean collide(Player character) {
        // S'il y a un objet ramassable sur cette case, il est ramassé dès que le joueur passe dessus
        if (item instanceof Pickable) {
            ((Pickable) item).pickup(character);
            item = new NoItem();
        }

        return false;
    }
}
