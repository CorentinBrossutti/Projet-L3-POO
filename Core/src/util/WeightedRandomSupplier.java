package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class WeightedRandomSupplier<T> {
    public abstract Map<Class<? extends T>, Integer> supplyWeights();

    public List<Class<? extends T>> supply(){
        List<Class<? extends T>> list = new ArrayList<>();
        for(Map.Entry<Class<? extends T>, Integer> entry : supplyWeights().entrySet()){
            for (int i = 0; i < entry.getValue(); i++)
                list.add(entry.getKey());
        }

        return list;
    }
}
