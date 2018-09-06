import java.util.Comparator;
import java.util.Iterator;


import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private int answerMoves;
    private SearchNode answerLastNode;
    private final Stack<Board> solutions = new Stack<Board>();

    public Solver(Board initial) {       // find a solution to the initial board
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        answerMoves = -1;
        answerLastNode = null;
        MinPQ<SearchNode> search = initSearchNode(initial);
        Board twinInitial = initial.twin();
        MinPQ<SearchNode> twinSearch = initSearchNode(twinInitial);
        while (!search.isEmpty()) {
            SearchNode minSearch = search.delMin();
            SearchNode minTwinSearch = twinSearch.delMin();
            if (minSearch.board.isGoal()) {
                answerMoves = minSearch.moves;
                answerLastNode = minSearch;
                return;
            }
            if (minTwinSearch.board.isGoal()) {
                answerMoves = -1;
                answerLastNode = null;
                return;
            }
            extendNode(search, minSearch);
            extendNode(twinSearch, minTwinSearch);
        }
    }

    private class SearchNode {
        public Board board;
        public int moves;
        public SearchNode preSN;
        public int manHat;

        public SearchNode(Board board){
            this.board = board;
            moves = 0;
            preSN = null;
            manHat = board.manhattan();
        }

        public SearchNode(Board board, int moves, SearchNode preSN){
            this.board = board;
            this.moves = moves;
            this.preSN = preSN;
            manHat = board.manhattan();
        }
    }

    private class SnComp implements Comparator<SearchNode> {
        public int compare(SearchNode sn1, SearchNode sn2) {
            int priority1 = sn1.manHat + sn1.moves;
            int priority2 = sn2.manHat + sn2.moves;
            return Integer.compare(priority1, priority2);
        }
    }

    private MinPQ<SearchNode> initSearchNode(Board init) {
        SnComp sncomp = new SnComp();
        SearchNode initNode = new SearchNode(init);
        MinPQ<SearchNode> searchNodes = new MinPQ<>(1, sncomp);
        searchNodes.insert(initNode);
        return searchNodes;
    }

    private void extendNode(MinPQ<SearchNode> searchNodes, SearchNode curSearchNode) {
        Iterator<Board> it = curSearchNode.board.neighbors().iterator();
        while (it.hasNext()) {
            SearchNode newSearchNode = new SearchNode(it.next(), curSearchNode.moves + 1, curSearchNode);
            if (newSearchNode.board != null && curSearchNode.preSN != null && newSearchNode.board.equals(curSearchNode.preSN.board)) {
                continue;
            }
            searchNodes.insert(newSearchNode);
        }
    }

    public boolean isSolvable() {            // is the initial board solvable?
        return answerLastNode != null;
    }

    public int moves() {     // min number of moves to solve initial board; -1 if unsolvable
        return answerMoves;
    }

    public Iterable<Board> solution() {          //sequence of board in a shortest solution; null if unsolvable
        if (isSolvable()) {
            return new SolutionIterable();
        }

        return null;
    }

    private class SolutionIterable implements Iterable<Board> {
        public Iterator<Board> iterator() {
            SearchNode curNode = answerLastNode;

            if(answerLastNode==null){
                solutions.push(null);
            }

            while (curNode != null) {
                solutions.push(curNode.board);
                curNode = curNode.preSN;
            }

            Iterator<Board> it = new Iterator<Board>() {
                public boolean hasNext() {
                    return !solutions.isEmpty();
                }
                public Board next() {
                    if (hasNext()) {
                        return solutions.pop();
                    } else {
                        throw new java.util.NoSuchElementException();
                    }

                }

            };
            return it;
        }
    }
}
