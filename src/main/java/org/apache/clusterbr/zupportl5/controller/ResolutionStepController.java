package org.apache.clusterbr.zupportl5.controller;

import org.apache.clusterbr.zupportl5.entity.ResolutionStep;
import org.apache.clusterbr.zupportl5.service.ResolutionStepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <!-- comment-processor-start -->
 * <p>REST-API Endpoints --</p>
 * <ul>
 * <li>GET /api/resolution-steps/{id}: Returns data from the API</li>
 * <li>PUT /api/resolution-steps/{id}: Updates an entry by ID</li>
 * <li>DELETE /api/resolution-steps/{id}: Deletes an entry by ID</li>
 * </ul>
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ResolutionStepController_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ResolutionStepController_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ResolutionStepController_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ResolutionStepController_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@RestController
@RequestMapping("/api/resolution-steps")
public class ResolutionStepController {

    private final ResolutionStepService service;

    @Autowired
    public ResolutionStepController(ResolutionStepService service) {
        this.service = service;
    }

    @GetMapping
    public List<ResolutionStep> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResolutionStep> getById(@PathVariable Integer id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ResolutionStep> save(@RequestBody ResolutionStep item) {
        ResolutionStep entity = service.create(item);
        return new ResponseEntity<>(entity, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResolutionStep> update(@PathVariable Integer id, @RequestBody ResolutionStep item) {

        try {
            ResolutionStep entity = service.update(id, item);
            return ResponseEntity.ok(entity);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Integer id) {
        boolean result = service.deleteById(id);
        return result ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
