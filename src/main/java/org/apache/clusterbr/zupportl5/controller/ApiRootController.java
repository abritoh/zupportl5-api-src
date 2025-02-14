package org.apache.clusterbr.zupportl5.controller;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.clusterbr.zupportl5.service.ApplicationInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.PropertyPlaceholderHelper;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <!-- comment-processor-start -->
 * <p>REST-API Endpoints --</p>
 * <ul>
 * <li>aboutZupportL5Api - aboutZupportL5Api</li>
 * </ul>
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ApiRootController_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ApiRootController_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ApiRootController_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ApiRootController_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@RestController
@RequestMapping("/api")
public class ApiRootController {

    @Autowired
    private Environment env;
    private final ApplicationInfoService service;

    @Autowired
    public ApiRootController(ApplicationInfoService service) {
        this.service = service;
    }

    @GetMapping(produces = "text/plain")
    public String aboutZupportL5Api() throws IOException {
        final String APP_INFO_FILE = "banner.txt";
        ClassPathResource resource = new ClassPathResource(APP_INFO_FILE);
        String content = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);

        PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("${", "}");
        String result = helper.replacePlaceholders(content, env::getProperty);
        result = service.replacePlaceholders(result);
        
        return result;
    }
}
