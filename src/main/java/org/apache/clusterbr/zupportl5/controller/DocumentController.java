package org.apache.clusterbr.zupportl5.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import org.apache.clusterbr.zupportl5.dto.DocumentDto;
import org.apache.clusterbr.zupportl5.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <!-- comment-processor-start -->
 * <p>REST-API Endpoints --</p>
 * <ul>
 * <li>GET /api/documents/id/{idsource}: Returns data from the API</li>
 * <li>GET /api/documents/top/{limitRows}: Returns data from the API</li>
 * <li>GET /api/documents/search: Returns data from the API</li>
 * </ul>
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DocumentController_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DocumentController_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DocumentController_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DocumentController_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/viewer")
    public ResponseEntity<String> getViewForProcessXml() throws IOException {
        String content;
        
        Resource resource = new ClassPathResource("static/public/document-viewer.html");
        try (InputStream inputStream = resource.getInputStream()) {
            content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);

        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllDocuments(@RequestParam(value = "limit", defaultValue = "0") Integer limitRows) {
        List<DocumentDto> results = documentService.getAllDocuments(limitRows);
        if (results.isEmpty()) {
            String reason = "No documents found.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", reason));
        }
        return ResponseEntity.ok(results);
    }      

    @GetMapping("/id/{idSource}")
    public ResponseEntity<Object> getDocument(@PathVariable("idSource") String idSource) {
        List<DocumentDto> results = documentService.getDocument(idSource);        
        if (results.isEmpty()) {
            String reason = "No document found for the given idSource.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", reason));
        }
        return ResponseEntity.ok(results.get(0));
    } 

    @GetMapping("/search")
    public ResponseEntity<Object> searchDocuments(@RequestParam("keyword") String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            String reason = "Keyword must not be empty.";
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", reason));            
        }        
        List<DocumentDto> results = documentService.searchDocuments(keyword);        
        if (results.isEmpty()) {
            String reason = "No documents found for the given keyword.";
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", reason));
        }        
        return ResponseEntity.ok(results);
    }

}

