package modele.plateau.inventaire;

import java.util.Objects;
import java.util.UUID;

public abstract class Objet {
    private UUID objectId = UUID.randomUUID();

    @Override
    public int hashCode() {
        return objectId.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return objectId.equals(((Objet)o).objectId);
    }
}
