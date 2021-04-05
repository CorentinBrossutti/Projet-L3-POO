package jump;

import model.Player;
import model.PlayerController;
import model.board.statics.StaticEntity;
import base.model.board.statics.Wall;
import util.Position;

public class PlayerJumpController extends PlayerController {
    public PlayerJumpController(Player player) {
        super(player);
    }

    /**
     * Tente de faire sauter le joueur deux cases plus loin
     */
    public void jump() {
        Thread t = Thread.currentThread();
        Position target = player.getOrientation().getNextPos(player.getPosition(), 2);
        StaticEntity from = player.room().getStatic(player.getOrientation().getNextPos(player.getPosition())),
                to = player.room().getStatic(target);

        if (!(from instanceof Wall) && !to.collide(player)) {
            from.leave(player);
            player.setPosition(target);
        }
    }
}
