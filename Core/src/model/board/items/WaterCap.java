package model.board.items;

import model.board.Player;

public class WaterCap extends Item implements Pickable {

    @Override
    public void pickup(Player picker) {
        picker.getInventory().add(this);
    }
}
