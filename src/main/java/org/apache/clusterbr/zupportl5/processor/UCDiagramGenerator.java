package org.apache.clusterbr.zupportl5.processor;

import java.util.List;

import org.apache.clusterbr.zupportl5.annotations.SkipJavadocProcessing;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.MethodInfo;
import io.github.classgraph.ScanResult;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DiagramGeneratorBase_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@SkipJavadocProcessing
public class UCDiagramGenerator extends DiagramGeneratorBase {

    @Override
    public void generate() {

        String msg;

        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()
                .acceptPackages(generatePlantumlForPackage)
                .scan()) {

            List<ClassInfo> classInfos = scanResult.getAllClasses();

            for (ClassInfo classInfo : classInfos) {

                if (!isRelevantForUseCaseAndSequence(classInfo)) continue;

                StringBuilder plantUML = new StringBuilder("@startuml")
                    .append(ProcessorSettings.DiagramGenerator.NEW_LINE);

                plantUML.append("skinparam usecase {")
                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                        .append(" BackgroundColor ").append(ProcessorSettings.DiagramGenerator.BACKGROUND_COLOR)
                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                        .append(" BorderColor ").append(ProcessorSettings.DiagramGenerator.BORDER_COLOR)
                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                        .append(" ArrowColor ").append(ProcessorSettings.DiagramGenerator.ARROW_COLOR)
                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                        .append("}").append(ProcessorSettings.DiagramGenerator.NEW_LINE);

                plantUML.append("left to right direction").append(ProcessorSettings.DiagramGenerator.NEW_LINE);
                plantUML.append("title Use-Case Diagram for ")
                        .append(sanitize(classInfo.getSimpleName()))
                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE);

                plantUML.append("actor User").append(ProcessorSettings.DiagramGenerator.NEW_LINE);

                boolean hasUseCases = false;

                for (MethodInfo method : classInfo.getDeclaredMethodInfo()) {

                    if (!method.isPublic()) {
                        continue; 
                    }

                    String useCaseName = sanitize(method.getName());

                    plantUML.append("usecase \"")
                            .append(useCaseName)
                            .append("\" as UC_")
                            .append(useCaseName)
                            .append(ProcessorSettings.DiagramGenerator.NEW_LINE);

                    plantUML.append("User --> UC_").append(useCaseName).append(ProcessorSettings.DiagramGenerator.NEW_LINE);
                    hasUseCases = true;
                }

                if (!hasUseCases) {
                    logger.info("(UCDiagramGenerator) Skipping class: {} - No use cases found", classInfo.getSimpleName());
                    continue;
                }

                plantUML.append("@enduml");

                if (isRelevantForUseCaseAndSequence(classInfo)) {
                    String className = sanitize(classInfo.getSimpleName());
                    String diagramFileName = className + "_usecase.puml";
                    super.savePUMLAndGenerateDiagram(plantUML, diagramFileName);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("[Exception] (UCDiagramGenerator)", ex);
        }
    }
}
