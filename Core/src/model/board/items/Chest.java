package model.board.items;

import model.board.Gen;
import model.board.Inventory;
import model.board.Player;

/**
 * Un coffre. Lorsque le joueur passe dessus, son contenu est ramassé directement dans l'inventaire du joueur.
 */
public class Chest extends Item implements Pickable {
    /**
     * Taille par défaut de l'inventaire du coffre, certains emplacements peuvent ne rien contenir
     */
    public static final int CHEST_INVENTORY_SIZE = 10;

    /**
     * Inventaire du coffre
     */
    private final Inventory inventory = new Inventory();

    public Chest() {
        this(true);
    }

    /**
     * Constructeur, génère potentiellement un contenu aléatoire
     * @param gen Générer aléatoirement un contenu pour le coffre
     */
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
