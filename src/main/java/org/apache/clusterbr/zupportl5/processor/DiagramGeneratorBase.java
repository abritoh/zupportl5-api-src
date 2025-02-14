package org.apache.clusterbr.zupportl5.processor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.clusterbr.zupportl5.annotations.SkipJavadocProcessing;
import org.apache.clusterbr.zupportl5.utils.AppConstants;
import org.apache.clusterbr.zupportl5.utils.AppLangUtil;
import org.apache.clusterbr.zupportl5.utils.PropertiesProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.classgraph.ClassInfo;
import io.github.classgraph.MethodInfo;
import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.SourceFileReader;

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
public abstract class DiagramGeneratorBase {
    
    protected static final Logger logger = LoggerFactory.getLogger(DiagramGeneratorBase.class);

    protected String 
        generatePlantumlForPackage = null
        , plantumlDestinationFolderPuml = null
        , plantumlDestinationFolderImg = null
        , staticPumlFolder = null
        ;

    protected PropertiesProvider 
        propertiesProvider = null;

    protected File 
        outputDirPuml = null, 
        outputDirImg = null
        ;

    
    public DiagramGeneratorBase() {

        propertiesProvider = PropertiesProvider.getInstance();

        generatePlantumlForPackage = propertiesProvider
                .getPropertyOrDefault("application.generate.plantuml.for.package", ProcessorSettings.DiagramGenerator.GENERATE_PLANTUML_FOR_PACKAGE_DEFAULT);

        plantumlDestinationFolderPuml = propertiesProvider
                .getPropertyOrDefault("application.plantuml.destination.folder.puml", ProcessorSettings.DiagramGenerator.PLANTUML_DESTINATION_FOLDER_DEFAULT);

        plantumlDestinationFolderImg = propertiesProvider
                .getPropertyOrDefault("application.plantuml.destination.folder.images", ProcessorSettings.DiagramGenerator.IMAGES_DESTINATION_FOLDER_DEFAULT);

        staticPumlFolder = propertiesProvider
                .getPropertyOrDefault("application.plantuml.static.puml.folder", ProcessorSettings.DiagramGenerator.PLANTUML_STATIC_PUML_FOLDER_DEFAULT);


        //-- <PUML> folder
        outputDirPuml = new File(plantumlDestinationFolderPuml);
        if (!outputDirPuml.exists()) {
            outputDirPuml.mkdirs();
        }

        //-- <images> folder
        outputDirImg = new File(plantumlDestinationFolderImg);
        if (!outputDirImg.isAbsolute()) {
            outputDirImg = outputDirImg.getAbsoluteFile();
        }
        if (!outputDirImg.exists()) {
            outputDirImg.mkdirs();
        }
    }

    //-- force to implement
    public abstract void generate ();

    //-- protected

    protected String sanitize(String input) {
        return AppLangUtil.sanitizeText(input);
    }

    protected void savePUMLAndGenerateDiagram(StringBuilder plantUML, String diagramFileName) {
        
        File pumlFile = new File(outputDirPuml,  diagramFileName);
        File staticPumlFile = new File(staticPumlFolder, diagramFileName);

        try (FileWriter writer = new FileWriter(pumlFile, false)) {

            writer.write(plantUML.toString());
            String msg = String.format("(ClassDiagramGenerator) PUML file generated : %s", pumlFile);
            logger.info(msg);           

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("[Exception] (DiagramGeneratorBase)", ex);
        }

        if (staticPumlFile.exists()) {
            generateDiagramFromStaticPuml(staticPumlFile);
        } else {
            generateDiagramFromDynamicPuml(pumlFile);
        }
    }

    protected void generateDiagramFromStaticPuml(File pumlFile) {
        String fmtLogMsg = "(DiagramGeneratorBase) PNG file generated from Static PUML: %s";
        generateDiagramFromPuml(pumlFile, fmtLogMsg);
    }

