package api;

import java.util.*;

public class DWGraph_DS implements directed_weighted_graph {
    private HashMap<Integer, HashMap<Integer, edge_data>> edges;
    private HashMap<Integer, node_data> nodes;
    private int size;
    private int mc;
    private int edgesamount;


    public DWGraph_DS (){
        this.size = 0;
        this.nodes = new HashMap<Integer, node_data>();
        this.edges = new HashMap<Integer, HashMap<Integer, edge_data>>();
        this.mc = 0;
        this.edgesamount = 0;
    }

    public boolean equals(Object other){
        if (!(other instanceof DWGraph_DS)) return false;
        DWGraph_DS g = (DWGraph_DS)other;
        ArrayList<node_data> gnodes = new ArrayList<node_data>();
        ArrayList<node_data> thisnodes = new ArrayList<node_data>();
        for (node_data n : g.getV()) {
            gnodes.add(n);
        }
        for (node_data m : this.getV()){
            thisnodes.add(m);
        }
        if  (gnodes.size()!=thisnodes.size()) return false;
        for (int i=0; i<gnodes.size(); i++) {
            node_data c = gnodes.get(i);
            node_data d = thisnodes.get(i);
            if (!c.equals(d)) return false;
            ArrayList<edge_data> gni = new ArrayList<edge_data>();
            for (edge_data o : this.getE(c.getKey())) {
                gni.add(o);
            }
            if (gni.size() != g.getE(d.getKey()).size()) return false;
            int j = 0;
            for (edge_data e : g.getE(d.getKey())) {
                edge_data f = gni.get(j);
                if (!e.equals(f)) return false;
                j++;
            }
        }
        return true;
    }





    /**
     * returns the gameClient.node_data by the node_id,
     *
     * @param key - the node_id
     * @return the gameClient.node_data by the node_id, null if none.
     */
    @Override
    public node_data getNode(int key) {
        if (nodes.containsKey(key)) return nodes.get(key);
        else return null;
    }

    /**
     * returns the data of the edge (src,dest), null if none.
     * Note: this method should run in O(1) time.
     *
     * @param src
     * @param dest
     * @return
     */
    @Override
    public edge_data getEdge(int src, int dest) {
        if (getNode(src) == null || getNode(dest) == null) return null;
        if (edges.get(src).containsKey(dest)) return (edges.get(src).get(dest));
        return null;
    }

    /**
     * adds a new node to the graph with the given gameClient.node_data.
     * Note: this method should run in O(1) time.
     *
     * @param n
     */
    @Override
    public void addNode(node_data n) {
        if (!nodes.containsKey(n.getKey())){
            nodes.put(n.getKey(), n);
            HashMap e = new HashMap<Integer, edge_data> ();
            edges.put(n.getKey(),e);
            size++; //mabey unnessery
            mc++;
        }
    }


    /**
     * Connects an edge with weight w between node src to node dest.
     * * Note: this method should run in O(1) time.
     *
     * @param src  - the source of the edge.
     * @param dest - the destination of the edge.
     * @param w    - positive weight representing the cost (aka time, price, etc) between src-->dest.
     */
    @Override
    public void connect(int src, int dest, double w) {
        if (src != dest || getNode(src) == null || getNode(dest) == null) {
            if (getEdge(src, dest) != null) {
                edge_data e = new EdgeData(src, dest, w);
                edges.get(src).replace(dest, e);
            } else {
                edge_data e = new EdgeData(src, dest, w);
                edges.get(src).put(dest, e);
                edgesamount++;
            }
            mc++;
        }
    }



    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the nodes in the graph.
     * Note: this method should run in O(1) time.
     *
     * @return Collection<gameClient.node_data>
     */
    @Override
    public Collection<node_data> getV() {
        return nodes.values();
    }

    /**
     * This method returns a pointer (shallow copy) for the
     * collection representing all the edges getting out of
     * the given node (all the edges starting (source) at the given node).
     * Note: this method should run in O(k) time, k being the collection size.
     *
     * @param node_id
     * @return Collection<gameClient.edge_data>
     */
    @Override
    public Collection<edge_data> getE(int node_id) {
        if (getNode(node_id) == null) return null;
        return edges.get(node_id).values();
    }

    /**
     * Deletes the node (with the given ID) from the graph -
     * and removes all edges which starts or ends at this node.
     * This method should run in O(k), V.degree=k, as all the edges should be removed.
     *
     * @param key
     * @return the data of the removed node (null if none).
     */
    @Override
    public node_data removeNode(int key) {
        if (!nodes.containsKey(key)) return null;
        else {
            node_data out = nodes.get(key);
            Iterator<node_data> it = getV().iterator();
            while (it.hasNext()){
                node_data n = it.next();
                if (n != out){
                    if (edges.get(n.getKey()).containsKey(key) == true){
                        edges.get(n.getKey()).remove(key);
                        edgesamount--;
                    }
                }
            }
            edgesamount = edgesamount - edges.get(key).size();
            edges.remove(key);
            nodes.remove(key);
            size--;
            mc++;
            return out;
        }
    }

    /**
     * Deletes the edge from the graph,
     * Note: this method should run in O(1) time.
     *
     * @param src
     * @param dest
     * @return the data of the removed edge (null if none).
     */
    @Override
    public edge_data removeEdge(int src, int dest) {
        if (nodes.containsKey(src) && nodes.containsKey(dest) && getEdge(src, dest)!=null){
            edge_data out = edges.get(src).get(dest);
            edges.get(src).remove(dest);
            mc++;
            edgesamount--;
            return out;
        }
        return null;
    }

    /**
     * Returns the number of vertices (nodes) in the graph.
     * Note: this method should run in O(1) time.
     *
     * @return
     */
    @Override
    public int nodeSize() {
        return size;
    }

    /**
     * Returns the number of edges (assume directional graph).
     * Note: this method should run in O(1) time.
     *
     * @return
     */
    @Override
    public int edgeSize() {
        return edgesamount;
    }

    /**
     * Returns the Mode Count - for testing changes in the graph.
     *
     * @return
     */
    @Override
    public int getMC() {
        return mc;
    }
}
