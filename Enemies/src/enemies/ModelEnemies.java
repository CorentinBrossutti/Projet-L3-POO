package enemies;

import meta.Plugin;
import model.Room;

public class ModelEnemies extends Plugin.DummyModel {
    public static final short ENEMY_ODDS = 200;

    public ModelEnemies(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void generateRoom(Room room, boolean noDoor) {
        room.forEachSlot(
                (x, y) -> {
                    if(plugin.game.gen.should(ENEMY_ODDS))
                        ((PluginEnemies)plugin).registerEnemy(x, y);
                }
        );
    }
}
