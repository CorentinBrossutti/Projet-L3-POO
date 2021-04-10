package enemies.model;

import model.Character;
import model.CharacterController;
import model.Room;
import util.Position;

import java.util.PriorityQueue;
import java.util.Queue;

public class CharacterControllerEnemy extends CharacterController {
    public CharacterControllerEnemy(Character character) {
        super(character);
        this.SX.add(character.position.x);
        this.SY.add(character.position.y);
        this.move_count = 0;
        this.nodes_left_in_layer = 0;
        this.nodes_in_next_layer = 1;
        this.ended = false;
    }
    /* --- VARIABLES GLOBALES --- */

    /**
     * SX est une file où l'on stocke initialement la coordonnées X de l'ennemi.
     * Elle sert à enfiler les coordonnées exploré en abscisse.
     * @type_T: Integer est la classe pour les int.
     */
    private Queue<Integer> SX = new PriorityQueue<>();

    /**
     * SY est une file où l'on stocke initialement la coordonnées Y de l'ennemi.
     * Elle sert à enfiler les coordonnées exploré en ordonné.
     * @type_T: Integer est la classe pour les int.
     */
    private Queue<Integer> SY = new PriorityQueue<>();

    /* --- VARIABLES POUR SUIVRE L'AVANCEE DE L'ALGO --- */
    /**
     * move_count est un compteur du nombre de mouvements.
     */
    private int move_count;

    /**
     * nodes_left_in_layer est un compteur du nombre de noeud restant à "défiler" avant la prochaine étape pour atteindre le joueur.
     */
    private int nodes_left_in_layer;

    /**
     * nodes_in_next_layer est un compteur du nombre de noeud que l'on a ajouté pendant l'éxecution de l'algo.
     * Permet l'update adéquat de la variable nodes_left_in_layer.
     */
    private int nodes_in_next_layer;

    /**
     * ended est un boolean permettant de detecter si l'on a atteint le joueur.
     */
    private boolean ended;

    /**
     * visited est un tableau permettant un historique des cellules déjà explorés.
     * Il évite que l'on retombe sur la même case plusieurs fois.
     */
    private boolean visited[][] = new boolean[Room.SIZE_X][Room.SIZE_Y];

    /**
     *  Vecteur pour les déplacements Nord et Sud.
     */
    private static int NS_directionVector[] = {-1, +1, 0, 0};

    /**
     * Vecteur pour les déplacements Est et Ouest.
     */
    private static int OE_directionVector[] = {0, 0, +1, -1};
    /**
     *
     */
    private boolean is_reached(Position position_player, Position enemy_pos){
        return position_player.equals(enemy_pos);
    }
    /**
     * Plus court chemin, faire aller l'ennemi à la cible.
     * @param position Position cible.
     */
    public void solve(Position position) {
        int x_ = character.position.x, y_ = character.position.y;
        visited[x_][y_] = true;

        do{
            explore(x_, y_);
            x_ = this.SX.poll();
            y_ = this.SY.poll();
            if (is_reached(position, this.character.position)) {
                this.ended = true;
                break;
            }
            this.nodes_left_in_layer--;
            if (nodes_left_in_layer == 0) {
                nodes_left_in_layer = nodes_in_next_layer;
                nodes_in_next_layer = 0;
                move_count++;
            }
        }while(SX.size() > 0);

        if(is_reached(position, this.character.position))
            this.ended = true;
    }

    /**
     * Procédure d'exploration des cases voisines
     * @param x
     * @param y
     */
    public void explore(int x, int y){
        Position pos_volatile = new Position(this.character.position.x, this.character.position.y);
        for (int i = 0; i < 4; i++){
            pos_volatile.x = x + this.NS_directionVector[i];
            pos_volatile.y = y + this.OE_directionVector[i];
            if(pos_volatile.x < 0 || pos_volatile.y < 0)
                continue;
            if(pos_volatile.x >= Room.SIZE_X || pos_volatile.y >= Room.SIZE_Y)
                continue;
            if(visited[pos_volatile.x][pos_volatile.y])
                continue;
            if(character.room().getStatic(pos_volatile).collide((Enemy)character))
                continue;

            this.SX.add(pos_volatile.x);
            this.SY.add(pos_volatile.y);
            this.visited[pos_volatile.x][pos_volatile.y] = true;

            nodes_in_next_layer++;
        }
    }
    /**
     * TODO: - Faire une file des cases pour atteindre l'ennemi à return dans solve;
     *       - Déplacer l'ennemie en lisant la file return de solve;
     */

    /**
     * Fait bouger aléatoirement l'ennemi d'une case.
     */
    public void randomMovement(){
        move(character.game.gen.randomOrientation());
    }
}
