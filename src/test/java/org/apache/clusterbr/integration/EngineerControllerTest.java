package org.apache.clusterbr.integration;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.clusterbr.Zupportl5Application;
import org.apache.clusterbr.zupportl5.entity.Engineer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

/**
 * <!-- comment-processor-start -->
 * <p><b>EngineerControllerTest - Integration Test</b></p>
 * 
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
 * 
 * <!-- comment-processor-end -->
 * 
 * <p><b>Source code of the Integration Test</b></p>
 * 
 * <pre>{@code
 * @AutoConfigureMockMvc
 * @SpringBootTest(classes = Zupportl5Application.class)
 * public class EngineerControllerTest {
 *
 *     private static final Logger logger = LoggerFactory.getLogger(EngineerControllerTest.class);
 *
 *     @Autowired
 *     private MockMvc mockMvc;
 *
 *     @Autowired
 *     private ObjectMapper objectMapper;
 *
 *     private Engineer prototypeEngineer;
 *
 *     private String firstName = "Euler",
 *             lastName = "Paul",
 *             email = "euler@math.org",
 *             role = "Math Engineer";
 *
 *     @BeforeEach
 *     public void setUp() {
 *         prototypeEngineer = new Engineer();
 *         prototypeEngineer.setFirstName(firstName);
 *         prototypeEngineer.setLastName(lastName);
 *         prototypeEngineer.setEmail(email);
 *         prototypeEngineer.setRole(role);
 *     }
 *
 *     @Test
 *     public void testCreateEngineer() throws Exception {
 *
 *         Engineer newEngineer = createNewEngineerDeleteIfEmailExists("euler_create@math.org");
 *         String engineerJson = objectMapper.writeValueAsString(newEngineer);
 *
 *         mockMvc.perform(post("/api/engineers")
 *                 .contentType(MediaType.APPLICATION_JSON)
 *                 .content(engineerJson))
 *                 .andExpect(status().isCreated())
 *                 .andExpect(jsonPath("$.firstName", is(firstName)))
 *                 .andExpect(jsonPath("$.role", is(role)));
 *     }
 *
 *     @Test
 *     public void testGetAllEngineers() throws Exception {
 *         mockMvc.perform(get("/api/engineers")
 *                 .contentType(MediaType.APPLICATION_JSON))
 *                 .andExpect(status().isOk())
 *                 .andExpect(jsonPath("$").isArray())
 *                 .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(0)));
 *     }
 *
 *     @Test
 *     public void testGetEngineerById() throws Exception {
 *         Engineer newEngineer = createNewEngineerDeleteIfEmailExists("euler_get_066@math.edu");
 *
 *         mockMvc.perform(get("/api/engineers/id/{id}", newEngineer.getId())
 *                 .contentType(MediaType.APPLICATION_JSON))
 *                 .andExpect(status().isOk())
 *                 .andExpect(jsonPath("$.id", is(newEngineer.getId().intValue())))
 *                 .andExpect(jsonPath("$.firstName", is(newEngineer.getFirstName())))
 *                 .andExpect(jsonPath("$.role", is(newEngineer.getRole())));
 *     }
 *
 *     @Test
 *     public void testUpdateEngineer() throws Exception {
 *
 *         Engineer newEngineer = createNewEngineerDeleteIfEmailExists("euler_update_666@math.com");
 *         newEngineer.setRole("Senior Engineer");
 *
 *         String updatedEngineerJson = objectMapper.writeValueAsString(newEngineer);
 *
 *         mockMvc.perform(put("/api/engineers/{id}", newEngineer.getId())
 *                 .contentType(MediaType.APPLICATION_JSON)
 *                 .content(updatedEngineerJson))
 *                 .andExpect(status().isOk())
 *                 .andExpect(jsonPath("$.role", is("Senior Engineer")));
 *     }
 *
 *     @Test
 *     public void testPartialUpdateEngineer() throws Exception {
 *
 *         Engineer newEngineer = createNewEngineerDeleteIfEmailExists("euler_partial_update@math.org");
 *
 *         mockMvc.perform(patch("/api/engineers/{id}", newEngineer.getId())
 *                 .contentType(MediaType.APPLICATION_JSON)
 *                 .content("{\"role\": \"Lead Engineer\"}"))
 *                 .andExpect(status().isOk())
 *                 .andExpect(jsonPath("$.role", is("Lead Engineer")));
 *     }
 *
 *     @Test
 *     public void testDeleteEngineer() throws Exception {
 *         Engineer newEngineer = createNewEngineerDeleteIfEmailExists("euler_delete@math.org");
 *
 *         mockMvc.perform(delete("/api/engineers/{id}", newEngineer.getId()))
 *                 .andExpect(status().isNoContent());
 *
 *         mockMvc.perform(
 *                 get("/api/engineers/id/{id}", newEngineer.getId()))
 *                 .andExpect(status().isNotFound());
 *     }
 *
 *     private Engineer createNewEngineerDeleteIfEmailExists(String newEmail)  {
 *
 *         prototypeEngineer.setEmail(newEmail);   
 *         String engineerJson = null;     
 *
 *         try {
 *             engineerJson = objectMapper.writeValueAsString(prototypeEngineer);
 *
 *             ResultActions result1 = mockMvc.perform(get("/api/engineers/email/{email}", newEmail));
 *             int status = result1.andReturn().getResponse().getStatus();
 *
 *             if (status == HttpStatus.OK.value()) {
 *
 *                 result1.andExpect(jsonPath("$.id").exists());
 *                 
 *                 String jsonStringResponse1 = result1.andReturn().getResponse().getContentAsString();
 *                 
 *                 int id = JsonPath.parse(jsonStringResponse1).read("$.id");       
 *                 
 *                 ResultActions result2 = mockMvc.perform(delete("/api/engineers/{id}", id))
 *                     .andExpect(status().isNoContent());
 *
 *             } else if (status == HttpStatus.NOT_FOUND.value()) {
 *                 result1.andExpect(status().isNotFound());
 *             }
 *
 *             ResultActions result3 = mockMvc.perform(
 *                 post("/api/engineers")
 *                         .contentType(MediaType.APPLICATION_JSON)
 *                         .content(engineerJson))
 *                 .andExpect(status().isCreated());
 *
 *             String responseBody = result3.andReturn().getResponse().getContentAsString();
 *             return objectMapper.readValue(responseBody, Engineer.class);
 *
 *         } catch (Exception ex) {
 *             ex.printStackTrace();
 *             logger.info("(createNewEngineerDeleteIfEmailExists:mockMvc.perform.get.by.email)");
 *             logger.info(ex.getMessage() != null ? ex.getMessage() : ex.toString());
 *         }
 *
 *         return prototypeEngineer;
 *     }
 * }
 * 
 *}</pre>
 */
