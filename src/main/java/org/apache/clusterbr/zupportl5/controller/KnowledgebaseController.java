package org.apache.clusterbr.zupportl5.controller;

import org.apache.clusterbr.zupportl5.entity.Knowledgebase;
import org.apache.clusterbr.zupportl5.service.KnowledgebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * <!-- comment-processor-start -->
 * <p>REST-API Endpoints --</p>
 * <ul>
 * <li>GET /api/knowledgebase/{id}: Returns data from the API</li>
 * <li>PUT /api/knowledgebase/{id}: Updates an entry by ID</li>
 * <li>DELETE /api/knowledgebase/{id}: Deletes an entry by ID</li>
 * </ul>
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/KnowledgebaseController_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/KnowledgebaseController_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/KnowledgebaseController_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/KnowledgebaseController_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@RestController
@RequestMapping("/api/knowledgebase")
public class KnowledgebaseController {

    private final KnowledgebaseService service;

    @Autowired
    public KnowledgebaseController(KnowledgebaseService service) {
        this.service = service;
    }

    @GetMapping
    public List<Knowledgebase> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Knowledgebase> getById(@PathVariable Long id) {
        return service.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Knowledgebase> create(@RequestBody Knowledgebase item) {
        Knowledgebase entity = service.create(item);
        return new ResponseEntity<>(entity, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Knowledgebase> update(@PathVariable Long id, @RequestBody Knowledgebase item) {
        try {
            Knowledgebase entity = service.update(id, item);
            return ResponseEntity.ok(entity);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean result = service.deleteById(id);
        return result ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
