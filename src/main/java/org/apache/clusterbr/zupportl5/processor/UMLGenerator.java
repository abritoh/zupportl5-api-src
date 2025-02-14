package org.apache.clusterbr.zupportl5.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/UMLGenerator_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
public class UMLGenerator {
    
    protected static final Logger logger = LoggerFactory.getLogger(UMLGenerator.class);
    
    public static void main(String[] args) {

        try {

            ClassDiagramGenerator classDiagramGenerator = new ClassDiagramGenerator();
            classDiagramGenerator.generate();

            SequenceDiagramGenerator sequenceDiagramGenerator = new SequenceDiagramGenerator();
            sequenceDiagramGenerator.generate();

            UCDiagramGenerator ucDiagramGenerator = new UCDiagramGenerator();
            ucDiagramGenerator.generate();

            ActivityDiagramGenerator activityDiagramGenerator = new ActivityDiagramGenerator();
            activityDiagramGenerator.generate();

            StaticDiagramGenerator staticDiagramGenerator = new StaticDiagramGenerator();
            staticDiagramGenerator.generate();

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("[Exception] (UMLGenerator)", ex);
        }
        
    }
}
