package base.model.board.items;

import model.Player;
import model.board.items.Item;
import model.board.items.Pickable;

/**
 * Une capsule d'eau
 */
public class WaterCap extends Item implements Pickable {

    @Override
    public void pickup(Player picker) {
        picker.inventory.add(this);
    }
}
