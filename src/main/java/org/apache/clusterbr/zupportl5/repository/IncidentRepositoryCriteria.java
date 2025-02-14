package org.apache.clusterbr.zupportl5.repository;

import java.util.List;
import org.apache.clusterbr.zupportl5.entity.Incident;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/IncidentRepositoryCriteria_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
public interface IncidentRepositoryCriteria {
    List<Incident> findByCriteriaWithCriteriaAPI(String title, String status, String priority);
}

