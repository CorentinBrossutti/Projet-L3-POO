package model.board.statics;

import model.board.items.Key;

public interface Lockable {
    public boolean isLocked();

    public void unlock(Key key);
}
