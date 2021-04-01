package modele.plateau.entites;

import modele.plateau.EntiteStatique;
import modele.plateau.Heros;
import modele.plateau.Salle;

public class Empty extends EntiteStatique {
    public Empty(Salle _salle) {
        super(_salle);
    }

    @Override
    public boolean traversable(Heros character) {
        return false;
    }
}
