/**
 * The Leona class simulates a simple drawing agent (similar to turtle graphics).
 * It tracks position, direction, drawing state (pen up/down), and color.
 * When the pen is down, it "draws" lines by printing coordinate output.
 */
public class Leona {
    public String color;        // current drawing color
    public double x, y;         // current position
    public boolean up;          // whether the pen is up (not drawing)
    public int angle;           // current direction in degrees (0 = east)

    /**
     * Constructor to initialize position, angle, pen state, and color.
     */
    public Leona(double x, double y, int angle, boolean up, String color) {
        this.color = "#0000FF"; // default color (this is overwritten below)
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.up = up;
        this.color = color;
    }

    /**
     * Moves Leona (turle) forward in the current direction.
     * If the pen is down, prints out the line segment as a string of coordinates.
     *
     * @param steps number of steps to move forward
     */
    public void move(double steps) {
        double turn = (Math.PI * angle) / 180; // convert angle to radians

        double newX = x + (steps * Math.cos(turn)); // compute new x
        double newY = y + (steps * Math.sin(turn)); // compute new y

        if (!up) {
            // simulate drawing by printing line segment
            System.out.println(color + " " + x + " " + y + " " + newX + " " + newY);
        }

        // update current position
        x = newX;
        y = newY;
    }

    /**
     * Turns the agent left by a given angle.
     *
     * @param newAngle degrees to turn left
     */
    public void turnLeft(int newAngle) {
        angle += newAngle;
    }

    /**
     * Turns the agent right by a given angle.
     *
     * @param newAngle degrees to turn right
     */
    public void turnRight(int newAngle) {
        angle -= newAngle;
    }

    /**
     * Lifts the pen up (no drawing while moving).
     */
    public void up() {
        up = true;
    }

    /**
     * Puts the pen down (drawing while moving).
     */
    public void down() {
        up = false;
    }

    /**
     * Changes the drawing color.
     *
     * @param newColor new color in hex format (e.g., "#FF0000")
     */
    public void changeColor(String newColor) {
        color = newColor;
    }
}
