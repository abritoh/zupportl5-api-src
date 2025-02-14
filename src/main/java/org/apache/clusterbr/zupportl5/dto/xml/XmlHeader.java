package org.apache.clusterbr.zupportl5.dto.xml;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlAccessType;
import org.apache.clusterbr.zupportl5.utils.XmlUtil;


/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/XmlHeader_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@XmlRootElement(name = "header")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlHeader implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "title")
    private String title;

    // -- Getters and setters
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    // -- toString()

    
    @Override
    public String toString() {
        return XmlUtil.objectToXml(this);
    }
}