package org.apache.clusterbr.zupportl5.service;

import java.util.List;
import java.util.Optional;

import org.apache.clusterbr.zupportl5.entity.Handover;
import org.apache.clusterbr.zupportl5.repository.HandoverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/HandoverService_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/HandoverService_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/HandoverService_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/HandoverService_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Service
public class HandoverService {

    private final HandoverRepository repository;

    @Autowired
    public HandoverService(HandoverRepository repository) {
        this.repository = repository;
    }

    public List<Handover> findAll() {
        return repository.findAll();
    }

    public Optional<Handover> findById(Integer id) {
        return repository.findById(Long.valueOf(id));
    }

    public Handover save(Handover entity) {
        return repository.save(entity);
    }

    public Optional<Handover> update(Integer id, Handover entity) {
        return repository.findById(Long.valueOf(id)).map(existing -> {
            existing.setIncidentID(entity.getIncidentID());
            existing.setFromEngineerID(entity.getFromEngineerID());
            existing.setToEngineerID(entity.getToEngineerID());
            existing.setHandoverTime(entity.getHandoverTime());
            existing.setNotes(entity.getNotes());
            return repository.save(existing);
        });
    }

    public boolean deleteById(Integer id) {
        if (repository.existsById(Long.valueOf(id))) {
            repository.deleteById(Long.valueOf(id));
            return true;
        }
        return false;
    }
}
