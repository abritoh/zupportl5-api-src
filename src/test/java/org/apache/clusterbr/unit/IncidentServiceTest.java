package org.apache.clusterbr.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.apache.clusterbr.zupportl5.entity.Incident;
import org.apache.clusterbr.zupportl5.repository.IncidentRepository;
import org.apache.clusterbr.zupportl5.service.IncidentService;
import org.apache.clusterbr.zupportl5.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * <!-- comment-processor-start -->
 * IncidentServiceTest - Unit Test
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
 * <!-- comment-processor-end -->
 * 
 * <p><b>Source code of the Unit Test</b></p>
 * 
 * <pre>{@code
 * public class IncidentServiceTest {
 * 
 *     @Mock
 *     private IncidentRepository incidentRepository;
 * 
 *     @InjectMocks
 *     private IncidentService incidentService;
 * 
 *     @Mock
 *     private MessageService msgService;     
 * 
 *     private Incident incident;
 *     private Integer idInt = 1000;
 *     private Long idLong = Long.valueOf(idInt);
 * 
 *     private String title = "Test Incident :: IncidentServiceTest",
 *             description = "New awesome Incident added :: IncidentServiceTest";
 * 
 *     @BeforeEach
 *     void setUp() {
 *         MockitoAnnotations.openMocks(this);
 * 
 *         incident = new Incident();
 *         incident.setId(idInt);
 *         incident.setTitle(title);
 *         incident.setDescription(description);
 *         incident.setCreatedAt(new Timestamp(System.currentTimeMillis()));
 *         incident.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
 *     }
 * 
 *     @Test
 *     void testSaveIncident() {
 *         when(incidentRepository.save(any(Incident.class))).thenReturn(incident);
 * 
 *         Incident savedIncident = incidentService.save(incident);
 * 
 *         assertNotNull(savedIncident);
 *         assertEquals(title, savedIncident.getTitle());
 *         assertEquals(description, savedIncident.getDescription());
 *         verify(incidentRepository, times(1)).save(incident);
 *     }
 * 
 *     @Test
 *     void testFindAllIncidents() {
 *         when(incidentRepository.findAll()).thenReturn(List.of(incident));
 * 
 *         List<Incident> incidents = incidentService.findAll();
 * 
 *         assertNotNull(incidents);
 *         assertEquals(1, incidents.size());
 *         assertEquals(title, incidents.get(0).getTitle());
 *         verify(incidentRepository, times(1)).findAll();
 *     }
 * 
 *     @Test
 *     void testFindIncidentById() {
 *         when(incidentRepository.findById(idLong)).thenReturn(Optional.of(incident));
 * 
 *         Optional<Incident> foundIncident = incidentService.getById(idInt);
 * 
 *         assertTrue(foundIncident.isPresent());
 *         assertEquals(title, foundIncident.get().getTitle());
 *         verify(incidentRepository, times(1)).findById(idLong);
 *     }
 * 
 *     @Test
 *     void testUpdateIncident() {
 * 
 *         String newTitle = "Test Incident from IncidentServiceTest :: testUpdateIncident",
 *                 newDescription = "New awesome Incident added from IncidentServiceTest :: testUpdateIncident";
 * 
 *         when(incidentRepository.findById(idLong)).thenReturn(Optional.of(incident));
 *         when(incidentRepository.save(any(Incident.class))).thenReturn(incident);
 * 
 *         Incident updatedDetails = new Incident();
 *         updatedDetails.setTitle(newTitle);
 *         updatedDetails.setDescription(newDescription);
 * 
 *         Optional<Incident> updatedIncident = incidentService.update(idInt, updatedDetails);
 * 
 *         assertTrue(updatedIncident.isPresent());
 *         assertEquals(newTitle, updatedIncident.get().getTitle());
 *         assertEquals(newDescription, updatedIncident.get().getDescription());
 * 
 *         verify(incidentRepository, times(1)).findById(idLong);
 *         verify(incidentRepository, times(1)).save(any(Incident.class));
 *     }
 * 
 *     @Test
 *     void testUpdateIncidentNotFound() {
 *         when(incidentRepository.findById(idLong)).thenReturn(Optional.empty());
 * 
 *         Optional<Incident> updatedIncident = incidentService.update(idInt, incident);
 * 
 *         assertFalse(updatedIncident.isPresent());
 *         verify(incidentRepository, times(1)).findById(idLong);
 *         verify(incidentRepository, never()).save(any(Incident.class));
 *     }
 * 
 *     @Test
 *     void testDeleteIncidentById() {
 *         when(incidentRepository.existsById(idLong)).thenReturn(true);
 * 
 *         boolean isDeleted = incidentService.deleteById(idInt);
 * 
 *         assertTrue(isDeleted);
 *         verify(incidentRepository, times(1)).existsById(idLong);
 *         verify(incidentRepository, times(1)).deleteById(idLong);
 *     }
 * 
 *     @Test
 *     void testDeleteIncidentByIdNotFound() {
 *         when(incidentRepository.existsById(idLong)).thenReturn(false);
 * 
 *         boolean isDeleted = incidentService.deleteById(idInt);
 * 
 *         assertFalse(isDeleted);
 *         verify(incidentRepository, times(1)).existsById(idLong);
 *         verify(incidentRepository, never()).deleteById(Long.valueOf(anyInt()));
 *     }
 * }
 * 
 *}</pre>
 * 
 */
