package org.apache.clusterbr.zupportl5.repository;

import java.util.List;

import org.apache.clusterbr.zupportl5.entity.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/IncidentRepository_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long>, IncidentRepositoryCriteria {
       
       @Query("SELECT i FROM Incident i " +
                     "WHERE (:title IS NULL OR i.title = :title) " +
                     "AND (:status IS NULL OR i.statusID.name = :status) " +
                     "AND (:priority IS NULL OR i.priorityID.name = :priority)")
       List<Incident> findByCriteria(
                     @Param("title") String title,
                     @Param("status") String status,
                     @Param("priority") String priority);
       
       List<Incident> findByCriteriaWithCriteriaAPI(String title, String status, String priority);
}
