import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private static final double MAX_ENERGY = 1000;
    private int[][] pic;
    //private double[][] energy;(put energy to local variable not instance variable)
    //private double[][] disTo;
    //private int[][] edgeTo;

    /**
     * Create a seam carver object based on the given picture. Create int[][]pic in order to use less memory.
     * better than create a new picture form the class Picture.- new Picture(picture)
     *
     * @param picture
     */
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("picture is null!");
        }
        this.pic = new int[picture.width()][picture.height()];
        //energy = new double[width()][height()];
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                pic[x][y] = picture.getRGB(x, y);
                //energy[x][y] = energy(x, y);
            }
        }
    }


    /**
     * Create a new picture by setting the RGB value (by using RGB saved in the int[][] pic).
     *
     * @return picture
     */
    public Picture picture() {
        Picture picture = new Picture(width(), height());
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                picture.setRGB(i, j, pic[i][j]);
            }
        }
        return picture;
    }

    /**
     * Return width of current picture
     *
     * @return width
     */
    public int width() {
        return pic.length;
    }

    /**
     * Return height of current picture
     *
     * @return height
     */
    public int height() {
        return pic[0].length;
    }

    /**
     * Return energy of pixel at column x and row y
     *
     * @param x
     * @param y
     * @return energy of pixel at column x and row y
     */
    public double energy(int x, int y) {
        if (x < 0 || x > width() - 1 || y < 0 || y > height() - 1) {
            throw new IllegalArgumentException("x or y out of prescribed range");
        }
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
            return MAX_ENERGY;
        }
        double deltaX2 = delta2(x - 1, y, x + 1, y);
        double deltaY2 = delta2(x, y - 1, x, y + 1);
        return Math.sqrt(deltaX2 + deltaY2);
    }

    private double delta2(int aX, int aY, int bX, int bY) {
        int rgb1 = pic[aX][aY];
        int rgb2 = pic[bX][bY];
        int r = ((rgb1 >> 16) & 0xFF) - ((rgb2 >> 16) & 0xFF);
        int g = ((rgb1 >> 8) & 0xFF) - ((rgb2 >> 8) & 0xFF);
        int b = ((rgb1 >> 0) & 0xFF) - ((rgb2 >> 0) & 0xFF);
        return r * r + g * g + b * b;
    }


    /**
     * Return sequence of indices for horizontal seam
     * use transpose to calculate
     *
     * @return horizontal seam as an array
     */
    public int[] findHorizontalSeam() {
        transpose();
        int[] result = findVerticalSeam();
        transpose();
        return result;
    }

    /**
     * Return sequence of indices for vertical seam
     * calculate the disTo (x-1) x (x+1) for each row and then choose the minDist
     * use edgeTo to record the path
     *
     * @return vertical seam as an array
     */
    public int[] findVerticalSeam() {
        double[][] disTo = new double[width()][height()];
        int[][] edgeTo = new int[width()][height()];
        double[][] energy = new double[width()][height()];
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                energy[x][y] = energy(x, y);
            }
        }

        for (int y = 0; y < height(); y++) {
            for (int x = 0; x < width(); x++) {
                if (y == 0) {
                    disTo[x][y] = 0.0;
                } else {
                    disTo[x][y] = Double.POSITIVE_INFINITY;
                }
            }
        }

        for (int y = 0; y < height() - 1; y++) {
            for (int x = 0; x < width(); x++) {
                for (int k = -1; k <= 1; k++) {
                    if (x + k < 0 || x + k > width() - 1) {
                        continue;
                    } else {
                        if (disTo[x + k][y + 1] > disTo[x][y] + energy[x][y]) {
                            disTo[x + k][y + 1] = disTo[x][y] + energy[x][y];
                            edgeTo[x + k][y + 1] = x + y * width();
                        }
                    }
                }


            }
        }

        // find min dist in the last row
        double minDist = Double.POSITIVE_INFINITY;
        int index = -1;
        for (int x = 0; x < width(); x++) {
            if (disTo[x][height() - 1] < minDist) {
                minDist = disTo[x][height() - 1];
                index = x + width() * (height() - 1);
            }
        }

        // find seam one by one form the bottom to the top
        int[] seam = new int[height()];
        for (int i = 0; i < height(); i++) {
            int y = height() - 1 - i;
            int x = index - width() * y;
            seam[height() - 1 - i] = x;
            index = edgeTo[x][y];
        }
        return seam;
    }

    /**
     * transpose the int[][] pic 2-d array to facilitate the calculation in the next dimension
     */
    private void transpose() {
        //Picture transposedPic = new Picture(height(), width());
        int[][] newPic = new int[height()][width()];
        //double[][] transEnergy = new double[height()][width()];
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                //transposedPic.setRGB(y, x, picture.getRGB(x, y));
                newPic[y][x] = pic[x][y];
                // transEnergy[y][x] = energy[x][y];
            }
        }
        //energy = transEnergy;
        //picture = transposedPic;
        //disTo = new double[height()][width()];
        //edgeTo = new int[height()][width()];
        //energy = new int[][];
        pic = newPic;

    }

    /**
     * Remove horizontal seam from current picture
     * use arraycopy. pic[i] represent to the i column of 2d-array pic[][]
     * therefore we need to calculate first hte horizontal and then the vertical
     *
     * @param seam
     */
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("seam is null!");
        }
        if (height() <= 1) {
            throw new IllegalArgumentException("picture size illegal!");
        }
        if (seam.length != width()) {
            throw new IllegalArgumentException("seam length not equal to picture height or width!");
        }
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] > height() - 1) {
                throw new IllegalArgumentException("seam value out of boundary!");
            }
            if (i < width() - 1 && Math.pow(seam[i + 1] - seam[i], 2) > 1) {
                throw new IllegalArgumentException("two adjacent entries of seam differ by more than 1!");
            }
        }
        int[][] newPic = new int[width()][height() - 1];
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] == 0) {
                System.arraycopy(pic[i], 1, newPic[i], 0, height() - 1);
            } else if (seam[i] == height() - 1) {
                System.arraycopy(pic[i], 0, newPic[i], 0, height() - 1);
            } else {
                System.arraycopy(pic[i], 0, newPic[i], 0, seam[i]);
                System.arraycopy(pic[i], seam[i] + 1, newPic[i], seam[i], height() - seam[i] - 1);
            }
        }
        pic = newPic;
    }

    /**
     * Remove vertical seam from current picture
     *
     * @param seam
     */
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("seam is null!");
        }
        transpose();
        removeHorizontalSeam(seam);
        transpose();
    }
}
