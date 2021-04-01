package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class WeightedRandomSupplier<T> {
    /**
     * Permet une randomisation avec des poids (certains éléments ont plus de chance d'être choisis que d'autre)
     * @return Une map contenant en clé les différents éléments, et en valeur leur poids
     */
    public abstract Map<Class<? extends T>, Integer> supplyWeights();

    /**
     *
     * @return Une liste des éléments avec les différents poids pris en compte
     */
    public List<Class<? extends T>> supply(){
        List<Class<? extends T>> list = new ArrayList<>();
        for(Map.Entry<Class<? extends T>, Integer> entry : supplyWeights().entrySet()){
            for (int i = 0; i < entry.getValue(); i++)
                list.add(entry.getKey());
        }

        return list;
    }

    /**
     *
     * @return Les éléments de base
     */
    public Set<Class<? extends T>> baseList(){
        return supplyWeights().keySet();
    }

    /**
     *
     * @return Le nombre d'éléments distincts
     */
    public int count(){
        return supplyWeights().keySet().size();
    }
}
