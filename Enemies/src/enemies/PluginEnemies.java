package enemies;

import enemies.model.CharacterControllerEnemy;
import enemies.model.Enemy;
import enemies.model.ModelEnemies;
import meta.Plugin;
import model.Character;
import model.Game;
import util.Position;
import view.RotatableImageIcon;
import view.ViewControllerHandle;

import java.util.ArrayList;

public class PluginEnemies extends Plugin {
    /**
     * Distance à partir de laquelle les ennemis repèrent les joueurs
     */
    public static final double ENEMY_RANGE = 10.;
    public static final String ENEMY_CTRL_HANDLER = "enemy";
    public static final byte ENEMY_MOVE_ODDS = 4;

    protected int tick_num = 0;
    protected ArrayList<CharacterControllerEnemy.Node> path;

    public RotatableImageIcon enemy;

    public PluginEnemies(Game game, ViewControllerHandle handle) {
        super(game, handle, "enemies");

        viewController = new ViewControllerEnemies(this, handle);
        events = new EventsEnemies(this);
        model = new ModelEnemies(this);
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
                    game.player.kill(ModelEnemies.ENEMY_DEATH_SOURCE);
                else if(dist <= ENEMY_RANGE){
                        path = controller.solve(game.player.position);
                        c.position.x = path.get(tick_num).x;
                        c.position.y = path.get(tick_num).y;
                        if (game.gen.should(10)) { // Permet au joueur d'avoir le temps de se déplacer -> sinon l'ennemi est trop rapide
                            controller.move(c.position);
                            this.tick_num++;
                    }
                }
                /*else if(game.gen.should(ENEMY_MOVE_ODDS))
                    controller.randomMovement();*/
            }
        }
    }
}
