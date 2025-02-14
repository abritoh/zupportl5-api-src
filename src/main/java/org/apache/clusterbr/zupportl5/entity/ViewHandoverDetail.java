
package org.apache.clusterbr.zupportl5.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/ViewHandoverDetail_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Entity
@Immutable
@Table(name = "view_handover_details")
public class ViewHandoverDetail {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "handover_time")
    private LocalDateTime handoverTime;

    @Column(name = "incident_title")
    private String incidentTitle;

    @Column(name = "incident_description")
    private String incidentDescription;

    @Column(name = "status_name")
    private String statusName;

    @Column(name = "priority_name")
    private String priorityName;

    @Column(name = "from_engineer_name")
    private String fromEngineerName;

    @Column(name = "to_engineer_name")
    private String toEngineerName;

    @Column(name = "notes")
    private String notes;

    @Column(name = "to_engineer_email")
    private String toEngineerEmail;

    @Column(name = "from_engineer_email")
    private String fromEngineerEmail;
    
    //-- getters & setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public LocalDateTime getHandoverTime() {
        return handoverTime;
    }

    public void setHandoverTime(LocalDateTime handoverTime) {
        this.handoverTime = handoverTime;
    }

    public String getIncidentTitle() {
        return incidentTitle;
    }

    public void setIncidentTitle(String incidentTitle) {
        this.incidentTitle = incidentTitle;
    }

    public String getIncidentDescription() {
        return incidentDescription;
    }

    public void setIncidentDescription(String incidentDescription) {
        this.incidentDescription = incidentDescription;
    }

    public String getFromEngineerName() {
        return fromEngineerName;
    }

    public void setFromEngineerName(String fromEngineerName) {
        this.fromEngineerName = fromEngineerName;
    }

    public String getFromEngineerEmail() {
        return fromEngineerEmail;
    }

    public void setFromEngineerEmail(String fromEngineerEmail) {
        this.fromEngineerEmail = fromEngineerEmail;
    }

    public String getToEngineerName() {
        return toEngineerName;
    }

    public void setToEngineerName(String toEngineerName) {
        this.toEngineerName = toEngineerName;
    }

    public String getToEngineerEmail() {
        return toEngineerEmail;
    }

    public void setToEngineerEmail(String toEngineerEmail) {
        this.toEngineerEmail = toEngineerEmail;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
