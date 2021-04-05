package model;

import model.board.items.Item;
import util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Un inventaire quelconque, pas forcément celui d'un joueur
 */
public class Inventory {
    /**
     * La liste des objets contenus dans l'inventaire
     */
    public final ArrayList<Item> items = new ArrayList<>();

    /**
     *
     * @param type Type, par ex. "WaterCap.class"
     * @return Le nombre d'objets du type demandé contenus dans l'inventaire
     */
    public int count(Class<? extends Item> type) {
        int temp = 0;
        for (Item item : items) {
            if (item.getClass().equals(type))
                temp++;
        }

        return temp;
    }

    /**
     *
     * @param type Type, par ex. "Chest.class"
     * @return Vrai si l'inventaire contient au moins un exemplaire du type, faux sinon
     */
    public boolean has(Class<? extends Item> type) {
        // On utilise "firstOf" plutôt que "count" car "firstOf" s'arrête au premier trouvé (optimisation)
        return firstOf(type) != null;
    }

    /**
     *
     * @param type Le type voulu
     * @param <T> Générique pour la syntaxe et éviter un cast
     * @return Le premier objet trouvé du type demandé si présent, null sinon
     */
    public <T extends Item> T firstOf(Class<T> type) {
        for (Item item : items) {
            if (item.getClass().equals(type))
                return (T) item;
        }

        return null;
    }

    /**
     * Ajoute un objet à l'inventaire
     * @param item Objet à ajouter
     */
    public void add(Item... item) {
        items.addAll(Arrays.asList(item));
    }

    /**
     * Ajoute un objet à l'inventaire
     * @param type Type de l'objet à ajouter, ex "Key.class"
     * @param count Nombre à ajouter
     */
    public void add(Class<? extends Item> type, short count) {
        for (short i = 0; i < count; i++)
            add(Util.Reflections.instantiate(type));
    }

    /**
     * Retire un objet de l'inventaire
     * @param item Objet à retirer
     */
    public void remove(Item item) {
        items.remove(item);
    }

    /**
     * Retire tous les objets d'un type demandé de l'inventaire
     * @param type Type à retirer, par exemple "WaterCap.class"
     */
    public void removeAllOf(Class<? extends Item> type) {
        items.removeIf(obj -> obj.getClass().equals(type));
    }

    /**
     * Retire un seul objet du type demandé de l'inventaire
     * @param type Type à retirer, par ex. "Chest.class"
     */
    public void removeOneOf(Class<? extends Item> type) {
        Iterator<Item> it = items.listIterator();
        while (it.hasNext()) {
            if (it.next().getClass().equals(type)) {
                it.remove();
                return;
            }
        }
    }

    /**
     * Transfère (copie) le contenu de cet inventaire dans un autre
     * @param another Inventaire destination
     */
    public void into(Inventory another) {
        another.items.addAll(items);
    }
}
