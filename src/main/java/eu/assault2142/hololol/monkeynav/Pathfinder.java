package eu.assault2142.hololol.monkeynav;

import com.jme3.math.Vector3f;
import eu.assault2142.hololol.monkeynav.data.Path;

/**
 * An interface for all pathfinders. You can find some implementations in the
 * pathfinders-Package
 *
 * @author hololol2
 */
public interface Pathfinder {

    public Path computePath(Vector3f start, Vector3f dest);
}
