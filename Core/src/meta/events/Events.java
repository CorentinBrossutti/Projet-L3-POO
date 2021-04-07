package meta.events;

import meta.Plugin;
import meta.PluginDataClass;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Gestion des événements
 */
public abstract class Events extends PluginDataClass {
    public static final String
            PLAYER_CHANGES_ROOM = "player.changes_room",
            PLAYER_MOVES = "player.moves",
            PLAYER_USES = "player.uses";

    private final Map<String, Function<Object[], ?>> events = new HashMap<>();

    public Events(Plugin plugin) {
        super(plugin);
    }

    /**
     * Appelle un événement
     *
     * @param event La clé de l'événement, généralement statique dans les fichiers de l'extension qui l'enregistre
     * @param args  Les objets à passer pour l'appel
     * @param <T>   Générique
     * @return Le résultat de l'événement, s'il en a un
     */
    public <T> T call(String event, Object... args) {

        return events.containsKey(event) ? (T) events.get(event).apply(args) : null;
    }

    /**
     * Enregistre un événément à traiter
     *
     * @param event La clé de l'événement
     * @param func  La fonction utilisée lorsque l'événement est appelé
     */
    public void registerEvent(String event, Function<Object[], ?> func) {
        events.put(event, func);
    }

    /**
     * Appelle un événement retournant un booléen sur une liste de plugins.
     * @param plugins Liste des plugins
     * @param event Clé de l'événement
     * @param ms Most Significant boolean : le booléen dont, s'il est retourné par au moins un événement, définira le retour.
     *           Utile par exemple pour les événements avec annulation, où un seul event retournant annulation définit l'annulation globale.
     * @param args Arguments de l'événement
     * @return !ms si aucun événement ne retourne ms, sinon ms
     */
    public static boolean callBool(Collection<Plugin> plugins, String event, boolean ms, Object... args) {
        boolean b = !ms;
        for (Plugin p : plugins){
            Object ret = p.events.call(event, args);
            if(ret instanceof Boolean && (boolean)ret == ms)
                b = ms;
        }

        return b;
    }

    /**
     * Appelle un événement ne retournant rien sur une liste de plugins.
     * @param plugins Liste des plugins
     * @param event Clé de l'événement
     * @param args Arguments de l'événement
     */
    public static void callVoid(Collection<Plugin> plugins, String event, Object... args){
        plugins.forEach(
                plugin -> plugin.events.call(event, args)
        );
    }

}
