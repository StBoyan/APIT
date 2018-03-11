/**
 * Class to generate Vehicles with a given
 * frequency and a preset speed and add them
 * to a grid.
 *
 * @author Boyan Stoynov
 */
public class TrafficGenerator extends Thread {
    /* GridSimulator to which the cars are added to */
    private final GridSimulator grid;
    /* Upper bound of the Vehicle speed
    * N.B - lower is faster */
    private final int MAXIMUM_SPEED;
    /* Lower bound of the Vehicle speed
     * N.B - higher is slower */
    private final int MINIMUM_SPEED;
    /* Millisecond delay between each Vehicle
     * generated */
    private final int GENERATION_FREQUENCY;
    /* StatisticCollector monitor */
    private StatisticsCollector statisticsCollector;

    /**
     * Creates a TrafficGenerator object. Maximum and minimum speed
     * for this generator are taken from the configuration class.
     * @param gs GridSimulator object
     * @param frequency frequency of generation
     */
    public TrafficGenerator(GridSimulator gs, int frequency) {
        grid = gs;
        GENERATION_FREQUENCY = frequency;
        MAXIMUM_SPEED = SimulationConfig.getMaximumSpeed();
        MINIMUM_SPEED = SimulationConfig.getMinimumSpeed();
    }

    /**
     * Creates a TrafficGenerator object with a
     * StatisticCollector object. Maximum and minimum
     * speed for this generator are taken from the
     * configuration class.
     * @param gs GridSimulator object
     * @param frequency frequency of generation
     * @param stat StatisticCollector object
     */
    public TrafficGenerator(GridSimulator gs, int frequency, StatisticsCollector stat) {
        grid = gs;
        GENERATION_FREQUENCY = frequency;
        MAXIMUM_SPEED = SimulationConfig.getMaximumSpeed();
        MINIMUM_SPEED = SimulationConfig.getMinimumSpeed();
        statisticsCollector = stat;
    }

    /**
     * Starts the thread. Vehicles are generated and put on the
     * Grid according to the frequency generation.
     */
    @Override
    public void run() {
        for (;;) {
            try {
                Thread.sleep(GENERATION_FREQUENCY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            /* If there is a StatisticCollector, generates
             * monitored traffic. */
            if (statisticsCollector == null)
                generateTraffic();
            else
                generateMonitoredTraffic();
        }
    }

    /**
     * Creates a Vehicle object with random speed, puts it on
     * the grid, and starts its thread.
     */
    private void generateTraffic() {
        Vehicle v = new Vehicle(getRandomSpeed(), grid);
        grid.addVehicleToGrid(v, this);
        v.start();
    }

    /**
     * Creates a Vehicle object with the StatisticCollector monitor
     * attached to it, puts it on the grid, and starts its thread.
     */
    private void generateMonitoredTraffic() {
        Vehicle v = new Vehicle(getRandomSpeed(), grid, statisticsCollector);
        grid.addVehicleToGrid(v, this);
        v.start();
    }

    /**
     * Gets a random speed between the upper and lower bound
     * defined in the class.
     * @return int random speed
     */
    private int getRandomSpeed() {
        // Gets a random delay, up to the minimum speed bound
        int randomSpeed = (int) (Math.random() * MINIMUM_SPEED);

        /* If the delay is lower than the maximum speed bound,
         * sets it to the maximum speed. */
        if (randomSpeed < MAXIMUM_SPEED)
            randomSpeed = MAXIMUM_SPEED;

        return randomSpeed;
    }
}
