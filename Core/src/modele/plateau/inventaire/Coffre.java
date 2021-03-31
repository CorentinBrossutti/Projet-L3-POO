package modele.plateau.inventaire;

public class Coffre extends Objet{
    private Inventaire inventaire = new Inventaire();

    public Inventaire getInventaire(){
        return inventaire;
    }
}
