/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

import modele.plateau.inventaire.Inventaire;

/**
 * Héros du jeu
 */
public class Heros {
    private int x;
    private int y;
    private double rotation;

    private Jeu jeu;
    private Inventaire inventaire;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Inventaire getInventaire(){
        return inventaire;
    }

    public double getRotation(){
        return rotation;
    }

    public void setRotationDeg(double degrees){
        rotation = Math.toRadians(degrees);
    }

    public Heros(Jeu _jeu, int _x, int _y) {
        jeu = _jeu;
        x = _x;
        y = _y;

        inventaire = new Inventaire();
    }

    public void droite() {
        if (traversable(x+1, y)) {
            x ++;
            setRotationDeg(0);
        }
    }

    public void gauche() {
        if (traversable(x-1, y)) {
            x --;
            setRotationDeg(180);
        }
    }

    public void bas() {
        if (traversable(x, y+1)) {
            y ++;
            setRotationDeg(90);
        }
    }

    public void haut() {
        if (traversable(x, y-1)) {
            y --;
            setRotationDeg(270);
        }
    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    private boolean traversable(int x, int y) {

        if (x > 0 && x < Salle.SIZE_X && y > 0 && y < Salle.SIZE_Y) {
            return jeu.currentSalle().getEntite(x, y).traversable();
        } else {
            return false;
        }
    }
}
