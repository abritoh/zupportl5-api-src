package org.apache.clusterbr.zupportl5.service;

import java.util.List;
import java.util.Optional;

import org.apache.clusterbr.zupportl5.entity.Incidentlogs;
import org.apache.clusterbr.zupportl5.repository.IncidentlogsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.clusterbr.zupportl5.exception.RestApiException;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/IncidentlogsService_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/IncidentlogsService_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/IncidentlogsService_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/IncidentlogsService_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Service
public class IncidentlogsService {

    private final IncidentlogsRepository repository;
    private final MessageService msgService;

    @Autowired
    public IncidentlogsService(IncidentlogsRepository repository, MessageService msgService) {
        this.repository = repository;
        this.msgService = msgService;
    }

    public List<Incidentlogs> getAll() {
        return repository.findAll();
    }

    public Optional<Incidentlogs> getById(Integer id) {
        return repository.findById(Long.valueOf(id));
    }

    public Incidentlogs save(Incidentlogs entity) {
        return repository.save(entity);
    }

    public Incidentlogs update(Integer id, Incidentlogs entity) {
        return repository.findById(Long.valueOf(id))
                .map(existing -> {
                    existing.setAction(entity.getAction());
                    existing.setTimestamp(entity.getTimestamp());
                    existing.setIncidentID(entity.getIncidentID());
                    existing.setEngineerID(entity.getEngineerID());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RestApiException(
                        msgService, "message.service.fmt.entity.not.found.by.id", id));
    }

    public boolean deleteById(Integer id) {
        if (repository.existsById(Long.valueOf(id))) {
            repository.deleteById(Long.valueOf(id));
            return true;
        }
        return false;
    }
}
