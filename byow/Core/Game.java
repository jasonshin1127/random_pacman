package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import edu.princeton.cs.algs4.StdDraw;
import java.io.File;
import java.io.IOException;

import java.awt.*;
import java.io.Serializable;

public class Game implements Serializable {

    private World world;
    private static int width = 90;
    private static int height = 50;

    public static final File CWD = new File(System.getProperty("user.dir"));
    private static File savedGame = Utils.join(CWD, "saved_game.txt");
    private static String seed;

    private static boolean isGameOver = false;
    private static boolean isGameCreated = false;

    private static boolean isNameCreated = false;

    private static boolean isGameSaved = false;

    private static TERenderer ter = new TERenderer();

    public boolean isGameOver() {
        return isGameOver;
    }

    public boolean isGameCreated() {
        return isGameCreated;
    }

    public boolean isNameCreated() {
        return isNameCreated;
    }

    public void setIsGameCreated(boolean x) {
        this.isGameCreated = x;
    }

    public void setIsNameCreated(boolean x) {
        this.isNameCreated = x;
    }

    public void setIsGameOver(boolean x) {
        this.isGameOver = x;
    }

    public Game() {
        seed = "";
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();
    }

    public String getSeed() {
        return seed;
    }

    public World getWorld() {
        return world;
    }

