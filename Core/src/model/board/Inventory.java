package model.board;

import model.board.items.Item;
import util.Util;

import java.util.ArrayList;
import java.util.Iterator;

public class Inventory {
    public final ArrayList<Item> items = new ArrayList<>();

    public Inventory(){

    }

    public int count(Class<? extends Item> type){
        int temp = 0;
        for(Item item : items){
            if(item.getClass().equals(type))
                temp++;
        }

        return temp;
    }

    public boolean has(Class<? extends Item> type){
        return firstOf(type) != null;
    }

    public <T extends Item> T firstOf(Class<T> type){
        for(Item item : items){
            if(item.getClass().equals(type))
                return (T) item;
        }

        return null;
    }

    public void add(Item item){
        items.add(item);
    }

    public void add(Class<? extends Item> type, int count){
        for (int i = 0; i < count; i++)
            add(Util.Reflections.instantiate(type));
    }

    public void remove(Item item){
        items.remove(item);
    }

    public void remomoveAllOf(Class<? extends Item> type){
        items.removeIf(obj -> obj.getClass().equals(type));
    }

    public void removeOneOf(Class<? extends Item> type){
        Iterator<Item> it = items.listIterator();
        while(it.hasNext()){
            if(it.next().getClass().equals(type)){
                it.remove();
                return;
            }
        }
    }

    public void into(Inventory another){
        another.items.addAll(items);
    }
}
