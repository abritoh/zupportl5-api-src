package org.apache.clusterbr.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.clusterbr.zupportl5.config.MessageSourceConfig;
import org.apache.clusterbr.zupportl5.service.MessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * <!-- comment-processor-start -->
 * MessageServiceTest - Unit Test
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
 * <!-- comment-processor-end -->
 * 
 * <p><b>Source code of the Unit Test</b></p>
 * 
 * <pre>{@code
 * @ExtendWith(SpringExtension.class)
 * @ContextConfiguration(classes = { MessageSourceConfig.class, MessageService.class })
 * public class MessageServiceTest {
 *
 *     @Autowired
 *     private MessageService messageService;
 *
 *     @Test
 *     public void testGetMessageDefault() {
 *         String message = messageService.getMessageDefault("message.service.hi.there");
 *         assertNotNull(message);
 *         assertEquals("HI THERE THIS IS VALIDATION ENTRY!", message);
 *     }
 * }
 * 
 *}</pre>
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { MessageSourceConfig.class, MessageService.class })
public class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @Test
    public void testGetMessageDefault() {
        String message = messageService.getMessageDefault("message.service.hi.there");
        assertNotNull(message);
        assertEquals("HI THERE THIS IS VALIDATION ENTRY!", message);
    }
}
