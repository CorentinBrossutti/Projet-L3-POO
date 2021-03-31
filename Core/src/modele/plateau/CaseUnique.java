package modele.plateau;

public class CaseUnique extends EntiteStatique {
    public CaseUnique(Jeu _jeu) {
        super(_jeu);
        this.vie = true;
    }
    // getter pour savoir si une case a été utilisée, si utilisée -> return true, sinon return false
    public boolean isUsed(){
        if(!this.vie)
            return true;
        else
            return false;
    }
    // TODO : Gestion capsule - Passage inventaire en paramètre
    @Override
    public boolean traversable(){
        if(this.isUsed())
            return false;
        else
            return true;
    }

    private void use(){
        this.vie = false;
    }

    private boolean vie;
}