    protected void generateDiagramFromDynamicPuml(File pumlFile) {
        String fmtLogMsg = "(DiagramGeneratorBase) PNG file generated from dynamic PUML: %s";
        generateDiagramFromPuml(pumlFile, fmtLogMsg);
    }    

    private void generateDiagramFromPuml(File pumlFile, String fmtLogMsg) {

        if (!pumlFile.exists()) {
            logger.error("[Error] (DiagramGeneratorBase) PUML file does not exists: %s", pumlFile.getName());
        }

        try {

            int complexity = estimateDiagramComplexity(pumlFile);

            configureImageSettings(pumlFile);
            if (complexity > 50) {
                insertScaleDirective(pumlFile, 3);
            }
    
           SourceFileReader reader = new SourceFileReader(pumlFile, outputDirImg);
           List<GeneratedImage> list = reader.getGeneratedImages();

           for (GeneratedImage image : list) {
                File pngFile = image.getPngFile();
                String msg = String.format(fmtLogMsg, pngFile);
                logger.info(msg);
            }

        } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("[Exception] (DiagramGeneratorBase)", ex);
        }
    }   
    
    protected boolean isRelevantForUseCaseAndSequence(ClassInfo classInfo) {

        String className = classInfo.getSimpleName().toLowerCase();
    
        //-- exclude classes based on name first
        if (
            className.contains("dto")
            || className.contains("util")
            || className.contains("config")
            || className.contains("enum")
            || className.contains("repository") 
            ) {
            return false;
        }
    
        //-- include only relevant classes by name or annotations
        return className.contains("controller") 
               || className.contains("service")
               || className.contains("usecase")
               || classInfo.hasAnnotation("org.springframework.stereotype.Controller")
               || classInfo.hasAnnotation("org.springframework.stereotype.Service")
               || classInfo.hasAnnotation("org.springframework.stereotype.Component")
               || classInfo.implementsInterface("org.springframework.batch.core.step.tasklet.Tasklet")
               || classInfo.implementsInterface("org.springframework.web.servlet.HandlerInterceptor")
               ;
    } 

    protected boolean requiresDetailedMode(MethodInfo method) {
        return method.hasAnnotation("org.apache.clusterbr.annotations.Detailed") 
            || method.getName().toLowerCase().contains("lambda");
    }

    //-- protected

    private void insertScaleDirective(File pumlFile, int scale) throws IOException {
        List<String> lines = Files.readAllLines(pumlFile.toPath());
        List<String> updatedLines = new ArrayList<>();
        
        for (String line : lines) {
            if (line.startsWith("@startuml")) {
                updatedLines.add(line);
            } else {
                updatedLines.add(line);
            }
        }
        Files.write(pumlFile.toPath(), updatedLines);
    }    
    
    private void configureImageSettings(File pumlFile) throws IOException {

        int complexity = estimateDiagramComplexity(pumlFile);
    
        if (complexity < 20) {
            System.setProperty("plantuml.dpi", "96");
            System.setProperty("plantuml.limit_size", "4096");
        } else if (complexity <= 50) {
            System.setProperty("plantuml.dpi", "150");
            System.setProperty("plantuml.limit_size", "8192");
        } else {
            System.setProperty("plantuml.dpi", "300");
            System.setProperty("plantuml.limit_size", "16384");
        }
    }    

    private int estimateDiagramComplexity(File pumlFile) throws IOException {
        int complexityScore = 0;
        List<String> lines = Files.readAllLines(pumlFile.toPath());    
        for (String line : lines) {
            if (line.startsWith("@startuml") || line.startsWith("@enduml")) continue;
            if (line.contains("if") || line.contains("loop") || line.contains("else")) complexityScore += 5;
            if (line.contains(":") || line.contains("->") || line.contains("-->")) complexityScore += 2;
            if (line.contains("participant") || line.contains("actor")) complexityScore += 3;
        }    
        return complexityScore;
    }    

}
