import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {
    private final ArrayList<LineSegment> segments = new ArrayList<>();

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("constructor argument is null!");
        }

        int n = points.length;

        for (Point p1 : points) {
            if (p1 == null) {
                throw new IllegalArgumentException("null point appears!");
            }
        }

        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (points[i].compareTo(points[j]) == 0) {
                    throw new IllegalArgumentException("Duplicated entries!");
                }
            }
        }

        if (points.length < 4){
            return;
        }

        Point[] points0 = points.clone();
        Arrays.sort(points0);
        Point[] points1 = points0.clone();

        for (int i = 0; i < n; i++) {
            Point base = points0[i];
            SlopeOrder piOrder = new SlopeOrder(base);
            Arrays.sort(points1, piOrder);

            int counts = 1;
            int lo = 1;
            int hi = 2;
            double currentSlope = points1[lo].slopeTo(base);
            while (hi < n) {
                if (currentSlope == points1[hi].slopeTo(base)) {
                    if (base.compareTo(points1[lo]) < 0) {
                        counts++;
                    }

                } else {
                    if (counts >= 3) {
                        segments.add(new LineSegment(base, points1[hi - 1]));
                    }
                    lo = hi;
                    currentSlope = points1[lo].slopeTo(base);
                    counts = 1;
                }

                hi++;
            }

            if (counts >= 3) {
                segments.add(new LineSegment(base, points1[hi - 1]));
            }
        }
    }

    private class SlopeOrder implements Comparator<Point> {
        Point p;

        public SlopeOrder(Point p) {
            this.p = p;
        }

        public int compare(Point q1, Point q2) {
            int comparison = p.slopeOrder().compare(q1, q2);

            if (comparison == 0) {
                return q1.compareTo(q2);
            }
            else {
                return comparison;
            }
        }

    }

    public int numberOfSegments() {

        return segments.size();

    }


    public LineSegment[] segments() {

        return segments.toArray(new LineSegment[segments.size()]);

    }
}