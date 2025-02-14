package org.apache.clusterbr.zupportl5.processor;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/OpenApiJsonDocGenerator_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
public class OpenApiJsonDocGenerator extends OpenApiDocGeneratorBase {

    private static final Logger logger = LoggerFactory.getLogger(OpenApiJsonDocGenerator.class);

    public void generateOpenAPIJsonDocs() {
        try {
            Set<Class<?>> controllerClasses = scanForControllerClasses(
                ProcessorSettings.OpenApiJsonDocGeneratorConfig.APPLICATION_BASE_PACKAGE);

            Map<String, Object> swaggerDoc = new LinkedHashMap<>();
            swaggerDoc.put("openapi", "3.0.0");
            swaggerDoc.put("info", ProcessorSettings.OpenApiJsonDocGeneratorConfig.getSwaggerInfo());
            swaggerDoc.put("paths", extractSwaggerPaths(controllerClasses));

            try (FileWriter writer = new FileWriter(ProcessorSettings.OpenApiJsonDocGeneratorConfig.GENERATED_JSON_REPORT_FILE)) {
                String jsonDoc = convertToJson(swaggerDoc);
                writer.write(jsonDoc);
                String msg = "Swagger-style documentation generated at " + ProcessorSettings.OpenApiJsonDocGeneratorConfig.GENERATED_JSON_REPORT_FILE;
                System.out.println(msg);
                logger.info(msg);
            }
        } catch (Exception ex) {
            logger.error("[Exception] (JsonSwaggerDocGenerator:generateSwaggerDocs)", ex);
        }
    }

    private Set<Class<?>> scanForControllerClasses(String basePackage) {
        Set<Class<?>> controllerClasses = new HashSet<>();
        String packagePath = basePackage.replace('.', '/');
        URL packageURL = getClass().getClassLoader().getResource(packagePath);

        if (packageURL != null) {
            try {
                File directory = new File(packageURL.toURI());
                if (directory.exists() && directory.isDirectory()) {
                    for (File file : Objects.requireNonNull(directory.listFiles())) {
                        if (file.isDirectory()) {
                            controllerClasses.addAll(scanForControllerClasses(basePackage + "." + file.getName()));
                        } else if (file.getName().endsWith(".class")) {
                            String className = basePackage + "." + file.getName().replace(".class", "");
                            try {
                                Class<?> clazz = Class.forName(className);
                                if (clazz.isAnnotationPresent(RestController.class)
                                        || clazz.isAnnotationPresent(RequestMapping.class)) {
                                    controllerClasses.add(clazz);
                                }
                            } catch (ClassNotFoundException e) {
                                logger.error("[Error] (scanForControllerClasses) Error loading class", e);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("[Error] (scanForControllerClasses) Error scanning package", e);
            }
        }
        return controllerClasses;
    }

    private Map<String, Object> extractSwaggerPaths(Set<Class<?>> controllerClasses) {
        Map<String, Object> paths = new LinkedHashMap<>();

        for (Class<?> controllerClass : controllerClasses) {
            String basePath = getClassLevelPath(controllerClass);

            for (Method method : controllerClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(RequestMapping.class) 
                    || isHttpMethodAnnotatedWithHttpVerb(method)) {

                    String methodPath = getPathFromMethod(method);
                    String fullPath = combinePaths(basePath, methodPath);
                    String httpMethod = getHttpVerbFromMethod(method).toLowerCase();

                    if (!fullPath.isEmpty() && httpMethod != null) {
                        paths.computeIfAbsent(fullPath, k -> new LinkedHashMap<>());
                        @SuppressWarnings("unchecked")
                        Map<String, Object> methodsMap = (Map<String, Object>) paths.get(fullPath);
                        methodsMap.put(httpMethod, createMethodDetails(method));
                    }
                }
            }
        }
        return paths;
    }

    private String getClassLevelPath(Class<?> controllerClass) {
        if (controllerClass.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping classMapping = controllerClass.getAnnotation(RequestMapping.class);
            String[] values = classMapping.value();
            return values.length > 0 ? values[0] : "";
        }
        return "";
    }

    private String combinePaths(String basePath, String methodPath) {
        if (basePath == null || basePath.isEmpty()) {
            return methodPath;
        }
        if (methodPath == null || methodPath.isEmpty()) {
            return basePath;
        }
        return removeTrailingSlash(basePath) + (methodPath.startsWith("/") ? methodPath : "/" + methodPath);
    }

    private Map<String, Object> createMethodDetails(Method method) {
        Map<String, Object> methodDetails = new LinkedHashMap<>();
        methodDetails.put("summary", method.getName());
        methodDetails.put("operationId", method.getName());
        methodDetails.put("parameters", extractParameters(method));
        methodDetails.put("responses", createResponseSchemas(method));
        return methodDetails;
    }

    private List<Map<String, Object>> extractParameters(Method method) {
        List<Map<String, Object>> parameters = new ArrayList<>();
        for (Parameter param : method.getParameters()) {
            Map<String, Object> paramDetails = new LinkedHashMap<>();
            if (param.isAnnotationPresent(PathVariable.class)) {
                paramDetails.put("name", param.getAnnotation(PathVariable.class).value());
                paramDetails.put("in", "path");
            } else if (param.isAnnotationPresent(RequestParam.class)) {
                paramDetails.put("name", param.getAnnotation(RequestParam.class).value());
                paramDetails.put("in", "query");
            } else if (param.isAnnotationPresent(RequestBody.class)) {
                paramDetails.put("name", param.getName());
                paramDetails.put("in", "body");
            }
            paramDetails.put("required",
                    param.isAnnotationPresent(PathVariable.class) 
                    || param.isAnnotationPresent(RequestBody.class));

            paramDetails.put("schema", Map.of("type", param.getType().getSimpleName().toLowerCase()));
            parameters.add(paramDetails);
        }
        return parameters;
    }

    private Map<String, Object> createResponseSchemas(Method method) {
        Map<String, Object> responses = new LinkedHashMap<>();
        Map<String, Object> successResponse = new LinkedHashMap<>();
        successResponse.put("description", "Successful operation");
        responses.put("200", successResponse);
        return responses;
    }

    private String convertToJson(Object obj) {
        return new com.google.gson.GsonBuilder().setPrettyPrinting().create().toJson(obj);
    }
}
