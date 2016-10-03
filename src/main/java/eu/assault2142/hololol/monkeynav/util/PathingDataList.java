package eu.assault2142.hololol.monkeynav.util;

import eu.assault2142.hololol.monkeynav.data.Grid;
import eu.assault2142.hololol.monkeynav.data.PathingData;

/**
 * A SortedList of PathingData
 *
 * @author hololol2
 */
public class PathingDataList {

    private ListElement first;

    /**
     * Remove and return the first data
     *
     * @return the first element of the list
     */
    public PathingData pop() {
        if (first == null) {
            return null;
        }
        ListElement tmp = first;
        first = first.getNext();
        return tmp.getData();
    }

    /**
     *
     * @return true if the list is empty
     */
    public boolean isEmpty() {
        return first == null;
    }

    /**
     * Checks if the list contains the grid
     *
     * @param grid the grid to search for
     * @return true if data found
     */
    public boolean contains(Grid grid) {
        if (first == null) {
            return false;
        }
        return first.contains(grid);
    }

    /**
     * Searches the list for data to this Grid
     *
     * @param grid the grid to search for
     * @return the data of the grid
     */
    public PathingData get(Grid grid) {
        if (first == null) {
            return null;
        }
        return first.get(grid);
    }

    /**
     * Adds a new PathingData to the list
     *
     * @param pd the data to add
     */
    public void add(PathingData pd) {
        if (first == null || first.getValue() > pd.getG() + pd.getH()) {
            first = new ListElement(pd, first);
        } else {
            first.add(pd);
        }
    }

    /**
     * Removes the PathingData from the list
     *
     * @param pd the data to remove
     */
    public void remove(PathingData pd) {
        if (first == null) {
            return;
        }
        if (pd.equals(first.getData())) {
            first = first.getNext();
        } else {
            first.remove(pd);
        }
    }
}
