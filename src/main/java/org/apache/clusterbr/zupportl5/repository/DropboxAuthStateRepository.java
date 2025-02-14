package org.apache.clusterbr.zupportl5.repository;

import java.util.Optional;

import org.apache.clusterbr.zupportl5.entity.DropboxAuthState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DropboxAuthStateRepository_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Repository
public interface DropboxAuthStateRepository extends JpaRepository<DropboxAuthState , String> {
 
    Optional<DropboxAuthState> findByRequestId(String requestId);

    void deleteByRequestId(String requestId);
       
}
