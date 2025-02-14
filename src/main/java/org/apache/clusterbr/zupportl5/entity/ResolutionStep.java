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
 * <p><img src="{@docRoot}/generated-resources/uml/images/ResolutionStep_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Entity
@Table(name = "resolution_step")
public class ResolutionStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "step_number")
    private Integer stepNumber;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    
    @ManyToOne
    @JoinColumn(name= "incident_id", referencedColumnName = "id")
    private Incident incidentID;    

    
    @ManyToOne
    @JoinColumn(name= "engineer_id", referencedColumnName = "id")
    private Engineer engineerID;

    
    // -- Getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber(Integer stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
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

