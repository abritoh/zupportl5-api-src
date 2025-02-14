package org.apache.clusterbr.zupportl5.entity;

import java.sql.Timestamp;

import org.hibernate.annotations.Check;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;


/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/PerformanceMetric_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Entity
@Table(name = "performance_metrics")
@Check(constraints = "satisfaction_score >= 1 AND satisfaction_score <= 5")
public class PerformanceMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "resolution_time")
    private Integer resolutionTime;

    @Column(name = "response_time")
    private Integer responseTime;

    
    @Min(1)
    @Max(5)
    @Column(name = "satisfaction_score")
    private Integer satisfactionScore;

    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    
    @ManyToOne
    @JoinColumn(name = "incident_id", referencedColumnName = "id")
    private Incident incidentID;

    // -- JPA hooks

    @PrePersist
    @PreUpdate
    private void validateSatisfactionScore() {
        if (satisfactionScore < 1 || satisfactionScore > 5) {
            String msg = "satisfaction_score must be between 1 and 5";
            throw new IllegalArgumentException(msg);
        }
    }

    // -- Getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getResolutionTime() {
        return resolutionTime;
    }

    public void setResolutionTime(Integer resolutionTime) {
        this.resolutionTime = resolutionTime;
    }

    public Integer getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Integer responseTime) {
        this.responseTime = responseTime;
    }

    public Integer getSatisfactionScore() {
        return satisfactionScore;
    }

    public void setSatisfactionScore(Integer satisfactionScore) {
        this.satisfactionScore = satisfactionScore;
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

}
