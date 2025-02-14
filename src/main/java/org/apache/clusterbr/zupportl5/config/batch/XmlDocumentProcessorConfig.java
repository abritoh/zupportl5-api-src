package org.apache.clusterbr.zupportl5.config.batch;

import org.apache.clusterbr.zupportl5.component.XmlDBInsertionTasklet;
import org.apache.clusterbr.zupportl5.component.XmlDropboxUploadTasklet;
import org.apache.clusterbr.zupportl5.component.XmlValidationTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/XmlDocumentProcessorConfig_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/XmlDocumentProcessorConfig_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/XmlDocumentProcessorConfig-flow.png" alt="UML Diagram" class="activity"></p> 
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Configuration
public class XmlDocumentProcessorConfig {

    public static final String COMPLETED = "COMPLETED", FAILED = "FAILED";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Autowired
    public XmlDocumentProcessorConfig (
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager) {

        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
    }

    @Bean
    public Job xmlProcessorJob (
        @Qualifier("xmlValidationStep") Step xmlValidationStep,
        @Qualifier("xmlDropboxUploadStep") Step xmlDropboxUploadStep,
        @Qualifier("xmlDBInsertionStep") Step xmlDBInsertionStep) {
        
            return new JobBuilder("xmlProcessorJob", this.jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(xmlValidationStep).on(FAILED).end()
                .from(xmlValidationStep).on(COMPLETED).to(xmlDropboxUploadStep)
                .from(xmlDropboxUploadStep).on(FAILED).to(xmlDBInsertionStep)
                .from(xmlDropboxUploadStep).on(COMPLETED).to(xmlDBInsertionStep)
                .from(xmlDBInsertionStep).on(FAILED).end()
                .end()
                .build()
                ;
    }

    @Bean
    public Step xmlValidationStep(XmlValidationTasklet xmlValidationTasklet) {

        return new StepBuilder("xmlValidationStep", this.jobRepository)
                .tasklet(xmlValidationTasklet, this.transactionManager)
                .build();
    }

    @Bean
    public Step xmlDropboxUploadStep(XmlDropboxUploadTasklet xmlDropboxUploadTasklet) {

        return new StepBuilder("xmlDropboxUploadStep", this.jobRepository)
                .tasklet(xmlDropboxUploadTasklet, this.transactionManager)
                .build();
    }

    @Bean
    public Step xmlDBInsertionStep(XmlDBInsertionTasklet xmlDBInsertionTasklet) {

        return new StepBuilder("xmlDBInsertionStep", this.jobRepository)
                .tasklet(xmlDBInsertionTasklet, this.transactionManager)
                .build();
    }
}
