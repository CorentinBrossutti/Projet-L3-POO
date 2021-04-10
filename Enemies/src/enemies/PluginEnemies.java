package enemies;

import enemies.model.CharacterControllerEnemy;
import enemies.model.Enemy;
import meta.Plugin;
import model.Character;
import model.Game;
import view.RotatableImageIcon;
import view.ViewControllerHandle;

public class PluginEnemies extends Plugin {
    /**
     * Distance à partir de laquelle les ennemis repèrent les joueurs
     */
    public static final double ENEMY_RANGE = 10.;
    public static final String ENEMY_CTRL_HANDLER = "enemy";
    public static final byte ENEMY_MOVE_ODDS = 4;

    public RotatableImageIcon enemy;

    public PluginEnemies(Game game, ViewControllerHandle handle) {
        super(game, handle, "enemies");

        model = new ModelEnemies(this);
        viewController = new ViewControllerEnemies(this, handle);
        events = new EventsEnemies(this);
    }

    public void registerEnemy(int x, int y){
        Enemy e = new Enemy(game, x, y);
        registerCharacter(e, enemy);
    }

    @Override
    public void tick() {
        for(Character c : game.characters){
            if(c instanceof Enemy){
                CharacterControllerEnemy controller = c.getController(ENEMY_CTRL_HANDLER);
                double dist = c.position.distance(game.player.position);

                if(dist == 0)
                    game.player.kill();
                else if(dist <= ENEMY_RANGE)
                    controller.solve(game.player.position);
                if(game.gen.should(ENEMY_MOVE_ODDS))
                    controller.randomMovement();
            }
        }
    }
}
