package util;

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
    }
}
