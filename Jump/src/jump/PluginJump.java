package jump;

import meta.Plugin;
import model.Game;
import model.Player;
import model.CharacterController;
import view.ViewControllerHandle;

public class PluginJump extends Plugin {
    public PluginJump(Game game, ViewControllerHandle handle) {
        super(game, handle, "jump");

        this.model = new ModelJump();
        this.viewController = new JumpViewController(handle, game);
    }

    public class ModelJump extends DummyModel{
        @Override
        public CharacterController customController(Player player) {
            return new PlayerControllerJump(player);
        }
    }
}
