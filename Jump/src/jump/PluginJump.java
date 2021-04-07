package jump;

import meta.Plugin;
import model.Game;
import model.Player;
import view.ViewControllerHandle;

public class PluginJump extends Plugin {
    public PluginJump(Game game, ViewControllerHandle handle) {
        super(game, handle, "jump");

        this.model = new ModelJump(this);
        this.viewController = new JumpViewController(this, handle, game);
    }

    public class ModelJump extends DummyModel{

        public ModelJump(Plugin plugin) {
            super(plugin);
        }

        @Override
        public Player.PlayerController customPlayerController(Player player) {
            return new PlayerControllerJump(player);
        }
    }
}
