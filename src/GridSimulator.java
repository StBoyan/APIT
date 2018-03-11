import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Class to model and simulate a grid of moving Vehicles
 * along an intersection of arbitrary size. By default
 * all horizontally-placed vehicles move West and all
 * vertically-placed vehicles move South with methods to
 * modify the directions. The grid takes a default generator
 * which adds cars along every possible starting position. It
 * could further take additional horizontal and vertical
 * generators. When such a generator is placed all rows or
 * columns it is responsible for are removed from the default's
 * responsibilities. The class implements the Traversable
 * interface allowing vehicles to move along the grid. It
 * also implements the ReadOnlyGrid interface to be used by
 * a class that visualises the simulation.

 * @author Boyan Stoynov
 */
public class GridSimulator implements ReadOnlyGrid, Traversable {
    /* 2d array to store the Vehicle objects */
    private final Vehicle[][] trafficGrid;
    /* The default generator for adding Vehicle objects */
    private TrafficGenerator defaultGenerator;
    /* Rows where the default generator can put traffic */
    private ArrayList<Integer> defaultGeneratorRows;
    /* Columns where the default generator can put traffic */
    private ArrayList<Integer> defaultGeneratorCols;
    /* Stores the generators used to put traffic horizontally
     * and the rows on which they can put the Vehicles */
    private HashMap<TrafficGenerator, ArrayList<Integer>> horizontalGeneratorMap;
    /* Stores the generators used to put traffic vertically
     * and the columns on which they can put the Vehicles */
    private HashMap<TrafficGenerator, ArrayList<Integer>> verticalGeneratorMap;
    /* Random object to get random numbers for rows/columns
     * when putting traffic */
    private final Random random;
    /* Stores the direction of travel for each row */
    private final Direction[] horizontalDirections;
    /* Stores the direction of travel for each column */
    private final Direction[] verticalDirections;
    /* ReentrantLock to facilitate cars waiting for each other */
    private final ReentrantLock vehicleLock;
    /* Condition to check whether a position is occupied  */
    private final Condition condition;

    /**
     * Creates a GridSimulator object with as many rows and columns
     * as specified in the SimulationConfig class. All horizontal lanes
     * are westbound and all vertical lanes are southbound.
     */
    public GridSimulator() {
        trafficGrid = new Vehicle[SimulationConfig.getGridRows()][SimulationConfig.getGridColumns()];

        // Sets all horizontal lanes' Direction to West
        horizontalDirections = new Direction[trafficGrid.length];
        Arrays.fill(horizontalDirections, Direction.West);
        // Sets all vertical lanes' Direction to South.
        verticalDirections = new Direction[trafficGrid[0].length];
        Arrays.fill(verticalDirections, Direction.South);
        random = new Random();

        vehicleLock = new ReentrantLock();
        condition = vehicleLock.newCondition();
    }

    /**
     * Adds a generator that can put traffic on all columns and
     * rows of the grid. Any horizontal and vertical generators
     * added after the default will reduce the rows/columns that
     * the generator is responsible for.
     * E.g - if a horizontal generator is added to rows 1 to 10,
     * the default generator will no longer put its traffic there.
     * @param generator TrafficGenerator object
     */
    public void addDefaultGenerator(TrafficGenerator generator) {
        defaultGenerator = generator;
        defaultGeneratorRows = new ArrayList<>();
        for (int i = 0; i < trafficGrid.length; i++) {
            defaultGeneratorRows.add(i);
        }
        defaultGeneratorCols = new ArrayList<>();
        for (int i = 0; i < trafficGrid[0].length; i++) {
            defaultGeneratorCols.add(i);
        }
    }

    /**
     * Adds a generator that can put traffic only on the specified
     * rows of the grid. If a default generator has been added
     * previously, it will no longer put traffic on these rows.
     * @param generator TrafficGenerator object
     * @param from from row (inclusive)
     * @param to to row (exclusive)
     */
    public void addHorizontalGenerator(TrafficGenerator generator, int from, int to){
        // Initialises the HashMap if it hasn't been already
        if (horizontalGeneratorMap == null)
            horizontalGeneratorMap = new HashMap<>();

        // Puts the rows into an ArrayList
        ArrayList<Integer> rows = new ArrayList<>();
        for (int i = from; i < to; i++) {
            rows.add(i);
        }

        // Removes the rows from the default generators' responsibility
        if (defaultGeneratorRows != null)
            defaultGeneratorRows.removeAll(rows);
        // Puts this generator and the rows it is responsible for in the Hashmap
        horizontalGeneratorMap.put(generator, rows);
    }

