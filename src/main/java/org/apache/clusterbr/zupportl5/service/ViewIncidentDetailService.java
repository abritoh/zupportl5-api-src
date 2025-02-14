package org.apache.clusterbr.zupportl5.service;

import org.apache.clusterbr.zupportl5.entity.ViewIncidentDetail;
import org.apache.clusterbr.zupportl5.repository.ViewIncidentDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ViewIncidentDetailService_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ViewIncidentDetailService_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ViewIncidentDetailService_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ViewIncidentDetailService_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Service
public class ViewIncidentDetailService {

    private final ViewIncidentDetailRepository repository;

    @Autowired
    public ViewIncidentDetailService(ViewIncidentDetailRepository repository) {
        this.repository = repository;
    }

    public List<ViewIncidentDetail> findAll() {
        return repository.findAll();
    }

    public Optional<ViewIncidentDetail> findById(Long id) {
        return repository.findById(id);
    }
}
