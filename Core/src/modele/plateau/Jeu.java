/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

import java.util.Observable;

public class Jeu extends Observable implements Runnable {
    private static int PAUSE = 200; // période de rafraichissement
    private static int NB_SALLES = 2;

    private Heros heros;
    private Salle[] salles = new Salle[NB_SALLES];
    private int currentSalle;


    public Heros getHeros() {
        return heros;
    }

    public Jeu() {
        for (int i = 0; i < NB_SALLES; i++) {
            salles[i] = new Salle();
            salles[i].initialisationDesEntites();
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
            }

            try {
                Thread.sleep(PAUSE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

}
