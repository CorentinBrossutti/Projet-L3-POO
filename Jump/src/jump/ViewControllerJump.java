package jump;

import meta.Plugin;
import meta.ViewController;
import view.ViewControllerHandle;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Contrôleur-vue pour le plugin saut
 */
public class ViewControllerJump extends ViewController {
    public ViewControllerJump(Plugin plugin, ViewControllerHandle handle) {
        super(plugin, handle);
    }

    @Override
    public void setupInputs() {
        handle.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_SPACE)
                    plugin.game.player.getController(plugin.name, PlayerControllerJump.class).jump();
            }
        });
    }
}
