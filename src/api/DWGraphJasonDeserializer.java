package api;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;

public class DWGraphJasonDeserializer implements JsonDeserializer<DWGraph_DS> {

    @Override
    public DWGraph_DS deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        DWGraph_DS graph = new DWGraph_DS();
        JsonArray nodesA = jsonObject.get("Nodes").getAsJsonArray();
        for (JsonElement nodes : nodesA){
            node_data n = new NodeData(nodes.getAsJsonObject().get("id").getAsInt());
            String [] locinfo = nodes.getAsJsonObject().get("pos").getAsString().split(",");
            double x = Double.parseDouble(locinfo[0]);
            double y = Double.parseDouble(locinfo[1]);
            double z = Double.parseDouble(locinfo[2]);
            geo_location loc = new NodeData.GeoLocation(x,y,z);
            n.setLocation(loc);
            graph.addNode(n);
        }
        JsonArray edgesA = jsonObject.get("Edges").getAsJsonArray();
        for (JsonElement edges : edgesA){
            int src = edges.getAsJsonObject().get("src").getAsInt();
            int dest = edges.getAsJsonObject().get("dest").getAsInt();
            double weight = edges.getAsJsonObject().get("w").getAsDouble();
            graph.connect(src,dest,weight);
        }
        return graph;
    }
}
