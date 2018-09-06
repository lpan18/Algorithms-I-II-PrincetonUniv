import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {

    private final ArrayList<LineSegment> segments = new ArrayList<>();

    public BruteCollinearPoints(Point[] points) {  // finds all line segments containing 4 points
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
        for (int i = 0; i < n - 3; i++) {
            for (int j = i + 1; j < n - 2; j++) {
                for (int k = j + 1; k < n - 1; k++) {
                    if (points0[i].slopeTo(points0[j]) == points0[i].slopeTo(points0[k])) {
                        for (int m = k + 1; m < n; m++) {
                            if (points0[i].slopeTo(points0[m]) == points0[i].slopeTo(points0[k])) {
                                segments.add(new LineSegment(points0[i], points0[m]));
                            }
                        }

                    }

                }
            }
        }
    }

    public int numberOfSegments() {

        return segments.size();


    }

    public LineSegment[] segments() {
        LineSegment[] arr = new LineSegment[segments.size()];
        return segments.toArray(arr);

    }


}