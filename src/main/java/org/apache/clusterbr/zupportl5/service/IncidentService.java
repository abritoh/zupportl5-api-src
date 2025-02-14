package org.apache.clusterbr.zupportl5.service;

import java.util.List;
import java.util.Optional;

import org.apache.clusterbr.zupportl5.entity.Incident;
import org.apache.clusterbr.zupportl5.repository.IncidentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/IncidentService_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/IncidentService_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/IncidentService_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/IncidentService_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Service
public class IncidentService {

    private final IncidentRepository repository;

    @Autowired
    public IncidentService(IncidentRepository repository) {
        this.repository = repository;
    }

    public List<Incident> findAll() {
        return repository.findAll();
    }

    public Optional<Incident> getById(Integer id) {
        return repository.findById(Long.valueOf(id));
    }

    public Incident save(Incident entity) {
        return repository.save(entity);
    }

    public Optional<Incident> update(Integer id, Incident entity) {
        return repository.findById(Long.valueOf(id)).map(existing -> {
            existing.setTitle(entity.getTitle());
            existing.setDescription(entity.getDescription());
            existing.setStatusID(entity.getStatusID());
            existing.setPriorityID(entity.getPriorityID());
            existing.setTeamID(entity.getTeamID());
            existing.setCreatedAt(entity.getCreatedAt());
            existing.setUpdatedAt(entity.getUpdatedAt());
            existing.setResolved_at(entity.getResolved_at());
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

    public List<Incident> getByCriteria(String title, String status, String priority) {
        return repository.findByCriteria(title, status, priority);
    }

    public List<Incident> getByCriteriaWithCriteriaAPI(String title, String status, String priority) {
        return repository.findByCriteriaWithCriteriaAPI(title, status, priority);
    }
}
