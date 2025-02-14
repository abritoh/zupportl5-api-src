package org.apache.clusterbr.zupportl5.utils;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/AppLangUtil_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
public class AppLangUtil {

    public static String sanitizeText(String input) {
        if (input == null) return AppConstants.EMPTY;
        return input.replaceAll("[^a-zA-Z0-9_]", AppConstants.EMPTY);            
    }

    /**
     * Converts from instant in milliseconds to String-date, format:'yyyy-MM-dd HH:mm:s'
     * @param instantMillis
     * @return String-date or null if instantMillis is null
     */
    public static String convertInstantToDateString(Long instantMillis) {

        if (instantMillis == null) return null;

        Instant instant = Instant.ofEpochMilli(instantMillis);

        ZonedDateTime equivalentDateTime = instant.atZone(ZoneId.systemDefault());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return equivalentDateTime.format(formatter);
    }

    /**
     * converts a string from "yyyy-MM-dd HH-mm-ss" to Timestamp
     * @param dateTimeString
     * @return a Timestamp instance
     */
    public static Timestamp convertToTimestamp(String dateTimeString) {
        
        try {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
            LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, formatter);            
            return Timestamp.valueOf(localDateTime);

        } catch(Exception ex) {
            /*DO-NOTHING*/
        }

        return Timestamp.from(Instant.now());
    }
    
}
