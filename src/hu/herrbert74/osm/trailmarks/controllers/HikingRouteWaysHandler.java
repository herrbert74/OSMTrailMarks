package hu.herrbert74.osm.trailmarks.controllers;

import hu.herrbert74.osm.trailmarks.osmentities.CustomNode;
import hu.herrbert74.osm.trailmarks.osmentities.CustomRelation;
import hu.herrbert74.osm.trailmarks.osmentities.CustomRelationMember;
import hu.herrbert74.osm.trailmarks.osmentities.CustomWay;

import java.util.HashMap;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class HikingRouteWaysHandler extends DefaultHandler {

	HashMap<Integer, CustomRelation> hikingRouteRelations = new HashMap<Integer, CustomRelation>();
	HashMap<Integer, CustomWay> hikingRouteWays = new HashMap<Integer, CustomWay>();
	HashMap<Integer, CustomNode> hikingRouteNodes = new HashMap<Integer, CustomNode>();
	private final Stack<String> eleStack = new Stack<String>();
	private CustomWay vw = new CustomWay();
	boolean isWayMember = false;

	public HikingRouteWaysHandler(HashMap<Integer, CustomRelation> hikingRouteRelations,
			HashMap<Integer, CustomWay> hikingRouteWays) {
		super();
		this.hikingRouteRelations = hikingRouteRelations;
		this.hikingRouteWays = hikingRouteWays;
	}

	public HashMap<Integer, CustomWay> getWays() {
		return hikingRouteWays;
	}

	public HashMap<Integer, CustomNode> getNodes() {
		return hikingRouteNodes;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		if ("way".equals(qName)) {
			vw.setWayId(Integer.parseInt(attrs.getValue("id")));
			if (hikingRouteWays.containsKey(Integer.parseInt(attrs.getValue("id")))) {
				isWayMember = true;
			}
		}
		if ("nd".equals(qName) && "way".equals(eleStack.peek())) {
			vw.addMember(Integer.parseInt(attrs.getValue("ref")));
		}
		eleStack.push(qName);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		eleStack.pop();
		if ("way".equals(qName)) {
			if (isWayMember) {
				hikingRouteWays.put(vw.getWayId(), vw);
				for (Integer member : vw.getMembers()) {
					CustomNode cn = new CustomNode();
					cn.setNodeId(member);
					hikingRouteNodes.put(member, cn);
				}
			}
			vw = new CustomWay();
			isWayMember = false;
		}
	}
}