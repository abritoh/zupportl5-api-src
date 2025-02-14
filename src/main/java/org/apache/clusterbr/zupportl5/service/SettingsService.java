package org.apache.clusterbr.zupportl5.service;

import java.util.List;
import java.util.Optional;

import org.apache.clusterbr.zupportl5.entity.Settings;
import org.apache.clusterbr.zupportl5.exception.RestApiException;
import org.apache.clusterbr.zupportl5.repository.SettingsRepository;
import org.apache.clusterbr.zupportl5.utils.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/SettingsService_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/SettingsService_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/SettingsService_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/SettingsService_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Service
public class SettingsService {

    private static final Logger logger = LoggerFactory.getLogger(SettingsService.class);

    private final SettingsRepository repository;
    private final MessageService msgService;

    @Autowired
    public SettingsService(SettingsRepository repository, MessageService msgService) {
        this.repository = repository;
        this.msgService = msgService;
    }

    public List<Settings> getAll() {
        return repository.findAll();
    }

    public Optional<Settings> getByKey(String key) {
        return repository.findById(key);
    }

    public Settings save(Settings entity) {

        String key = entity.getIdKey();

        logger.info("(SettingsService::save()) was invoked key: " + key);

        Optional<Settings> found = this.getByKey(key);

        if(found.isPresent()) {
            logger.info("(SettingsService::save()::found) updating item in database: ");
            logger.info(JsonUtil.toJson(entity) );
            return this.update(key, entity);
        } else {
            logger.info("(SettingsService::save()::not-found) inserting into database: ");
            logger.info(JsonUtil.toJson(entity) );            
            return this.create(entity);
        }
    }

    public Settings create(Settings entity) {
        return repository.save(entity);
    }

    public Settings update(String key, Settings entity) {
        return repository.findById(key)
                .map(existing -> {
                    existing.setValue(entity.getValue());
                    existing.setLabel(entity.getLabel());
                    existing.setExpiresAtLong(entity.getExpiresAtLong());
                    existing.setExpiresAtTime(entity.getExpiresAtTime());                    
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RestApiException(
                        msgService, "message.service.fmt.entity.not.found.by.id", key));
    }

    public boolean deleteByKey(String key) {
        if (repository.existsById(key)) {
            repository.deleteById(key);
            return true;
        }
        return false;
    }
}
