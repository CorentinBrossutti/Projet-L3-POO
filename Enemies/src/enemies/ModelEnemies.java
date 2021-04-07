package enemies;

import base.model.board.statics.NormalSlot;
import meta.Plugin;
import model.Room;

public class ModelEnemies extends Plugin.DummyModel {
    public static final short ENEMY_ODDS = 150;

    public ModelEnemies(Plugin plugin) {
        super(plugin);
    }

    @Override
    public void generateRoom(Room room, boolean noDoor) {
        room.forEachSlot(
                (x, y) -> {
                    if(room.getStatic(x, y) instanceof NormalSlot && plugin.game.gen.should(ENEMY_ODDS))
                        ((PluginEnemies) plugin).registerEnemy(x, y);
                }
        );
    }
}
