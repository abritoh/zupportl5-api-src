package org.apache.clusterbr.zupportl5.service;

import java.util.List;
import java.util.Optional;

import org.apache.clusterbr.zupportl5.entity.ResolutionStep;
import org.apache.clusterbr.zupportl5.exception.RestApiException;
import org.apache.clusterbr.zupportl5.repository.ResolutionStepRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ResolutionStepService_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ResolutionStepService_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ResolutionStepService_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ResolutionStepService_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Service
public class ResolutionStepService {

    private final ResolutionStepRepository repository;
    private final MessageService msgService;

    @Autowired
    public ResolutionStepService(ResolutionStepRepository repository, MessageService msgService) {
        this.repository = repository;
        this.msgService = msgService;
    }

    public List<ResolutionStep> getAll() {
        return repository.findAll();
    }

    public Optional<ResolutionStep> getById(Integer id) {
        return repository.findById(Long.valueOf(id));
    }

    public ResolutionStep create(ResolutionStep entity) {
        return repository.save(entity);
    }

    public ResolutionStep update(Integer id, ResolutionStep entity) {
        return repository.findById(Long.valueOf(id))
                .map(existing -> {
                    existing.setStepNumber(entity.getStepNumber());
                    existing.setDescription(entity.getDescription());
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
