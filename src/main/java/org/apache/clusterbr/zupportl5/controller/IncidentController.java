package org.apache.clusterbr.zupportl5.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.clusterbr.zupportl5.entity.Incident;
import org.apache.clusterbr.zupportl5.service.IncidentService;

import java.util.List;
import java.util.Optional;


/**
 * <!-- comment-processor-start -->
 * <p>REST-API Endpoints --</p>
 * <ul>
 * <li>GET /api/incidents/{id}: Returns data from the API</li>
 * <li>PUT /api/incidents/{id}: Updates an entry by ID</li>
 * <li>DELETE /api/incidents/{id}: Deletes an entry by ID</li>
 * </ul>
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/IncidentController_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/IncidentController_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/IncidentController_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/IncidentController_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@RestController
@RequestMapping("/api/incidents")
public class IncidentController {

    private final IncidentService service;

    @Autowired
    public IncidentController(IncidentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Incident> create(@RequestBody Incident item) {
        Incident entity = service.save(item);
        return new ResponseEntity<>(entity, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Incident>> getAll() {
        List<Incident> list = service.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Incident> getById(@PathVariable Integer id) {
        Optional<Incident> entity = service.getById( id );
        return entity.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                       .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Incident> update(@PathVariable Integer id, @RequestBody Incident item) {
        Optional<Incident> updated = service.update(id, item);
        return updated.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                              .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boolean result = service.deleteById( id );
        return result ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
