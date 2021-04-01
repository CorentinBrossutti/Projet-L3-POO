package model.board.items;

import util.WeightedRandomSupplier;

import java.util.Map;

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
