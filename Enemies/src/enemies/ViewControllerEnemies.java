package enemies;

import meta.Plugin;
import meta.ViewController;
import util.Util;
import view.ViewControllerHandle;

public class ViewControllerEnemies extends ViewController {

    public ViewControllerEnemies(Plugin plugin, ViewControllerHandle handle) {
        super(plugin, handle);
    }

    @Override
    public void initGraphics() {
        ((PluginEnemies)plugin).enemy = Util.Images.loadIconResource("/img/ghost.png", PluginEnemies.class);
    }
}
