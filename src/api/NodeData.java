package api;

import com.google.gson.annotations.SerializedName;

public class NodeData implements node_data, Comparable<node_data>{
    @SerializedName("pos")
    private GeoLocation geo;
    @SerializedName("id")
    private int key;
    private transient double weight;
    private transient String info;
    private transient int tag;

    // add a keycounter
    public NodeData (int key){
        this.key = key;
        this.geo = new GeoLocation();// was null see if it changes things
        this.weight = 0;
        this.info = "";
        this.tag = 0;

    }

    static class GeoLocation implements geo_location {
        private double x;
        private double y;
        private double z;

        public GeoLocation (){
            this.x = 0;
            this.y = 0;
            this.z = 0;
        }

        public GeoLocation (double x, double y, double z){
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public boolean equals(Object other){
            if (!(other instanceof GeoLocation)) return false;
            GeoLocation n = (GeoLocation) other;
            return (n.x==this.x && n.y==this.y && n.z==this.z);

        }

        @Override
        public double x() {
            return x;
        }

        @Override
        public double y() {
            return y;
        }

        @Override
        public double z() {
            return z;
        }

        @Override
        public double distance(geo_location g) {
            double ans = Math.sqrt(Math.pow((this.x-g.x()),2)+Math.pow((this.y-g.y()),2)+Math.pow((this.z-g.z()),2));
            return ans;
        }
    }

    /**
     *  equals - Compare method - is used for Comparing objects, and letting you the option of using Priority Queue.
     * @param other
     * @return
     */
    public boolean equals(Object other){
        if (!(other instanceof NodeData)) return false;
        NodeData n = (NodeData) other;
        boolean flag = true;
        if (n.getLocation()==null&&this.getLocation()==null) flag = true;
        else if(n.getLocation().equals(this.getLocation())) flag = true;
        return (n.getKey()==this.getKey() && n.getTag()==this.getTag() && n.getInfo()==this.getInfo() && n.weight==this.weight
        && flag);
    }
    @Override
    public int compareTo(node_data o) {
        if (this.getWeight() < o.getWeight()) return -1;
        else if (this.getWeight() > o.getWeight()) return 1;
        return 0;
    }

    /**
     * Returns the key (id) associated with this node.
     *
     * @return
     */
    @Override
    public int getKey() {
        return key;
    }

    /**
     * Returns the location of this node, if
     * none return null.
     *
     * @return
     */
    @Override
    public geo_location getLocation() {
        return geo;
    }

    /**
     * Allows changing this node's location.
     *
     * @param p - new new location  (position) of this node.
     */
    @Override
    public void setLocation(geo_location p) {
        geo.x = p.x();
        geo.y = p.y();
        geo.z = p.z();
    }

    /**
     * Returns the weight associated with this node.
     *
     * @return
     */
    @Override
    public double getWeight() {
        return weight;
    }

    /**
     * Allows changing this node's weight.
     *
     * @param w - the new weight
     */
    @Override
    public void setWeight(double w) {
        weight = w;
    }

    /**
     * Returns the remark (meta data) associated with this node.
     *
     * @return
     */
    @Override
    public String getInfo() {
        return info;
    }

    /**
     * Allows changing the remark (meta data) associated with this node.
     *
     * @param s
     */
    @Override
    public void setInfo(String s) {
        info = s;
    }

    /**
     * Temporal data (aka color: e,g, white, gray, black)
     * which can be used be algorithms
     *
     * @return
     */
    @Override
    public int getTag() {
        return tag;
    }

    /**
     * Allows setting the "tag" value for temporal marking an node - common
     * practice for marking by algorithms.
     *
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        tag = t;
    }
}
