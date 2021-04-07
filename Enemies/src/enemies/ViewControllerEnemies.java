package enemies;

import meta.Plugin;
import meta.ViewController;
import model.Game;
import util.Util;
import view.RotatableImageIcon;
import view.ViewControllerHandle;

public class ViewControllerEnemies extends ViewController {

    public ViewControllerEnemies(Plugin plugin, ViewControllerHandle handle, Game game) {
        super(plugin, handle, game);
    }

    @Override
    public void initGraphics() {
        ((PluginEnemies)plugin).enemy = Util.Images.loadIconResource("/img/ghost.png", PluginEnemies.class);
    }
}
