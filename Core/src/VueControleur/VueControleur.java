package VueControleur;

import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;


import modele.plateau.*;
import modele.plateau.entites.Empty;
import modele.plateau.entites.Porte;
import modele.plateau.inventaire.Capsule;
import modele.plateau.inventaire.Cle;
import modele.plateau.inventaire.Coffre;
import modele.plateau.inventaire.Objet;
import modele.plateau.pickup.NoItem;


/** Cette classe a deux fonctions :
 *  (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 *  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle (flèches direction, etc.))
 *
 */
public class VueControleur extends JFrame implements Observer {
    private Jeu jeu; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)

    private int sizeX; // taille de la grille affichée
    private int sizeY;

    // icones affichées dans la grille
    private RotatableImageIcon icoHero;
    private RotatableImageIcon icoCaseNormale;
    private RotatableImageIcon icoMur;
    private RotatableImageIcon icoColonne;
    private RotatableImageIcon wall;
    private RotatableImageIcon bottle;
    private RotatableImageIcon key;
    private RotatableImageIcon chest;
    private RotatableImageIcon door;

    private JLabel[][] tabJLabel; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône, suivant ce qui est présent dans le modèle)


    public VueControleur(Jeu _jeu) {
        sizeX = Salle.SIZE_X;
        sizeY = Salle.SIZE_Y;
        jeu = _jeu;

        chargerLesIcones();
        placerLesComposantsGraphiques();
        ajouterEcouteurClavier();
    }

    private void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {  // on regarde quelle touche a été pressée
                    case KeyEvent.VK_LEFT : jeu.getHeros().gauche(); break;
                    case KeyEvent.VK_RIGHT : jeu.getHeros().droite();break;
                    case KeyEvent.VK_DOWN : jeu.getHeros().bas(); break;
                    case KeyEvent.VK_UP : jeu.getHeros().haut(); break;

                }
            }
        });
    }


    private void chargerLesIcones() {
        icoHero = chargerIcone("/img/Pacman.png");
        icoCaseNormale = chargerIcone("/img/Vide.png");
        icoMur = chargerIcone("/img/Mur.png");
        icoColonne = chargerIcone("/img/Colonne.png");
        wall = chargerIcone("/img/wall.png");
        bottle = chargerIcone("/img/Bouteille.png");
        key = chargerIcone("/img/Cle.png");
        chest = chargerIcone("/img/Coffre.png");
        door = chargerIcone("/img/door.png");
    }

    private RotatableImageIcon chargerIcone(String urlIcone) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(VueControleur.class.getResourceAsStream(urlIcone));
        } catch (IOException ex) {
            Logger.getLogger(VueControleur.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return new RotatableImageIcon(image);
    }

    private void placerLesComposantsGraphiques() {
        setTitle("Roguelike");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre

        JComponent grilleJLabels = new JPanel(new GridLayout(sizeY, sizeX)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille

        tabJLabel = new JLabel[sizeX][sizeY];

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                JLabel jlab = new JLabel();
                tabJLabel[x][y] = jlab; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )
                grilleJLabels.add(jlab);
            }
        }
        add(grilleJLabels);
    }

    
    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabJLabel)
     */
    private void mettreAJourAffichage() {

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
				EntiteStatique e = jeu.currentSalle().getEntite(x, y);
                if (e instanceof Mur) {
                    tabJLabel[x][y].setIcon(wall);
                } else if (e instanceof CaseNormale) {
                    CaseNormale cn = (CaseNormale) e;
                    tabJLabel[x][y].setIcon(cn.item instanceof NoItem ? icoCaseNormale : getItemIcon(cn.item));
                } else if (e instanceof Porte) {
                    tabJLabel[x][y].setIcon(door);
                } else if (e instanceof Empty) {
                    tabJLabel[x][y].setIcon(icoMur);
                }
            }
        }


        icoHero.rotate(jeu.getHeros().getRotation());
        tabJLabel[jeu.getHeros().getX()][jeu.getHeros().getY()].setIcon(icoHero);
    }

    @Override
    public void update(Observable o, Object arg) {
        mettreAJourAffichage();
        /*
        SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mettreAJourAffichage();
                    }
                }); 
        */

    }

    private ImageIcon getItemIcon(Objet item){
        if(item instanceof Capsule)
            return bottle;
        else if(item instanceof Cle)
            return key;
        else if(item instanceof Coffre)
            return chest;
        return null;
    }
}
