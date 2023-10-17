package byow.Core;

import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.ArrayList;

public class Fire implements Serializable {

    private Position myPos;


    public Fire(Position myPos, PortalRoom world) {
        this.myPos = myPos;
        world.getRoomTiles()[myPos.getX()][myPos.getY()] = Tileset.SAND;
    }

    public static ArrayList<Fire> startFire(int count, PortalRoom portalroom) {
        ArrayList<Fire> fires = new ArrayList<>();
        if ((count + 2) % 20 == 0 || count == 2) {
            Position init1 = new Position(1, 22);
            Position init2 = new Position(1, 8);
            Fire a = new Fire(init1, portalroom);
            Fire b = new Fire(init2, portalroom);
            fires.add(a);
            fires.add(b);
            return fires;
        }
        return fires;
    }

    public static void moveFire(ArrayList<Fire> fires, PortalRoom portalRoom,
                                Avatar avatar1, Avatar avatar2) {
        ArrayList<Fire> outOfbound = new ArrayList<>();
        for (Fire i : fires) {
            portalRoom.getRoomTiles()[i.myPos.getX()][i.myPos.getY()] = Tileset.FLOOR;
            portalRoom.getRoomTiles()[i.myPos.getX() + 1][i.myPos.getY()] = Tileset.SAND;
            i.myPos = new Position(i.myPos.getX() + 1, i.myPos.getY());
            if (i.myPos.equal(avatar2.getMyPos())) {
                Avatar.decrementHealth(avatar1);
            }
            if (i.myPos.getX() == 60) {
                outOfbound.add(i);
                portalRoom.getRoomTiles()[i.myPos.getX()][i.myPos.getY()] = Tileset.FLOOR;
            }
        }
        for (Fire i: outOfbound) {
            avatar2.getFires().remove(i);
        }
    }
}
