package App.HKM;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ModifyXMLFile {
	public static void ChangeTheme(String theme) {
		try {
			String current = new java.io.File(".").getCanonicalPath();
			String xmlFilePath = current + "/config.xml";
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document document = builder.parse(new File(xmlFilePath));

			// Update the Salary Of the First Employee
			Node firstEmployee = document.getElementsByTagName("employee").item(0);
			NodeList firstEmpNodeList = firstEmployee.getChildNodes();
			for (int i = 0; i < firstEmpNodeList.getLength(); i++) {
				Node element = firstEmpNodeList.item(i);
				if ("theme".equals(element.getNodeName())) {
					element.setTextContent(theme);
				}
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
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

	public static void Changepagesize(String pagesize) {
		try {
			String current = new java.io.File(".").getCanonicalPath();
			String xmlFilePath = current + "/config.xml";
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document document = builder.parse(new File(xmlFilePath));

			// Update the Salary Of the First Employee
			Node firstEmployee = document.getElementsByTagName("employee").item(0);
			NodeList firstEmpNodeList = firstEmployee.getChildNodes();
			for (int i = 0; i < firstEmpNodeList.getLength(); i++) {
				Node element = firstEmpNodeList.item(i);
				if ("pagesize".equals(element.getNodeName())) {
					element.setTextContent(pagesize);
					System.out.println(element.getTextContent());
				}
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
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

	public static void Changeloadimage(String loadimage) {
		try {
			String current = new java.io.File(".").getCanonicalPath();
			String xmlFilePath = current + "/config.xml";
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document document = builder.parse(new File(xmlFilePath));

			// Update the Salary Of the First Employee
			Node firstEmployee = document.getElementsByTagName("employee").item(0);
			NodeList firstEmpNodeList = firstEmployee.getChildNodes();
			for (int i = 0; i < firstEmpNodeList.getLength(); i++) {
				Node element = firstEmpNodeList.item(i);
				if ("loadimage".equals(element.getNodeName())) {
					element.setTextContent(loadimage);
					System.out.println(element.getTextContent());
				}
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
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

	public static void ChangeHost(String host) {
		try {
			String current = new java.io.File(".").getCanonicalPath();
			String xmlFilePath = current + "/config.xml";
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document document = builder.parse(new File(xmlFilePath));

			// Update the Salary Of the First Employee
			Node firstEmployee = document.getElementsByTagName("employee").item(0);
			NodeList firstEmpNodeList = firstEmployee.getChildNodes();
			for (int i = 0; i < firstEmpNodeList.getLength(); i++) {
				Node element = firstEmpNodeList.item(i);
				if ("host".equals(element.getNodeName())) {
					element.setTextContent(host);
					System.out.println(element.getTextContent());
				}
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
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

	public static void ChangePort(String port) {
		try {
			String current = new java.io.File(".").getCanonicalPath();
			String xmlFilePath = current + "/config.xml";
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document document = builder.parse(new File(xmlFilePath));

			// Update the Salary Of the First Employee
			Node firstEmployee = document.getElementsByTagName("employee").item(0);
			NodeList firstEmpNodeList = firstEmployee.getChildNodes();
			for (int i = 0; i < firstEmpNodeList.getLength(); i++) {
				Node element = firstEmpNodeList.item(i);
				if ("port".equals(element.getNodeName())) {
					element.setTextContent(port);
					System.out.println(element.getTextContent());
				}
			}
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
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

	public static String getTheme() {
		try {
			String current = new java.io.File(".").getCanonicalPath();
			String xmlFilePath = current + "/config.xml";
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document document = builder.parse(new File(xmlFilePath));

			// Update the Salary Of the First Employee
			Node firstEmployee = document.getElementsByTagName("employee").item(0);
			NodeList firstEmpNodeList = firstEmployee.getChildNodes();
			for (int i = 0; i < firstEmpNodeList.getLength(); i++) {
				Node element = firstEmpNodeList.item(i);
				if ("theme".equals(element.getNodeName())) {
					return element.getTextContent();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getloadimage() {
		try {
			String current = new java.io.File(".").getCanonicalPath();
			String xmlFilePath = current + "/config.xml";
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document document = builder.parse(new File(xmlFilePath));

			// Update the Salary Of the First Employee
			Node firstEmployee = document.getElementsByTagName("employee").item(0);
			NodeList firstEmpNodeList = firstEmployee.getChildNodes();
			for (int i = 0; i < firstEmpNodeList.getLength(); i++) {
				Node element = firstEmpNodeList.item(i);
				if ("loadimage".equals(element.getNodeName())) {
					return element.getTextContent();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getpagesize() {
		try {
			String current = new java.io.File(".").getCanonicalPath();
			String xmlFilePath = current + "/config.xml";
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document document = builder.parse(new File(xmlFilePath));

			// Update the Salary Of the First Employee
			Node firstEmployee = document.getElementsByTagName("employee").item(0);
			NodeList firstEmpNodeList = firstEmployee.getChildNodes();
			for (int i = 0; i < firstEmpNodeList.getLength(); i++) {
				Node element = firstEmpNodeList.item(i);
				if ("pagesize".equals(element.getNodeName())) {
					return element.getTextContent();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String gethost() {
		try {
			String current = new java.io.File(".").getCanonicalPath();
			String xmlFilePath = current + "/config.xml";
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document document = builder.parse(new File(xmlFilePath));

			// Update the Salary Of the First Employee
			Node firstEmployee = document.getElementsByTagName("employee").item(0);
			NodeList firstEmpNodeList = firstEmployee.getChildNodes();
			for (int i = 0; i < firstEmpNodeList.getLength(); i++) {
				Node element = firstEmpNodeList.item(i);
				if ("host".equals(element.getNodeName())) {
					return element.getTextContent();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getport() {
		try {
			String current = new java.io.File(".").getCanonicalPath();
			String xmlFilePath = current + "/config.xml";
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			Document document = builder.parse(new File(xmlFilePath));

			// Update the Salary Of the First Employee
			Node firstEmployee = document.getElementsByTagName("employee").item(0);
			NodeList firstEmpNodeList = firstEmployee.getChildNodes();
			for (int i = 0; i < firstEmpNodeList.getLength(); i++) {
				Node element = firstEmpNodeList.item(i);
				if ("port".equals(element.getNodeName())) {
					return element.getTextContent();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
