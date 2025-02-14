package org.apache.clusterbr.zupportl5.service.dropbox;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.clusterbr.zupportl5.dto.SettingsDto;
import org.apache.clusterbr.zupportl5.dto.Tuple;
import org.apache.clusterbr.zupportl5.utils.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;
import com.dropbox.core.v2.files.WriteMode;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DropboxService_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DropboxService_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DropboxService_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/DropboxService_activity.png" alt="UML ACTIVITY Diagram" class="activity"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Service
public class DropboxService {

    private static final Logger logger = LoggerFactory.getLogger(DropboxService.class);

    @Value("${property.application.dropbox.client}")
    private String dropboxClientName;

    private final DropboxTokenService dropboxTokenService;

    @Autowired
    public DropboxService(DropboxTokenService dropboxTokenService) {
        this.dropboxTokenService = dropboxTokenService;
    }

    public DbxClientV2 createDropboxClient() {

        String fmtMsg = "[debug] (DropboxService::createDropboxClient): client-name=%s";
        logger.debug(String.format(fmtMsg, dropboxClientName));

        SettingsDto accessToken = dropboxTokenService.getAccessTokenFromDBOrRequestNew();
        logger.info("(DropboxService::createDropboxClient()) accessToken: ".concat(accessToken.getValue()) );
        
        DbxClientV2 dropboxClient = new DbxClientV2(DbxRequestConfig.newBuilder(dropboxClientName).build(), accessToken.getValue());

        if ( accessToken.getValue().equals(DropboxTokenService.NO_VALUE) ) {
            logger.error("(DropboxService::createDropboxClient) Access token is not available. dropboxClient: ".concat(dropboxClient.toString()) );
        }

        return dropboxClient;
    }

    public List<String> getFileNames(String folderPath) throws Exception {
        List<String> fileNames = new ArrayList<>();

        try {
            DbxClientV2 dropboxClient = this.createDropboxClient();
            ListFolderResult listFolderResult = dropboxClient.files().listFolder(folderPath);
            for (Metadata metadata : listFolderResult.getEntries()) {
                if (metadata instanceof FileMetadata) {
                    fileNames.add(metadata.getName());
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("[Exception] (DropboxService::getFileNames:folderPath)", ex);
        }

        return fileNames;
    }

    public byte[] downloadFile(String folderPath, String fileName) throws Exception {

        String dboxPath = folderPath.concat(AppConstants.DIAG).concat(fileName);
        dboxPath = (dboxPath.startsWith(AppConstants.DIAG)) ? dboxPath : AppConstants.DIAG.concat(dboxPath);

        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            DbxClientV2 dropboxClient = this.createDropboxClient();
            dropboxClient.files().download(dboxPath).download(outputStream);
            return outputStream.toByteArray();
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("[Exception] (DropboxService::downloadFile:folderPath,fileName)", ex);
        }

        return null;

    }

    public List<Tuple<String, Boolean>> uploadFile(MultipartFile[] files) {

        List<Tuple<String, Boolean>> result = new ArrayList<Tuple<String, Boolean>>();

        for (MultipartFile file : files) {
            Tuple<Boolean, String> uploaded = this.uploadFile(file);
            result.add( new Tuple<>(file.getName(), uploaded.getValue1()) );
        }

        return result;
    }

    public Tuple<Boolean, String> uploadFile(MultipartFile file) {

        Tuple<Boolean, String> result = new Tuple<Boolean, String>(Boolean.FALSE, AppConstants.EMPTY);

        try (InputStream inputStream = file.getInputStream()) {

            result = this.uploadFile(inputStream, file.getOriginalFilename());

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("[Exception] (DropboxService::uploadFile:MultipartFile)", ex);
            result.setValue2(getExceptionMessageOrDefault(ex));
        }        
        return result;
    }

    public Tuple<Boolean, String> uploadFile(String fileTextString, String fileName) {
        
        Tuple<Boolean, String> result = new Tuple<Boolean, String>(Boolean.FALSE, AppConstants.EMPTY);

        try (InputStream inputStream = new ByteArrayInputStream(fileTextString.getBytes())) {
            
            result = this.uploadFile(inputStream, fileName);

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("[Exception] (DropboxService::uploadFile:fileTextString)", ex);
            result.setValue2(getExceptionMessageOrDefault(ex));
        }
        return result;
    }

    public Tuple<Boolean, String> uploadFile(InputStream inputStream, String fileName) {

        Tuple<Boolean, String> result = new Tuple<Boolean, String>(Boolean.FALSE, AppConstants.EMPTY);

        try {
            String destFilePath = AppConstants.DIAG.concat(fileName);

            DbxClientV2 dropboxClient = this.createDropboxClient();

            FileMetadata metadata = dropboxClient.files().uploadBuilder(destFilePath)
                    .withMode(WriteMode.OVERWRITE).uploadAndFinish(inputStream);
            
            result.setValue1(true);
            result.setValue2(AppConstants.UPLOADED);

            logger.info("[Info] (DropboxService::uploadFile) File UPLOADED: ".concat(metadata.getPathLower()));

        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("[Exception] (DropboxService::uploadFile:inputStream)", ex);
            result.setValue2(getExceptionMessageOrDefault(ex));
        }

        return result;
    }

    private String getExceptionMessageOrDefault(Exception ex) {
        String msg = (ex.getMessage() != null && !ex.getMessage().isEmpty()) 
                    ? ex.getMessage() 
                    : ex.toString()
                    ;
        msg = msg.contains(AppConstants.DROPBOX_EXCEPTION_INVALID_TOKEN) 
            ? AppConstants.MSG_UPLOAD_INVALID_TOKEN : AppConstants.MSG_UPLOAD_FAILED;
        return msg;
    }

}
