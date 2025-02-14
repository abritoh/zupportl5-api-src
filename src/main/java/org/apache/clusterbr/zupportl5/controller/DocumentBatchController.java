package org.apache.clusterbr.zupportl5.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.clusterbr.zupportl5.dto.MethodResult;
import org.apache.clusterbr.zupportl5.utils.StreamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <!-- comment-processor-start -->
 * <p>REST-API Endpoints --</p>
 * <ul>
 * <li>GET /api/batch/process-xmlfiles: Returns data from the API</li>
 * <li>POST /api/batch/process-xmlfiles: Creates a new entry in the API</li>
 * </ul>
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DocumentBatchController_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DocumentBatchController_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DocumentBatchController_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DocumentBatchController_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@RestController
@RequestMapping("/api/batch")
public class DocumentBatchController {
    
    private static final Logger logger = LoggerFactory.getLogger(DocumentBatchController.class);    

    private final JobLauncher jobLauncher;
    private final Job xmlProcessorJob;
    @Autowired
    public DocumentBatchController(
            JobLauncher jobLauncher,
            @Qualifier("xmlProcessorJob") Job xmlProcessorJob) {

        this.jobLauncher = jobLauncher;
        this.xmlProcessorJob = xmlProcessorJob;
    }

    @GetMapping("/process-xmlfiles")
    public ResponseEntity<String> getViewForProcessXml() throws IOException {
        String content;
        
        Resource resource = new ClassPathResource("static/public/view-batch-upload.html");
        try (InputStream inputStream = resource.getInputStream()) {
            content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);

        return new ResponseEntity<>(content, headers, HttpStatus.OK);
    }
    
    @PostMapping("/process-xmlfiles")
    public ResponseEntity<?> processXmlFiles(@RequestParam("files") MultipartFile[] files) {

        List<Map<String, Object>> results = new ArrayList<>();

        for (MultipartFile file : files) {

            try (InputStream xmlInputStream = file.getInputStream()) {
                
                byte[] xmlInputBytes = xmlInputStream.readAllBytes();

                String documentAsString = new String(xmlInputBytes);
                String documentEncoded = StreamUtil.encodeStringToStringBase64(documentAsString);

                JobParameters jobParameters = new JobParametersBuilder()
                        .addLong("startAt", System.currentTimeMillis())
                        .addString("fileName", file.getOriginalFilename())
                        .addString("documentEncoded", documentEncoded)
                        .toJobParameters();

                JobExecution jobExecution = jobLauncher.run(xmlProcessorJob, jobParameters);

                while (jobExecution.isRunning()) Thread.sleep(100);

                ExecutionContext jobContext = jobExecution.getExecutionContext();

                MethodResult<String> validationResult = getMethodResultOrNull(jobContext, "validationResult");
                MethodResult<String> uploadResult = getMethodResultOrNull(jobContext, "uploadResult");
                MethodResult<String> insertResult = getMethodResultOrNull(jobContext, "insertResult");

                results.add(Map.of(
                        "file", file.getOriginalFilename(),
                        "validationResult", validationResult != null ? validationResult : NULL,
                        "uploadResult", uploadResult != null ? uploadResult : NULL,
                        "insertResult", insertResult != null ? insertResult : NULL
                        ));

            } catch (Exception ex) {

                ex.printStackTrace();

                results.add(Map.of(
                        "file", file.getOriginalFilename(),
                        "status", "FAILED",
                        "error", (ex.getMessage() == null) ? NULL : ex.getMessage() ));

                logger.error("[Exception] (processXmlFiles)", ex);
            }
        }

        return ResponseEntity.ok(results);
    }

    @SuppressWarnings("unchecked")
    private MethodResult<String> getMethodResultOrNull(        
        ExecutionContext jobContext, String key) {
        if (jobContext.containsKey(key)) {
            Object result = jobContext.get(key);
            if (result instanceof MethodResult<?>) {
                MethodResult<?> methodResult = (MethodResult<?>) result;
                if (methodResult.getItem() instanceof String) {
                    return (MethodResult<String>) methodResult;
                }
            }
        }
        return null;
    }
    

    private static final String NULL = "NULL";
}
