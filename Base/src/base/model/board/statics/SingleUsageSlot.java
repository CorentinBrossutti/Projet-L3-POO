package base.model.board.statics;

import base.model.board.items.WaterCap;
import model.Character;
import model.Collideable;
import model.Player;
import model.Room;
import model.board.items.Item;
import model.board.statics.Usable;

/**
 * Une case à usage unique, qui devient inutilisable lorsqu'un joueur passe dessus (après l'avoir quittée)
 */
public class SingleUsageSlot extends NormalSlot implements Usable {
    /**
     * Usable = false si la case est utilisée (ne peut pas passer dessus)
     * Vrai sinon
     */
    protected boolean usable = true;

    public SingleUsageSlot(Room _room) {
        super(_room);
    }

    @Override
    public boolean usable() {
        return usable;
    }

    @Override
    public void mark(boolean usable) {
        this.usable = usable;
    }

    @Override
    public boolean collide(Collideable collideable) {
        super.collide(collideable);

        // Si la case est utilisée, le joueur ne peut pas la traverser (collision = vrai)
        return collideable.askCollision(this, !usable, Boolean::logicalAnd);
    }

    @Override
    public void enter(Character character) {
        super.enter(character);

        if (!usable)
            character.kill();
    }

    @Override
    public void leave(Character character) {
        super.leave(character);
        // La case devient utilisée lorsque le joueur passe dessus
        usable = false;
    }

    @Override
    public boolean use(Player user, Item item) {
        super.use(user, item);

        // Si le joueur essaie d'utiliser une capsule sur cette case...
        if (item instanceof WaterCap) {
            // Alors elle devient de nouveau utilisable
            return (usable = true);
        }
        return false;
    }
}
