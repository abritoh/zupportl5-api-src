package org.apache.clusterbr.zupportl5.service;

import org.apache.clusterbr.zupportl5.entity.ViewHandoverDetail;
import org.apache.clusterbr.zupportl5.repository.ViewHandoverDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ViewHandoverDetailService_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ViewHandoverDetailService_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ViewHandoverDetailService_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ViewHandoverDetailService_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Service
public class ViewHandoverDetailService {

    private final ViewHandoverDetailRepository repository;

    @Autowired
    public ViewHandoverDetailService(ViewHandoverDetailRepository repository) {
        this.repository = repository;
    }

    public List<ViewHandoverDetail> findAll() {
        return repository.findAll();
    }

    public Optional<ViewHandoverDetail> findById(Long id) {
        return repository.findById(id);
    }    
}
