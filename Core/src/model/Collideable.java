package model;

import model.board.statics.StaticEntity;

import java.util.function.BiFunction;

public interface Collideable {
    /**
     *
     * @param staticEntity Entité statique
     * @return Retourne vrai si l'entité statique entre à priori (dernier mot revient à l'entité) en collision, faux sinon, ou null si aucun avis
     */
    Boolean collidesWith(StaticEntity staticEntity);

    /**
     *
     * @param from Entité statique source
     * @param assumption Booléen assumé par défaut (l'entité est-elle à priori génératrice de collision ?)
     * @param mixer Fonction permettant de lier l'assomption avec la volonté de collision de l'entité
     * @return Un booleén final de collision
     */
    default boolean askCollision(StaticEntity from, boolean assumption, BiFunction<Boolean, Boolean, Boolean> mixer){
        Boolean b = collidesWith(from);

        return b == null ? assumption : mixer.apply(b, assumption);
    }
}
