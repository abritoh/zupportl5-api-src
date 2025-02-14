package org.apache.clusterbr.zupportl5.controller;

import org.apache.clusterbr.zupportl5.entity.Priority;
import org.apache.clusterbr.zupportl5.service.PriorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <!-- comment-processor-start -->
 * <p>REST-API Endpoints --</p>
 * <ul>
 * <li>GET /api/priorities/{id}: Returns data from the API</li>
 * <li>PUT /api/priorities/{id}: Updates an entry by ID</li>
 * <li>DELETE /api/priorities/{id}: Deletes an entry by ID</li>
 * </ul>
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/PriorityController_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/PriorityController_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/PriorityController_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/PriorityController_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@RestController
@RequestMapping("/api/priorities")
public class PriorityController {

    private final PriorityService service;

    @Autowired
    public PriorityController(PriorityService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Priority>> getAll() {
        List<Priority> list = service.getAll();
        return ResponseEntity.ok(list); 
    }

    @GetMapping("/{id}")
    public ResponseEntity<Priority> getById(@PathVariable Integer id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Priority> create(@RequestBody Priority item) {
        Priority createdPriority = service.create(item);
        return new ResponseEntity<>(createdPriority, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Priority> update(@PathVariable Integer id, @RequestBody Priority item) {
        try {
            Priority updated = service.update(id, item);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boolean result = service.deleteById(id);
        return result ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
