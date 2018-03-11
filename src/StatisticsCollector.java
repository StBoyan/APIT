import java.util.ArrayList;
import java.util.Collections;

/**
 * A class that collects time of existence of Vehicle threads
 * spawned by a generator. It can calculate various statistical
 * measures and produce a report to the standard output. There is no
 * need to explicitly call the run method of this class. It gets
 * triggered automatically upon program termination..
 *
 * @author Boyan Stoynov
 */
public class StatisticsCollector extends Thread {
    /* Generator name */
    private final String name;
    /* Travel time for each vehicle */
    private final ArrayList<Long> times;

    /**
     * Creates a StatisticCollector object.
     * @param name name of the generator
     */
    public StatisticsCollector(String name) {
        this.name = name;
        times = new ArrayList<>();
        //Triggers the run method upon JVM termination
        Runtime.getRuntime().addShutdownHook(this);
    }

    /**
     * Logs the nano time a single vehicle took to traverse
     * the grid.
     * @param nanoTime vehicle nano time
     */
    public synchronized void logVehicleTime(long nanoTime) {
        times.add(nanoTime);
    }

    /**
     * Returns the minimum vehicle travel time as double.
     * @return double minimum vehicle travel time
     */
    private double getMinAsDouble() {
        return (double) Collections.min(times);
    }

    /**
     * Returns the maximum vehicle travel time as double.
     * @return double minimum vehicle travel time
     */
    private double getMaxAsDouble() {
        return (double) Collections.max(times);
    }

    /**
     * Returns the mean vehicle travel time as double.
     * @return double mean travel time
     */
    private double getMeanAsDouble() {
        double sum = 0.0;

        for (Long time : times) {
            sum+= time.doubleValue();
        }

        return sum/times.size();
    }

    /**
     * Returns the variance of vehicle travel times as double.
     * @return double variance of travel times
     */
    private double getVarianceAsDouble() {
        double mean = getMeanAsDouble();
        double temp = 0.0;

        for (Long time : times) {
            temp += (time.doubleValue() - mean) * (time.doubleValue() - mean);
        }

        return temp/(times.size());
    }

    /**
     * Calculates and formats the statistics for the population
     * and prints them to the standard output stream. This is triggered
     * as soon as the simulation is finished.
     */
    @Override
    public void run() {
        StringBuilder sb = new StringBuilder();

        sb.append("\nReport for Generator - ").append(name).append("\n");
        sb.append("========================================\n");
        sb.append("Number of vehicles generated : ").append(times.size()).append("\n");

        double min = getMinAsDouble() / 100000000.0;
        sb.append("Minimum travel time : ").append(String.format("%.5f", min)).append("s\n");

        double max = getMaxAsDouble() / 100000000.0;
        sb.append("Maximum travel time : ").append(String.format("%.5f", max)).append("s\n");

        double mean = getMeanAsDouble() / 100000000.0;
        sb.append("Mean travel time : ").append(String.format("%.5f", mean)).append("s\n");

        double variance = getVarianceAsDouble() / 100000000.0;
        sb.append("Variance of travel times : ").append(String.format("%.5f", variance)).append("\n");

        System.out.print(sb.toString());
    }
}
