package util;

import meta.Main;
import view.RotatableImageIcon;
import view.ViewControllerHandle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Divers utilitaires
 */
public final class Util {
    /**
     * Utilitaires de réflexion et d'instanciation dynamique
     */
    public static final class Reflections {
        /**
         * Instacie un objet depuis une classe
         * @param clazz Classe type de l'objet à instancier
         * @param ctargs Arguments du constructeur, objets et NON types
         * @param <T> Générique pour la syntaxe
         * @return Un objet instancié depuis le type donné avec les arguments donnés
         */
        public static <T> T instantiate(Class<T> clazz, Object... ctargs) {
            Class<?>[] params = new Class<?>[ctargs.length];
            for (int i = 0; i < params.length; i++)
                params[i] = ctargs[i].getClass();

            try {
                return clazz.getConstructor(params).newInstance(ctargs);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         *
         * @param clazz Chemin complet de la classe à instancier
         * @param ctargs Arguments du constructeurs, objets et NON types
         * @param <T> Générique syntaxique
         * @return L'objet instancié depuis le type donné avec les arguments fournis
         */
        public static <T> T dynInstantiate(String clazz, Object... ctargs) {
            return dynInstantiate(clazz, ClassLoader.getSystemClassLoader(), ctargs);
        }

        /**
         * Extrêmement utile pour s'assurer que les objets construits utilisent le bon class loader
         * @param clazz Chemin complet de la classe à instancier
         * @param loader ClassLoader à utiliser, sera passé à l'objet construit (TRES utile)
         * @param ctargs Arguments du constructeurs, objets et NON types
         * @param <T> Générique syntaxique
         * @return L'objet instancié depuis le type donné avec les arguments fournis, construit avec le loader fourni
         */
        public static <T> T dynInstantiate(String clazz, ClassLoader loader, Object... ctargs) {
            try {
                return (T) instantiate(loader.loadClass(clazz), ctargs);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    /**
     * Utilitaires liés aux images
     */
    public static final class Images {
        /**
         * Charge une icône contenue dans le jar en tant que ressources
         * @param url Url de ressource, sous une forme acceptée par {@link java.lang.Class#getResourceAsStream(String)}
         * @return Une icône représentant l'image qui peut subir une rotation
         */
        public static RotatableImageIcon loadIconResource(String url) {
            return loadIconResource(url, Images.class);
        }

        /**
         * Charge une icône contenue dans le jar en tant que ressources
         * @param url Url de ressource, sous une forme acceptée par {@link java.lang.Class#getResourceAsStream(String)}
         * @param clazz Classe source, utile pour que Java situe correctement l'emplacement de la ressource
         * @return Une icône représentant l'image qui peut subir une rotation
         */
        public static RotatableImageIcon loadIconResource(String url, Class<?> clazz) {
            BufferedImage image;

            try {
                image = ImageIO.read(clazz.getResourceAsStream(url));
            } catch (IOException ex) {
                Logger.getLogger(clazz.getName()).log(Level.SEVERE, null, ex);
                return null;
            }

            return new RotatableImageIcon(image);
        }
    }

    /**
     * Utilitaires de chaînes de caractères
     */
    public static final class Strings{
        /**
         *
         * @param s Chaîne
         * @param delim Délimiteur
         * @return La dernière partie de la chaîne de caractères découpée avec le délimiteur
         */
        public static String getLastPart(String s, String delim){
            String[] temp = s.split(delim);

            return temp[temp.length - 1];
        }
    }

    /**
     * Utilitaires de fichiers
     */
    public static final class Files{
        /**
         *
         * @return Le dossier dans lequel se trouve le jar contenant la classe spécifiée
         */
        public static File getExecutingDirectory(Class<?> clazz){
            try {
                return new File(clazz.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
