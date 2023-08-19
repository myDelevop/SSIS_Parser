package it.regione.rer.parser.xpath;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ParserXML {
	
	private String filePath = "";
	
	private Document doc = null;
	private XPath xpath = null;
	
	public ParserXML(String filePath) {
		this.filePath = filePath;
	}
	
	
	public List<String> getQueriesFromXML() {
		this.initializeXPath();
		
        return getQueryList();
	}

	public List<String> getCreatedTablesFromXML() {
		this.initializeXPath();
		
        return getCreatedTables();
	}

	private void initializeXPath() {
		
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;

        try {
			builder = factory.newDocumentBuilder();
	        this.doc = builder.parse(filePath);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 

        XPathFactory xpathFactory = XPathFactory.newInstance();
        this.xpath = xpathFactory.newXPath();
        
	}
	
	private List<String> getCreatedTables() {
		List<String> list = new ArrayList<String>();
        
        try {
        	// questa espressione xpath prende le query che sono dentro ai pacchetti SSIS di IMPORT
            XPathExpression expression = this.xpath.compile("//*/property[@name='OpenRowset']/text()");
            
            NodeList nodes = (NodeList) expression.evaluate(this.doc, XPathConstants.NODESET);
                       
            list.addAll(getValuesFromNodes(nodes));
            
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
                
        System.out.println("FINISH");
        return list;
		
	}
	
	private List<String> getQueryList() {
        List<String> list = new ArrayList<String>();
        
        try {
        	
        	// questa espressione xpath prende le query che sono dentro ai pacchetti SSIS di IMPORT
            XPathExpression importExpression = this.xpath.compile("//*/property[@UITypeEditor]/text()");
            
        	// questa espressione xpath prende le query che sono dentro ai pacchetti SSIS di Staging Area (script Task in particolare)
            XPathExpression stagingExpression = this.xpath.compile("//*/@*[local-name()='SqlStatementSource']");
            
            
            NodeList imNodes = (NodeList) importExpression.evaluate(this.doc, XPathConstants.NODESET);
            NodeList saNodes = (NodeList) stagingExpression.evaluate(this.doc, XPathConstants.NODESET);
            
            
            list = getQueryFromNodes(imNodes);
            list.addAll(getQueryFromNodes(saNodes));
            
        } catch (XPathExpressionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
                
        System.out.println("FINISH");
        return list;
    }

	private List<String> getQueryFromNodes(NodeList nodes) {
		List<String> list = new ArrayList<String>();
		 
        for (int i = 0; i < nodes.getLength(); i++) {
        	if (nodes.item(i).getNodeValue().toUpperCase().contains(new String("SELECT")) &&
        			nodes.item(i).getNodeValue().toUpperCase().contains(new String("FROM"))) {
                list.add(nodes.item(i).getNodeValue());            	            		
        	}
        }

		return list;
	}

	private List<String> getValuesFromNodes(NodeList nodes) {
		List<String> list = new ArrayList<String>();
		 
        for (int i = 0; i < nodes.getLength(); i++) {
        	list.add(nodes.item(i).getNodeValue());   
        }

		return list;
	}
	
	
	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	
}
