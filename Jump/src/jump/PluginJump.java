package jump;

import meta.Plugin;
import model.Game;
import view.ViewControllerHandle;

public class PluginJump extends Plugin {
    public PluginJump(Game game, ViewControllerHandle handle) {
        super(game, handle, "jump");

        this.model = new ModelJump(this);
        this.viewController = new ViewControllerJump(this, handle);
    }

}
