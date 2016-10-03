package eu.assault2142.hololol.monkeynav.data;

import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import java.util.HashMap;

/**
 * Stores the navigation info of a game-map
 *
 * @author hololol2
 */
public class GridMap {

    private final HashMap<Integer, Grid> grid;
    private final float minx;
    private final float minz;
    private final float cellsize;
    private final int width;

    /**
     * Create a new GridMap
     *
     * @param g the hashmap with the grids
     * @param x the minimum x-coordinate
     * @param z the minimum z-coordinate
     * @param s the length of one cell
     * @param w the width of the grid (in grids)
     */
    public GridMap(HashMap<Integer, Grid> g, float x, float z, float s, int w) {
        grid = g;
        minx = x;
        minz = z;
        cellsize = s;
        width = w;
    }

    /**
     * Get the grid at the given (GridMap-)coordinates
     *
     * @param x the x-position
     * @param z the z-position
     * @return the grid at the specified location
     */
    public Grid getGrid(int x, int z) {
        return grid.get(width * z + x);
    }

    /**
     * Get the grid at the given (real-world-)coordinates
     *
     * @param x the x-coordinate
     * @param z the z-coordinate
     * @return the grid at the specified coordinates
     */
    public Grid getGrid(float x, float z) {
        x = x - minx;
        int nx = (int) Math.floor(x / cellsize);
        z = z - minz;
        int nz = (int) Math.floor(z / cellsize);
        return getGrid(nx, nz);
    }

    /**
     * Blocks an area around a location
     *
     * @param center the center of the area
     * @param length the width of the area
     */
    public void blockArea(Vector3f center, float length) {
        Grid gr = getGrid(center.x, center.z);
        if (gr == null) {
            return;
        }
        int gx = gr.getGridX();
        int gy = gr.getGridY();
        gr.setWalkable(0);
        int d = (int) FastMath.ceil(length / cellsize);
        d++;
        for (int x = -d; x < d; x++) {
            for (int y = -d; y < d; y++) {
                if (getGrid(gx + x, gy + y) != null) {
                    getGrid(gx + x, gy + y).setWalkable(0);
                }
            }
        }
    }
}
