package meta;

import model.Game;
import model.board.items.Item;
import model.board.statics.StaticEntity;
import util.WeightedRandomSupplier;
import view.ViewControllerHandle;

import java.util.HashMap;
import java.util.Map;

/**
 * Un plugin, une extension du jeu.
 * Les classes modèles, événementielles et contrôleuses sont supposées être écrasées au besoin
 */
public abstract class Plugin {
    /**
     * Fonctionnalités du plugin liées au modèle
     */
    public Model model;
    /**
     * Gestion des événements par le plugin
     */
    public Events events;
    /**
     * Le contrôleur-vue du plugin
     */
    public ViewController viewController;
    public Game game;
    public String name;

    public Plugin(Game game, ViewControllerHandle handle, String name) {
        this.game = game;
        this.name = name;
        // Par défaut, modèles, événements et contrôleur vides
        this.viewController = new ViewController.DummyViewController(handle, game);
        this.events = new DummyEvents();
    }

    /**
     * Appelée sur chaque plugin à chaque rafraîchissement du jeu
     */
    public void tick() {
    }

    /**
     * Un modèle vide par défaut pour les plugins ne s'en servant pas
     */
    public static class DummyModel extends Model{
        public DummyModel(){
            itemSupplier = new WeightedRandomSupplier<>() {
                @Override
                public Map<Class<? extends Item>, Integer> supplyWeights() {
                    return new HashMap<>();
                }
            };
            staticSupplier = new WeightedRandomSupplier<>() {
                @Override
                public Map<Class<? extends StaticEntity>, Integer> supplyWeights() {
                    return new HashMap<>();
                }
            };
        }

    }

    public static class DummyEvents extends Events {}
}
