package org.apache.clusterbr.zupportl5.repository;

import org.apache.clusterbr.zupportl5.entity.PerformanceMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/PerformanceMetricRepository_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Repository
public interface PerformanceMetricRepository extends JpaRepository<PerformanceMetric, Long> {}
