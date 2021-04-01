package modele.plateau;

import modele.plateau.entites.Porte;
import modele.plateau.entites.WeightedEntitiesSupplier;
import modele.plateau.inventaire.Objet;
import modele.plateau.pickup.WeightedItemSupplier;
import util.Util;

import java.util.List;
import java.util.Random;

public class Salle {
    public static final int SIZE_X = 20;
    public static final int SIZE_Y = 10;

    private EntiteStatique[][] grilleEntitesStatiques = new EntiteStatique[SIZE_X][SIZE_Y];
    private int startX, startY;
    private int exitX = -1, exitY = -1;
    private boolean done;

    public EntiteStatique getEntite(int x, int y) {
        if (x < 0 || x >= SIZE_X || y < 0 || y >= SIZE_Y) {
            // L'entité demandée est en-dehors de la grille
            return null;
        }
        return grilleEntitesStatiques[x][y];
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int getExitX() {
        return exitX;
    }

    public int getExitY() {
        return exitY;
    }

    public boolean isDone() {
        return done;
    }

    public Salle(){
        this(false);
    }

    public Salle(boolean noDoor){
        this(noDoor, -1, -1);
    }

    public Salle(boolean noDoor, int startX, int startY){
        this.startX = startX;
        this.startY = startY;
        if(startX >= 0 && startY >= 0)
            addEntiteStatique(new CaseNormale(this), startX, startY);

        // murs extérieurs horizontaux
        for (int x = 0; x < 20; x++) {
            addEntiteStatique(new Mur(this), x, 0);
            addEntiteStatique(new Mur(this), x, SIZE_Y - 1);
        }

        // murs extérieurs verticaux
        for (int y = 1; y < 9; y++) {
            addEntiteStatique(new Mur(this), 0, y);
            addEntiteStatique(new Mur(this), SIZE_X - 1, y);
        }

        EntiteStatique temp;
        for (int x = 1; x < SIZE_X - 1; x++) {
            for (int y = 1; y < SIZE_Y - 1; y++) {
                if(this.startX == x && this.startY == y)
                    continue;

                addEntiteStatique((temp = Gen.pickEntity(this)), x, y);
                if(temp instanceof CaseNormale){
                    ((CaseNormale)temp).item = Gen.pickItem(this);
                    if(this.startX < 0 || this.startY < 0) {
                        this.startX = x;
                        this.startY = y;
                    }
                }
            }
        }

        if(!noDoor){
            Random rand = new Random();
            if(rand.nextInt(2) == 0)
                grilleEntitesStatiques[exitX = rand.nextInt(2) == 0 ? 0 : SIZE_X - 1][exitY = rand.nextInt(SIZE_Y - 1)] = new Porte(this);
            else
                grilleEntitesStatiques[exitX = rand.nextInt(SIZE_X - 1)][exitY = rand.nextInt(2) == 0 ? 0 : SIZE_Y - 1] = new Porte(this);

            int[] ns = Gen.getSlotNextToDoor(exitX, exitY);
            if(!(grilleEntitesStatiques[ns[0]][ns[1]] instanceof CaseNormale))
                grilleEntitesStatiques[ns[0]][ns[1]] = new CaseNormale(this);
        }
    }

    private void addEntiteStatique(EntiteStatique e, int x, int y) {
        grilleEntitesStatiques[x][y] = e;
    }

    public void terminate(){
        done = true;
    }

    public static final class Gen{
        private static final Random rand = new Random();

        public static List<Class<? extends EntiteStatique>> entityPicker;
        public static List<Class<? extends Objet>> itemPicker;

        static {
            entityPicker = new WeightedEntitiesSupplier().supply();
            itemPicker = new WeightedItemSupplier().supply();
        }

        public static EntiteStatique pickEntity(Salle salle){
            return Util.Reflections.instantiate(entityPicker.get(rand.nextInt(entityPicker.size())), salle);
        }

        public static Objet pickItem(Salle salle){
            return Util.Reflections.instantiate(itemPicker.get(rand.nextInt(itemPicker.size())));
        }

        public static int[] getSlotNextToDoor(int doorX, int doorY){
            switch(doorX){
                case 0:
                    return new int[]{1, doorY};
                case Salle.SIZE_X - 1:
                    return new int[]{SIZE_X - 2, doorY};
                default:
                    switch(doorY){
                        case 0:
                            return new int[]{doorX, 1};
                        case Salle.SIZE_Y - 1:
                            return new int[]{doorX, SIZE_Y - 2};
                    }
            }
            return new int[]{-1, -1};
        }
    }
}
