package modele.plateau.inventaire;

import java.util.ArrayList;

public class Inventaire {
    private ArrayList<Objet> objets = new ArrayList<>();

    public Inventaire(){

    }

    public void ajouter(Objet objet){
        objets.add(objet);
    }

    public void retirer(Objet objet){
        objets.remove(objet);
    }

    public void removeType(Class<? extends Objet> type){
        objets.removeIf(obj -> obj.getClass().equals(type));
    }
}
