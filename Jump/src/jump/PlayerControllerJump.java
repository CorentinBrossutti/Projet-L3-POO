package jump;

import base.model.board.statics.Wall;
import model.Player;
import model.board.statics.StaticEntity;
import util.Position;

public class PlayerControllerJump extends Player.PlayerController {
    /**
     * Distance du saut
     */
    public static final short JUMP_DISTANCE = 2;

    public PlayerControllerJump(Player player) {
        super(player);
    }

    /**
     * Tente de faire sauter le joueur deux cases plus loin
     */
    public void jump() {
        Position target = character.orientation.getNextPos(character.position, JUMP_DISTANCE);
        StaticEntity from = character.room().getStatic(character.orientation.getNextPos(character.position)),
                to = character.room().getStatic(target);

        // Le joueur ne peut pas sauter, ni atterir sur un objet créant une collision
        if (!(from instanceof Wall) && !to.collide(player)) {
            from.leave(character);
            character.position = target;
            to.enter(character);
        }
    }
}
