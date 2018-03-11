/**
 * This class models a vehicle object that
 * runs on a separate thread and can move
 * along a grid.
 *
 * @author Boyan Stoynov
 */
public class Vehicle extends Thread {
    /* Milliseconds between each move */
    private final int speed;
    /* Direction of travel along the grid */
    private Direction direction;
    /* Vehicle's position on the grid */
    private Position position;
    /* Traversable object to be traversed */
    private final Traversable grid;
    /* StatisticCollector used to monitor the Vehicle */
    private StatisticsCollector statsCollector;

    /**
     * Creates a new Vehicle object.
     * @param sp speed
     * @param tr Traversable object
     */
    public Vehicle(int sp, Traversable tr) {
        speed = sp;
        grid = tr;
    }

    /**
     * Creates new monitored Vehicle object.
     * @param sp speed
     * @param tr Traversable object
     * @param stats StatisticCollect object
     */
    public Vehicle(int sp, Traversable tr, StatisticsCollector stats) {
        speed = sp;
        grid = tr;
        statsCollector = stats;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    /**
     * Starts the thread. The vehicle tries to move further
     * if it can after a delay. If the vehicle is not able
     * to move further it removes itself from the grid and
     * breaks out of the loop.
     */
    @Override
    public void run() {
        for (;;) {
            // Nano time at beginning of thread's existence
            long startTime = System.nanoTime();
            // Simulates the speed by waiting
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Advances position if grid hasn't been traversed
            if (grid.canMove(this))
                grid.advancePosition(this);
                // Removes vehicle from grid and breaks out of loop
            else {
                grid.removeVehicle(this);

                // Estimated time of thread's existence
                long estimatedTime = System.nanoTime() - startTime;
                // Log estimated existence time if stats are collected
                if (statsCollector != null)
                    statsCollector.logVehicleTime(estimatedTime);

                break;
            }
        }
    }
}
