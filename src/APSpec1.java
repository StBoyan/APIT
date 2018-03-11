/**
 * This class demonstrates the functionality required
 * by the level 1 specifications for the Advanced Programming
 * coursework.
 *
 * @author Boyan Stoynov
 */
public class APSpec1 {

    public static void main(String[] args) {
        /* Create the GridSimulator and default TrafficGenerator
         * and adds the generator to the grid */
        GridSimulator grid = new GridSimulator();
        TrafficGenerator defGenerator = new TrafficGenerator(grid, 250);
        grid.addDefaultGenerator(defGenerator);
        // Create the visualiser and pass the grid to be visualised
        SimulationVisualiser visualiser = new SimulationVisualiser(grid);
        // Start the visualisation and the traffic generation
        visualiser.start();
        defGenerator.start();
    }
}
