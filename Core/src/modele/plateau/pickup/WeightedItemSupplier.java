package modele.plateau.pickup;

import modele.plateau.inventaire.Capsule;
import modele.plateau.inventaire.Cle;
import modele.plateau.inventaire.Coffre;
import modele.plateau.inventaire.Objet;
import util.WeightedRandomSupplier;

import java.util.Map;

public final class WeightedItemSupplier extends WeightedRandomSupplier<Objet> {
    @Override
    public Map<Class<? extends Objet>, Integer> supplyWeights() {
        return Map.of(
                NoItem.class, 100,
                Capsule.class, 2,
                Cle.class, 1,
                Coffre.class, 2
        );
    }
}
