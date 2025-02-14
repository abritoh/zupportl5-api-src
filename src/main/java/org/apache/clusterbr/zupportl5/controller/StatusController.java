package org.apache.clusterbr.zupportl5.controller;

import java.util.List;

import org.apache.clusterbr.zupportl5.entity.Status;
import org.apache.clusterbr.zupportl5.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <!-- comment-processor-start -->
 * <p>REST-API Endpoints --</p>
 * <ul>
 * <li>GET /api/status/{id}: Returns data from the API</li>
 * <li>PUT /api/status/{id}: Updates an entry by ID</li>
 * <li>DELETE /api/status/{id}: Deletes an entry by ID</li>
 * </ul>
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/StatusController_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/StatusController_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/StatusController_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/StatusController_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@RestController
@RequestMapping("/api/status")
public class StatusController {

    private final StatusService service;

    @Autowired
    public StatusController(StatusService service) {
        this.service = service;
    }

    @GetMapping
    public List<Status> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Status> getById(@PathVariable Integer id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Status> create(@RequestBody Status runbook) {
        Status entity = service.create(runbook);
        return new ResponseEntity<>(entity, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Status> update(@PathVariable Integer id, @RequestBody Status item) {

        try {
            Status entity = service.update(id, item);
            return ResponseEntity.ok(entity);
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
