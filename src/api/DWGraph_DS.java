
package api;

import java.util.Collection;
import java.util.HashMap;

import java.util.Objects;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import gameClient.util.Point3D;



public class DWGraph_DS implements directed_weighted_graph{

	private int MC;
	private int edgeSize;

	public HashMap <Integer,node_data> NodeMap;
	public HashMap <node_data, HashMap<Integer, edge_data>> EdgeMap;
	

	/**
	 * Deaffult constructor sets all values to zeros and null.
	 */
	public DWGraph_DS(){
		this.edgeSize=0;
		this.MC=0;
		this.NodeMap=new HashMap<Integer,node_data>();
		this.EdgeMap=new HashMap <node_data, HashMap<Integer, edge_data>> ();

	}


	/**
	 * return the node with the input key
	 * @param key- the node_id
	 * @return the node_data by the node_id, null if none
	 */

	@Override
	public node_data getNode(int key) {
		if(NodeMap.containsKey(key))return NodeMap.get(key);
		return null;
	}

	/**
	 *  return an Edge that has the inputs source and destination values inside the graph.
	 *  this method run in O(1) time.
	 *  @param src - the source Node's key.
	 *  @param dest - the destination Node's key.
	 *  
	 */

	@Override
	public edge_data getEdge(int src, int dest) {

		if(NodeMap.get(src)==null||NodeMap.get(dest)==null||src==dest)return null;
		if(!NodeMap.containsKey(src)||!NodeMap.containsKey(dest)){
			return null;
		}

		return EdgeMap.get(NodeMap.get(src)).get(dest);   
	}


	/**
	 * adds the Node n to the graph 
	 * this method run in O(1) time.
	 * @param n - The node that will be added to the HashMap.
	 */
	@Override
	public void addNode(node_data n) {
		if(NodeMap.containsKey(n.getKey())) {
			throw new RuntimeException("node with this key is already exist");
		}
		NodeMap.put(n.getKey(),n); 
		MC++;
	}

	/**
	 * Connects an edge with weight w between node src to node dest.
	 *  this method run in O(1) time.
	 *  @param src - the source Node's key.
	 *  @param dest - the destination Node's key.
	 *  @param w - the weight of the edge between the two nodes.
	 */


	@Override
	public void connect(int src, int dest, double w) {

		if((NodeMap.get(src)==null) || (NodeMap.get(dest)==null)) {
			throw new RuntimeException("Invalid input");
		}
		if(EdgeMap.get(getNode(src))==null){
			EdgeMap.put(getNode(src),new HashMap<>());
		}
		if(EdgeMap.get(getNode(src)).containsKey(dest)) {
			throw new RuntimeException("This edge is already exist");
		}
		Edge e = new Edge(src, dest, w );
		EdgeMap.get(getNode(src)).put(dest, e);
		MC++;
		edgeSize++;
	}


	/**
	 * This method returns a pointer (shallow copy) for the
	 * collection representing all the nodes in the graph. 
	 * this method run in O(1) time.
	 * @return Collection<node_data>- a collection of Nodes that contains all nodes inside the graph 
	 */

	@Override
	public Collection<node_data> getV() {
		if(!this.NodeMap.isEmpty())return NodeMap.values();
		return null;
	
	}

	/**
	 * return a collection of the input node Edge's.
	 * @param node_id - represents the key of the Nodes.
	 */

	@Override
	public Collection<edge_data> getE(int node_id) {
		if(NodeMap.containsKey(node_id)){ // the node is exist
			node_data temp_node = NodeMap.get(node_id);

			if(EdgeMap.get(temp_node)!=null){
				return EdgeMap.get(temp_node).values(); 
			}
		}
		return null;

	}

	/**
	 * Delete the node (with the given ID) from the graph -
	 * and removes all edges which starts or ends at this node.
	 * This method should run in O(n), |V|=n, as all the edges should be removed.
	 * @return the data of the removed node (null if none). 
	 * @param key represents the ID of the requested node.
	 */

	@Override
	public node_data removeNode(int key) {
		if(!NodeMap.containsKey(key)) { // the node not exist
			return null;
		}
		else{
			node_data temp_node = NodeMap.get(key);
			Set<node_data> node_set = EdgeMap.keySet();
			for (node_data node_data : node_set) {
				if (EdgeMap.get(node_data).remove(key)!=null) {
					edgeSize--;
					MC++;
				}
			}
			if(getE(key)!=null) {
			edgeSize-=getE(key).size();
			}
			EdgeMap.remove(temp_node);
			MC++;
			return NodeMap.remove(key);
		}
	}
	

