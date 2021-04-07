package enemies.model.board;

import model.Character;
import model.CharacterController;
import util.Position;

public class CharacterControllerEnemy extends CharacterController {
    public CharacterControllerEnemy(Character character) {
        super(character);
    }

    /**
     * Plus court chemin, faire aller l'ennemi à la position.
     * @param position Position cible.
     */
    public void go(Position position){
        // TODO
        //character.position = position;
    }

    /**
     * Fait bouger aléatoirement l'ennemi d'une case.
     */
    public void randomMovement(){
        move(character.game.gen.randomOrientation());
    }
}
