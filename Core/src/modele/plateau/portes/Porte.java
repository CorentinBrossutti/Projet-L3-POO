package modele.plateau.portes;

import modele.plateau.inventaire.Cle;

public class Porte implements Verrouillable{
    protected boolean verrouillee;

    @Override
    public boolean isVerrouille() {
        return verrouillee;
    }

    @Override
    public void deverouiller(Cle cle) {
        verrouillee = false;
    }

    public Porte(){

    }
}
