package org.apache.clusterbr.zupportl5.processor;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.clusterbr.zupportl5.annotations.SkipJavadocProcessing;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ProcessorSettings_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@SkipJavadocProcessing
public class ProcessorSettings {

    public static final String 
    PROJECT_SOURCE_CODE_PATH = "src/main/java"
    , APPLICATION_BASE_PACKAGE = "org.apache.clusterbr"
    ;

    public class DiagramGenerator {

        private DiagramGenerator() {}

        public static final String 
        NEW_LINE = "\n" 
        , SPACE = " "
        , BACKGROUND_COLOR = "#DEE3E9 "
        , ARROW_COLOR = "#4D7A97 "
        , BORDER_COLOR = "#4D7A97 "
        , STEREOTYPE_CB_BG_COLOR = "#F8981D "
        ;            
    
        public static final String 
        GENERATE_PLANTUML_FOR_PACKAGE_DEFAULT = DiagramGeneratorBase.class.getPackage().getName()
        , PLANTUML_DESTINATION_FOLDER_DEFAULT =  "target/generated-resources/uml"
        , IMAGES_DESTINATION_FOLDER_DEFAULT   =  "target/generated-resources/uml/images"
        , PLANTUML_STATIC_PUML_FOLDER_DEFAULT =  "target/classes/static/puml"
        ;
    }

    public class CodeMetricsGeneratorConfig {
        
        private CodeMetricsGeneratorConfig() {}

        public static final String 
        PROJECT_SOURCE_CODE_PATH = ProcessorSettings.PROJECT_SOURCE_CODE_PATH
        , CLASSES_TARGET_PATH = "target/classes"
        , APPLICATION_BASE_PACKAGE = ProcessorSettings.APPLICATION_BASE_PACKAGE
        , METRICS_TEMPLATE_PATH = "target/classes/static/html/project-metrics-template.html"
        , GENERATED_HTML_REPORT_FILE = "target/classes/static/public/ZupportL5-api-metrics.html"
        ;
    }


    public class JavadocProcessorConfig {

        private JavadocProcessorConfig() {}

        public static final String 
        SPACE = " "
        , PROJECT_SOURCE_CODE_PATH = ProcessorSettings.PROJECT_SOURCE_CODE_PATH
        , INPUT_DIAGRAMS_FOLDER = "target/generated-resources/uml/images"
        , COMMENTS_AFTER_SINCE = "@since 2024-1108"
        , TAG_UML_DIAGRAMS = "<p><b>UML Diagrams:</b></p>"
        , TAG_REST_API_END_POINTS = "<p>REST-API Endpoints --</p>"
        , COMMENTS_AFTER_AUTHOR = "@author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>"        
        , PLANTUML_DIAGRAM_TAG_TEMPLATE = "<p><img src=\"{@docRoot}/generated-resources/uml/images/%s\" alt=\"UML %s Diagram\" class=\"%s\"></p>"
        ;        

        public static final String[] STR_ARRAY_DIAGRAM_TYPES = {"class", "usecase", "seq", "activity"};
    }

    public class OpenApiJsonDocGeneratorConfig {

        private OpenApiJsonDocGeneratorConfig() {}

        private static Map<String, Object> swaggerInfo;

        public static final String 
        APPLICATION_BASE_PACKAGE = ProcessorSettings.APPLICATION_BASE_PACKAGE
        ,GENERATED_JSON_REPORT_FILE = "target/classes/static/public/ZupportL5-api-swagger-doc.json"
        ;

        public static Map<String, Object> getSwaggerInfo() {
            if (swaggerInfo == null) {
                //-- Thread-safe lazy initialization
                synchronized (OpenApiJsonDocGeneratorConfig.class) {
                    if (swaggerInfo == null) {
                        swaggerInfo = new LinkedHashMap<>();

                        String isoInstant = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
                            .withZone(ZoneId.of("UTC")).format(Instant.now());
                            
                        String origin = String.format(
                            "This documentation was generated at %s using ZupportL5 json-api-processor", isoInstant);
    
                        swaggerInfo.put("title", "ZupportL5 API Documentation");
                        swaggerInfo.put("description", "Comprehensive OpenAPI-compliant JSON documentation for ZupportL5-API");
                        swaggerInfo.put("version", "1.0.0");
                        swaggerInfo.put("contact", Map.of(
                                "name", "Development",
                                "email", "arcbrth@gmail.com"));
                        swaggerInfo.put("license", Map.of(
                                "name", "Apache 2.0",
                                "url", "https://github.com/abritoh/zupportl5-api"));
                        swaggerInfo.put("origin", origin);
                    }
                }
            }
            return swaggerInfo;
        }
    }

    public class HtmlSwaggerDocGeneratorConfig {

        private HtmlSwaggerDocGeneratorConfig() {}

        public static final String 
        APPLICATION_BASE_PACKAGE = ProcessorSettings.APPLICATION_BASE_PACKAGE
        , SWAGGER_TEMPLATE_PATH = "target/classes/static/html/project-swagger-template.html"
        , GENERATED_HTML_REPORT_FILE = "target/classes/static/public/ZupportL5-api-swagger-doc.html"
        ;
    }

}
