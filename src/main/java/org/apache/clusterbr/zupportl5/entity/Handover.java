package org.apache.clusterbr.zupportl5.entity;

import java.sql.Timestamp;

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
 * <p><img src="{@docRoot}/generated-resources/uml/images/Handover_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Entity
@Table(name = "handover")
public class Handover {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    
    @ManyToOne
    @JoinColumn(name = "incident_id", referencedColumnName = "id")
    private Incident incidentID;

    
    @ManyToOne
    @JoinColumn(name = "from_engineer_id", referencedColumnName = "id")
    private Engineer fromEngineerID;

    
    @ManyToOne
    @JoinColumn(name = "to_engineer_id", referencedColumnName = "id")
    private Engineer toEngineerID;

    @Column(name = "handover_time", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp handoverTime;

    @Column(name = "notes")
    private String notes;

    
    // -- Getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Incident getIncidentID() {
        return incidentID;
    }

    public void setIncidentID(Incident incidentID) {
        this.incidentID = incidentID;
    }

    public Engineer getFromEngineerID() {
        return fromEngineerID;
    }

    public void setFromEngineerID(Engineer fromEngineerID) {
        this.fromEngineerID = fromEngineerID;
    }

    public Engineer getToEngineerID() {
        return toEngineerID;
    }

    public void setToEngineerID(Engineer toEngineerID) {
        this.toEngineerID = toEngineerID;
    }

    public Timestamp getHandoverTime() {
        return handoverTime;
    }

    public void setHandoverTime(Timestamp handoverTime) {
        this.handoverTime = handoverTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

}
