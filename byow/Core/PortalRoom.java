package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.io.Serializable;



public class PortalRoom implements Serializable {

    private static final int WIDTH = 61;
    private static final int HEIGHT = 31;
    private TETile[][] roomTiles;

    private final Position initPos = new Position(0, 30);
    private final Position finalPos = new Position(60, 0);

    private boolean isSaved = false;

    private Avatar avatar;

    private World world;

    public void setAvatar(Avatar a) {
        avatar = a;
    }


    public boolean isSaved() {
        return isSaved;
    }

    public void setIsSaved(boolean x) {
        isSaved = x;
    }

    public TETile[][] getRoomTiles() {
        return roomTiles;
    }

    public Avatar getAvatar() {
        return avatar;
    }
    public World getWorld() {
        return world;
    }

    public PortalRoom(World world) {
        this.world = world;
        this.roomTiles = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                roomTiles[x][y] = Tileset.GRASS;
            }
        }
        Position a = initPos;
        Position b = finalPos;
        for (int y = a.getY(); y >= b.getY(); y--) {
            if (y == a.getY() || y == b.getY()) {
                for (int x = a.getX(); x <= b.getX(); x++) {
                    roomTiles[x][y] = Tileset.WALL;
                }
            } else {
                for (int x = a.getX() + 1; x <= b.getX(); x++) {
                    roomTiles[x][y] = Tileset.FLOOR;
                }
                roomTiles[a.getX()][y] = Tileset.WALL;
                roomTiles[b.getX()][y] = Tileset.WALL;
            }
        }
        roomTiles[0][15] = Tileset.FLOWER;
        for (int i = 1; i < 60; i++) {
            roomTiles[i][7] = Tileset.WALL;
        }
        for (int i = 1; i < 60; i++) {
            roomTiles[i][9] = Tileset.WALL;
        }
        for (int i = 1; i < 60; i++) {
            roomTiles[i][21] = Tileset.WALL;
        }
        for (int i = 1; i < 60; i++) {
            roomTiles[i][23] = Tileset.WALL;
        }
        for (int i = 4; i < 60; i += 15) {
            hideSpaceUp(i, 23);
            hideSpaceDown(i, 7);
        }
        for (int i = 12; i < 60; i += 15) {
            createPath(i, 9);
        }
        for (int i = 1; i < 12; i++) {
            roomTiles[i][16] = Tileset.WALL;
        }
        for (int i = 1; i < 12; i++) {
            roomTiles[i][14] = Tileset.WALL;
        }
        roomTiles[12][15] = Tileset.FLOOR;
        roomTiles[0][22] = Tileset.TREE;
        roomTiles[0][8] = Tileset.TREE;
    }

    public boolean checkBound(Position a) {
        if (a.getX() >= 0 && a.getX() <= 60
                && a.getY() >= 0 && a.getY() <= 30) {
            return true;
        }
        return false;
    }

    public void hideSpaceUp(int x, int y) {
        roomTiles[x][y + 1] = Tileset.WALL;
        roomTiles[x][y + 2] = Tileset.WALL;
        roomTiles[x + 3][y + 1] = Tileset.WALL;
        roomTiles[x + 3][y + 2] = Tileset.WALL;
        roomTiles[x + 1][y] = Tileset.FLOOR;
        roomTiles[x + 2][y] = Tileset.FLOOR;
        for (int i = 0; i < 4; i++) {
            roomTiles[x + i][y + 3] = Tileset.WALL;
        }
    }

    public void hideSpaceDown(int x, int y) {
        roomTiles[x][y - 1] = Tileset.WALL;
        roomTiles[x][y - 2] = Tileset.WALL;
        roomTiles[x + 3][y - 1] = Tileset.WALL;
        roomTiles[x + 3][y - 2] = Tileset.WALL;
        roomTiles[x + 1][y] = Tileset.FLOOR;
        roomTiles[x + 2][y] = Tileset.FLOOR;
        for (int i = 0; i < 4; i++) {
            roomTiles[x + i][y - 3] = Tileset.WALL;
        }
    }

    public void createPath(int x, int y) {
        roomTiles[x + 1][y] = Tileset.FLOOR;
        roomTiles[x + 2][y] = Tileset.FLOOR;
        roomTiles[x + 1][y + 12] = Tileset.FLOOR;
        roomTiles[x + 2][y + 12] = Tileset.FLOOR;
        for (int i = 1; i < 12; i++) {
            roomTiles[x][y + i] = Tileset.WALL;
        }
        for (int i = 1; i < 12; i++) {
            roomTiles[x + 3][y + i] = Tileset.WALL;
        }
    }

}
