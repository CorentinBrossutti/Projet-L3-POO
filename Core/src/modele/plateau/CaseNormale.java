package modele.plateau;

public class CaseNormale extends EntiteStatique {
    public CaseNormale(Salle _salle) { super(_salle); }

    @Override
    public boolean traversable() {
        return true;
    }

}
