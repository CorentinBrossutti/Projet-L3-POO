package model.board.items;

import java.util.UUID;

/**
 * Un objet abstrait pouvant être disposé sur des cases
 */
public abstract class Item {
    private final UUID itemId = UUID.randomUUID();

    @Override
    public int hashCode() {
        return itemId.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Item && itemId.equals(((Item) o).itemId);
    }
}
