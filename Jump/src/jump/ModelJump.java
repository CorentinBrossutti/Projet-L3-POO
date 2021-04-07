package jump;

import meta.Plugin;
import model.Player;

public class ModelJump extends Plugin.DummyModel {

    public ModelJump(Plugin plugin) {
        super(plugin);
    }

    @Override
    public Player.PlayerController customPlayerController(Player player) {
        return new PlayerControllerJump(player);
    }
}
