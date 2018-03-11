/**
 * This class demonstrates the functionality required
 * by the level 2 specifications for the Advanced Programming
 * coursework.
 *
 * @author Boyan Stoynov
 */
public class APSpec2 {

    public static void main(String[] args) {
        // Create GridSimulator object
        GridSimulator grid = new GridSimulator();
        /* Reverse the horizontal directions of rows 5 to 10
         * and the vertical direction of rows 10 to 20 */
        grid.reverseHorizontalDirection(5, 10);
        grid.reverseVerticalDirection(10, 20);
        /* Create StatisticCollector for the default generator, the
         * generator itself by passing the generator object to it.
         * Then add the generator to the grid. */
        StatisticsCollector defCollector = new StatisticsCollector("Default");
        TrafficGenerator defGenerator = new TrafficGenerator(grid, 350, defCollector);
        grid.addDefaultGenerator(defGenerator);
        /* Same as above for a generator attached horizontally to
         * rows 5 to 10. */
        StatisticsCollector altCollector1 = new StatisticsCollector("Horizontal Generator");
        TrafficGenerator altGenerator1 = new TrafficGenerator(grid, 500, altCollector1);
        grid.addHorizontalGenerator(altGenerator1, 5, 10);
        /* Same as above for a generator attached vertically to
         * columns 10 to 20. */
        StatisticsCollector altCollector2 = new StatisticsCollector("Vertical Generator");
        TrafficGenerator altGenerator2 = new TrafficGenerator(grid, 600, altCollector2);
        grid.addVerticalGenerator(altGenerator2, 10, 20);
        // Create the visualiser and pass the grid to be visualised
        SimulationVisualiser visualiser = new SimulationVisualiser(grid);
        // Start the visualiser and the generators
        visualiser.start();
        defGenerator.start();
        altGenerator1.start();
        altGenerator2.start();
    }
}
