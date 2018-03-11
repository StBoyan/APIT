/**
 * Traversable is an interface that can be implemented by
 * another class that models a grid of Vehicles. It provides
 * methods for a Vehicle object to use for traversing
 * the grid.
 *
 * @author Boyan Stoynov
 */
public interface Traversable {

    /**
     * Checks if the Vehicle can move further along the grid.
     * If false - the Vehicle has reached the last position
     * possible.
     * @param v Vehicle object
     * @return boolean whether the vehicle can move further
     */
    boolean canMove(Vehicle v);

    /**
     * Advances the position of the Vehicle object to the
     * next available one.
     * @param v Vehicle object
     */
    void advancePosition(Vehicle v);

    /**
     * Removes the Vehicle from the grid. This should only be
     * called after the Vehicle has reached its final position.
     * @param v Vehicle object
     */
    void removeVehicle(Vehicle v);
}
