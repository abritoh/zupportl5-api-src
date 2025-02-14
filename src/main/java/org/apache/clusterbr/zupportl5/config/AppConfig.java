package org.apache.clusterbr.zupportl5.config;

import org.apache.clusterbr.zupportl5.utils.ValidatorXmlXsd;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/AppConfig_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/AppConfig_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Configuration
public class AppConfig {

    @Value("${property.application.xsd.schema.filepath:UNKNOWN}")
    private String xsdSchemaFilepath;
    
    @Bean
    public ValidatorXmlXsd xmlValidator() {
        return new ValidatorXmlXsd(xsdSchemaFilepath);
    }
    
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new FormHttpMessageConverter());
        return restTemplate;
    }    
}