	/**
	 * Delete the edge from the graph, 
	 * Note: this method should run in O(1) time.
	 * @param src represents the source node.
	 * @param dest represents the destination node.
	 * @return the data of the removed edge (null if none).
	 */

	@Override
	public edge_data removeEdge(int src, int dest) {


		if(getNode(src)!=null&& getNode(dest)!=null && this.EdgeMap.get(getNode(src))!=null&&this.EdgeMap.get(getNode(src)).containsKey(dest) ) {
			MC++;
			edgeSize--;
			return this.EdgeMap.get(getNode(src)).remove(dest);
		}
		return null;
	}

	/**
	 * returns the number of nodes in the graph.
	 */

	@Override
	public int nodeSize() {
		return this.NodeMap.size();
	}

	/**
	 * Returns the number of edges
	 */
	@Override
	public int edgeSize() {
		return this.edgeSize;
	}

	/**
	 * Returns the Mode Count - for testing changes in the graph.
	 */

	@Override
	public int getMC() {
		return this.MC;
	}

	@Override
	public String toString() {
		String edge_str = "[";
		for (node_data i : getV()) {
			for (edge_data j : getE(i.getKey())) {
				edge_str += j + ", ";
			}
		}
		return "WDGraph_DS{" +
		", edge size=" + edgeSize +
		", mode count=" + MC +
		"\n\tnodes=" + NodeMap +
		"\n\tedges=" + edge_str + "]" +
		'}';
	}


	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		directed_weighted_graph dwg = (directed_weighted_graph) o;

		return edgeSize == dwg.edgeSize();
	}

	@Override
	public int hashCode() {
		return Objects.hash(NodeMap, edgeSize);
	}


	private class Edge implements edge_data {

		private int _src, _dest, _tag;
		private double _weight;
		private String _info;

		/**
		 * Full constructor.
		 * @param src - The id of the source node of this edge.
		 * @param dest - The id of the destination node of this edge.
		 * @param tag - temporal data (aka color: e,g, white, gray, black).
		 * @param weigth - the weight of this edge (positive value).
		 * @param info -the remark (meta data) associated with this edge.
		 */
		public Edge(int src, int dest, double weigth) {
			this._src = src;
			this._dest = dest;
			this._tag =0;
			this._weight = weigth;
			this._info = "";
		}

		/**
		 * Copy constructor.
		 * @param other - a copied version of this edge.
		 */
		public Edge(Edge e) {
			this._src = e._src;
			this._dest = e._dest;
			this._tag = e._tag;
			this._info = e._info;
			this._weight = e._weight;
		}


		/**
		 * The id of the source node of this edge.
		 * @return src
		 */

		@Override
		public int getSrc() {
			return this._src;
		}

		/**
		 * The id of the destination node of this edge
		 * @return dest
		 */

		@Override
		public int getDest() {
			return this._dest;
		}

		/**
		 * Get an edge's Weight.
		 */
		@Override
		public double getWeight() {

			return this._weight;
		}

		/**
		 * Get an edge's info 
		 */
		@Override
		public String getInfo() {

			return this._info;
		}

		/**
		 * Allows changing the remark associated with this edge.
		 */

		@Override
		public void setInfo(String s) {
			this._info=s;

		}

		/**
		 *  Get an edge's tag, which can be used be algorithms.
		 */

		@Override
		public int getTag() {

			return this._tag;
		}

		/**
		 * set the tag of an edge, 
		 * @param t - the new tag value.
		 */
		@Override
		public void setTag(int t) {
			this._tag=t;

		}
	}



	/**
	 * This class represents a position on the graph (a relative position
	 * on an edge - between two consecutive nodes).
	 */

	private class Edge_location implements edge_location {

		private edge_data edge;


		public Edge_location(int src, int dest, double weigth) {
			this.edge=new Edge(src,dest,weigth);
		}

		/**
		 *  @return edge- the edge on which the location is.
		 */

		@Override
		public edge_data getEdge() {
			return edge;
		}

		/**
		 * @return ratio- the relative ration [0,1] of the location between src and dest.
		 */

		@Override
		public double getRatio() {
			return 0;
		}

	}
}
