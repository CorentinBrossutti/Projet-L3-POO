package modele.plateau;

import modele.plateau.items.Item;
import modele.plateau.items.WeightedItemSupplier;
import modele.plateau.statics.StaticEntity;
import modele.plateau.statics.WeightedEntitiesSupplier;
import util.Position;
import util.Util;

import java.util.List;
import java.util.Random;

public final class Gen {
    private static final Random rand = new Random();

    public static List<Class<? extends StaticEntity>> entityPicker;
    public static List<Class<? extends Item>> itemPicker;

    static {
        entityPicker = new WeightedEntitiesSupplier().supply();
        itemPicker = new WeightedItemSupplier().supply();
    }

    public static StaticEntity pickEntity(Room room) {
        return Util.Reflections.instantiate(entityPicker.get(rand.nextInt(entityPicker.size())), room);
    }

    public static Item pickItem() {
        return Util.Reflections.instantiate(itemPicker.get(rand.nextInt(itemPicker.size())));
    }

    public static Position getSlotNextToDoor(Position doorPos) {
        switch (doorPos.x) {
            case 0:
                return new Position(1, doorPos.y);
            case Room.SIZE_X - 1:
                return new Position(Room.SIZE_X - 2, doorPos.y);
            default:
                switch (doorPos.y) {
                    case 0:
                        return new Position(doorPos.x, 1);
                    case Room.SIZE_Y - 1:
                        return new Position(doorPos.x, Room.SIZE_Y - 2);
                }
        }
        return doorPos;
    }
}
