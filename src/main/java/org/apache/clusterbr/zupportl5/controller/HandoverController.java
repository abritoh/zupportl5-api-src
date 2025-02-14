package org.apache.clusterbr.zupportl5.controller;

import org.apache.clusterbr.zupportl5.annotations.ExecuteJavadocProcessing;
import org.apache.clusterbr.zupportl5.entity.Handover;
import org.apache.clusterbr.zupportl5.service.HandoverService;
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
 * <li>GET /api/handover/{id}: Returns data from the API</li>
 * <li>PUT /api/handover/{id}: Updates an entry by ID</li>
 * <li>DELETE /api/handover/{id}: Deletes an entry by ID</li>
 * </ul>
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/HandoverController_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/HandoverController_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/HandoverController_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/HandoverController_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@ExecuteJavadocProcessing
@RestController
@RequestMapping("/api/handover")
public class HandoverController {

    private final HandoverService service;

    @Autowired
    public HandoverController(HandoverService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Handover> createHandover(@RequestBody Handover item) {
        Handover entity = service.save(item);
        return new ResponseEntity<>(entity, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Handover>> getAll() {
        List<Handover> list = service.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Handover> getById(@PathVariable Integer id) {        
        Optional<Handover> entity = service.findById(id);
        return entity.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Handover> update(@PathVariable Integer id, @RequestBody Handover item) {
        Optional<Handover> entity = service.update(id, item);
        return entity.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        boolean result = service.deleteById(id);
        return result ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
