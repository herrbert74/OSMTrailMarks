package hu.herrbert74.osm.trailmarks.osmentities;

import hu.herrbert74.osm.trailmarks.TrailMarksConstants;

import java.util.HashMap;

public class CustomNode implements Comparable<CustomNode>, TrailMarksConstants {
	int nodeId;
	HashMap<String, String> tags;
	double lat;
	double lon;

	public CustomNode() {
		this.nodeId = 0;
		tags = new HashMap<String, String>();
	}

	public CustomNode(double lon, double lat, int nodeId, String osmTMKey) {
		this.lon = lon;
		this.lat = lat;
		this.nodeId = nodeId;
		tags = new HashMap<String, String>();
		addTag(OSM_TM_KEY, osmTMKey);
	}

	public int getNodeId() {
		return nodeId;
	}

	public void setNodeId(int nodeId) {
		this.nodeId = nodeId;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public void addTag(String K, String V) {
		tags.put(K, V);
	}

	public HashMap<String, String> getTags() {
		return tags;
	}

	@Override
	public int compareTo(CustomNode o) {
		Double lat2 = lat;
		int i = lat2.compareTo(o.lat);
		if (i != 0)
			return i;

		Double lon2 = lon;
		i = lon2.compareTo(o.lon);

		return i;

	}

}
