package util;

import view.RotatableImageIcon;
import view.ViewControllerHandle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
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

        public static <T> T dynInstantiate(String clazz, Object... ctargs) {
            return dynInstantiate(clazz, ClassLoader.getSystemClassLoader(), ctargs);
        }

        public static <T> T dynInstantiate(String clazz, ClassLoader loader, Object... ctargs) {
            try {
                return (T) instantiate(loader.loadClass(clazz), ctargs);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
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
         * @return Une icône représentant l'image qui peut subir une rotation
         */
        public static RotatableImageIcon loadIconResource(String url, Class clazz) {
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
    public static final class Strings{
        public static String getLastPart(String s, String delim){
            String temp[] = s.split(delim);

            return temp[temp.length - 1];
        }
    }
}
