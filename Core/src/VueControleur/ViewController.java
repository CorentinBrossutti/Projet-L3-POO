package VueControleur;

import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;


import modele.plateau.*;
import modele.plateau.statics.*;
import modele.plateau.items.WaterCap;
import modele.plateau.items.Key;
import modele.plateau.items.Chest;
import modele.plateau.items.Item;
import modele.plateau.items.NoItem;


/** Cette classe a deux fonctions :
 *  (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 *  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle (flèches direction, etc.))
 *
 */
public class ViewController extends JFrame implements Observer {
    private Game game; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)

    private int sizeX = Room.SIZE_X; // taille de la grille affichée
    private int sizeY = Room.SIZE_Y;

    // icones affichées dans la grille
    private RotatableImageIcon
            player,
            normalSlot,
            wall,
            bottle,
            key,
            chest,
            door,
            hole,
            singleUse,
            fire;

    private JLabel[][] viewGrid; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône, suivant ce qui est présent dans le modèle)


    public ViewController(Game game) {
        this.game = game;

        loadIcons();
        initGraphics();
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyCode()) {  // on regarde quelle touche a été pressée
                    case KeyEvent.VK_LEFT: game.getPlayer().getController().move(Player.Orientation.LEFT); break;
                    case KeyEvent.VK_RIGHT: game.getPlayer().getController().move(Player.Orientation.RIGHT);break;
                    case KeyEvent.VK_DOWN: game.getPlayer().getController().move(Player.Orientation.DOWN); break;
                    case KeyEvent.VK_UP: game.getPlayer().getController().move(Player.Orientation.UP); break;
                    case KeyEvent.VK_C: game.getPlayer().getController().use(WaterCap.class); break;
                }
            }
        });
    }

    private void loadIcons() {
        player = loadIconResource("/img/pacman.png");
        normalSlot = loadIconResource("/img/normal.png");
        wall = loadIconResource("/img/wall.png");
        bottle = loadIconResource("/img/wbottle.png");
        key = loadIconResource("/img/key.png");
        chest = loadIconResource("/img/chest.png");
        door = loadIconResource("/img/door.png");
        hole = loadIconResource("/img/void.png");
        singleUse = loadIconResource("/img/column.png");
        fire = loadIconResource("/img/fire.png");
    }

    private RotatableImageIcon loadIconResource(String url) {
        BufferedImage image = null;

        try {
            image = ImageIO.read(ViewController.class.getResourceAsStream(url));
        } catch (IOException ex) {
            Logger.getLogger(ViewController.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return new RotatableImageIcon(image);
    }

    private void initGraphics() {
        setTitle("Roguelike");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre

        JComponent tempGrid = new JPanel(new GridLayout(sizeY, sizeX)); // tempGrid va contenir les cases graphiques et les positionner sous la forme d'une grille

        viewGrid = new JLabel[sizeX][sizeY];

        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                JLabel jl = new JLabel();
                viewGrid[x][y] = jl; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )
                tempGrid.add(jl);
            }
        }
        add(tempGrid);
    }

    
    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabJLabel)
     */
    private void updateDisplay() {

        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {
				StaticEntity e = game.currentRoom().getStatic(x, y);
                if (e instanceof Wall) {
                    viewGrid[x][y].setIcon(wall);
                } else if(e instanceof SingleUsageSlot) {
                    viewGrid[x][y].setIcon(((SingleUsageSlot) e).isUsed() ? fire : singleUse);
                } else if (e instanceof NormalSlot) {
                    NormalSlot cn = (NormalSlot) e;
                    viewGrid[x][y].setIcon(cn.item instanceof NoItem ? normalSlot : getItemIcon(cn.item));
                } else if (e instanceof Door) {
                    viewGrid[x][y].setIcon(door);
                } else if (e instanceof Hole) {
                    viewGrid[x][y].setIcon(hole);
                }
            }
        }


        player.rotate(game.getPlayer().getOrientation().getRadians());
        viewGrid[game.getPlayer().getPosition().x][game.getPlayer().getPosition().y].setIcon(player);
    }

    @Override
    public void update(Observable o, Object arg) {
        updateDisplay();
        /*
        SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mettreAJourAffichage();
                    }
                }); 
        */

    }

    private ImageIcon getItemIcon(Item item){
        if(item instanceof WaterCap)
            return bottle;
        else if(item instanceof Key)
            return key;
        else if(item instanceof Chest)
            return chest;
        return null;
    }
}
