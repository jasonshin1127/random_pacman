package byow.Core;

import java.io.Serializable;

public class Position implements Serializable {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean equal(Position a) {
        if (a.x != this.x || a.y != this.y) {
            return false;
        }
        return true;
    }
}
