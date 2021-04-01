package modele.plateau.items;

import modele.plateau.Player;

public class Key extends Item implements Pickable {

    @Override
    public void pickup(Player picker) {
        picker.getInventory().add(this);
    }
}
