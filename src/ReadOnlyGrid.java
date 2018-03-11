/**
 * ReadOnlyGrid is an interface that should be implemented by
 * another class that models a grid of Vehicles and wants to
 * expose only its get method for the traffic grid and protect
 * all others.
 *
 * @author Boyan Stoynov
 */
public interface ReadOnlyGrid {

    /**
     * Returns a defensive copy of the 2d array used to
     * represent the traffic grid.
     * @return Vehicle[][] traffic grid
     */
    Vehicle[][] getTrafficGrid();

}
