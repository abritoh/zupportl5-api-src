package org.apache.clusterbr.zupportl5.utils;

import com.google.cloud.secretmanager.v1.AccessSecretVersionRequest;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretPayload;
import com.google.cloud.secretmanager.v1.SecretVersionName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * See: https://cloud.google.com/security/products/secret-manager?hl=en
 * 
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/GCPSecretManagerUtil_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1123
 * <!-- comment-processor-end -->
 */
public class GCPSecretManagerUtil {

    private static final Logger logger = LoggerFactory.getLogger(GCPSecretManagerUtil.class);

    public static String getSecret(String projectId, String secretId, String versionId) {
        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {

            SecretVersionName secretVersionName = SecretVersionName.of(projectId, secretId, versionId);

            logger.info("(GCPSecretManagerUtil::getSecret) projectId: " + projectId + " secretId:" + secretId + "versionId: " + versionId);

            AccessSecretVersionRequest request = AccessSecretVersionRequest.newBuilder()
                    .setName(secretVersionName.toString())
                    .build();

            SecretPayload payload = client.accessSecretVersion(request).getPayload();
            
            return payload.getData().toStringUtf8();

        } catch (Exception ex) {
            logger.error("(GCPSecretManagerUtil::getSecret()) Failed to access secret: " + secretId, ex);
            return null;
        }
    }

    /**
     * From app.yml:
     * "projects/1097917279253/secrets/DATASOURCE_PLATFORM/versions/latest"
     *  0        1*            2       3*                  4        5*
     */
    public static String getSecret(String secretResourceName) {
        String[] parts = secretResourceName.split("/");
        if (parts.length >= 4) {
            return getSecret(parts[1], parts[3], parts[5]);
        }
        return null;
    }
}

