package jump;

import meta.ViewController;
import model.Game;
import view.ViewControllerHandle;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Contrôleur-vue pour le plugin saut
 */
public class JumpViewController extends ViewController {
    public JumpViewController(ViewControllerHandle handle, Game game) {
        super(handle, game);
    }

    @Override
    public void setupInputs() {
        handle.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_SPACE)
                    game.getPlayer().getController("jump", PlayerControllerJump.class).jump();
            }
        });
    }
}
