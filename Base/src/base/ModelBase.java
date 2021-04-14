package base;

import base.model.board.items.Chest;
import base.model.board.items.Key;
import base.model.board.items.WaterCap;
import base.model.board.statics.*;
import meta.Model;
import meta.Plugin;
import model.Room;
import model.board.items.Item;
import model.board.items.NoItem;
import model.board.statics.StaticEntity;
import util.Position;
import util.WeightedRandomSupplier;

import java.util.Map;
import java.util.Random;

import static model.Room.SIZE_X;
import static model.Room.SIZE_Y;

public class ModelBase extends Model {
    public static final byte LOCKED_ODDS = 2;
    public static final String
            HOLE_DEATH_SOURCE = "falling",
            SU_SLOT_DEATH_SOURCE = "burning";

    public ModelBase(PluginBase pluginBase, Plugin plugin) {
        super(plugin);

        itemSupplier = new WeightedRandomSupplier<>() {
            @Override
            public Map<Class<? extends Item>, Integer> supplyWeights() {
                return Map.of(
                        NoItem.class, 200,
                        WaterCap.class, 2,
                        Key.class, 1,
                        Chest.class, 2
                );
            }
        };

        staticSupplier = new WeightedRandomSupplier<>() {
            @Override
            public Map<Class<? extends StaticEntity>, Integer> supplyWeights() {
                return Map.of(
                        NormalSlot.class, 6,
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
        for (short x = 0; x < SIZE_X; x++) {
            room.addStatic(new Wall(room), x, 0);
            room.addStatic(new Wall(room), x, SIZE_Y - 1);
        }

        // Murs extérieurs verticaux
        for (short y = 1; y < SIZE_Y; y++) {
            room.addStatic(new Wall(room), 0, y);
            room.addStatic(new Wall(room), SIZE_X - 1, y);
        }

        // Placement aléatoire d'entités statiques
        StaticEntity temp;
        boolean roomHasKey = false;
        for (short x = 1; x < SIZE_X - 1; x++) {
            for (short y = 1; y < SIZE_Y - 1; y++) {
                // Si on traite actuellement la case de départ, on l'ignore (traitée plus haut)
                if (room.start.x == x && room.start.y == y)
                    continue;

                // On ajoute une entité statique aléatoire, voir classe "Gen", à la position actuelle
                room.addStatic((temp = plugin.game.gen.pickStatic(room)), x, y);
                // Si la case est normale
                if (temp instanceof NormalSlot) {
                    // Si la position de départ n'est pas encore valide, alors c'est celle-ci (première case valide)
                    if (room.start.x < 0 || room.start.y < 0) {
                        room.start.x = x;
                        room.start.y = y;
                    } else {
                        // On y ajoute un objet aléatoire (ou pas, si la fonction de sélection retourne NoItem)
                        Item i = plugin.game.gen.pickItem();
                        ((NormalSlot) temp).item = i;
                        if (i instanceof Key)
                            roomHasKey = true;
                    }
                }
            }
        }

        // Si on souhaite ajouter une porte
        if (!noDoor) {
            Random rand = new Random();
            // La porte se trouve elle sur les rangées horizontales ou verticales ?
            // Après, on choisit aléatoirement, de façon à ce que la porte ne soit pas dans un coin non plus
            Door d = new Door(room);
            if (rand.nextInt(2) == 0)
                room.addStatic(d,
                        room.exit.x = rand.nextInt(2) == 0 ? 0 : SIZE_X - 1,
                        room.exit.y = 1 + rand.nextInt(SIZE_Y - 2));
            else
                room.addStatic(d,
                        room.exit.x = 1 + rand.nextInt(SIZE_X - 2),
                        room.exit.y = rand.nextInt(2) == 0 ? 0 : SIZE_Y - 1);

            // Puis on s'assure que la case à côté de la porte est accessible
            Position ns = plugin.game.gen.getInnerCellPosNextToDoor(room.exit);
            if (!(room.getStatic(ns.x, ns.y) instanceof NormalSlot))
                room.addStatic(new NormalSlot(room), ns.x, ns.y);

            if (roomHasKey && plugin.game.gen.should(LOCKED_ODDS))
                d.mark(false);
        }
    }

}
