package api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.*;
import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms {
    private directed_weighted_graph gr;

    public Collection<node_data> getV(int key) {
        Collection<node_data> V = new LinkedList<>();
        for (edge_data k: gr.getE(key))
        {
            V.add(gr.getNode(k.getDest()));
        }
        return  V;
    }

    public List <node_data> Dijkstra(int src, int dest, directed_weighted_graph gr) {
        directed_weighted_graph g = this.copy();
        for(node_data r : g.getV()){
            r.setWeight(Double.MAX_VALUE);
            r.setInfo(" ");
            for (edge_data e : g.getE(r.getKey())){
                e.setInfo(" ");
            }
        }
        PriorityQueue<node_data> q = new PriorityQueue<>();
        HashMap<Integer, Integer> parents = new HashMap();
        q.add(g.getNode(src));
        g.getNode(src).setWeight(0);
        while (!q.isEmpty()) {
            node_data curr = q.poll();
            if (curr.getInfo() == " ") {
                curr.setInfo("v");
                if (curr.getKey() == dest) break;
                Iterator<edge_data> it = g.getE(curr.getKey()).iterator();
                while (it.hasNext()) {
                    edge_data e = it.next();
                    node_data ni = g.getNode(e.getDest());
                    //if ni != "v"
                    if (e.getWeight() + curr.getWeight() < ni.getWeight()) {
                        ni.setWeight(e.getWeight() + curr.getWeight());
                        if (parents.containsKey(ni.getKey())) parents.remove(ni.getKey());
                        parents.put(ni.getKey(), curr.getKey());
                        if (!q.contains(ni)) q.add(ni);
                    }
                }
            }
        }
        List<node_data> path = new ArrayList<node_data>();
        if (g.getNode(dest).getWeight() == Double.MAX_VALUE) return null;
        int assemble = dest;
        while (assemble != src) {
            node_data par = g.getNode(assemble);
            path.add(par);
            assemble = parents.get(assemble);
        }
        path.add(g.getNode(src));
        List<node_data> almostthere = new ArrayList<node_data>();
        List<node_data>  reversedpath = new ArrayList<node_data>();
        for (int i = path.size() - 1; i >= 0; i--) {
            almostthere.add(path.get(i));
        }
        for (node_data f : almostthere){
            int i = f.getKey();
            node_data r = gr.getNode(i);
            r.setWeight(f.getWeight());
            reversedpath.add(r);
        }
        return reversedpath;
    }

    /**
     * Init the graph on which this set of algorithms operates on.
     *
     * @param g
     */
    @Override
    public void init(directed_weighted_graph g) {
        this.gr = g;
    }

    /**
     * Return the underlying graph of which this class works.
     *
     * @return
     */
    @Override
    public directed_weighted_graph getGraph() {
        return gr;
    }

    /**
     * Compute a deep copy of this weighted graph.
     *
     * @return
     */

    @Override
    public directed_weighted_graph copy() {
        if (gr.nodeSize() == 0) return gr;
        directed_weighted_graph c = new DWGraph_DS();
        for (node_data n : gr.getV()){
            node_data d = new NodeData(n.getKey());
            d.setInfo(n.getInfo());
            d.setLocation(n.getLocation());
            d.setTag(n.getTag());
            d.setWeight(n.getWeight());
            c.addNode(d);
        }
        for (node_data n : gr.getV()){
            for (edge_data m : gr.getE(n.getKey())){
                c.connect(m.getSrc(), m.getDest(), m.getWeight());
                c.getEdge(m.getSrc(),m.getDest()).setInfo(m.getInfo());
                c.getEdge(m.getSrc(),m.getDest()).setTag(m.getTag());
            }
        }
        return c;
    }

    /**
     * Returns true if and only if (iff) there is a valid path from each node to each
     * other node. NOTE: assume directional graph (all n*(n-1) ordered pairs).
     *
     * @return
     */
    @Override
    public boolean isConnected() {
        boolean isCo = true;
        if (this.gr.edgeSize() + 1 < this.gr.nodeSize())
            return false;
        if (this.gr.nodeSize() == 1 || this.gr.nodeSize() == 0)
            return true;
        Queue<node_data> Q1 = new LinkedList<>();
        //HashMap<Integer, Boolean> nod = new HashMap<>();
        //Queue<gameClient.node_data> Q = new LinkedList<>();
        node_data current = this.gr.getV().iterator().next();
        int SRCkey = current.getKey();
        for (node_data n : this.gr.getV()) {
            n.setInfo("white");
        }
        gr.getNode(SRCkey).setInfo("black");
        Q1.add(current);
        while (!Q1.isEmpty()) {
            current = Q1.poll();
            if(this.getV(current.getKey()).size()==0)
                isCo=false;
            for (node_data nB : this.getV(current.getKey()) // ->> need specific getV
            ) {
                if (nB.getInfo().equals("white")) {
                    nB.setInfo("black");
                    Q1.add(nB);
                }
                if(nB.getKey()==SRCkey&&nB.getInfo().equals("black"))
                    nB.setInfo("green");
            }
        }

        for (node_data n : this.gr.getV()
        ) {
            // <---- check if green - for the first node.
            if (n.getKey()!=SRCkey && n.getInfo().equals("white"))
                isCo = false;
        }
        if(!this.gr.getNode(SRCkey).getInfo().equals("green"))
            isCo = false;
        return isCo;
    }

    /**
     * returns the length of the shortest path between src to dest
     * Note: if no such path --> returns -1
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */

    @Override
    public double shortestPathDist(int src, int dest) {
        if (gr.nodeSize() == 0 || gr.getNode(src) == null || gr.getNode(dest) == null) return -1;
        if (src == dest ) return 0;
        List <node_data> path = Dijkstra(src,dest,gr);
        if (path == null) return -1;
        if (path.get(0) == gr.getNode(src)) return gr.getNode(dest).getWeight();
        return -1;
    }

    /**
     * returns the the shortest path between src to dest - as an ordered List of nodes:
     * src--> n1-->n2-->...dest
     * see: https://en.wikipedia.org/wiki/Shortest_path_problem
     * Note if no such path --> returns null;
     *
     * @param src  - start node
     * @param dest - end (target) node
     * @return
     */

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        if (src == dest || gr.getNode(src) == null || gr.getNode(dest) == null) return null;
        List <node_data> path= Dijkstra(src,dest,gr);
        if (path.get(0) == gr.getNode(src)) return path;
        else return null;
    }

    /**
     * Saves this weighted (directed) graph to the given
     * file name - in JSON format
     *
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
    @Override
    public boolean save(String file) {
        Gson gson = new GsonBuilder().create();//.setPrettyPrinting()
        JsonArray nodes = new JsonArray();
        JsonArray edges = new JsonArray();
        JsonObject graph = new JsonObject();
        for (node_data n : gr.getV()){
            JsonObject no = new JsonObject();
            geo_location g = n.getLocation();
            double x = g.x();
            double y = g.y();
            double z = g.z();
            String xval = String.valueOf(x);
            String yval = String.valueOf(y);
            String zval = String.valueOf(z);
            String loc = xval + "," +yval + "," +zval;
            no.addProperty("pos",loc);
            no.addProperty("id", n.getKey());
            nodes.add(no);
            for (edge_data e : gr.getE(n.getKey())){
                JsonObject eo = new JsonObject();
                eo.addProperty("src", e.getSrc());
                eo.addProperty("w", e.getWeight());
                eo.addProperty("dest", e.getDest());
                edges.add(eo);
            }
        }
        graph.add("Edges", edges);
        graph.add("Nodes", nodes);
        String json = gson.toJson(graph);
        try {
            PrintWriter pw = new PrintWriter(new File(file));
            pw.write(json);
            pw.close();
        }
        catch (FileNotFoundException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }



    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     *
     * @param file - file name of JSON file
     * @return true - iff the graph was successfully loaded.
     */
    @Override
    public boolean load(String file) {
        try {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(DWGraph_DS.class , new DWGraphJasonDeserializer());
            Gson gson = builder.create();
            FileReader rd = new FileReader(file);
            directed_weighted_graph ng = gson.fromJson(rd,DWGraph_DS.class);
            init(ng);
        }
        catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
