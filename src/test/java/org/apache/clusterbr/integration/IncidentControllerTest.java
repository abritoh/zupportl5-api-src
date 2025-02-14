package org.apache.clusterbr.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.util.Optional;

import org.apache.clusterbr.Zupportl5Application;
import org.apache.clusterbr.zupportl5.entity.Incident;
import org.apache.clusterbr.zupportl5.repository.IncidentRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * <!-- comment-processor-start -->
 * IncidentControllerTest - Integration Test
 * 
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
 * <!-- comment-processor-end -->
 * 
 * <p><b>Source code of the Integration Test</b></p>
 * 
 * <pre>{@code
 * @AutoConfigureMockMvc
 * @SpringBootTest(classes = Zupportl5Application.class)
 * public class IncidentControllerTest {
 *
 *     @Autowired
 *     private MockMvc mockMvc;
 *
 *     @Autowired
 *     private ObjectMapper objectMapper;
 *
 *     @Autowired
 *     private IncidentRepository incidentRepository;
 *
 *     private Incident incident;
 *
 *     @BeforeEach
 *     public void setup() {
 *         String title = "Test Incident",
 *                 description = "Description of test incident from IncidentControllerTest::setup";
 *
 *         incident = new Incident();
 *         incident.setTitle(title);
 *         incident.setDescription(description);
 *         incident.setCreatedAt(new Timestamp(System.currentTimeMillis()));
 *         incident.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
 *
 *         incident = incidentRepository.save(incident);
 *     }
 *
 *     @Test
 *     public void testCreateIncident() throws Exception {
 *
 *         String title = "New Incident",
 *                 description = "Description of new incident from IncidentControllerTest::testCreateIncident";
 *
 *         Incident newIncident = new Incident();
 *         newIncident.setTitle(title);
 *         newIncident.setDescription(description);
 *         newIncident.setCreatedAt(new Timestamp(System.currentTimeMillis()));
 *         newIncident.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
 *
 *         mockMvc.perform(post("/api/incidents")
 *                 .contentType(MediaType.APPLICATION_JSON)
 *                 .content(objectMapper.writeValueAsString(newIncident)))
 *                 .andExpect(status().isCreated())
 *                 .andExpect(jsonPath("$.title").value(title))
 *                 .andExpect(jsonPath("$.description").value(description));
 *     }
 *
 *     @Test
 *     public void testGetAllIncidents() throws Exception {
 *         mockMvc.perform(get("/api/incidents")
 *                 .contentType(MediaType.APPLICATION_JSON))
 *                 .andExpect(status().isOk())
 *                 .andExpect(jsonPath("$.length()").value(Matchers.greaterThanOrEqualTo(1)));
 *     }
 *
 *     @Test
 *     public void testGetIncidentById() throws Exception {
 *         mockMvc.perform(get("/api/incidents/{id}", incident.getId())
 *                 .contentType(MediaType.APPLICATION_JSON))
 *                 .andExpect(status().isOk())
 *                 .andExpect(jsonPath("$.title").value(incident.getTitle()))
 *                 .andExpect(jsonPath("$.description").value(incident.getDescription()));
 *     }
 *
 *     @Test
 *     public void testUpdateIncident() throws Exception {
 *
 *         String title = "Updated Incident",
 *                 description = "Updated description from IncidentControllerTest::testUpdateIncident";
 *
 *         Incident updatedIncident = new Incident();
 *         updatedIncident.setTitle(title);
 *         updatedIncident.setDescription(description);
 *         updatedIncident.setCreatedAt(incident.getCreatedAt());
 *         updatedIncident.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
 *
 *         mockMvc.perform(put("/api/incidents/{id}", incident.getId())
 *                 .contentType(MediaType.APPLICATION_JSON)
 *                 .content(objectMapper.writeValueAsString(updatedIncident)))
 *                 .andExpect(status().isOk())
 *                 .andExpect(jsonPath("$.title").value(title))
 *                 .andExpect(jsonPath("$.description").value(description));
 *     }
 *
 *     @Test
 *     public void testDeleteIncident() throws Exception {
 *         mockMvc.perform(delete("/api/incidents/{id}", incident.getId())
 *                 .contentType(MediaType.APPLICATION_JSON))
 *                 .andExpect(status().isNoContent());
 *
 *         Optional<Incident> deletedIncident = incidentRepository.findById(Long.valueOf(incident.getId()));
 *         assert deletedIncident.isEmpty();
 *     }
 * }
 * 
 *}</pre>
 */
@AutoConfigureMockMvc
@SpringBootTest(classes = Zupportl5Application.class)
public class IncidentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IncidentRepository incidentRepository;

    private Incident incident;

    @BeforeEach
    public void setup() {
        String title = "Test Incident",
                description = "Description of test incident from IncidentControllerTest::setup";

        incident = new Incident();
        incident.setTitle(title);
        incident.setDescription(description);
        incident.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        incident.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        incident = incidentRepository.save(incident);
    }

    @Test
    public void testCreateIncident() throws Exception {

        String title = "New Incident",
                description = "Description of new incident from IncidentControllerTest::testCreateIncident";

        Incident newIncident = new Incident();
        newIncident.setTitle(title);
        newIncident.setDescription(description);
        newIncident.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        newIncident.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        mockMvc.perform(post("/api/incidents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newIncident)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.description").value(description));
    }

    @Test
    public void testGetAllIncidents() throws Exception {
        mockMvc.perform(get("/api/incidents")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(Matchers.greaterThanOrEqualTo(1)));
    }

    @Test
    public void testGetIncidentById() throws Exception {
        mockMvc.perform(get("/api/incidents/{id}", incident.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(incident.getTitle()))
                .andExpect(jsonPath("$.description").value(incident.getDescription()));
    }

    @Test
    public void testUpdateIncident() throws Exception {

        String title = "Updated Incident",
                description = "Updated description from IncidentControllerTest::testUpdateIncident";

        Incident updatedIncident = new Incident();
        updatedIncident.setTitle(title);
        updatedIncident.setDescription(description);
        updatedIncident.setCreatedAt(incident.getCreatedAt());
        updatedIncident.setUpdatedAt(new Timestamp(System.currentTimeMillis()));

        mockMvc.perform(put("/api/incidents/{id}", incident.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedIncident)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.description").value(description));
    }

    @Test
    public void testDeleteIncident() throws Exception {
        mockMvc.perform(delete("/api/incidents/{id}", incident.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Optional<Incident> deletedIncident = incidentRepository.findById(Long.valueOf(incident.getId()));
        assert deletedIncident.isEmpty();
    }
}
