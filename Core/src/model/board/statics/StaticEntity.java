package model.board.statics;

import model.board.Player;
import model.board.Room;
import model.board.items.Item;

/**
 * Ne bouge pas (murs...)
 */
public abstract class StaticEntity {
    protected Room room;

    public StaticEntity(Room _room) {
        this.room = _room;
    }

    /**
     * Retourne vrai s'il y a colllision
     *
     * @param character Joueur
     * @return Vrai ou faux
     */
    public abstract boolean collide(Player character);

    /**
     * Méthode exécutée lorsque le joueur quitte la case
     *
     * @param character Joueur
     */
    public void leave(Player character) {
    }

    /**
     * Méthode exécutée lorsque le joueur utilise un objet sur la case
     *
     * @param character Le joueur
     * @param item      L'objet
     * @return Vrai si l'objet peut être utilisé (retiré de l'inventaire donc), faux sinon
     */
    public boolean use(Player character, Item item) {
        return false;
    }

}