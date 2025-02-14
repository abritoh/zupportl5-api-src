package org.apache.clusterbr.zupportl5.service.dropbox;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.clusterbr.zupportl5.entity.DropboxAuthState;
import org.apache.clusterbr.zupportl5.repository.DropboxAuthStateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DropboxAuthStateService_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DropboxAuthStateService_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DropboxAuthStateService_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DropboxAuthStateService_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
 * <!-- comment-processor-end -->
 */
@Service
public class DropboxAuthStateService {

    private static final Logger logger = LoggerFactory.getLogger(DropboxAuthStateService.class);

    private final DropboxAuthStateRepository repository;

    @Autowired
    public DropboxAuthStateService(DropboxAuthStateRepository repository) {
        this.repository = repository;
        logger.info("DropboxAuthStateService: repository" + repository.toString());
    }

    public void saveState(String requestId, String state, String clientId, LocalDateTime expiresAt) {
        DropboxAuthState authState = new DropboxAuthState(requestId, state, clientId, expiresAt);
        repository.save(authState);
    }

    public Optional<String> getState(String requestId) {
        return repository.findByRequestId(requestId).map(DropboxAuthState::getState);
    }

    public void deleteState(String requestId) {
        repository.deleteByRequestId(requestId);
    }
}


