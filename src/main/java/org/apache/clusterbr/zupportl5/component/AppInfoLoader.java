package org.apache.clusterbr.zupportl5.component;

import org.apache.clusterbr.zupportl5.dto.AppInfoHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/AppInfoLoader_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/AppInfoLoader_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/AppInfoLoader_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/AppInfoLoader_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Component
public class AppInfoLoader {

    private static final Logger logger = LoggerFactory.getLogger(AppInfoLoader.class);

    @Autowired
    public AppInfoLoader(Environment environment) {
        loadServerInfo(environment);
    }

    public void loadServerInfo(Environment environment) {

        AppInfoHolder instance = AppInfoHolder.getInstance();

        instance.setProtocol(environment.getProperty("server.scheme", "http"));
        instance.setHostName(environment.getProperty("server.host", "localhost"));
        instance.setServerPort(Integer.parseInt(environment.getProperty("server.port", "8080")));

        logger.info("(AppInfoLoader::loadServerInfo) AppInfoHolder loaded: ".concat(instance.toString() ));
    }
}
