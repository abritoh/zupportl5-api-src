package org.apache.clusterbr.zupportl5.component;

import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.clusterbr.zupportl5.dto.MethodResult;
import org.apache.clusterbr.zupportl5.dto.xml.DocumentXml;
import org.apache.clusterbr.zupportl5.enums.HttpStatusCodeEnum;
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
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Component;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/XmlDBInsertionTasklet_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/XmlDBInsertionTasklet_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/XmlDBInsertionTasklet_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/XmlDBInsertionTasklet_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Component
public class XmlDBInsertionTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(XmlDBInsertionTasklet.class);

    private final DataSource dataSource;

    @Autowired
    public XmlDBInsertionTasklet(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        MethodResult<String> insertResult = new MethodResult<>();

        ExecutionContext jobContext = chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext();
        JobParameters jobParameters = chunkContext.getStepContext().getStepExecution().getJobParameters();

        String fileName = jobParameters.getString("fileName");
        String documentEncoded = jobParameters.getString("documentEncoded");
        String documentString = StreamUtil.decodeStringBase64(documentEncoded);

        if (!areStepParameteresNotNull(fileName, documentEncoded)) {
            jobContext.put("insertResult", insertResult);
            contribution.setExitStatus(ExitStatus.FAILED);
            return RepeatStatus.FINISHED;
        }

        DocumentXml document = DocumentXml.convertFromString(documentString);

        if(document != null) {
            insertResult = insertIntoDatabase(document);
        } else {
            logger.debug("[XmlDBInsertionTasklet] (execute) document is null");
        }

        jobContext.put("insertResult", insertResult);

        logger.debug("[XmlDBInsertionTasklet] (execute) insertResult:");
        logger.debug(insertResult.toString());

        contribution.setExitStatus(insertResult.getSuccess() ? ExitStatus.COMPLETED : ExitStatus.FAILED);

        return RepeatStatus.FINISHED;
    }

    private MethodResult<String> insertIntoDatabase(DocumentXml document) throws Exception {

        MethodResult<String> result = new MethodResult<>();

        SimpleJdbcCall jdbcCallSP = new SimpleJdbcCall(this.dataSource)
                .withProcedureName("sp_insert_document")
                .declareParameters(
                        new SqlParameter("document_type", Types.VARCHAR),
                        new SqlParameter("category", Types.VARCHAR),
                        new SqlParameter("xmlheader", Types.VARCHAR),
                        new SqlParameter("xmlcontent", Types.VARCHAR),
                        new SqlOutParameter("status_code", Types.INTEGER),
                        new SqlOutParameter("status_message", Types.VARCHAR));

        Map<String, Object> sp_in_params = new HashMap<>();
        sp_in_params.put("document_type", document.getDocumentType());
        sp_in_params.put("category", document.getCategory());
        sp_in_params.put("xmlheader", document.getXmlHeader());
        sp_in_params.put("xmlcontent", document.getXmlContent());

        Map<String, Object> outParams = jdbcCallSP.execute(sp_in_params);

        int code = (int) outParams.get("status_code");
        String status_message = (String) outParams.get("status_message");
        HttpStatusCodeEnum status = HttpStatusCodeEnum.fromCode(code);

        result.setCode(code);
        result.setItem(status_message);
        result.setSuccess(status == HttpStatusCodeEnum.OK);

        return result;
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
