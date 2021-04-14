package enemies.model;

import base.model.board.statics.Wall;
import model.Character;
import model.CharacterController;
import model.Room;
import model.board.statics.StaticEntity;
import util.Position;

import java.util.Collections;
import java.util.ArrayList;

/**
 * CharacterControllerEnemy implemente A* search Algorithme
 */
public class CharacterControllerEnemy extends CharacterController {
    /**
     * open est une ArrayList des noeuds pas encore explorés
     */
    private ArrayList<Node> open;

    /**
     * clodes est une ArrayListe des noeuds explorés
     */
    private ArrayList<Node> closed;

    /**
     * path est l'ArrayList du chemin vers le joueur
     */
    private ArrayList<Node> path;

    /**
     * room est la liste de nos entitées static
     */
    private StaticEntity[][] room = new StaticEntity[Room.SIZE_X][Room.SIZE_Y];

    /**
     * now est le noeud actuel
     */
    private Node now;

    /**
     * xstart est la position en x initiale de l'ennemi - A changer en position
     */
    private int xStart;

    /**
     * ystart est la position en y initale de l'ennemi - A changer en position
     */
    private int yStart;

    /**
     * xPlayer est la position initiale en x du joueur,
     * yPlayer est la position initiale en y du joueur
     * A changer en Position
     */
    private int xPlayer, yPlayer;

    /**
     * Constructeur de CharacterControllerEnemy
     * @param character
     */
    public CharacterControllerEnemy(Character character) {
        super(character);
        this.open = new ArrayList<>();
        this.closed = new ArrayList<>();
        this.path = new ArrayList<>();
        for(int i = 0; i < Room.SIZE_X; i++) {
            for (int j = 0; j < Room.SIZE_Y; j++){
                room[i][j] = character.room().getStatic(i,j);
            }
        }
        this.now = new Node(null, character.position.x, character.position.y, 0, 0);
        this.xStart = character.position.x;
        this.yStart = character.position.y;
    }

    /**
     * Procédure de reset des données du charactère controlleur
     */
    private void resetCharacterControllerEnemy(){
        this.open = new ArrayList<>();
        this.closed = new ArrayList<>();
        this.path = new ArrayList<>();
        for(int i = 0; i < Room.SIZE_X; i++) {
            for (int j = 0; j < Room.SIZE_Y; j++){
                room[i][j] = character.room().getStatic(i,j);
            }
        }
        this.now = new Node(null, character.position.x, character.position.y, 0, 0);
        this.xStart = character.position.x;
        this.yStart = character.position.y;
    }
    /**
     * Classe interne pour les neouds
     */
    public class Node implements Comparable{
        public Node parent;
        public int x,y;
        public double g;
        public double h;

        Node(Node parent, int xpos, int ypos, double g, double h){
            this.parent = parent;
            this.x = xpos;
            this.y = ypos;
            this.g = g;
            this.h = h;
        }

        @Override
        public int compareTo(Object o){
            Node that = (Node) o;
            return (int)((this.g + this.h) - (that.g + that.h));
        }
    }

    /**
     * Recherche le chemin vers la position du joueur ou retourne null
     * @param position_player
     */
    public void findPathTo(Position position_player) {
        this.xPlayer = position_player.x;
        this.yPlayer = position_player.y;
        this.closed.add(this.now);
        addNeigborsToOpenList();
        while (this.now.x != this.xPlayer || this.now.y != this.yPlayer) {
            if (this.open.isEmpty()) { // Rien a examiner
                break;
            }
            this.now = this.open.get(0); // Prend le premier noeud (avec le plus petit f)
            this.open.remove(0); // Le supprime
            this.closed.add(this.now); // L'ajoute aux neouds explorés
            addNeigborsToOpenList();
        }
        this.path.add(0, this.now);
        while (this.now.x != this.xStart || this.now.y != this.yStart) {
            this.now = this.now.parent;
            this.path.add(0, this.now);
        }
    }

    /**
     ** Regarde un noeud dans List<> passé en paramètre
     ** @return (bool) NeightborInListFound
     */
    private static boolean findNeighbor(ArrayList<Node> array, Node node) {
        return array.stream().anyMatch((n) -> (n.x == node.x && n.y == node.y));
    }

    /**
     * Calule la distance entre this.now et la position du joueur - clacul de la distance de Manathan
     * @return (int) distance
     */
    private double distance(int dx, int dy) {
        return Math.abs(this.now.x + dx - this.xPlayer) + Math.abs(this.now.y + dy - this.yPlayer); // return Distance de Manathan
    }

    /**
     * Ajoute les Noeuds voisin à la liste des noeuds à explorer
     */
    private void addNeigborsToOpenList() {
        Node node;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                node = new Node(this.now, this.now.x + x, this.now.y + y, this.now.g, this.distance(x, y));
                if ((x != 0 || y != 0) //si on est pas au même endroit
                        && this.now.x + x >= 0 && this.now.x + x < Room.SIZE_X // verification qu'on ne sort pas du cadre
                        && this.now.y + y >= 0 && this.now.y + y < Room.SIZE_Y
                        && !findNeighbor(this.open, node) && !findNeighbor(this.closed, node)) { // si ce n'a pas déjà été fait
                    node.g = node.parent.g + 1.; // coût Horizontal/vertical = 1.0

                    this.open.add(node); // Ajout aux noeud qui ont été déjà fait
                }
            }
        }
        Collections.sort(this.open); // on trie la liste des noeuds non explorés
    }

    /**
     *
     * @param position_player
     * @return La liste des mouvements pour atteindre le joueur
     */
    public ArrayList<Node> solve(Position position_player){
        path.clear();
        resetCharacterControllerEnemy(); // On reset ici car sinon cela creer des problèmes -> ex: L'ennemi retourne à son xStart et yStart précedement calculé (et qui n'est donc plus bon)
        findPathTo(position_player);
        return this.path;
    }

    /**
     * Déplace l'ennemi de manière aléatoire sur le terrain
     */
    public void randomMovement(){
        move(character.game.gen.randomOrientation());
    }
}
