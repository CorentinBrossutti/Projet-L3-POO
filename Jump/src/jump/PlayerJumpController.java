package jump;

import model.Player;
import model.PlayerController;
import model.board.statics.StaticEntity;
import base.model.board.statics.Wall;
import util.Position;

public class PlayerJumpController extends PlayerController {
    /**
     * Distance du saut
     */
    public static final short JUMP_DISTANCE = 2;

    public PlayerJumpController(Player player) {
        super(player);
    }

    /**
     * Tente de faire sauter le joueur deux cases plus loin
     */
    public void jump() {
        Position target = player.orientation.getNextPos(player.position, JUMP_DISTANCE);
        StaticEntity from = player.room().getStatic(player.orientation.getNextPos(player.position)),
                to = player.room().getStatic(target);

        // Le joueur ne peut pas sauter, ni atterir sur un objet créant une collision
        if (!(from instanceof Wall) && !to.collide(player)) {
            from.leave(player);
            player.position = target;
            to.enter(player);
        }
    }
}
