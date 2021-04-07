package base;

import meta.Plugin;
import model.Game;
import view.ViewControllerHandle;

public class PluginBase extends Plugin {
    /**
     * Le nombre de capsules lorsque l'on rentre dans une salle
     */
    public static byte WCAP_COUNT = 1;

    public PluginBase(Game game, ViewControllerHandle handle) {
        super(game, handle, "Base");

        model = new ModelBase(this, this);
        viewController = new ViewControllerBase(this, handle);
        events = new EventsBase(this);
    }


}
