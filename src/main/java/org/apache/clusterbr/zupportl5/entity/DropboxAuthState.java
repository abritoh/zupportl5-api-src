package org.apache.clusterbr.zupportl5.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DropboxAuthState_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Entity
@Table(name = "dropbox_auth_state")
public class DropboxAuthState {

    @Id
    @Column(name = "request_id", nullable = false, unique = true)
    private String requestId;

    @Column(name = "state", nullable = false)
    private String state;

    @Column(name = "client_id", nullable = true)
    private String clientId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    public DropboxAuthState() {}

    public DropboxAuthState(String requestId, String state) {
        this.requestId = requestId;
        this.state = state;
        this.createdAt = LocalDateTime.now();
    }    

    public DropboxAuthState(String requestId, String state, String clientId, LocalDateTime expiresAt) {
        this.requestId = requestId;
        this.state = state;
        this.clientId = clientId;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = expiresAt;
    }

    //-- getters and setters
    
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
}
