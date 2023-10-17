package byow.TileEngine;

import java.awt.Color;

/**
 * Contains constant tile objects, to avoid having to remake the same tiles in different parts of
 * the code.
 *
 * You are free to (and encouraged to) create and add your own tiles to this file. This file will
 * be turned in with the rest of your code.
 *
 * Ex:
 *      world[x][y] = Tileset.FLOOR;
 *
 * The style checker may crash when you try to style check this file due to use of unicode
 * characters. This is OK.
 */

public class Tileset {
    public static final TETile AVATAR = new TETile('@', Color.white, Color.black, "you",
            "/Users/chloeliu/Desktop/cs61bl/su22-p79/proj3/byow/textures/ourMario.png");
    public static final TETile WALL = new TETile('#', new Color(201, 99, 99), Color.darkGray,
            "wall", "/Users/chloeliu/Desktop/cs61bl/su22-p79/proj3/byow/textures/wall_small.png");
    public static final TETile FLOOR = new TETile('·', new Color(128, 192, 128), Color.black,
            "floor", "/Users/chloeliu/Desktop/cs61bl/su22-p79/proj3/byow/textures/grass.png");
    public static final TETile NOTHING = new TETile(' ', Color.black, Color.black, "nothing");
    public static final TETile GRASS = new TETile('"', Color.lightGray, Color.black, "sea",
           "/Users/chloeliu/Desktop/cs61bl/su22-p79/proj3/byow/textures/sea.png");
    public static final TETile WATER = new TETile('≈', Color.blue, Color.black, "health",
            "/Users/chloeliu/Desktop/cs61bl/su22-p79/proj3/byow/textures/red cross.png");
    public static final TETile FLOWER = new TETile('❀', Color.magenta, Color.pink, "exit",
            "/Users/chloeliu/Desktop/cs61bl/su22-p79/proj3/byow/textures/door.png");
    public static final TETile LOCKED_DOOR = new TETile('█', Color.orange, Color.black,
            "locked door");
    public static final TETile UNLOCKED_DOOR = new TETile('▢', Color.orange, Color.black,
            "unlocked door");
    public static final TETile SAND = new TETile('▒', Color.yellow, Color.black, "fire",
            "/Users/chloeliu/Desktop/cs61bl/su22-p79/proj3/byow/textures/real fire.png");
    public static final TETile MOUNTAIN = new TETile('▲', Color.gray, Color.black, "locked door",
            "/Users/chloeliu/Desktop/cs61bl/su22-p79/proj3/byow/textures/door.png");
    public static final TETile TREE = new TETile('♠', Color.green, Color.black, "cannon",
            "/Users/chloeliu/Desktop/cs61bl/su22-p79/proj3/byow/textures/cannon .png");

}


