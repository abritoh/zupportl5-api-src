package org.apache.clusterbr.zupportl5.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <!-- comment-processor-start -->
 *  
 * <p><b>UML Diagrams:</b></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/TrailingSlashInterceptor_class.png" alt="UML CLASS Diagram" class="class"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/TrailingSlashInterceptor_usecase.png" alt="UML USECASE Diagram" class="usecase"></p>
 * <p><img src="{@docRoot}/generated-resources/uml/images/TrailingSlashInterceptor_seq.png" alt="UML SEQ Diagram" class="seq"></p>
 *  
 * @author <a href='mailto:arcbrth@gmail.com'>arcbrth@gmail.com</a>
 * @since 2024-1108
* <!-- comment-processor-end -->
 */
@Component
public class TrailingSlashInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        String requestURI = request.getRequestURI();

        /* if the URI ends with a slash and is not the root context, redirect to the same URI without the slash */
        if (requestURI.endsWith("/") && requestURI.length() > 1) {
            String newURI = UriComponentsBuilder.fromUriString(requestURI)
                    .replacePath(requestURI.substring(0, requestURI.length() - 1))
                    .build()
                    .toUriString();

            response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
            response.setHeader("Location", newURI);
            return false;
        }
        
        return true;
    }
}
