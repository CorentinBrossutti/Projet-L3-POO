package enemies;

import base.model.board.statics.NormalSlot;
import enemies.model.ModelEnemies;
import meta.Plugin;
import meta.events.Events;
import model.Room;

import java.util.function.Function;

public class EventsEnemies extends Events {
    public EventsEnemies(Plugin plugin) {
        super(plugin);

        registerEvent(
                Events.PLAYER_CHANGES_ROOM,
                (Function<Object[], Void>) objects -> {
                    Room room = (Room) objects[2];

                    room.forEachSlot(
                            (x, y) -> {
                                if(room.getStatic(x, y) instanceof NormalSlot && plugin.game.gen.should(ModelEnemies.ENEMY_ODDS))
                                    ((PluginEnemies) plugin).registerEnemy(x, y);
                            }
                    );

                    return null;
                }
        );
    }
}
