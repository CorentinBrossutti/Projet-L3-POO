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

    private Game game;

    // Icônes de base pour les entités statiques
    private RotatableImageIcon
            player,
            normalSlot,
            wall,
            door,
            hole,
            singleUse,
            fire;

    /**
     * Grille de jeu
     */
    private JLabel[][] viewGrid;

    /**
     * Composant d'affichage de l'inventaire
     */
    private JComponent inventoryDisplay;
    /**
     * Grilles de l'inventaire, le premier indice correspond à la ligne de l'objet.
     * Le deuxième indice correspond, en 0, au label (l'icône de l'objet) et en 1 à la quantité
     */
    private JLabel[][] inventoryGrid;

    private final WeightedItemSupplier itemSupplier = new WeightedItemSupplier();
    /**
     * Map liant les types d'objet à leurs icônes
     */
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

    /**
     * Charge une icône contenue dans le jar en tant que ressources
     * @param url Url de ressource, sous une forme acceptée par {@link java.lang.Class#getResourceAsStream(String)}
     * @return Une icône représentant l'image qui peut subir une rotation
     */
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
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // On crée les grilles
        int itemCount = itemSupplier.count();
        inventoryGrid = new JLabel[2][itemCount];
        viewGrid = new JLabel[GRID_SIZE_X][GRID_SIZE_Y];

        // On crée les composants
        JComponent tempGrid = new JPanel(new GridLayout(GRID_SIZE_Y, GRID_SIZE_X));
        inventoryDisplay = new JPanel(new GridLayout(itemCount, 2));

        // On initialise les cases de jeu...
        for (int y = 0; y < GRID_SIZE_Y; y++) {
            for (int x = 0; x < GRID_SIZE_X; x++) {
                JLabel jl = new JLabel();
                viewGrid[x][y] = jl;
                tempGrid.add(jl);
            }
        }
        // ...ainsi que les cases de l'inventaire...
        for (int y = 0; y < itemCount; y++) {
            for (int x = 0; x < 2; x++) {
                JLabel jl = new JLabel();
                inventoryGrid[x][y] = jl;
                inventoryDisplay.add(jl);
            }
        }
        // ...ce qui est nécessaire pour pouvoir en modifier l'affichage plus tard, grâce aux variables de classe respectives

        // On souhaite que l'inventaire se place horizontalement après la grille de jeu, et que chaque élément soit justifié à gauche
        ((FlowLayout) getLayout()).setAlignment(FlowLayout.LEFT);
        // Taille de la grille de jeu : 3/4 de la fenêtre en largeur
        tempGrid.setBounds(0, 0, Double.valueOf(getWidth() * 0.75).intValue(), getHeight());
        // Taille de l'inventaire : 1/4 de la fenêtre en largeur, invisible par défaut
        inventoryDisplay.setVisible(false);
        inventoryDisplay.setBounds(Double.valueOf(getWidth() * 0.75).intValue() + 1, 0, Double.valueOf(getWidth() * 0.25).intValue(), getHeight());
        add(tempGrid);
        add(inventoryDisplay);
    }

    @Override
    public void update(Observable o, Object arg) {
        for (int x = 0; x < GRID_SIZE_X; x++) {
            for (int y = 0; y < GRID_SIZE_Y; y++) {
                StaticEntity e = game.currentRoom().getStatic(x, y);
                if (e instanceof Wall) {
                    viewGrid[x][y].setIcon(wall);
                } else if (e instanceof SingleUsageSlot) {
                    viewGrid[x][y].setIcon(((SingleUsageSlot) e).isUsable() ? singleUse : fire);
                } else if (e instanceof NormalSlot) {
                    NormalSlot cn = (NormalSlot) e;
                    // Soit la case a l'icône blanche de base (pas d'objet), soit l'icône de l'objet
                    viewGrid[x][y].setIcon(cn.item == null || cn.item instanceof NoItem ? normalSlot : itemIcons.get(cn.item.getClass()));
                } else if (e instanceof Door) {
                    viewGrid[x][y].setIcon(door);
                } else if (e instanceof Hole) {
                    viewGrid[x][y].setIcon(hole);
                }
            }
        }

        // On applique la rotation au joueur
        player.rotate(game.getPlayer().getOrientation().getRadians());
        viewGrid[game.getPlayer().getPosition().x][game.getPlayer().getPosition().y].setIcon(player);

        // Si l'inventaire est visible on le met à jour
        if (inventoryDisplay.isVisible())
            updateInventoryDisplay(game.getPlayer().getInventory());
    }

    /**
     * Met à jour l'affichage de l'inventaire.
     * @param inv Inventaire à afficher
     */
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
