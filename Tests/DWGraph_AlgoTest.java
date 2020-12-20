import api.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_AlgoTest {

    @Test
    void getV() {
        DWGraph_DS g1 = small_graph();
        g1.connect(1, 2, 3);
        g1.connect(1, 3, 2);
        g1.connect(2, 1, 3);
        g1.connect(2, 5, 3);
        g1.connect(2, 4, 3);
        g1.connect(3, 1, 1);
        g1.connect(3, 4, 2);
        g1.connect(4, 3, 1);
        g1.connect(4, 5, 1);
        g1.connect(5, 4, 1);
        ArrayList<node_data> c = new ArrayList<node_data>();
        c.add(g1.getNode(1));
        c.add(g1.getNode(4));
        c.add(g1.getNode(5));
        DWGraph_Algo dwga = new DWGraph_Algo();
        dwga.init(g1);
        assertEquals(c,dwga.getV(2));
        dwga.getGraph().removeEdge(2,1);
        assertNotEquals(c,dwga.getV(2));
    }

    @Test
    void init_getGraph() {
        DWGraph_DS g1 = small_graph();
        DWGraph_Algo dwga = new DWGraph_Algo();
        dwga.init(g1);
        assertEquals(g1,dwga.getGraph());
    }

    @Test
    void copy() {
        DWGraph_DS g1 = new DWGraph_DS();
        DWGraph_DS g2 = small_graph();
        DWGraph_Algo dwga = new DWGraph_Algo();
        dwga.init(g1);
        assertEquals(g1,dwga.copy());

        dwga.init(g2);
        assertEquals(g2,dwga.copy());
    }

    @Test
    void isConnected() {
        DWGraph_DS g1 = new DWGraph_DS();
        DWGraph_DS g2 = small_graph();
        DWGraph_Algo dwga = new DWGraph_Algo();
        dwga.init(g1);
        assertTrue(dwga.isConnected());

        node_data n = new NodeData(1);
        g1.addNode(n);
        assertTrue(dwga.isConnected());

        g2.connect(1, 2, 3);
        g2.connect(1, 3, 2);
        g2.connect(2, 1, 3);
        g2.connect(2, 5, 3);
        g2.connect(3, 1, 1);
        g2.connect(3, 4, 2);
        g2.connect(4, 3, 1);
        g2.connect(4, 5, 1);
        g2.connect(5, 4, 1);
        dwga.init(g2);
        assertTrue(dwga.isConnected());

        g2.removeEdge(5,4);
        assertFalse(dwga.isConnected());
    }

    @Test
    void shortestPathDist() {
        DWGraph_DS g1 = new DWGraph_DS();
        DWGraph_DS g2 = small_graph();
        DWGraph_Algo dwga = new DWGraph_Algo();
        g2.connect(1, 2, 3);
        g2.connect(1, 3, 2);
        g2.connect(2, 1, 3);
        g2.connect(2, 5, 3);
        g2.connect(3, 1, 1);
        g2.connect(3, 4, 2);
        g2.connect(4, 3, 1);
        g2.connect(4, 5, 1);
        g2.connect(5, 4, 1);
        dwga.init(g1);
        assertEquals(-1,dwga.shortestPathDist(1,2));

        node_data n1 = new NodeData(1);
        node_data n2 = new NodeData(2);
        g1.addNode(n1);
        g1.addNode(n2);
        assertEquals(-1,dwga.shortestPathDist(0,1));
        assertEquals(-1,dwga.shortestPathDist(1,2));

        dwga.init(g2);
        assertEquals(5,dwga.shortestPathDist(1,5));
    }

    @Test
    void shortestPath() {
        DWGraph_DS g1 = new DWGraph_DS();
        DWGraph_DS g2 = small_graph();
        DWGraph_Algo dwga = new DWGraph_Algo();
        g2.connect(1, 2, 3);
        g2.connect(1, 3, 2);
        g2.connect(2, 1, 3);
        g2.connect(2, 5, 3);
        g2.connect(3, 1, 1);
        g2.connect(3, 4, 2);
        g2.connect(4, 3, 1);
        g2.connect(4, 5, 1);
        g2.connect(5, 4, 1);
        dwga.init(g2);
        List<node_data> l = new ArrayList<node_data>();
        l.add(g2.getNode(1));
        l.add(g2.getNode(3));
        l.add(g2.getNode(4));
        l.add(g2.getNode(5));
        assertEquals(l,dwga.shortestPath(1,5));

        g2.removeEdge(4,5);
        l.remove(1);
        l.remove(2);
        l.remove(1);
        l.add(g2.getNode(2));
        l.add(g2.getNode(5));
        assertEquals(l,dwga.shortestPath(1,5));

        dwga.init(g1);
        assertNull(dwga.shortestPath(1,0));
    }

    @Test
    void save_load() {
        directed_weighted_graph g1 = new DWGraph_DS();
        directed_weighted_graph g2 = small_graph();
        g2.connect(1, 2, 3);
        g2.connect(1, 3, 2);
        DWGraph_Algo dwga = new DWGraph_Algo();
        dwga.init(g2);
        String str = "g2";
        dwga.save(str);
        dwga.load(str);
        assertEquals(g2,dwga.getGraph());

        g2.removeNode(1);
        assertNotEquals(g2,dwga.getGraph());

        dwga.init(g1);
        String str1 = "g";
        dwga.save(str1);
        directed_weighted_graph e = new DWGraph_DS();;
        dwga.load(str1);
        assertEquals(dwga.getGraph(),e);
        node_data n = new NodeData(1);
        dwga.getGraph().addNode(n);
        assertNotEquals(dwga.getGraph(),e);
    }

    public DWGraph_DS small_graph ()
    {
        node_data n1 = new NodeData(1);
        node_data n2 = new NodeData(2);
        node_data n3 = new NodeData(3);
        node_data n4 = new NodeData(4);
        node_data n5 = new NodeData(5);
        DWGraph_DS g1 = new DWGraph_DS();
        g1.addNode(n1);
        g1.addNode(n2);
        g1.addNode(n3);
        g1.addNode(n4);
        g1.addNode(n5);
        return g1;
    }
}