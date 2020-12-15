package api;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import gameClient.util.Point3D;


public class DWGraph_Algo implements dw_graph_algorithms {

	private directed_weighted_graph  _graph; 
	
	/**
	 * Default constructor.
	 * @param g - a graph
	 */
	public DWGraph_Algo() {
		this._graph = new DWGraph_DS();
	}

	/**
	 * This function initializes graph field from a given graph.
	 * @param g - an input graph.
	 */

	@Override
	public void init(directed_weighted_graph g) {
		this._graph=g;

	}

	/**
	 * return the underlying graph of which this class works.
	 * @return this graph
	 */

	@Override
	public directed_weighted_graph getGraph() {
		return this._graph;
	}

	/**
	 * return a deep copy of this weighted graph 
	 */

	@Override
	public directed_weighted_graph copy() {
		DWGraph_DS temp = new DWGraph_DS();
		Collection<node_data> Nodes = _graph.getV();

		for (node_data Node : Nodes) {
			temp.addNode(Node);
		}
		for (node_data Node : Nodes) {

			Collection<edge_data> Edges = _graph.getE(Node.getKey());

			for (edge_data Edge : Edges) {
				temp.connect(Edge.getSrc(), Edge.getDest(), Edge.getWeight());
			}
		}
		return temp;
	}


	/**
	 * Returns true if and only if (iff) there is a valid path from each node to each
	 * other node. NOTE: assume directional graph (all n*(n-1) ordered pairs).
	 * @return true- if there a valid path from Every node to each, otherwise return false.
	 */


	@Override
	public boolean isConnected() {
		if(_graph.getV()==null)return false;
		boolean ans= DFS(_graph);
		if(!ans) return false;//DFS traversal not visit all the nodes
		DWGraph_DS temp= (DWGraph_DS) Transpose(_graph);//Create a reverse graph 
		ans=DFS(temp);//DFS for reverse graph start from the same node as before
		return ans;
	}

	/**
	 * Runs DFS algorithm on the current graph.
	 * @param gr-  the graph to be traveled
	 * @return true- if all the nodes are connected 
	 */

	private boolean DFS(directed_weighted_graph  gr) {
		boolean flag_first=true;
		Iterator<node_data> it=gr.getV().iterator();
		//Marks all the nodes as not visited 
		while(it.hasNext()){
			it.next().setTag(0);
		}
		//Do DFS traversal starting from first node. 
		Iterator<node_data> itr=gr.getV().iterator();

		while(itr.hasNext()&&flag_first){

			node_data v=itr.next();
			flag_first=false;
			DFS_st(v.getKey(),gr);
		}
		// If DFS traversal doesn't visit all nodes, then return false.
		Iterator<node_data> itr1=gr.getV().iterator();
		while(itr1.hasNext()){
			node_data v=itr1.next();
			if(gr.getNode(v.getKey()).getTag()==0)
				return false;
		}
		return true;
	}

	/**
	 * DFS starting from the first node.
	 * @param key -is the first node.
	 */
	private void DFS_st(int key,directed_weighted_graph _gr) 
	{ 
		_gr.getNode(key).setTag(1); 
		if(_gr.getE(key)==null)return;

		Iterator<edge_data> edgeItr=_gr.getE(key).iterator();

		while(edgeItr.hasNext()){

			edge_data e=edgeItr.next();
			node_data e_dest=_gr.getNode(e.getDest());
			if(e_dest.getTag()==0)
				DFS_st(e_dest.getKey(),_gr);	
		}
	}

	/**
	 * Transposes the original graph.
	 * @return transpose of this graph 
	 */

	private directed_weighted_graph Transpose(directed_weighted_graph _gr) 
	{ 
		directed_weighted_graph g = new DWGraph_DS(); 
		Iterator<node_data> itr = _gr.getV().iterator();

		while(itr.hasNext()){
			g.addNode(itr.next());
		}
		Iterator<node_data> itr1 = _graph.getV().iterator();
		while(itr1.hasNext()){
			node_data v=itr1.next();
			if(_gr.getE(v.getKey())!=null)
				for(Iterator<edge_data> edgeIt=_gr.getE(v.getKey()).iterator();edgeIt.hasNext();) {
					edge_data e=edgeIt.next();
					node_data e_dest=_gr.getNode(e.getDest());
					g.connect(e_dest.getKey(), v.getKey(), e.getWeight());
				}
		}
		return g; 
	} 



	/**
	 * returns the length of the shortest path between src to dest
	 * @param src - start node
	 * @param dest - end node
	 */


	@Override
	public double shortestPathDist(int src, int dest) {
		if( _graph.getNode(src) == null || _graph.getNode(dest) == null) {
			return -1; // -1 = there is a path from src to dst
		}
		if(src==dest)
			return 0;

		dijkstra(src);
		double temp=_graph.getNode(dest).getWeight();
		return temp == Double.POSITIVE_INFINITY ? -1 : temp;

	}

	/**
	 *  returns the the shortest path between src to dest
	 *  https://en.wikipedia.org/wiki/Shortest_path_problem
	 * @param src -  the source Node key.
	 * @param dest-	 the destination Node key.
	 */

