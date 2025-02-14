package org.apache.clusterbr.zupportl5.processor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.MethodParameterInfo;
import io.github.classgraph.ScanResult;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/SequenceDiagramGenerator_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
public class SequenceDiagramGenerator extends DiagramGeneratorBase {

    @Override
    public void generate() {
    
        String msg;
    
        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()
                .acceptPackages(generatePlantumlForPackage)
                .scan()) {
    
            List<ClassInfo> classInfos = scanResult.getAllClasses();
    
            for (ClassInfo classInfo : classInfos) {

                if ( !isRelevantForUseCaseAndSequence(classInfo) ) continue;
    
                //-- initialize PlantUML content for sequence diagram
                StringBuilder plantUML = new StringBuilder("@startuml")
                    .append(ProcessorSettings.DiagramGenerator.NEW_LINE);
    
                plantUML.append("skinparam sequence {")
                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                        .append(" ArrowColor ").append(ProcessorSettings.DiagramGenerator.ARROW_COLOR)
                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                        .append(" ParticipantBorderColor ").append(ProcessorSettings.DiagramGenerator.BORDER_COLOR)
                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                        .append(" ParticipantBackgroundColor ").append(ProcessorSettings.DiagramGenerator.BACKGROUND_COLOR)
                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                        .append("}")
                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE);
    
                plantUML.append("title Sequence Diagram for ")
                        .append(sanitize(classInfo.getSimpleName()))
                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE);
    
                plantUML.append("actor User").append(ProcessorSettings.DiagramGenerator.NEW_LINE);
    
                Set<String> relevantParticipants = new HashSet<>();
                relevantParticipants.add(sanitize(classInfo.getSimpleName()));
    
                classInfo.getDeclaredMethodInfo().forEach(method -> {
    
                    plantUML.append("User -> ")
                            .append(sanitize(classInfo.getSimpleName()))
                            .append(": ")
                            .append(sanitize(method.getName()))
                            .append("()").append(ProcessorSettings.DiagramGenerator.NEW_LINE);
    
                    MethodParameterInfo[] parametersInfo = method.getParameterInfo();
                    for (MethodParameterInfo parameter : parametersInfo) {
    
                        String paramType = parameter.getTypeSignatureOrTypeDescriptor().toString();
                        if (paramType.contains(".")) { 
                            String paramClassName = sanitize(paramType.substring(paramType.lastIndexOf('.') + 1));
    
                            if (relevantParticipants.add(paramClassName)) {
                                plantUML.append("participant ").append(paramClassName)
                                    .append(ProcessorSettings.DiagramGenerator.NEW_LINE);
                            }
    
                            plantUML.append(sanitize(classInfo.getSimpleName()))
                                    .append(" -> ").append(paramClassName)
                                    .append(": invoke dependency").append(ProcessorSettings.DiagramGenerator.NEW_LINE);
                        }
                    }
    
                    plantUML.append(sanitize(classInfo.getSimpleName()))
                            .append(" --> User : return").append(ProcessorSettings.DiagramGenerator.NEW_LINE);
                });
    
                plantUML.append("@enduml");
    
                if (isRelevantForUseCaseAndSequence(classInfo)) {
                    String className = sanitize(classInfo.getSimpleName());
                    String diagramFileName = className + "_seq.puml";
                    super.savePUMLAndGenerateDiagram(plantUML, diagramFileName);
                }
            }
    
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("[Exception] (SequenceDiagramGenerator)", ex);
        }
    }
        
}

