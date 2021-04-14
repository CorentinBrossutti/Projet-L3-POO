package enemies.model;

import base.ModelBase;
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
    public static final byte COLLISION_ODDS = 14;

    public Enemy(Game game, int x, int y) {
        super(game, x, y);

        addController(PluginEnemies.ENEMY_CTRL_HANDLER, new CharacterControllerEnemy(this));
    }

    public Enemy(Game game, Position position) {
        super(game, position);
    }


    @Override
    public Boolean collidesWith(StaticEntity staticEntity) {
        // Il y a une grande chance qu'il ignore la collision, afin qu'il ne meure pas trop vite
        if(staticEntity instanceof SingleUsageSlot && game.gen.should(COLLISION_ODDS))
            return false;
        else if(staticEntity instanceof Hole)
            return false;
        else if(staticEntity instanceof Door)
            return true;

        return null;
    }

    @Override
    public void kill(String source) {
        if(source.equals(ModelBase.SU_SLOT_DEATH_SOURCE))
            super.kill(source);
    }
}
