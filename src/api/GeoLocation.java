package api;

public class GeoLocation implements geo_location  {

	private double _x;
	private double _y;
	private double _z;
	
	  public GeoLocation(double x, double y, double z) {
	      _x = x;
	      _y = y;
	      _z = z;
	   }
	  
// copy constructor
	  
	  public GeoLocation(geo_location g) {

	      _x = g.x();
	      _y = g.y();
	      _z = g.z();
	   }

	@Override
	public double x() {
		return _x;
	}

	@Override
	public double y() {
		
		return _y;
	}

	@Override
	public double z() {
		
		return _z;
	}

	// distance between two points
	
	@Override
	public double distance(geo_location g) {
	    return Math.sqrt(Math.pow((_x - g.x()), 2) + Math.pow((_y - g.y()), 2) + Math.pow((_z - g.z()), 2));
	   }

}
