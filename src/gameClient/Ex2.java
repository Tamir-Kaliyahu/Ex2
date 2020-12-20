package gameClient;

import api.*;
import Server.Game_Server_Ex2;
import gameClient.util.Point3D;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.util.*;
import java.util.List;

public class Ex2 implements Runnable {

    private static Ex2Frame _win;
    private static StartFrame _login;
    private static Arena _ar;
    private static double Eps=0;
    private static int LVL;
    private static int ID;
    private static HashMap<Integer, HashMap<Integer, Integer>> HashPoke;
    private static HashMap<Integer, HashMap<Integer, Double>> HashPokeWeight;
    private static HashMap<Integer, Point3D> Poke_Agent;

    // omer>>>>> write together wiki.
    public static void main(String[] a) {
//        Thread client = new Thread(new GameOn());
//        client.start();
        if(a.length==0) {
            _login = new StartFrame();
            ChooseLvl(_login);
        }else
        {
            Scanner in = new Scanner(System.in);
            System.out.println("please enter level: [0-23]");
        }


    }

    /**
     * run func - starts the game according to the chosen level recieved from the server.
     */
    @Override
    public void run() {
        LVL=_login.getLvl();
        ID = _login.getId();
        int scenario_num = LVL;
        game_service game = Game_Server_Ex2.getServer(scenario_num); // you have [0,23] games
        //	int id = 999;
        //game.login(Login());
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

            if (Poke_Agent.containsValue(c.getLocation())) {
                PokeReal.remove(c);
            }
        }
        _ar.setPokemons(ffs);
        log = goAgentA(log);

        boolean flag = false;
        int destForFast=0;
        for (CL_Pokemon c: Pokelist) {
            if(NearbyPoke(c.get_edge().getSrc(),Pokelist,Eps)>Pokelist.size()/2) {
                flag = true;
                int srcVal=getPokemonHighestValue(PokeReal).get_edge().getSrc();
                if (NearbyPoke(srcVal,Pokelist,Eps)>Pokelist.size()/3){
                    destForFast = srcVal;
                    System.out.println("srcval" + srcVal);
                    break;
                }
                destForFast = c.get_edge().getSrc();
                System.out.println("destforfast: " + destForFast+" destination poke"+c.get_edge().getDest()+")");
                break;
            }
        }
        int counterDest =0;
        for (int i = 0; i < log.size(); i++) {
            CL_Agent ag = log.get(i);
            if (!Poke_Agent.containsKey(ag.getID())) {
                Poke_Agent.put(ag.getID(), null);
            }
            int dest = ag.getNextNode();
            int src = ag.getSrcNode();
            if (dest == -1) {
                counterDest++;
                if(log.size()>1 && i==log.size()-1 &&flag==true) {
                    dest = destForFast;
                    System.out.println("agent go to area! node: "+dest);
                } else {
                    CL_Pokemon c = getPokemonClosestPlace(PokeReal, src);
                    Poke_Agent.replace(ag.getID(), c.getLocation());
                    System.out.println(Poke_Agent.toString()+"\n.........");
                    PokeReal.remove(c);

                    int srcPoke = c.get_edge().getSrc();
                    int destPoke = c.get_edge().getDest();

                    if (src == srcPoke) {
                        dest = nextNode(gg, src, destPoke);// eat


                    } else {
                        dest = nextNode(gg, src, srcPoke); // poke
                    }
                }
                game.chooseNextEdge(ag.getID(), dest);
            }
        }
        //System.out.println("counter dest -1: "+counterDest);
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
        double highestval = 0;
        for (int z = 0; z < pokeval.length; z++) {
            if (pokeval[z] > highestval) {
                highestval = pokeval[z];
                ans = z;
            }
        }
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
    private static CL_Pokemon getPokemonClosestPlace(List<CL_Pokemon> pokelist, int src) {
        double x = 0;
        double min = Double.MAX_VALUE;
        CL_Pokemon c1 = pokelist.get(0);
        Iterator<CL_Pokemon> It = pokelist.iterator();
        while (It.hasNext()) {
            CL_Pokemon P = It.next();
            x = HashPokeWeight.get(P.get_edge().getSrc()).get(src);
            if (x < min) {
                min = x;
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
    private static int nextNode(directed_weighted_graph g, int src, int dest) {
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
        this.Eps=MidWeight(gg);
        _ar = new Arena();
        _ar.setGraph(gg);
        _ar.setPokemons(Arena.json2Pokemons(fs));
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

            ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());
            for (int a = 0; a < cl_fs.size(); a++) {
                Arena.updateEdge(cl_fs.get(a), gg);
            }


            CL_Pokemon[] PokeLoc = new CL_Pokemon[cl_fs.size()];//
            int cl_size = cl_fs.size();
            for (int a = 0; a < cl_size; a++) {
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

    /**
     * this method computes an epsilon for this graph
     * @param gg - the givem graph
     * @return
     */
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

    /**
     * this method counts how manny pokemons are in the area (epsilon) of a certain source node
     * @param src - the given source node
     * @param Poke_List - list of all pokemons
     * @param epsilon - the area we want to explore around the source node
     * @return
     */
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

    /**
     * this method create a GUI to get ID and level from the user.
     * @param CurLvl - the StartFrame object that initiates the GUI
     */
    public static void ChooseLvl(StartFrame CurLvl)
    {
        StartFrame level = CurLvl;
        level.f = new JFrame("START GAME");
        level.l = new JLabel("Enter level :");
        level.l2 = new JLabel("Enter ID :");
        level.b = new JButton("submit");
        level.b.addActionListener(level);
        level.t = new JTextField(4);
        level.t2 = new JTextField(9);
        JPanel p = new JPanel();
        p.add(level.l);
        p.add(level.t);
        p.add(level.l2);
        p.add(level.t2);
        p.add(level.b);
        level.f.add(p);
        level.f.setSize(300, 300);
        level.f.show();
    }

}