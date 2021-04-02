package model.board.statics;

import model.board.items.Key;

/**
 * Interface pour les entités statiques verrouillables
 */
public interface Lockable {
    public boolean isLocked();

    public void unlock(Key key);
}
