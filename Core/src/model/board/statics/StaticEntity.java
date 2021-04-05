package model.board.statics;

import model.Player;
import model.Room;
import model.board.items.Item;

/**
 * Une entité statique (qui ne bouge pas)
 */
public abstract class StaticEntity {
    /**
     * La salle dans laquelle se trouve cette entité statique
     */
    protected Room room;

    public StaticEntity(Room _room) {
        this.room = _room;
    }

    /**
     * Retourne vrai s'il y a colllision, et donc si le joueur ne peut PAS passer
     * @param character Joueur
     * @return Vrai ou faux
     */
    public abstract boolean collide(Player character);

    /**
     * Méthode exécutée lorsque le joueur entre sur la case
     * @param character Le joueur
     */
    public void enter(Player character){
    }
    /**
     * Méthode exécutée lorsque le joueur quitte la case
     * @param character Joueur
     */
    public void leave(Player character) {
    }

    /**
     * Méthode exécutée lorsque le joueur utilise un objet sur la case
     * @param character Le joueur
     * @param item      L'objet
     * @return Vrai si l'objet peut être utilisé (retiré de l'inventaire donc), faux sinon
     */
    public boolean use(Player character, Item item) {
        return false;
    }

}