@AutoConfigureMockMvc
@SpringBootTest(classes = Zupportl5Application.class)
public class EngineerControllerTest {

    private static final Logger logger = LoggerFactory.getLogger(EngineerControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Engineer prototypeEngineer;

    private String firstName = "Euler",
            lastName = "Paul",
            email = "euler@math.org",
            role = "Math Engineer";

    @BeforeEach
    public void setUp() {
        prototypeEngineer = new Engineer();
        prototypeEngineer.setFirstName(firstName);
        prototypeEngineer.setLastName(lastName);
        prototypeEngineer.setEmail(email);
        prototypeEngineer.setRole(role);
    }

    @Test
    public void testCreateEngineer() throws Exception {

        Engineer newEngineer = createNewEngineerDeleteIfEmailExists("euler_create@math.org");
        String engineerJson = objectMapper.writeValueAsString(newEngineer);

        mockMvc.perform(post("/api/engineers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(engineerJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", is(firstName)))
                .andExpect(jsonPath("$.role", is(role)));
    }

    @Test
    public void testGetAllEngineers() throws Exception {
        mockMvc.perform(get("/api/engineers")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()", greaterThanOrEqualTo(0)));
    }

    @Test
    public void testGetEngineerById() throws Exception {
        Engineer newEngineer = createNewEngineerDeleteIfEmailExists("euler_get_066@math.edu");

        mockMvc.perform(get("/api/engineers/id/{id}", newEngineer.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(newEngineer.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is(newEngineer.getFirstName())))
                .andExpect(jsonPath("$.role", is(newEngineer.getRole())));
    }

    @Test
    public void testUpdateEngineer() throws Exception {

        Engineer newEngineer = createNewEngineerDeleteIfEmailExists("euler_update_666@math.com");
        newEngineer.setRole("Senior Engineer");

        String updatedEngineerJson = objectMapper.writeValueAsString(newEngineer);

        mockMvc.perform(put("/api/engineers/{id}", newEngineer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedEngineerJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role", is("Senior Engineer")));
    }

    @Test
    public void testPartialUpdateEngineer() throws Exception {

        Engineer newEngineer = createNewEngineerDeleteIfEmailExists("euler_partial_update@math.org");

        mockMvc.perform(patch("/api/engineers/{id}", newEngineer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"role\": \"Lead Engineer\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role", is("Lead Engineer")));
    }

    @Test
    public void testDeleteEngineer() throws Exception {
        Engineer newEngineer = createNewEngineerDeleteIfEmailExists("euler_delete@math.org");

        mockMvc.perform(delete("/api/engineers/{id}", newEngineer.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(
                get("/api/engineers/id/{id}", newEngineer.getId()))
                .andExpect(status().isNotFound());
    }
    
    private Engineer createNewEngineerDeleteIfEmailExists(String newEmail)  {

        prototypeEngineer.setEmail(newEmail);   
        String engineerJson = null;     

        try {
            engineerJson = objectMapper.writeValueAsString(prototypeEngineer);

            ResultActions result1 = mockMvc.perform(get("/api/engineers/email/{email}", newEmail));
            int status = result1.andReturn().getResponse().getStatus();

            //-- if exist by email then delete it
            if (status == HttpStatus.OK.value()) {

                result1.andExpect(jsonPath("$.id").exists());
                
                String jsonStringResponse1 = result1.andReturn().getResponse().getContentAsString();
                
                int id = JsonPath.parse(jsonStringResponse1).read("$.id");       
                
                ResultActions result2 = mockMvc.perform(delete("/api/engineers/{id}", id))
                    .andExpect(status().isNoContent());

            } else if (status == HttpStatus.NOT_FOUND.value()) {
                result1.andExpect(status().isNotFound());
            }

            //-- safe post (insert) the entity, since if existed was deleted in previous step
            ResultActions result3 = mockMvc.perform(
                post("/api/engineers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(engineerJson))
                .andExpect(status().isCreated());

            String responseBody = result3.andReturn().getResponse().getContentAsString();
            return objectMapper.readValue(responseBody, Engineer.class);

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.info("(createNewEngineerDeleteIfEmailExists:mockMvc.perform.get.by.email)");
            logger.info(ex.getMessage() != null ? ex.getMessage() : ex.toString());
        }

        return prototypeEngineer;
    }
}
