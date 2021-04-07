package base;

import base.model.board.items.WaterCap;
import meta.Plugin;
import meta.events.Events;
import model.Player;

import java.util.function.Function;

public class EventsBase extends Events {
    public EventsBase(Plugin plugin) {
        super(plugin);

        registerEvent(
                Events.PLAYER_CHANGES_ROOM,
                (Function<Object[], Void>) objects -> {
                    Player p = (Player) objects[0];

                    // On réinitialise les capsules d'eau du joueur
                    p.inventory.removeAllOf(WaterCap.class);
                    p.inventory.add(WaterCap.class, PluginBase.WCAP_COUNT);

                    return null;
                }
        );
    }
}
