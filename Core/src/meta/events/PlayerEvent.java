package meta.events;

import model.Player;

public abstract class PlayerEvent extends Event{
    public final Player player;

    public PlayerEvent(Player player) {
        this.player = player;
    }
}
