package org.apache.clusterbr.zupportl5.processor;

import java.io.File;

import org.springframework.context.annotation.Configuration;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/StaticDiagramGenerator_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/StaticDiagramGenerator_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/StaticDiagramGenerator_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Configuration
public class StaticDiagramGenerator extends DiagramGeneratorBase {
    
    @Override
    public void generate() {
        File staticPumlDir = new File(ProcessorSettings.DiagramGenerator.PLANTUML_STATIC_PUML_FOLDER_DEFAULT);
    
        if (!staticPumlDir.exists() || !staticPumlDir.isDirectory()) {
            logger.warn("(StaticDiagramGenerator) Static PUML directory not found: " + staticPumlDir.getAbsolutePath());
            return;
        }
    
        File[] pumlFiles = staticPumlDir.listFiles((dir, name) -> name.endsWith(".puml"));
    
        if (pumlFiles == null || pumlFiles.length == 0) {
            logger.warn("(StaticDiagramGenerator) No PUML files found in static directory: " + staticPumlDir.getAbsolutePath());
            return;
        }
    
        for (File pumlFile : pumlFiles) {
            try {
                super.generateDiagramFromStaticPuml(pumlFile);
    
                String msg = String.format("(StaticDiagramGenerator) Generated PNG for file: %s", pumlFile.getName());
                logger.info(msg);
            } catch (Exception ex) {
                String errMsg = String.format("(StaticDiagramGenerator) Error generating PNG for file: %s", pumlFile.getName());
                logger.error(errMsg, ex);
            }
        }
    }
}


