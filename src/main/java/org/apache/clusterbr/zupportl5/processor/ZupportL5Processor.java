package org.apache.clusterbr.zupportl5.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ZupportL5Processor_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
public class ZupportL5Processor {

    private static final Logger logger = LoggerFactory.getLogger(CodeMetricsGenerator.class);

    public static void main(String[] args) {
        logger.info("(ZupportL5Processor) initializing ...");
        
        CodeMetricsGenerator codeMetricsGenerator = new CodeMetricsGenerator();
        codeMetricsGenerator.generateMetrics();

        HtmlSwaggerDocGenerator swaggerDocumentGenerator = new HtmlSwaggerDocGenerator();
        swaggerDocumentGenerator.generateSwaggerDocs();

        OpenApiJsonDocGenerator jsonSwaggerDocGenerator = new OpenApiJsonDocGenerator();
        jsonSwaggerDocGenerator.generateOpenAPIJsonDocs();        
        
        logger.info("(ZupportL5Processor) process completed.");
    }
    
}
