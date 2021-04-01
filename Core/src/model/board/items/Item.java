package model.board.items;

import java.util.UUID;

public abstract class Item {
    private UUID itemId = UUID.randomUUID();

    @Override
    public int hashCode() {
        return itemId.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Item && itemId.equals(((Item)o).itemId);
    }
}
