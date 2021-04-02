package model.board.items;

import util.WeightedRandomSupplier;

import java.util.Map;

/**
 * Classe permettant d'obtenir une génération aléatoire relative aux objets avec des poids différents
 * @see util.WeightedRandomSupplier Classe générique pour la génération aléatoire avec poids
 */
public final class WeightedItemSupplier extends WeightedRandomSupplier<Item> {

    @Override
    public Map<Class<? extends Item>, Integer> supplyWeights() {
        return Map.of(
                NoItem.class, 100,
                WaterCap.class, 2,
                Key.class, 1,
                Chest.class, 2
        );
    }
}