    public void init() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(fontBig);
        StdDraw.text(width / 2, height / 2 + 5, "CS61B: Welcome to our world");
        Font fontMedium = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(fontMedium);
        StdDraw.text(width / 2, height / 2, "Press N : start a new game");
        StdDraw.text(width / 2, height / 2 - 3, "Press L : load your old game");
        StdDraw.text(width / 2, height / 2 - 6, "Press :Q : save and quit the game");
        StdDraw.show();
    }


    public void drawFrame(String s, double x, double y) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 50);
        StdDraw.setFont(fontBig);
        StdDraw.text(x, y, s);
    }

    public void drawFrame2(String s) {
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        Font fontBig = new Font("Monaco", Font.BOLD, 40);
        StdDraw.setFont(fontBig);
        StdDraw.text(37, 19, s);
    }



    public String createName() {
        String name = "";
        while (!isNameCreated()) {
            if (StdDraw.hasNextKeyTyped()) {
                char curr = StdDraw.nextKeyTyped();
                if (curr != '/') {
                    name += curr;
                    drawFrame(name, width / 2, height / 2);
                    StdDraw.show();
                }
                if (curr == '/') {
                    setIsNameCreated(true);
                }
            }
        }
        return name;
    }

    public void createSeed(char curr) {
        if (curr == 'N' || curr == 'n') {
            drawFrame("Please Enter a Seed!", width / 2, height / 2);
            StdDraw.show();
        } else if (Character.isDigit(curr)) {
            seed += curr;
            drawFrame(seed, width / 2, height / 2);
            StdDraw.show();
        } else if (curr == 'S' || curr == 's') {
            setIsGameCreated(true);
            drawFrame("Your Name Here:", width / 2, height / 2);
            StdDraw.show();
        }
    }

    public void saveGame(char curr) throws IOException {
        if (curr == ':') {
            while (!isGameSaved) {
                if (StdDraw.hasNextKeyTyped()) {
                    if (StdDraw.nextKeyTyped() == 'q' || StdDraw.nextKeyTyped() == 'Q') {
                        Utils.writeObject(savedGame, this);
                        isGameSaved = true;
                        if (getWorld().getAvatar().getMyPos().
                                equal(getWorld().getPortalEntry())) {
                            drawFrame2("Your game is saved, see you next time!");
                        } else {
                            drawFrame("Your game is saved, see you next time!",
                                    width / 2, height / 2);
                        }
                        StdDraw.show();
                        StdDraw.pause(2000);
                        System.exit(0);
                    }
                }
            }

        }
    }

    public static Game loadGame(char curr) {
        if (curr == 'L' || curr == 'l') {
            Game game = Utils.readObject(savedGame, Game.class);
            return game;
        }
        return null;
    }


    public void createWorld(String givenseed, String name) {
        world = new World(Long.parseLong(givenseed));
        world.fillBlanks();
        world.generateWorld();
        Position avatarPos = world.setAvatar();
        Avatar avatar = new Avatar(avatarPos, world);
        avatar.setName(name);
        world.placeAvatar(avatar);
        world.createPortal();
        world.createCoins();
        ter.initialize(width, height);
        ter.renderFrame(world.getWorldTiles());
        StdDraw.show();
    }


    public void moveAvatarCheck(char curr) {
        Avatar avatar = world.getAvatar();
        if (avatar.move(curr)) {
            avatar.checkCoin();
            avatar.checkTrapped();
        }
        if (avatar.getIsTrapped() && (!avatar.getMyPos().equal(world.getPortalEntry()))
                || avatar.getHealth() <= 0) {
            setIsGameOver(true);
        }
        ter.renderFrame(world.getWorldTiles());
    }

    public void detectMouse(int x, int y) {
        int mouseX = (int) StdDraw.mouseX();
        int mouseY = (int) StdDraw.mouseY();
        ter.renderFrame(world.getWorldTiles());
        if ((StdDraw.mouseX() != mouseX || StdDraw.mouseX() != mouseY)) {
            mouseX = (int) StdDraw.mouseX();
            mouseY = (int) StdDraw.mouseY();
            Position mousepos = new Position((int) Math.round(mouseX), (int) Math.round(mouseY));
            if (world.checkBound2(mousepos)) {
                TETile figure = world.getWorldTiles()[mousepos.getX()][mousepos.getY()];
                StdDraw.text(x, y, figure.description());
            } else {
                StdDraw.text(x, y, "outside the game");
            }
        }
    }

    public void detectMousePortal(int x, int y) {
        int mouseX = (int) StdDraw.mouseX();
        int mouseY = (int) StdDraw.mouseY();
        ter.renderFrame(world.getPortalRoom().getRoomTiles());
        if ((StdDraw.mouseX() != mouseX || StdDraw.mouseX() != mouseY)) {
            mouseX = (int) StdDraw.mouseX() - 5;
            mouseY = (int) StdDraw.mouseY() - 1;
            Position mousepos = new Position((int) Math.round(mouseX), (int) Math.round(mouseY));
            if (world.getPortalRoom().checkBound(mousepos)) {
                TETile figure = world.getPortalRoom().
                        getRoomTiles()[mousepos.getX()][mousepos.getY()];
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.setFont(new Font("Monaco", Font.BOLD, 15));
                StdDraw.text(x, y, figure.description());
            } else {
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.setFont(new Font("Monaco", Font.BOLD, 15));
                StdDraw.text(x, y, "outside the game");
            }
        }
    }

    public void checkWin() {
        Avatar avatar = world.getAvatar();
        if (avatar.getIsTrapped() && (!avatar.getMyPos().equal(world.getPortalEntry()))
                || avatar.getHealth() <= 0) {
            if (world.getPortalRoom().isSaved()) {
                drawFrame2("game over :/");
                StdDraw.show();
            } else {
                drawFrame("game over :/", width / 2, height / 2);
                StdDraw.show();
            }
        } else {
            drawFrame("YOU WIN!", width / 2, height / 2);
            StdDraw.show();
        }
    }

    public static void loadPortal(Game game) throws IOException {
        PortalRoom portal = game.getWorld().getPortalRoom();
        ter.initialize(70, 35, 5, 1);
        World world = game.getWorld();
        Avatar avatar2 = game.getWorld().getPortalRoom().getAvatar();
        while (!portal.isSaved()) {
            ter.renderFrame(portal.getRoomTiles());
            game.detectMousePortal(55, 33);
            StdDraw.setPenColor(Color.WHITE);
            StdDraw.setFont(new Font("Monaco", Font.BOLD, 15));
            StdDraw.text(10, 33, "health : " + String.valueOf(world.getAvatar().getHealth()));
            String nameHUD = world.getAvatar().getName() + " is playing";
            StdDraw.text(35, 33, nameHUD);
            StdDraw.show();
            if (StdDraw.hasNextKeyTyped()) {
                char curr = StdDraw.nextKeyTyped();
                game.saveGame(curr);
                if (!avatar2.portalMove(curr)) {
                    Avatar.decrementHealth(world.getAvatar());
                }
                if (world.getAvatar().getHealth() == 0) {
                    game.setIsGameOver(true);
                    portal.setIsSaved(true);
                } else {
                    ter.renderFrame(portal.getRoomTiles());
                    StdDraw.show();
                }
            }
            /** check if we have escaped the portal room **/
            if (avatar2.getMyPos().equal(new Position(0, 15))) {
                portal.setIsSaved(true);
                ter.initialize(width, height);
                ter.renderFrame(world.getWorldTiles());
                while (!isGameOver) {
                    if (StdDraw.hasNextKeyTyped()) {
                        world.getAvatar().move(StdDraw.nextKeyTyped());
                    }
                    ter.renderFrame(world.getWorldTiles());
                    StdDraw.setPenColor(Color.WHITE);
                    StdDraw.setFont(new Font("Monaco", Font.BOLD, 15));
                    StdDraw.text(width / 2, 48, "MOVE ONE MORE STEP!");
                    StdDraw.show();
                    if (StdDraw.hasNextKeyTyped()) {
                        game.setIsGameOver(true);
                    }
                }
            }
        }
    }

    public void enterPortal() throws IOException {
        if (world.getAvatar().getMyPos().equal(world.getPortalEntry())) {
            drawFrame("Escape from this secret game!", width / 2, height / 2);
            StdDraw.show();
            StdDraw.pause(2000);
            ter.initialize(70, 35, 5, 1);
            Avatar avatar2 = new Avatar(new Position(59, 15), world.getPortalRoom());
            PortalRoom portal = world.getPortalRoom();
            while (!portal.isSaved()) {
                ter.renderFrame(portal.getRoomTiles());
                detectMousePortal(55, 33);
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.setFont(new Font("Monaco", Font.BOLD, 15));
                StdDraw.text(10, 33, "health : " + String.valueOf(world.getAvatar().getHealth()));
                String nameHUD = world.getAvatar().getName() + " is playing";
                StdDraw.text(35, 33, nameHUD);
                StdDraw.show();
                if (StdDraw.hasNextKeyTyped()) {
                    char curr = StdDraw.nextKeyTyped();
                    saveGame(curr);
                    if (!avatar2.portalMove(curr)) {
                        Avatar.decrementHealth(world.getAvatar());
                    }
                    if (world.getAvatar().getHealth() == 0) {
                        setIsGameOver(true);
                        portal.setIsSaved(true);
                    } else {
                        ter.renderFrame(portal.getRoomTiles());
                        StdDraw.show();
                    }
                }
                /** check if we have escaped the portal room **/
                if (avatar2.getMyPos().equal(new Position(0, 15))) {
                    portal.setIsSaved(true);
                    ter.initialize(width, height);
                    ter.renderFrame(world.getWorldTiles());
                    while (!isGameOver) {
                        if (StdDraw.hasNextKeyTyped()) {
                            world.getAvatar().move(StdDraw.nextKeyTyped());
                        }
                        ter.renderFrame(world.getWorldTiles());
                        StdDraw.setPenColor(Color.WHITE);
                        StdDraw.setFont(new Font("Monaco", Font.BOLD, 15));
                        StdDraw.text(width / 2, 48, "MOVE ONE MORE STEP!");
                        StdDraw.show();
                        if (StdDraw.hasNextKeyTyped()) {
                            setIsGameOver(true);
                        }
                    }
                }
            }
        }
        StdDraw.show();
    }

    public void displayHUD() {
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.setFont(new Font("Monaco", Font.BOLD, 15));
        StdDraw.text(width / 2, 48, world.getAvatar().getName() + " is playing");
        StdDraw.text(5, 48, "Health : " + String.valueOf(world.getAvatar().getHealth()));
        StdDraw.show();
    }

}
