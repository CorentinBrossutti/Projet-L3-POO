package model;

import meta.Events;
import meta.Main;
import model.board.statics.StaticEntity;
import util.Position;

/**
 * Classe abstraite contrôlant le mouvement du joueur
 */
public abstract class CharacterController {
    protected final Character character;

    public CharacterController(Character character) {
        this.character = character;
    }

    /**
     * Tente de faire avancer le joueur d'une case
     *
     * @param direction Direction du mouvement, définira aussi l'orientation du joueur
     */
    public void move(Player.Orientation direction) {
        Position pos = direction.getNextPos(character.position);
        StaticEntity se = character.room().getStatic(pos);

        if ((!(character instanceof Collideable) || !se.collide((Collideable) character)) &&
                Events.callBool(Main.plugins, Events.PLAYER_MOVES, false, character, character.position, pos, direction)) {
            // On obtient la case actuelle et on éxécute l'événement pour la quitter
            character.room().getStatic(character.position).leave(character);
            character.position = pos;
            se.enter(character);
        }
        character.orientation = direction;
    }
}
