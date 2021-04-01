/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

import modele.plateau.inventaire.Capsule;

import java.util.Observable;

public class Jeu extends Observable implements Runnable {
    private static int PAUSE = 100; // période de rafraichissement
    private static int NB_SALLES = 3;

    private Heros heros;
    private Salle[] salles = new Salle[NB_SALLES];
    private int currentSalle;


    public Heros getHeros() {
        return heros;
    }

    public Jeu() {
        for (int i = 0; i < NB_SALLES; i++){
            int[] spos = Salle.Gen.getSlotNextToDoor(
                    i == 0 ? -1 : salles[i - 1].getExitX(),
                    i == 0 ? -1 : salles[i - 1].getExitY()
            );
            salles[i] = new Salle(i == NB_SALLES - 1, spos[0], spos[1]);
        }
        heros = new Heros(this, salles[0].getStartX(), salles[0].getStartY());
    }

    public Salle currentSalle(){
        return salles[currentSalle];
    }

    public void start() {
        new Thread(this).start();
    }

    public void run() {

        while(true) {
            setChanged();
            notifyObservers();
            if(currentSalle().isDone()){
                currentSalle++;
                heros.setPosition(currentSalle().getStartX(), currentSalle().getStartY());
                heros.getInventaire().removeType(Capsule.class);
            }

            try {
                Thread.sleep(PAUSE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
