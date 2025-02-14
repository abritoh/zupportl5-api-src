package org.apache.clusterbr.zupportl5.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/StreamUtil_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
public class StreamUtil {

    private static final Logger logger = LoggerFactory.getLogger(StreamUtil.class);

    
    public static String readInputStreamTextLines(InputStream inputStream) throws IOException {
        try {
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder content = new StringBuilder();            
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            return content.toString();
        } catch(Exception ex) {
            ex.printStackTrace();
            logger.error("[Exception] (readInputStreamTextLines)", ex);
        }
        return "NULL";
    }

    public static String encodeStringToStringBase64(String originalString) {
        return Base64.getEncoder().encodeToString(originalString.getBytes());
    }

    public static String encodeObjectToStringBase64(Object object) throws IOException {

        try ( 
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        ) {

            objectOutputStream.writeObject(object);

            byte[] serializedObject = byteArrayOutputStream.toByteArray();

            return Base64.getEncoder().encodeToString( serializedObject );

        } catch(IOException ex) {
            ex.printStackTrace();
            logger.error("[Exception] (encodeObjectToStringBase64)", ex);
            throw ex;
        }
    }

    public static String decodeStringBase64(String stringEncodedBase64) throws IOException {
        
        byte[] byteArrayDecoded = Base64.getDecoder().decode(stringEncodedBase64);

        return new String(byteArrayDecoded);
    }
    
}

