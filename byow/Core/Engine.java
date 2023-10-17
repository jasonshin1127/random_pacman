package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
//import byow.TileEngine.Tileset;
//import edu.princeton.cs.algs4.StdArrayIO;
//import edu.princeton.cs.algs4.StdAudio;

import edu.princeton.cs.algs4.StdDraw;

import java.io.File;
import java.io.IOException;

import static byow.Core.Game.CWD;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 90;
    public static final int HEIGHT = 50;

    private static File savedWorld = Utils.join(CWD, "world.txt");




    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() throws IOException {
        /** start the game **/
        Game game = new Game();
        game.init();
//        StdAudio.play("Komiku_-_12_-_Bicycle.wav");
        /** get the seed & name from the user and create the world **/
        while (!game.isGameCreated()) {
            if (StdDraw.hasNextKeyTyped()) {
                char curr = StdDraw.nextKeyTyped();
                if (curr == 'L' || curr == 'l') {
                    Game gameLoaded = Game.loadGame(curr);
                    game = gameLoaded;
                    game.setIsGameCreated(true);
                    if (game.getWorld().getAvatar().getMyPos().
                            equal(game.getWorld().getPortalEntry())) {
                        Game.loadPortal(game);
                    } else {
                        ter.initialize(WIDTH, HEIGHT);
                        ter.renderFrame(game.getWorld().getWorldTiles());
                        StdDraw.show();
                    }
                } else {
                    game.createSeed(curr);
                    if (game.isGameCreated()) {
                        game.createWorld(game.getSeed(), game.createName());
                    }
                }
            }
        }


        /** start the game, move the avatar to the portal room **/
        while (!game.isGameOver()) {
//            StdAudio.play("starting.au");
            if (StdDraw.hasNextKeyTyped()) {
                char curr = StdDraw.nextKeyTyped();
                game.saveGame(curr);
                game.moveAvatarCheck(curr);

                game.enterPortal();
            }
            game.detectMouse(82, 48);
            game.displayHUD();
        }
        game.checkWin();
    }








    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123s swwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */


    public String getSeed(String input) {
        String seed = "";
        for (int i = 0; i < input.length(); i++) {
            if (!Character.isDigit(input.charAt(i))) {
                continue;
            }
            seed += input.charAt(i);
        }
        return seed;
    }


    public TETile[][] interactWithInputString(String input) {
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        // from the input, we can get a seed //

        World world;
        Avatar avatar;
        int indexFirst = 1;

        if (input.charAt(0) == 'n' || input.charAt(0) == 'N') {
            world = new World(Long.parseLong(getSeed(input)));
            world.fillBlanks();
            world.generateWorld();
            Position avatarPos = world.setAvatar();
            avatar = new Avatar(avatarPos, world);
            world.placeAvatar(avatar);
            world.createPortal();
            world.createCoins();
            int seedLen = getSeed(input).length();
            indexFirst = seedLen + 2;
        } else if (input.charAt(0) == 'L' || input.charAt(0) == 'l') {
            world = Utils.readObject(savedWorld, World.class);
            avatar = world.getAvatar();
        } else {
            return null;
        }

        for (int i = indexFirst; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == 'a' || c == 'A' || c == 'D' || c == 's' || c == 'S' || c == 'd'
                    || c == 'w' || c == 'W') {
                avatar.move(c);
            }
            if (c == ':') {
                if (input.charAt(i + 1) == 'q' || input.charAt(i + 1) == 'Q') {
                    Utils.writeObject(savedWorld, world);
                    break;
                } else {
                    continue;
                }
            }
        }

        return world.getWorldTiles();
    }
}
