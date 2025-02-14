package org.apache.clusterbr.zupportl5.component;

import org.apache.clusterbr.zupportl5.dto.MethodResult;
import org.apache.clusterbr.zupportl5.dto.Tuple;
import org.apache.clusterbr.zupportl5.enums.HttpStatusCodeEnum;
import org.apache.clusterbr.zupportl5.service.dropbox.DropboxService;
import org.apache.clusterbr.zupportl5.utils.StreamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/XmlDropboxUploadTasklet_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/XmlDropboxUploadTasklet_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/XmlDropboxUploadTasklet_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/XmlDropboxUploadTasklet_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Component
public class XmlDropboxUploadTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(XmlDropboxUploadTasklet.class);

    private final DropboxService dropboxService;

    @Autowired
    public XmlDropboxUploadTasklet(DropboxService dropboxService) {
        this.dropboxService = dropboxService;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        MethodResult<String> uploadResult = new MethodResult<>();

        ExecutionContext jobContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
        JobParameters jobParameters = chunkContext.getStepContext().getStepExecution().getJobParameters();

        String fileName = jobParameters.getString("fileName");
        String documentEncoded = jobParameters.getString("documentEncoded");
        String documentString = StreamUtil.decodeStringBase64(documentEncoded);

        if (!areStepParameteresNotNull(fileName, documentEncoded)) {
            jobContext.put("uploadResult", uploadResult);
            contribution.setExitStatus(ExitStatus.FAILED);
            return RepeatStatus.FINISHED;
        }

        Tuple<Boolean, String> uploaded = dropboxService.uploadFile(documentString, fileName);

        uploadResult.setSuccess(uploaded.getValue1());
        uploadResult.setCode(uploaded.getValue1() ? HttpStatusCodeEnum.OK : HttpStatusCodeEnum.Conflict);
        uploadResult.setItem(uploaded.getValue2());

        jobContext.put("uploadResult", uploadResult);

        contribution.setExitStatus(uploadResult.getSuccess() ? ExitStatus.COMPLETED : ExitStatus.FAILED);

        return RepeatStatus.FINISHED;
    }

    private boolean areStepParameteresNotNull(String fileName, String documentEncoded) {
        boolean result = true;
        if (fileName == null) {
            result = false;
            logger.debug("[debug] (areStepParameteresNotNull) fileName is null ");
        }
        if (documentEncoded == null) {
            result = false;
            logger.debug("[debug] (areStepParameteresNotNull) documentEncoded is null ");
        }
        return result;
    }

}
