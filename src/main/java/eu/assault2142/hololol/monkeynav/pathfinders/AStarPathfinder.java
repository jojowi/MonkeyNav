/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.monkeynav.pathfinders;

import com.jme3.math.Vector3f;
import eu.assault2142.hololol.monkeynav.Pathfinder;
import eu.assault2142.hololol.monkeynav.data.Grid;
import eu.assault2142.hololol.monkeynav.data.GridMap;
import eu.assault2142.hololol.monkeynav.data.Path;
import eu.assault2142.hololol.monkeynav.data.PathingData;
import eu.assault2142.hololol.monkeynav.util.PathingDataList;

/**
 *
 * @author hololol2
 */
public class AStarPathfinder implements Pathfinder {

    private GridMap map;
    private PathingDataList open;
    private PathingDataList closed;

    /**
     * Creates a new Pathfinder
     *
     * @param gm the map to find paths on
     */
    public AStarPathfinder(GridMap gm) {
        map = gm;
    }

    @Override
    public Path computePath(Vector3f start, Vector3f dest) {
        PathingData current;
        final Grid gstart = map.getGrid(start.x, start.z);
        final Grid gdest = map.getGrid(dest.x, dest.z);
        open = new PathingDataList();
        closed = new PathingDataList();
        open.add(new PathingData(gstart));
        while (!open.isEmpty()) {
            current = open.pop();
            if (current.getCurrent().equals(gdest)) {
                return Path.generatePath(current, start);
            }
            closed.add(current);
            expandNode(current);
        }
        return null;
    }

    private void expandNode(PathingData current) {
        for (Grid grid : current.getCurrent().getAllNeigbors()) {
            if (closed.contains(grid)) {
                continue;
            }
            PathingData next;
            if (open.contains(grid)) {
                next = open.get(grid);
            } else {
                next = new PathingData(grid);
                next.setG(Float.POSITIVE_INFINITY);
            }
            float g = current.getG() + distance(current, next);
            if (g < next.getG()) {
                next.setG(g);
            }
            open.remove(next);
            open.add(next);
        }
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
