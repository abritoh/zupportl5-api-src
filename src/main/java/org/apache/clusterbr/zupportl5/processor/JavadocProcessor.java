package org.apache.clusterbr.zupportl5.processor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.clusterbr.zupportl5.annotations.SkipJavadocProcessing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/JavadocProcessor_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
public class JavadocProcessor extends JavadocProcessorBase {

    private static final Logger logger = LoggerFactory.getLogger(JavadocProcessor.class);

    public static void main(String[] args) throws IOException {

        Path sourceDir = Paths.get(ProcessorSettings.JavadocProcessorConfig.PROJECT_SOURCE_CODE_PATH);

        JavadocCommentBuilder commentsBefore = new JavadocCommentBuilder();
        JavadocCommentBuilder commentsAfter = new JavadocCommentBuilder();

        commentsAfter.insertCommentLine(ProcessorSettings.JavadocProcessorConfig.SPACE);
        commentsAfter.insertCommentLine(ProcessorSettings.JavadocProcessorConfig.COMMENTS_AFTER_AUTHOR);
        commentsAfter.insertCommentLine(ProcessorSettings.JavadocProcessorConfig.COMMENTS_AFTER_SINCE);

        JavadocProcessor processor = new JavadocProcessor();

        Files.walk(sourceDir)
            .filter(path -> path.toString().endsWith(".java"))
            .forEach(path -> processor.processFile(path, false, commentsBefore, commentsAfter));
    }

    public void processFile(Path javaFilePath, boolean restore, 
                JavadocCommentBuilder commentsBefore, JavadocCommentBuilder commentsAfter) {

        File javaFile = javaFilePath.toFile();        

        try {

            String fileSourceCode = new String(Files.readAllBytes(javaFile.toPath()));

            if (restore) {
                processRestore(javaFile, fileSourceCode);
                return;
            }

            if (shouldSkipProcessing(javaFile)) return;

            String className = getClassName(javaFile);

            String classFullName = getClassFullName(javaFile);

            JavadocCommentBuilder apiComments = generateRestAPIComments(classFullName);

            JavadocCommentBuilder diagramsContent = generateDiagramComments(className);

            JavadocCommentBuilder javadocComments = constructJavadocComments(
                            apiComments, commentsBefore, diagramsContent, commentsAfter);

            fileSourceCode = replaceOrInsertJavadocProcessorComments(fileSourceCode, javadocComments.toString());

            writeUpdatedContent(javaFile, fileSourceCode);

        } catch (Exception ex) {
            logger.error("(JavadocProcessor) Error processing file: {}", javaFile.getName(), ex);
        }
    }

    private JavadocCommentBuilder constructJavadocComments (
        JavadocCommentBuilder apiComments, JavadocCommentBuilder commentsBefore, 
        JavadocCommentBuilder diagramsContent, JavadocCommentBuilder commentsAfter) {

        JavadocCommentBuilder javadocBuilder = new JavadocCommentBuilder();

        if (apiComments.haveComments() ) javadocBuilder.insertJavadocCommentBuilder( apiComments );

        if (commentsBefore.haveComments() ) javadocBuilder.insertJavadocCommentBuilder( commentsBefore );

        if (diagramsContent.haveComments() ) javadocBuilder.insertJavadocCommentBuilder( diagramsContent );

        if (commentsAfter.haveComments() ) javadocBuilder.insertJavadocCommentBuilder( commentsAfter );

        return javadocBuilder;
    } 

    private JavadocCommentBuilder generateRestAPIComments(String classFullName) {
        
        JavadocCommentBuilder apiComments = new JavadocCommentBuilder();

        try {
            Class<?> clazz = Class.forName(classFullName);
            if (clazz.isAnnotationPresent(RestController.class) 
                || clazz.isAnnotationPresent(RequestMapping.class)) {
                apiComments = generateRestApiComments(clazz);                
            }
        } catch (ClassNotFoundException e) {
            logger.warn("(JavadocProcessor) Class not found: {}. Skipping annotation check.", classFullName);
        }
        return apiComments;
    }

    private JavadocCommentBuilder generateDiagramComments(String className) {

        JavadocCommentBuilder diagramsComments = new JavadocCommentBuilder();
        
        diagramsComments.insertCommentLine(ProcessorSettings.JavadocProcessorConfig.SPACE);
        diagramsComments.insertCommentLine(ProcessorSettings.JavadocProcessorConfig.TAG_UML_DIAGRAMS);

        boolean generated = false;
        for (String diagramType : ProcessorSettings.JavadocProcessorConfig.STR_ARRAY_DIAGRAM_TYPES) {

            String diagramFileName = className + "_" + diagramType + ".png";
            File diagramFile = new File(ProcessorSettings.JavadocProcessorConfig.INPUT_DIAGRAMS_FOLDER, diagramFileName);

            if (diagramFile.exists()) {                

                String imageJavadocComment = 
                    String.format(ProcessorSettings.JavadocProcessorConfig.PLANTUML_DIAGRAM_TAG_TEMPLATE, 
                        diagramFileName, diagramType.toUpperCase(), diagramType);
                
                diagramsComments.insertCommentLine(imageJavadocComment);

                generated = true;

            }
        }

        return generated ? diagramsComments : new JavadocCommentBuilder();
    }

}
