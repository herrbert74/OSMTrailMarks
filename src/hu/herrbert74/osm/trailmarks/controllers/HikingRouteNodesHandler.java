package hu.herrbert74.osm.trailmarks.controllers;

import hu.herrbert74.osm.trailmarks.osmentities.CustomNode;
import hu.herrbert74.osm.trailmarks.osmentities.CustomWay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class HikingRouteNodesHandler extends DefaultHandler {

	HashMap<Integer, CustomWay> hikingRouteWays = new HashMap<Integer, CustomWay>();
	HashMap<Integer, CustomNode> hikingRouteNodes = new HashMap<Integer, CustomNode>();
	private final Stack<String> eleStack = new Stack<String>();
	private CustomNode vn = new CustomNode();
	private boolean isNodeMember = false;

	public HikingRouteNodesHandler(HashMap<Integer, CustomWay> hikingRouteWays,
			HashMap<Integer, CustomNode> hikingRouteNodes) {
		super();
		this.hikingRouteNodes = hikingRouteNodes;
		this.hikingRouteWays = hikingRouteWays;
	}

	public HashMap<Integer, CustomNode> getNodes() {
		return hikingRouteNodes;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		if ("node".equals(qName)) {
			vn.setNodeId(Integer.parseInt(attrs.getValue("id")));
			vn.setLat(Double.parseDouble(attrs.getValue("lat")));
			vn.setLon(Double.parseDouble(attrs.getValue("lon")));
			if (hikingRouteNodes.containsKey(Integer.parseInt(attrs.getValue("id")))) {
				isNodeMember = true;
			}
		}

		eleStack.push(qName);

	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		eleStack.pop();
		if ("node".equals(qName)) {
			if (isNodeMember) {
				hikingRouteNodes.put((Integer) vn.getNodeId(), vn);
			}
			vn = new CustomNode();
			isNodeMember = false;
		}
	}
}