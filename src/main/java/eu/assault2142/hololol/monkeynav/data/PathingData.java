/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.monkeynav.data;

/**
 * Contains data for path-generation: the grid we currently look at, the g and h
 * for this grid (see A*), the status (nothing, open, closed) and the
 * predecessor of the grid in this specific sub-path
 *
 * @author hololol2
 */
public class PathingData {

    private PathingData from;
    private float g, h;
    private Grid current;

    /**
     * Create a new PathingData
     *
     * @param c the current Grid
     */
    public PathingData(Grid c) {
        current = c;
        g = 0;
        h = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() == PathingData.class) {
            return current.equals(((PathingData) o).current);
        } else {
            return super.equals(o);
        }
    }

    /**
     * Returns the predecessor in the (sub-)path
     *
     * @return
     */
    public PathingData getFrom() {
        return from;
    }

    /**
     * Sets the predecessor in the (sub-)path
     *
     * @param from the predecessor
     */
    public void setFrom(PathingData from) {
        this.from = from;
    }

    /**
     * Returns the g-value
     *
     * @return the g-value
     */
    public float getG() {
        return g;
    }

    /**
     * Sets the g-value
     *
     * @param g the g-value
     */
    public void setG(float g) {
        this.g = g;
    }

    public void setShortestPath(PathingData from, float g) {
        this.from = from;
        this.g = g;
    }

    /**
     * Returns the h-value
     *
     * @return the h-value
     */
    public float getH() {
        return h;
    }

    /**
     * Sets the h-value
     *
     * @param h the h-value
     */
    public void setH(float h) {
        this.h = h;
    }

    /**
     * Returns the current grid
     *
     * @return the grid of this node
     */
    public Grid getCurrent() {
        return current;
    }

    /**
     * Sets the current grid
     *
     * @param current the grid of this node
     */
    public void setCurrent(Grid current) {
        this.current = current;
    }

}
