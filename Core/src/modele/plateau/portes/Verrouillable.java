package modele.plateau.portes;

import modele.plateau.inventaire.Cle;

public interface Verrouillable {
    public boolean isVerrouille();

    public void deverouiller(Cle cle);
}
