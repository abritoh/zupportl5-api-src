package org.apache.clusterbr.zupportl5.exception;

import org.apache.clusterbr.zupportl5.service.MessageService;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/RestApiException_class.png" alt="UML CLASS Diagram" class="class"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
public class RestApiException extends RuntimeException {
    
    private static final long serialVersionUID = 1L;

    public RestApiException(String message) {
        super(message);
    }

    public RestApiException(MessageService messageService, String messageKey, java.lang.Object... values) {
        super(String.format(messageService.getMessage(messageKey), (Object[]) values));
    }

}
