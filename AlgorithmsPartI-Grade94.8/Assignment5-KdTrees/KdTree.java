import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private Node root;
    private int size;

    private static class Node {
        private final Point2D p;      // the point
        private final RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private final int level;      // level of node

        public Node(Point2D p, RectHV rect, Node lb, Node rt, int level) {
            this.p = p;
            this.rect = rect;
            this.lb = lb;
            this.rt = rt;
            this.level = level;
        }

        private double compare(Point2D k) {
            if (level % 2 == 0) {
                return k.x() - p.x();
            } else {
                return k.y() - p.y();
            }
        }

        private boolean doesSplitLineIntersect(RectHV queryRect) {
            if (level % 2 == 0) {
                return queryRect.xmin() <= p.x() && p.x() <= queryRect.xmax();
            } else {
                return queryRect.ymin() <= p.y() && p.y() <= queryRect.ymax();
            }
        }

        private boolean isRightOf(RectHV queryRect) {
            if (level % 2 == 0) {
                return queryRect.xmin() < p.x() && queryRect.xmax() < p.x();
            } else {
                return queryRect.ymin() < p.y() && queryRect.ymax() < p.y();
            }
        }
    }

    // construct an empty KdTree
    public KdTree() {
    }

    // is the KdTree empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // number of key-value pairs in this symbol table
    public int size() {
        //return size(root);
        return size;
    }

    // return number of key-value pairs in BST rooted at x
    /*private int size(final Node k) {
        if (k == null) {
            return 0;
        }
        return 1 + size(k.lb) + size(k.rt);
    }*/

    // add the point to the set (if it is not already in the set)
    public void insert(final Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        root = put(root, p, 0, 0, 1, 1, 0);
    }

    private Node put(Node k, Point2D p, double xmin, double ymin, double xmax, double ymax, int level) {
        if (k == null) {
            size++;
            return new Node(p, new RectHV(xmin, ymin, xmax, ymax), null, null, level);
        }
        double comp = k.compare(p);
        if (comp < 0) {
            if (level % 2 == 0) {
                k.lb = put(k.lb, p, xmin, ymin, k.p.x(), ymax, level + 1);
            } else {
                k.lb = put(k.lb, p, xmin, ymin, xmax, k.p.y(), level + 1);
            }
        }
        if (comp >= 0 && !p.equals(k.p)) {
            if (level % 2 == 0) {
                k.rt = put(k.rt, p, k.p.x(), ymin, xmax, ymax, level + 1);
            } else {
                k.rt = put(k.rt, p, xmin, k.p.y(), xmax, ymax, level + 1);
            }
        }
        return k;
    }

    /*
 root = put(root, p, new RectHV(0, 0, 1, 1), 0);
    }

    private Node put(Node k, Point2D p, RectHV rect, int level) {
        if (k == null) return new Node(p, rect, null, null, level);
        double comp = k.compare(p);
        if (comp < 0) {
            if (level % 2 == 0) {
                k.lb = put(k.lb, p, new RectHV(rect.xmin(), rect.ymin(), k.p.x(), rect.ymax()), level + 1);
            } else {
                k.lb = put(k.lb, p, new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), k.p.y()), level + 1);
            }
        }
        if (comp >= 0) {
            if (level % 2 == 0) {
                k.rt = put(k.rt, p, new RectHV(k.p.x(), rect.ymin(), rect.xmax(), rect.ymax()), level + 1);
            } else {
                k.rt = put(k.rt, p, new RectHV(rect.xmin(), p.y(), rect.xmax(), rect.ymax()), level + 1);
            }
        }

        return k;
    }

    private Node put(final Node k, final Point2D p, final RectHV rect, final int level) {
        if (k == null) return new Node(p, rect, null, null, level);
        double comp = k.compare(p);
        RectHV rectLb = null;
        RectHV rectRt = null;
        if (comp < 0 && k.lb == null) {
            if (level % 2 == 0) {
                rectLb = new RectHV(k.rect.xmin(), k.rect.ymin(), k.p.x(), k.rect.ymax());
            } else {
                rectLb = new RectHV(k.rect.xmin(), k.rect.ymin(), k.rect.xmax(), k.p.y());
            }
        }
        if (comp >= 0 && k.rt == null) {
            if (level % 2 == 0) {
                rectRt = new RectHV(k.p.x(), k.rect.ymin(), k.rect.xmax(), k.rect.ymax());
            } else {
                rectRt = new RectHV(k.rect.xmin(), k.p.y(), k.rect.xmax(), k.rect.ymax());
            }
        }
        if (comp < 0) {
            k.lb = put(k.lb, p, rectLb, level + 1);
        } else if (comp > 0) {
            k.rt = put(k.rt, p, rectRt, level + 1);
        } else if (!p.equals(k.p) && comp == 0) {
            k.rt = put(k.rt, p, rectRt, level + 1);
        }
        return k;
    }*/

    // does the KdTree contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return get(root, p, 0) != null;
    }

    private Point2D get(Node k, Point2D p, int level) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (k == null) {
            return null;
        }
        double comp = k.compare(p);
        if (comp < 0)
            return get(k.lb, p, level + 1);
        else if (comp > 0)
            return get(k.rt, p, level + 1);
        else if (!p.equals(k.p) && comp == 0)
            return get(k.rt, p, level + 1);
        else return k.p;
    }

// draw all points to standard draw

    public void draw() {
        draw(root);
    }

    private void draw(Node k) {
        if (k == null) {
            return;
        }
        StdDraw.point(k.p.x(), k.p.y());
        draw(k.rt);
        draw(k.lb);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        List<Point2D> solution = new ArrayList<Point2D>();
        range(root, rect, solution);
        return solution;
    }

    private void range(Node k, RectHV rect, List<Point2D> solution) {
        if (k == null) {
            return;
        }
        if (rect.contains(k.p)) {
            solution.add(k.p);
        }
        if (k.doesSplitLineIntersect(rect)) {
            range(k.lb, rect, solution);
            range(k.rt, rect, solution);
        } else {
            if (k.isRightOf(rect)) {
                range(k.lb, rect, solution);
            } else {
                range(k.rt, rect, solution);
            }
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            return null;
        } else {
            /* return nearest(root, p, null); */
            return nearest(root, p, root.p);
        }
    }

    private Point2D nearest(Node k, Point2D p, Point2D nearest) {
        if (k == null) {
            return nearest;
        }
        if (k.p.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
            nearest = k.p;
        }
        double comp = k.compare(p);
        if (comp < 0) {
            nearest = nearest(k.lb, p, nearest);
            if (k.rt != null && k.rt.rect.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
                nearest = nearest(k.rt, p, nearest);
            }
        } else if (comp >= 0 && !p.equals(k.p)) {
            nearest = nearest(k.rt, p, nearest);
            if (k.lb!= null && k.lb.rect.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
                nearest = nearest(k.lb, p, nearest);
            }
        }
        return nearest;
    }
/*
    public static void main(String[] args) {
        KdTree kdtree = new KdTree();
        kdtree.insert(new Point2D(0.7, 0.2));
        kdtree.insert(new Point2D(0.5, 0.4));
        kdtree.insert(new Point2D(0.2, 0.3));
        kdtree.insert(new Point2D(0.4, 0.7));
        kdtree.insert(new Point2D(0.9, 0.6));
        RectHV rect = new RectHV(0.12, 0.16, 0.69, 0.82);
        for (Point2D p : kdtree.range(rect))
            System.out.print(p.x() + " " + p.y());
    }*/

}