package org.apache.clusterbr.zupportl5.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.clusterbr.zupportl5.annotations.ExecuteJavadocProcessing;
import org.apache.clusterbr.zupportl5.entity.Engineer;
import org.apache.clusterbr.zupportl5.service.EngineerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
 * <li>GET /api/engineers/id/{id:d+}: Returns data from the API</li>
 * <li>PUT /api/engineers/{id}: Updates an entry by ID</li>
 * <li>DELETE /api/engineers/{id}: Deletes an entry by ID</li>
 * <li>GET /api/engineers/email/{email:.+@.+..+}: Returns data from the API</li>
 * <li>PATCH /api/engineers/{id}: Partially updates an entry by ID</li>
 * </ul>
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/EngineerController_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/EngineerController_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/EngineerController_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/EngineerController_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@RestController
@ExecuteJavadocProcessing
@RequestMapping("/api/engineers")
public class EngineerController {

    private final EngineerService service;

    @Autowired
    public EngineerController(EngineerService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Engineer> create(@RequestBody Engineer item) {

        Engineer created = service.save(item);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Engineer>> getAll() {

        List<Engineer> list = service.findAll();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/id/{id:\\d+}")
    public ResponseEntity<Engineer> getById(@PathVariable Long id) {
        
        Optional<Engineer> entity = service.getById(id);
        return entity.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/email/{email:.+@.+\\..+}")
    public ResponseEntity<Engineer> getByEmail(@PathVariable String email) {
        
        Optional<Engineer> entity = service.getByEmail(email);
        return entity.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }    

    @PutMapping("/{id}")
    public ResponseEntity<Engineer> update(@PathVariable Long id, @RequestBody Engineer item) {

        Optional<Engineer> entity = service.update(id, item);
        return entity.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Engineer> partialUpdate(@PathVariable Long id, @RequestBody Map<String, Object> updates) {

        Optional<Engineer> entity = service.partialUpdate(id, updates);
        return entity.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        boolean result = service.deleteById(id);
        return result ? new ResponseEntity<>(HttpStatus.NO_CONTENT) : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
