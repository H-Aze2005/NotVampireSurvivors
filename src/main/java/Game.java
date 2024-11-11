import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

public class Game implements ScreenRefresher {
    private Screen screen;
    private Arena arena;
    private boolean running = true;

    public Game() {
        try {
            DefaultTerminalFactory terminalFactory = new DefaultTerminalFactory()
                    .setInitialTerminalSize(new TerminalSize(111, 37)); // Set desired terminal size

            Terminal terminal = terminalFactory.createTerminal();

            screen = new TerminalScreen(terminal);
            screen.setCursorPosition(null); // we don't need a cursor
            screen.startScreen(); // screens must be started
            screen.doResizeIfNecessary(); // resize screen if necessary

            TerminalSize terminalSize = screen.getTerminalSize();
            //System.out.println("Terminal Size: " + terminalSize.getColumns() + "x" + terminalSize.getRows());
            arena = new Arena(terminalSize.getColumns(), terminalSize.getRows(), this);

            screen.clear();
            screen.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() throws IOException {
        handleFalling();
        while (running) {
            draw();
            KeyStroke key = screen.readInput();
            try {
                processKey(key);
                handleFalling();
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

    private void handleFalling() throws IOException {
        while (arena.isHeroFalling()) {
            arena.moveHeroDown();
            try {
                Thread.sleep(100); // Adjust the speed of falling as needed
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            draw();
        }
    }


    private void resetGame() throws IOException {
        arena = new Arena(40, 20, this);
        running = true;
    }

    @Override
    public void drawAndRefresh() throws IOException {
        draw();
    }

}
