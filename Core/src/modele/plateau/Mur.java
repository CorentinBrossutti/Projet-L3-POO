package modele.plateau;

public class Mur extends EntiteStatique {
    public Mur(Salle _salle) { super(_salle); }

    @Override
    public boolean traversable(Heros character) {
        return false;
    }
}
