import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class Game {
    private Screen screen;
    private Arena arena;
    private boolean running = true;

    public Game() {
        try {
            TerminalSize terminalSize = new TerminalSize(40, 20);
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory().setInitialTerminalSize(terminalSize);
            Terminal terminal = terminalFactory.createTerminal();

            screen = new TerminalScreen(terminal);
            screen.setCursorPosition(null); // we don't need a cursor
            screen.startScreen(); // screens must be started
            screen.doResizeIfNecessary(); // resize screen if necessary

            arena = new Arena(40,20);

            screen.clear();
            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() throws IOException {
        while (running) {
            draw();
            KeyStroke key = screen.readInput();
            try {
                processKey(key);
            } catch (IOException e) {
                running = false;
            }
        }
        screen.close();
    }

    private void draw()  throws IOException  {
        screen.clear();
        arena.draw(screen.newTextGraphics());
        screen.refresh();
    }

    private void processKey(KeyStroke key) throws IOException {
        arena.processKey(key);
    }


    private void resetGame() {
        arena = new Arena(40, 20);
        running = true;
    }

}
