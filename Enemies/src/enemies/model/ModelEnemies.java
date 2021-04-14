package enemies.model;

import meta.Plugin;

public class ModelEnemies extends Plugin.DummyModel {
    public static final String ENEMY_DEATH_SOURCE = "attack";
    public static final short ENEMY_ODDS = 100;

    public ModelEnemies(Plugin plugin) {
        super(plugin);
    }
}
