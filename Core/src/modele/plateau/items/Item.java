package modele.plateau.items;

import java.util.UUID;

public abstract class Item {
    private UUID itemId = UUID.randomUUID();

    @Override
    public int hashCode() {
        return itemId.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return itemId.equals(((Item)o).itemId);
    }
}
