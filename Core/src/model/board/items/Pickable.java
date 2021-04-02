package model.board.items;

import model.board.Player;

/**
 * Interface pour les objets ramassables
 */
public interface Pickable {
    /**
     * Méthode appelée lorsque le joueur passe sur une case avec l'objet
     * @param picker Le joueur qui souhaite ramasser l'objet
     */
    public void pickup(Player picker);
}
