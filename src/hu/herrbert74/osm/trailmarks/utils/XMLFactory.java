package hu.herrbert74.osm.trailmarks.utils;

import hu.herrbert74.osm.trailmarks.osmentities.CustomNode;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

public class XMLFactory {
	public static void writeOSM(HashMap<Integer, CustomNode> trailMarkNodes, String filename) {
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		XMLStreamWriter writer;
		FileOutputStream stream;
		try {
			stream = new FileOutputStream(filename);
		} catch (Exception e) {
			System.out.append("nincs file");
			return;
		}
		try {
			writer = factory.createXMLStreamWriter(stream);

			writer.writeStartDocument("UTF-8", "1.0");

			writer.writeStartElement("osm");
			writer.writeAttribute("version", "0.6");
			writer.writeAttribute("upload", "true");
			writer.writeAttribute("generator", "JOSM");
			writer.writeCharacters("\r\n");

			// nodes
			for (CustomNode cn : trailMarkNodes.values()) {
				writer.writeStartElement("node");
				writer.writeAttribute("id", Integer.toString(cn.getNodeId()));
				writer.writeAttribute("timestamp", "2010-05-02T23:34:43Z");
				writer.writeAttribute("visible", "true");
				writer.writeAttribute("lat", Double.toString(cn.getLat()));
				writer.writeAttribute("lon", Double.toString(cn.getLon()));
				writer.writeCharacters("\r\n");
				Iterator<Map.Entry<String, String>> it = cn.getTags().entrySet().iterator();
				while (it.hasNext()) {
					writer.writeStartElement("tag");

					Map.Entry<String, String> pairs = (Map.Entry<String, String>) it.next();
					writer.writeAttribute("k", pairs.getKey());
					writer.writeAttribute("v", pairs.getValue());
					writer.writeEndElement();
					writer.writeCharacters("\r\n");
					// it.remove(); // avoids a ConcurrentModificationException
				}

				writer.writeEndElement();
				writer.writeCharacters("\r\n");
			}
			writer.writeEndDocument();

			writer.flush();
			writer.close();
		} catch (Exception e) {

		}
	}
}
