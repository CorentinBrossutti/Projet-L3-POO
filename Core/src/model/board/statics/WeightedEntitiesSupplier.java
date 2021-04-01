package model.board.statics;

import util.WeightedRandomSupplier;

import java.util.Map;

public final class WeightedEntitiesSupplier extends WeightedRandomSupplier<StaticEntity> {
    @Override
    public Map<Class<? extends StaticEntity>, Integer> supplyWeights() {
        return Map.of(
                NormalSlot.class, 40,
                Wall.class, 1,
                Hole.class, 2,
                SingleUsageSlot.class, 2
        );
    }
}
