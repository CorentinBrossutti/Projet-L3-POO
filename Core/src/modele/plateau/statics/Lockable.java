package modele.plateau.statics;

import modele.plateau.items.Key;

public interface Lockable {
    public boolean isLocked();

    public void unlock(Key key);
}
