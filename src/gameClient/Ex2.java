package gameClient;

import Server.Game_Server_Ex2;
import api.DWGraph_Algo;
import api.DWGraph_DS;
import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import api.edge_data;
import api.game_service;
import api.node_data;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class Ex2 implements Runnable{

	private static directed_weighted_graph dwgraph_ds;
	private static DWGraph_Algo graph_algo = new DWGraph_Algo();;
	static List<CL_Pokemon> Pokemon = new ArrayList<>();
	private static MyFrame _win;
	private static Arena _ar;
	private int level = -1;
	private long id = 0;

	public static void main(String[] str) {

		if (str.length == 0) {
			Ex2 ex2 = new Ex2();
			StartGame log = new StartGame(ex2);
			log.draw_start_game();
			log.setVisible(true);
		} else {
			Ex2 ex_2 = new Ex2();
			ex_2.id = Integer.parseInt(str[0]);
			ex_2.level = Integer.parseInt(str[1]);
			Thread client = new Thread(ex_2);
			client.start();
		}
	}



	@Override
	public void run() {
		game_service game = Game_Server_Ex2.getServer(level); // you have [0,23] games
		game.login(id);
		System.out.println("id: " + id + " level: " + level);
		String g = game.getGraph();
		String pks = game.getPokemons();

		directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();
		init(game);
		String info = game.toString();
		System.out.println(info);
		System.out.println(g);
		System.out.println(game.getPokemons());
		game.startGame();
		_win.setTitle("Ex2 - OOP: (NONE trivial Solution) "+game.toString());
		int ind=500;
		long dt=100;

		while(game.isRunning()) {
			moveAgants(game, gg);
			_win.setTime(game.timeToEnd());
			_win.repaint();
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


	public void setNum(int x) {
		level = x;
	}


	public void setid(long x) {
		id = x;
	}


	/**
	 * This method move the agants to the next Pokemon using shortest path algorithms
	 * @param game - The game
	 * @param gg - directed weighted graph
	 */

	public static game_service moveAgants(game_service game,  directed_weighted_graph gg) {
		dwgraph_ds = gg;
		String lg = game.move();
		Ex2.graph_algo.init(dwgraph_ds);
		String fs =  game.getPokemons();
		Pokemon	=  Arena.json2Pokemons(fs);
		List<CL_Agent> log = Arena.getAgents(lg, gg);
		_ar.setAgents(log);
		_ar.setPokemons(Pokemon);
		if (log != null) {
			for (int i = 0; i < log.size(); i++) {
				CL_Agent ag = log.get(i);
				int agentID =ag.getID();
				int src = ag.getSrcNode();
				int dest = ag.getNextNode();
				if (dest == -1) {
					edge_data nextEdge = ClosestEdge(src, Ex2.Pokemon);
					List<node_data> nodesPath=null;
					try {
						nodesPath = graph_algo.shortestPath(src, nextEdge.getSrc());
					}catch(NullPointerException e) {
						game.chooseNextEdge(agentID, nextEdge.getDest());
					}
					if (nodesPath == null) {
						game.chooseNextEdge(agentID, nextEdge.getDest());
					}
					else {
						for (node_data n : nodesPath) {
							dest = n.getKey();
							game.chooseNextEdge(agentID, dest);
						}
						game.chooseNextEdge(agentID, nextEdge.getDest());
						System.out.println("Agent: "+agentID+", val: "+src+" turned to node: "+dest);
					}
				}
			}
		}
		return game;
	}

	/**
	 * this function find the closet edge with a  in shortest path by using
	 * "shortest path" algorithm from the class "DWGraph_Algo" and return this edge.
	 * @param agantPos- the current position of the agants
	 * @param pok- the list of the current Pokemons
	 * @return the closest edge with a Pokemon.
	 */

	private static edge_data ClosestEdge(int agantPos, List<CL_Pokemon> pok) { // give the edge with the Pokemon with the shortest path
		double minPath = Double.POSITIVE_INFINITY;
		int bestSrc = agantPos;
		int bestDest = agantPos;
		double temp = -1;
		for (int i = 0; i < pok.size(); i++) {
			CL_Pokemon currentPok = pok.get(i);
			edge_data e = dwgraph_ds.getEdge(currentPok.get_edge().getSrc() , currentPok.get_edge().getDest());
			temp = graph_algo.shortestPathDist(agantPos, e.getSrc());
			if ( temp < minPath) {
				minPath = temp;
				bestSrc = e.getSrc();
				bestDest = e.getDest();
			}
		}
		return dwgraph_ds.getEdge(bestSrc, bestDest);
	}



	private void init(game_service game) {
		String fs = game.getPokemons();
		directed_weighted_graph gg = game.getJava_Graph_Not_to_be_used();

		_ar = new Arena();
		_ar.setGraph(gg);
		_ar.setPokemons(Arena.json2Pokemons(fs));
		_win = new MyFrame("test Ex2");
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
			ArrayList<CL_Pokemon> cl_fs = Arena.json2Pokemons(game.getPokemons());
			for(int a = 0;a<cl_fs.size();a++) { Arena.updateEdge(cl_fs.get(a),gg);}
			for(int a = 0;a<rs;a++) {
				int ind = a%cl_fs.size();
				CL_Pokemon c = cl_fs.get(ind);
				int nn = c.get_edge().getDest();
				if(c.getType()<0 ) {nn = c.get_edge().getSrc();}

				game.addAgent(nn);
			}
		}
		catch (JSONException e) {e.printStackTrace();}
	}


}