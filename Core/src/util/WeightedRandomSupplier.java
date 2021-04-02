package util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Classe permettant une génération aléatoire basée sur des poids relatifs
 * @param <T> Générique, type (parent) à retour à la génération
 */
public abstract class WeightedRandomSupplier<T> {
    /**
     * Permet une randomisation avec des poids (certains éléments ont plus de chance d'être choisis que d'autre).
     * Les poids sont toujours relatifs, parmi deux éléments, si l'un a un poids de 50 et l'autre un poids de 5,
     *  le premier a 90% de chance d'être choisi, et le deuxième 10%
     * @return Une map contenant en clé les différents éléments, et en valeur leur poids
     */
    public abstract Map<Class<? extends T>, Integer> supplyWeights();

    /**
     * Une telle liste à poids permet de choisir un élément en utilisant {@link java.util.List#get(int)} avec un indice
     *  aléatoire, tout en maintenant le concept de poids relatifs
     * @return Une liste des éléments avec les différents poids pris en compte
     */
    public List<Class<? extends T>> supply() {
        List<Class<? extends T>> list = new ArrayList<>();
        // Globalement, on ajoute dans la liste chaque élément autant de fois que son poids le demande
        for (Map.Entry<Class<? extends T>, Integer> entry : supplyWeights().entrySet()) {
            for (int i = 0; i < entry.getValue(); i++)
                list.add(entry.getKey());
        }

        return list;
    }

    /**
     * @return Les éléments de base, une seule fois par élément, poids ignorés
     */
    public Set<Class<? extends T>> baseList() {
        return supplyWeights().keySet();
    }

    /**
     * @return Le nombre d'éléments distincts dans la génération
     */
    public int count() {
        return supplyWeights().keySet().size();
    }
}
