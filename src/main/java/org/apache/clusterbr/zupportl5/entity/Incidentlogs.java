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
 * <p><img src="{@docRoot}/generated-resources/uml/images/Incidentlogs_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Entity
@Table(name = "incidentlogs")
public class Incidentlogs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)    
    @Column(name = "id")
    private Integer id;

    @Column(name = "action", nullable = false)
    private String action;

    @Column(name = "timestamp", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp timestamp;

    
    @ManyToOne
    @JoinColumn(name="incident_id", referencedColumnName = "id")
    private Incident incidentID;

    
    @ManyToOne
    @JoinColumn(name="engineer_id", referencedColumnName = "id")
    private Engineer engineerID;


    // -- Getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Incident getIncidentID() {
        return incidentID;
    }

    public void setIncidentID(Incident incidentID) {
        this.incidentID = incidentID;
    }

    public Engineer getEngineerID() {
        return engineerID;
    }

    public void setEngineerID(Engineer engineerID) {
        this.engineerID = engineerID;
    }

}

