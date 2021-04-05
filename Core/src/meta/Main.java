package meta;

import model.Game;
import util.Util;
import view.ViewControllerHandle;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


public class Main {
    public static final String PLUGIN_FOLDER_NAME = "plugins/";
    public static List<Plugin> plugins;
    public static MetaLoader loader = new MetaLoader();

    public static void main(String[] args) {
        Game game = Util.Reflections.dynInstantiate("model.Game", loader);
        ViewControllerHandle vc = Util.Reflections.dynInstantiate("view.ViewControllerHandle", loader, game);
        game.addObserver(vc);

        /*
        Initialisation des plugins
         */
        List<Plugin> plist = new ArrayList<>();

        try{
            File pluginFolder = new File(
                    new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile(),
                    PLUGIN_FOLDER_NAME);
            if(!pluginFolder.exists()){
                System.exit(1);
                return;
            }

            JarFile current;
            for(File plugin : pluginFolder.listFiles((dir, name) -> name.startsWith("Plugin") && name.endsWith(".jar"))){
                loader.add(plugin.toURI().toURL());
                current = new JarFile(plugin);
                Enumeration<JarEntry> en = current.entries();

                JarEntry temp;
                while (en.hasMoreElements()) {
                    temp = en.nextElement();
                    //The $ is meant to avoid inner classes
                    if (temp.getName().contains("$") || !temp.getName().endsWith(".class") || temp.getName().contains("package-info"))
                        continue;

                    Class pluginClazz = Class.forName(
                            temp.getName().substring(0, temp.getName().lastIndexOf(".")).replaceAll("/", "."),
                            true,
                            loader);
                    //if the class isn't a spell or is abstract, skip
                    if (!Plugin.class.isAssignableFrom(pluginClazz) || Modifier.isAbstract(pluginClazz.getModifiers()))
                        continue;

                    Plugin p = (Plugin) Util.Reflections.instantiate(pluginClazz, game, vc);
                    plist.add(p);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            System.exit(1);
            return;
        }

        plugins = Collections.unmodifiableList(plist);
        /*
        Fin initialisation des plugins
         */

        game.init();
        vc.initGraphics();
        vc.setupInputs();
        game.start();
        vc.setVisible(true);
    }

}
