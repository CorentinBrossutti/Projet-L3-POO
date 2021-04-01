package modele.plateau;

/**
 * Ne bouge pas (murs...)
 */
public abstract class EntiteStatique {
    protected Salle salle;

    public EntiteStatique(Salle _salle) {
        this.salle = _salle;
    }

    public abstract boolean traversable(Heros character);

}