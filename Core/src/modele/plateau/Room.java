package modele.plateau;

import modele.plateau.statics.*;
import modele.plateau.items.Item;
import modele.plateau.items.WeightedItemSupplier;
import util.Position;
import util.Util;

import java.util.List;
import java.util.Random;

public class Room {
    public static final int SIZE_X = 20;
    public static final int SIZE_Y = 10;

    private final StaticEntity[][] grid = new StaticEntity[SIZE_X][SIZE_Y];
    private Position start, exit = new Position(-1, -1);
    private boolean done;

    public StaticEntity getStatic(int x, int y){
        if (x < 0 || x >= SIZE_X || y < 0 || y >= SIZE_Y) {
            // L'entité demandée est en-dehors de la grille
            return null;
        }
        return grid[x][y];
    }

    public StaticEntity getStatic(Position pos){
        return getStatic(pos.x, pos.y);
    }

    public Position getStart(){
        return start;
    }

    public Position getExit(){
        return exit;
    }

    public boolean isDone() {
        return done;
    }

    public Room(){
        this(false);
    }

    public Room(boolean noDoor){
        this(noDoor, new Position(-1, -1));
    }

    public Room(boolean noDoor, Position start){
        this.start= start;
        if(start.x >= 0 && start.y >= 0)
            addStatic(new NormalSlot(this), start);

        // murs extérieurs horizontaux
        for (int x = 0; x < 20; x++) {
            addStatic(new Wall(this), x, 0);
            addStatic(new Wall(this), x, SIZE_Y - 1);
        }

        // murs extérieurs verticaux
        for (int y = 1; y < 9; y++) {
            addStatic(new Wall(this), 0, y);
            addStatic(new Wall(this), SIZE_X - 1, y);
        }

        StaticEntity temp;
        for (int x = 1; x < SIZE_X - 1; x++) {
            for (int y = 1; y < SIZE_Y - 1; y++) {
                if(this.start.x == x && this.start.y == y)
                    continue;

                addStatic((temp = Gen.pickEntity(this)), x, y);
                if(temp instanceof NormalSlot){
                    ((NormalSlot)temp).item = Gen.pickItem(this);
                    if(this.start.x < 0 || this.start.y < 0) {
                        this.start = new Position(x, y);
                    }
                }
            }
        }

        if(!noDoor){
            Random rand = new Random();
            if(rand.nextInt(2) == 0)
                grid[exit.x = rand.nextInt(2) == 0 ? 0 : SIZE_X - 1][exit.y = 1+ rand.nextInt(SIZE_Y - 2)] = new Door(this);
            else
                grid[exit.x = 1 + rand.nextInt(SIZE_X - 2)][exit.y = rand.nextInt(2) == 0 ? 0 : SIZE_Y - 1] = new Door(this);

            Position ns = Gen.getSlotNextToDoor(exit);
            if(!(grid[ns.x][ns.y] instanceof NormalSlot))
                grid[ns.x][ns.y] = new NormalSlot(this);
        }
    }

    public void addStatic(Class<? extends StaticEntity> type, Position position){
        addStatic(Util.Reflections.instantiate(type, this), position);
    }

    public void addStatic(StaticEntity entity, Position position){
        addStatic(entity, position.x, position.y);
    }

    public void addStatic(StaticEntity entity, int x, int y){
        grid[x][y] = entity;
    }

    public void terminate(){
        done = true;
    }

    public static final class Gen{
        private static final Random rand = new Random();

        public static List<Class<? extends StaticEntity>> entityPicker;
        public static List<Class<? extends Item>> itemPicker;

        static {
            entityPicker = new WeightedEntitiesSupplier().supply();
            itemPicker = new WeightedItemSupplier().supply();
        }

        public static StaticEntity pickEntity(Room room){
            return Util.Reflections.instantiate(entityPicker.get(rand.nextInt(entityPicker.size())), room);
        }

        public static Item pickItem(Room room){
            return Util.Reflections.instantiate(itemPicker.get(rand.nextInt(itemPicker.size())));
        }

        public static Position getSlotNextToDoor(Position doorPos){
            switch(doorPos.x){
                case 0:
                    return new Position(1, doorPos.y);
                case Room.SIZE_X - 1:
                    return new Position(SIZE_X - 2, doorPos.y);
                default:
                    switch(doorPos.y){
                        case 0:
                            return new Position(doorPos.x, 1);
                        case Room.SIZE_Y - 1:
                            return new Position(doorPos.x, SIZE_Y - 2);
                    }
            }
            return doorPos;
        }
    }
}
