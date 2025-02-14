package org.apache.clusterbr.zupportl5.dto.xml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlAccessType;

import org.apache.clusterbr.zupportl5.utils.XmlUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DocumentXml_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@XmlRootElement(name = "document")
@XmlAccessorType(XmlAccessType.FIELD)
public class DocumentXml implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = LoggerFactory.getLogger(DocumentXml.class);

    @XmlAttribute(name = "version")
    private String version;

    @XmlElement(name = "document-type")
    private String documentType;

    @XmlElement(name = "category")
    private String category;

    @XmlElement(name = "xmlheader")
    private XmlHeader xmlHeader;
    
    @XmlElement(name = "xmlcontent")
    private XmlContent xmlContent;

    // -- Getters and setters

    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }

    public String getDocumentType() {
        return documentType;
    }
    
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }

    public XmlHeader getXmlHeader() {
        return xmlHeader;
    }
    
    public void setXmlHeader(XmlHeader xmlHeader) {
        this.xmlHeader = xmlHeader;
    }

    public XmlContent getXmlContent() {
        return xmlContent;
    }
    
    public void setXmlContent(XmlContent xmlContent) {
        this.xmlContent = xmlContent;
    }

    // -- toString()

    
    @Override
    public String toString() {
        return XmlUtil.objectToXml(this);
    }

    // -- public static

    
    public static DocumentXml convertFromString(String xmlString)  {

        try (InputStream inputStream = new ByteArrayInputStream(xmlString.getBytes())) {
            
            return DocumentXml.convertFromStream(inputStream);

        } catch(Exception ex) {
            ex.printStackTrace();
            logger.error("[Exception] (DocumentXml::convertFromString)", ex);
        }    

        return null;
    }

    
    
    public static DocumentXml convertFromStream(InputStream xmlInputStream)  {
        try {
            JAXBContext context = JAXBContext.newInstance (
                DocumentXml.class, 
                XmlHeader.class, 
                XmlContent.class, 
                XmlSteps.class
                );
            Unmarshaller unmarshaller = context.createUnmarshaller();
            
            Object object = unmarshaller.unmarshal(xmlInputStream);

            return (object == null ) ? null : (DocumentXml) object;

        } catch(Exception ex) {
            ex.printStackTrace();
            logger.error("[Exception] (DocumentXml::convertFromStream)", ex);
        }    
        return null;
    }
}

