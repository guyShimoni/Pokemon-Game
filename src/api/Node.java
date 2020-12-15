package api;

import gameClient.util.Point3D;

public class Node implements node_data {

	private static int uniqe=0;
	private int _key;
	private geo_location _geo;
	private double _weight;
	private String _info;
	private int _tag=0;

	/**
	 * Default constructor - sets all values to zeros and null.
	 */
	public Node(){

		this._key=uniqe++;
		this._weight=0;
		this._info="";
		this._tag=0;
		_geo=new Point3D(0,0,0);
	}
	public Node(int _key){

		this._key=_key;
		this._weight=0;
		this._info="";
		this._tag=0;
		_geo=new Point3D(0,0,0);
	}

	 public Node(node_data n){
	        _key =uniqe++;
	        _tag = n.getTag();
	        _info = n.getInfo();
	        _weight = n.getWeight();
	        _geo = new Point3D(n.getLocation().x(),n.getLocation().y(),n.getLocation().z());
	       
	 }

	/**
	 * Returns the key (id) associated with this node.
	 */

	@Override
	public int getKey() {
		return this._key;
	}

	/**
	 * Returns the location of this node,
	 * if none return null.
	 */

	@Override
	public geo_location getLocation() {
		return this._geo;
	}

	/**
	 * Allows changing this node's location.
	 * @param p - new new location  (position) of this node.
	 */

	@Override
	public void setLocation(geo_location p) {
		this._geo= new Point3D( p.x(), p.y(), p.z());

	}

	@Override
	public double getWeight() {
		return this._weight;
	}

	@Override
	public void setWeight(double w) {
		this._weight=w;

	}

	@Override
	public String getInfo() {
		return this._info;
	}

	@Override
	public void setInfo(String s) {
		this._info=s;

	}

	@Override
	public int getTag() {
		return this._tag;
	}

	@Override
	public void setTag(int t) {
		this._tag=t;

	}



}
