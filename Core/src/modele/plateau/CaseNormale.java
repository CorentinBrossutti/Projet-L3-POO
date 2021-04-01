package modele.plateau;

import modele.plateau.inventaire.Objet;
import modele.plateau.pickup.NoItem;
import modele.plateau.pickup.Ramassable;

public class CaseNormale extends EntiteStatique {
    public Objet item;

    public CaseNormale(Salle _salle) { super(_salle); }

    @Override
    public boolean traversable(Heros character) {
        if(item instanceof Ramassable) {
            ((Ramassable) item).ramasser(character);
            item = new NoItem();
        }

        return true;
    }

}
