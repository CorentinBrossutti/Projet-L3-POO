package meta;

import model.Game;
import util.Util;
import view.ViewControllerHandle;

import java.io.File;
import java.util.Set;
import java.util.SortedSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


public class Main {
    /**
     * Le nom du dossier contenant les plugins, depuis le chemin courant de l'éxécutable
     */
    public static final String PLUGIN_FOLDER_NAME = "plugins/";
    /**
     * Le dossier contenant les plugins
     */
    public static final File PLUGIN_FOLDER = new File(Util.Files.getExecutingDirectory(Main.class), PLUGIN_FOLDER_NAME);

    /**
     * Le class loader qui sert à charger dans le class path toutes les classes des modules, et à charger les plugins
     */
    public static final PluginLoader loader = new PluginLoader();
    /**
     * Set final et non modifiable des plugins chargés avec le jeu
     */
    public static final Set<Plugin> plugins = loader.safeLoadPlugins(PLUGIN_FOLDER);

    public static void main(String[] args) {
        /*
        On récupère une instance de Game ainsi que de ViewControllerHandle depuis le loader.
        En effet, ce dernier les a instanciés dynamiquement afin qu'ils utilisent le bon class loader.
         */
        Game game = loader.game;
        ViewControllerHandle vcHandle = loader.vcHandle;
        game.addObserver(vcHandle);

        game.init();
        vcHandle.initGraphics();
        vcHandle.setupInputs();
        game.start();
        vcHandle.setVisible(true);
    }

}
