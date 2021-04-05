package meta;

import model.Game;
import util.Util;
import view.ViewControllerHandle;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginLoader extends URLClassLoader {
    public static final String
            GAME_CLASS_PATH = "model.Game",
            VC_CLASS_PATH = "view.ViewControllerHandle";

    protected Game game;
    protected ViewControllerHandle vcHandle;

    public PluginLoader(){
        super(
                new URL[]{},
                ClassLoader.getSystemClassLoader()
        );

        // L'instanciation dynamique permet de faire en sorte que game et vcHandle utilisent le bon class loader (celui-ci)
        game = Util.Reflections.dynInstantiate(GAME_CLASS_PATH, this);
        vcHandle = Util.Reflections.dynInstantiate(VC_CLASS_PATH, this, game);
    }

    public PluginLoader(Game game, ViewControllerHandle vcHandle){
        super(
                new URL[]{},
                ClassLoader.getSystemClassLoader()
        );

        this.game = game;
        this.vcHandle = vcHandle;
    }

    /**
     * Charge un plugin sans lever d'exception.
     * Les classes seront ajoutées au loader.
     * @param pluginFile Fichier du plugin (PluginXXX.jar)
     * @return Le plugin chargé
     */
    public Plugin safeLoad(File pluginFile) {
        try {
            return load(pluginFile);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Charge un plugin depuis un fichier.
     * Les classes seront ajoutées au loader.
     * @param pluginFile Fichier du plugin (PluginXXX.jar)
     * @return Le plugin chargé
     */
    public Plugin load(File pluginFile) throws IOException, ClassNotFoundException {
        // Mauvais format
        if(!pluginFile.getName().startsWith("Plugin") || !pluginFile.getName().endsWith(".jar"))
            return null;

        // On ajoute les classes de l'extension en paramètre dans le classpath
        addURL(pluginFile.toURI().toURL());
        // On crée un fichier jar basé sur le fichier en paramètre pour énumérer les éléments
        JarFile jf = new JarFile(pluginFile);

        return load(jf);
    }

    /**
     * Charge un plugin depuis un JarFile.
     * Les classes seront ajoutées au loader.
     * @param pluginFile  Fichier JAR du plugin
     * @return Le plugin chargé
     */
    public Plugin load(JarFile pluginFile) throws ClassNotFoundException {
        Enumeration<JarEntry> en = pluginFile.entries();

        JarEntry temp;
        while (en.hasMoreElements()) {
            temp = en.nextElement();
            // On scanne les classes, en ignorant les classes internes (contenant un $ dans le nom brut)
            if (temp.getName().contains("$") || !temp.getName().endsWith(".class") || temp.getName().contains("package-info"))
                continue;

            // Obtention de la classe
            Class<?> clazz = Class.forName(
                    temp.getName().substring(0, temp.getName().lastIndexOf(".")).replaceAll("/", "."),
                    true,
                    this
            );

            // Si la classe n'est pas la classe principale qui nous intéresse
            if (!Plugin.class.isAssignableFrom(clazz) || Modifier.isAbstract(clazz.getModifiers()))
                continue;

            // On instancie le plugin depuis la classe et le retourne
            return (Plugin) Util.Reflections.instantiate(clazz, game, vcHandle);
        }

        throw new ClassNotFoundException("Impossible de trouver la classe principale du plugin !");
    }

    /**
     * Charge les plugins d'un dossier sans avoir à gérer les exceptions.
     * Utilise le filtre par défaut soit PluginXXX.jar
     * @param folder Dossier de plugins
     * @return L'ensemble des plugins chargés
     */
    public Set<Plugin> safeLoadPlugins(File folder) {
        try {
            return loadPlugins(folder);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Charge les plugins d'un dossier sans avoir à gérer les exceptions.
     * @param folder Dossier de plugins
     * @param filter Filtre à utiliser pour les fichiers
     * @return L'ensemble des plugins chargés
     */
    public Set<Plugin> safeLoadPlugins(File folder, FilenameFilter filter){
        try {
            return loadPlugins(folder, filter);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Charge les plugins d'un dossier.
     * Utilise le filtre par défaut soit PluginXXX.jar
     * @param folder Dossier de plugins
     * @return L'ensemble des plugins chargés
     */
    public Set<Plugin> loadPlugins(File folder) throws IOException, ClassNotFoundException {
        return loadPlugins(folder, (dir, name) -> name.startsWith("Plugin") && name.endsWith(".jar"));
    }

    /**
     * Charge les plugins d'un dossier.
     * @param folder Dossier de plugins
     * @param filter Filtre à utiliser pour les fichiers
     * @return L'ensemble des plugins chargés
     */
    public Set<Plugin> loadPlugins(File folder, FilenameFilter filter) throws IOException, ClassNotFoundException {
        Set<Plugin> temp = new HashSet<>();

        for (File f : folder.listFiles(filter))
            temp.add(load(f));

        return Collections.unmodifiableSet(temp);
    }
}
