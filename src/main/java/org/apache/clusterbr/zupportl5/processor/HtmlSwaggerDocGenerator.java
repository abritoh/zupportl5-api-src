package org.apache.clusterbr.zupportl5.processor;

import java.io.FileWriter;
import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/HtmlSwaggerDocGenerator_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/HtmlSwaggerDocGenerator_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/HtmlSwaggerDocGenerator_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Component
public class HtmlSwaggerDocGenerator extends OpenApiDocGeneratorBase {

    private static final Logger logger = LoggerFactory.getLogger(HtmlSwaggerDocGenerator.class);

    public void generateSwaggerDocs() {
        StringBuilder methodsSection = new StringBuilder();

        try {
            List<Class<?>> controllerClasses 
                = getClassesInPackage(ProcessorSettings.HtmlSwaggerDocGeneratorConfig.APPLICATION_BASE_PACKAGE);

            int methodNumber = 0;
            for (Class<?> controllerClass : controllerClasses) {
                
                String rootPath = "/", controllerName = controllerClass.getSimpleName();

                if (controllerClass.isAnnotationPresent(RestController.class)) {

                    if(controllerClass.isAnnotationPresent(RequestMapping.class)) {
                        String[] values = controllerClass.getAnnotation(RequestMapping.class).value();
                        rootPath = (values.length > 0) ? String.join(",", values) : "/";                        
                    }

                    Method[] methods = controllerClass.getMethods();
                    for (Method method : methods) {

                        if (method.isAnnotationPresent(RequestMapping.class) 
                        || isHttpMethodAnnotatedWithHttpVerb(method)) {

                            String 
                                httpMethod = getHttpVerbFromMethod(method)
                                ,endpoint = rootPath + getPathFromMethod(method)
                                ;
                            methodNumber++;
                            endpoint = removeTrailingSlash(endpoint);
                            methodsSection.append(generateMethodHtml(method, httpMethod, endpoint, controllerName, methodNumber));
                        }
                    }
                }
            }

            //-- load template and replace dynamic content
            String templateContent = loadTemplate(ProcessorSettings.HtmlSwaggerDocGeneratorConfig.SWAGGER_TEMPLATE_PATH);
            String finalHtml = templateContent.replace("{{METHODS_SECTION}}", methodsSection.toString());
            
            //-- Write to file
            try (FileWriter writer = new FileWriter(ProcessorSettings.HtmlSwaggerDocGeneratorConfig.GENERATED_HTML_REPORT_FILE)) {
                writer.write(finalHtml);
                String msg = "(SwaggerDocumentGenerator) Swagger API documentation generated at " 
                    + ProcessorSettings.HtmlSwaggerDocGeneratorConfig.GENERATED_HTML_REPORT_FILE;
                System.out.println(msg);
                logger.info(msg);
            } catch (Exception ex) {
                ex.printStackTrace();
                logger.error("[Exception] (SwaggerDocumentGenerator:FileWriter(GENERATED_HTML_REPORT_FILE))", ex);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("[Exception] (SwaggerDocumentGenerator:generateSwaggerDocs)", ex);
        }
    }

    private String generateMethodHtml (
        Method method, String httpMethod, String endPoint, String controllerName, int methodNumber) {
        
        String html = """
        <div class='method'><a name='{{methodName}}' />
            <div class='method-path'>
                <pre class='{{httpMethodName}}'><code class='huge'><span class='http-method'>{{methodNumber}}  {{httpMethodName}}</span> {{endPoint}}</code></pre>
            </div>
            <div class='method-summary'>{{controllerName}} (<span class='nickname'>{{methodName}}</span>)</div>
            <div class='method-notes'></div>
            <h3 class='field-label'>Responses</h3>
            <h4 class='field-label'>200</h4>
            <p>Successful operation</p>
        </div>
        <hr />
        """;
        
        html = html.replace("{{httpMethodName}}", httpMethod.toLowerCase());
        html = html.replace("{{methodNumber}}", String.valueOf(methodNumber) );
        html = html.replace("{{controllerName}}", controllerName);
        html = html.replace("{{methodName}}", method.getName());
        html = html.replace("{{endPoint}}", endPoint);

        return html;
    }
}
