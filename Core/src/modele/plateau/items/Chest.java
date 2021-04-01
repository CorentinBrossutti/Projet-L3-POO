package modele.plateau.items;

import modele.plateau.Inventory;

public class Chest extends Item {
    private Inventory inventory = new Inventory();

    public Inventory getInventory(){
        return inventory;
    }
}
