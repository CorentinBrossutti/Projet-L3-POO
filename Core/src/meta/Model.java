package meta;

import model.Player;
import model.CharacterController;
import model.Room;
import model.board.items.Item;
import model.board.statics.StaticEntity;
import util.WeightedRandomSupplier;

/**
 * La classe Model contient les objets et méthodes liés à la gestion du modèle par le plugin
 */
public abstract class Model extends PluginDataClass{
    /**
     * Sera additionné avec les autres fournisseurs (!)
     */
    public WeightedRandomSupplier<Item> itemSupplier;
    /**
     * Sera additionné avec les autres fournisseurs (!)
     */
    public WeightedRandomSupplier<StaticEntity> staticSupplier;

    public Model(Plugin plugin) {
        super(plugin);
    }

    /**
     * Appelée sur chaque plugin lors de la génération d'une salle
     *
     * @param room   L'objet à salle à remplir
     * @param noDoor Vrai si la salle ne doit pas contenir de portes
     */
    public void generateRoom(Room room, boolean noDoor) {
    }

    /**
     * Les contrôleurs personnalisés sont accessibles en utilisant {@link Player#getController(String)}.
     * Le nom du plugin est la référence d'accès, par défaut.
     *
     * @param player Le joueur
     * @return Un contrôleur personnalisé pour ce plugin, qui sera attaché au joueur
     */
    public CharacterController customController(Player player) {
        return null;
    }

}
