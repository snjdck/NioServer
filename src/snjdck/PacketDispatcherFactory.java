package snjdck;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import snjdck.core.IPacketDispatcher;
import snjdck.core.IPacketHandler;
import snjdck.util.ClassUtil;

public class PacketDispatcherFactory
{
	static public PacketDispatcher newPacketDispatcher()
	{
		PacketDispatcher packetDispatcher = new PacketDispatcher();
		
		try{
			config(packetDispatcher);
		}catch(ParserConfigurationException e){
			e.printStackTrace();
		}catch(SAXException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return packetDispatcher;
	}
	
	static private void config(IPacketDispatcher packetDispatcher) throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse("handler_config.xml");
		
		Element element = document.getDocumentElement();
		NodeList nodeList = element.getChildNodes();
		
		for(int i=0, n=nodeList.getLength(); i<n; i++){
			Node node = nodeList.item(i);
			if(node.getNodeType() != Node.ELEMENT_NODE){
				continue;
			}
			NamedNodeMap nodeMap = node.getAttributes();
			int msgId = Integer.parseInt(nodeMap.getNamedItem("id").getTextContent());
			String handlerClassName = nodeMap.getNamedItem("class").getTextContent();
			IPacketHandler handler = (IPacketHandler) ClassUtil.newInstance(handlerClassName);
			packetDispatcher.addHandler(msgId, handler);
		}
	}
}
