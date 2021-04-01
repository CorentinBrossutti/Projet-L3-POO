package modele.plateau.entites;

import modele.plateau.CaseNormale;
import modele.plateau.EntiteStatique;
import modele.plateau.Mur;
import util.WeightedRandomSupplier;

import java.util.Map;

public final class WeightedEntitiesSupplier extends WeightedRandomSupplier<EntiteStatique> {
    @Override
    public Map<Class<? extends EntiteStatique>, Integer> supplyWeights() {
        return Map.of(
          CaseNormale.class, 40,
          Mur.class, 1,
          Empty.class, 2
        );
    }
}
