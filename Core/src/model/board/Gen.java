package model.board;

import model.board.items.Item;
import model.board.items.WeightedItemSupplier;
import model.board.statics.StaticEntity;
import model.board.statics.WeightedEntitiesSupplier;
import util.Position;
import util.Util;

import java.util.List;
import java.util.Random;

/**
 * Classe utilitaire de génération aléatoire pour les entités statiques et les objets.
 * Cette classe ne s'instancie pas et s'utilise de façon statique.
 */
public final class Gen {
    private static final Random rand = new Random();

    public static List<Class<? extends StaticEntity>> entityPicker;
    public static List<Class<? extends Item>> itemPicker;

    static {
        entityPicker = new WeightedEntitiesSupplier().supply();
        itemPicker = new WeightedItemSupplier().supply();
    }

    /**
     *
     * @param room Salle parente de l'entité statique
     * @return Une entité statique aléatoire, avec les poids pris en compte
     */
    public static StaticEntity pickStatic(Room room) {
        return Util.Reflections.instantiate(entityPicker.get(rand.nextInt(entityPicker.size())), room);
    }

    /**
     *
     * @return Un objet aléatoire, avec les poids pris en compte
     */
    public static Item pickItem() {
        return Util.Reflections.instantiate(itemPicker.get(rand.nextInt(itemPicker.size())));
    }

    /**
     * Utile pour savoir où faire apparaître le joueur dans la salle suivante.
     * @param doorPos Position de la porte
     * @return Retourne la position d'entrée / sortie de la porte
     */
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
