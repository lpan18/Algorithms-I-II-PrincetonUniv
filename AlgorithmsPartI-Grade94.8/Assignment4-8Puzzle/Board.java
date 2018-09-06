import java.util.Iterator;

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdRandom;

public class Board {
    private final int n;
    private int[][] board;
    private int zeroI;
    private int zeroJ;

    public Board(int[][] blocks) {    //construct a board from an n-by-n array of blocks
        n = blocks.length;
        board = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (blocks[i][j] == 0) {
                    zeroI = i;
                    zeroJ = j;
                } else {
                    board[i][j] = blocks[i][j];
                }
            }
        }
    }



    public int dimension() {          //board dimension n
        return n;
    }

    public int hamming() {           //number of blocks out of place
        int wrongBlocks = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] !=0 && board[i][j] != i * n + j + 1) {
                    wrongBlocks++;
                }
            }
        }
        return wrongBlocks;

    }

    public int manhattan() {        // sum of Manhattan distances between blocks and goal
        int wrongDist = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (board[i][j] !=0) {
                    wrongDist += Math.abs(i - ((board[i][j] - 1) / n)) + Math.abs(j - ((board[i][j] - 1) % n));
                }
            }
        }
        return wrongDist;

    }

    public boolean isGoal() {        // is this board the goal board?
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (!(i == zeroI && j == zeroJ) && (board[i][j] != i * n + j + 1)) {
                    return false;
                }
            }
        }
        return true;
    }

    public Board twin() {        // a board that is obtained by exchanging any pair of blocks
         Board twin = new Board(board);
        int rowSwap = 0;
        if (twin.board[0][0] == 0 || twin.board[0][1] == 0) {
            rowSwap = 1;
        }

        twin.board[rowSwap][0] = board[rowSwap][1];
        twin.board[rowSwap][1] = board[rowSwap][0];
        return twin;
     }
        /*public Board twin() {
            int[][] newBoard = new int[dimension()][dimension()];
            for (int i = 0; i < dimension(); i++) {
                for (int j = 0; j < dimension(); j++) {
                    newBoard[i][j] = board[i][j];
                }
            }
            int rowSwap = 0;
            if (newBoard[0][0] == 0 || newBoard[0][1] == 0) {
                rowSwap = 1;
            }

            int temp = newBoard[rowSwap][0];
            newBoard[rowSwap][0] = newBoard[rowSwap][1];
            newBoard[rowSwap][1] = temp;
            return new Board(newBoard);
        }*/

    public boolean equals(Object y) {            // does this board equal y?
        if (y == null) {
            return false;
        }
        if (!y.getClass().equals(this.getClass())) {
            return false;
        }
        Board boardY = (Board) y;
        if (boardY.n != this.n) {
            return false;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (boardY.board[i][j] != board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {      // all neighboring boards
        return new NeighborIterable();
    }

    private class NeighborIterable implements Iterable<Board> {
        private final Queue<Board> neighbors = new Queue<Board>();

        public Iterator<Board> iterator() {
            if (zeroI > 0) {
                Board upNeighbor = new Board(board);
                upNeighbor.board[zeroI][zeroJ] = upNeighbor.board[zeroI - 1][zeroJ];
                upNeighbor.board[zeroI - 1][zeroJ] = 0;
                --upNeighbor.zeroI;
                neighbors.enqueue(upNeighbor);
            }

            if (zeroI < n - 1) {
                Board downNeighbor = new Board(board);
                downNeighbor.board[zeroI][zeroJ] = downNeighbor.board[zeroI + 1][zeroJ];
                downNeighbor.board[zeroI + 1][zeroJ] = 0;
                ++downNeighbor.zeroI;
                neighbors.enqueue(downNeighbor);
            }
            if (zeroJ > 0) {
                Board leftNeighbor = new Board(board);
                leftNeighbor.board[zeroI][zeroJ] = leftNeighbor.board[zeroI][zeroJ - 1];
                leftNeighbor.board[zeroI][zeroJ - 1] = 0;
                --leftNeighbor.zeroJ;
                neighbors.enqueue(leftNeighbor);

            }
            if (zeroJ < n - 1) {
                Board rightNeighbor = new Board(board);
                rightNeighbor.board[zeroI][zeroJ] = rightNeighbor.board[zeroI][zeroJ + 1];
                rightNeighbor.board[zeroI][zeroJ + 1] = 0;
                ++rightNeighbor.zeroJ;
                neighbors.enqueue(rightNeighbor);
            }

            Iterator<Board> it = new Iterator<Board>() {

                public boolean hasNext() {
                    return !neighbors.isEmpty();
                }

                public Board next() {

                    if (hasNext()) {
                        return neighbors.dequeue();
                    } else {
                        throw new java.util.NoSuchElementException();
                    }
                }

            };
            return it;
        }
    }

    public String toString() {         // string representation of this board

        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                s.append(String.format("%2d", board[i][j]) + " ");
            }
            s.append("\n");
        }
        return s.toString();

    }
}

