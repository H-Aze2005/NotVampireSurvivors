import com.googlecode.lanterna.SGR;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;

import java.util.ArrayList;
import java.util.List;

public class Hero extends Element {

    private String color = "#FFFFFF";

    public Hero (int x, int y) {
        super(x, y);
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Position moveUp() {
        return new Position(position.getX(), position.getY() - 1);
    }

    public Position moveDown() {
        return new Position(position.getX(), position.getY() + 1);
    }

    public Position moveLeft() {
        return new Position(position.getX() - 1, position.getY());
    }

    public Position moveRight() {
        return new Position(position.getX() + 1, position.getY());
    }

    private int calculateX(double height) {
        double velocity = 1;
        double angle = 38;
        //velocity is 9.15 for running and 2.7 for standing
        //angle is 21 for running and 38 for standing
        // Convert angle to radians
        double angleRadians = Math.toRadians(angle);
        // Calculate the distance using the projectile motion formula
        double distance = velocity * Math.cos(angleRadians) * ((velocity * Math.sin(angleRadians) + Math.sqrt(Math.pow(velocity * Math.sin(angleRadians), 2) - (2 * 9.81 * height))) / 9.81);
        // Return the new position
        return position.getX() + (int) Math.round(distance);
    }

    public List<Position> projectileMotion(double height, int direction, int maxX) {
        List<Position> points = new ArrayList<>();

        // Vertex of the parabola is at (maxX/2, height)
        double h = maxX / 2.0;
        double k = height;

        // Calculate "a" for the parabola equation y = a * (x - h)^2 + k
        double a = -4.0 * height / (maxX * maxX);

        // Generate points along the arc
        for (double x = 0; x <= maxX; x += 0.1) {
            double y = a * Math.pow(x - h, 2) + k;
            // Round x and y to integers and add to the list as Position objects
            points.add(new Position(position.getX() + (int) Math.round(x * direction), position.getY() - (int) Math.round(y)));
        }

        return points;
    }

    @Override
    public void draw(TextGraphics graphics) {
        graphics.setBackgroundColor(TextColor.Factory.fromString(color));
        graphics.fillRectangle(new TerminalPosition(position.getX(), position.getY()), new TerminalSize(1, 1), ' ');
    }

}
