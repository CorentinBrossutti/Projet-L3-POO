package enemies.model;

import base.ModelBase;
import base.model.board.statics.Door;
import base.model.board.statics.Hole;
import base.model.board.statics.SingleUsageSlot;
import base.model.board.statics.Wall;
import enemies.PluginEnemies;
import model.Character;
import model.Collideable;
import model.Game;
import model.board.statics.StaticEntity;
import util.Position;

public class Enemy extends Character implements Collideable {
    /**
     * Probabilité qu'il y ai une collision 1/2 avec les cases causant la mort de l'ennemi
     */
    public static final byte COLLISION_ODDS = 2;

    /**
     * Un compteur pour gerer les déplacement de l'ennemi dans PLuginEnemies
     */
    private int tick_num = 0;

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
        else if(staticEntity instanceof Hole || staticEntity instanceof Wall)
            return false;
        else if(staticEntity instanceof Door)
            return true;

        return null;
    }

    /**
     * Setter - Incremente le tick_num de 1
     */
    public void upTick_num(){
        this.tick_num += 1;
    }

    /**
     * Reset le Tick_num - le met à 0
     */
    public void resetTick_num(){
        this.tick_num = 0;
    }

    /**
     * Getter - Retourne la valeur du tick_num
     * @return
     */
    public int getTick_num(){
        return this.tick_num;
    }

    @Override
    public void kill(String source) {
        if(source.equals(ModelBase.SU_SLOT_DEATH_SOURCE))
            super.kill(source);
    }
}
