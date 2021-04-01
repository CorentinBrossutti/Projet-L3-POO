package modele.plateau.entites;

import modele.plateau.EntiteStatique;
import modele.plateau.Salle;
import modele.plateau.inventaire.Cle;

public class Porte extends EntiteStatique implements Verrouillable{
    protected boolean verrouillee;

    public Porte(Salle _salle) {
        super(_salle);
    }

    @Override
    public boolean isVerrouille() {
        return verrouillee;
    }

    @Override
    public void deverouiller(Cle cle) {
        verrouillee = false;
    }

    @Override
    public boolean traversable() {
        if(verrouillee)
            return false;
        salle.terminate();
        return false;
    }
}
