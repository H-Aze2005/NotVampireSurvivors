import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Arena {
    private int width;
    private int height;
    private Hero hero;
    private List<Block> blocks;
    private Instant keyPressStartTime;
    private boolean upKeyPressed = false;
    private final int MIN_JUMP_HEIGHT = 1;
    private final int MAX_JUMP_HEIGHT = 10;
    private ScreenRefresher screenRefresher;

    public Arena (int width, int height, ScreenRefresher screenRefresher) {
        this.width = width;
        this.height = height;
        this.hero = new Hero(width / 2, height-2); //spawn hero at the bottom of the screen
        this.blocks = createBlocks();
        this.screenRefresher = screenRefresher;
    }

    public Hero getHero() {
        return hero;
    }

    public void draw(TextGraphics graphics) {
        graphics.setBackgroundColor(com.googlecode.lanterna.TextColor.ANSI.BLUE);
        graphics.fillRectangle(new com.googlecode.lanterna.TerminalPosition(0, 0), new com.googlecode.lanterna.TerminalSize(width, height), ' ');

        for (Block block : blocks) {
            block.draw(graphics);
        }

        hero.draw(graphics);
    }

    private List<Block> createBlocks() {
        List<Block> blocks = new ArrayList<>();
        for (int c = 0; c < width; c++) {
            blocks.add(new Block(c, 0));
            blocks.add(new Block(c, height - 1));
        }
        for (int r = 1; r < height - 1; r++) {
            blocks.add(new Block(0, r));
            blocks.add(new Block(width - 1, r));
        }
        return blocks;
    }

    public void moveHeroUp(int steps) throws IOException {
        for (int i = 0; i < steps; i++) {
            Position newPosition = hero.moveUp();
            if (canHeroMove(newPosition)) {
                hero.setPosition(newPosition);
                screenRefresher.drawAndRefresh();
                try {
                    Thread.sleep(100); // Adjust the speed of jumping as needed
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    public void moveHeroDown() {
        Position newPosition = hero.moveDown();
        if (canHeroMove(newPosition)) {
            hero.setPosition(newPosition);
        }
    }

    public void moveHeroLeft() {
        Position newPosition = hero.moveLeft();
        if (canHeroMove(newPosition)) {
            hero.setPosition(newPosition);
        }
    }

    public void moveHeroRight() {
        Position newPosition = hero.moveRight();
        if (canHeroMove(newPosition)) {
            hero.setPosition(newPosition);
        }
    }

    public void moveHero(int jumpHeight) throws IOException {
        List<Position> trajectory = hero.projectileMotion(jumpHeight);
        for (Position position : trajectory) {
            if (canHeroMove(position)) {
                hero.setPosition(position);
                screenRefresher.drawAndRefresh();
                try {
                    Thread.sleep(100); // Adjust the speed of jumping as needed
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } else {
                break;
            }
        }
    }

    public void processKey(KeyStroke key) throws IOException {
        int jumpHeight = 0;
        int movement = 0;
        if (key.getKeyType() == KeyType.ArrowUp) {
            if (!upKeyPressed) { // If key was not previously pressed
                upKeyPressed = true;
                keyPressStartTime = Instant.now();
            } else { // If key was previously pressed
                Duration keyPressDuration = Duration.between(keyPressStartTime, Instant.now());
                jumpHeight = (int) keyPressDuration.toMillis() / 100; // Adjust divisor for jump sensitivity
                jumpHeight = Math.max(MIN_JUMP_HEIGHT, Math.min(jumpHeight, MAX_JUMP_HEIGHT)); // Apply limits
                moveHeroUp(jumpHeight);

                // Reset the flag
                upKeyPressed = false;
            }
        } else {
            if (upKeyPressed) { // Key was released (when any other key is pressed)
                Duration keyPressDuration = Duration.between(keyPressStartTime, Instant.now());
                jumpHeight = (int) keyPressDuration.toMillis() / 100; // Adjust divisor for jump sensitivity
                jumpHeight = Math.max(MIN_JUMP_HEIGHT, Math.min(jumpHeight, MAX_JUMP_HEIGHT)); // Apply limits
                moveHero(jumpHeight);


                // Reset the flag
                upKeyPressed = false;
            }
        }

        if (key.getKeyType() == KeyType.ArrowDown) {
            moveHeroDown();
        } else if (key.getKeyType() == KeyType.ArrowLeft) {
            moveHeroLeft();
        } else if (key.getKeyType() == KeyType.ArrowRight) {
            moveHeroRight();
        } else if (key.getKeyType() == KeyType.Character && key.getCharacter() == 'q') {
            throw new IOException("Exit the loop");
        } else if (key.getKeyType() == KeyType.EOF) {
            throw new IOException("Exit the loop");
        }
    }

    private boolean canHeroMove(Position position) {
        for (Block block : blocks) {
            if (block.getPosition().equals(position)) {
                return false;
            }
        }
        return true;
    }

    public boolean isHeroFalling() {
        Position heroPosition = hero.getPosition();
        Position positionBelowHero = new Position(heroPosition.getX(), heroPosition.getY() + 1);

        for (Block block : blocks) {
            if (block.getPosition().equals(positionBelowHero)) {
                return false;
            }
        }
        return true;
    }

}
