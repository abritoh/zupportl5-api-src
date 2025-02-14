package org.apache.clusterbr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/Zupportl5Application_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/Zupportl5Application_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/Zupportl5Application_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@SpringBootApplication(scanBasePackages = "org.apache.clusterbr.zupportl5")
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@EntityScan(basePackages = "org.apache.clusterbr.zupportl5.entity")
@EnableJpaRepositories(basePackages = "org.apache.clusterbr.zupportl5.repository")
@PropertySource("classpath:application.properties")
@PropertySource("classpath:application-dev.properties")
@PropertySource("classpath:application-devgcp.properties")
@PropertySource("classpath:application-prod.properties")
public class Zupportl5Application  {

    private static final Logger logger = LoggerFactory.getLogger(Zupportl5Application.class);

    public static void main(String[] args) {
        System.out.println("::::::::::::::::::::ZupportL5 started::::::::::::::::::::");        

        //-->> ApplicationContext context = SpringApplication.run(Zupportl5Application.class, args);
        
        SpringApplication.run(Zupportl5Application.class, args);

        System.out.println("::::::::::::::::::::::::::::::::::::::::::::::::::::::::");
    }
}
