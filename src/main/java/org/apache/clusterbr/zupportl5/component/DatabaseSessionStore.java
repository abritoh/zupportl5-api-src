package org.apache.clusterbr.zupportl5.component;

import java.time.LocalDateTime;

import org.apache.clusterbr.zupportl5.service.dropbox.DropboxAuthStateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.dropbox.core.DbxSessionStore;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DatabaseSessionStore_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DatabaseSessionStore_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DatabaseSessionStore_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DatabaseSessionStore_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Component
public class DatabaseSessionStore implements DbxSessionStore {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseSessionStore.class);

    private static final String 
        NO_VALUE = "_NO_VALUE_"
        , CURRENT_THREAD_NOT_KEY_SET = "No request-id is set in the current thread context"
        ;

    private final int expirationMinutes;
    private final DropboxAuthStateService authStateService;
    private static final ThreadLocal<String> THREAD_LOCAL_KEY = new ThreadLocal<>();

    @Autowired
    public DatabaseSessionStore(DropboxAuthStateService authStateService, 
        @Value("${property.dropbox.auth.state.expiration.minutes:30}") int expirationMinutes) {

        this.authStateService = authStateService;
        this.expirationMinutes = expirationMinutes;

        logger.info("DatabaseSessionStore: authStateService: ".concat(authStateService.toString()));
        logger.info("DatabaseSessionStore: expirationMinutes: ".concat(String.valueOf(expirationMinutes)));
    }

    /* 
     * methods of the interface: DbxSessionStore 
     * ***/

    @Override
    public String get() {
        String requestId = THREAD_LOCAL_KEY.get();
        if (requestId != null) {            
            return get(requestId);
        } else {
            logger.error("(DatabaseSessionStore::String get() ) ".concat(CURRENT_THREAD_NOT_KEY_SET));
        }
        return NO_VALUE;
    }

    @Override
    public void set(String state) {
        String requestId = THREAD_LOCAL_KEY.get();
        if (requestId != null) {
            set(requestId, state, NO_VALUE);            
        } else {
            logger.error("(DatabaseSessionStore::set(String state) ) ".concat(CURRENT_THREAD_NOT_KEY_SET));
        }
    }

    @Override
    public void clear() {
        String requestId = THREAD_LOCAL_KEY.get();
        if (requestId != null) {
            clear(requestId);
            THREAD_LOCAL_KEY.remove();
        }
    }    

    /**
     * Utility method to set the thread-local request-id 
     * This must-be invoked before calling the no-argument methods.
     * @param requestId
     */
    public void setThreadLocalRequestId(String requestId) {
        THREAD_LOCAL_KEY.set(requestId);
    }

    /* 
     * reloaded methods that accept parameters 
     * ***/

    public String get(String requestId) {
        return authStateService.getState(requestId).orElse(null);
    }

    public void set(String requestId, String state, String clientId) {
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(expirationMinutes);
        authStateService.saveState(requestId, state, clientId, expiresAt);
    }
    
    public void clear(String requestId) {
        authStateService.deleteState(requestId);
    }
}
