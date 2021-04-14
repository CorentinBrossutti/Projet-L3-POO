package enemies;

import enemies.model.CharacterControllerEnemy;
import enemies.model.Enemy;
import enemies.model.ModelEnemies;
import meta.Plugin;
import model.Character;
import model.Game;
import model.Player;
import util.Position;
import view.RotatableImageIcon;
import view.ViewControllerHandle;

import java.util.ArrayList;

public class PluginEnemies extends Plugin {
    /**
     * Distance à partir de laquelle les ennemis repèrent les joueurs
     */
    public static final double ENEMY_RANGE = 10.;

    /**
     * Handler du controller
     */
    public static final String ENEMY_CTRL_HANDLER = "enemy";

    /**
     * Probabilité que l'ennemi se déplace 1/4
     */
    public static final byte ENEMY_MOVE_ODDS = 4;

    protected int tick_num = 0;
    protected ArrayList<CharacterControllerEnemy.Node> path;

    /**
     * L'icone de l'ennemi
     */
    public RotatableImageIcon enemy;

    /**
     * Constructeur du plugin
     * @param game
     * @param handle
     */
    public PluginEnemies(Game game, ViewControllerHandle handle) {
        super(game, handle, "enemies");

        model = new ModelEnemies(this);
        viewController = new ViewControllerEnemies(this, handle);
        events = new EventsEnemies(this);
    }

    /**
     * Enregistre l'ennemi, lui assigne une position
     * @param x
     * @param y
     */
    public void registerEnemy(int x, int y){
        Enemy e = new Enemy(game, x, y);
        registerCharacter(e, enemy);
    }

    /**
     * Fonction tick - gère la distance avec le joueur et les actions qui en découle.
     */
    @Override
    public void tick() {
        for(Character c : game.characters){
            if(c instanceof Enemy){
                CharacterControllerEnemy controller = c.getController(ENEMY_CTRL_HANDLER); // on récupère le controleur de l'ennemi étudié
                double dist = c.position.distance(game.player.position); // on calcul sa distance par rapport au joueur

                // Si il y a une collision avec le joueur -> le joueur est tué
                if(dist == 0)
                    game.player.kill();

                // Si le joueur est dans le périmètre d'action de l'ennemi, l'ennemi se dirige vers le joueur
                else if(dist <= ENEMY_RANGE){
                    if(path == null){
                        ((Enemy) c).resetTick_num();
                        path = controller.solve(game.player.position); // On récupère le chemin vers le joueur
                    }
                    if(game.player.position.x != path.get(path.size()-1).x || game.player.position.y != path.get(path.size()-1).y){
                        ((Enemy) c).resetTick_num();
                        path = controller.solve(game.player.position);
                    }
                    else{
                        c.position.x = path.get(((Enemy) c).getTick_num()).x;
                        c.position.y = path.get(((Enemy) c).getTick_num()).y;
                        // Permet au joueur d'avoir le temps de se déplacer -> sinon l'ennemi est trop rapide
                        if (game.gen.should(10)) {
                            c.orientation = Character.Orientation.RIGHT; // A changé, creer des erreurs sinon
                            controller.move(c.position);
                            ((Enemy) c).upTick_num();
                        }
                    }
                }

                // Si le joueur n'est pas dans le périmètre de l'ennemi on fais des mouvement aléatoire sur le terrain
                else if(dist > ENEMY_RANGE && game.gen.should(ENEMY_MOVE_ODDS))
                    controller.randomMovement();
            }
        }
    }
}
