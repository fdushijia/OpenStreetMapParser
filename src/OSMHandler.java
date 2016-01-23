import java.io.*;

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
			bw = new BufferedWriter(new FileWriter("rochester_nodes_old.txt"));
			roadNodeBw = new BufferedWriter(new FileWriter("rochester_roads_old.txt"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		if ("way".equals(qName)) {
			waysCount++;
			try {
				roadNodeBw.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// System.out.println(attributes.getValue("id"));
		} else if ("nd".equals(qName)) {
			try {
				roadNodeBw.write(attributes.getValue("ref") + " ");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if ("node".equals(qName)) {
			try {
				bw.write(nodesCount + " " + attributes.getValue("id").toString() + " " + attributes.getValue("lat")
						+ " " + attributes.getValue("lon").toString());
				bw.newLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			nodesCount++;

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
			InputStream stream = OSMHandler.class.getClassLoader().getResourceAsStream("rochester_new-york.osm");
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
