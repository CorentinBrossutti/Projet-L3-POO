package base;

import meta.Plugin;
import base.model.board.items.*;
import base.model.board.statics.*;
import model.Game;
import model.Player;
import model.Room;
import model.board.items.Item;
import model.board.items.NoItem;
import model.board.statics.StaticEntity;
import util.Position;
import util.WeightedRandomSupplier;
import view.ViewControllerHandle;

import java.util.Map;
import java.util.Random;

public class PluginBase extends Plugin {
    /**
     * Le nombre de capsules lorsque l'on rentre dans une salle
     */
    public static int WCAP_COUNT = 1;

    public PluginBase(Game game, ViewControllerHandle handle) {
        super(game, handle, "Base");

        model = new BaseModel();
        viewController = new BaseViewController(handle, game);
        events = ((BaseModel)model).new BaseEvents();
    }



    public class BaseModel extends Model{
        public static final int SIZE_X = 30, SIZE_Y = 15;

        public BaseModel(){
            itemSupplier = new WeightedRandomSupplier<Item>() {
                @Override
                public Map<Class<? extends Item>, Integer> supplyWeights() {
                    return Map.of(
                            NoItem.class, 100,
                            WaterCap.class, 2,
                            Key.class, 1,
                            Chest.class, 2
                    );
                }
            };

            staticSupplier = new WeightedRandomSupplier<StaticEntity>() {
                @Override
                public Map<Class<? extends StaticEntity>, Integer> supplyWeights() {
                    return Map.of(
                            NormalSlot.class, 40,
                            Wall.class, 1,
                            Hole.class, 2,
                            SingleUsageSlot.class, 2
                    );
                }
            };
        }

        @Override
        public void generateRoom(Room room, boolean noDoor) {
            // Si la position de départ est invalide, on y ajoute une case normale
            if (room.start.x >= 0 && room.start.y >= 0)
                room.addStatic(new NormalSlot(room), room.start);

            // Murs extérieurs horizontaux
            for (int x = 0; x < SIZE_X; x++) {
                room.addStatic(new Wall(room), x, 0);
                room.addStatic(new Wall(room), x, SIZE_Y - 1);
            }

            // Murs extérieurs verticaux
            for (int y = 1; y < SIZE_Y; y++) {
                room.addStatic(new Wall(room), 0, y);
                room.addStatic(new Wall(room), SIZE_X - 1, y);
            }

            // Placement aléatoire d'entités statiques
            StaticEntity temp;
            for (int x = 1; x < SIZE_X - 1; x++) {
                for (int y = 1; y < SIZE_Y - 1; y++) {
                    // Si on traite actuellement la case de départ, on l'ignore (traitée plus haut)
                    if (room.start.x == x && room.start.y == y)
                        continue;

                    // On ajoute une entité statique aléatoire, voir classe "Gen", à la position actuelle
                    room.addStatic((temp = game.gen.pickStatic(room)), x, y);
                    // Si la case est normale
                    if (temp instanceof NormalSlot) {
                        // On y ajoute un objet aléatoire (ou pas, si la fonction de sélection retourne NoItem)
                        ((NormalSlot) temp).item = game.gen.pickItem();
                        // Si la position de départ n'est pas encore valide, alors c'est celle-ci (première case valide)
                        if (room.start.x < 0 || room.start.y < 0){
                            room.start.x = x;
                            room.start.y = y;
                        }
                    }
                }
            }

            // Si on souhaite ajouter une porte
            if (!noDoor) {
                Random rand = new Random();
                // La porte se trouve elle sur les rangées horizontales ou verticales ?
                // Après, on choisit aléatoirement, de façon à ce que la porte ne soit pas dans un coin non plus
                if (rand.nextInt(2) == 0)
                    room.addStatic(new Door(room),
                            room.exit.x = rand.nextInt(2) == 0 ? 0 : SIZE_X - 1,
                            room.exit.y = 1 + rand.nextInt(SIZE_Y - 2));
                else
                    room.addStatic(new Door(room),
                            room.exit.x = 1 + rand.nextInt(SIZE_X - 2),
                            room.exit.y = rand.nextInt(2) == 0 ? 0 : SIZE_Y - 1);

                // Puis on s'assure que la case à côté de la porte est accessible
                Position ns = game.gen.getSlotNextToDoor(room.exit);
                if (!(room.getStatic(ns.x, ns.y) instanceof NormalSlot))
                    room.addStatic(new NormalSlot(room), ns.x, ns.y);
            }
        }

        public class BaseEvents extends Events{
            @Override
            public void playerChangesRoom(Player player, Room previous, Room next) {
                player.inventory.removeAllOf(WaterCap.class);
                player.inventory.add(WaterCap.class, WCAP_COUNT);
            }
        }
    }

}