package modele.plateau.inventaire;

import modele.plateau.Heros;
import modele.plateau.pickup.Ramassable;

public class Capsule extends Objet implements Ramassable {

    @Override
    public void ramasser(Heros ramasseur) {
        ramasseur.getInventaire().ajouter(this);
    }
}
