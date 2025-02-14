package org.apache.clusterbr.zupportl5.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import org.springframework.boot.SpringBootVersion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ApplicationInfoService_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ApplicationInfoService_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ApplicationInfoService_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ApplicationInfoService_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Component
public class ApplicationInfoService {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationInfoService.class);

    @Value("#{systemProperties['java.version']}")
    private String javaVersion;

    private String springBootVersion;

    @Value("${property.application.title:UNKNOWN}")
    private String applicationTitle;

    @Value("${property.application.version:UNKNOWN}")
    private String applicationVersion;

    public ApplicationInfoService() {
        this.springBootVersion = SpringBootVersion.getVersion();
    }

    @PostConstruct
    private void Init() {
        String fmtMsg = "(ApplicationInfoService) applicationTitle=%s | applicationVersion=%s";
        logger.info(String.format(fmtMsg, applicationTitle, applicationVersion));
    }

    public String replacePlaceholders(String input) {
        return input
            .replace("${java.version}", this.getJavaVersion())
            .replace("${spring-boot.version}", this.getSpringBootVersion())
            .replace("${spring-boot.formatted-version}", getFormattedVersion(this.getSpringBootVersion()))
            .replace("${application.title}", this.getApplicationTitle())
            .replace("${application.version}", this.getApplicationVersion())
            .replace("${application.formatted-version}", getFormattedVersion(this.getApplicationVersion()))
            ;
    }

    private String getFormattedVersion(String input) {
        return String.format("(v%s)", input);
    }

    // -- getters

    public String getJavaVersion() {
        return javaVersion;
    }

    public String getSpringBootVersion() {
        return springBootVersion;
    }    

    public String getApplicationTitle() {
        return applicationTitle;
    }

    public String getApplicationVersion() {
        return applicationVersion;
    }

}
