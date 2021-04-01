package model.board.items;

import model.board.Gen;
import model.board.Inventory;
import model.board.Player;

public class Chest extends Item implements Pickable {
    public static final int CHEST_INVENTORY_SIZE = 10;

    private Inventory inventory = new Inventory();

    public Chest() {
        this(true);
    }

    public Chest(boolean gen) {
        if (gen) {
            for (int i = 0; i < CHEST_INVENTORY_SIZE; i++)
                inventory.add(Gen.pickItem());
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Override
    public void pickup(Player picker) {
        inventory.into(picker.getInventory());
    }
}
