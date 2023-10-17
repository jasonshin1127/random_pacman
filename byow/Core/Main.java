package byow.Core;

import byow.TileEngine.TERenderer;
import edu.princeton.cs.algs4.StdDraw;

import java.io.IOException;

/** This is the main entry point for the program. This class simply parses
 *  the command line inputs, and lets the byow.Core.Engine class take over
 *  in either keyboard or input string mode.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length > 2) {
            System.out.println("Can only have two arguments - the flag and input string");
            System.exit(0);
        } else if (args.length == 2 && args[0].equals("-s")) {
            TERenderer ter = new TERenderer();
            Engine engine = new Engine();
            ter.initialize(engine.WIDTH, engine.HEIGHT);
            ter.renderFrame(engine.interactWithInputString(args[1]));
            StdDraw.show();
            System.out.println(engine.toString());
            // DO NOT CHANGE THESE LINES YET ;)
        } else if (args.length == 2 && args[0].equals("-p")) {
            System.out.println("Coming soon.");
        } else { // DO NOT CHANGE THESE LINES YET ;)
            Engine engine = new Engine();
            engine.interactWithKeyboard();
        }
    }
}
