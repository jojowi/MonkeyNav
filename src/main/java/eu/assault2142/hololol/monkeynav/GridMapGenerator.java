/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.monkeynav;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;
import com.jme3.scene.mesh.IndexBuffer;
import com.jme3.terrain.Terrain;
import eu.assault2142.hololol.monkeynav.data.Grid;
import eu.assault2142.hololol.monkeynav.data.GridMap;
import java.nio.FloatBuffer;
import java.util.HashMap;
import org.critterai.nmgen.HeightSpan;
import org.critterai.nmgen.SolidHeightfield;
import org.critterai.nmgen.SolidHeightfieldBuilder;

/**
 *
 * @author hololol2
 */
public class GridMapGenerator {

    SolidHeightfieldBuilder builder;
    private static final float CELLSIZE = 5f;
    private static final float CELLHEIGHT = 1.5f;
    private static final float MINTRAVERSABLEHEIGHT = 7.5f;
    private static final float MAXTRAVERSABLESTEP = 1f;
    private static final float MAXTRAVERSABLESLOPE = 30.0f;
    private static final boolean CLIPLEDGES = false;
    float fx, fz;

    private GridMapGenerator(float cellSize, float cellHeight, float minTraversableHeight, float maxTraversableStep, float maxTraversableSlope, boolean clipLedges) {
        // Convert certain values from world units to voxel units.
        int vxMinTraversableHeight = 1;
        if (minTraversableHeight != 0) {
            vxMinTraversableHeight = (int) Math.ceil(
                    Math.max(Float.MIN_VALUE, minTraversableHeight)
                    / Math.max(Float.MIN_VALUE, cellHeight));
        }
        int vxMaxTraversableStep = 0;
        if (maxTraversableStep != 0) {
            vxMaxTraversableStep = (int) Math.ceil(
                    Math.max(Float.MIN_VALUE, maxTraversableStep)
                    / Math.max(Float.MIN_VALUE, cellHeight));
        }
        builder = new SolidHeightfieldBuilder(cellSize, cellHeight, vxMinTraversableHeight, vxMaxTraversableStep, maxTraversableSlope, clipLedges);
    }

    /**
     * Create a new GridMapGenerator with default values
     *
     * @return a default Generator
     */
    public static GridMapGenerator createGenerator() {
        return new GridMapGenerator(CELLSIZE, CELLHEIGHT, MINTRAVERSABLEHEIGHT, MAXTRAVERSABLESTEP, MAXTRAVERSABLESLOPE, CLIPLEDGES);
    }

    /**
     * Greate a new GridMapGenerator with customized values. For further
     * documentation of the Parameters also see
     * http://critterai.org/projects/nmgen_study/config.html
     *
     * @param cellSize the width and depth resolution
     * @param cellHeight the height resolution
     * @param minTraversableHeight minimum floor to ceiling height
     * @param maxTraversableStep maximum ledge height considered traversable
     * @param maxTraversableSlope maximum traversable slope (in degrees)
     * @param clipLedges wether ledges should be considered un-walkable
     * @return a customized Generator
     */
    public static GridMapGenerator createGenerator(float cellSize, float cellHeight, float minTraversableHeight, float maxTraversableStep, float maxTraversableSlope, boolean clipLedges) {
        return new GridMapGenerator(cellSize, cellHeight, minTraversableHeight, maxTraversableStep, maxTraversableSlope, clipLedges);
    }

    /**
     * Build a new GridMap of the given Mesh
     *
     * @param mesh the Mesh to be GridMapped
     * @return a GridMap-Representation of the the given Mesh
     */
    public GridMap build(Mesh mesh) {
        FloatBuffer pb = mesh.getFloatBuffer(VertexBuffer.Type.Position);
        IndexBuffer ib = mesh.getIndexBuffer();

        // copy positions to float array
        float[] positions = new float[pb.capacity()];
        pb.clear();
        pb.get(positions);

        // generate int array of indices
        int[] indices = new int[ib.size()];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = ib.get(i);
        }
        SolidHeightfield heightfield = builder.build(positions, indices);

