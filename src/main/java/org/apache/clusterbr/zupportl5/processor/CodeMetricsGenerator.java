package org.apache.clusterbr.zupportl5.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.clusterbr.zupportl5.annotations.SkipJavadocProcessing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jdepend.framework.JDepend;
import jdepend.framework.JavaPackage;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/CodeMetricsGenerator_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
public class CodeMetricsGenerator {

    private static final Logger logger = LoggerFactory.getLogger(CodeMetricsGenerator.class);

    public void generateMetrics() {

        try {
            
            JDepend jdepend = new JDepend();
            jdepend.addDirectory(ProcessorSettings.CodeMetricsGeneratorConfig.CLASSES_TARGET_PATH);
            StringBuilder html = new StringBuilder();

            int packageNumber = 0, classesInPackage = 0, abstractClassesInPackage = 0,
                totalClasses = 0, totalAbstractClasses = 0, totalMethods = 0, totalLines = 0, totalPackages = 0
                ;

            html.append("<table><thead><tr>")
                .append("<th>#</th> <th>Package</th> <th>Classes</th> <th>Abstract Classes</th> <th>Afferent Couplings (Ca)</th>")
                .append("<th>Efferent Couplings (Ce)</th> <th>Instability (I)</th> <th>Cycles</th>")
                .append("</tr></thead><tbody>");

            Collection<?> packages = jdepend.analyze();
            for (Object obj : packages) {
                
                JavaPackage javaPackage = (JavaPackage) obj;

                if (!javaPackage.getName().startsWith(ProcessorSettings.CodeMetricsGeneratorConfig.APPLICATION_BASE_PACKAGE)) continue;

                packageNumber++;
                totalPackages++;
                classesInPackage = javaPackage.getClassCount();
                abstractClassesInPackage = javaPackage.getAbstractClassCount();

                totalClasses += classesInPackage;
                totalAbstractClasses += abstractClassesInPackage;

                for (Object clazzObj : javaPackage.getClasses()) {
                    jdepend.framework.JavaClass javaClass = (jdepend.framework.JavaClass) clazzObj;

                    String sourceFile = javaClass.getSourceFile()
                        , packagePath = javaClass.getPackageName().replace('.', File.separatorChar)
                        , sourceFilePath = ProcessorSettings.CodeMetricsGeneratorConfig.PROJECT_SOURCE_CODE_PATH 
                            + File.separator + packagePath + File.separator + sourceFile
                        ;                    
                    
                    logger.info("(CodeMetricsGenerator) Source file: " + sourceFilePath);                    
                    try {
                        File sourceFileObject = new File(sourceFilePath);
                        if (sourceFileObject.exists()) {
                            totalLines += countLinesOfCode(sourceFileObject);
                            totalMethods += countMethodsInClass(sourceFileObject);
                        }
                    } catch(Exception ex1) {
                        ex1.printStackTrace();
                        logger.error("[Exception] (CodeMetricsGenerator: new File(sourceFilePath))", ex1);                        
                    }                    
                }

                String cycle = (javaPackage.containsCycle()) ? "Cycle detected" : "No cycle";
                html.append("<tr>");
                html.append("<td>").append(packageNumber).append("</td>");
                html.append("<td>").append(javaPackage.getName()).append("</td>");
                html.append("<td>").append(classesInPackage).append("</td>");
                html.append("<td>").append(abstractClassesInPackage).append("</td>");
                html.append("<td>").append(javaPackage.afferentCoupling()).append("</td>");
                html.append("<td>").append(javaPackage.efferentCoupling()).append("</td>");
                html.append("<td>").append(roundDef(javaPackage.instability())).append("</td>");
                html.append("<td>").append(cycle).append("</td>");
                html.append("</tr>");
            }

            html.append("</tbody></table>");

            String templateContent = loadTemplate(ProcessorSettings.CodeMetricsGeneratorConfig.METRICS_TEMPLATE_PATH);
            String finalHtml = templateContent.replace("{{METRICS_TABLE}}", html.toString());

            finalHtml = finalHtml.replace("{{totalPackages}}", String.valueOf(totalPackages));
            finalHtml = finalHtml.replace("{{totalClasses}}", String.valueOf(totalClasses));
            finalHtml = finalHtml.replace("{{totalAbstractClasses}}", String.valueOf(totalAbstractClasses));
            finalHtml = finalHtml.replace("{{totalMethods}}", String.valueOf(totalMethods));
            finalHtml = finalHtml.replace("{{totalLines}}", String.valueOf(totalLines));

            try (FileWriter writer = new FileWriter(ProcessorSettings.CodeMetricsGeneratorConfig.GENERATED_HTML_REPORT_FILE)) {
                writer.write(finalHtml);
                String msg = "Code Metrics Report generated at " 
                    + ProcessorSettings.CodeMetricsGeneratorConfig.GENERATED_HTML_REPORT_FILE;
                System.out.println(msg);
                logger.info(msg);    
            }

        } catch(Exception ex) {
            ex.printStackTrace();
            logger.error("[Exception] (CodeMetricsGenerator: execute())", ex);
        }

    }

    private BigDecimal roundDef(float number) {
        return roundDecimals(number, 2);
    }

    private BigDecimal roundDecimals(float number, int decimalNumbers) {
        return new BigDecimal(number).setScale(decimalNumbers, RoundingMode.HALF_UP);
    }

    private String loadTemplate(String templatePath) throws IOException {
        StringBuilder templateContent = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(templatePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                templateContent.append(line).append("\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("[Exception] (CodeMetricsGenerator:loadTemplate)", ex);
        }
        return templateContent.toString();
    }

    private int countLinesOfCode(File file) {
        int lineCount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                //-- ignore empty lines and lines that are only comments
                if (!line.trim().isEmpty() && !line.trim().startsWith("//") && !line.trim().startsWith("/*")) {
                    lineCount++;
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            logger.error("[Exception] (CodeMetricsGenerator:countLinesOfCode)", ex);
        }
        return lineCount;
    }

    private int countMethodsInClass(File file) {
        int methodsCount = 0;
        String methodPattern = "\\b(public|protected|private|static|final|synchronized|abstract|native|transient|volatile|strictfp)\\s+[\\w\\<>\\[\\]]+\\s+\\w+\\s*\\([^\\)]*\\)\\s*\\{?";
        Pattern pattern = Pattern.compile(methodPattern);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (!line.trim().isEmpty() && !line.trim().startsWith("//") && !line.trim().startsWith("/*") && !line.trim().startsWith("*") && !line.trim().startsWith("*/")) {
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        methodsCount++;
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            logger.error("[Exception] (CodeMetricsGenerator:countMethodsInClass)", ex);
        }
        return methodsCount;
    } 
    
}