	@Override
	public List<node_data> shortestPath(int src, int dest) {
		List<node_data> path = new ArrayList<>();
		double dist=shortestPathDist(src, dest);
		if(dist == Double.POSITIVE_INFINITY||_graph.getNode(dest).getInfo().equals("")) {
			return null;
		}

		path.add(_graph.getNode(dest));
		String str = _graph.getNode(dest).getInfo();
		while(!str.equals("")) {		
			path.add(_graph.getNode(Integer.parseInt(str)));
			str = _graph.getNode(Integer.parseInt(str)).getInfo();
		}
		Collections.reverse(path);
		return path;
	}

	/**
	 * Saves the graph to a file.
	 * file name - in JSON format
	 * @param file_name represents the name of the new file.
	 */

	@Override
	public boolean save(String file) {

		JsonObject graph = new JsonObject();
		JsonArray edges = new JsonArray();
		JsonArray nodes = new JsonArray();
//{"pos":"35.20319591121872,32.1031462,0.0","id":8}
		for (node_data n : this._graph.getV()) {
			JsonObject node = new JsonObject();
			node.addProperty("pos", n.getLocation().toString());
			node.addProperty("id", n.getKey());
			nodes.add(node);
			if(this._graph.getE(n.getKey())!=null) {
				for (edge_data e : this._graph.getE(n.getKey())) {
					JsonObject edge = new JsonObject();
					edge.addProperty("src", e.getSrc());
					edge.addProperty("w", e.getWeight());
					edge.addProperty("dest", e.getDest());
					edges.add(edge);
				}
			}
		}
		graph.add("Edges", edges);
		graph.add("Nodes", nodes);

		try {
			Gson Gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().serializeNulls().create();;
			FileWriter f = new FileWriter(file);
			f.write(Gson.toJson(graph));
			f.close();
		} catch (IOException e) {
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
	 * @param file - file name of JSON file
	 * @return true - iff the graph was successfully loaded.
	 */


	@Override
	public boolean load(String file) {
	       try {
	    	    	FileInputStream input = new FileInputStream(file);
	    	    	JsonReader reader = new JsonReader(new InputStreamReader(input));
	                JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
	                
	                JsonArray edges = obj.getAsJsonArray("Edges");
	                JsonArray nodes = obj.getAsJsonArray("Nodes");
	                
	                directed_weighted_graph temp_graph = new DWGraph_DS();
	                
	                for(JsonElement i: nodes) {
	                	node_data temp=new Node(i.getAsJsonObject().get("id").getAsInt());
	                	String p = i.getAsJsonObject().get("pos").getAsString();
	                	//split x,y,z
	                	ArrayList<Double> split = new ArrayList<Double>();
	                    for (String part : p.split(",")) {
	                    	split.add(Double.parseDouble(part));
	                    }
	                	temp.setLocation(new Point3D(split.get(0),split.get(1),split.get(2)));
	                	temp_graph.addNode(temp);
	                }
	               for(JsonElement i: edges) {
	            	   temp_graph.connect(i.getAsJsonObject().get("src").getAsInt(),i.getAsJsonObject().get("dest").getAsInt(),i.getAsJsonObject().get("w").getAsDouble());;
	                }
	                 
	               init(temp_graph);
	            } catch (IOException e) {
	                e.printStackTrace();
	                return false;
	            }
	        return true;
	}





	/**
	 * Dijkstra algorithm for more information : https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm.
	 * @param src - Integer that represent node key
	 */
	private void dijkstra(int src) {

		INIT();
		PriorityQueue<node_data> Nq = new PriorityQueue<node_data>(new Comparator<node_data>() {
			@Override
			public int compare(node_data o1, node_data o2) {
				return -Double.compare(o2.getWeight(),o1.getWeight());
			}
		});
		node_data srcS= _graph.getNode(src);
		srcS.setTag(0);
		srcS.setWeight(0);
		Nq.add(srcS);

		while(!Nq.isEmpty()){
			node_data temp1 = Nq.poll();
			Collection<edge_data> Edges = _graph.getE(temp1.getKey());
			if(Edges!=null) {
				for(edge_data edge: Edges){
					node_data temp2 = _graph.getNode(edge.getDest());
					if(temp2.getTag() != 1) {
						double weightEdge = edge.getWeight()+temp1.getWeight();
						if(weightEdge  < temp2.getWeight()) 
						{
							Nq.remove(temp2);
							temp2.setWeight(weightEdge);
							temp2.setInfo(temp1.getKey()+"");
							Nq.add(temp2);
						}
					}
				}
			}
		}
	}

	private void INIT() {
		Collection<node_data> nodes = this._graph.getV();
		for (node_data n : nodes) {
			n.setWeight(Double.POSITIVE_INFINITY);
			n.setTag(0);
			n.setInfo("");
		}
	}
	public static void main(String[] args) {

		directed_weighted_graph g=new DWGraph_DS();
		dw_graph_algorithms ga=new DWGraph_Algo();
		g.addNode(new Node());
		g.addNode(new Node());
		g.addNode(new Node());

		g.getNode(0).setLocation(new Point3D(1,8,500));
		g.connect(0, 1, 20);
		g.connect(0, 2, 30);
		g.connect(2, 0, 40);
		g.connect(1, 0, 50);

		ga.init(g);
		ga.save("testSave");
		ga.load("testSave");
		ga.save("testSave1");
		//System.out.println(ga.isConnected());
	}
}
