package model.board.statics;

/**
 * Interface pour les entités statiques verrouillables / à usage limité
 */
public interface Usable {
    /**
     *
     * @return Vrai si la case est utilisable
     */
    boolean usable();
    /**
     * Marque l'entité comme utilisable (ou non)
     * @param usable Vrai si utilisable, non sinon
     */
    void mark(boolean usable);
}
