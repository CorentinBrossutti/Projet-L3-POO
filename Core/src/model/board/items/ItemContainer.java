package model.board.items;

import model.Game;
import model.Inventory;

public abstract class ItemContainer extends Item {
    protected Game.Gen gen;
    /**
     * Inventaire du coffre
     */
    protected Inventory inventory = new Inventory();

    public ItemContainer(Game.Gen gen) {
        this.gen = gen;
    }
}
