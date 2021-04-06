package meta;

import model.Game;
import view.ViewControllerHandle;

/**
 * Une classe permettant de gérer les événements liées au graphisme du jeu.
 */
public abstract class ViewController extends PluginDataClass{
    /**
     * La JFrame du jeu
     */
    protected ViewControllerHandle handle;
    protected Game game;

    public ViewController(Plugin plugin, ViewControllerHandle handle, Game game) {
        super(plugin);
        this.handle = handle;
        this.game = game;
    }

    /**
     * Appelée au lancement de l'application sur chaque plugin pour initialiser ses graphismes propres
     */
    public void initGraphics() {
    }

    /**
     * Appelée au lancement de l'application sur chaque plugin pour initialiser les entrées
     */
    public void setupInputs() {
    }

    /**
     * Appelée à chaque mise à jour des graphismes de l'application, sur chaque plugin
     */
    public void update() {
    }

    /**
     * Un contrôleur vide pour les plugins qui ne s'en servent pas
     */
    public static class DummyViewController extends ViewController{
        public DummyViewController(Plugin plugin, ViewControllerHandle handle, Game game) {
            super(plugin, handle, game);
        }
    }
}
