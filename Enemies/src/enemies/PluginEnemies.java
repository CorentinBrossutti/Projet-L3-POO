package enemies;

import enemies.model.board.CharacterControllerEnemy;
import enemies.model.board.Enemy;
import meta.Plugin;
import model.Game;
import view.RotatableImageIcon;
import view.ViewControllerHandle;

public class PluginEnemies extends Plugin {
    public static final double ENEMY_RANGE = 1.5;

    public RotatableImageIcon enemy;

    public PluginEnemies(Game game, ViewControllerHandle handle) {
        super(game, handle, "enemies");

        model = new ModelEnemies(this);
        viewController = new ViewControllerEnemies(this, handle, game);
    }

    public void registerEnemy(int x, int y){
        Enemy e = new Enemy(game, x, y);
        registerCharacter(e, enemy);
    }

    @Override
    public void tick() {
        for(model.Character c : game.characters){
            if(c instanceof Enemy){
                CharacterControllerEnemy controller = c.getController("enemy");
                double dist = c.position.distance(game.getPlayer().position);
                if(dist == 0)
                    game.getPlayer().kill();
                else if(dist <= ENEMY_RANGE)
                    controller.go(game.getPlayer().position);
                if(game.gen.should(5))
                    controller.randomMovement();
            }
        }
    }
}
