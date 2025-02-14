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
 * <p><img src="{@docRoot}/generated-resources/uml/images/Incident_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Entity
@Table(name = "incident")
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    
    @ManyToOne
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    private Status statusID;

    
    @ManyToOne
    @JoinColumn(name = "priority_id", referencedColumnName = "id")
    private Priority priorityID;

    
    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "id")
    private Priority teamID;

    @Column(name = "createdAt", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "updatedAt", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp updatedAt;

    @Column(name = "resolved_at", columnDefinition = "TIMESTAMP NULL DEFAULT NULL")
    private Timestamp resolvedAt;

    
    // -- Getters and setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatusID() {
        return statusID;
    }

    public void setStatusID(Status statusID) {
        this.statusID = statusID;
    }

    public Priority getPriorityID() {
        return priorityID;
    }

    public void setPriorityID(Priority priorityID) {
        this.priorityID = priorityID;
    }

    public Priority getTeamID() {
        return teamID;
    }

    public void setTeamID(Priority teamID) {
        this.teamID = teamID;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Timestamp getResolved_at() {
        return resolvedAt;
    }

    public void setResolved_at(Timestamp resolvedAt) {
        this.resolvedAt = resolvedAt;
    }

}