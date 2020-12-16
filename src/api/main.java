package api;

import gameClient.Arena;

import java.util.HashMap;
import java.util.List;

public class main {
    public static void main(String[] args) {
        node_data n1 = new NodeData(1);
        node_data n2 = new NodeData(2);
        node_data n3 = new NodeData(3);
        node_data n4 = new NodeData(4);
        node_data n5 = new NodeData(5);
        node_data n6 = new NodeData(6);


        //System.out.println("node n1 key: "+n1.getKey());
        // System.out.println("node n1 key: "+n1);
        //System.out.println("node n1 key: "+n1.getKey());
        directed_weighted_graph g1 = new DWGraph_DS();
        g1.addNode(n1);
        g1.addNode(n2);
        g1.addNode(n3);
        g1.addNode(n4);
        g1.addNode(n5);
        g1.addNode(n6);
        g1.connect(1, 2, 3);
        g1.connect(1, 3, 2);
        g1.connect(2, 5, 3);
        g1.connect(3, 1, 1);
        g1.connect(3, 4, 2);
        g1.connect(4, 3, 1);
        g1.connect(4, 5, 1);
        //g1.connect(6,2,1);
        dw_graph_algorithms check = new DWGraph_Algo();
        check.init(g1);
        //double c = check.shortestPathDist(1,5);
        //System.out.println(c);
        boolean ans = check.isConnected();
        //System.out.println(ans);
        g1.connect(5, 6, 1);
        g1.connect(2, 4, 1);
        //g1.connect(5,2,1);
        ans = check.isConnected();
        //System.out.println(ans);
        System.out.println(check.save("graph.json"));
        System.out.println(check.load("graph.json"));
        System.out.println(check.save("graph1.json"));
        //List<node_data> L = check.shortestPath(1, 5);
        //HashMap<Integer, HashMap<Integer, Double>> HashPoke;
        //System.out.println(L);
        //Arena _ar = new Arena();
        //HashPoke = _ar.getWays();
    }
}
