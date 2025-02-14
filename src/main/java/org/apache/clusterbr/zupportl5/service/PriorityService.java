package org.apache.clusterbr.zupportl5.service;

import java.util.List;
import java.util.Optional;

import org.apache.clusterbr.zupportl5.entity.Priority;
import org.apache.clusterbr.zupportl5.exception.RestApiException;
import org.apache.clusterbr.zupportl5.repository.PriorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/PriorityService_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/PriorityService_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/PriorityService_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/PriorityService_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Service
public class PriorityService {

    private final PriorityRepository repository;
    private final MessageService msgService;

    @Autowired
    public PriorityService(PriorityRepository repository, MessageService msgService) {
        this.repository = repository;
        this.msgService = msgService;
    }

    public List<Priority> getAll() {
        return repository.findAll();
    }

    public Optional<Priority> getById(Integer id) {
        return repository.findById(Long.valueOf(id));
    }

    public Priority create(Priority entity) {
        return repository.save(entity);
    }

    public Priority update(Integer id, Priority entity) {
        return repository.findById(Long.valueOf(id))
                .map(existing -> {
                    existing.setName(entity.getName());
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
