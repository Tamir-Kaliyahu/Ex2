import api.*;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_DSTest {

    @org.junit.jupiter.api.Test
    void getNode_addNode_removeNode() {
        DWGraph_DS g1 = small_graph();
        g1.getNode(1).setTag(2);
        g1.getNode(1).setInfo("hello");
        DWGraph_DS g2 = new DWGraph_DS();
        node_data n = new NodeData(1);
        n.setTag(2);
        n.setInfo("hello");
        g2.addNode(n);
        g2.getNode(1);
        assertEquals(g1.getNode(1),g2.getNode(1));
        g2.removeNode(1);
        assertNull(g2.getNode(1));

        g1.connect(1,4,12.0);
        g1.connect(2,5,1.0);
        g1.connect(2,4,3.0);
        g1.connect(3,2,2.0);
        g1.removeEdge(1,4);
        assertEquals(3,g1.edgeSize());
        g1.removeEdge(2,1);
        g1.removeEdge(4,2);
        assertEquals(3,g1.edgeSize());
        assertNull(g1.removeEdge(4,2));
    }

    @org.junit.jupiter.api.Test
    void getEdge_connect() {
        DWGraph_DS g1 = small_graph();
        EdgeData e = new EdgeData(1,4,12.0);
        g1.connect(1,4,12.0);
        assertEquals(g1.getEdge(1,4),e);

        g1.connect(2,5,1.0);
        assertEquals(g1.getEdge(5,2),null);

        g1.connect(5,2,6.0);
        assertEquals(g1.getEdge(2,5).getWeight(),1.0);

        g1.connect(3,6,5);
        assertEquals(g1.getEdge(3,6),null);

    }

    @org.junit.jupiter.api.Test
    void getV() {
        DWGraph_DS g1 = small_graph();
        ArrayList<node_data> c = new ArrayList<node_data>();
        c.add(g1.getNode(1));
        c.add(g1.getNode(2));
        c.add(g1.getNode(3));
        c.add(g1.getNode(4));
        c.add(g1.getNode(5));
        int i= 0;
        for (node_data n : g1.getV()){
            assertEquals(c.get(i),n);
            i++;
        }
        i = 0;
        g1.removeNode(1);
        for (node_data n : g1.getV()){
            assertNotEquals(c.get(i),n);
        }
    }

    @org.junit.jupiter.api.Test
    void getE() {
        DWGraph_DS g1 = small_graph();
        g1.connect(1,4,12.0);
        g1.connect(2,5,1.0);
        g1.connect(2,4,3.0);
        g1.connect(2,3,7.0);
        g1.connect(3,2,2.0);
        ArrayList<edge_data> c = new ArrayList<edge_data>();
        c.add(g1.getEdge(2,3));
        c.add(g1.getEdge(2,4));
        c.add(g1.getEdge(2,5));
        int i= 0;
        for (edge_data e : g1.getE(2)){
            assertEquals(c.get(i),e);
            i++;
        }
        i = 0;
        g1.removeNode(3);
        for (edge_data e : g1.getE(2)){
            assertNotEquals(c.get(i),e);
        }


    }



    @org.junit.jupiter.api.Test
    void removeEdge() {
        DWGraph_DS g1 = small_graph();
        g1.connect(1,4,12.0);
        g1.connect(2,5,1.0);
        g1.connect(2,4,3.0);
        g1.connect(3,2,2.0);
        g1.removeEdge(1,4);
        Assertions.assertEquals(3,g1.edgeSize());
        g1.removeEdge(2,1);
        g1.removeEdge(4,2);
        Assertions.assertEquals(3,g1.edgeSize());
        assertNull(g1.removeEdge(4,2));
    }

    @org.junit.jupiter.api.Test
    void nodeSize() {
        DWGraph_DS g1 = small_graph();
        assertEquals(5,g1.nodeSize());

    }

    @org.junit.jupiter.api.Test
    void edgeSize() {
        DWGraph_DS g1 = small_graph();
        assertEquals(0,g1.edgeSize());

        g1.connect(1,4,12.0);
        g1.connect(2,5,1.0);
        g1.connect(2,4,3.0);
        g1.connect(3,2,2.0);
        assertEquals(4,g1.edgeSize());
    }

    @org.junit.jupiter.api.Test
    void getMC() {
        DWGraph_DS g1 = small_graph();
        assertEquals(5,g1.getMC());

        g1.connect(1,4,12.0);
        assertEquals(6,g1.getMC());
    }


    @org.junit.jupiter.api.Test
    void equals() {
        DWGraph_DS g1 = small_graph();
        DWGraph_DS g2 = small_graph();
        g1.connect(1,4,12.0);
        g1.connect(2,5,1.0);
        g1.connect(2,4,3.0);
        g1.connect(3,2,2.0);
        g2.connect(1,4,12.0);
        g2.connect(2,5,1.0);
        g2.connect(2,4,3.0);
        g2.connect(3,2,2.0);
        Assertions.assertEquals(g1,g2);
        g2.removeEdge(3,2);
        Assertions.assertNotEquals(g1,g2);
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