/**
 * A class that runs on its own thread and is responsible
 * for drawing the simulation to the standard output stream.
 *
 * @author Boyan Stoynov
 */
public class SimulationVisualiser extends Thread {

    /* ReadOnlyGrid object that is used to draw the simulation */
    private final ReadOnlyGrid grid;
    /* Number of times to draw the simulation */
    private final int NUM_OF_DRAWS;
    /* Milliseconds between each draw */
    private final int REFRESH_RATE;
    /* Char used to represent vehicle moving vertically */
    private final char VERTICAL_VEHICLE;
    /* Char used to represent vehicle moving horizontally */
    private final char HORIZONTAL_VEHICLE;

    /**
     * Creates the object and assigns the ReadOnlyGrid object
     * to draw the simulation. Other class attributes are set at this point
     * from the SimulationConfig static class.
     * @param gs ReadOnlyGrid object
     */
    public SimulationVisualiser(ReadOnlyGrid gs) {
        grid = gs;
        NUM_OF_DRAWS = SimulationConfig.getNumOfVisualisations();
        REFRESH_RATE = SimulationConfig.getVisualisationRefreshRate();
        VERTICAL_VEHICLE = SimulationConfig.getVerticalVehicleSymbol();
        HORIZONTAL_VEHICLE = SimulationConfig.getHorizontalVehicleSymbol();
    }

    /**
     * Starts the visualisation of the simulation.
     * The visualisation is printed according to the
     * desired refresh rate.
     */
    @Override
    public void run() {
        for (int i = 0; i < NUM_OF_DRAWS; i++) {
            System.out.println("Frame: " + (i + 1));
            this.visualiseSimulation();
            try {
                Thread.sleep(REFRESH_RATE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // Terminates program after visualisation is over
        System.exit(0);
    }

    /**
     * Prints the current state of the Grid to the standard
     * output stream.
     */
    private void visualiseSimulation() {
    Vehicle[][] visGrid = grid.getTrafficGrid();
    StringBuilder sb = new StringBuilder();

    // Prints upper road bound
    for (Vehicle v : visGrid[0]) {
        sb.append("==");
    }
    sb.append("\n");

    // Prints lanes along with any vehicles on them
    for (Vehicle[] vRow : visGrid) {
        sb.append("|");

        for (Vehicle v : vRow) {
            if (v != null) {
                Direction dir = v.getDirection();
                if (dir == Direction.West || dir == Direction.East)
                    sb.append(HORIZONTAL_VEHICLE);
                else if (dir == Direction.South || dir == Direction.North)
                    sb.append(VERTICAL_VEHICLE);
            }
            else
                sb.append(" ");

            sb.append("|");
        }

        sb.append("\n");
    }

    // Prints lower road bound
    for (Vehicle v : visGrid[0]) {
        sb.append("==");
    }

    System.out.println(sb.toString());
    }
}
