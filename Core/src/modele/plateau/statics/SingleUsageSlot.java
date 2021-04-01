package modele.plateau.statics;

import modele.plateau.Player;
import modele.plateau.Room;
import modele.plateau.items.Item;
import modele.plateau.items.WaterCap;
import modele.plateau.statics.StaticEntity;

public class SingleUsageSlot extends NormalSlot {
    protected boolean used;

    public SingleUsageSlot(Room _room) {
        super(_room);
    }

    // getter pour savoir si une case a été utilisée, si utilisée -> return true, sinon return false
    public boolean isUsed(){
        return used;
    }

    @Override
    public boolean collide(Player player){
        return used;
    }

    @Override
    public void leave(Player character) {
        super.leave(character);
        used = true;
    }

    @Override
    public boolean use(Player character, Item item) {
        if(item instanceof WaterCap){
            used = false;
            return true;
        }
        return false;
    }
}
