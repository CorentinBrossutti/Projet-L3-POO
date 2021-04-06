package base;

import base.model.board.items.Chest;
import base.model.board.items.Key;
import base.model.board.items.WaterCap;
import base.model.board.statics.*;
import meta.Plugin;
import meta.ViewController;
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
    /**
     * Map liant les types d'objet à leurs icônes
     */
    private Map<Class<? extends Item>, RotatableImageIcon> itemIcons;
    /**
     * Map liant les types d'entités statiques à leurs icônes
     */
    private Map<Class<? extends StaticEntity>, RotatableImageIcon> staticIcons;
    private RotatableImageIcon fire;

    public BaseViewController(Plugin plugin, ViewControllerHandle handle, Game game) {
        super(plugin, handle, game);
    }

    @Override
    public void initGraphics() {
        staticIcons = Map.of(
                NormalSlot.class, loadIconResource("/img/normal.png", getClass()),
                Wall.class, loadIconResource("/img/wall.png", getClass()),
                Door.class, loadIconResource("/img/door.png", getClass()),
                Hole.class, loadIconResource("/img/void.png", getClass()),
                SingleUsageSlot.class, loadIconResource("/img/column.png", getClass())
        );
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
                if (e.getKeyCode() == KeyEvent.VK_C)
                    game.getPlayer().getCoreController().use(WaterCap.class);
            }
        });
    }

    @Override
    public void update() {
        for (short x = 0; x < ViewControllerHandle.GRID_SIZE_X; x++) {
            for (short y = 0; y < ViewControllerHandle.GRID_SIZE_Y; y++) {
                StaticEntity e = game.currentRoom().getStatic(x, y);
                if (e instanceof SingleUsageSlot)
                    handle.viewGrid[x][y].setIcon(((SingleUsageSlot) e).usable() ? staticIcons.get(SingleUsageSlot.class) : fire);
                else if (e instanceof NormalSlot) {
                    NormalSlot cn = (NormalSlot) e;
                    // Soit la case a l'icône blanche de base (pas d'objet), soit l'icône de l'objet
                    handle.viewGrid[x][y].setIcon(
                            cn.item == null || cn.item instanceof NoItem ?
                                    staticIcons.get(NormalSlot.class) :
                                    itemIcons.get(cn.item.getClass()));
                } else
                    handle.viewGrid[x][y].setIcon(staticIcons.get(e.getClass()));
            }
        }
    }
}
