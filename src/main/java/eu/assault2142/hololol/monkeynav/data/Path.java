package eu.assault2142.hololol.monkeynav.data;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import java.util.ArrayList;

/**
 * Stores a navigation-path
 *
 * @author hololol2
 */
public class Path {

    private final Vector2f[] points;
    private float length;
    private int current;

    /**
     * Creates a new Path
     *
     * @param p the coordinates of the waypoints
     */
    public Path(Vector2f[] p) {
        current = 0;
        points = new Vector2f[p.length];
        for (int a = 0; a < p.length; a++) {
            points[a] = p[a];
        }
        calcLength();
    }

    /**
     * Generates a new Path with the given start- and end-point
     *
     * @param end the last waypoint
     * @param start the first waypoint
     * @return the Path with the given start and destination
     */
    public static Path generatePath(PathingData end, Vector3f start) {
        ArrayList<Vector2f> p = new ArrayList();
        PathingData pd = end;
        while (true) {
            p.add(pd.getCurrent().getCenter());
            if (pd.getFrom() == null) {
                break;
            } else {
                pd = pd.getFrom();
            }
        }
        int l = p.size() - 1;
        Vector2f[] points = new Vector2f[l];
        for (int a = 0; a < l - 1; a++) {
            points[a] = p.get(l - (a + 1));
        }
        points[points.length - 1] = new Vector2f(start.x, start.z);
        return new Path(points);
    }

    /**
     * Calculates the length of the path
     */
    private void calcLength() {
        length = 0;
        for (int a = 0; a < points.length - 1; a++) {
            length += points[a].distance(points[a + 1]);
        }
    }

    /**
     * Returns the current waypoint
     *
     * @return the current waypoint
     */
    public Vector2f getCurrentWaypoint() {
        return points[current];
    }

    /**
     * Select next waypoint
     */
    public void nextWaypoint() {
        current++;
    }

    /**
     * Whether the last waypoint is reached
     *
     * @return whether this path reached its last waypoint
     */
    public boolean isAtGoal() {
        return current + 1 == points.length;
    }

    @Override
    public String toString() {
        String s = "" + points.length;
        for (Vector2f p : points) {
            s += p.toString();
        }
        return s;
    }

    /**
     * Returns the waypoint at the given index
     *
     * @param index the index of a waypoint
     * @return the waypoint at the given index
     */
    public Vector2f getWayPoint(int index) {
        return points[index];
    }

    /**
     * Returns the last waypoint
     *
     * @return the last waypoint
     */
    public Vector2f getLastWayPoint() {
        return points[points.length - 1];
    }

    /**
     * Returns the index of the waypoint
     *
     * @param waypoint a waypoint of this path
     * @return the index of the waypoint in this path, -1 if the path does not
     * contain the waypoint
     */
    public int indexOf(Vector2f waypoint) {
        for (int i = 0; i < points.length; i++) {
            if (points[i] == waypoint) {
                return i;
            }
        }
        return -1;
    }
}
