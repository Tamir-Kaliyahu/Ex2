package api;

import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.*;

class DWGraph_DSTest {

    @org.junit.jupiter.api.Test
    void getNode() {
        DWGraph_DS g1 = small_graph();
        g1.getNode(1);
        g1.getNode(2);

    }

    @org.junit.jupiter.api.Test
    void getEdge() {
    }

    @org.junit.jupiter.api.Test
    void addNode() {
    }

    @org.junit.jupiter.api.Test
    void connect() {
        DWGraph_DS g1 = small_graph();
        g1.connect(1,4,12.0);
        g1.connect(2,5,1.0);
        Assertions.assertEquals(g1.getEdge(5,2),null);
        g1.connect(5,2,6.0);

        Assertions.assertEquals(g1.getEdge(2,5).getWeight(),1.0);

        g1.connect(3,6,5);
        Assertions.assertEquals(g1.getEdge(3,6),null);

    }

    @org.junit.jupiter.api.Test
    void getV() {
    }

    @org.junit.jupiter.api.Test
    void getE() {
    }

    @org.junit.jupiter.api.Test
    void removeNode() {
        DWGraph_DS g1 = small_graph();
        g1.connect(1,4,12.0);
        g1.connect(2,5,1.0);
        g1.connect(2,4,3.0);
        g1.connect(3,2,2.0);
        Assertions.assertEquals(4,g1.edgeSize());
        g1.removeNode(2);
        Assertions.assertEquals(1,g1.edgeSize());
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
    }

    @org.junit.jupiter.api.Test
    void edgeSize() {
    }

    @org.junit.jupiter.api.Test
    void getMC() {
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