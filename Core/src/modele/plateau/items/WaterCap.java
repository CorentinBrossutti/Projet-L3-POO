package modele.plateau.items;

import modele.plateau.Player;

public class WaterCap extends Item implements Pickable {

    @Override
    public void pickup(Player picker) {
        picker.getInventory().add(this);
    }
}
