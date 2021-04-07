package model.events;

import meta.events.PlayerEvent;
import model.Player;
import model.board.items.Item;
import model.board.statics.StaticEntity;

public class PlayerUses extends PlayerEvent {
    public final Item item;
    public final StaticEntity target;

    public PlayerUses(Player player, Item item, StaticEntity target) {
        super(player);
        this.item = item;
        this.target = target;
    }

    @Override
    public boolean cancellable() {
        return true;
    }
}
