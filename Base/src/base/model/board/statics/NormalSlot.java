package base.model.board.statics;

import model.Character;
import model.Collideable;
import model.Player;
import model.Room;
import model.board.items.Item;
import model.board.items.NoItem;
import model.board.items.Pickable;
import model.board.statics.StaticEntity;

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
    public boolean collide(Collideable collideable) {
        return false;
    }

    @Override
    public void enter(Character character) {
        // S'il y a un objet ramassable sur cette case, il est ramassé dès que le joueur passe dessus
        if (item instanceof Pickable && character instanceof Player) {
            ((Pickable) item).pickup((Player) character);
            item = new NoItem();
        }
    }
}
