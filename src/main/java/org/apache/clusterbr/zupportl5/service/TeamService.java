package org.apache.clusterbr.zupportl5.service;

import java.util.List;
import java.util.Optional;

import org.apache.clusterbr.zupportl5.entity.Team;
import org.apache.clusterbr.zupportl5.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.clusterbr.zupportl5.exception.RestApiException;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/TeamService_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/TeamService_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/TeamService_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/TeamService_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Service
public class TeamService {

    private final TeamRepository repository;
    private final MessageService msgService;

    @Autowired
    public TeamService(TeamRepository repository, MessageService msgService) {
        this.repository = repository;
        this.msgService = msgService;
    }

    public List<Team> getAll() {
        return repository.findAll();
    }

    public Optional<Team> getById(Integer id) {
        return repository.findById(Long.valueOf(id));
    }

    public Team create(Team entity) {
        return repository.save(entity);
    }

    public Team update(Integer id, Team entity) {
        return repository.findById(Long.valueOf(id))
                .map(existing -> {
                    existing.setName(entity.getName());
                    existing.setDescription(entity.getDescription());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RestApiException(
                    msgService, "message.service.fmt.entity.not.found.by.id", id) );

    }

    public boolean deleteById(Long id) {
        if (repository.existsById(Long.valueOf(id))) {
            repository.deleteById(Long.valueOf(id));
            return true;
        }
        return false;
    }
}
