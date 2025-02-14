package org.apache.clusterbr.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.clusterbr.zupportl5.entity.Engineer;
import org.apache.clusterbr.zupportl5.repository.EngineerRepository;
import org.apache.clusterbr.zupportl5.service.EngineerService;
import org.apache.clusterbr.zupportl5.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * <!-- comment-processor-start -->
 * EngineerServiceTest - Unit Test
 * 
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
 * <!-- comment-processor-end -->
 * 
 * <p><b>Source code of the Unit Test</b></p>
 * 
 * <pre>{@code
 * public class EngineerServiceTest {
 * 
 *     @Mock
 *     private EngineerRepository repository;
 * 
 *     @Mock
 *     private MessageService msgService;    
 * 
 *     @InjectMocks
 *     private EngineerService service;
 * 
 *     private Engineer sampleEngineer;
 * 
 *     private Integer idInt = 1000;
 *     Long idLong = Long.valueOf(idInt);
 *     private String firstName = "David",
 *             lastName = "Hume",
 *             role = "Software Engineer";
 * 
 *     @BeforeEach
 *     public void setUp() {
 *         MockitoAnnotations.openMocks(this);
 *         sampleEngineer = new Engineer();
 *         sampleEngineer.setId(idInt);
 *         sampleEngineer.setFirstName(firstName);
 *         sampleEngineer.setLastName(lastName);
 *         sampleEngineer.setRole(role);
 *     }
 * 
 *     @Test
 *     public void testFindAll() {
 *         List<Engineer> engineers = Collections.singletonList(sampleEngineer);
 *         when(repository.findAll()).thenReturn(engineers);
 * 
 *         List<Engineer> result = service.findAll();
 *         assertEquals(1, result.size());
 *         assertEquals(firstName, result.get(0).getFirstName());
 *     }
 * 
 *     @Test
 *     public void testGetById() {
 *         when(repository.findById(anyLong())).thenReturn(Optional.of(sampleEngineer));
 * 
 *         Optional<Engineer> result = service.getById(idLong);
 *         assertTrue(result.isPresent());
 *         assertEquals(firstName, result.get().getFirstName());
 *     }
 * 
 *     @Test
 *     public void testSave() {
 *         when(repository.save(any(Engineer.class))).thenReturn(sampleEngineer);
 * 
 *         Engineer result = service.save(sampleEngineer);
 *         assertNotNull(result);
 *         assertEquals(firstName, result.getFirstName());
 *     }
 * 
 *     @Test
 *     public void testUpdate() {
 *         Engineer updatedEngineer = new Engineer();
 *         updatedEngineer.setFirstName("Thomas");
 *         updatedEngineer.setLastName("Hobbes");
 *         updatedEngineer.setRole("Network Engineer");
 * 
 *         when(repository.findById(anyLong())).thenReturn(Optional.of(sampleEngineer));
 *         when(repository.save(any(Engineer.class))).thenReturn(updatedEngineer);
 * 
 *         Optional<Engineer> result = service.update(idLong, updatedEngineer);
 *         assertTrue(result.isPresent());
 *         assertEquals("Thomas", result.get().getFirstName());
 *         assertEquals("Hobbes", result.get().getLastName());
 *     }
 * 
 *     @Test
 *     public void testPartialUpdate() {
 *         Map<String, Object> updates = new HashMap<>();
 *         updates.put("firstName", "Kant");
 *         updates.put("role", "Team Lead");
 * 
 *         when(repository.findById(anyLong())).thenReturn(Optional.of(sampleEngineer));
 *         when(repository.save(any(Engineer.class))).thenReturn(sampleEngineer);
 * 
 *         Optional<Engineer> result = service.partialUpdate(idLong, updates);
 *         assertTrue(result.isPresent());
 *         assertEquals("Kant", result.get().getFirstName());
 *         assertEquals("Team Lead", result.get().getRole());
 *     }
 * 
 *     @Test
 *     public void testDeleteById() {
 *         when(repository.existsById(anyLong())).thenReturn(true);
 *         doNothing().when(repository).deleteById(anyLong());
 * 
 *         boolean result = service.deleteById(idLong);
 *         assertTrue(result);
 *         verify(repository, times(1)).deleteById(idLong);
 *     }
 * 
 *     @Test
 *     public void testDeleteById_NotFound() {
 *         when(repository.existsById(anyLong())).thenReturn(false);
 * 
 *         boolean result = service.deleteById(idLong);
 *         assertFalse(result);
 *         verify(repository, never()).deleteById(anyLong());
 *     }
 * }
 * 
 *}</pre>
 */
