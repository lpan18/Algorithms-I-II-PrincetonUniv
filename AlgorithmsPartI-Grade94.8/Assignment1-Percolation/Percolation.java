
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final int n;
    private boolean[] grid;
    private final WeightedQuickUnionUF newQF;

    public Percolation(int n) {               // create n-by-n grid, with all sites blocked
        if (n <= 0) {
            throw new IllegalArgumentException(n + " should be greater than 0!");
        }
        this.n = n;
        this.grid = new boolean[n * n];
        this.newQF = new WeightedQuickUnionUF(n * n + 2);
    }

        /* public static void main(String[] args) {  // test client (optional)
        int dim = 10;
        Percolation test1 = new Percolation(dim);
        test1.run();
        System.out.println("OpenSites = " +test1.numberOfOpenSites());

        for (int i = 0; i < dim; i++)
        {
            for (int j =0; j<dim; j++){
                var index = i * dim + j;
                System.out.print(test1.grid[index]);
            }
            System.out.println("");
        } */


    public void open(int row, int col) {    // open site (row, col) if it is not open already
        if (!isvalidRowCol(row, col)) {
            throw new IllegalArgumentException(row + " and " + col + " is not between 1 and " + n);
        }
        if (!isOpen(row, col)) {
            if (row == 1) {
                newQF.union(getIndex(row, col), n * n);
            }
            if (row == n) {
                newQF.union(getIndex(row, col), n * n + 1);
            }

            int gridIndex = getIndex(row, col);
            grid[gridIndex] = true;

            if (row > 1 && isOpen(row - 1, col)) {
                newQF.union(gridIndex, gridIndex - n);
            }
            if (row < n && isOpen(row + 1, col)) {
                newQF.union(gridIndex, gridIndex + n);
            }
            if (col > 1 && isOpen(row, col - 1)) {
                newQF.union(gridIndex - 1, gridIndex);
            }
            if (col < n && isOpen(row, col + 1)) {
                newQF.union(gridIndex + 1, gridIndex);
            }
        }
    }

    public boolean isOpen(int row, int col) {  // is site (row, col) open?
        if (!isvalidRowCol(row, col)) {
            throw new IllegalArgumentException(row + " and " + col + " is not between 1 and " + n);
        }

        return grid[getIndex(row, col)];
    }

    public boolean isFull(int row, int col) { // is site (row, col) full?
        if (!isvalidRowCol(row, col)) {
            throw new IllegalArgumentException(row + " and " + col + " is not between 1 and " + n);
        }

        return newQF.connected(getIndex(row, col), n * n);
    }

    public int numberOfOpenSites() {       // number of open sites
        int count = 0;
        for (int i = 0; i < n * n; i++) {
            if (grid[i]) {
                count++;
            }
        }
        return count;
    }

    public boolean percolates() {             // does the system percolate?
        return newQF.connected(n * n, n * n + 1);
    }

    private boolean isvalidRowCol(int row, int col) {
        return row > 0 && row <= n && col > 0 && col <= n;
    }

    private int getIndex(int row, int col) {
        int index = (row - 1) * n + col - 1;
        return index;
    }
}
