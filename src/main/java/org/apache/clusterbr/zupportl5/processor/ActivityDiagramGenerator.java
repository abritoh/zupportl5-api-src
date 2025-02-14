package org.apache.clusterbr.zupportl5.processor;

import java.util.List;

import org.apache.clusterbr.zupportl5.annotations.ExecuteJavadocProcessing;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.MethodInfo;
import io.github.classgraph.ScanResult;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ActivityDiagramGenerator_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author ArBR
 * @since 2024-1106
* <!-- comment-processor-end -->
 */
@ExecuteJavadocProcessing
public class ActivityDiagramGenerator extends DiagramGeneratorBase {

    @Override
    public void generate() {
    
        String msg;
    
        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()
                .acceptPackages(generatePlantumlForPackage)
                .scan()) {
    
            List<ClassInfo> classInfos = scanResult.getAllClasses();
    
            for (ClassInfo classInfo : classInfos) {
    
                StringBuilder plantUML = new StringBuilder("@startuml")
                    .append(ProcessorSettings.DiagramGenerator.NEW_LINE);
    
                plantUML.append("skinparam activity {")
                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                        .append(" BackgroundColor ").append(ProcessorSettings.DiagramGenerator.BACKGROUND_COLOR)
                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                        .append(" BorderColor ").append(ProcessorSettings.DiagramGenerator.BORDER_COLOR)
                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                        .append(" ArrowColor ").append(ProcessorSettings.DiagramGenerator.ARROW_COLOR)
                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                        .append("}").append(ProcessorSettings.DiagramGenerator.NEW_LINE);
    
                plantUML.append("!pragma useVerticalIf on").append(ProcessorSettings.DiagramGenerator.NEW_LINE);
                plantUML.append("start").append(ProcessorSettings.DiagramGenerator.NEW_LINE);
    
                plantUML.append("title Activity Diagram for ")
                        .append(sanitize(classInfo.getSimpleName()))
                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE);

                if (isRelevantForActivityDiagram(classInfo)) {

                    for (MethodInfo method : classInfo.getDeclaredMethodInfo()) {
                        String methodName = sanitize(method.getName());
                    
                        if (requiresDetailedMode(method)) {
                            plantUML.append("if (").append(methodName).append(") then (yes)")
                                    .append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                                    .append(":").append(methodName).append(";")
                                    .append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                                    .append("else (nothing)").append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                                    .append(":No Operation;").append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                                    .append("endif").append(ProcessorSettings.DiagramGenerator.NEW_LINE);
                        } else {
                            plantUML.append("if (").append(methodName).append(") then (yes)")
                                    .append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                                    .append(":").append(methodName).append(";")
                                    .append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                                    .append("endif").append(ProcessorSettings.DiagramGenerator.NEW_LINE);
                        }
                    }
                }
    
                plantUML.append("stop").append(ProcessorSettings.DiagramGenerator.NEW_LINE);
                plantUML.append("@enduml").append(ProcessorSettings.DiagramGenerator.NEW_LINE);
    
                if (isRelevantForActivityDiagram(classInfo)) {
                    String className = sanitize(classInfo.getSimpleName());
                    String diagramFileName = className + "_activity.puml";
                    super.savePUMLAndGenerateDiagram(plantUML, diagramFileName);
                }
    
            }
    
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("[Exception] (ActivityDiagramGenerator)", ex);
        }
    }

    private boolean isRelevantForActivityDiagram(ClassInfo classInfo) {
        String className = classInfo.getSimpleName().toLowerCase();
        return className.contains("tasklet") || 
               className.contains("service") || 
               className.contains("controller") ||
               className.contains("config") ||
               classInfo.hasAnnotation("org.springframework.batch.core.step.tasklet.Tasklet") ||
               classInfo.hasAnnotation("org.springframework.stereotype.Service") ||
               classInfo.hasAnnotation("org.springframework.stereotype.Controller");
    }
}
