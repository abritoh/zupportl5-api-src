package org.apache.clusterbr.zupportl5.utils;

import java.io.StringWriter;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/XmlUtil_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
public class XmlUtil {

    private static final Logger logger = LoggerFactory.getLogger(XmlUtil.class);

    
    public static String objectToXml(Object object) {
        return objectToXml(object, true);
    }

    
    public static String objectToXml(Object object, boolean suppressXMLdeclaration) {
        try {
            
            JAXBContext context = JAXBContext.newInstance(object.getClass());
            Marshaller marshaller = context.createMarshaller();
            
            if(suppressXMLdeclaration) {
                marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
            }
            
            StringWriter stringWriter = new StringWriter();
            marshaller.marshal(object, stringWriter);

            String xmlResult = stringWriter.toString();

            /* remove any XML declaration */
            if(suppressXMLdeclaration) {
                xmlResult = xmlResult.replaceFirst("^<\\?xml[^>]*>\\s*", "");
            }
            
            return xmlResult;

        } catch (JAXBException ex) {
            ex.printStackTrace();
            logger.error("[Exception] (XmlUtil::objectToXml)", ex);
            return "NULL";
        }
    }    
}
