package org.apache.clusterbr.zupportl5.component;

import java.time.LocalDateTime;

import org.apache.clusterbr.zupportl5.repository.DropboxAuthStateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DropboxAuthStateCleanupJob_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DropboxAuthStateCleanupJob_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DropboxAuthStateCleanupJob_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DropboxAuthStateCleanupJob_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Component
public class DropboxAuthStateCleanupJob {

    private static final Logger logger = LoggerFactory.getLogger(DropboxAuthStateCleanupJob.class);

    private final DropboxAuthStateRepository repository;

    public DropboxAuthStateCleanupJob(DropboxAuthStateRepository repository) {
        this.repository = repository;
    }

    /**
     * (second minute hour day-of-month month day-of-week)
     * 
     * ------------------------------------------------------------------
     * "0 *\/5 * * * ?" - for every 5-minutes
     * "0 0 * * * ?" - for every hour
     * "0 0 0/6 * * ?" - for every 6 hours (midnight, 6 AM, 12 PM, 6 PM)
     * "0 0 0 * * ?" - for once daily at midnight
     * ------------------------------------------------------------------
     */
    @Scheduled(cron = "0 0 0/6 * * ?")
    public void cleanupExpiredStates() {
        LocalDateTime now = LocalDateTime.now();
        repository.findAll().stream()
            .filter(state -> state.getExpiresAt() != null && state.getExpiresAt().isBefore(now))
            .forEach(row -> repository.deleteByRequestId(row.getRequestId()));
        
        logger.info("(DropboxAuthStateCleanupJob::cleanupExpiredStates) executed successfully.");
    }
}

