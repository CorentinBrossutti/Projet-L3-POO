package model.events;

import meta.events.PlayerEvent;
import model.Character;
import model.Player;
import util.Position;

public class PlayerMoves extends PlayerEvent {
    public final Position from, to;
    public final Character.Orientation direction;

    public PlayerMoves(Player player, Position from, Position to, Character.Orientation direction) {
        super(player);
        this.from = from;
        this.to = to;
        this.direction = direction;
    }

    @Override
    public boolean cancellable() {
        return true;
    }
}
