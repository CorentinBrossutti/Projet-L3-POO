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

public abstract class Plugin {
    public Model model;
    public Model.Events events;
    public ViewController viewController;
    public Game game;
    public String name;

    public Plugin(Game game, ViewControllerHandle handle, String name) {
        this.game = game;
        this.name = name;
        this.viewController = new ViewController.DummyViewController(handle, game);
        this.events = ((DummyModel)(this.model = new DummyModel())).new DummyEvents();
    }

    /**
     * Appelée sur chaque plugin à chaque rafraîchissement du jeu
     */
    public void tick() {
    }

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

        public PlayerController customController(Player player){
            return null;
        }

        public abstract class Events{
            public void playerChangesRoom(Player player, Room previous, Room next){
            }

            public boolean playerMoves(Player player, Position from, Position to, Player.Orientation direction){
                return true;
            }

            public boolean playerUses(Player player, StaticEntity target, Item used){
                return true;
            }
        }
    }

    public class DummyModel extends Model{
        public DummyModel(){
            itemSupplier = new WeightedRandomSupplier<Item>() {
                @Override
                public Map<Class<? extends Item>, Integer> supplyWeights() {
                    return new HashMap<>();
                }
            };
            staticSupplier = new WeightedRandomSupplier<StaticEntity>() {
                @Override
                public Map<Class<? extends StaticEntity>, Integer> supplyWeights() {
                    return new HashMap<>();
                }
            };
        }

        public class DummyEvents extends Events{}
    }
}
