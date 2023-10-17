package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class World implements Serializable {

    private static final int WIDTH = 90;
    private static final int HEIGHT = 45;

    private Random random;

    private TETile[][] worldTiles;
    private ArrayList<Position> walls;
    private ArrayList<Position> floors;

    private ArrayList<Position> coins;

    private Avatar avatar;
    private Position portalEntry;
    private PortalRoom portalRoom;


    public World(long seed) {
        this.worldTiles = new TETile[WIDTH][HEIGHT];
        this.walls = new ArrayList<>();
        this.floors = new ArrayList<Position>();
        this.random = new Random(seed);
        this.portalRoom = new PortalRoom(this);
    }

    public Position getPortalEntry() {
        return portalEntry;
    }

    public ArrayList<Position> getCoins() {
        return coins;
    }

    public PortalRoom getPortalRoom() {
        return portalRoom;
    }



    public Position setAvatar() {
        int randomIndex = random.nextInt(floors.size());
        Iterator<Position> iterator = floors.iterator();
        int currIndex = 0;
        Position randomElement = new Position(0, 0);
        while (iterator.hasNext()) {
            randomElement = iterator.next();
            if (currIndex == randomIndex) {
                break;
            }
            currIndex++;
        }
        floors.remove(randomElement);
        return randomElement;
    }

    public void placeAvatar(Avatar a) {
        this.avatar = a;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void createPortal() {
        int randomIndex = random.nextInt(floors.size());
        Iterator<Position> iterator = floors.iterator();
        int currIndex = 0;
        Position randomElement = new Position(0, 0);
        while (iterator.hasNext()) {
            randomElement = iterator.next();
            if (currIndex == randomIndex) {
                break;
            }
            currIndex++;
        }
        worldTiles[randomElement.getX()][randomElement.getY()] = Tileset.MOUNTAIN;
        portalEntry = randomElement;
        floors.remove(randomElement);
    }

    public void createCoins() {
        coins = new ArrayList<>();
        for (int i = 0; i <= 7; i++) {
            int randomIndex = random.nextInt(floors.size());
            Iterator<Position> iterator = floors.iterator();
            int currIndex = 0;
            Position randomElement = new Position(0, 0);
            while (iterator.hasNext()) {
                randomElement = iterator.next();
                if (currIndex == randomIndex) {
                    break;
                }
                currIndex++;
            }
            worldTiles[randomElement.getX()][randomElement.getY()] = Tileset.WATER;
            coins.add(randomElement);
            floors.remove(randomElement);
        }
    }


    public TETile[][] getWorldTiles() {
        return this.worldTiles;
    }

    /** write methods that fill the 2D tile arrays of my world **/
    public void fillBlanks() {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                worldTiles[x][y] = Tileset.GRASS;
            }
        }
    }
    /** draw a closed rectangular room, fill it with walls and floors **
     * takes in two Position, a must be the left up point and b must be the right down point**/
    public void buildRoom(Position a, Position b) {
        assert (a.getX() < b.getX() && a.getY() > b.getY());
        for (int y = a.getY(); y >= b.getY(); y--) {
            if (y == a.getY() || y == b.getY()) {
                for (int x = a.getX(); x <= b.getX(); x++) {
                    worldTiles[x][y] = Tileset.WALL;
                    walls.add(new Position(x, y));
                }
            } else {
                for (int x = a.getX() + 1; x < b.getX(); x++) {
                    worldTiles[x][y] = Tileset.FLOOR;
                    floors.add(new Position(x, y));
                }
                worldTiles[a.getX()][y] = Tileset.WALL;
                walls.add(new Position(a.getX(), y));
                worldTiles[b.getX()][y] = Tileset.WALL;
                walls.add(new Position(b.getX(), y));
            }
        }
    }

    public void buildHall(Position a, Position b) {
        assert (a.getX() < b.getX() || a.getY() < b.getY());
        // we build a hall vertically //
        if (a.getX() == b.getX()) {
            for (int y = a.getY(); y <= b.getY(); y++) {
                worldTiles[a.getX()][y] = Tileset.FLOOR;
                floors.add(new Position(a.getX(), y));
                worldTiles[a.getX() - 1][y] = Tileset.WALL;
                walls.add(new Position(a.getX() - 1, y));
                worldTiles[a.getX() + 1][y] = Tileset.WALL;
                walls.add(new Position(a.getX() + 1, y));
            }
        }
        // we build a hall horizontally //
        if (a.getY() == b.getY()) {
            for (int x = a.getX(); x <= b.getX(); x++) {
                worldTiles[x][a.getY()] = Tileset.FLOOR;
                floors.add(new Position(x, a.getY()));
                worldTiles[x][a.getY() - 1] = Tileset.WALL;
                walls.add(new Position(x, a.getY() - 1));
                worldTiles[x][a.getY() + 1] = Tileset.WALL;
                walls.add(new Position(x, a.getY() + 1));
            }
        }
    }



    /** generate a valid position from existing walls **/
    public Position generatePos(Random variable) {
        int randomIndex = variable.nextInt(walls.size());
        Iterator<Position> iterator = walls.iterator();
        int currIndex = 0;
        Position randomElement = new Position(0, 0);
        while (iterator.hasNext()) {
            randomElement = iterator.next();
            if (currIndex == randomIndex) {
                break;
            }
            currIndex++;
        }
        while (!checkBound(randomElement) || checkCorner(randomElement)) {
            return generatePos(variable);
        }
        return randomElement;
    }

    /** check if a point is a valid starting position for either drawing hallways for rooms **/
    public boolean checkBound(Position a) {
        if (a.getX() > 5 && a.getX() < 85
                && a.getY() > 5 && a.getY() < 35) {
            return true;
        }
        return false;
    }

    public boolean checkBound2(Position a) {
        if (a.getX() > 0 && a.getX() < 89
                && a.getY() > 0 && a.getY() < 40) {
            return true;
        }
        return false;
    }

    public boolean checkCorner(Position a) {
        int numEmpty = 0;
        int numWalls = 0;
        if (worldTiles[a.getX() - 1][a.getY()] == Tileset.GRASS) {
            numEmpty += 1;
        }
        if (worldTiles[a.getX() + 1][a.getY()] == Tileset.GRASS) {
            numEmpty += 1;
        }
        if (worldTiles[a.getX()][a.getY() - 1] == Tileset.GRASS) {
            numEmpty += 1;
        }
        if (worldTiles[a.getX()][a.getY() + 1] == Tileset.GRASS) {
            numEmpty += 1;
        }
        if (worldTiles[a.getX() - 1][a.getY()] == Tileset.WALL) {
            numWalls += 1;
        }
        if (worldTiles[a.getX() + 1][a.getY()] == Tileset.WALL) {
            numWalls += 1;
        }
        if (worldTiles[a.getX()][a.getY() - 1] == Tileset.WALL) {
            numWalls += 1;
        }
        if (worldTiles[a.getX()][a.getY() + 1] == Tileset.WALL) {
            numWalls += 1;
        }
        if (numEmpty == 2 || numWalls == 3) {
            return true;
        }
        return false;
    }


    /** take in a valid position and check which direction we can build more rooms/hallways **/
    /** 1 west ; 0 east; 3 north; 2 south **/
    public int checkDir(Position a) {
        assert (checkBound(a));
        if (worldTiles[a.getX()][a.getY() + 1].equals(Tileset.GRASS)) {
            return 3;
        }
        if (worldTiles[a.getX()][a.getY() - 1].equals(Tileset.GRASS)) {
            return 2;
        }
        if (worldTiles[a.getX() - 1][a.getY()].equals(Tileset.GRASS)) {
            return 1;
        }
        if (worldTiles[a.getX() + 1][a.getY()].equals(Tileset.GRASS)) {
            return 0;
        } else {
            return -1;
        }
    }

    /** check if there are existing walls or floors alreay built **/
    public boolean areaCheck(Position a, Position b) {
        assert (a.getX() < b.getX() && a.getY() > b.getY());
        for (int x = a.getX(); x <= b.getX(); x++) {
            for (int y = a.getY(); y >= b.getY(); y--) {
                if (worldTiles[x][y].equals(Tileset.WALL)
                        || worldTiles[x][y].equals(Tileset.FLOOR)) {
                    return false;
                }
            }
        }
        return true;
    }

    /** produces leftup and rightdown point of the box **/
    public Position[] getRoomPos(Position a) {
        int width = 0;
        int height = 0;
        Position leftup = new Position(0, 0);
        Position rightdown = new Position(0, 0);
        Position[] result = new Position[2];
        while (width < 4 || height < 4 || width > 15 || height > 12) {
            width = RandomUtils.poisson(random, 7);
            height = RandomUtils.poisson(random, 4);
        }
        if (checkDir(a) < 0) {
            return null;
        } else {
            if (checkDir(a) == 0 || checkDir(a) == 1) {
                int up = RandomUtils.uniform(random, 2, height);
                int down = height - up;
                if (checkDir(a) == 0) {
                    leftup = new Position(a.getX(), a.getY() + up);
                    rightdown = new Position(a.getX() + width, a.getY() - down);
                    if (!checkBound2(leftup)
                            || !checkBound2(rightdown)
                            || !areaCheck(new Position(leftup.getX()
                            + 1, leftup.getY()), rightdown)) {
                        return null;
                    }
                }
                if (checkDir(a) == 1) {
                    leftup = new Position(a.getX() - width, a.getY() + up);
                    rightdown = new Position(a.getX(), a.getY() - down);
                    if (!checkBound2(leftup)
                            || !checkBound2(rightdown)
                            || !areaCheck(leftup, new Position(rightdown.getX()
                            - 1, rightdown.getY()))) {
                        return null;
                    }
                }
            }
            if (checkDir(a) == 2 || checkDir(a) == 3) {
                int left = RandomUtils.uniform(random, 2, width);
                int right = width - left;
                if (checkDir(a) == 2) {
                    leftup = new Position(a.getX() - left, a.getY());
                    rightdown = new Position(a.getX() + right, a.getY() - height);
                    if (!checkBound2(leftup)
                            || !checkBound2(rightdown)
                            || !areaCheck(new Position(leftup.getX(), leftup.getY()
                            - 1), rightdown)) {
                        return null;
                    }
                }
                if (checkDir(a) == 3) {
                    leftup = new Position(a.getX() - left, a.getY() + height);
                    rightdown = new Position(a.getX() + right, a.getY());
                    if (!checkBound2(leftup)
                            || !checkBound2(rightdown)
                            || !areaCheck(leftup, new Position(rightdown.getX(), rightdown.getY()
                            + 1))) {
                        return null;
                    }
                }
            }
            if (leftup.getX() != 0 && leftup.getY() != 0
                    && rightdown.getX() != 0 && rightdown.getY() != 0) {
                result[0] = leftup;
                result[1] = rightdown;
                return result;
            } else {
                return null;
            }
        }
    }

    /** produces centerdown and centerup point of the box **/
    public Position[] getHallPos(Position a) {
        int length = 0;
        Position leftordown = new Position(0, 0);
        Position rightorup = new Position(0, 0);
        Position end = new Position(0, 0);
        Position[] result = new Position[3];
        while (length < 2 || length > 10) {
            length = RandomUtils.poisson(random, 4);
        }
        if (checkDir(a) < 0) {
            return null;
        } else {
            if (checkDir(a) == 0) {
                leftordown = a;
                rightorup = new Position(a.getX() + length, a.getY());
                end = new Position(a.getX() + length, a.getY());
                Position x = new Position(leftordown.getX() + 1, leftordown.getY() + 1);
                Position y = new Position(rightorup.getX(), rightorup.getY() - 1);
                if (!checkBound2(x) || !checkBound2(y) || !areaCheck(x, y)) {
                    return null;
                }
            }
            if (checkDir(a) == 1) {
                leftordown = new Position(a.getX() - length, a.getY());
                rightorup = a;
                end = new Position(a.getX() - length, a.getY());
                Position x = new Position(leftordown.getX(), leftordown.getY() + 1);
                Position y = new Position(rightorup.getX() - 1, rightorup.getY() - 1);
                if (!checkBound2(x) || !checkBound2(y) || !areaCheck(x, y)) {
                    return null;
                }
            }
            if (checkDir(a) == 2) {
                leftordown = new Position(a.getX(), a.getY() - length);
                rightorup = a;
                end = new Position(a.getX(), a.getY() - length);
                Position x = new Position(rightorup.getX() - 1, rightorup.getY() - 1);
                Position y = new Position(leftordown.getX() + 1, leftordown.getY());
                if (!checkBound2(x) || !checkBound2(y) || !areaCheck(x, y)) {
                    return null;
                }
            }
            if (checkDir(a) == 3) {
                leftordown = a;
                rightorup = new Position(a.getX(), a.getY() + length);
                end = new Position(a.getX(), a.getY() + length);
                Position x = new Position(rightorup.getX() - 1, rightorup.getY());
                Position y = new Position(leftordown.getX() + 1, leftordown.getY() + 1);
                if (!checkBound2(x) || !checkBound2(y) || !areaCheck(x, y)) {
                    return null;
                }
            }

            result[0] = leftordown;
            result[1] = rightorup;
            result[2] = end;
            return result;

        }
    }

    public void drawHUD() {
        TERenderer ter = new TERenderer();
        ter.initialize(60, 30);

    }



    /** draw the first room at a random spot with random width and length and random direction **/
    public void drawFirstRoom() {
        int x = 45;
        int y = 25;
        worldTiles[x][y] = Tileset.WALL;
        Position firstwall = new Position(x, y);
        Position[] firstpos = getRoomPos(firstwall);
        buildRoom(firstpos[0], firstpos[1]);
    }

    public void generateWorld() {
        fillBlanks();
        drawFirstRoom();
        while (floors.size() < 800) {
            Boolean headcoin = RandomUtils.bernoulli(random, 0.45);
            Position a = generatePos(random);
            if (headcoin) {
                Position[] positoins = getRoomPos(a);
                if (positoins == null) {
                    continue;
                } else {
                    buildRoom(positoins[0], positoins[1]);
                    worldTiles[a.getX()][a.getY()] = Tileset.FLOOR;
                    walls.remove(a);
                    floors.add(a);
                }
            } else {
                Position[] positions = getHallPos(a);
                if (positions == null) {
                    continue;
                } else {
                    buildHall(positions[0], positions[1]);
                    if (positions[0].equal(a)) {
                        Position[] newroom = getRoomPos(positions[1]);
                        if (newroom == null) {
                            worldTiles[positions[1].getX()][positions[1].getY()] = Tileset.WALL;
                            floors.remove(positions[1]);
                            walls.add(positions[1]);
                        } else {
                            buildRoom(newroom[0], newroom[1]);
                            worldTiles[positions[1].getX()][positions[1].getY()] = Tileset.FLOOR;
                            floors.add(positions[1]);
                            walls.remove(positions[1]);
                        }
                    } else {
                        Position[] newroom = getRoomPos(positions[0]);
                        if (newroom == null) {
                            worldTiles[positions[0].getX()][positions[0].getY()] = Tileset.WALL;
                            floors.remove(positions[0]);
                            walls.add(positions[0]);
                        } else {
                            buildRoom(newroom[0], newroom[1]);
                            worldTiles[positions[0].getX()][positions[0].getY()] = Tileset.FLOOR;
                            floors.add(positions[0]);
                            walls.remove(positions[0]);
                        }
                    }
                }
            }
        }
    }
}
