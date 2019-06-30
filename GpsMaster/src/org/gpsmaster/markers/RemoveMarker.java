package org.gpsmaster.markers;

import org.gpsmaster.cleaning.CleaningAlgorithm;
import org.gpsmaster.gpxpanel.Waypoint;

/**
 * Marker representing a Trackpoint which has been 
 * marked for removal by one of the cleaning
 * algorithms. For preview purposes.
 * 
 * @author rfu
 *
 */
public class RemoveMarker extends Marker {

	private CleaningAlgorithm source = null;

	/**
	 * 
	 * @param lat Latitude
	 * @param lon Longitude
	 */
	public RemoveMarker(double lat, double lon) {
		super(lat, lon);
	}

	/**
	 * 
	 * @param waypoint
	 */
	public RemoveMarker(Waypoint waypoint) {
		super(waypoint);
	}
	

	@Override
	protected void setup() {
		setIcon("remove.png");		
	}
}
