package view;

import meta.Main;
import model.Character;
import model.Game;
import model.Inventory;
import model.Room;
import model.board.items.Item;
import model.board.items.NoItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;

import static util.Util.Images.loadIconResource;


/**
 * Cette classe a deux fonctions :
 * (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 * (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle (flèches direction, etc.))
 */
public class ViewControllerHandle extends JFrame implements Observer {
    public static final short
            GRID_SIZE_X = Room.SIZE_X,
            GRID_SIZE_Y = Room.SIZE_Y,
            WINDOW_SIZE_X = 620,
            WINDOW_SIZE_Y = 330;

    private Game game;

    /**
     * Grille de jeu
     */
    public JLabel[][] viewGrid;

    /**
     * Composant d'affichage de l'inventaire
     */
    public JComponent inventoryDisplay;
    /**
     * Grilles de l'inventaire, le premier indice correspond à la ligne de l'objet.
     * Le deuxième indice correspond, en 0, au label (l'icône de l'objet) et en 1 à la quantité
     */
    public JLabel[][] inventoryGrid;
    public final Map<Class<? extends Item>, RotatableImageIcon> inventoryIcons = new HashMap<>();

    public final Map<Character, RotatableImageIcon> characterIcons = new HashMap<>();


    public ViewControllerHandle(Game game) {
        this.game = game;
    }

    public void setupInputs(){
        Main.plugins.forEach(
                plugin -> plugin.viewController.setupInputs()
        );
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                // on regarde quelle touche a été pressée
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> game.getPlayer().getCoreController().move(Character.Orientation.LEFT);
                    case KeyEvent.VK_RIGHT -> game.getPlayer().getCoreController().move(Character.Orientation.RIGHT);
                    case KeyEvent.VK_DOWN -> game.getPlayer().getCoreController().move(Character.Orientation.DOWN);
                    case KeyEvent.VK_UP -> game.getPlayer().getCoreController().move(Character.Orientation.UP);
                    case KeyEvent.VK_I -> inventoryDisplay.setVisible(!inventoryDisplay.isVisible());
                }
            }
        });
    }

    public void initGraphics() {
        setTitle("Roguelike");
        setSize(WINDOW_SIZE_X, WINDOW_SIZE_Y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // On crée les grilles
        short itemCount = game.gen.itemDistinctCount;
        inventoryGrid = new JLabel[2][itemCount];
        viewGrid = new JLabel[GRID_SIZE_X][GRID_SIZE_Y];

        // On crée les composants
        JComponent tempGrid = new JPanel(new GridLayout(GRID_SIZE_Y, GRID_SIZE_X));
        inventoryDisplay = new JPanel(new GridLayout(itemCount, 2));

        // On initialise les cases de jeu...
        for (short y = 0; y < GRID_SIZE_Y; y++) {
            for (short x = 0; x < GRID_SIZE_X; x++) {
                JLabel jl = new JLabel();
                viewGrid[x][y] = jl;
                tempGrid.add(jl);
            }
        }
        // ...ainsi que les cases de l'inventaire...
        for (short y = 0; y < itemCount; y++) {
            for (short x = 0; x < 2; x++) {
                JLabel jl = new JLabel();
                inventoryGrid[x][y] = jl;
                inventoryDisplay.add(jl);
            }
        }
        JComponent root = new JPanel();
        // ...ce qui est nécessaire pour pouvoir en modifier l'affichage plus tard, grâce aux variables de classe respectives

        // On souhaite que l'inventaire se place horizontalement après la grille de jeu, et que chaque élément soit justifié à gauche
        ((FlowLayout) root.getLayout()).setAlignment(FlowLayout.LEFT);
        // Taille de la grille de jeu : 3/4 de la fenêtre en largeur
        tempGrid.setBounds(0, 0, Double.valueOf(getWidth() * 0.75).intValue(), getHeight());
        // Taille de l'inventaire : 1/4 de la fenêtre en largeur, invisible par défaut
        inventoryDisplay.setVisible(false);
        inventoryDisplay.setBounds(Double.valueOf(getWidth() * 0.75).intValue() + 1, 0, Double.valueOf(getWidth() * 0.25).intValue(), getHeight());
        root.add(tempGrid);
        root.add(inventoryDisplay);
        add(root);

        characterIcons.put(game.getPlayer(), loadIconResource("/img/pacman.png"));

        Main.plugins.forEach(
                plugin -> plugin.viewController.initGraphics()
        );
    }

    @Override
    public void update(Observable o, Object arg) {
        // Si l'inventaire est visible on le met à jour
        if (inventoryDisplay.isVisible())
            updateInventoryDisplay(game.getPlayer().inventory);

        // Tick des plugins
        Main.plugins.forEach(
                plugin -> plugin.viewController.update()
        );

        // On fait le rendu des personnages
        Iterator<Character> it = game.characters.iterator();
        Character temp;
        while(it.hasNext()){
            temp = it.next();
            // Si le personnage est mort, on le retire de la liste
            if(temp.dead){
                it.remove();
                continue;
            }
            else if(!characterIcons.containsKey(temp))
                continue;

            characterIcons.get(temp).rotate(temp.orientation.radians);
            viewGrid[temp.position.x][temp.position.y].setIcon(characterIcons.get(temp));
        }
    }

    /**
     * Met à jour l'affichage de l'inventaire.
     * @param inv Inventaire à afficher
     */
    private void updateInventoryDisplay(Inventory inv) {
        int i = 0;
        for (Class<? extends Item> itemType : game.gen.baseItems) {
            if (itemType.equals(NoItem.class))
                continue;

            // La case 0 est l'icône de l'objet
            inventoryGrid[0][i].setIcon(inventoryIcons.get(itemType));
            // La case 1 affiche le nombre
            inventoryGrid[1][i++].setText(" x" + inv.count(itemType));
        }
    }
}
