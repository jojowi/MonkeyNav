package eu.assault2142.hololol.monkeynav.util;

import eu.assault2142.hololol.monkeynav.data.Grid;
import eu.assault2142.hololol.monkeynav.data.PathingData;

/**
 * Stores a PathingData and a successor
 *
 * @author hololol2
 */
public class ListElement {

    private final PathingData pathingdata;
    private ListElement next;

    /**
     * Create a new ListElement
     *
     * @param data the data to store
     * @param next the successor
     */
    ListElement(PathingData data, ListElement next) {
        pathingdata = data;
        this.next = next;
    }

    /**
     *
     * @return the data
     */
    PathingData getData() {
        return pathingdata;
    }

    /**
     *
     * @return the successor
     */
    ListElement getNext() {
        return next;
    }

    /**
     * Checks if data for the Grid is present
     *
     * @param grid the grid to search for
     * @return true if data found
     */
    boolean contains(Grid grid) {
        if (pathingdata.getCurrent().equals(grid)) {
            return true;
        }
        if (next == null) {
            return false;
        }
        return next.contains(grid);
    }

    /**
     * Searches for data to this Grid
     *
     * @param grid the grid to search for
     * @return the data of the grid
     */
    PathingData get(Grid grid) {
        if (pathingdata.getCurrent().equals(grid)) {
            return pathingdata;
        }
        if (next == null) {
            return null;
        }
        return next.get(grid);
    }

    /**
     *
     * @return the value of the data (g+h)
     */
    float getValue() {
        return pathingdata.getG() + pathingdata.getH();
    }

    /**
     * Adds a new PathingData
     *
     * @param pd the data to add
     */
    void add(PathingData pd) {
        if (next == null || next.getValue() > pd.getG() + pd.getH()) {
            next = new ListElement(pd, next);
        } else {
            next.add(pd);
        }
    }

    /**
     * Removes the PathingData from the list
     *
     * @param pd the data to remove
     */
    void remove(PathingData pd) {
        if (next == null) {
            return;
        }
        if (pd.equals(next.getData())) {
            next = next.getNext();
        } else {
            next.remove(pd);
        }
    }
}
