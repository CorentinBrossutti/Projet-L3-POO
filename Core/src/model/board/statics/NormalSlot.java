package model.board.statics;

import model.board.Player;
import model.board.items.Pickable;
import model.board.Room;
import model.board.items.Item;
import model.board.items.NoItem;

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
