package org.apache.clusterbr.zupportl5.processor;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
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
 * <p><img src="{@docRoot}/generated-resources/uml/images/OpenApiDocGeneratorBase_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/OpenApiDocGeneratorBase_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Component
public class OpenApiDocGeneratorBase {

    protected static final Logger logger = LoggerFactory.getLogger(OpenApiDocGeneratorBase.class);

    protected List<Class<?>> getClassesInPackage(String packageName) throws IOException, ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        String path = packageName.replace('.', '/');
        File directory = new File(getClass().getClassLoader().getResource(path).getFile());

        if (directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isDirectory()) {
                    classes.addAll(getClassesInPackage(packageName + "." + file.getName()));
                } else if (file.getName().endsWith(".class")) {
                    String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                    classes.add(Class.forName(className));
                }
            }
        }
        return classes;
    }

    protected boolean isHttpMethodAnnotatedWithHttpVerb(Method method) {
        return method.isAnnotationPresent(GetMapping.class) ||
               method.isAnnotationPresent(PostMapping.class) ||
               method.isAnnotationPresent(PutMapping.class) ||
               method.isAnnotationPresent(DeleteMapping.class) ||
               method.isAnnotationPresent(PatchMapping.class);
    }

    protected String getHttpVerbFromMethod(Method method) {
        if (method.isAnnotationPresent(GetMapping.class)) {
            return "GET";
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            return "POST";
        } else if (method.isAnnotationPresent(PutMapping.class)) {
            return "PUT";
        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
            return "DELETE";
        } else if (method.isAnnotationPresent(PatchMapping.class)) {
            return "PATCH";
        } else if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            if (requestMapping.method().length > 0) {
                switch (requestMapping.method()[0]) {
                    case GET: return "GET";
                    case POST: return "POST";
                    case PUT: return "PUT";
                    case DELETE: return "DELETE";
                    case PATCH: return "PATCH";
                    case OPTIONS: return "OPTIONS";
                    case HEAD: return "HEAD";
                    case TRACE: return "TRACE";
                    default: return requestMapping.method()[0].name();
                }
            }
        }
        return "UNKNOWN";
    }    

    protected String getPathFromMethod(Method method) {
        if (method.isAnnotationPresent(GetMapping.class)) {
            String[] values = method.getAnnotation(GetMapping.class).value();
            return values.length > 0 ?  String.join(",", values) : "/";
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            String[] values = method.getAnnotation(PostMapping.class).value();
            return values.length > 0 ? String.join(",", values)  : "/";
        } else if (method.isAnnotationPresent(PutMapping.class)) {
            String[] values = method.getAnnotation(PutMapping.class).value();
            return values.length > 0 ? String.join(",", values)  : "/";
        } else if (method.isAnnotationPresent(DeleteMapping.class)) {
            String[] values = method.getAnnotation(DeleteMapping.class).value();
            return values.length > 0 ? String.join(",", values)  : "/";
        } else if (method.isAnnotationPresent(PatchMapping.class)) {
            String[] values = method.getAnnotation(PatchMapping.class).value();
            return values.length > 0 ? String.join(",", values)  : "/";
        } else if (method.isAnnotationPresent(RequestMapping.class)) {
            String[] values = method.getAnnotation(RequestMapping.class).value();
            return values.length > 0 ? String.join(",", values) : "/";
        }
        return "";
    }

    protected String loadTemplate(String templatePath) throws IOException {
        StringBuilder templateContent = new StringBuilder();
        File file = new File(templatePath);
        try (FileReader reader = new FileReader(file)) {
            int character;
            while ((character = reader.read()) != -1) {
                templateContent.append((char) character);
            }
        } catch(Exception ex) {
            ex.printStackTrace();
            logger.error("[Exception] (OpenApiDocGeneratorBase: loadTemplate())", ex);
        }
        return templateContent.toString();
    }

    protected String removeTrailingSlash(String endpoint) {
        if (endpoint != null && endpoint.length() > 1 && endpoint.endsWith("/")) {
            return endpoint.substring(0, endpoint.length() - 1);
        }
        return endpoint;
    }      
}
