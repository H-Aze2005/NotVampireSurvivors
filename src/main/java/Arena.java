import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Arena {
    private int width;
    private int height;
    private Hero hero;

    public Arena (int width, int height) {
        this.width = width;
        this.height = height;
        this.hero = new Hero(width / 2, height / 2);
    }

    public Hero getHero() {
        return hero;
    }

    public void draw(TextGraphics graphics) {
        graphics.setBackgroundColor(com.googlecode.lanterna.TextColor.ANSI.BLUE);
        graphics.fillRectangle(new com.googlecode.lanterna.TerminalPosition(0, 0), new com.googlecode.lanterna.TerminalSize(width, height), ' ');
        hero.draw(graphics);
    }

    public void moveHeroUp() {
        Position newPosition = hero.moveUp();
        if (canHeroMove(newPosition)) {
            hero.setPosition(newPosition);
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

    public void processKey(KeyStroke key) throws IOException {
        if (key.getKeyType() == KeyType.ArrowUp) {
            moveHeroUp();
        } else if (key.getKeyType() == KeyType.ArrowDown) {
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
        return true;
    }

}
