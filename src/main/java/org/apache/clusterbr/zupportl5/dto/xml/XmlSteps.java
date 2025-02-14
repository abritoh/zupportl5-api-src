package org.apache.clusterbr.zupportl5.dto.xml;

import java.io.Serializable;
import java.util.List;

import org.apache.clusterbr.zupportl5.utils.XmlUtil;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;


/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/XmlSteps_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@XmlRootElement(name = "steps")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlSteps implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlElement(name = "step")
    private List<String> stepList;

    //-- Getters and Setter
    
    public List<String> getStepList() {
        return stepList;
    }

    public void setStepList(List<String> stepList) {
        this.stepList = stepList;
    }
        
    @Override
    public String toString() {
        return XmlUtil.objectToXml(this);
    }
}
