package org.apache.clusterbr.zupportl5.service;

import java.util.List;
import java.util.Optional;

import org.apache.clusterbr.zupportl5.exception.RestApiException;
import org.apache.clusterbr.zupportl5.generic.GenericEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/GenericServiceImpl_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/GenericServiceImpl_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/GenericServiceImpl_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/GenericServiceImpl_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Service
public abstract class GenericServiceImpl<T, ID, R extends JpaRepository<T, ID>> implements GenericEntityService<T, ID> {

    protected final R repository;
    protected final MessageService messageService;

    @Autowired
    public GenericServiceImpl(R repository, MessageService messageService) {
        this.repository = repository;
        this.messageService = messageService;
    }

    @Override
    public List<T> getAll() {
        return repository.findAll();
    }

    @Override
    public Optional<T> getById(ID id) {
        return repository.findById(id);
    }

    @Override
    public T create(T entity) {
        return repository.save(entity);
    }

    @Override
    public T update(ID id, T entity) {
        return repository.findById(id)
                .map(existing -> {
                    fillPropertiesOnUpdate(entity, existing);
                    return repository.save(existing);
                }).orElseThrow(() -> new RestApiException(
                        messageService, "error.entity.not.found", id.toString()));
    }

    @Override
    public boolean deleteById(ID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        } else {
            throw new RestApiException(
                    messageService, "error.entity.not.found", id.toString());
        }
    }

    protected abstract void fillPropertiesOnUpdate(T source, T target);
}
