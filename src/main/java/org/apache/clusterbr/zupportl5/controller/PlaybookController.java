package org.apache.clusterbr.zupportl5.controller;

import java.util.List;
import java.util.Optional;

import org.apache.clusterbr.zupportl5.entity.Playbook;
import org.apache.clusterbr.zupportl5.service.PlaybookService;
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
 * <li>GET /api/playbooks/{id}: Returns data from the API</li>
 * <li>PUT /api/playbooks/{id}: Updates an entry by ID</li>
 * <li>DELETE /api/playbooks/{id}: Deletes an entry by ID</li>
 * </ul>
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/PlaybookController_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/PlaybookController_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/PlaybookController_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/PlaybookController_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@RestController
@RequestMapping("/api/playbooks")
public class PlaybookController {

    private final PlaybookService service;

    @Autowired
    public PlaybookController(PlaybookService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Playbook>> getAll() {
        List<Playbook> result = service.getAll();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Playbook> getById(@PathVariable Long id) {
        Optional<Playbook> result = service.getById(id);
        return result.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Playbook> create(@RequestBody Playbook item) {
        Playbook created = service.create(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Playbook> update(@PathVariable Long id, @RequestBody Playbook item) {
        Playbook updated = service.update(id, item);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        boolean deleted = service.deleteById(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}
