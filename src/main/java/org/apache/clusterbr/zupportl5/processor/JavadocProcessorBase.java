package org.apache.clusterbr.zupportl5.processor;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.clusterbr.zupportl5.annotations.ExecuteJavadocProcessing;
import org.apache.clusterbr.zupportl5.annotations.SkipJavadocProcessing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/JavadocProcessorBase_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@SkipJavadocProcessing
public class JavadocProcessorBase {

    protected static final Logger logger = LoggerFactory.getLogger(JavadocProcessor.class);

    protected boolean shouldSkipProcessing(File file) {
        if (containsSkipAnnotation(file)) {
            logger.info("(JavadocProcessor) file ({}) contains @SkipJavadocProcessing annotation", file.getName());
            return true;
        }
        if (containsExecuteAnnotation(file)) {
            logger.info("(JavadocProcessor) file ({}) contains @ExecuteJavadocProcessing annotation", file.getName());
            return false;
        }
        return false;
    } 

    protected JavadocCommentBuilder generateRestApiComments(Class<?> controllerClass) {

        JavadocCommentBuilder apiComments = new JavadocCommentBuilder();

        String baseEndpoint = "";
        if ( controllerClass.isAnnotationPresent(RequestMapping.class) ) {
            RequestMapping requestMappingClass = controllerClass.getAnnotation(RequestMapping.class);
            baseEndpoint = String.join(", ", requestMappingClass.value());

            apiComments.insertCommentLine(ProcessorSettings.JavadocProcessorConfig.TAG_REST_API_END_POINTS);
            apiComments.insertCommentLine("<ul>");
        }

        for (Method method : controllerClass.getDeclaredMethods()) {
            String httpMethod = "";
            String endpoint = "";
            String description = "";

            if (method.isAnnotationPresent(GetMapping.class)) {
                GetMapping getMapping = method.getAnnotation(GetMapping.class);
                httpMethod = "GET";
                endpoint = String.join(", ", getMapping.value());
                description = "Returns data from the API";
            } else if (method.isAnnotationPresent(PostMapping.class)) {
                PostMapping postMapping = method.getAnnotation(PostMapping.class);
                httpMethod = "POST";
                endpoint = String.join(", ", postMapping.value());
                description = "Creates a new entry in the API";
            } else if (method.isAnnotationPresent(PutMapping.class)) {
                PutMapping putMapping = method.getAnnotation(PutMapping.class);
                httpMethod = "PUT";
                endpoint = String.join(", ", putMapping.value());
                description = "Updates an entry by ID";
            } else if (method.isAnnotationPresent(PatchMapping.class)) {
                PatchMapping patchMapping = method.getAnnotation(PatchMapping.class);
                httpMethod = "PATCH";
                endpoint = String.join(", ", patchMapping.value());
                description = "Partially updates an entry by ID";
            } else if (method.isAnnotationPresent(DeleteMapping.class)) {
                DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
                httpMethod = "DELETE";
                endpoint = String.join(", ", deleteMapping.value());
                description = "Deletes an entry by ID";
            } else if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
                httpMethod = requestMapping.method()[0].name(); // Get the HTTP method
                endpoint = String.join(", ", requestMapping.value());
                description = "Handles the request for the given endpoint";
            }

            if (!httpMethod.isEmpty() && !endpoint.isEmpty()) {
                endpoint = baseEndpoint + endpoint;
                String comment = String.format("<li>%s %s: %s</li>", httpMethod, endpoint, description);
                apiComments.insertCommentLine(comment);
            }
        }

        if ( controllerClass.isAnnotationPresent(RequestMapping.class) ) {
            apiComments.insertCommentLine("</ul>");
        }

