/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import meta.Events;
import meta.Main;
import model.board.items.Item;
import model.board.statics.StaticEntity;
import util.Position;

/**
 * Héros du jeu
 */
public class Player extends Character implements Collideable {
    /**
     * L'inventaire du joueur
     */
    public final Inventory inventory = new Inventory();

    public Player(Game game, int x, int y) {
        this(game, new Position(x, y));
    }

    public Player(Game game, Position position) {
        super(game, position);

        // Ajout du contrôleur base
        controllers.put("core", new PlayerControllerCore(this));
    }

    /**
     *
     * @return Le contrôleur basique du joueur "core"
     */
    public PlayerControllerCore getCoreController(){
        return getController("core");
    }

    @Override
    public Boolean collidesWith(StaticEntity staticEntity) {
        return null;
    }

    /**
     * Un contrôleur spécifiquement pour un joueur
     */
    public static class PlayerController extends CharacterController{
        protected final Player player;

        public PlayerController(Player player) {
            super(player);

            this.player = player;
        }
    }

    /**
     * Classe "core" pour les mouvements de base du joueur
     */
    public static class PlayerControllerCore extends PlayerController {
        public PlayerControllerCore(Player player) {
            super(player);
        }

        /**
         * Tente de faire utiliser un objet au joueur sur la case qu'il regarde, et le retire de son inventaire.
         * @param item L'objet à utiliser
         */
        public void use(Item item) {
            StaticEntity target = player.room().getStatic(player.orientation.getNextPos(player.position));
            if (Events.callBool(Main.plugins, Events.PLAYER_USES, false, player, target, item) && target.use(player, item))
                player.inventory.remove(item);
        }

        /**
         * Tente de faire utiliser un certain type d'objet au joueur sur la case qu'il regarde.
         * Nécessite que le joueur ait au moins un exemplaire du type d'objet dans son inventaire, et lui retire après utilisation.
         *
         * @param type Type d'objet à utiliser
         */
        public void use(Class<? extends Item> type) {
            if (!player.inventory.has(type))
                return;

            use(player.inventory.firstOf(type));
        }
    }
}
