package meta.events;

import model.Character;

public abstract class CharacterEvent extends Event{
    public final Character character;

    public CharacterEvent(Character character){
        this.character = character;
    }
}
