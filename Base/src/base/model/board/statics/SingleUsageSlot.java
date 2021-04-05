package base.model.board.statics;

import model.Player;
import model.Room;
import model.board.items.Item;
import base.model.board.items.WaterCap;

/**
 * Une case à usage unique, qui devient inutilisable lorsqu'un joueur passe dessus (après l'avoir quittée)
 */
public class SingleUsageSlot extends NormalSlot {
    /**
     * Usable = false si la case est utilisée (ne peut pas passer dessus)
     * Vrai sinon
     */
    protected boolean usable = true;

    public SingleUsageSlot(Room _room) {
        super(_room);
    }

    public boolean isUsable() {
        return usable;
    }

    @Override
    public boolean collide(Player player) {
        // Si la case est utilisée, le joueur ne peut pas la traverser (collision = vrai)
        return !usable;
    }

    @Override
    public void leave(Player character) {
        super.leave(character);
        // La case devient utilisée lorsque le joueur passe dessus
        usable = false;
    }

    @Override
    public boolean use(Player character, Item item) {
        // Si le joueur essaie d'utiliser une capsule sur cette case...
        if (item instanceof WaterCap) {
            // Alors elle devient de nouveau utilisable
            return (usable = true);
        }
        return false;
    }
}
