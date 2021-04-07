package enemies.model.board;

import base.model.board.statics.Hole;
import base.model.board.statics.SingleUsageSlot;
import model.Character;
import model.Collideable;
import model.Game;
import model.board.statics.StaticEntity;
import util.Position;

public class Enemy extends Character implements Collideable {
    public Enemy(Game game, int x, int y) {
        super(game, x, y);

        addController("enemy", new CharacterControllerEnemy(this));
    }

    public Enemy(Game game, Position position) {
        super(game, position);
    }


    @Override
    public Boolean collidesWith(StaticEntity staticEntity) {
        return staticEntity instanceof Hole || staticEntity instanceof SingleUsageSlot ? false : null;
    }
}
