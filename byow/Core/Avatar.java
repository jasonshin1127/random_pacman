package byow.Core;

import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.io.Serializable;
import java.util.ArrayList;

public class Avatar implements Serializable {

    private Position myPos;

    private World world;

    private PortalRoom portalroom;

    private boolean isTrapped = false;

    private int health = 1;

    private String name;

    private int countMove = 0;

    private ArrayList<Fire> fires = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealth() {
        return health;
    }

    public void setWorld(World world) {
        this.world = world;
    }


    public ArrayList<Fire> getFires() {
        return fires;
    }

    public void incrementHealth() {
        health += 1;
    }

    public static void decrementHealth(Avatar avatar) {
        avatar.health -= 1;
    }

    public Avatar(Position myPos, World world) {
        this.myPos = myPos;
        this.world = world;
        world.getWorldTiles()[myPos.getX()][myPos.getY()] = Tileset.AVATAR;
    }
    public Avatar(Position myPos, PortalRoom world) {
        this.myPos = myPos;
        this.portalroom = world;
        portalroom.setAvatar(this);
        world.getRoomTiles()[myPos.getX()][myPos.getY()] = Tileset.AVATAR;
    }

    public boolean getIsTrapped() {
        return isTrapped;
    }

    public boolean isWall(Position position) {
        return world.getWorldTiles()[position.getX()][position.getY()] == Tileset.WALL;
    }

    public void checkTrapped() {
        Position up = new Position(myPos.getX(), myPos.getY() + 1);
        Position down = new Position(myPos.getX(), myPos.getY() - 1);
        Position left = new Position(myPos.getX() - 1, myPos.getY());
        Position right = new Position(myPos.getX() + 1, myPos.getY());
        if (isWall(up) && isWall(left) && isWall(right) && isWall(down)) {
            isTrapped = true;
        }
    }



    public Position getMyPos() {
        return myPos;
    }

    public void checkCoin() {
        for (Position p : world.getCoins()) {
            if (p.equal(myPos)) {
                incrementHealth();
                world.getCoins().remove(myPos);
                return;
            }
        }
    }

    public boolean move(char s) {
        if (s == 'W' || s == 'w') {
            if (world.getWorldTiles()[myPos.getX()][myPos.getY() + 1]
                    .description().equals("wall")) {
                decrementHealth(this);
                return false;
            } else {
                world.getWorldTiles()[myPos.getX()][myPos.getY() + 1] = Tileset.AVATAR;
                world.getWorldTiles()[myPos.getX()][myPos.getY()] = Tileset.WALL;
                this.myPos = new Position(myPos.getX(), myPos.getY() + 1);
                return true;
            }
        }
        if (s == 'S' || s == 's') {
            TETile temp = world.getWorldTiles()[myPos.getX()][myPos.getY() - 1];
            if (temp.description().equals("wall")) {
                return true;
            }
            if (world.getWorldTiles()[myPos.getX()][myPos.getY() - 1].
                    description().equals("wall")) {
                decrementHealth(this);
                return false;
            } else {
                world.getWorldTiles()[myPos.getX()][myPos.getY() - 1] = Tileset.AVATAR;
                world.getWorldTiles()[myPos.getX()][myPos.getY()] = Tileset.WALL;
                this.myPos = new Position(myPos.getX(), myPos.getY() - 1);
                return true;
            }
        }
        if (s == 'D' || s == 'd') {
            if (world.getWorldTiles()[myPos.getX() + 1][myPos.getY()].
                    description().equals("wall")) {
                decrementHealth(this);
                return false;
            } else {
                world.getWorldTiles()[myPos.getX() + 1][myPos.getY()] = Tileset.AVATAR;
                world.getWorldTiles()[myPos.getX()][myPos.getY()] = Tileset.WALL;
                this.myPos = new Position(myPos.getX() + 1, myPos.getY());
                return true;
            }
        }
        if (s == 'A' || s == 'a') {
            if (world.getWorldTiles()[myPos.getX() - 1][myPos.getY()].
                    description().equals("wall")) {
                decrementHealth(this);
                return false;
            } else {
                world.getWorldTiles()[myPos.getX() - 1][myPos.getY()] = Tileset.AVATAR;
                world.getWorldTiles()[myPos.getX()][myPos.getY()] = Tileset.WALL;
                this.myPos = new Position(myPos.getX() - 1, myPos.getY());
                return true;
            }
        }
        return false;
    }

    public boolean portalMove(char s) {
        if (s == 'W' || s == 'w') {
            if (portalroom.getRoomTiles()[myPos.getX()][myPos.getY() + 1].
                    description().equals("wall")) {
                return false;
            } else {
                portalroom.getRoomTiles()[myPos.getX()][myPos.getY() + 1] = Tileset.AVATAR;
                portalroom.getRoomTiles()[myPos.getX()][myPos.getY()] = Tileset.FLOOR;
                this.myPos = new Position(myPos.getX(), myPos.getY() + 1);
                countMove += 1;
                fires.addAll(Fire.startFire(countMove, portalroom));
                Fire.moveFire(fires, portalroom, portalroom.
                        getWorld().getAvatar(), portalroom.getAvatar());
                return true;
            }
        }
        if (s == 'S' || s == 's') {
            if (portalroom.getRoomTiles()[myPos.getX()][myPos.getY() - 1].
                    description().equals("wall")) {
                return false;
            } else {
                portalroom.getRoomTiles()[myPos.getX()][myPos.getY() - 1] = Tileset.AVATAR;
                portalroom.getRoomTiles()[myPos.getX()][myPos.getY()] = Tileset.FLOOR;
                this.myPos = new Position(myPos.getX(), myPos.getY() - 1);
                countMove += 1;
                fires.addAll(Fire.startFire(countMove, portalroom));
                Fire.moveFire(fires, portalroom, portalroom.getWorld().
                        getAvatar(), portalroom.getAvatar());
                return true;
            }
        }
        if (s == 'D' || s == 'd') {
            if (portalroom.getRoomTiles()[myPos.getX() + 1][myPos.getY()].
                    description().equals("wall")) {
                return false;
            } else {
                portalroom.getRoomTiles()[myPos.getX() + 1][myPos.getY()] = Tileset.AVATAR;
                portalroom.getRoomTiles()[myPos.getX()][myPos.getY()] = Tileset.FLOOR;
                this.myPos = new Position(myPos.getX() + 1, myPos.getY());
                countMove += 1;
                fires.addAll(Fire.startFire(countMove, portalroom));
                Fire.moveFire(fires, portalroom, portalroom.getWorld().
                        getAvatar(), portalroom.getAvatar());
                return true;
            }
        }
        if (s == 'A' || s == 'a') {
            if (portalroom.getRoomTiles()[myPos.getX() - 1][myPos.getY()].
                    description().equals("wall")) {
                return false;
            } else {
                portalroom.getRoomTiles()[myPos.getX() - 1][myPos.getY()] = Tileset.AVATAR;
                portalroom.getRoomTiles()[myPos.getX()][myPos.getY()] = Tileset.FLOOR;
                this.myPos = new Position(myPos.getX() - 1, myPos.getY());
                countMove += 1;
                fires.addAll(Fire.startFire(countMove, portalroom));
                Fire.moveFire(fires, portalroom,
                        portalroom.getWorld().getAvatar(), portalroom.getAvatar());
                return true;
            }
        }
        return true;
    }
}
