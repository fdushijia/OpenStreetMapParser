import java.io.*;
import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

public class OSMHandler extends DefaultHandler {

	private int waysCount, nodesCount;
	static BufferedWriter bw;
	static BufferedWriter roadNodeBw;
	HashMap<String, Integer> map = new HashMap<String, Integer>();

	@Override
	public void endDocument() throws SAXException {
		System.out.println(waysCount);
		System.out.println(nodesCount);
		try {
			bw.close();
			roadNodeBw.newLine();
			roadNodeBw.write("EOF");
			roadNodeBw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void startDocument() throws SAXException {
		waysCount = 0;
		nodesCount = 0;
		try {
			bw = new BufferedWriter(new FileWriter("porto_nodes.txt"));
			roadNodeBw = new BufferedWriter(new FileWriter("porto_roads.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		try {
			if ("way".equals(qName)) {
				roadNodeBw.newLine();
				roadNodeBw.write(waysCount + " " + attributes.getValue("id").toString() + " primary ");
				waysCount++;
			} else if ("nd".equals(qName)) {
				roadNodeBw.write(map.get(attributes.getValue("ref").toString()) + " ");
			} else if ("node".equals(qName)) {
				map.put(attributes.getValue("id").toString(), nodesCount);
				bw.write(nodesCount + " " + attributes.getValue("id").toString() + " " + attributes.getValue("lat")
						+ " " + attributes.getValue("lon").toString());
				bw.newLine();
				nodesCount++;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
	}

	public static void main(String[] args) {
		System.out.println("main");
		// TODO Auto-generated method stub
		SAXParser parser = null;
		try {
			// 构建SAXParser
			parser = SAXParserFactory.newInstance().newSAXParser();
			// 实例化 DefaultHandler对象
			OSMHandler parseXml = new OSMHandler();
			// 加载资源文件 转化为一个输入流
			InputStream stream = OSMHandler.class.getClassLoader().getResourceAsStream("porto_portugal.osm");
			// 调用parse()方法
			parser.parse(stream, parseXml);

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
