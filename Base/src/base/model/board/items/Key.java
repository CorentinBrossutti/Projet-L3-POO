package base.model.board.items;

import model.Player;
import model.board.items.Item;
import model.board.items.Pickable;

/**
 * Un clé, ramassable
 */
public class Key extends Item implements Pickable {

    @Override
    public void pickup(Player picker) {
        picker.inventory.add(this);
    }
}