    /**
     * Adds a generator that can put traffic only on the specified
     * columns of the grid. If a default generator has been added
     * previously, it will no longer put traffic on these columns.
     * @param generator TrafficGenerator object
     * @param from from row (inclusive)
     * @param to to row (exclusive)
     */
    public void addVerticalGenerator(TrafficGenerator generator, int from, int to){
        // Initialises the HashMap if it hasn't been already
        if(verticalGeneratorMap == null)
            verticalGeneratorMap = new HashMap<>();

        // Puts the columns into an ArrayList
        ArrayList<Integer> columns = new ArrayList<>();
        for (int i = from; i < to; i++) {
            columns.add(i);
        }
        // Removes the columns from the default generators' responsibility
        if (defaultGeneratorCols != null)
            defaultGeneratorCols.removeAll(columns);
        // Puts this generator and the columns it is responsible for in the Hashmap
        verticalGeneratorMap.put(generator, columns);
    }

    /**
     * Returns a defensive copy of the 2d array used to
     * represent the traffic grid.
     * @return Vehicle[][] traffic grid
     */
    public Vehicle[][] getTrafficGrid() {
        return Arrays.copyOf(trafficGrid, trafficGrid.length);
    }

    /**
     * Adds a Vehicle generated by a TrafficGenerator into the
     * grid.
     * @param vhc Vehicle object
     * @param tg TrafficGenerator object
     */
    public void addVehicleToGrid(Vehicle vhc, TrafficGenerator tg) {
        // If the generator that called the method is the default
        if (tg == defaultGenerator) {
            // Get a random grid axis to put the new Vehicle
            int axis = random.nextInt(2);
            // Add the vehicle to the respective axis
            if (axis == 0)
                addVehicleHorizontally(vhc, defaultGeneratorRows);
            else
                addVehicleVertically(vhc, defaultGeneratorCols);
        }
        // If another generator called this method
        else {
            // If it is a horizontal generator
            if (horizontalGeneratorMap.containsKey(tg)) {
                ArrayList<Integer> generatorRows = horizontalGeneratorMap.get(tg);
                addVehicleHorizontally(vhc, generatorRows);
            }
            // If it is a vertical generator
            else {
                ArrayList<Integer> generatorCols = verticalGeneratorMap.get(tg);
                addVehicleVertically(vhc, generatorCols);
            }
        }
    }

    /**
     * Adds a vehicle randomly on one of the given rows in the grid.
     * @param vhc Vehicle object
     * @param positions rows that the Vehicle should be put in
     */
    private void addVehicleHorizontally(Vehicle vhc, ArrayList<Integer> positions) {
        int startRow;
        int startCol;
        for (;;) {
            //Get a random starting row from the ones provided
            startRow = positions.get(random.nextInt(positions.size()));
            //Get the Direction of the randomly chosen row
            Direction dir = horizontalDirections[startRow];
            /* If it is westbound, try to put it on the left-hand side
             * if it is unoccupied */
            if (dir == Direction.West) {
                startCol = 0;
                if (trafficGrid[startRow][startCol] == null) {
                    vhc.setDirection(Direction.West);
                    break;
                }
            }
            /* If it is eastbound, try to put it on the right-hand side
             * if it is unoccupied */
            else {
                startCol = trafficGrid[0].length - 1;
                if (trafficGrid[startRow][startCol] == null) {
                    vhc.setDirection(Direction.East);
                    break;
                }
            }
        }

        // Puts the vehicle on the grid and set its position
        trafficGrid[startRow][startCol] = vhc;
        vhc.setPosition(new Position(startRow, startCol));
    }

