package model.board.statics;

/**
 * Interface pour les entités statiques verrouillables
 */
public interface Lockable {
    boolean isLocked();

    void unlock();
    void lock();
}
