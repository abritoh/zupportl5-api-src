
package org.apache.clusterbr.zupportl5.controller;

import org.apache.clusterbr.zupportl5.entity.ViewHandoverDetail;
import org.apache.clusterbr.zupportl5.service.ViewHandoverDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * <!-- comment-processor-start -->
 * <p>REST-API Endpoints --</p>
 * <ul>
 * <li>GET /api/view-handover-details/{id}: Returns data from the API</li>
 * </ul>
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ViewHandoverDetailController_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ViewHandoverDetailController_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ViewHandoverDetailController_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ViewHandoverDetailController_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@RestController
@RequestMapping("/api/view-handover-details")
public class ViewHandoverDetailController {

    private final ViewHandoverDetailService service;

    @Autowired
    public ViewHandoverDetailController(ViewHandoverDetailService service) {
        this.service = service;
    }


    @GetMapping
    public ResponseEntity<List<ViewHandoverDetail>> getAll() {
        return ResponseEntity.ok(service.findAll());
    }    

    @GetMapping("/{id}")
    public ResponseEntity<ViewHandoverDetail> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }    
}
