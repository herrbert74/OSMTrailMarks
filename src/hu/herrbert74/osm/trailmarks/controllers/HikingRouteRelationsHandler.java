package hu.herrbert74.osm.trailmarks.controllers;

import hu.herrbert74.osm.trailmarks.TrailMarksConstants;
import hu.herrbert74.osm.trailmarks.osmentities.CustomRelation;
import hu.herrbert74.osm.trailmarks.osmentities.CustomRelationMember;
import hu.herrbert74.osm.trailmarks.osmentities.CustomWay;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class HikingRouteRelationsHandler extends DefaultHandler implements TrailMarksConstants {

	HashMap<Integer, CustomRelation> hikingRouteRelations = new HashMap<Integer, CustomRelation>();
	HashMap<Integer, CustomWay> hikingRouteWays = new HashMap<Integer, CustomWay>();
	private final Stack<String> eleStack = new Stack<String>();
	private CustomRelation cr = new CustomRelation();
	boolean isHikingRoute = false;

	public HikingRouteRelationsHandler() {
		super();
	}

	public HashMap<Integer, CustomRelation> getRelations() {
		return hikingRouteRelations;
	}

	public HashMap<Integer, CustomWay> getWays() {
		return hikingRouteWays;
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
		if ("relation".equals(qName)) {
			cr.setRelationId(Integer.parseInt(attrs.getValue("id")));

		} else if ("member".equals(qName) && "relation".equals(eleStack.peek())) {
			cr.addMember(new CustomRelationMember(attrs.getValue("type"), Integer.parseInt(attrs.getValue("ref")),
					attrs.getValue("role")));
		} else if ("tag".equals(qName) && "relation".equals(eleStack.peek())
				&& attrs.getValue("k").equals("osmc:symbol")) {
			cr.addTag("osmc:symbol", attrs.getValue("v"));
			isHikingRoute = true;
		}
		eleStack.push(qName);
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		eleStack.pop();
		if ("relation".equals(qName)) {
			if (isHikingRoute) {
				hikingRouteRelations.put(cr.getRelationId(), cr);
				for (CustomRelationMember crm : cr.getMembers()) {
					String z = cr.getTag("osmc:symbol");
					int index = Arrays.asList(OSMC_CODES).indexOf(z);
					if (hikingRouteWays.containsKey(crm.getRef())) {

						hikingRouteWays.get(crm.getRef()).addSymbol(index);
					} else {
						CustomWay cw = new CustomWay();
						cw.setWayId(crm.getRef());
						cw.addSymbol(index);
						hikingRouteWays.put(cw.getWayId(), cw);
					}
				}
			}
			cr = new CustomRelation();
			isHikingRoute = false;
		}

	}
}