public class IncidentServiceTest {

    @Mock
    private IncidentRepository incidentRepository;

    @InjectMocks
    private IncidentService incidentService;

    @Mock
    private MessageService msgService;     

    private Incident incident;
    private Integer idInt = 1000;
    private Long idLong = Long.valueOf(idInt);

    private String title = "Test Incident :: IncidentServiceTest",
            description = "New awesome Incident added :: IncidentServiceTest";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        incident = new Incident();
        incident.setId(idInt);
        incident.setTitle(title);
        incident.setDescription(description);
        incident.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        incident.setUpdatedAt(new Timestamp(System.currentTimeMillis()));
    }

    @Test
    void testSaveIncident() {
        when(incidentRepository.save(any(Incident.class))).thenReturn(incident);

        Incident savedIncident = incidentService.save(incident);

        assertNotNull(savedIncident);
        assertEquals(title, savedIncident.getTitle());
        assertEquals(description, savedIncident.getDescription());
        verify(incidentRepository, times(1)).save(incident);
    }

    @Test
    void testFindAllIncidents() {
        when(incidentRepository.findAll()).thenReturn(List.of(incident));

        List<Incident> incidents = incidentService.findAll();

        assertNotNull(incidents);
        assertEquals(1, incidents.size());
        assertEquals(title, incidents.get(0).getTitle());
        verify(incidentRepository, times(1)).findAll();
    }

    @Test
    void testFindIncidentById() {
        when(incidentRepository.findById(idLong)).thenReturn(Optional.of(incident));

        Optional<Incident> foundIncident = incidentService.getById(idInt);

        assertTrue(foundIncident.isPresent());
        assertEquals(title, foundIncident.get().getTitle());
        verify(incidentRepository, times(1)).findById(idLong);
    }

    @Test
    void testUpdateIncident() {

        String newTitle = "Test Incident from IncidentServiceTest :: testUpdateIncident",
                newDescription = "New awesome Incident added from IncidentServiceTest :: testUpdateIncident";

        when(incidentRepository.findById(idLong)).thenReturn(Optional.of(incident));
        when(incidentRepository.save(any(Incident.class))).thenReturn(incident);

        Incident updatedDetails = new Incident();
        updatedDetails.setTitle(newTitle);
        updatedDetails.setDescription(newDescription);

        Optional<Incident> updatedIncident = incidentService.update(idInt, updatedDetails);

        assertTrue(updatedIncident.isPresent());
        assertEquals(newTitle, updatedIncident.get().getTitle());
        assertEquals(newDescription, updatedIncident.get().getDescription());

        verify(incidentRepository, times(1)).findById(idLong);
        verify(incidentRepository, times(1)).save(any(Incident.class));
    }

    @Test
    void testUpdateIncidentNotFound() {
        when(incidentRepository.findById(idLong)).thenReturn(Optional.empty());

        Optional<Incident> updatedIncident = incidentService.update(idInt, incident);

        assertFalse(updatedIncident.isPresent());
        verify(incidentRepository, times(1)).findById(idLong);
        verify(incidentRepository, never()).save(any(Incident.class));
    }

    @Test
    void testDeleteIncidentById() {
        when(incidentRepository.existsById(idLong)).thenReturn(true);

        boolean isDeleted = incidentService.deleteById(idInt);

        assertTrue(isDeleted);
        verify(incidentRepository, times(1)).existsById(idLong);
        verify(incidentRepository, times(1)).deleteById(idLong);
    }

    @Test
    void testDeleteIncidentByIdNotFound() {
        when(incidentRepository.existsById(idLong)).thenReturn(false);

        boolean isDeleted = incidentService.deleteById(idInt);

        assertFalse(isDeleted);
        verify(incidentRepository, times(1)).existsById(idLong);
        verify(incidentRepository, never()).deleteById( Long.valueOf(anyInt()) );
    }
}
