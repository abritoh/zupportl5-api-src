package org.apache.clusterbr.zupportl5.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.clusterbr.zupportl5.dto.MethodResultXml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/GenericXpathReader_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
public class GenericXpathReader {
    private static final Logger logger = LoggerFactory.getLogger(GenericXpathReader.class);
    private Document document;
    private XPath xpath;
    
    public GenericXpathReader() {
        this.xpath = XPathFactory.newInstance().newXPath();
    }

    public MethodResultXml<Void> setXmlFile(String xmlFilePath) {
        try {
            loadXml(xmlFilePath);
            return new MethodResultXml<>(null, true, "XML file loaded successfully.");
        } catch (Exception e) {
            logger.error("Error loading XML from file: " + xmlFilePath, e);
            return new MethodResultXml<>(null, false, "Error loading XML from file: " + e.getMessage());
        }
    }

    public void loadXml(String xmlFilePath) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        document = builder.parse(new File(xmlFilePath));
    }

    public MethodResultXml<String> getAttribute(String xpathExpression) {
        try {
            String result = (String) xpath.evaluate(xpathExpression, document, XPathConstants.STRING);
            return new MethodResultXml<>(result, !result.isEmpty(), result.isEmpty() ? ">NOT_FOUND<" : "");
        } catch (XPathExpressionException e) {
            logger.error("Error evaluating XPath expression (getAttribute): " + xpathExpression, e);
            return new MethodResultXml<>(">MALFORMED_XPATH<", false, "XPath Expression Error: " + e.getMessage());
        }
    }

    public MethodResultXml<Integer> getAttributeInt(String xpathExpression) {
        try {
            String result = (String) xpath.evaluate(xpathExpression, document, XPathConstants.STRING);
            return new MethodResultXml<>(result.isEmpty() ? -1 : Integer.parseInt(result), true, "");
        } catch (Exception e) {
            logger.error("Error evaluating XPath expression (getAttributeInt): " + xpathExpression, e);
            return new MethodResultXml<>(-1, false, "XPath Expression Error: " + e.getMessage());
        }
    }

    public MethodResultXml<Double> getAttributeDouble(String xpathExpression) {
        try {
            String result = (String) xpath.evaluate(xpathExpression, document, XPathConstants.STRING);
            return new MethodResultXml<>(result.isEmpty() ? -1.0 : Double.parseDouble(result), true, "");
        } catch (Exception e) {
            logger.error("Error evaluating XPath expression (getAttributeDouble): " + xpathExpression, e);
            return new MethodResultXml<>(-1.0, false, "XPath Expression Error: " + e.getMessage());
        }
    }

    public MethodResultXml<Long> getAttributeLong(String xpathExpression) {
        try {
            String result = (String) xpath.evaluate(xpathExpression, document, XPathConstants.STRING);
            return new MethodResultXml<>(result.isEmpty() ? -1L : Long.parseLong(result), true, "");
        } catch (Exception e) {
            logger.error("Error evaluating XPath expression (getAttributeLong): " + xpathExpression, e);
            return new MethodResultXml<>(-1L, false, "XPath Expression Error: " + e.getMessage());
        }
    }

    public MethodResultXml<Boolean> getAttributeBoolean(String xpathExpression) {
        try {
            String result = (String) xpath.evaluate(xpathExpression, document, XPathConstants.STRING);
            return new MethodResultXml<>(!result.isEmpty() && Boolean.parseBoolean(result), true, "");
        } catch (Exception e) {
            logger.error("Error evaluating XPath expression (getAttributeBoolean): " + xpathExpression, e);
            return new MethodResultXml<>(false, false, "XPath Expression Error: " + e.getMessage());
        }
    }

    public MethodResultXml<String> getNodeValue(String xpathExpression) {
        try {
            String result = (String) xpath.evaluate(xpathExpression, document, XPathConstants.STRING);
            return new MethodResultXml<>(result.isEmpty() ? ">NOT_FOUND<" : result, true, "");
        } catch (Exception e) {
            logger.error("Error evaluating XPath expression (getNodeValue): " + xpathExpression, e);
            return new MethodResultXml<>(">MALFORMED_XPATH<", false, "XPath Expression Error: " + e.getMessage());
        }
    }

    public MethodResultXml<Integer> getNodeValueInt(String xpathExpression) {
        try {
            String result = (String) xpath.evaluate(xpathExpression, document, XPathConstants.STRING);
            return new MethodResultXml<>(result.isEmpty() ? -1 : Integer.parseInt(result), true, "");
        } catch (Exception e) {
            logger.error("Error evaluating XPath expression (getNodeValueInt): " + xpathExpression, e);
            return new MethodResultXml<>(-1, false, "XPath Expression Error: " + e.getMessage());
        }
    }

    public MethodResultXml<Double> getNodeValueDouble(String xpathExpression) {
        try {
            String result = (String) xpath.evaluate(xpathExpression, document, XPathConstants.STRING);
            return new MethodResultXml<>(result.isEmpty() ? -1.0 : Double.parseDouble(result), true, "");
        } catch (Exception e) {
            logger.error("Error evaluating XPath expression (getNodeValueDouble): " + xpathExpression, e);
            return new MethodResultXml<>(-1.0, false, "XPath Expression Error: " + e.getMessage());
        }
    }

    public MethodResultXml<Long> getNodeValueLong(String xpathExpression) {
        try {
            String result = (String) xpath.evaluate(xpathExpression, document, XPathConstants.STRING);
            return new MethodResultXml<>(result.isEmpty() ? -1L : Long.parseLong(result), true, "");
        } catch (Exception e) {
            logger.error("Error evaluating XPath expression (getNodeValueLong): " + xpathExpression, e);
            return new MethodResultXml<>(-1L, false, "XPath Expression Error: " + e.getMessage());
        }
    }

    public MethodResultXml<Boolean> getNodeValueBoolean(String xpathExpression) {
        try {
            String result = (String) xpath.evaluate(xpathExpression, document, XPathConstants.STRING);
            return new MethodResultXml<>(!result.isEmpty() && Boolean.parseBoolean(result), true, "");
        } catch (Exception e) {
            logger.error("Error evaluating XPath expression (getNodeValueBoolean): " + xpathExpression, e);
            return new MethodResultXml<>(false, false, "XPath Expression Error: " + e.getMessage());
        }
    }

    public MethodResultXml<List<String>> getNodeValuesList(String xpathExpression) {
        try {
            NodeList nodeList = (NodeList) xpath.evaluate(xpathExpression, document, XPathConstants.NODESET);
            List<String> values = new ArrayList<>();
            for (int i = 0; i < nodeList.getLength(); i++) {
                values.add(nodeList.item(i).getTextContent());
            }
            return new MethodResultXml<>(values, true, "");
        } catch (Exception e) {
            logger.error("Error evaluating XPath expression (getNodeValuesList): " + xpathExpression, e);
            return new MethodResultXml<>(Collections.emptyList(), false, "XPath Expression Error: " + e.getMessage());
        }
    }

    public MethodResultXml<Boolean> isValidXML(String xmlFilePath, String xsdFilePathTemplate) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(xsdFilePathTemplate));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(new File(xmlFilePath)));
            return new MethodResultXml<>(true, true, "XML is valid.");
        } catch (Exception e) {
            logger.error("Invalid XML: " + xmlFilePath, e);
            return new MethodResultXml<>(false, false, "Invalid XML: " + e.getMessage());
        }
    }
}