    /**
     * Adds a vehicle randomly on one of the given columns in the grid.
     * @param vhc Vehicle object
     * @param positions columns that the Vehicle should be put in
     */
    private void addVehicleVertically(Vehicle vhc, ArrayList<Integer> positions) {
        int startRow;
        int startCol;
        for (;;) {
            //Get a random starting column from the ones provided
            startCol = positions.get(random.nextInt(positions.size()));
            //Get the Direction of the randomly chosen row
            Direction dir = verticalDirections[startCol];
            /* If it is southbound, try to put it on the top-most side
             * if it is unoccupied */
            if (dir == Direction.South) {
                startRow = 0;
                if (trafficGrid[startRow][startCol] == null) {
                    vhc.setDirection(Direction.South);
                    break;
                }
            }
            /* If it is northbound, try to put it on the bottom-most side
             * if it is unoccupied */
            else {
                startRow = trafficGrid.length - 1;
                if (trafficGrid[startRow][startCol] == null) {
                    vhc.setDirection(Direction.North);
                    break;
                }
            }
        }

        // Puts the vehicle on the grid and set its position
        trafficGrid[startRow][startCol] = vhc;
        vhc.setPosition(new Position(startRow, startCol));
    }

    /**
     * Advances the position of a given Vehicle object to the
     * next available one.
     * @param vehicle Vehicle object
     */
    public void advancePosition(Vehicle vehicle) {
        vehicleLock.lock();
        // Gets the current position of the Vehicle
        Position currPos = vehicle.getPosition();
        // Gets the next position of the Vehicle
        Position nextPos = getNextPosition(vehicle.getPosition(), vehicle.getDirection());

        // Try to move the Vehicle to the next position if it is unoccupied
        try {
            while (isOccupied(nextPos)) {
                condition.await();
            }

            // Moves the vehicle from one its current position to its new position
            trafficGrid[nextPos.getHorzPosition()][nextPos.getVertPosition()] = trafficGrid[currPos.getHorzPosition()][currPos.getVertPosition()];
            trafficGrid[currPos.getHorzPosition()][currPos.getVertPosition()] = null;
            // Sets the vehicle's position
            vehicle.setPosition(nextPos);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            vehicleLock.unlock();
        }
    }

    /**
     * Checks if the Position in the grid is occupied by a car.
     * If unoccupied - signals the waiting threads.
     * @param pos Position to check
     * @return boolean whether the row is occupied
     */
    private boolean isOccupied(Position pos) {
        if (trafficGrid[pos.getHorzPosition()][pos.getVertPosition()] != null) {
            return true;
        } else {
            condition.signalAll();
            return false;
        }
    }

    /**
     * Gets the next Position according to a Direction.
     * @param pos Position object
     * @param dir Direction object
     * @return Position the next Position
     */
    private Position getNextPosition(Position pos, Direction dir) {
        int nextRow = pos.getHorzPosition();
        int nextCol = pos.getVertPosition();

        if (dir == Direction.West)
            nextCol++;
        else if (dir == Direction.East)
            nextCol--;
        else if (dir == Direction.South)
            nextRow++;
        else
            nextRow--;

        return new Position(nextRow, nextCol);
    }

    /**
     * Checks if a Vehicle can move further along the grid.
     * If false - the Vehicle has reached the last position
     * possible.
     * @param v Vehicle object
     * @return boolean whether the vehicle can move further
     */
    public boolean canMove(Vehicle v) {
        Position pos = v.getPosition();
        Direction dir = v.getDirection();

        if (dir == Direction.West)
            return pos.getVertPosition() != (trafficGrid[0].length - 1);
        else if (dir == Direction.East)
            return pos.getVertPosition() != 0;
        else if (dir == Direction.South)
            return pos.getHorzPosition() != (trafficGrid.length - 1);
        else
            return pos.getHorzPosition() != 0;
    }

    /**
     * Removes the Vehicle from the grid. This should only be
     * called after the Vehicle has reached its final position.
     * @param v Vehicle object
     */
    public void removeVehicle(Vehicle v) {
        Position pos = v.getPosition();
        trafficGrid[pos.getHorzPosition()][pos.getVertPosition()] = null;
    }

    /**
     * Reverse the direction of the rows in the given range.
     * @param from from row (inclusive)
     * @param to to row (exclusive)
     */
    public void reverseHorizontalDirection(int from, int to) {
        for (int i = from; i < to; i++) {
            if (horizontalDirections[i] == Direction.West)
                horizontalDirections[i] = Direction.East;
            else
                horizontalDirections[i] = Direction.West;
        }
    }

    /**
     * Reverse the direction of the columns in the given range.
     * @param from from row (inclusive)
     * @param to to row (exclusive)
     */
    public void reverseVerticalDirection(int from, int to) {
        for (int i = from; i < to; i++) {
            if (verticalDirections[i] == Direction.South)
                verticalDirections[i] = Direction.North;
            else
                verticalDirections[i] = Direction.South;
        }
    }
}