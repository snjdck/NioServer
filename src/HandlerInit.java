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

import snjdck.util.ClassUtil;
import cudgel.PacketDispatcher;
import cudgel.PacketHandler;

public class HandlerInit
{
	static public void Init(PacketDispatcher dispatcher, String path)
	{
		try{
			config(dispatcher, path);
		}catch(ParserConfigurationException e){
			e.printStackTrace();
		}catch(SAXException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	static private void config(PacketDispatcher dispatcher, String path) throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(path);
		
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
			PacketHandler handler = (PacketHandler) ClassUtil.newInstance(handlerClassName);
			dispatcher.regHandler(msgId, handler);
		}
	}
}
