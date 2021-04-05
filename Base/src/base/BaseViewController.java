package base;

import meta.ViewController;
import base.model.board.items.*;
import base.model.board.statics.*;
import model.Game;
import model.board.items.Item;
import model.board.items.NoItem;
import model.board.statics.StaticEntity;
import view.RotatableImageIcon;
import view.ViewControllerHandle;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;

import static util.Util.Images.loadIconResource;

public class BaseViewController extends ViewController {
    // Icônes de base pour les entités statiques
    private RotatableImageIcon
            normalSlot,
            wall,
            door,
            hole,
            singleUse,
            fire;

    /**
     * Map liant les types d'objet à leurs icônes
     */
    private Map<Class<? extends Item>, RotatableImageIcon> itemIcons;

    public BaseViewController(ViewControllerHandle handle, Game game) {
        super(handle, game);
    }

    @Override
    public void initGraphics() {
        normalSlot = loadIconResource("/img/normal.png", getClass());
        wall = loadIconResource("/img/wall.png", getClass());
        door = loadIconResource("/img/door.png", getClass());
        hole = loadIconResource("/img/void.png", getClass());
        singleUse = loadIconResource("/img/column.png", getClass());
        fire = loadIconResource("/img/fire.png", getClass());

        itemIcons = Map.of(
                Chest.class, loadIconResource("/img/chest.png", getClass()),
                Key.class, loadIconResource("/img/key.png", getClass()),
                NoItem.class, loadIconResource("/img/normal.png", getClass()),
                WaterCap.class, loadIconResource("/img/wbottle.png", getClass())
        );

        handle.inventoryIcons.putAll(itemIcons);
    }

    @Override
    public void setupInputs() {
        handle.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_C)
                    game.getPlayer().getCoreController().use(WaterCap.class);
            }
        });
    }

    @Override
    public void update() {
        for (int x = 0; x < ViewControllerHandle.GRID_SIZE_X; x++) {
            for (int y = 0; y < ViewControllerHandle.GRID_SIZE_Y; y++) {
                StaticEntity e = game.currentRoom().getStatic(x, y);
                if (e instanceof Wall) {
                    handle.viewGrid[x][y].setIcon(wall);
                } else if (e instanceof SingleUsageSlot) {
                    handle.viewGrid[x][y].setIcon(((SingleUsageSlot) e).isUsable() ? singleUse : fire);
                } else if (e instanceof NormalSlot) {
                    NormalSlot cn = (NormalSlot) e;
                    // Soit la case a l'icône blanche de base (pas d'objet), soit l'icône de l'objet
                    handle.viewGrid[x][y].setIcon(cn.item == null || cn.item instanceof NoItem ? normalSlot : itemIcons.get(cn.item.getClass()));
                } else if (e instanceof Door) {
                    handle.viewGrid[x][y].setIcon(door);
                } else if (e instanceof Hole) {
                    handle.viewGrid[x][y].setIcon(hole);
                }
            }
        }
    }
}
