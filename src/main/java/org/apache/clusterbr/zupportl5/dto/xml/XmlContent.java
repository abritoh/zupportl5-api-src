package org.apache.clusterbr.zupportl5.dto.xml;

import java.io.Serializable;


import org.apache.clusterbr.zupportl5.utils.XmlUtil;

import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlAccessType;


/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/XmlContent_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@XmlRootElement(name = "content")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlContent implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "steps")
    private XmlSteps steps;

    // -- Getters and setters
    public XmlSteps getSteps() {
        return steps;
    }

    public void setSteps(XmlSteps steps) {
        this.steps = steps;
    }

    // -- toString()

    
    @Override
    public String toString() {
        return XmlUtil.objectToXml(this);
    }
}