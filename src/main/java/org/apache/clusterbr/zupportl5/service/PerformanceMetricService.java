package org.apache.clusterbr.zupportl5.service;

import java.util.List;
import java.util.Optional;

import org.apache.clusterbr.zupportl5.entity.PerformanceMetric;
import org.apache.clusterbr.zupportl5.exception.RestApiException;
import org.apache.clusterbr.zupportl5.repository.PerformanceMetricRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/PerformanceMetricService_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/PerformanceMetricService_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/PerformanceMetricService_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/PerformanceMetricService_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Service
public class PerformanceMetricService {

    private final PerformanceMetricRepository repository;
    private final MessageService msgService;

    @Autowired
    public PerformanceMetricService(PerformanceMetricRepository repository, MessageService msgService) {
        this.repository = repository;
        this.msgService = msgService;
    }

    public List<PerformanceMetric> getAll() {
        return repository.findAll();
    }

    public Optional<PerformanceMetric> getById(Integer id) {
        return repository.findById(Long.valueOf(id));
    }

    public PerformanceMetric create(PerformanceMetric entity) {
        return repository.save(entity);
    }

    public PerformanceMetric update(Integer id, PerformanceMetric entity) {
        return repository.findById(Long.valueOf(id))
                .map(existing -> {
                    existing.setResolutionTime(entity.getResolutionTime());
                    existing.setResponseTime(entity.getResponseTime());
                    existing.setSatisfactionScore(entity.getSatisfactionScore());
                    existing.setIncidentID(entity.getIncidentID());
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
