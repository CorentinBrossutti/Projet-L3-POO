package base.model.board.items;

import model.Game;
import model.Player;
import model.board.items.ItemContainer;
import model.board.items.Pickable;

/**
 * Un coffre. Lorsque le joueur passe dessus, son contenu est ramassé directement dans l'inventaire du joueur.
 */
public class Chest extends ItemContainer implements Pickable {
    /**
     * Taille par défaut de l'inventaire du coffre, certains emplacements peuvent ne rien contenir
     */
    public static final byte CHEST_INVENTORY_SIZE = 10;

    public Chest(Game.Gen gen) {
        this(gen, true);
    }

    /**
     * Constructeur, génère potentiellement un contenu aléatoire
     *
     * @param fill Générer aléatoirement un contenu pour le coffre si vrai
     */
    public Chest(Game.Gen gen, boolean fill) {
        super(gen);

        if (fill) {
            for (byte i = 0; i < CHEST_INVENTORY_SIZE; i++)
                inventory.add(gen.pickItem());
        }
    }

    @Override
    public void pickup(Player picker) {
        inventory.into(picker.inventory);
    }
}
