package meta;

import meta.events.Events;
import model.Game;
import model.board.items.Item;
import model.board.statics.StaticEntity;
import util.WeightedRandomSupplier;
import view.RotatableImageIcon;
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
    public final Game game;
    public final String name;

    public Plugin(Game game, ViewControllerHandle handle, String name) {
        this.game = game;
        this.name = name;
        // Par défaut, modèles, événements et contrôleur vides
        this.viewController = new ViewController.DummyViewController(this, handle);
        this.events = new DummyEvents(this);
    }

    /**
     * Appelée sur chaque plugin à chaque rafraîchissement du jeu.
     */
    public void tick() {
    }

    /**
     * Enregistre un personnage.
     * @param character Le personnage
     * @param icon L'icône du personnage pour affichage
     */
    public void registerCharacter(model.Character character, RotatableImageIcon icon){
        viewController.handle.characterIcons.put(character, icon);
        game.characters.add(character);
    }

    /**
     * Un modèle vide par défaut pour les plugins ne s'en servant pas.
     */
    public static class DummyModel extends Model{
        public DummyModel(Plugin plugin){
            super(plugin);

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

    public static class DummyEvents extends Events {
        public DummyEvents(Plugin plugin) {
            super(plugin);
        }
    }
}
