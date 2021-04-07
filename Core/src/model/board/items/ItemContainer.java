package model.board.items;

import model.Game;
import model.Inventory;

/**
 * Un objet possédant un inventaire
 */
public abstract class ItemContainer extends Item {
    /**
     * Inventaire du conteneur
     */
    public final Inventory inventory = new Inventory();
    /**
     * Objet handle de génération aléatoire
     */
    protected Game.Gen gen;

    public ItemContainer(Game.Gen gen) {
        this.gen = gen;
    }
}
