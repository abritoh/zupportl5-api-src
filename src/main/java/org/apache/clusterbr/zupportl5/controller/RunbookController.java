package org.apache.clusterbr.zupportl5.controller;

import java.util.List;
import java.util.Optional;

import org.apache.clusterbr.zupportl5.entity.Runbook;
import org.apache.clusterbr.zupportl5.service.RunbookService;
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
 * <li>GET /api/runbooks/{id}: Returns data from the API</li>
 * <li>PUT /api/runbooks/{id}: Updates an entry by ID</li>
 * <li>DELETE /api/runbooks/{id}: Deletes an entry by ID</li>
 * </ul>
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/RunbookController_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/RunbookController_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/RunbookController_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/RunbookController_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@RestController
@RequestMapping("/api/runbooks")
public class RunbookController {

    private final RunbookService service;

    @Autowired
    public RunbookController(RunbookService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Runbook>> getAll() {
        List<Runbook> result = service.getAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Runbook> getById(@PathVariable Long id) {
        Optional<Runbook> result = service.getById(id);
        return result.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Runbook> create(@RequestBody Runbook item) {
        Runbook created = service.create(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Runbook> update(@PathVariable Long id, @RequestBody Runbook item) {
        Runbook updated = service.update(id, item);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        boolean deleted = service.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
