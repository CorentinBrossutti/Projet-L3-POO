package meta;

import model.Game;
import model.Player;
import model.PlayerController;
import model.Room;
import model.board.items.Item;
import model.board.statics.StaticEntity;
import util.Position;
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
    public Model.Events events;
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
        this.events = ((DummyModel)(this.model = new DummyModel())).new DummyEvents();
    }

    /**
     * Appelée sur chaque plugin à chaque rafraîchissement du jeu
     */
    public void tick() {
    }

    /**
     * La classe Model contient les objets et méthodes liés à la gestion du modèle par le plugin
     */
    public abstract class Model{
        /**
         * Sera additionné avec les autres fournisseurs (!)
         */
        public WeightedRandomSupplier<Item> itemSupplier;
        /**
         * Sera additionné avec les autres fournisseurs (!)
         */
        public WeightedRandomSupplier<StaticEntity> staticSupplier;

        /**
         * Appelée sur chaque plugin lors de la génération d'une salle
         * @param room L'objet à salle à remplir
         * @param noDoor Vrai si la salle ne doit pas contenir de portes
         */
        public void generateRoom(Room room, boolean noDoor){
        }

        /**
         * Les contrôleurs personnalisés sont accessibles en utilisant {@link Player#getController(String)}.
         * Le nom du plugin est la référence d'accès, par défaut.
         * @param player Le joueur
         * @return Un contrôleur personnalisé pour ce plugin, qui sera attaché au joueur
         */
        public PlayerController customController(Player player){
            return null;
        }

        /**
         * Gestion des événements
         */
        public abstract class Events{
            /**
             * Appelée lorsque le joueur change de salle
             * @param player Le joueur
             * @param previous Salle précédente
             * @param next Salle suivante
             */
            public void playerChangesRoom(Player player, Room previous, Room next){
            }

            /**
             * Appelée lorsque le joueur bouge
             * @param player Le joueur
             * @param from Position précédente
             * @param to Position suivante (destination)
             * @param direction La direction du mouvement
             * @return Vrai si le mouvement doit avoir lieu, faux sinon
             */
            public boolean playerMoves(Player player, Position from, Position to, Player.Orientation direction){
                return true;
            }

            /**
             * Appelée lorsque le joueur utilise un objet
             * @param player Le joueur
             * @param target L'entité ciblée par l'objet
             * @param used L'objet utilisé
             * @return Vrai si l'objet doit être utilisé, faux sinon
             */
            public boolean playerUses(Player player, StaticEntity target, Item used){
                return true;
            }
        }
    }

    /**
     * Un modèle vide par défaut pour les plugins ne s'en servant pas
     */
    public class DummyModel extends Model{
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

        public class DummyEvents extends Events{}
    }
}
