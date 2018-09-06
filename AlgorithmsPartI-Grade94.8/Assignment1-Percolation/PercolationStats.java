import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;

public class PercolationStats {
    private final int n;
    private final int trials;
    private Percolation test;
    private double[] perRate;

    public PercolationStats(int n, int trials) {  // perform trials independent experiments on an n-by-n grid
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException(n + " should be greater than 0!");
        }
        this.n = n;
        this.trials = trials;
        this.perRate = new double[trials];

        for (int i = 0; i < trials; i++) {
            test = new Percolation(n);
            runTest(test);
            perRate[i] = (double) test.numberOfOpenSites() / (n * n);
        }
    }

    private void runTest(Percolation test) {
        while (!test.percolates()) {
            int ranNumber = StdRandom.uniform(n * n);
            int row = ranNumber / n + 1;
            int col = ranNumber % n + 1;
            test.open(row, col);

        }
    }

    public double mean() {  // sample mean of percolation threshold
        return StdStats.mean(perRate);
    }

    public double stddev() {                       // sample standard deviation of percolation threshold
        return StdStats.stddev(perRate);
    }

    public double confidenceLo() {                 // low  endpoint of 95% confidence interval
        return mean() - 1.96 * stddev() / Math.sqrt(trials);
    }

    public double confidenceHi() {                 // high endpoint of 95% confidence interval
        return mean() + 1.96 * stddev() / Math.sqrt(trials);
    }

    public static void main(String[] args) {  // test client (optional)
        int n = 20;
        int trials = 1000;

        PercolationStats perStats1 = new PercolationStats(n, trials);

        System.out.println("n= " + perStats1.n + ", trials= " + perStats1.trials);
        StdOut.printf("      mean %10.3f\n", perStats1.mean());
        StdOut.printf("    stddev %10.3f\n", perStats1.stddev());
        System.out.println("95% confidence interval = [" + perStats1.confidenceLo() + " , "
                + perStats1.confidenceHi() + "]");
    }
}
