package hu.herrbert74.osm.trailmarks;

public interface TrailMarksConstants {
	public final String INPUTFILE = "c:\\osm\\osmdata\\hiking_routes.osm";
	public final String OUTPUT_SUFFIX = "_tm.osm";
	
	public static final String OSM_TM_KEY = "osmc:symbol";  
	public final String[] OSMC_CODES = {"blue:white:blue_bar", "blue:white:blue_cross", "blue:white:blue_triangle", "blue:white:blue_rectangle", "blue:white:blue_dot", "blue:white:blue_arch", "blue:white:blue_L", "blue:white:blue_circle", "red:white:red_bar"};
	//public final int[] TYP_CODES = {0x9f01, 0x9f02, 0x9f03, 0x9f04, 0x9f05, 0x9f06, 0x9f07, 0x9f08, 0x9f09, 0x9f0a, 0x9f0b, 0x9f0c};

	public static final int INTERSTICE = 200;
}
