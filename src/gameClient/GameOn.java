package gameClient;
import api.*;
//import gameClient.util.*;
import Server.Game_Server_Ex2;
import gameClient.util.Point3D;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class GameOn implements Runnable{
    private static MyFrame _win;
    private static Arena _ar;
    private static HashMap<Point3D,Boolean> AvailableP;
    private static HashMap<Integer,HashMap<Integer,Integer>> HashPoke;
    private static HashMap<Integer,HashMap<Integer,Double>> HashPokeWeight;
    public static void main(String[] a) {
        Thread client = new Thread(new GameOn());
        client.start();
    }

    @Override
    public void run() {
        Scanner in = new Scanner(System.in);
        System.out.println("please enter level: [0-23]");
        int scenario_num =in.nextInt(); //players selection
        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
        //	int id = 999;
        //	game.login(id);
        String g = game.getGraph();
        String pks = game.getPokemons();
        directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
        init(game);
        //System.out.println("finished init:");
        game.startGame();
        _win.setTitle("Ex2 - OOP: (NONE trivial Solution) "+game.toString());
        int ind=0;
        long dt=100;

        while(game.isRunning()) {
            moveAgants(game, gg);
            System.out.println();
            try {
                if(ind%1==0) {_win.repaint();}
                Thread.sleep(dt);
                ind++;
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        String res = game.toString();

        System.out.println(res);
        System.exit(0);
    }
    /**
     * Moves each of the agents along the edge,
     * in case the agent is on a node the next destination (next edge) is chosen (randomly).
     * @param game
     * @param gg
     * @param
     */
    private static void moveAgants(game_service game, directed_weighted_graph gg) {
        String lg = game.move();
        List<CL_Agent> log = Arena.getAgents(lg, gg);
        _ar.setAgents(log);
        //ArrayList<OOP_Point3D> rs = new ArrayList<OOP_Point3D>();
        String fs =  game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
        List<CL_Pokemon> Pokelist = Arena.json2Pokemons(fs);
        for(int a = 0;a<Pokelist.size();a++) {
            Arena.updateEdge(Pokelist.get(a),gg);
        }
        for (CL_Pokemon pokemon : Pokelist){
            AvailableP.put(pokemon.getLocation(),false);
        }
        _ar.setPokemons(ffs);
        for(int i=0;i<log.size();i++) { // agents for
            CL_Agent ag = log.get(i); // ag = next agent.
            int id = ag.getID();
            int dest = ag.getNextNode();
            int src = ag.getSrcNode();
            double v = ag.getValue();
            double speed = ag.getSpeed();
            if(dest==-1) {
                //if (game.timeToEnd())
                CL_Pokemon c = getPokemonClosestPlace(Pokelist,src);

                int srcPoke = c.get_edge().getSrc();
                int destPoke = c.get_edge().getDest();

                if(src==srcPoke)
                    dest = nextNode(gg, src,destPoke);
                else {
                    dest = nextNode(gg, src, srcPoke);
                }
                game.chooseNextEdge(ag.getID(), dest);
                AvailableP.replace(c.getLocation(),true);
                System.out.println("Agent: "+id+", val: "+v+"   turned to node: "+dest+ " speed : "+speed);
            }
        }
    }

    private static int getPokemonHighestValue(List<CL_Pokemon> pokelist, int src){//newwww
        int ans=0;
        double [] pokeval = new double[pokelist.size()];
        int i = 0;
        for (CL_Pokemon P: pokelist) {
            pokeval [i] = P.getValue();
            i++;
        }
        //double avg = 0;
        double highestval = 0;
        for (int z=0; z<pokeval.length; z++){
            //avg+= pokeval[z];
            if (pokeval[z]>highestval){
                highestval = pokeval[z];
                ans = z;
            }
        }
        //avg = avg%pokeval.length;
        System.out.println("highest value :"+ highestval);
        return pokelist.get(ans).get_edge().getSrc();
    }


    private static CL_Pokemon getPokemonClosestPlace(List<CL_Pokemon> pokelist, int src) { // check omer.
        int ans=0;
        double x=0;
        double min=Double.MAX_VALUE;
        CL_Pokemon c1=pokelist.get(0);
        Iterator<CL_Pokemon> It = pokelist.iterator();
        while(It.hasNext()){
            CL_Pokemon P = It.next();
            if(AvailableP.get(P.getLocation())==false){
                x = HashPokeWeight.get(P.get_edge().getSrc()).get(src);
                if (x < min) {
                    min = x;
                    ans = P.get_edge().getSrc();
                    c1 = P;
                } }
            }
        return c1;
    }


//    private static int getPokemonClosestPlaceDest(List<CL_Pokemon> pokelist, int src) { // check omer.
//        int ans=0;
//        double x=0;
//        double min=Double.MAX_VALUE;
//        for (CL_Pokemon P: pokelist) {
//            x=HashPokeWeight.get(P.get_edge().getSrc()).get(src);
//            if(x<min) {
//                min = x;
//                ans= P.get_edge().getDest();
//            }
//        }
//        return ans;
//    }

    /**
     * a very simple random walk implementation!
     * @param g
     * @param src
     * @return
     */
    private static int nextNode(directed_weighted_graph g, int src, int dest) { // src - agent.
        int ans= HashPoke.get(dest).get(src);
        return ans;
    }
    private void init(game_service game) {
        String g = game.getGraph();
        String fs = game.getPokemons();
        directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
        //gg.init(g);//
        _ar = new Arena();
        _ar.setGraph(gg);
        _ar.setPokemons(Arena.json2Pokemons(fs));
        //HashMap<Integer,HashMap<Integer,Double>> HashPoke;
        this.HashPoke = _ar.getWays();
        this.HashPokeWeight = _ar.getWaysWeight();
        this.AvailableP = new HashMap<Point3D,Boolean>();
        _win = new MyFrame("test2 Ex2");
        _win.setSize(1000, 700);
        _win.update(_ar);
        _win.show();
        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int rs = ttt.getInt("agents");
            System.out.println(info);
            System.out.println(game.getPokemons());
            int src_node = 0;  // arbitrary node, you should start at one of the pokemon
            ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());
            for(int a = 0;a<cl_fs.size();a++) {
                Arena.updateEdge(cl_fs.get(a),gg);
            }
            System.out.println("pokelist size: "+cl_fs.size());
            //System.out.println("first pokemon");
            int [] PokeLoc = new int[cl_fs.size()];//
            for(int a = 0;a<cl_fs.size();a++) {
                CL_Pokemon c = cl_fs.get(a);
                PokeLoc[a]=getPokemonHighestValue(cl_fs,0);
            }

            for(int a = 0;a<rs;a++) { // a=3 a=2 a=1
                if(a<=cl_fs.size())
                    game.addAgent(PokeLoc[a]);
                else
                    game.addAgent(PokeLoc[0]);
            }
        }
        catch (JSONException e) {e.printStackTrace();}
    }
    //call for the agent when finish Mission.
    private static int [] BestPlace(int size)
    {
        int [] ans=new int [size];
        for (int i = 0; i < size ; i++) {
            //ans[i]=
        }
        return ans;
    }


}