package org.apache.clusterbr.zupportl5.repository;

import java.util.List;
import org.apache.clusterbr.zupportl5.entity.Incident;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;


/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/IncidentRepositoryImpl_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Repository
public class IncidentRepositoryImpl implements IncidentRepositoryCriteria {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Incident> findByCriteriaWithCriteriaAPI(String title, String status, String priority) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Incident> query = cb.createQuery(Incident.class);
        Root<Incident> root = query.from(Incident.class);

        List<Predicate> predicates = new ArrayList<>();

        if (title != null) {
            predicates.add(cb.equal(root.get("title"), title));
        }
        if (status != null) {
            predicates.add(cb.equal(root.get("statusID").get("name"), status));
        }
        if (priority != null) {
            predicates.add(cb.equal(root.get("priorityID").get("name"), priority));
        }

        query.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query).getResultList();
    }
}
