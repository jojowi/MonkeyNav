/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.monkeynav.data;

import com.jme3.math.Vector2f;

/**
 * A single Grid in the map. Stores info about its position, neighbors and the
 * terrain
 *
 * @author hololol2
 */
public class Grid {

    /**
     * The direction of the neighbor
     */
    public static enum DIRECTION {

        NORTH, EAST, SOUTH, WEST
    }

    private final float centerx;
    private final float centery;
    private Grid neighbourn;
    private Grid neighboure;
    private Grid neighbours;
    private Grid neighbourw;
    private final int x;
    private final int y;
    private int walkable;

    /**
     * Creates a new grid
     *
     * @param cx the x-coordinate
     * @param cy the y-coordinate
     * @param x the x-number
     * @param y the y-number
     */
    public Grid(float cx, float cy, int x, int y) {
        centerx = cx;
        centery = cy;
        this.x = x;
        this.y = y;
        walkable = 1;
    }

    /**
     * Returns the center of the Grid
     *
     * @return the (real-world-)coordinates of the center
     */
    public Vector2f getCenter() {
        return new Vector2f(centerx, centery);
    }

    /**
     * Returns the neighbor in the given direction
     *
     * @param direction one of NORD, EAST, SOUTH, WEST
     * @return the neighbor
     */
    public Grid getNeighbor(DIRECTION direction) {
        Grid result;
        switch (direction) {
            case NORTH:
                result = neighbourn;
                break;
            case EAST:
                result = neighboure;
                break;
            case SOUTH:
                result = neighbours;
                break;
            default:
                result = neighbourw;
        }
        return result;
    }

    /**
     * Sets a neighbor of this grid
     *
     * @param direction the direction of the neighbor
     * @param n the neighbor-grid
     */
    public void setNeighbor(DIRECTION direction, Grid n) {
        switch (direction) {
            case NORTH:
                neighbourn = n;
                break;
            case EAST:
                neighboure = n;
                break;
            case SOUTH:
                neighbours = n;
                break;
            default:
                neighbourw = n;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (Grid.class == o.getClass()) {
            Grid other = (Grid) o;
            return getCenter().equals(other.getCenter());
        }
        return super.equals(o);
    }

    /**
     * Returns all neighbors
     *
     * @return all 4 neighbors as an array
     */
    public Grid[] getAllNeigbors() {
        return new Grid[]{neighbourn, neighboure, neighbours, neighbourw};
    }

    /**
     * Returns the number of neighbors
     *
     * @return number of neighbors this grid has (0-4)
     */
    public int getNeigborCount() {
        int c = 0;
        if (neighboure != null) {
            c++;
        }
        if (neighbourn != null) {
            c++;
        }
        if (neighbours != null) {
            c++;
        }
        if (neighbourw != null) {
            c++;
        }
        return c;
    }

    /**
     * Returns the gridmap-x-coordinate of this grid
     *
     * @return the x-coordinate (in the gridmap!)
     */
    public int getGridX() {
        return x;
    }

    /**
     * Returns the gridmap-y-coordinate of this grid
     *
     * @return the y-coordinate (in the gridmap!)
     */
    public int getGridY() {
        return y;
    }

    /**
     * Returns the walkable-info of this grid
     *
     * @return true if the grid is walkable
     */
    public boolean isWalkable() {
        return walkable != 0;
    }

    /**
     * Set if the grid is walkable
     *
     * @param w whether the grid should be walkable
     */
    public void setWalkable(int w) {
        walkable = w;
    }
}
