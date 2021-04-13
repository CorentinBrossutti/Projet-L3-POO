package model;

import meta.events.Events;
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
     * Tente de faire avancer le personnage d'une case
     * @param direction Direction du mouvement, définira aussi l'orientation du personnage
     */
    public void move(Character.Orientation direction){
        move(direction, 1);
    }

    /**
     * Tente de faire avancer le personnage d'un certain nombre de cases
     * @param direction Direction du mouvement, définira aussi l'orientation du personnage
     * @param distance Distance du mouvement (nombre de cases dans la direction)
     */
    public void move(Player.Orientation direction, int distance) {
        move(direction.getNextPos(character.position, distance), direction);
    }

    /**
     * Déplace le personnage à la position souhaitée.
     * L'orientation sera automatiquement assignée en fonction de la direction entre la position actuelle et la position de destination.
     * @param destination Position de destination
     */
    public void move(Position destination){
        move(destination, Character.Orientation.resolve(character.position, destination));
    }

    /**
     * Déplace le personnage à la position souhaitée avec l'orientation souhaitée.
     * @param destination Position de destination
     * @param direction Orientation désirée
     */
    public void move(Position destination, Character.Orientation direction){
        StaticEntity se = character.room().getStatic(destination);

        // Mouvement invalide
        if(se == null)
            return;

        if ((!(character instanceof Collideable) || !se.collide((Collideable) character)) &&
                Events.callBool(Main.plugins, Events.PLAYER_MOVES, false, character, character.position, destination, direction)) {
            // On obtient la case actuelle et on éxécute l'événement pour la quitter
            character.room().getStatic(character.position).leave(character);
            character.position = destination;
            se.enter(character);
        }
        character.orientation = direction;
    }
}
