package gameClient;

import api.*;
import Server.Game_Server_Ex2;
import gameClient.util.Point3D;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.util.*;

public class GameOn implements Runnable {

    private static Ex2Frame _win;
    private static JMenuBar _login;
    private static JMenuBar _lvl;
    private static Arena _ar;
    private double Eps;
    private static double maxSpeed;
    private static HashMap<CL_Agent,Boolean> AgAngry; // <<< or hashmap
    private static HashMap<Integer, HashMap<Integer, Integer>> HashPoke;
    private static HashMap<Integer, HashMap<Integer, Double>> HashPokeWeight;
    private static HashMap<CL_Agent, CL_Pokemon> Poke_Agent;
    // omer>>>>> write together wiki.
    public static void main(String[] a) {
        Thread client = new Thread(new GameOn());
        client.start();
    }

    /**
     * run func - starts the game according to the chosen level recieved from the server.
     */
    @Override
    public void run() {
        Scanner in = new Scanner(System.in);
        System.out.println("please enter level: [0-23]");

        // ask for level in window

        // ask for id . check input. game.login?

        int scenario_num = in.nextInt(); //players selection
        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
        //	int id = 999;
        //	game.login(id);
        String g = game.getGraph();
        String pks = game.getPokemons();
        directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
        init(game);
        game.startGame();
        _win.setTitle("Ex2 - Pokemon: " + game.toString());
        int ind = 0;
        long dt = 100;

        while (game.isRunning()) {
            moveAgants(game, gg);
            System.out.println();
            try {
                if (ind % 1 == 0) {
                    _win.repaint();
                }
                Thread.sleep(dt);
                ind++;
            } catch (Exception e) {
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
     *
     * @param game
     * @param gg
     * @param
     */
    private static void moveAgants(game_service game, directed_weighted_graph gg) {
        String lg = game.move();
        List<CL_Agent> log = Arena.getAgents(lg, gg);
        _ar.setAgents(log);
        // getting information:
        AgAngry = new HashMap<CL_Agent,Boolean>();  //int [log.size()];
        maxSpeed=0;
        for (CL_Agent c1: log) {
            AgAngry.put(c1,false);
            if(c1.getSpeed()>maxSpeed)
             maxSpeed=c1.getSpeed();
        }
        // achived max speed + AgAngry is all agents + false.

        String fs = game.getPokemons();
        List<CL_Pokemon> ffs = Arena.json2Pokemons(fs);
        List<CL_Pokemon> Pokelist = Arena.json2Pokemons(fs);
        List<CL_Pokemon> PokeReal = Arena.json2Pokemons(fs);


        for (int a = 0; a < PokeReal.size(); a++) {
            Arena.updateEdge(PokeReal.get(a), gg);
        }
        for (int a = 0; a < Pokelist.size(); a++) {
            Arena.updateEdge(Pokelist.get(a), gg);
        }
        for (CL_Pokemon c : Pokelist) {

            if (Poke_Agent.containsValue(c))
                PokeReal.remove(c); //<<<<<<<<omer
        }
        _ar.setPokemons(ffs);
        log = goAgentA(log);

        for (int i = 0; i < log.size(); i++) {
            CL_Agent ag = log.get(i); // ag = next agent.
            if (!Poke_Agent.containsKey(ag))
                Poke_Agent.put(ag, null);
            if(ag.getSpeed()>1.0&&ag.getSpeed()>maxSpeed)
                AgAngry.replace(ag,true); // also if theres nearby 4
            int id = ag.getID();
            int dest = ag.getNextNode();

            int src = ag.getSrcNode();
            double v = ag.getValue();
            double speed = ag.getSpeed();
            if (dest == -1) {
                //if (game.timeToEnd())
                CL_Pokemon c = getPokemonClosestPlace(PokeReal, src);
                Poke_Agent.replace(ag, c);
                PokeReal.remove(c); // omer < remove also others?

                int srcPoke = c.get_edge().getSrc();
                int destPoke = c.get_edge().getDest();

                if (src == srcPoke) {
                    dest = nextNode(gg, src, destPoke);// eat


                } else {
                    dest = nextNode(gg, src, srcPoke); // poke
                }
                game.chooseNextEdge(ag.getID(), dest);
                //AvailableP.replace(c.getLocation(),true);
                System.out.println("Agent: " + id + ", val: " + v + "   turned to node: " + dest + " speed : " + speed);

            }

        }

        //System.out.println(Poke_Agent.toString());
    }

    /**
     * This method returns the pokemon with the highest value in the list.
     *
     * @param pokelist
     * @return
     */
    private static CL_Pokemon getPokemonHighestValue(List<CL_Pokemon> pokelist) {//newwww
        int ans = 0;
        double[] pokeval = new double[pokelist.size()];
        int i = 0;
        for (CL_Pokemon P : pokelist) {
            pokeval[i] = P.getValue();
            i++;
        }
        //double avg = 0;
        double highestval = 0;
        for (int z = 0; z < pokeval.length; z++) {
            //avg+= pokeval[z];
            if (pokeval[z] > highestval) {
                highestval = pokeval[z];
                ans = z;
            }
        }
        //avg = avg%pokeval.length;
        System.out.println("highest value :" + highestval);
        return pokelist.get(ans);
    }

    /**
     * This method returns the closest pokemon to src.
     *
     * @param pokelist
     * @param src
     * @return
     */
    private static CL_Pokemon getPokemonClosestPlace(List<CL_Pokemon> pokelist, int src) { // check omer.
        int ans = 0;
        double x = 0;
        double min = Double.MAX_VALUE;
        CL_Pokemon c1 = pokelist.get(0);
        Iterator<CL_Pokemon> It = pokelist.iterator();
        while (It.hasNext()) {
            CL_Pokemon P = It.next();
            x = HashPokeWeight.get(P.get_edge().getSrc()).get(src);
            if (x < min) {
                min = x;
                ans = P.get_edge().getSrc();
                c1 = P;
            }
        }
        return c1;
    }

    /**
     * a very simple random walk implementation!
     *
     * @param g
     * @param src
     * @return // <<<<<<<<<<<<<<<<<<,
     */
    private static int nextNode(directed_weighted_graph g, int src, int dest) { // src - agent.
        int ans = HashPoke.get(dest).get(src);
        return ans;
    }

    /**
     * Initiates all the game objects in order to start the game (Frame, Arena, Agents, Pokemons).
     *
     * @param game
     */
    private void init(game_service game) {
        String g = game.getGraph();
        String fs = game.getPokemons();
        directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
        //gg.init(g);//
        this.Eps=MidWeight(gg);
        System.out.println("the midWeight of this level: "+Eps);
        // omer - > if is smaller than Eps > 100% slower gets the pokemon..>change direction far from
        // find pokemons 2 EPS from source of pokemon.
        // area within 3 EPS with 3+ pokemon > send fastest.
        // lower dt when ag.speed = 10, when src = 1 poke src?
        // normalize after maybe?
        // must check if position is not changing untill reach pokemon. Double Angry.
        _ar = new Arena();
        _ar.setGraph(gg);
        _ar.setPokemons(Arena.json2Pokemons(fs));
        //HashMap<Integer,HashMap<Integer,Double>> HashPoke;
        this.HashPoke = _ar.getWays();
        this.HashPokeWeight = _ar.getWaysWeight();
        this.Poke_Agent = new HashMap<>();



        _win = new Ex2Frame("Ex2");
        _win.setSize(900, 800);
        _win.update(_ar);
        _win.show();
        String info = game.toString();
        JSONObject line;
        try {
            line = new JSONObject(info);
            JSONObject ttt = line.getJSONObject("GameServer");
            int rs = ttt.getInt("agents");
            int pks =ttt.getInt("pokemons");
            System.out.println(info);
            System.out.println(game.getPokemons());
            int src_node = 0;  // arbitrary node, you should start at one of the pokemon
            ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());
            for (int a = 0; a < cl_fs.size(); a++) {
                Arena.updateEdge(cl_fs.get(a), gg);
            }
            System.out.println("pokelist size: " + cl_fs.size());
            //System.out.println("first pokemon");
            CL_Pokemon[] PokeLoc = new CL_Pokemon[cl_fs.size()];//
            int cl_size = cl_fs.size();
            for (int a = 0; a < cl_size; a++) {
                //CL_Pokemon c = cl_fs.get(a);
                PokeLoc[a] = getPokemonHighestValue(cl_fs);
                cl_fs.remove(PokeLoc[a]);
            }

            for (int a = 0; a < rs; a++) { // a=3 a=2 a=1
                if (a <= cl_size)
                    game.addAgent(PokeLoc[a].get_edge().getSrc());
                else
                    game.addAgent(PokeLoc[0].get_edge().getSrc());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method returns a list of agents sorted by speed. (from low to high)
     *
     * @param log - agents list (unsorted).
     * @return List<CL_Agent>
     */
    private static List<CL_Agent> goAgentA(List<CL_Agent> log) {
        List<CL_Agent> AgNew = new ArrayList<CL_Agent>();

        CL_Agent[] AgentS = new CL_Agent[log.size()];
        for (int i = 0; i < log.size(); i++) {
            AgentS[i] = log.get(i);
        }

        boolean sorted = false;
        CL_Agent temp;
        while (!sorted) {
            sorted = true;
            for (int i = 0; i < log.size() - 1; i++) {
                if (AgentS[i].getSpeed() > AgentS[i + 1].getSpeed()) {
                    temp = AgentS[i];
                    AgentS[i] = AgentS[i + 1];
                    AgentS[i + 1] = temp;
                    sorted = false;
                }
            }
        }
        for (int i = 0; i < log.size(); i++) {
            AgNew.add(AgentS[i]);
        }
        return AgNew;
    }

    private static double MidWeight(directed_weighted_graph gg)
    {
        double min = 0;
        double max = 0;
        if(gg.getV()!=null)
            if(gg.getE(gg.getV().iterator().next().getKey())!=null) {
                min = gg.getE(gg.getV().iterator().next().getKey()).iterator().next().getWeight();
                max = gg.getE(gg.getV().iterator().next().getKey()).iterator().next().getWeight();

                for (node_data n : gg.getV()
                ) {
                    for (edge_data e : gg.getE(n.getKey())
                    ) {
                        if (e.getWeight() > max)
                            max = e.getWeight();
                        if (e.getWeight() < min)
                            min = e.getWeight();
                    }

                }
            }
        return ((max+min)/2);
    }

    private static int NearbyPoke(int src, List<CL_Pokemon> Poke_List, double epsilon)
    {
        int ans =0;

        for (CL_Pokemon c: Poke_List
             ) {
            if(HashPokeWeight.get(src).get(c.get_edge().getSrc())<epsilon)
                ans++;
        }
        return ans;
    }


}