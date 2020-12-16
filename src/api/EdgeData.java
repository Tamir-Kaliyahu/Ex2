package api;

import com.google.gson.annotations.SerializedName;

public class EdgeData implements edge_data {
    private int src;
    @SerializedName("w")
    private double weight;
    private int dest;
    private transient int tag;
    private transient String info;

    public EdgeData(int src, int dest, double weight){
        this.src = src;
        this.dest = dest;
        this.tag = 0;
        this.info = "";
        this.weight = weight;
    }

    public boolean equals(Object other){
        if (!(other instanceof EdgeData)) return false;
        EdgeData n = (EdgeData) other;
        return (n.getSrc()==this.getSrc() && n.getTag()==this.getTag() && n.getInfo()==this.getInfo() && n.weight==this.weight
                && n.getDest()==this.getDest());
    }

    /**
     * The id of the source node of this edge.
     *
     * @return
     */
    @Override
    public int getSrc() {
        return src;
    }

    /**
     * The id of the destination node of this edge
     *
     * @return
     */
    @Override
    public int getDest() {
        return dest;
    }

    /**
     * @return the weight of this edge (positive value).
     */
    @Override
    public double getWeight() {
        return weight;
    }

    /**
     * Returns the remark (meta data) associated with this edge.
     *
     * @return
     */
    @Override
    public String getInfo() {
        return info;
    }

    /**
     * Allows changing the remark (meta data) associated with this edge.
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
     * This method allows setting the "tag" value for temporal marking an edge - common
     * practice for marking by algorithms.
     *
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        tag = t;
    }
}
