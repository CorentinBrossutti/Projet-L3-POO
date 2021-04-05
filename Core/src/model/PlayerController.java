package model;

/**
 * Classe abstraite contrôlant le mouvement du joueur
 */
public abstract class PlayerController {
    protected final Player player;

    public PlayerController(Player player) {
        this.player = player;
    }
}
