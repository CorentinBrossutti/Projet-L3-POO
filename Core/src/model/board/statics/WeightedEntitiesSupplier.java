package model.board.statics;

import util.WeightedRandomSupplier;

import java.util.Map;

/**
 * Une classe permettant la génération aléatoire avec poids d'entités statiques
 * @see util.WeightedRandomSupplier Pour des informations générales
 */
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
