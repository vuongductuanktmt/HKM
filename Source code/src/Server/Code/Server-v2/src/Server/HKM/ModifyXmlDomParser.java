package Server.HKM;
import java.io.File;
 
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
 
public class ModifyXmlDomParser {
 
	public static void main(String[] args) {
		try {
			String current = new java.io.File( "." ).getCanonicalPath();
			String xmlFilePath = current+"/config.xml";
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
 
			Document document = builder.parse(new File(xmlFilePath));
 
			// Update the Salary Of the First Employee
			Node firstEmployee = document.getElementsByTagName("employee")
					.item(0);
			NodeList firstEmpNodeList = firstEmployee.getChildNodes();
			for (int i = 0; i < firstEmpNodeList.getLength(); i++) {
				Node element = firstEmpNodeList.item(i);
				if ("theme".equals(element.getNodeName())) {
					element.setTextContent("$67777750090");
					System.out.println(element.getTextContent());
				}
			}
 
			// Add element "Age" to the First Employee
			/*Element age = document.createElement("age");
			age.appendChild(document.createTextNode("30"));
			firstEmployee.appendChild(age);
 
			// Remove the department from Second Employee
			Node secondEmployee = document.getElementsByTagName("employee")
					.item(1);
			NodeList secondEmpNodeList = secondEmployee.getChildNodes();
			for (int i = 0; i < secondEmpNodeList.getLength(); i++) {
				Node element = secondEmpNodeList.item(i);
				if ("department".equals(element.getNodeName())) {
					secondEmployee.removeChild(element);
				}
			}*/
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File(xmlFilePath));
			transformer.transform(source, result);
 
			System.out.println("Changes to the XML completed !!!!");
 
		} catch (Exception e) {
			System.out.println("Exception occured" + e);
			e.printStackTrace();
		}
	}
}
 