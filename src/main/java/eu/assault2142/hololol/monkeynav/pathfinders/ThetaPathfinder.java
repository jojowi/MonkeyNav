/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.monkeynav.pathfinders;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import eu.assault2142.hololol.monkeynav.Pathfinder;
import eu.assault2142.hololol.monkeynav.data.Grid;
import eu.assault2142.hololol.monkeynav.data.GridMap;
import eu.assault2142.hololol.monkeynav.data.Path;
import eu.assault2142.hololol.monkeynav.data.PathingData;
import eu.assault2142.hololol.monkeynav.util.PathingDataList;

/**
 *
 * @author jojow
 */
public class ThetaPathfinder implements Pathfinder {

    private final GridMap map;
    private PathingDataList open;

    /**
     * Creates a new Pathfinder
     *
     * @param gm the map to find paths on
     */
    public ThetaPathfinder(GridMap gm) {
        map = gm;
    }

    /**
     * Computes a new path
     *
     * @param start the starting point
     * @param dest the destination
     * @return the Path object or null
     */
    @Override
    public Path computePath(Vector3f start, Vector3f dest) {
        final Grid gstart = map.getGrid(start.x, start.z);
        final Grid gdest = map.getGrid(dest.x, dest.z);
        if (gstart == null || gdest == null) {
            return null;
        }
        if (gstart.equals(gdest)) {
            return new Path(new Vector2f[]{new Vector2f(start.x, start.z), new Vector2f(dest.x, dest.z)});
        }
        PathingDataList closed = new PathingDataList();
        open = new PathingDataList();
        PathingData pd = new PathingData(gstart);
        PathingData pdnext;
        pd.setG(0);
        open.add(pd);
        while (!open.isEmpty()) {
            pd = open.pop();
            if (pd.getCurrent().equals(gdest)) {
                return Path.generatePath(pd, dest);
            }
            closed.add(pd);
            for (Grid n : pd.getCurrent().getAllNeigbors()) {
                if (n != null && n.isWalkable()) {
                    if (open.contains(n)) {
                        pdnext = open.get(n);
                    } else {
                        pdnext = new PathingData(n);
                    }
                    if (!closed.contains(n)) {
                        if (!open.contains(n)) {
                            pdnext.setG(Float.POSITIVE_INFINITY);
                        }
                        updateVertex(pd, pdnext);
                    }
                }
            }
        }
        return null;
    }

    /**
     * Processes a single Point of the Path
     *
     * @param a the point before
     * @param b the point
     */
    private void updateVertex(PathingData a, PathingData b) {
        float gold = b.getG();
        computeCost(a, b);
        if (b.getG() < gold) {
            open.remove(b);
            open.add(b);
        }
    }

    /**
     * Computes the cost to reach a given point
     *
     * @param a the point before
     * @param b the point
     */
    private void computeCost(PathingData a, PathingData b) {
        if (a == null || b == null) {
            return;
        }
        float distance;
        if (lineOfSight(a.getFrom(), b)) {
            distance = a.getFrom().getG() + distance(a.getFrom(), b);
            if (distance < b.getG()) {
                b.setShortestPath(a.getFrom(), distance);
            }
        } else {
            distance = a.getG() + distance(a, b);
            if (distance < b.getG()) {
                b.setShortestPath(a, distance);
            }
        }
    }

    /**
     * Checks whether there exists a line of sight between the two point
     *
     * @param a point 1
     * @param b point 2
     * @return true if there's a line of sight, false otherwise
     */
    private boolean lineOfSight(PathingData a, PathingData b) {
        if (a == null || b == null) {
            return false;
        }
        int x0, x1, y0, y1, dx, dy, f, sx, sy;
        x0 = a.getCurrent().getGridX();
        x1 = b.getCurrent().getGridX();
        y0 = a.getCurrent().getGridY();
        y1 = b.getCurrent().getGridY();
        dx = x1 - x0;
        dy = y1 - y0;
        f = 0;
        if (dy < 0) {
            dy = -dy;
            sy = -1;
        } else {
            sy = 1;
        }
        if (dx < 0) {
            dx = -dx;
            sx = -1;
        } else {
            sx = 1;
        }
        if (dx >= dy) {
            while (x0 != x1) {
                f = f + dy;
                Grid g;
                if (f >= dx) {
                    g = map.getGrid(x0 + ((sx - 1) / 2), y0 + ((sy - 1) / 2));
                    if (g == null || !g.isWalkable()) {
                        return false;
                    }
                    y0 = y0 + sy;
                    f = f - dx;
                }
                g = map.getGrid(x0 + ((sx - 1) / 2), y0 + ((sy - 1) / 2));
                if (f != 0 && (g == null || !g.isWalkable())) {
                    return false;
                }
                g = map.getGrid(x0 + ((sx - 1) / 2), y0);
                Grid g2 = map.getGrid(x0 + ((sx - 1) / 2), y0 - 1);
                if (dy == 0 && (g == null || !g.isWalkable()) && (g2 == null || !g2.isWalkable())) {
                    return false;
                }
                x0 = x0 + sx;
            }
        } else {
            while (y0 != y1) {
                f = f + dx;
                Grid g;
                if (f >= dy) {
                    g = map.getGrid(x0 + ((sx - 1) / 2), y0 + ((sy - 1) / 2));
                    if (g == null || !g.isWalkable()) {
                        return false;
                    }
                    x0 = x0 + sx;
                    f = f - dy;
                }
                g = map.getGrid(x0 + ((sx - 1) / 2), y0 + ((sy - 1) / 2));
                if (f != 0 && (g == null || !g.isWalkable())) {
                    return false;
                }
                g = map.getGrid(x0, y0 + ((sy - 1) / 2));
                Grid g2 = map.getGrid(x0 - 1, y0 + ((sy - 1) / 2));
                if (dx == 0 && (g == null || !g.isWalkable()) && (g2 == null || !g2.isWalkable())) {
                    return false;
                }
                y0 = y0 + sy;
            }
        }
        return true;
    }

    /**
     * Calculates the line-distance between two points
     *
     * @param g1 point 1
     * @param g2 point 2
     * @return the length of the straight line
     */
    private float distance(PathingData g1, PathingData g2) {
        if (g1 == null || g2 == null) {
            return Float.POSITIVE_INFINITY;
        }
        return g1.getCurrent().getCenter().distance(g2.getCurrent().getCenter());
    }
}
