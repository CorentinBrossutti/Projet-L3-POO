package modele.plateau;

public class CaseUnique extends EntiteStatique {
    public CaseUnique(Jeu _jeu) {
        super(_jeu);
        this.used = false; // La case n'est pas encore traversée
    }
    // getter pour savoir si une case a été utilisée, si utilisée -> return true, sinon return false
    public boolean isUsed(){
        if(this.used)
            return true;
        else
            return false;
    }
    // TODO : Gestion capsule - Passage inventaire en paramètre
    @Override
    public boolean traversable(){
        if(this.isUsed()) // Si la case a déjà été traversée -> elle n'est pas traversable
            return false;
        else
            return true;
    }

    private void use(){
        this.used = true;
    }

    private boolean used;
}
