package org.apache.clusterbr.zupportl5.entity;

import java.sql.Timestamp;

import org.apache.clusterbr.zupportl5.utils.JsonUtil;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/Settings_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Entity
@Table(name = "settings")
public class Settings {

    @Id
    @Column(name = "id_key")
    private String idKey;

    @Column(name = "value", nullable = false)
    private String value;

    @Column(name = "label", nullable = true)
    private String label;    

    @Column(name = "expires_at_long", nullable = true)
    private Long expiresAtLong;    

    @Column(name = "expires_at_time", nullable = true)
    private Timestamp expiresAtTime;

    @Column(name = "last_update", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp lastUpdate;    

    public Settings() {}
    
    public Settings(String idKey, String value) {
        this.idKey = idKey;
        this.value = value;
    }    

    // -- Getters and setters

    public String getIdKey() {
        return idKey;
    }

    public void setIdKey(String idKey) {
        this.idKey = idKey;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Long getExpiresAtLong() {
        return expiresAtLong;
    }

    public void setExpiresAtLong(Long expiresAtLong) {
        this.expiresAtLong = expiresAtLong;
    }

    public Timestamp getExpiresAtTime() {
        return expiresAtTime;
    }

    public void setExpiresAtTime(Timestamp expiresAtTime) {
        this.expiresAtTime = expiresAtTime;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }  

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}

