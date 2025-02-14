package org.apache.clusterbr.zupportl5.service;

import java.util.List;

import org.apache.clusterbr.zupportl5.dto.DocumentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;


/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DocumentService_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DocumentService_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DocumentService_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DocumentService_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Service
public class DocumentService {
    
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public DocumentService
        (JdbcTemplate jdbcTemplate, 
        NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
            
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<DocumentDto> getDocument(String idSource) {

        String query = "CALL sp_get_document(:idsource)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("idsource", idSource);
    
        return namedParameterJdbcTemplate.query(
            query,
            params,
            (rs, rowNum) -> {
                DocumentDto dto = new DocumentDto();
                dto.setDocumentType(rs.getString("document_type"));
                dto.setCategory(rs.getString("category"));
                dto.setXmlHeader(rs.getString("xmlheader"));
                dto.setXmlContent(rs.getString("xmlcontent"));
                dto.setCreatedAt(rs.getTimestamp("created_at"));
                dto.setIdSource(rs.getString("idsource"));
                return dto;
            }
        );
    }


    public List<DocumentDto> getAllDocuments(int limitRows) {

        String query = "CALL sp_get_documents(:limit_rows)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("limit_rows", limitRows);
    
        return namedParameterJdbcTemplate.query(
            query,
            params,
            (rs, rowNum) -> {
                DocumentDto dto = new DocumentDto();
                dto.setDocumentType(rs.getString("document_type"));
                dto.setCategory(rs.getString("category"));
                dto.setXmlHeader(rs.getString("xmlheader"));
                dto.setCreatedAt(rs.getTimestamp("created_at"));
                dto.setIdSource(rs.getString("idsource"));
                return dto;
            }
        );
    }

    public List<DocumentDto> searchDocuments(String keyword) {

        String query = "CALL sp_search_documents(:keyword)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("keyword", keyword);
    
        return namedParameterJdbcTemplate.query(
            query,
            params,
            (rs, rowNum) -> {
                DocumentDto dto = new DocumentDto();
                dto.setDocumentType(rs.getString("document_type"));
                dto.setCategory(rs.getString("category"));
                dto.setXmlHeader(rs.getString("xmlheader"));
                dto.setXmlContent(rs.getString("xmlcontent"));
                dto.setCreatedAt(rs.getTimestamp("created_at"));
                dto.setIdSource(rs.getString("idsource"));
                return dto;
            }
        );
    }  
    
}
