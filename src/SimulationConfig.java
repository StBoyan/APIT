/**
 * This class provides configuration facilities for
 * the simulation. It has default values for each
 * setting, methods to access them, as well as methods
 * to modify them if necessary.
 *
 * @author Boyan Stoynov
 */
public class SimulationConfig {
    /* Number of grid rows in simulation */
    private static int gridRows = 10;
    /* Number of grid columns in simulation */
    private static int gridColumns = 20;
    /* Number of times to draw simulation */
    private static int numOfVisualisations = 2000;
    /* Milliseconds between each draw */
    private static int visualisationRefreshRate = 20;
    /* Symbol to represent vertically moving vehicle */
    private static char verticalVehicleSymbol = 'o';
    /* Symbol to represent horizontally moving vehicle */
    private static char horizontalVehicleSymbol = '-';
    /* Minimum vehicle speed. N.B - lower is faster */
    private static int maximumSpeed = 80;
    /* Maximum vehicle speed. N.B - higher is slower */
    private static int minimumSpeed = 450;

    public static int getGridRows() {
        return gridRows;
    }

    public static void setGridRows(int gridRows) {
        SimulationConfig.gridRows = gridRows;
    }

    public static int getGridColumns() {
        return gridColumns;
    }

    public static void setGridColumns(int gridColumns) {
        SimulationConfig.gridColumns = gridColumns;
    }

    public static int getNumOfVisualisations() {
        return numOfVisualisations;
    }

    public static void setNumOfVisualisations(int numOfVisualisations) {
        SimulationConfig.numOfVisualisations = numOfVisualisations;
    }

    public static int getVisualisationRefreshRate() {
        return visualisationRefreshRate;
    }

    public static void setVisualisationRefreshRate(int visualisationRefreshRate) {
        SimulationConfig.visualisationRefreshRate = visualisationRefreshRate;
    }

    public static char getVerticalVehicleSymbol() {
        return verticalVehicleSymbol;
    }

    public static void setVerticalVehicleSymbol(char verticalVehicleSymbol) {
        SimulationConfig.verticalVehicleSymbol = verticalVehicleSymbol;
    }

    public static char getHorizontalVehicleSymbol() {
        return horizontalVehicleSymbol;
    }

    public static void setHorizontalVehicleSymbol(char horizontalVehicleSymbol) {
        SimulationConfig.horizontalVehicleSymbol = horizontalVehicleSymbol;
    }

    public static int getMaximumSpeed() {
        return maximumSpeed;
    }

    public static void setMaximumSpeed(int maximumSpeed) {
        SimulationConfig.maximumSpeed = maximumSpeed;
    }

    public static int getMinimumSpeed() {
        return minimumSpeed;
    }

    public static void setMinimumSpeed(int minimumSpeed) {
        SimulationConfig.minimumSpeed = minimumSpeed;
    }

}
