package enemies.model;

import base.model.board.statics.Door;
import base.model.board.statics.Hole;
import base.model.board.statics.SingleUsageSlot;
import enemies.PluginEnemies;
import model.Character;
import model.Collideable;
import model.Game;
import model.board.statics.StaticEntity;
import util.Position;

public class Enemy extends Character implements Collideable {
    public static final byte COLLISION_ODDS = 2;

    public Enemy(Game game, int x, int y) {
        super(game, x, y);

        addController(PluginEnemies.ENEMY_CTRL_HANDLER, new CharacterControllerEnemy(this));
    }

    public Enemy(Game game, Position position) {
        super(game, position);
    }


    @Override
    public Boolean collidesWith(StaticEntity staticEntity) {
        // Il y a une chance qu'il ignore la collision
        if(game.gen.should(2))
            return null;

        if(staticEntity instanceof Hole)
            return false;
        else if(staticEntity instanceof SingleUsageSlot)
            return false;
        else if(staticEntity instanceof Door)
            return true;

        return null;
    }
}