        return apiComments;
    }

    protected boolean containsSkipAnnotation(File file) {
        try {
            String className = getClassFullName(file);
            Class<?> clazz = Class.forName(className); 
            return clazz.isAnnotationPresent(SkipJavadocProcessing.class);
        } catch (ClassNotFoundException e) {
            logger.warn("(JavadocProcessor) Class not found: {}. Skipping annotation check.", file.getName());
        } catch (Exception e) {
            logger.error("(JavadocProcessor) Unexpected error checking annotations: {}", file.getName(), e);
        }
        return false;
    }

    protected boolean containsExecuteAnnotation(File file) {
        try {
            String className = getClassFullName(file);
            Class<?> clazz = Class.forName(className); 
            return clazz.isAnnotationPresent(ExecuteJavadocProcessing.class);
        } catch (ClassNotFoundException e) {
            logger.warn("(JavadocProcessor) Class not found: {}. Skipping annotation check.", file.getName());
        } catch (Exception e) {
            logger.error("(JavadocProcessor) Unexpected error checking annotations: {}", file.getName(), e);
        }
        return false;
    }

    protected String getClassName(File file) {
        String fileName = file.getName();        
        if (fileName.endsWith(".java")) {
            return fileName.substring(0, fileName.length() - 5);
        }        
        return fileName;
    }    

    protected String getClassFullName(File file) {
        try {
            String absolutePath = file.getCanonicalPath();
            
            int srcIndex = absolutePath.indexOf("src" + File.separator + "main" + File.separator + "java");
            if (srcIndex == -1) {
                throw new IllegalArgumentException("File is not located within 'src/main/java' directory: " + absolutePath);
            }
    
            String relativePath = absolutePath.substring(srcIndex + ("src" + File.separator + "main" + File.separator + "java").length() + 1);
            
            String classFullName = relativePath
                .replace(File.separator, ".")
                .replace(".java", "");
            
            return classFullName;
        } catch (IOException e) {
            throw new RuntimeException("Error resolving class full name for file: " + file, e);
        }
    }

    protected String extractFullyQualifiedClassName(String content) {
        Pattern packagePattern = Pattern.compile("package\\s+([\\w\\.]+);");
        Matcher packageMatcher = packagePattern.matcher(content);
        String packageName = packageMatcher.find() ? packageMatcher.group(1) : "";

        Pattern classPattern = Pattern.compile("class\\s+(\\w+)");
        Matcher classMatcher = classPattern.matcher(content);
        String className = classMatcher.find() ? classMatcher.group(1) : "";

        return packageName.isEmpty() ? className : packageName + "." + className;
    }

    protected void processRestore(File file, String content) throws IOException {        
        content = deleteJavadocProcessorComments(content);
        Files.write(file.toPath(), content.getBytes());
        logger.info("(JavadocProcessor) Restored file: {}", file.getName());
    }

    protected void writeUpdatedContent(File file, String content) throws IOException {
        Files.write(file.toPath(), content.getBytes());
        logger.info("(JavadocProcessor) Processed file: {}", file.getName());
    }
 

    /**
     * Deletes the entire class-level Javadoc comment, ensuring it is removed only if it precedes
     * a class, interface, enum, or record declaration, regardless of its access modifier.
     *
     * @param content the source code content
     * @return the modified source code with the class-level Javadoc removed
     */
    protected String deleteEntireClassLevelComment(String content) {
        return content.replaceAll(
            "(?s)^\\s*/\\*\\*.*?\\*/\\s*(?=\\s*(public|protected|private)?\\s*(class|interface|enum|record)\\b)",
            ""
        );
    }    

    /**
     * Delete all content between <!-- comment-processor-start --> and <!-- comment-processor-end --> (inclusive)
     * The regex matches everything between 
     * <!-- comment-processor-start --> and <!-- comment-processor-end -->
     * including the start and end markers.
     * The (?s) flag allows . to match newline characters.
     * @param content
     * @return String
     */
    protected String deleteJavadocProcessorComments(String content) {
        return content.replaceAll("(?s)\\* <!-- comment-processor-start -->.*?\\* <!-- comment-processor-end -->", "");
    }

    /**
     * Replaces or inserts comment processor tags and content into the source code.
     * Handles three cases:
     * - No comments at all at class level: Inserts the new comment block before the class.
     * - Tags do not exist but class-level comments exist: Adds the new comments just before the ending of the existing class-level comments.
     * - Tags exist and class-level comments exist: Replaces content between the tags with new comments.
     *
     * @param fileSourceCode the full source code of the file
     * @param newComments    the new comments to be added
     * @return the modified source code
     */
    protected String replaceOrInsertJavadocProcessorComments(String fileSourceCode, String newComments) {
        String startTag = " * <!-- comment-processor-start -->";
        String endTag   = " * <!-- comment-processor-end -->";
    
        //-- case 1: No comments at all
        if (!fileSourceCode.contains(startTag) && !fileSourceCode.contains(endTag)) {
            String processorBlock = "/**\n" + startTag + "\n" + newComments + "\n " + endTag + "\n */\n";
            
            int classInsertionPoint = findClassLevelInsertionPoint(fileSourceCode),
                classAnnotationStartPoint = findClassLevelAnnotationStart(fileSourceCode);

            classInsertionPoint = (classAnnotationStartPoint != -1) &&  (classAnnotationStartPoint < classInsertionPoint)
                ? classAnnotationStartPoint 
                : classInsertionPoint;

            return insertAt(fileSourceCode, processorBlock, classInsertionPoint);
        }

        // case 2: Tags exist and class-level comments exist
        if (fileSourceCode.contains(startTag) && fileSourceCode.contains(endTag)) {
            return fileSourceCode.replaceAll(
                "(?s)(\\* <!-- comment-processor-start -->).*?(\\* <!-- comment-processor-end -->)",
                "$1\n" + newComments + "\n$2"
            );
        }        

        // case 3: Class-level comment exists, but tags do not
        if (fileSourceCode.contains("/**") && !fileSourceCode.contains(startTag)) {
            int commentStartIndex = fileSourceCode.indexOf("/**");
            int commentEndIndex = fileSourceCode.indexOf("*/", commentStartIndex) + 2;

            String existingCommentWithoutStartEnd = fileSourceCode.substring(commentStartIndex + 2, commentEndIndex - 2).trim();

            String processorBlock = "\n * <!-- comment-processor-start -->\n" + newComments + "\n * <!-- comment-processor-end -->";

            String newClassComment = "/**\n" + existingCommentWithoutStartEnd + processorBlock + "\n */";

            return fileSourceCode.substring(0, commentStartIndex) + newClassComment + fileSourceCode.substring(commentEndIndex);
        }
    
        return fileSourceCode;
    }
    
    /**
     * Finds the insertion point for class-level comments, looking for the class, interface, enum, or record keywords.
     * @param content the source code content
     * @return the index where the block should be inserted
     */
    private int findClassLevelInsertionPoint(String content) {
        var matcher = Pattern.compile("(?m)^\\s*(public|protected|private)?\\s*(class|interface|enum|record)\\b").matcher(content);
        if (matcher.find()) {
            return matcher.start();
        }
        return 0;
    }

    /**
     * Finds the start index of the first class-level annotation (e.g., @SkipJavadocProcessing).
     * @param content the source code content
     * @return the index of the first class-level annotation or -1 if none are found
     */
    private int findClassLevelAnnotationStart(String content) {
        var matcher = Pattern.compile("(?m)@(\\w+)\\b.*\\s*(class|enum|interface|record)\\b\\s+(\\w+)").matcher(content);
        if (matcher.find()) {
            return matcher.start();
        }        
        return -1;
    }
    
    /**
     * Inserts a block of text at a specific index in the source content.
     * @param content the original content
     * @param block the block of text to insert
     * @param index the index where the block should be inserted
     * @return the modified content
     */
    private String insertAt(String content, String block, int index) {
        return content.substring(0, index) + block + content.substring(index);
    }    
 
}

