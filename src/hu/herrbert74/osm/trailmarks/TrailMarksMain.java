package hu.herrbert74.osm.trailmarks;

import hu.herrbert74.osm.trailmarks.controllers.HikingRouteNodesHandler;
import hu.herrbert74.osm.trailmarks.controllers.HikingRouteRelationsHandler;
import hu.herrbert74.osm.trailmarks.controllers.HikingRouteWaysHandler;
import hu.herrbert74.osm.trailmarks.osmentities.CustomNode;
import hu.herrbert74.osm.trailmarks.osmentities.CustomRelation;
import hu.herrbert74.osm.trailmarks.osmentities.CustomRelationMember;
import hu.herrbert74.osm.trailmarks.osmentities.CustomWay;
import hu.herrbert74.osm.trailmarks.utils.LatLongUtil;
import hu.herrbert74.osm.trailmarks.utils.XMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public class TrailMarksMain implements TrailMarksConstants {

	String inputFileName;
	String outputFileName;
	HashMap<Integer, CustomNode> hikingRouteNodes = new HashMap<Integer, CustomNode>();
	HashMap<Integer, CustomWay> hikingRouteWays = new HashMap<Integer, CustomWay>();
	HashMap<Integer, CustomRelation> hikingRouteRelations = new HashMap<Integer, CustomRelation>();

	HashMap<Integer, CustomNode> trailMarkNodes = new HashMap<Integer, CustomNode>();
	int countTrailMarkNodes = 0;

	public static void main(String[] args) {
		TrailMarksMain main = new TrailMarksMain();
		main.setFileNames(args);
		main.loadHikingRoutes();
		main.writeTrailMarks();
		main.writeOSM();
	}

	private void setFileNames(String[] args) {
		if (args.length > 0) {
			inputFileName = args[0];
		} else {
			inputFileName = INPUTFILE;
		}
		String outputSuffix;
		if (args.length > 1) {
			outputSuffix = args[1];
		} else {
			outputSuffix = OUTPUT_SUFFIX;
		}
		outputFileName = inputFileName.substring(0, inputFileName.length() - 4);
		outputFileName += outputSuffix;
	}

	private void loadHikingRoutes() {
		System.out.println("Loading hiking routes");
		try {
			hikingRouteRelations = readRelations(new File(inputFileName));
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		System.out.println("Loading hiking route ways");
		try {
			hikingRouteWays = readWays(new File(inputFileName), hikingRouteRelations);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		System.out.println("Loading hiking route nodes");
		try {
			hikingRouteNodes = readNodes(new File(inputFileName));
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

	private void writeTrailMarks() {
		for (Entry<Integer, CustomRelation> cr : hikingRouteRelations.entrySet()) {
			double offset21 = 0;
			for (CustomRelationMember crm : cr.getValue().getMembers()) {
				offset21 = addTrailMarks(hikingRouteWays.get(crm.getRef()), offset21, cr.getValue().getTag(OSM_TM_KEY));
			}
		}
		System.out.println("finished");
	}

	private double addTrailMarks(CustomWay customWay, double offset21, String osmTMKey) {
		
		for (int i = 0; i < (customWay.getMembers().size() - 1); i++) {
			CustomNode node1 = hikingRouteNodes.get(customWay.getMembers().get(i));
			CustomNode node2 = hikingRouteNodes.get(customWay.getMembers().get(i + 1));
			double strechLength = LatLongUtil.distance(node1.getLat(), node2.getLon(), node2.getLat(), node2.getLon());
			double offset = offset21 % INTERSTICE;
			double cummDistance = -offset;
			do {
				cummDistance += 200;
				if (strechLength > cummDistance) {
					countTrailMarkNodes--;
					CustomNode cn = calculateNodeCoordinates(countTrailMarkNodes, node1, node2, cummDistance, strechLength, osmTMKey);
					if ((offset21 + cummDistance) % 1600 == 0) {
						cn.addTag("res", "21");
					} else if ((offset21 + cummDistance) % 800 == 0) {
						cn.addTag("res", "22");
					} else if ((offset21 + cummDistance) % 400 == 0) {
						cn.addTag("res", "23");
					} else {
						cn.addTag("res", "24");
					}
					trailMarkNodes.put(countTrailMarkNodes, cn);

				} else {
					offset21 = (offset21 + strechLength) % 1600;
				}
			} while (strechLength > cummDistance);
		}
		return offset21;
	}

	private CustomNode calculateNodeCoordinates(int countTrailMarkNodes, CustomNode node1, CustomNode node2,
			double cummDistance, double stretchLength,  String osmTMKey) {
		double lat, lon;
		lat = node1.getLat() + (node2.getLat() - node1.getLat()) * cummDistance / stretchLength;
		lon = node1.getLon() + (node2.getLon() - node1.getLon()) * cummDistance / stretchLength;
		CustomNode result = new CustomNode(lon, lat, countTrailMarkNodes, osmTMKey);
		return result;
	}

	private void writeOSM() {
		XMLFactory.writeOSM(trailMarkNodes, outputFileName);

	}

	public HashMap<Integer, CustomRelation> readRelations(File file) throws ParserConfigurationException, SAXException,
			IOException {
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		SAXParser parser = parserFactory.newSAXParser();
		HashMap<Integer, CustomRelation> result = new HashMap<Integer, CustomRelation>();

		HikingRouteRelationsHandler hrRelationsHandler = new HikingRouteRelationsHandler();
		parser.parse(file, hrRelationsHandler);
		hikingRouteWays = hrRelationsHandler.getWays();
		result = hrRelationsHandler.getRelations();
		return result;
	}

	public HashMap<Integer, CustomWay> readWays(File file, HashMap<Integer, CustomRelation> hikingRouteRelations)
			throws ParserConfigurationException, SAXException, IOException {
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		SAXParser parser = parserFactory.newSAXParser();
		HashMap<Integer, CustomWay> result = new HashMap<Integer, CustomWay>();

		HikingRouteWaysHandler hrWaysHandler = new HikingRouteWaysHandler(hikingRouteRelations, hikingRouteWays);
		parser.parse(file, hrWaysHandler);
		hikingRouteNodes = hrWaysHandler.getNodes();
		result = hrWaysHandler.getWays();
		return result;
	}

	public HashMap<Integer, CustomNode> readNodes(File file) throws ParserConfigurationException, SAXException,
			IOException {
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		SAXParser parser = parserFactory.newSAXParser();
		HashMap<Integer, CustomNode> result = new HashMap<Integer, CustomNode>();

		HikingRouteNodesHandler hrNodesHandler = new HikingRouteNodesHandler(hikingRouteWays, hikingRouteNodes);
		parser.parse(file, hrNodesHandler);
		result = hrNodesHandler.getNodes();
		return result;
	}
}