public class EngineerServiceTest {

    @Mock
    private EngineerRepository repository;

    @Mock
    private MessageService msgService;    

    @InjectMocks
    private EngineerService service;

    private Engineer sampleEngineer;

    private Integer idInt = 1000;
    Long idLong = Long.valueOf(idInt);
    private String firstName = "David",
            lastName = "Hume",
            role = "Software Engineer";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleEngineer = new Engineer();
        sampleEngineer.setId(idInt);
        sampleEngineer.setFirstName(firstName);
        sampleEngineer.setLastName(lastName);
        sampleEngineer.setRole(role);
    }

    @Test
    public void testFindAll() {
        List<Engineer> engineers = Collections.singletonList(sampleEngineer);
        when(repository.findAll()).thenReturn(engineers);

        List<Engineer> result = service.findAll();
        assertEquals(1, result.size());
        assertEquals(firstName, result.get(0).getFirstName());
    }

    @Test
    public void testGetById() {
        when(repository.findById(anyLong())).thenReturn(Optional.of(sampleEngineer));

        Optional<Engineer> result = service.getById(idLong);
        assertTrue(result.isPresent());
        assertEquals(firstName, result.get().getFirstName());
    }

    @Test
    public void testSave() {
        when(repository.save(any(Engineer.class))).thenReturn(sampleEngineer);

        Engineer result = service.save(sampleEngineer);
        assertNotNull(result);
        assertEquals(firstName, result.getFirstName());
    }

    @Test
    public void testUpdate() {
        Engineer updatedEngineer = new Engineer();
        updatedEngineer.setFirstName("Thomas");
        updatedEngineer.setLastName("Hobbes");
        updatedEngineer.setRole("Network Engineer");

        when(repository.findById(anyLong())).thenReturn(Optional.of(sampleEngineer));
        when(repository.save(any(Engineer.class))).thenReturn(updatedEngineer);

        Optional<Engineer> result = service.update(idLong, updatedEngineer);
        assertTrue(result.isPresent());
        assertEquals("Thomas", result.get().getFirstName());
        assertEquals("Hobbes", result.get().getLastName());
    }

    @Test
    public void testPartialUpdate() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("firstName", "Kant");
        updates.put("role", "Team Lead");

        when(repository.findById(anyLong())).thenReturn(Optional.of(sampleEngineer));
        when(repository.save(any(Engineer.class))).thenReturn(sampleEngineer);

        Optional<Engineer> result = service.partialUpdate(idLong, updates);
        assertTrue(result.isPresent());
        assertEquals("Kant", result.get().getFirstName());
        assertEquals("Team Lead", result.get().getRole());
    }

    @Test
    public void testDeleteById() {
        when(repository.existsById(anyLong())).thenReturn(true);
        doNothing().when(repository).deleteById(anyLong());

        boolean result = service.deleteById(idLong);
        assertTrue(result);
        verify(repository, times(1)).deleteById(idLong);
    }

    @Test
    public void testDeleteById_NotFound() {
        when(repository.existsById(anyLong())).thenReturn(false);

        boolean result = service.deleteById(idLong);
        assertFalse(result);
        verify(repository, never()).deleteById(anyLong());
    }
}