        fx = heightfield.boundsMin()[0];
        fz = heightfield.boundsMin()[2];
        float cellsize = heightfield.cellSize();
        HashMap<Integer, Vector2f> points = new HashMap();
        HashMap<Integer, Grid> grids = new HashMap();
        for (int x = 0; x < heightfield.width(); x++) {
            for (int y = 0; y < heightfield.depth(); y++) {
                HeightSpan span = heightfield.getData(x, y);
                if (span.flags() == 1) {
                    points.put(heightfield.width() * y + x, new Vector2f(x * cellsize + 0.5f * cellsize + fx, y * cellsize + 0.5f * cellsize + fz));
                }
            }
        }

        int maxsize = 0;
        HashMap<Integer, Grid> gr = new HashMap();
        int width = heightfield.width();
        points.entrySet().stream().forEach((point) -> {
            Grid g = new Grid(point.getValue().x, point.getValue().y, point.getKey() % width, point.getKey() / width);
            grids.put(point.getKey(), g);
            if ((point.getKey() - 1) % width != 0 && grids.containsKey(point.getKey() - 1)) {
                g.setNeighbor(Grid.DIRECTION.WEST, grids.get(point.getKey() - 1));
                grids.get(point.getKey() - 1).setNeighbor(Grid.DIRECTION.EAST, g);
            }
            if ((point.getKey() + 1) % width != 0 && grids.containsKey(point.getKey() + 1)) {
                g.setNeighbor(Grid.DIRECTION.EAST, grids.get(point.getKey() + 1));
                grids.get(point.getKey() + 1).setNeighbor(Grid.DIRECTION.WEST, g);
            }
            if ((point.getKey() - width) % width != 0 && grids.containsKey(point.getKey() - width)) {
                g.setNeighbor(Grid.DIRECTION.SOUTH, grids.get(point.getKey() - width));
                grids.get(point.getKey() - width).setNeighbor(Grid.DIRECTION.NORTH, g);
            }
            if ((point.getKey() + width) % width != 0 && grids.containsKey(point.getKey() + width)) {
                g.setNeighbor(Grid.DIRECTION.NORTH, grids.get(point.getKey() + width));
                grids.get(point.getKey() + width).setNeighbor(Grid.DIRECTION.SOUTH, g);
            }
        });
        return new GridMap(grids, fx, fz, cellsize, heightfield.width());

    }

    /**
     * Extracts the Mesh-Data from JME3-Terrain
     *
     * @param terr the terrain
     * @return the Mesh of the terrain
     */
    public Mesh terrain2mesh(Terrain terr) {
        float[] heights = terr.getHeightMap();
        int length = heights.length;
        int side = (int) FastMath.sqrt(heights.length);
        float[] vertices = new float[length * 3];
        int[] indices = new int[(side - 1) * (side - 1) * 6];

        //Vector3f trans = ((Node) terr).getWorldTranslation().clone();
        Vector3f trans = new Vector3f(0, 0, 0);
        trans.x -= terr.getTerrainSize() / 2f;
        trans.z -= terr.getTerrainSize() / 2f;
        float offsetX = trans.x;
        float offsetZ = trans.z;

        // do vertices
        int i = 0;
        for (int z = 0; z < side; z++) {
            for (int x = 0; x < side; x++) {
                vertices[i++] = x + offsetX;
                vertices[i++] = heights[z * side + x];
                vertices[i++] = z + offsetZ;
            }
        }

        // do indexes
        i = 0;
        for (int z = 0; z < side - 1; z++) {
            for (int x = 0; x < side - 1; x++) {
                // triangle 1
                indices[i++] = z * side + x;
                indices[i++] = (z + 1) * side + x;
                indices[i++] = (z + 1) * side + x + 1;
                // triangle 2
                indices[i++] = z * side + x;
                indices[i++] = (z + 1) * side + x + 1;
                indices[i++] = z * side + x + 1;
            }
        }

        Mesh mesh2 = new Mesh();
        mesh2.setBuffer(VertexBuffer.Type.Position, 3, vertices);
        mesh2.setBuffer(VertexBuffer.Type.Index, 3, indices);
        mesh2.updateBound();
        mesh2.updateCounts();

        return mesh2;
    }
}
