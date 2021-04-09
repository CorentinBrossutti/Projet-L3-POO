package enemies.model;

import model.Character;
import model.CharacterController;
import model.Room;
import util.Position;
import java.util.Queue;

public class CharacterControllerEnemy extends CharacterController {
    public CharacterControllerEnemy(Character character) {
        super(character);
        this.R = Room.SIZE_X;
        this.S = Room.SIZE_Y;
        this.SX.add(character.position.x);
        this.SY.add(character.position.y);
        this.move_count = 0;
        this.nodes_left_in_layer = 0;
        this.nodes_in_next_layer = 1;
        this.ended = false;
    }
    /* --- VARIABLES GLOBALES --- */
    /**
     * R est un entier static du nombre de colonnes dans le dongeon.
     */
    private int R;
    /**
     * S est un entier static du nombre de lignes dans le dongeon.
     */
    private int S;

    /**
     * SX est une file où l'on stocke initialement la coordonnées X de l'ennemi.
     * Elle sert à enfiler les coordonnées exploré en abscisse.
     * @type_T: Integer est la classe pour les int.
     */
    private Queue<Integer> SX;

    /**
     * SY est une file où l'on stocke initialement la coordonnées Y de l'ennemi.
     * Elle sert à enfiler les coordonnées exploré en ordonné.
     * @type_T: Integer est la classe pour les int.
     */
    private Queue<Integer> SY;

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
    private boolean visited[][];

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
        if(position_player.equals(enemy_pos)){
            return true;
        }
        else
            return false;
    }
    /**
     * Plus court chemin, faire aller l'ennemi à la cible.
     * @param position Position cible.
     */
    public void solve(Position position) {
        this.visited[this.SX.element()][this.SY.element()] = true;

        while (this.SX.size() > 0) {
            int x_ = this.SX.poll();
            int y_ = this.SY.poll();
            if (is_reached(position, this.character.position)) {
                this.ended = true;
                break;
            }
            explore(x_, y_);
            this.nodes_left_in_layer--;
            if (nodes_left_in_layer == 0) {
                nodes_left_in_layer = nodes_in_next_layer;
                nodes_in_next_layer = 0;
                move_count++;
            }
        }

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
            if(pos_volatile.x >= this.R || pos_volatile.y >= this.S)
                continue;
            if(visited[pos_volatile.x][pos_volatile.y])
                continue;
            /*if(J'ai une collision avec un MUR)
                continue;
            */
            this.SX.add(pos_volatile.x);
            this.SY.add(pos_volatile.y);
            this.visited[x][y] = true;

            nodes_in_next_layer++;
        }
    }
    /**
     * TODO: - Faire une file des cases pour atteindre l'ennemi à return dans solve;
     *       - Gerer la collision avec les murs;
     *       - Déplacer l'ennemie en lisant la file return de solve;
     */

    /**
     * Fait bouger aléatoirement l'ennemi d'une case.
     */
    public void randomMovement(){
        move(character.game.gen.randomOrientation());
    }
}
