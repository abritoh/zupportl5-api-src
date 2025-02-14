package org.apache.clusterbr.zupportl5.processor;

import java.util.List;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ClassDiagramGenerator_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
public class ClassDiagramGenerator extends DiagramGeneratorBase {

    @Override
    public void generate () {

        String msg;
        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo()
                .acceptPackages(generatePlantumlForPackage)
                .scan() ) {

            List<ClassInfo> classInfos = scanResult.getAllClasses();

            for (ClassInfo classInfo : classInfos) {

                StringBuilder plantUML = new StringBuilder("@startuml ")
                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE);

                plantUML.append("skinparam class { ")
                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                        .append(" BackgroundColor ").append(ProcessorSettings.DiagramGenerator.BACKGROUND_COLOR)
                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                        .append(" ArrowColor ").append(ProcessorSettings.DiagramGenerator.ARROW_COLOR)
                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                        .append(" BorderColor ").append(ProcessorSettings.DiagramGenerator.BORDER_COLOR)
                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                        .append("}")
                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                        ;

                //-- title and specific configuration for the diagram
                plantUML.append("left to right direction ")
                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE);                
                plantUML.append("title Class Diagram for ")
                        .append(classInfo.getPackageName()).append(".")
                        .append(classInfo.getSimpleName())
                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                        .append("skinparam titleBackgroundColor White")
                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                        .append("skinparam classHeaderBackgroundColor ")
                        .append(ProcessorSettings.DiagramGenerator.ARROW_COLOR)
                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                        .append("skinparam stereotypeCBackgroundColor ")
                        .append(ProcessorSettings.DiagramGenerator.STEREOTYPE_CB_BG_COLOR)
                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                        ;

                String className = classInfo.getSimpleName();
                plantUML.append("class ")
                        .append(className)
                        .append(" {").append(ProcessorSettings.DiagramGenerator.NEW_LINE);

                //-- fields
                classInfo.getDeclaredFieldInfo()
                        .forEach(field -> plantUML
                                        .append(" - ")
                                        .append(field.getTypeSignatureOrTypeDescriptor())
                                        .append(ProcessorSettings.DiagramGenerator.SPACE)
                                        .append(sanitize(field.getName()))
                                        .append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                                );

                //-- methods
                classInfo.getDeclaredMethodInfo()
                        .forEach(method -> plantUML
                                        .append(" + ")
                                        .append(method.getTypeSignatureOrTypeDescriptor())
                                        .append(ProcessorSettings.DiagramGenerator.SPACE)
                                        .append(sanitize(method.getName()))
                                        .append("()").append(ProcessorSettings.DiagramGenerator.NEW_LINE)
                                );

                plantUML.append("}").append(ProcessorSettings.DiagramGenerator.NEW_LINE);

                if (classInfo.getSuperclass() != null) {
                    String superclassName = sanitize(classInfo.getSuperclass().getName());
                    plantUML.append(className)
                            .append(" --|> ")
                            .append(superclassName)
                            .append(ProcessorSettings.DiagramGenerator.NEW_LINE);
                }

                classInfo.getInterfaces().forEach(implementedInterface -> {
                    String interfaceName = sanitize(implementedInterface.getName());
                    plantUML.append(className)
                            .append(" ..|> ")
                            .append(interfaceName)
                            .append(ProcessorSettings.DiagramGenerator.NEW_LINE);
                });

                plantUML.append("@enduml");

                String diagramFileName = className + "_class.puml";
                super.savePUMLAndGenerateDiagram(plantUML, diagramFileName);

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("[Exception] (ClassDiagramGenerator)", ex);
        }
    }
}
