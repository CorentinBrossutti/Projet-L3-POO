package model.board.items;

import model.board.Player;

/**
 * Un clé, ramassable
 */
public class Key extends Item implements Pickable {

    @Override
    public void pickup(Player picker) {
        picker.getInventory().add(this);
    }
}
