/**
 * Immutable class to represent a position
 * along a two-dimensional grid.
 *
 * @author Boyan Stoynov
 */
public class Position {
    private final int horizontalPosition;
    private final int verticalPosition;

    /**
     * Creates a position object.
     * @param horizontalPosition horizontal position
     * @param verticalPosition vertical position
     */
    public Position (int horizontalPosition, int verticalPosition) {
        this.horizontalPosition = horizontalPosition;
        this.verticalPosition = verticalPosition;
    }

    public int getHorzPosition() {
        return horizontalPosition;
    }

    public int getVertPosition() {
        return verticalPosition;
    }
}
