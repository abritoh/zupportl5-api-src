package org.apache.clusterbr.zupportl5.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.clusterbr.zupportl5.entity.Engineer;
import org.apache.clusterbr.zupportl5.exception.RestApiException;
import org.apache.clusterbr.zupportl5.repository.EngineerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/EngineerService_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/EngineerService_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/EngineerService_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/EngineerService_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Service
public class EngineerService {

    private static final Logger logger = LoggerFactory.getLogger(EngineerService.class);

    private final EngineerRepository repository;
    private final MessageService msgService;

    @Autowired
    public EngineerService(EngineerRepository repository, MessageService msgService) {
        this.repository = repository;
        this.msgService = msgService;
        logger.info(msgService.getMessageDefault("message.service.hi.there"));
    }

    public List<Engineer> findAll() {
        return repository.findAll();
    }

    public Optional<Engineer> getById(Long id) {
        return repository.findById(id);
    }

    public Optional<Engineer> getByEmail(String email) {
        return repository.findByEmail(email);
    }    

    public Engineer save(Engineer entity) {
        return repository.save(entity);
    }

    @Transactional
    public Optional<Engineer> update(Long id, Engineer entity) {
        return repository.findById(id).map(existing -> {
            existing.setFirstName(entity.getFirstName());
            existing.setLastName(entity.getLastName());
            existing.setRole(entity.getRole());
            return repository.save(existing);
        });
    }

    @Transactional
    public Optional<Engineer> partialUpdate(Long id, Map<String, Object> updates) {

        return repository.findById(id).map(existing -> {
            updates.forEach((key, value) -> {
                switch (key) {
                    case "firstName":
                        existing.setFirstName((String) value);
                        break;
                    case "lastName":
                        existing.setLastName((String) value);
                        break;
                    case "role":
                        existing.setRole((String) value);
                        break;
                    default:
                        throw new RestApiException(msgService, "message.service.fmt.invalid.field", key);
                }
            });
            return repository.save(existing);
        });
    }

    public boolean deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
