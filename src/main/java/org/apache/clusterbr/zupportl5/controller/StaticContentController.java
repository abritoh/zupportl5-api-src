package org.apache.clusterbr.zupportl5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <!-- comment-processor-start -->
 * <p>REST-API Endpoints --</p>
 * <ul>
 * <li>GET /: Returns index</li>
 * </ul>
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/StaticContentController_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/StaticContentController_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/StaticContentController_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/StaticContentController_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Controller
@RequestMapping("/")
public class StaticContentController {
    
    @RequestMapping("/")
    public String root() {
        return "index";
    }

    @RequestMapping("/about")
    public String about() {
        return "index";
    }
}

