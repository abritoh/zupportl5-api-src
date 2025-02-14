package org.apache.clusterbr.zupportl5.controller;

import org.apache.clusterbr.zupportl5.entity.Incidentlogs;
import org.apache.clusterbr.zupportl5.service.IncidentlogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


/**
 * <!-- comment-processor-start -->
 * <p>REST-API Endpoints --</p>
 * <ul>
 * <li>GET /api/incidentlogs/{id}: Returns data from the API</li>
 * <li>PUT /api/incidentlogs/{id}: Updates an entry by ID</li>
 * <li>DELETE /api/incidentlogs/{id}: Deletes an entry by ID</li>
 * </ul>
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/IncidentlogsController_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/IncidentlogsController_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/IncidentlogsController_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/IncidentlogsController_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@RestController
@RequestMapping("/api/incidentlogs")
public class IncidentlogsController {

    private final IncidentlogsService service;

    @Autowired
    public IncidentlogsController(IncidentlogsService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Incidentlogs>> getAll() {
        List<Incidentlogs> list = service.getAll();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Incidentlogs> getById(@PathVariable Integer id) {
        Optional<Incidentlogs> entity = service.getById(id);
        return entity.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Incidentlogs> create(@RequestBody Incidentlogs item) {
        Incidentlogs entity = service.save(item);
        return ResponseEntity.status(HttpStatus.CREATED).body(entity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Incidentlogs> update(@PathVariable Integer id,
            @RequestBody Incidentlogs incidentLog) {
        try {
            Incidentlogs entity = service.update(id, incidentLog);
            return ResponseEntity.ok(entity);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boolean result = service.deleteById(id);
        return result ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
