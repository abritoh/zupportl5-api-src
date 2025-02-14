package org.apache.clusterbr.zupportl5.service;

import org.apache.clusterbr.zupportl5.entity.Runbook;
import org.apache.clusterbr.zupportl5.repository.RunbookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/RunbookService_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/RunbookService_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/RunbookService_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Service
public class RunbookService extends GenericServiceImpl<Runbook, Long, RunbookRepository> {

    @Autowired
    public RunbookService(RunbookRepository repository, MessageService msgService) {
        super(repository, msgService);
    }

    @Override
    protected void fillPropertiesOnUpdate(Runbook source, Runbook target) {
        target.setXmlHeader(source.getXmlHeader());
        target.setXmlContent(source.getXmlContent());
    }
}
