package org.apache.clusterbr.zupportl5.dto;

import java.sql.Timestamp;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DocumentDto_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1209
* <!-- comment-processor-end -->
 */
public class DocumentDto {
    private String documentType;
    private String category;
    private String xmlHeader;
    private String xmlContent;
    private Timestamp createdAt;
    private String idSource;

    //-- Getters & Setters
    
    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getXmlHeader() {
        return xmlHeader;
    }

    public void setXmlHeader(String xmlHeader) {
        this.xmlHeader = xmlHeader;
    }

    public String getXmlContent() {
        return xmlContent;
    }

    public void setXmlContent(String xmlContent) {
        this.xmlContent = xmlContent;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getIdSource() {
        return idSource;
    }

    public void setIdSource(String idSource) {
        this.idSource = idSource;
    }
}
