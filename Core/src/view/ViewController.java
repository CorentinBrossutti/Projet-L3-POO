package view;

import model.board.Game;
import model.board.Inventory;
import model.board.Player;
import model.board.Room;
import model.board.items.*;
import model.board.statics.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Cette classe a deux fonctions :
 * (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 * (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle (flèches direction, etc.))
 */
public class ViewController extends JFrame implements Observer {
    private static int
            GRID_SIZE_X = Room.SIZE_X,
            GRID_SIZE_Y = Room.SIZE_Y,
            WINDOW_SIZE_X = 620,
            WINDOW_SIZE_Y = 330;

    private Game game; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)

    // icones affichées dans la grille
    private RotatableImageIcon
            player,
            normalSlot,
            wall,
            door,
            hole,
            singleUse,
            fire;

    private JLabel[][] viewGrid; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône, suivant ce qui est présent dans le modèle)

    private JComponent inventoryDisplay;
    private JLabel[][] inventoryGrid;
    private WeightedItemSupplier itemSupplier = new WeightedItemSupplier();
    private Map<Class<? extends Item>, RotatableImageIcon> itemIcons;


    public ViewController(Game game) {
        this.game = game;

        loadIcons();
        initGraphics();
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                // on regarde quelle touche a été pressée
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> game.getPlayer().getController().move(Player.Orientation.LEFT);
                    case KeyEvent.VK_RIGHT -> game.getPlayer().getController().move(Player.Orientation.RIGHT);
                    case KeyEvent.VK_DOWN -> game.getPlayer().getController().move(Player.Orientation.DOWN);
                    case KeyEvent.VK_UP -> game.getPlayer().getController().move(Player.Orientation.UP);
                    case KeyEvent.VK_C -> game.getPlayer().getController().use(WaterCap.class);
                    case KeyEvent.VK_SPACE -> game.getPlayer().getController().jump();
                    case KeyEvent.VK_I -> inventoryDisplay.setVisible(!inventoryDisplay.isVisible());
                }
            }
        });
    }

    private void loadIcons() {
        player = loadIconResource("/img/pacman.png");
        normalSlot = loadIconResource("/img/normal.png");
        wall = loadIconResource("/img/wall.png");
        door = loadIconResource("/img/door.png");
        hole = loadIconResource("/img/void.png");
        singleUse = loadIconResource("/img/column.png");
        fire = loadIconResource("/img/fire.png");

        itemIcons = Map.of(
                Chest.class, loadIconResource("/img/chest.png"),
                Key.class, loadIconResource("/img/key.png"),
                NoItem.class, loadIconResource("/img/normal.png"),
                WaterCap.class, loadIconResource("/img/wbottle.png")
        );
    }

    private RotatableImageIcon loadIconResource(String url) {
        BufferedImage image;

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
        setSize(WINDOW_SIZE_X, WINDOW_SIZE_Y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre

        int itemCount = itemSupplier.count();
        viewGrid = new JLabel[GRID_SIZE_X][GRID_SIZE_Y];
        inventoryGrid = new JLabel[2][itemCount];

        JComponent root = new JPanel();
        JComponent tempGrid = new JPanel(new GridLayout(GRID_SIZE_Y, GRID_SIZE_X)); // tempGrid va contenir les cases graphiques et les positionner sous la forme d'une grille
        inventoryDisplay = new JPanel(new GridLayout(itemCount, 2));

        for (int y = 0; y < GRID_SIZE_Y; y++) {
            for (int x = 0; x < GRID_SIZE_X; x++) {
                JLabel jl = new JLabel();
                viewGrid[x][y] = jl; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )
                tempGrid.add(jl);
            }
        }
        for (int y = 0; y < itemCount; y++) {
            for (int x = 0; x < 2; x++) {
                JLabel jl = new JLabel();
                inventoryGrid[x][y] = jl;
                inventoryDisplay.add(jl);
            }
        }

        ((FlowLayout) root.getLayout()).setAlignment(FlowLayout.LEFT);
        tempGrid.setBounds(0, 0, Double.valueOf(getWidth() * 0.75).intValue(), getHeight());
        root.add(tempGrid);
        inventoryDisplay.setVisible(false);
        inventoryDisplay.setBounds(Double.valueOf(getWidth() * 0.75).intValue() + 1, 0, Double.valueOf(getWidth() * 0.25).intValue(), getHeight());
        root.add(inventoryDisplay);
        add(root);
    }


    /**
     * Il y a une grille du côté du modèle ( jeu.getGrille() ) et une grille du côté de la vue (tabJLabel)
     */
    private void updateDisplay() {

        for (int x = 0; x < GRID_SIZE_X; x++) {
            for (int y = 0; y < GRID_SIZE_Y; y++) {
                StaticEntity e = game.currentRoom().getStatic(x, y);
                if (e instanceof Wall) {
                    viewGrid[x][y].setIcon(wall);
                } else if (e instanceof SingleUsageSlot) {
                    viewGrid[x][y].setIcon(((SingleUsageSlot) e).isUsed() ? fire : singleUse);
                } else if (e instanceof NormalSlot) {
                    NormalSlot cn = (NormalSlot) e;
                    viewGrid[x][y].setIcon(cn.item == null || cn.item instanceof NoItem ? normalSlot : itemIcons.get(cn.item.getClass()));
                } else if (e instanceof Door) {
                    viewGrid[x][y].setIcon(door);
                } else if (e instanceof Hole) {
                    viewGrid[x][y].setIcon(hole);
                }
            }
        }

        player.rotate(game.getPlayer().getOrientation().getRadians());
        viewGrid[game.getPlayer().getPosition().x][game.getPlayer().getPosition().y].setIcon(player);

        if (inventoryDisplay.isVisible())
            updateInventoryDisplay(game.getPlayer().getInventory());
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

    private void updateInventoryDisplay(Inventory inv) {
        int i = 0;
        for (Class<? extends Item> itemType : itemSupplier.baseList()) {
            if (itemType.equals(NoItem.class))
                continue;

            inventoryGrid[0][i].setIcon(itemIcons.get(itemType));
            inventoryGrid[1][i++].setText(" x" + inv.count(itemType));
        }
    }
}
